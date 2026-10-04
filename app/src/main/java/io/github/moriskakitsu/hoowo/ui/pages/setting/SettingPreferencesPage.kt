package io.github.moriskakitsu.hoowo.ui.pages.setting

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeFlexibleTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import me.rerere.hugeicons.HugeIcons
import me.rerere.hugeicons.stroke.Notification01
import me.rerere.hugeicons.stroke.Internet
import me.rerere.hugeicons.stroke.PaintBoard
import me.rerere.hugeicons.stroke.Settings03
import me.rerere.hugeicons.stroke.Sun01
import io.github.moriskakitsu.hoowo.R
import io.github.moriskakitsu.hoowo.Screen
import io.github.moriskakitsu.hoowo.ui.components.nav.BackButton
import io.github.moriskakitsu.hoowo.ui.components.ui.CardGroup
import io.github.moriskakitsu.hoowo.ui.context.LocalNavController
import io.github.moriskakitsu.hoowo.ui.hooks.rememberDeveloperMode
import io.github.moriskakitsu.hoowo.ui.theme.CustomColors
import io.github.moriskakitsu.hoowo.utils.plus

@Composable
fun SettingPreferencesPage() {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val navController = LocalNavController.current
    val developerMode by rememberDeveloperMode()

    Scaffold(
        topBar = {
            LargeFlexibleTopAppBar(
                title = {
                    Text(stringResource(R.string.setting_page_preferences))
                },
                navigationIcon = {
                    BackButton()
                },
                scrollBehavior = scrollBehavior,
                colors = CustomColors.topBarColors
            )
        },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        containerColor = CustomColors.topBarColors.containerColor
    ) { contentPadding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = contentPadding + PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                CardGroup(
                    modifier = Modifier.padding(horizontal = 8.dp),
                ) {
                    item(
                        onClick = { navController.navigate(Screen.SettingPreferencesTheme) },
                        leadingContent = { Icon(HugeIcons.Sun01, null) },
                        headlineContent = { Text(stringResource(R.string.setting_page_preferences_theme)) },
                        supportingContent = { Text(stringResource(R.string.setting_page_preferences_theme_desc)) },
                    )
                    item(
                        onClick = { navController.navigate(Screen.SettingPreferencesGeneral) },
                        leadingContent = { Icon(HugeIcons.Settings03, null) },
                        headlineContent = { Text(stringResource(R.string.setting_page_preferences_general)) },
                        supportingContent = { Text(stringResource(R.string.setting_page_preferences_general_desc)) },
                    )
                    item(
                        onClick = { navController.navigate(Screen.SettingPreferencesUI) },
                        leadingContent = { Icon(HugeIcons.PaintBoard, null) },
                        headlineContent = { Text(stringResource(R.string.setting_page_preferences_ui)) },
                        supportingContent = { Text(stringResource(R.string.setting_page_preferences_ui_desc)) },
                    )
                    // 通知提醒与网络请求属于高级设置, 仅在开发者选项开启时显示
                    if (developerMode) {
                        item(
                            onClick = { navController.navigate(Screen.SettingPreferencesNotification) },
                            leadingContent = { Icon(HugeIcons.Notification01, null) },
                            headlineContent = { Text(stringResource(R.string.setting_page_preferences_notification)) },
                            supportingContent = { Text(stringResource(R.string.setting_page_preferences_notification_desc)) },
                        )
                        item(
                            onClick = { navController.navigate(Screen.SettingPreferencesNetwork) },
                            leadingContent = { Icon(HugeIcons.Internet, null) },
                            headlineContent = { Text(stringResource(R.string.setting_page_preferences_network)) },
                            supportingContent = { Text(stringResource(R.string.setting_page_preferences_network_desc)) },
                        )
                    }
                }
            }
        }
    }
}
