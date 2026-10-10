package io.github.moriskakitsu.hoowo.ui.components.ui

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ContainedLoadingIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import io.github.moriskakitsu.hoowo.R
import io.github.moriskakitsu.hoowo.ui.context.LocalSettings
import kotlin.math.abs

/**
 * 生成/等待中的加载指示器。
 *
 * [useAppIconStyleLoadingIndicator] 开启时（默认）显示 Hoowo 标志的呼吸动画；
 * 关闭时回退到 Material 的 [ContainedLoadingIndicator]。
 *
 * 动画：整体 0.90~1.00 缩放、0.55~1.00 透明度，1.6 秒一轮。
 * 两条曲线由同一个无限过渡驱动，用三角波保证首尾衔接处不跳变。
 */
@Composable
fun HoowoLoadingIndicator(modifier: Modifier = Modifier) {
    val useAppIconStyleLoadingIndicator =
        LocalSettings.current.displaySetting.useAppIconStyleLoadingIndicator

    if (!useAppIconStyleLoadingIndicator) {
        ContainedLoadingIndicator(modifier = modifier)
        return
    }

    val transition = rememberInfiniteTransition(label = "hoowo_loading")
    val progress by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1600, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "progress",
    )
    // 三角波 0 -> 1 -> 0, 端点连续
    val wave = 1f - abs(progress * 2f - 1f)

    // 调用方负责传尺寸(现有两处分别传 28dp / 32dp), 这里只兜一个默认值
    Box(
        modifier = modifier.then(Modifier.size(28.dp)),
    ) {
        Image(
            painter = painterResource(R.drawable.hoowo_loading),
            contentDescription = null,
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    val scale = 0.90f + 0.10f * wave
                    scaleX = scale
                    scaleY = scale
                    alpha = 0.55f + 0.45f * wave
                },
        )
    }
}
