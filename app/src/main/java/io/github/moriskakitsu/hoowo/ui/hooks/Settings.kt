package io.github.moriskakitsu.hoowo.ui.hooks

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.moriskakitsu.hoowo.data.datastore.Settings
import io.github.moriskakitsu.hoowo.data.datastore.SettingsStore
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import org.koin.compose.koinInject

@Composable
fun rememberUserSettingsState(): State<Settings> {
    val store = koinInject<SettingsStore>()
    return store.settingsFlow.collectAsStateWithLifecycle(
        initialValue = Settings.dummy(),
    )
}

/**
 * 观察"开发者选项"开关状态。
 *
 * 默认返回 false（即初始值为关闭），避免设置尚未从 DataStore 载入时闪现高级入口。
 * 完整设置请使用 [rememberUserSettingsState]。
 */
@Composable
fun rememberDeveloperMode(): State<Boolean> {
    val store = koinInject<SettingsStore>()
    return store.settingsFlow
        .map { it.developerMode }
        .distinctUntilChanged()
        .collectAsStateWithLifecycle(initialValue = false)
}
