package io.github.moriskakitsu.search

import android.util.Log
import androidx.compose.runtime.Composable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.put
import io.github.moriskakitsu.ai.core.InputSchema
import io.github.moriskakitsu.search.SearchResult.SearchResultItem
import io.github.moriskakitsu.search.SearchService.Companion.httpClient
import io.github.moriskakitsu.search.SearchService.Companion.json
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

private const val TAG = "HoowoSearchService"

object HoowoSearchService : SearchService<SearchServiceOptions.HoowoOptions> {
    override val name: String = "Hoowo"

    @Composable
    override fun Description() {
        // 该服务依赖的原作者搜索后端已不可用，UI 中保留选项但明确提示不可用；
        // 后续做功能精简时可整体移除该选项。
        androidx.compose.material3.Text("Service unavailable: the original backend has been shut down.")
    }

    override fun parameters(options: SearchServiceOptions.HoowoOptions): InputSchema? =
        InputSchema.Obj(
            properties = buildJsonObject {
                put("query", buildJsonObject {
                    put("type", "string")
                    put("description", "search keyword")
                })
            },
            required = listOf("query")
        )

    override fun scrapingParameters(options: SearchServiceOptions.HoowoOptions): InputSchema? =
        null

    override suspend fun search(
        params: JsonObject,
        commonOptions: SearchCommonOptions,
        serviceOptions: SearchServiceOptions.HoowoOptions
    ): Result<SearchResult> = withContext(Dispatchers.IO) {
        runCatching {
            val query = params["query"]?.jsonPrimitive?.content ?: error("query is required")
            val body = buildJsonObject {
                put("q", JsonPrimitive(query))
                put("depth", JsonPrimitive(serviceOptions.depth))
                put("outputType", JsonPrimitive("sourcedAnswer"))
                put("includeImages", JsonPrimitive("false"))
            }

            val request = Request.Builder()
                .url("https://api.example.com/v1/search") // 原服务已停用，占位地址
                .post(body.toString().toRequestBody())
                .addHeader("Authorization", "Bearer ${serviceOptions.apiKey}")
                .addHeader("Content-Type", "application/json")
                .build()

            Log.i(TAG, "search: $query")

            val response = httpClient.newCall(request).await()
            if (response.isSuccessful) {
                val responseBody = response.body.string().let {
                    json.decodeFromString<HoowoSearchResponse>(it)
                }

                return@withContext Result.success(
                    SearchResult(
                        answer = responseBody.answer,
                        items = responseBody.sources.take(commonOptions.resultSize).map {
                            SearchResultItem(
                                title = it.name,
                                url = it.url,
                                text = it.snippet
                            )
                        }
                    )
                )
            } else {
                error("response failed #${response.code}: ${response.body?.string()}")
            }
        }
    }

    override suspend fun scrape(
        params: JsonObject,
        commonOptions: SearchCommonOptions,
        serviceOptions: SearchServiceOptions.HoowoOptions
    ): Result<ScrapedResult> {
        error("Hoowo does not support scraping")
    }

    @Serializable
    data class HoowoSearchResponse(
        val answer: String,
        val sources: List<Source>
    )

    @Serializable
    data class Source(
        val name: String,
        val url: String,
        val snippet: String
    )
}
