package io.github.moriskakitsu.hoowo.ui.components.nav

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import io.github.moriskakitsu.hoowo.Screen
import io.github.moriskakitsu.hoowo.ui.context.LocalNavController
import io.github.moriskakitsu.hoowo.ui.hooks.rememberDeveloperMode

/**
 * 仅在"开发者选项"开启时渲染 [content] 的路由守卫。
 *
 * 关闭时（默认）不渲染任何内容，并按以下顺序退出：
 * - 不是栈底 -> 回退上一页，保留用户原本的浏览路径
 * - 已是栈底（例如通过 Deep Link / 桌面快捷方式 / 分享 Intent 直达）-> 跳到设置页，
 *   避免出现空白页面或退出应用
 *
 * 这是入口隐藏之外的第二层防护：即使某处遗漏了入口判断，或者用户通过
 * Deep Link、快捷方式、分享 Intent 绕过了界面入口，也无法进入开发者专属页面。
 */
@Composable
fun DeveloperOnlyScreen(content: @Composable () -> Unit) {
    val developerMode by rememberDeveloperMode()
    val navController = LocalNavController.current

    if (developerMode) {
        content()
        return
    }

    LaunchedEffect(Unit) {
        if (navController.backStackSize > 1) {
            navController.popBackStack()
        } else {
            navController.clearAndNavigate(Screen.Setting)
        }
    }
}
