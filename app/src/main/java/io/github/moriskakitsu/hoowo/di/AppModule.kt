package io.github.moriskakitsu.hoowo.di

import kotlinx.serialization.json.Json
import io.github.moriskakitsu.hoowo.AppScope
import io.github.moriskakitsu.hoowo.data.ai.tools.local.LocalTools
import io.github.moriskakitsu.hoowo.data.ai.tools.ChatToolFactory
import io.github.moriskakitsu.hoowo.data.event.AppEventBus
import io.github.moriskakitsu.hoowo.service.ChatNotificationManager
import io.github.moriskakitsu.hoowo.service.ChatService
import io.github.moriskakitsu.hoowo.ui.pages.extensions.workspace.WorkspaceTerminalSessionManager
import io.github.moriskakitsu.hoowo.utils.EmojiData
import io.github.moriskakitsu.hoowo.utils.EmojiUtils
import io.github.moriskakitsu.hoowo.utils.JsonInstant
import io.github.moriskakitsu.hoowo.utils.SoundEffectPlayer
import io.github.moriskakitsu.hoowo.utils.UpdateChecker
import io.github.moriskakitsu.hoowo.web.WebServerManager
import io.github.moriskakitsu.tts.provider.TTSManager
import org.koin.dsl.module

val appModule = module {
    single<Json> { JsonInstant }

    single {
        AppEventBus()
    }

    single {
        LocalTools(get(), get(), get(), get())
    }

    single {
        UpdateChecker(
            client = get(),
            appScope = get(),
        )
    }

    single {
        AppScope()
    }

    single<EmojiData> {
        EmojiUtils.loadEmoji(get())
    }

    single {
        TTSManager(get())
    }

    single {
        SoundEffectPlayer(get())
    }

    single {
        WorkspaceTerminalSessionManager(get(), get())
    }

    // 生成通知与业务解耦：ChatService 只发事件，通知由这里消费；
    // createdAtStart 保证进程启动即订阅，否则后台生成的事件会因无订阅者而丢失
    single(createdAtStart = true) {
        ChatNotificationManager(
            context = get(),
            appScope = get(),
            eventBus = get(),
            settingsStore = get(),
        )
    }

    single {
        ChatToolFactory(
            json = get(),
            memoryRepository = get(),
            conversationRepository = get(),
            localTools = get(),
            mcpManager = get(),
            skillManager = get(),
            workspaceRepository = get(),
        )
    }

    single {
        ChatService(
            context = get(),
            appScope = get(),
            appEventBus = get(),
            settingsStore = get(),
            conversationRepo = get(),
            memoryRepository = get(),
            generationLoop = get(),
            translationHandler = get(),
            templateTransformer = get(),
            providerManager = get(),
            chatToolFactory = get(),
            mcpManager = get(),
            filesManager = get(),
            workspaceRepository = get(),
            folderRepository = get()
        )
    }

    single {
        WebServerManager(
            context = get(),
            appScope = get(),
            chatService = get(),
            conversationRepo = get(),
            folderRepo = get(),
            settingsStore = get(),
            filesManager = get()
        )
    }
}
