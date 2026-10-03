package io.github.moriskakitsu.hoowo.ui.hooks

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.moriskakitsu.hoowo.data.datastore.Settings
import io.github.moriskakitsu.hoowo.data.datastore.SettingsStore
import org.koin.compose.koinInject

@Composable
fun rememberUserSettingsState(): State<Settings> {
    val store = koinInject<SettingsStore>()
    return store.settingsFlow.collectAsStateWithLifecycle(
        initialValue = Settings.dummy(),
    )
}
