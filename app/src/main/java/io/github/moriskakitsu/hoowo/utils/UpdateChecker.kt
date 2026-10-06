package io.github.moriskakitsu.hoowo.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import io.github.moriskakitsu.common.http.await
import io.github.moriskakitsu.hoowo.AppScope
import io.github.moriskakitsu.hoowo.Hoowo
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.concurrent.TimeUnit

// GitHub Releases 直连可能失败(部分网络环境下不可达), 超时设短一些, 失败后如实提示
private const val CONNECT_TIMEOUT_SECONDS = 8L
private const val READ_TIMEOUT_SECONDS = 8L

class UpdateChecker(
    private val client: OkHttpClient,
    appScope: AppScope,
) {
    private val json = Json { ignoreUnknownKeys = true }

    // 更新检查只需要读取 release 信息, 单独用一个短超时的客户端, 避免长时间转圈
    private val updateClient: OkHttpClient by lazy {
        client.newBuilder()
            .connectTimeout(CONNECT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(READ_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .build()
    }

    val updateState: StateFlow<UiState<UpdateInfo>> = checkUpdate().stateIn(
        scope = appScope,
        started = SharingStarted.Lazily,
        initialValue = UiState.Loading,
    )

    /**
     * 从 GitHub Releases 读取最新版本。
     *
     * 只做"有新版本"的提示, 不在应用内下载安装包; 需要更新时引导用户前往 Releases 页面。
     * 网络不可达时抛出异常, 由 UI 显示"检查更新失败", 不做静默降级。
     */
    private fun checkUpdate(): Flow<UiState<UpdateInfo>> = flow {
        emit(UiState.Loading)
        val request = Request.Builder()
            .url("https://api.github.com/repos/${Hoowo.REPO_OWNER}/${Hoowo.REPO_NAME}/releases/latest")
            .header("Accept", "application/vnd.github+json")
            .build()
        val response = updateClient.newCall(request).await()
        if (!response.isSuccessful) {
            throw IllegalStateException("GitHub Releases responded with HTTP ${response.code}")
        }
        val body = response.body?.string()
            ?: throw IllegalStateException("Empty response from GitHub Releases")
        val release = json.decodeFromString<GitHubRelease>(body)
        emit(
            UiState.Success(
                data = UpdateInfo(
                    // GitHub 的 tag 通常带 v 前缀, 版本比较只认数字部分
                    version = release.tagName.removePrefix("v").removePrefix("V"),
                    publishedAt = release.publishedAt.orEmpty(),
                    changelog = release.body.orEmpty(),
                )
            )
        )
    }.flowOn(Dispatchers.IO)
        .catch { throwable ->
            emit(UiState.Error(throwable))
        }
}

/** GitHub Releases API 中我们关心的字段, 其余字段忽略 */
@Serializable
private data class GitHubRelease(
    @SerialName("tag_name") val tagName: String,
    @SerialName("published_at") val publishedAt: String? = null,
    @SerialName("body") val body: String? = null,
)

@Serializable
data class UpdateInfo(
    val version: String,
    val publishedAt: String,
    val changelog: String,
)

/**
 * 版本号值类，封装版本号字符串并提供比较功能
 *
 * 支持完整的 SemVer 规范：MAJOR.MINOR.PATCH[-prerelease][+build]
 * - 预发布版本优先级低于正式版：1.0.0-alpha < 1.0.0
 * - 预发布标识符按段逐个比较：数字按数值比较，字符串按字典序比较
 * - 预发布标识符优先级：alpha < beta < rc（通过字典序自然满足）
 * - build metadata（+号后面的部分）不影响优先级比较
 */
@JvmInline
value class Version(val value: String) : Comparable<Version> {

    private fun parse(): ParsedVersion {
        // 去掉 build metadata（+号后面的部分）
        val withoutBuild = value.split("+").first()
        // 分离主版本号和预发布标识符
        val hyphenIndex = withoutBuild.indexOf('-')
        val (coreStr, prereleaseStr) = if (hyphenIndex >= 0) {
            withoutBuild.substring(0, hyphenIndex) to withoutBuild.substring(hyphenIndex + 1)
        } else {
            withoutBuild to null
        }
        val core = coreStr.split(".").map { it.toIntOrNull() ?: 0 }
        val prerelease = prereleaseStr?.split(".")
        return ParsedVersion(core, prerelease)
    }

    override fun compareTo(other: Version): Int {
        val a = this.parse()
        val b = other.parse()

        // 先比较主版本号
        val maxLen = maxOf(a.core.size, b.core.size)
        for (i in 0 until maxLen) {
            val ap = if (i < a.core.size) a.core[i] else 0
            val bp = if (i < b.core.size) b.core[i] else 0
            if (ap != bp) return ap.compareTo(bp)
        }

        // 主版本号相同时比较预发布标识符
        // 有预发布标识符的版本优先级低于没有的：1.0.0-alpha < 1.0.0
        return when {
            a.prerelease == null && b.prerelease == null -> 0
            a.prerelease != null && b.prerelease == null -> -1
            a.prerelease == null && b.prerelease != null -> 1
            else -> comparePrerelease(a.prerelease!!, b.prerelease!!)
        }
    }

    companion object {
        fun compare(version1: String, version2: String): Int {
            return Version(version1).compareTo(Version(version2))
        }

        private fun comparePrerelease(a: List<String>, b: List<String>): Int {
            val maxLen = maxOf(a.size, b.size)
            for (i in 0 until maxLen) {
                // 字段少的优先级更低：1.0.0-alpha < 1.0.0-alpha.1
                if (i >= a.size) return -1
                if (i >= b.size) return 1

                val aNum = a[i].toIntOrNull()
                val bNum = b[i].toIntOrNull()

                val cmp = when {
                    // 都是字：按数值比较
                    aNum != null && bNum != null -> aNum.compareTo(bNum)
                    // 数字优先级低于字符串
                    aNum != null -> -1
                    bNum != null -> 1
                    // 都是字符串：按字典序比较
                    else -> a[i].compareTo(b[i])
                }
                if (cmp != 0) return cmp
            }
            return 0
        }
    }
}

private data class ParsedVersion(
    val core: List<Int>,
    val prerelease: List<String>?,
)

// 扩展操作符函数，使比较更直观
operator fun String.compareTo(other: Version): Int = Version(this).compareTo(other)
operator fun Version.compareTo(other: String): Int = this.compareTo(Version(other))
