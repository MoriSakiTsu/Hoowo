package io.github.moriskakitsu.hoowo.ui.context

import androidx.compose.runtime.compositionLocalOf
import io.github.moriskakitsu.hoowo.ui.hooks.CustomAsrState

val LocalASRState = compositionLocalOf<CustomAsrState> { error("Not provided yet") }

