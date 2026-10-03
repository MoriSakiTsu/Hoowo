package io.github.moriskakitsu.ai.provider.providers.openai

import kotlinx.coroutines.flow.Flow
import io.github.moriskakitsu.ai.provider.ProviderSetting
import io.github.moriskakitsu.ai.provider.TextGenerationResult
import io.github.moriskakitsu.ai.provider.TextGenerationParams
import io.github.moriskakitsu.ai.ui.StreamChunk
import io.github.moriskakitsu.ai.ui.UIMessage

interface OpenAIImpl {
    suspend fun generateText(
        providerSetting: ProviderSetting.OpenAI,
        messages: List<UIMessage>,
        params: TextGenerationParams,
    ): TextGenerationResult

    suspend fun streamText(
        providerSetting: ProviderSetting.OpenAI,
        messages: List<UIMessage>,
        params: TextGenerationParams,
    ): Flow<StreamChunk>
}
