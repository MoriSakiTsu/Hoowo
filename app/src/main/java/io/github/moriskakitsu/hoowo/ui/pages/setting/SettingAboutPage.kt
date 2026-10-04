package io.github.moriskakitsu.hoowo.ui.pages.setting

import android.content.ActivityNotFoundException
import android.content.Intent
import android.widget.Toast
import me.rerere.hugeicons.HugeIcons
import me.rerere.hugeicons.stroke.Code
import me.rerere.hugeicons.stroke.CustomerService
import me.rerere.hugeicons.stroke.Earth
import me.rerere.hugeicons.stroke.File02
import me.rerere.hugeicons.stroke.Github
import me.rerere.hugeicons.stroke.Mail01
import me.rerere.hugeicons.stroke.SmartPhone01
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeFlexibleTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import coil3.compose.AsyncImage
import io.github.moriskakitsu.hoowo.BuildConfig
import io.github.moriskakitsu.hoowo.Hoowo
import io.github.moriskakitsu.hoowo.R
import io.github.moriskakitsu.hoowo.Screen
import io.github.moriskakitsu.hoowo.ui.components.nav.BackButton
import io.github.moriskakitsu.hoowo.ui.components.easteregg.EmojiBurstHost
import io.github.moriskakitsu.hoowo.ui.components.ui.CardGroup
import io.github.moriskakitsu.hoowo.ui.context.LocalNavController
import io.github.moriskakitsu.hoowo.ui.theme.CustomColors
import io.github.moriskakitsu.hoowo.utils.SoundEffectPlayer
import io.github.moriskakitsu.hoowo.utils.openUrl
import io.github.moriskakitsu.hoowo.utils.plus
import io.github.moriskakitsu.hoowo.utils.writeClipboardText

@Composable
fun SettingAboutPage() {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val context = LocalContext.current
    val navController = LocalNavController.current
    val soundOptions = remember { listOf(R.raw.bingbingbing, R.raw.gangguan) }
    val soundEffectPlayer = remember(context) { SoundEffectPlayer(context) }
    DisposableEffect(soundEffectPlayer) {
        soundEffectPlayer.preload(*soundOptions.toIntArray())
        onDispose {
            soundEffectPlayer.release()
        }
    }
    val emojiOptions = remember {
        listOf(
            "🎉", "✨", "🌟", "💫", "🎊", "🥳", "🎈", "🎆", "🎇", "🧨",
            "🌈", "🧧", "🎁", "🍬", "🍭", "🍉", "🍓", "🍒", "🍍", "🥭",
            "🐱", "🐶", "🦊", "🐼", "🦁", "🐯", "🐵", "🦄",
            "❤️", "🧡", "💛", "💚", "💙", "💜",
            "🇨🇳", "🌏", "🌍", "🌎",
            "🤗", "🤩", "😆", "😺", "😸", "🤡",
            "💡", "🔥", "💥", "🚀", "⭐", "🌙"
        )
    }
    var logoCenterPx by remember { mutableStateOf(Offset.Zero) }
    Scaffold(
        topBar = {
            LargeFlexibleTopAppBar(
                title = {
                    Text(stringResource(R.string.about_page_title))
                },
                navigationIcon = {
                    BackButton()
                },
                scrollBehavior = scrollBehavior,
                colors = CustomColors.topBarColors,
            )
        },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        containerColor = CustomColors.topBarColors.containerColor,
    ) { innerPadding ->
        EmojiBurstHost(
            modifier = Modifier.fillMaxSize(),
            emojiOptions = emojiOptions,
            burstCount = 12
        ) { onBurst ->
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = innerPadding + PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        AsyncImage(
                            model = R.mipmap.ic_launcher,
                            contentDescription = "Logo",
                            modifier = Modifier
                                .clip(CircleShape)
                                .size(150.dp)
                                .onGloballyPositioned { coordinates ->
                                    val position = coordinates.positionInParent()
                                    val size = coordinates.size
                                    logoCenterPx = Offset(
                                        position.x + size.width / 2f,
                                        position.y + size.height / 2f
                                    )
                                }
                                .clickable {
                                    onBurst(logoCenterPx)
                                    soundEffectPlayer.play(soundOptions.random())
                                }
                        )

                        Text(
                            text = "Hoowo",
                            style = MaterialTheme.typography.displaySmall,
                        )
                    }
                }

                item {
                    CardGroup(
                        modifier = Modifier.padding(horizontal = 8.dp),
                    ) {
                        item(
                            modifier = Modifier.combinedClickable(
                                onClick = {},
                                onLongClick = { navController.navigate(Screen.Debug) },
                            ),
                            leadingContent = { Icon(HugeIcons.Code, null) },
                            supportingContent = {
                                Text("${BuildConfig.VERSION_NAME} / ${BuildConfig.VERSION_CODE}")
                            },
                            headlineContent = { Text(stringResource(R.string.about_page_version)) },
                        )
                        item(
                            leadingContent = { Icon(HugeIcons.SmartPhone01, null) },
                            supportingContent = {
                                Text("${android.os.Build.MANUFACTURER} ${android.os.Build.MODEL} / Android ${android.os.Build.VERSION.RELEASE} / SDK ${android.os.Build.VERSION.SDK_INT}")
                            },
                            headlineContent = { Text(stringResource(R.string.about_page_system)) },
                        )
                    }
                }

                item {
                    CardGroup(
                        modifier = Modifier.padding(horizontal = 8.dp),
                    ) {
                        item(
                            onClick = { context.openUrl(Hoowo.REPO_URL) },
                            leadingContent = { Icon(HugeIcons.Github, null) },
                            supportingContent = { Text(Hoowo.REPO_URL) },
                            headlineContent = { Text(stringResource(R.string.about_page_github)) },
                        )
                        item(
                            onClick = { context.openUrl(Hoowo.LICENSE_URL) },
                            leadingContent = { Icon(HugeIcons.File02, null) },
                            supportingContent = { Text(Hoowo.LICENSE_URL) },
                            headlineContent = { Text(stringResource(R.string.about_page_license)) },
                        )
                    }
                }

                item {
                    val emailToast = stringResource(R.string.about_page_contact_email_copied)
                    CardGroup(
                        modifier = Modifier.padding(horizontal = 8.dp),
                        title = { Text(stringResource(R.string.about_page_contact)) },
                    ) {
                        item(
                            onClick = { context.writeClipboardText(Hoowo.DEVELOPER_QQ) },
                            leadingContent = { Icon(HugeIcons.CustomerService, null) },
                            supportingContent = { Text(Hoowo.DEVELOPER_QQ) },
                            headlineContent = { Text(stringResource(R.string.about_page_contact_qq)) },
                        )
                        item(
                            onClick = {
                                val intent = Intent(Intent.ACTION_SENDTO).apply {
                                    data = "mailto:${Hoowo.DEVELOPER_EMAIL}".toUri()
                                }
                                try {
                                    context.startActivity(intent)
                                } catch (e: ActivityNotFoundException) {
                                    context.writeClipboardText(Hoowo.DEVELOPER_EMAIL)
                                    Toast.makeText(context, emailToast, Toast.LENGTH_SHORT).show()
                                }
                            },
                            leadingContent = { Icon(HugeIcons.Mail01, null) },
                            supportingContent = { Text(Hoowo.DEVELOPER_EMAIL) },
                            headlineContent = { Text(stringResource(R.string.about_page_contact_email)) },
                        )
                    }
                }
            }
        }
    }
}
