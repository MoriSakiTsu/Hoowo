package io.github.moriskakitsu.hoowo.ui.hooks

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import io.github.moriskakitsu.hoowo.utils.PlayStoreUtil

@Composable
fun rememberIsPlayStoreVersion(): Boolean {
    val context = LocalContext.current
    return remember {
        PlayStoreUtil.isInstalledFromPlayStore(context)
    }
}