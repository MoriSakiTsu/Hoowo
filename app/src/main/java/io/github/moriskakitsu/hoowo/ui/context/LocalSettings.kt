package io.github.moriskakitsu.hoowo.ui.context

import androidx.compose.runtime.staticCompositionLocalOf
import io.github.moriskakitsu.hoowo.data.datastore.Settings

val LocalSettings = staticCompositionLocalOf<Settings> {
    error("No SettingsStore provided")
}
