package io.github.moriskakitsu.hoowo.di

import android.content.Context
import io.github.moriskakitsu.hoowo.data.files.FileFolders
import io.github.moriskakitsu.hoowo.data.files.FilesManager
import io.github.moriskakitsu.hoowo.data.files.SkillManager
import io.github.moriskakitsu.hoowo.data.repository.ConversationRepository
import io.github.moriskakitsu.hoowo.data.repository.FavoriteRepository
import io.github.moriskakitsu.hoowo.data.repository.FolderRepository
import io.github.moriskakitsu.hoowo.data.repository.FilesRepository
import io.github.moriskakitsu.hoowo.data.repository.GenMediaRepository
import io.github.moriskakitsu.hoowo.data.repository.MemoryRepository
import io.github.moriskakitsu.hoowo.data.repository.WorkspaceRepository
import io.github.moriskakitsu.workspace.ProotShellRunner
import io.github.moriskakitsu.workspace.RootfsInstaller
import io.github.moriskakitsu.workspace.WorkspaceBindMount
import io.github.moriskakitsu.workspace.WorkspaceManager
import org.koin.dsl.module
import java.io.File

val repositoryModule = module {
    single {
        ConversationRepository(get(), get(), get(), get(), get(), get())
    }

    single {
        FolderRepository(get(), get())
    }

    single {
        MemoryRepository(get())
    }

    single {
        GenMediaRepository(get())
    }

    single {
        FilesRepository(get())
    }

    single {
        FavoriteRepository(get())
    }

    single {
        val context: Context = get()
        WorkspaceManager(
            baseDir = File(context.filesDir, "workspaces"),
            shellRunner = ProotShellRunner(
                nativeLibraryDir = File(context.applicationInfo.nativeLibraryDir),
            ),
            // 同一份挂载表既用于 PRoot 的 -b 参数, 也用于文件工具的路径解析, 避免两处漂移
            bindMounts = listOf(
                WorkspaceBindMount(
                    source = File(context.filesDir, FileFolders.SKILLS).apply { mkdirs() },
                    target = "/skills",
                ),
                WorkspaceBindMount(
                    source = File(context.filesDir, FileFolders.BUILTIN_SKILLS).apply { mkdirs() },
                    target = "/builtin_skills",
                ),
                WorkspaceBindMount(
                    source = File(context.filesDir, FileFolders.TOOL_OUTPUTS).apply { mkdirs() },
                    target = "/tool_outputs",
                ),
                WorkspaceBindMount(
                    source = File(context.filesDir, FileFolders.UPLOAD).apply { mkdirs() },
                    target = "/upload",
                ),
            ),
        )
    }

    single {
        RootfsInstaller(get())
    }

    single {
        WorkspaceRepository(get(), get(), get(), get())
    }

    single {
        FilesManager(get(), get(), get())
    }

    single {
        SkillManager(get(), get())
    }
}
