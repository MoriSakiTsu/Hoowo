package io.github.moriskakitsu.hoowo

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import coil3.ImageLoader
import coil3.compose.setSingletonImageLoaderFactory
import coil3.gif.AnimatedImageDecoder
import coil3.gif.GifDecoder
import coil3.network.cachecontrol.CacheControlCacheStrategy
import coil3.network.okhttp.OkHttpNetworkFetcherFactory
import coil3.request.crossfade
import coil3.svg.SvgDecoder
import com.dokar.sonner.Toaster
import com.dokar.sonner.rememberToasterState
import kotlinx.serialization.Serializable
import io.github.moriskakitsu.hoowo.data.datastore.SettingsStore
import io.github.moriskakitsu.hoowo.data.db.DatabaseMigrationTracker
import io.github.moriskakitsu.hoowo.data.db.MigrationState
import io.github.moriskakitsu.hoowo.data.event.AppEvent
import io.github.moriskakitsu.hoowo.data.event.AppEventBus
import io.github.moriskakitsu.hoowo.ui.activity.SafeModeActivity
import io.github.moriskakitsu.hoowo.ui.components.nav.DeveloperOnlyScreen
import io.github.moriskakitsu.hoowo.ui.components.ui.TTSController
import io.github.moriskakitsu.hoowo.ui.context.LocalASRState
import io.github.moriskakitsu.hoowo.ui.context.LocalNavController
import io.github.moriskakitsu.hoowo.ui.context.LocalSettings
import io.github.moriskakitsu.hoowo.ui.context.LocalSharedTransitionScope
import io.github.moriskakitsu.hoowo.ui.context.LocalTTSState
import io.github.moriskakitsu.hoowo.ui.context.LocalToaster
import io.github.moriskakitsu.hoowo.ui.context.Navigator
import io.github.moriskakitsu.hoowo.ui.hooks.readBooleanPreference
import io.github.moriskakitsu.hoowo.ui.hooks.readStringPreference
import io.github.moriskakitsu.hoowo.ui.hooks.rememberCustomAsrState
import io.github.moriskakitsu.hoowo.ui.hooks.rememberCustomTtsState
import io.github.moriskakitsu.hoowo.ui.pages.assistant.AssistantPage
import io.github.moriskakitsu.hoowo.ui.pages.assistant.detail.AssistantBasicPage
import io.github.moriskakitsu.hoowo.ui.pages.assistant.detail.AssistantDetailPage
import io.github.moriskakitsu.hoowo.ui.pages.assistant.detail.AssistantExtensionsPage
import io.github.moriskakitsu.hoowo.ui.pages.assistant.detail.AssistantLocalToolPage
import io.github.moriskakitsu.hoowo.ui.pages.assistant.detail.AssistantMcpPage
import io.github.moriskakitsu.hoowo.ui.pages.assistant.detail.AssistantMemoryPage
import io.github.moriskakitsu.hoowo.ui.pages.assistant.detail.AssistantPromptPage
import io.github.moriskakitsu.hoowo.ui.pages.assistant.detail.AssistantRequestPage
import io.github.moriskakitsu.hoowo.ui.pages.backup.BackupPage
import io.github.moriskakitsu.hoowo.ui.pages.chat.ChatPage
import io.github.moriskakitsu.hoowo.ui.pages.debug.DebugPage
import io.github.moriskakitsu.hoowo.ui.pages.extensions.ExtensionsPage
import io.github.moriskakitsu.hoowo.ui.pages.extensions.PromptPage
import io.github.moriskakitsu.hoowo.ui.pages.extensions.QuickMessagesPage
import io.github.moriskakitsu.hoowo.ui.pages.extensions.skills.SkillDetailPage
import io.github.moriskakitsu.hoowo.ui.pages.extensions.skills.SkillsPage
import io.github.moriskakitsu.hoowo.ui.pages.extensions.workspace.WorkspacePage
import io.github.moriskakitsu.hoowo.ui.pages.extensions.workspace.WorkspaceDetailPage
import io.github.moriskakitsu.hoowo.ui.pages.extensions.workspace.WorkspaceFileEditorPage
import io.github.moriskakitsu.hoowo.ui.pages.extensions.workspace.WorkspaceTerminalPage
import io.github.moriskakitsu.workspace.WorkspaceStorageArea
import io.github.moriskakitsu.hoowo.ui.pages.favorite.FavoritePage
import io.github.moriskakitsu.hoowo.ui.pages.history.HistoryPage
import io.github.moriskakitsu.hoowo.ui.pages.imggen.ImageGenPage
import io.github.moriskakitsu.hoowo.ui.pages.log.LogPage
import io.github.moriskakitsu.hoowo.ui.pages.search.SearchPage
import io.github.moriskakitsu.hoowo.ui.pages.setting.SettingAboutPage
import io.github.moriskakitsu.hoowo.ui.pages.setting.SettingPreferencesPage
import io.github.moriskakitsu.hoowo.ui.pages.setting.SettingPreferencesThemePage
import io.github.moriskakitsu.hoowo.ui.pages.setting.SettingPreferencesNotificationPage
import io.github.moriskakitsu.hoowo.ui.pages.setting.SettingPreferencesGeneralPage
import io.github.moriskakitsu.hoowo.ui.pages.setting.SettingPreferencesNetworkPage
import io.github.moriskakitsu.hoowo.ui.pages.setting.SettingPreferencesUIPage
import io.github.moriskakitsu.hoowo.ui.pages.setting.SettingThemePage
import io.github.moriskakitsu.hoowo.ui.pages.setting.SettingFilesPage
import io.github.moriskakitsu.hoowo.ui.pages.setting.SettingMcpPage
import io.github.moriskakitsu.hoowo.ui.pages.setting.SettingModelPage
import io.github.moriskakitsu.hoowo.ui.pages.setting.SettingPage
import io.github.moriskakitsu.hoowo.ui.pages.setting.SettingProviderDetailPage
import io.github.moriskakitsu.hoowo.ui.pages.setting.SettingProviderPage
import io.github.moriskakitsu.hoowo.ui.pages.setting.SettingSearchDetailPage
import io.github.moriskakitsu.hoowo.ui.pages.setting.SettingSearchPage
import io.github.moriskakitsu.hoowo.ui.pages.setting.SettingSpeechPage
import io.github.moriskakitsu.hoowo.ui.pages.setting.SettingWebPage
import io.github.moriskakitsu.hoowo.ui.pages.share.handler.ShareHandlerPage
import io.github.moriskakitsu.hoowo.ui.pages.stats.StatsPage
import io.github.moriskakitsu.hoowo.ui.pages.translator.TranslatorPage
import io.github.moriskakitsu.hoowo.ui.pages.webview.WebViewPage
import io.github.moriskakitsu.hoowo.ui.theme.LocalDarkMode
import io.github.moriskakitsu.hoowo.ui.theme.HoowoTheme
import io.github.moriskakitsu.hoowo.utils.CrashHandler
import io.github.moriskakitsu.hoowo.utils.openUsageAccessSettings
import okhttp3.OkHttpClient
import org.koin.android.ext.android.inject
import org.koin.compose.koinInject
import kotlin.uuid.Uuid

private const val TAG = "RouteActivity"
private const val ACTION_TRANSLATE = "io.github.moriskakitsu.hoowo.action.TRANSLATE"
private const val ACTION_IMAGE_GEN = "io.github.moriskakitsu.hoowo.action.IMAGE_GEN"

class RouteActivity : ComponentActivity() {
    private val okHttpClient by inject<OkHttpClient>()
    private val settingsStore by inject<SettingsStore>()
    private var navStack: MutableList<NavKey>? = null
    private val pendingIntents = ArrayDeque<Intent>()

    // Volume key listener registry — last registered handler wins
    internal val volumeKeyListeners = mutableListOf<(isVolumeUp: Boolean) -> Boolean>()

    @SuppressLint("RestrictedApi")
    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        if (event.action == KeyEvent.ACTION_DOWN) {
            val isVolumeUp = when (event.keyCode) {
                KeyEvent.KEYCODE_VOLUME_UP -> true
                KeyEvent.KEYCODE_VOLUME_DOWN -> false
                else -> return super.dispatchKeyEvent(event)
            }
            if (volumeKeyListeners.lastOrNull()?.invoke(isVolumeUp) == true) return true
        }
        return super.dispatchKeyEvent(event)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        disableNavigationBarContrast()
        super.onCreate(savedInstanceState)
        if (CrashHandler.hasCrashed(this)) {
            startActivity(Intent(this, SafeModeActivity::class.java))
            finish()
            return
        }
        if (savedInstanceState == null) {
            handleIntent(intent)
        }
        setContent {
            HoowoTheme {
                setSingletonImageLoaderFactory { context ->
                    ImageLoader.Builder(context)
                        .crossfade(true)
                        .components {
                            add(
                                OkHttpNetworkFetcherFactory(
                                    callFactory = { okHttpClient },
                                    cacheStrategy = { CacheControlCacheStrategy() },
                                )
                            )
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                                add(AnimatedImageDecoder.Factory())
                            } else {
                                add(GifDecoder.Factory())
                            }
                            add(SvgDecoder.Factory(scaleToDensity = true))
                        }
                        .build()
                }
                AppRoutes()
            }
        }
    }

    private fun disableNavigationBarContrast() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            window.isNavigationBarContrastEnforced = false
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        val backStack = navStack ?: run {
            // Compose 尚未创建导航栈，待就绪后处理。
            pendingIntents.addLast(intent)
            return
        }
        val destination = when (intent.action) {
            ACTION_TRANSLATE -> Screen.Translator
            ACTION_IMAGE_GEN -> Screen.ImageGen
            Intent.ACTION_SEND -> Screen.ShareHandler(
                text = intent.getStringExtra(Intent.EXTRA_TEXT).orEmpty(),
                streamUri = intent.getStringExtra(Intent.EXTRA_STREAM),
            )
            Intent.ACTION_PROCESS_TEXT -> Screen.ShareHandler(
                text = intent.getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT)?.toString().orEmpty(),
            )
            else -> intent.getStringExtra("conversationId")?.let { Screen.Chat(it) }
        }
        if (destination != null && backStack.lastOrNull() != destination) {
            backStack.add(destination)
        }
    }

    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    fun AppRoutes() {
        val toastState = rememberToasterState()
        val settings by settingsStore.settingsFlow.collectAsStateWithLifecycle()
        val tts = rememberCustomTtsState()
        val asr = rememberCustomAsrState()
        val eventBus = koinInject<AppEventBus>()
        LaunchedEffect(tts) {
            eventBus.events.collect { event ->
                when (event) {
                    is AppEvent.Speak -> tts.speak(event.text)
                    is AppEvent.OpenUsageAccessSettings -> this@RouteActivity.openUsageAccessSettings()
                    is AppEvent.ChatGenerationUpdate -> Unit // 由 ChatNotificationManager 消费
                    is AppEvent.ChatGenerationEnded -> Unit // 由 ChatNotificationManager 消费
                }
            }
        }
        val migrationState by DatabaseMigrationTracker.state.collectAsStateWithLifecycle()

        val startScreen = Screen.Chat(
            id = if (readBooleanPreference("create_new_conversation_on_start", true)) {
                Uuid.random().toString()
            } else {
                readStringPreference(
                    "lastConversationId",
                    Uuid.random().toString()
                ) ?: Uuid.random().toString()
            }
        )

        val backStack = rememberNavBackStack(startScreen)
        SideEffect {
            navStack = backStack
            while (pendingIntents.isNotEmpty()) {
                handleIntent(pendingIntents.removeFirst())
            }
        }

        SharedTransitionLayout {
            CompositionLocalProvider(
                LocalNavController provides Navigator(backStack),
                LocalSharedTransitionScope provides this,
                LocalSettings provides settings,
                LocalToaster provides toastState,
                LocalTTSState provides tts,
                LocalASRState provides asr,
            ) {
                Toaster(
                    state = toastState,
                    darkTheme = LocalDarkMode.current,
                    richColors = true,
                    alignment = Alignment.TopCenter,
                    showCloseButton = true,
                )
                TTSController()
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .semantics { testTagsAsResourceId = true }
                        .background(MaterialTheme.colorScheme.background)
                ) {
                    NavDisplay(
                        backStack = backStack,
                        entryDecorators = listOf(
                            rememberSaveableStateHolderNavEntryDecorator(),
                            rememberViewModelStoreNavEntryDecorator(),
                        ),
                        modifier = Modifier.fillMaxSize(),
                        onBack = { backStack.removeLastOrNull() },
                        transitionSpec = {
                            if (backStack.size == 1) fadeIn() togetherWith fadeOut()
                            else {
                                slideInHorizontally { it } togetherWith
                                    slideOutHorizontally { -it / 2 } + scaleOut(targetScale = 0.7f) + fadeOut()
                            }
                        },
                        popTransitionSpec = {
                            slideInHorizontally { -it / 2 } + scaleIn(initialScale = 0.7f) + fadeIn() togetherWith
                                slideOutHorizontally { it }
                        },
                        predictivePopTransitionSpec = {
                            slideInHorizontally { -it / 2 } + scaleIn(initialScale = 0.7f) + fadeIn() togetherWith
                                slideOutHorizontally { it }
                        },
                        entryProvider = entryProvider {
                            entry<Screen.Chat>(
                                metadata = NavDisplay.transitionSpec { fadeIn() togetherWith fadeOut() }
                                    + NavDisplay.popTransitionSpec { fadeIn() togetherWith fadeOut() }
                            ) { key ->
                                ChatPage(
                                    id = Uuid.parse(key.id),
                                    text = key.text,
                                    files = key.files.map { it.toUri() },
                                    nodeId = key.nodeId?.let { Uuid.parse(it) }
                                )
                            }

                            entry<Screen.ShareHandler> { key ->
                                ShareHandlerPage(
                                    text = key.text,
                                    image = key.streamUri
                                )
                            }

                            entry<Screen.History> {
                                HistoryPage()
                            }

                            entry<Screen.Favorite> {
                                FavoritePage()
                            }

                            entry<Screen.Assistant> {
                                AssistantPage()
                            }

                            entry<Screen.AssistantDetail> { key ->
                                AssistantDetailPage(key.id)
                            }

                            entry<Screen.AssistantBasic> { key ->
                                AssistantBasicPage(key.id)
                            }

                            entry<Screen.AssistantPrompt> { key ->
                                AssistantPromptPage(key.id)
                            }

                            entry<Screen.AssistantMemory> { key ->
                                DeveloperOnlyScreen {
                                    AssistantMemoryPage(key.id)
                                }
                            }

                            entry<Screen.AssistantRequest> { key ->
                                DeveloperOnlyScreen {
                                    AssistantRequestPage(key.id)
                                }
                            }

                            entry<Screen.AssistantMcp> { key ->
                                DeveloperOnlyScreen {
                                    AssistantMcpPage(key.id)
                                }
                            }

                            entry<Screen.AssistantLocalTool> { key ->
                                DeveloperOnlyScreen {
                                    AssistantLocalToolPage(key.id)
                                }
                            }

                            entry<Screen.AssistantInjections> { key ->
                                DeveloperOnlyScreen {
                                    AssistantExtensionsPage(key.id)
                                }
                            }

                            entry<Screen.Translator> {
                                DeveloperOnlyScreen {
                                    TranslatorPage()
                                }
                            }

                            entry<Screen.Setting> {
                                SettingPage()
                            }

                            entry<Screen.Backup> {
                                BackupPage()
                            }

                            entry<Screen.ImageGen> {
                                DeveloperOnlyScreen {
                                    ImageGenPage()
                                }
                            }

                            entry<Screen.WebView> { key ->
                                WebViewPage(key.url, key.contentId)
                            }

                            entry<Screen.SettingTheme> {
                                SettingThemePage()
                            }

                            entry<Screen.SettingPreferences> {
                                SettingPreferencesPage()
                            }

                            entry<Screen.SettingPreferencesTheme> {
                                SettingPreferencesThemePage()
                            }

                            entry<Screen.SettingPreferencesNotification> {
                                SettingPreferencesNotificationPage()
                            }

                            entry<Screen.SettingPreferencesGeneral> {
                                SettingPreferencesGeneralPage()
                            }

                            entry<Screen.SettingPreferencesUI> {
                                SettingPreferencesUIPage()
                            }

                            entry<Screen.SettingPreferencesNetwork> {
                                SettingPreferencesNetworkPage()
                            }

                            entry<Screen.SettingProvider> {
                                SettingProviderPage()
                            }

                            entry<Screen.SettingProviderDetail> { key ->
                                val id = Uuid.parse(key.providerId)
                                SettingProviderDetailPage(id = id)
                            }

                            entry<Screen.SettingModels> {
                                DeveloperOnlyScreen {
                                    SettingModelPage()
                                }
                            }

                            entry<Screen.SettingAbout> {
                                SettingAboutPage()
                            }

                            entry<Screen.SettingSearch> {
                                DeveloperOnlyScreen {
                                    SettingSearchPage()
                                }
                            }

                            entry<Screen.SettingSearchDetail> { key ->
                                val id = Uuid.parse(key.serviceId)
                                DeveloperOnlyScreen {
                                    SettingSearchDetailPage(id)
                                }
                            }

                            entry<Screen.SettingSpeech> {
                                DeveloperOnlyScreen {
                                    SettingSpeechPage()
                                }
                            }

                            entry<Screen.SettingMcp> {
                                DeveloperOnlyScreen {
                                    SettingMcpPage()
                                }
                            }

                            entry<Screen.SettingFiles> {
                                SettingFilesPage()
                            }

                            entry<Screen.SettingWeb> {
                                DeveloperOnlyScreen {
                                    SettingWebPage()
                                }
                            }

                            entry<Screen.Debug> {
                                DeveloperOnlyScreen {
                                    DebugPage()
                                }
                            }

                            entry<Screen.Log> {
                                LogPage()
                            }

                            entry<Screen.Extensions> {
                                ExtensionsPage()
                            }

                            entry<Screen.QuickMessages> {
                                DeveloperOnlyScreen {
                                    QuickMessagesPage()
                                }
                            }

                            entry<Screen.Prompts> {
                                DeveloperOnlyScreen {
                                    PromptPage()
                                }
                            }

                            entry<Screen.Skills> {
                                DeveloperOnlyScreen {
                                    SkillsPage()
                                }
                            }

                            entry<Screen.Workspaces> {
                                DeveloperOnlyScreen {
                                    WorkspacePage()
                                }
                            }

                            entry<Screen.WorkspaceDetail> { key ->
                                DeveloperOnlyScreen {
                                    WorkspaceDetailPage(key.id)
                                }
                            }

                            entry<Screen.WorkspaceTerminal> { key ->
                                DeveloperOnlyScreen {
                                    WorkspaceTerminalPage(key.id)
                                }
                            }

                            entry<Screen.WorkspaceFileEditor> { key ->
                                DeveloperOnlyScreen {
                                    WorkspaceFileEditorPage(
                                        id = key.id,
                                        area = WorkspaceStorageArea.valueOf(key.area),
                                        path = key.path,
                                    )
                                }
                            }

                            entry<Screen.SkillDetail> { key ->
                                DeveloperOnlyScreen {
                                    SkillDetailPage(skillName = key.skillName)
                                }
                            }

                            entry<Screen.MessageSearch> {
                                SearchPage()
                            }

                            entry<Screen.Stats> {
                                DeveloperOnlyScreen {
                                    StatsPage()
                                }
                            }
                        }
                    )
                    if (BuildConfig.DEBUG) {
                        Text(
                            text = "[开发模式]",
                            modifier = Modifier
                                .align(Alignment.TopCenter)
                                .padding(top = 4.dp),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.error.copy(alpha = 0.7f)
                        )
                    }
                    AnimatedVisibility(
                        visible = migrationState is MigrationState.Migrating,
                        enter = fadeIn(),
                        exit = fadeOut(),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        val state = migrationState as? MigrationState.Migrating
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                CircularProgressIndicator()
                                Text(
                                    text = stringResource(R.string.db_migrating),
                                    style = MaterialTheme.typography.bodyLarge
                                )
                                if (state != null) {
                                    Text(
                                        text = "v${state.from} → v${state.to}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

sealed interface Screen : NavKey {
    @Serializable
    data class Chat(
        val id: String,
        val text: String? = null,
        val files: List<String> = emptyList(),
        val nodeId: String? = null
    ) : Screen

    @Serializable
    data class ShareHandler(val text: String, val streamUri: String? = null) : Screen

    @Serializable
    data object History : Screen

    @Serializable
    data object Favorite : Screen

    @Serializable
    data object Assistant : Screen

    @Serializable
    data class AssistantDetail(val id: String) : Screen

    @Serializable
    data class AssistantBasic(val id: String) : Screen

    @Serializable
    data class AssistantPrompt(val id: String) : Screen

    @Serializable
    data class AssistantMemory(val id: String) : Screen

    @Serializable
    data class AssistantRequest(val id: String) : Screen

    @Serializable
    data class AssistantMcp(val id: String) : Screen

    @Serializable
    data class AssistantLocalTool(val id: String) : Screen

    @Serializable
    data class AssistantInjections(val id: String) : Screen

    @Serializable
    data object Translator : Screen

    @Serializable
    data object Setting : Screen

    @Serializable
    data object Backup : Screen

    @Serializable
    data object ImageGen : Screen

    @Serializable
    data class WebView(val url: String = "", val contentId: String = "") : Screen

    @Serializable
    data object SettingTheme : Screen

    @Serializable
    data object SettingPreferences : Screen

    @Serializable
    data object SettingPreferencesTheme : Screen

    @Serializable
    data object SettingPreferencesNotification : Screen

    @Serializable
    data object SettingPreferencesGeneral : Screen

    @Serializable
    data object SettingPreferencesUI : Screen

    @Serializable
    data object SettingPreferencesNetwork : Screen

    @Serializable
    data object SettingProvider : Screen

    @Serializable
    data class SettingProviderDetail(val providerId: String) : Screen

    @Serializable
    data object SettingModels : Screen

    @Serializable
    data object SettingAbout : Screen

    @Serializable
    data object SettingSearch : Screen

    @Serializable
    data class SettingSearchDetail(val serviceId: String) : Screen

    @Serializable
    data object SettingSpeech : Screen

    @Serializable
    data object SettingMcp : Screen

    @Serializable
    data object SettingFiles : Screen

    @Serializable
    data object SettingWeb : Screen

    @Serializable
    data object Debug : Screen

    @Serializable
    data object Log : Screen

    @Serializable
    data object Extensions : Screen

    @Serializable
    data object QuickMessages : Screen

    @Serializable
    data object Prompts : Screen

    @Serializable
    data object Skills : Screen

    @Serializable
    data object Workspaces : Screen

    @Serializable
    data class WorkspaceDetail(val id: String) : Screen

    @Serializable
    data class WorkspaceTerminal(val id: String) : Screen

    @Serializable
    data class WorkspaceFileEditor(val id: String, val area: String, val path: String) : Screen

    @Serializable
    data class SkillDetail(val skillName: String) : Screen

    @Serializable
    data object MessageSearch : Screen

    @Serializable
    data object Stats : Screen
}
