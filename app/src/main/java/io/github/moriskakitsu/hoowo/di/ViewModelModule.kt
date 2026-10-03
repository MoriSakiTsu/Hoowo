package io.github.moriskakitsu.hoowo.di

import io.github.moriskakitsu.hoowo.ui.pages.assistant.AssistantVM
import io.github.moriskakitsu.hoowo.ui.pages.assistant.detail.AssistantDetailVM
import io.github.moriskakitsu.hoowo.ui.pages.backup.BackupVM
import io.github.moriskakitsu.hoowo.ui.pages.chat.ChatDrawerVM
import io.github.moriskakitsu.hoowo.ui.pages.chat.ChatVM
import io.github.moriskakitsu.hoowo.ui.pages.debug.DebugVM
import io.github.moriskakitsu.hoowo.ui.pages.favorite.FavoriteVM
import io.github.moriskakitsu.hoowo.ui.pages.search.SearchVM
import io.github.moriskakitsu.hoowo.ui.pages.history.HistoryVM
import io.github.moriskakitsu.hoowo.ui.pages.stats.StatsVM
import io.github.moriskakitsu.hoowo.ui.pages.imggen.ImgGenVM
import io.github.moriskakitsu.hoowo.ui.pages.extensions.PromptVM
import io.github.moriskakitsu.hoowo.ui.pages.extensions.QuickMessagesVM
import io.github.moriskakitsu.hoowo.ui.pages.extensions.skills.SkillDetailVM
import io.github.moriskakitsu.hoowo.ui.pages.extensions.skills.SkillsVM
import io.github.moriskakitsu.hoowo.ui.pages.extensions.workspace.WorkspaceDetailVM
import io.github.moriskakitsu.hoowo.ui.pages.extensions.workspace.WorkspaceVM
import io.github.moriskakitsu.hoowo.ui.pages.setting.SettingVM
import io.github.moriskakitsu.hoowo.ui.pages.share.handler.ShareHandlerVM
import io.github.moriskakitsu.hoowo.ui.pages.translator.TranslatorVM
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModel<ChatVM> { params ->
        ChatVM(
            id = params.get(),
            context = get(),
            settingsStore = get(),
            conversationRepo = get(),
            chatService = get(),
            updateChecker = get(),
            filesManager = get(),
            favoriteRepository = get(),
        )
    }
    viewModelOf(::ChatDrawerVM)
    viewModelOf(::SettingVM)
    viewModelOf(::DebugVM)
    viewModelOf(::HistoryVM)
    viewModelOf(::AssistantVM)
    viewModel<AssistantDetailVM> {
        AssistantDetailVM(
            id = it.get(),
            settingsStore = get(),
            memoryRepository = get(),
            filesManager = get(),
            skillManager = get(),
            workspaceRepository = get(),
        )
    }
    viewModelOf(::TranslatorVM)
    viewModel<ShareHandlerVM> {
        ShareHandlerVM(
            text = it.get(),
            settingsStore = get(),
        )
    }
    viewModelOf(::BackupVM)
    viewModelOf(::ImgGenVM)
    viewModelOf(::PromptVM)
    viewModelOf(::QuickMessagesVM)
    viewModelOf(::SkillsVM)
    viewModelOf(::SkillDetailVM)
    viewModelOf(::WorkspaceVM)
    viewModel<WorkspaceDetailVM> {
        WorkspaceDetailVM(
            id = it.get(),
            repository = get(),
            terminalSessionManager = get(),
        )
    }
    viewModelOf(::FavoriteVM)
    viewModelOf(::SearchVM)
    viewModelOf(::StatsVM)
}
