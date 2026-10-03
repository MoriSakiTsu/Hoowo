package io.github.moriskakitsu.tts.controller

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import io.github.moriskakitsu.tts.model.AudioChunk
import io.github.moriskakitsu.tts.model.AudioFormat
import io.github.moriskakitsu.tts.model.TTSRequest
import io.github.moriskakitsu.tts.model.TTSResponse
import io.github.moriskakitsu.tts.provider.TTSManager
import io.github.moriskakitsu.tts.provider.TTSProviderSetting
import java.io.ByteArrayOutputStream

/**
 * Bridge TTS provider flow to a single audio buffer.
 */
class TtsSynthesizer(
    private val ttsManager: TTSManager
) {
    suspend fun synthesize(
        setting: TTSProviderSetting,
        chunk: TtsChunk
    ): TTSResponse = withContext(Dispatchers.IO) {
        collectToResponse(
            ttsManager.generateSpeech(setting, TTSRequest(text = chunk.text))
        )
    }

    private suspend fun collectToResponse(flow: Flow<AudioChunk>): TTSResponse {
        var format: AudioFormat? = null
        var sampleRate: Int? = null
        val output = ByteArrayOutputStream()
        flow.collect { chunk ->
            if (format == null) format = chunk.format
            if (sampleRate == null) sampleRate = chunk.sampleRate
            output.write(chunk.data)
        }
        return TTSResponse(
            audioData = output.toByteArray(),
            format = format ?: AudioFormat.MP3,
            sampleRate = sampleRate
        )
    }
}

