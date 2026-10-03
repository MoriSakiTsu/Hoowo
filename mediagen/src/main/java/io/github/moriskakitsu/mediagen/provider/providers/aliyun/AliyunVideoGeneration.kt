package io.github.moriskakitsu.mediagen.provider.providers.aliyun

import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonArray
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import io.github.moriskakitsu.mediagen.model.ImageRole
import io.github.moriskakitsu.mediagen.model.MediaGenerationError
import io.github.moriskakitsu.mediagen.model.MediaGenerationInput
import io.github.moriskakitsu.mediagen.model.MediaGenerationModel
import io.github.moriskakitsu.mediagen.model.MediaGenerationOutput
import io.github.moriskakitsu.mediagen.model.MediaGenerationRequest
import io.github.moriskakitsu.mediagen.model.MediaGenerationStatus
import io.github.moriskakitsu.mediagen.model.MediaGenerationTask
import io.github.moriskakitsu.mediagen.model.MediaGenerationUsage
import io.github.moriskakitsu.mediagen.provider.MediaGenerationProviderSetting
import io.github.moriskakitsu.mediagen.provider.providers.double
import io.github.moriskakitsu.mediagen.provider.providers.executeJson
import io.github.moriskakitsu.mediagen.provider.providers.obj
import io.github.moriskakitsu.mediagen.provider.providers.postJson
import io.github.moriskakitsu.mediagen.provider.providers.string
import okhttp3.OkHttpClient
import okhttp3.Request

internal class AliyunVideoGeneration(
    private val client: OkHttpClient,
) {
    private val id = AliyunMediaGenerationProvider.PROVIDER_ID

    suspend fun create(
        setting: MediaGenerationProviderSetting.Aliyun,
        model: MediaGenerationModel,
        request: MediaGenerationRequest,
    ): MediaGenerationTask {
        val body = buildCreateBody(model, request)
        val httpRequest = authorizedRequest(
            setting,
            "${setting.resolvedBaseUrl()}/services/aigc/video-generation/video-synthesis",
        ).addHeader("X-DashScope-Async", "enable")
            .postJson(body)
            .build()
        return parseTask(client.executeJson(httpRequest, id), model.modelId)
    }

    suspend fun query(
        setting: MediaGenerationProviderSetting.Aliyun,
        model: MediaGenerationModel,
        taskId: String,
    ): MediaGenerationTask {
        val httpRequest = authorizedRequest(
            setting,
            "${setting.resolvedBaseUrl()}/tasks/$taskId",
        ).get().build()
        return parseTask(client.executeJson(httpRequest, id), model.modelId)
    }

    internal fun buildCreateBody(
        model: MediaGenerationModel,
        request: MediaGenerationRequest,
    ): JsonObject = buildJsonObject {
        put("model", model.modelId)
        put("input", buildJsonObject {
            request.prompt?.takeIf(String::isNotBlank)?.let { put("prompt", it) }
            if (request.inputs.isNotEmpty()) {
                put("media", buildJsonArray {
                    request.inputs.forEach { input ->
                        add(buildJsonObject {
                            input.extra.forEach { (key, value) -> put(key, value) }
                            when (input) {
                                is MediaGenerationInput.Image -> {
                                    put(
                                        "type", when (input.role) {
                                            ImageRole.FIRST_FRAME -> "first_frame"
                                            ImageRole.LAST_FRAME -> "last_frame"
                                            ImageRole.REFERENCE -> "reference_image"
                                        }
                                    )
                                    put("url", input.url)
                                }

                                is MediaGenerationInput.Video -> {
                                    put("type", "reference_video")
                                    put("url", input.url)
                                }

                                is MediaGenerationInput.Audio -> {
                                    put("type", "reference_audio")
                                    put("url", input.url)
                                }

                                is MediaGenerationInput.Document -> {
                                    put("type", "file")
                                    put("url", input.url)
                                }

                                is MediaGenerationInput.WebPage -> {
                                    put("type", "link")
                                    put("url", input.url)
                                }

                                is MediaGenerationInput.Raw ->
                                    input.value.forEach { (key, value) -> put(key, value) }
                            }
                        })
                    }
                })
            }
        })
        put("parameters", buildJsonObject {
            request.extraParameters.forEach { (key, value) -> put(key, value) }
            request.resolution?.let { put("resolution", it) }
            request.aspectRatio?.let { put("ratio", it) }
            request.durationSeconds?.let { put("duration", it) }
            request.generateAudio?.let { put("audio", it) }
            request.watermark?.let { put("watermark", it) }
            request.seed?.let { put("seed", it) }
            request.promptEnhancement?.let { put("prompt_extend", it) }
        })
        require(request.callbackUrl == null) {
            "Aliyun uses account-level asynchronous callbacks instead of a callback_url request field"
        }
    }

    internal fun parseTask(root: JsonObject, fallbackModel: String? = null): MediaGenerationTask {
        val output = root.obj("output") ?: root
        val usageObject = root.obj("usage")
        val videoUrl = output.string("video_url")
        val errorMessage = output.string("message") ?: root.string("message")
        return MediaGenerationTask(
            id = output.string("task_id") ?: error("Aliyun response does not contain task_id"),
            provider = id,
            model = output.string("model") ?: fallbackModel,
            status = mapStatus(output.string("task_status")),
            outputs = videoUrl?.let {
                listOf(
                    MediaGenerationOutput(
                        url = it,
                        mimeType = "video/mp4",
                        durationSeconds = usageObject?.double("output_video_duration")
                            ?: usageObject?.double("duration"),
                        resolution = usageObject?.string("SR"),
                        aspectRatio = usageObject?.string("ratio"),
                    )
                )
            }.orEmpty(),
            error = errorMessage?.let { MediaGenerationError(root.string("code"), it) },
            usage = usageObject?.let {
                MediaGenerationUsage(
                    inputSeconds = it.double("input_video_duration"),
                    outputSeconds = it.double("output_video_duration"),
                    totalSeconds = it.double("duration"),
                )
            },
            metadata = root,
        )
    }

    private fun authorizedRequest(
        setting: MediaGenerationProviderSetting.Aliyun,
        url: String,
    ): Request.Builder = Request.Builder()
        .url(url)
        .addHeader("Authorization", "Bearer ${setting.apiKey}")
        .addHeader("Content-Type", "application/json")

    private fun mapStatus(status: String?): MediaGenerationStatus = when (status?.uppercase()) {
        "PENDING" -> MediaGenerationStatus.QUEUED
        "RUNNING" -> MediaGenerationStatus.RUNNING
        "SUCCEEDED" -> MediaGenerationStatus.SUCCEEDED
        "FAILED" -> MediaGenerationStatus.FAILED
        "CANCELED", "CANCELLED" -> MediaGenerationStatus.CANCELLED
        else -> MediaGenerationStatus.UNKNOWN
    }
}
