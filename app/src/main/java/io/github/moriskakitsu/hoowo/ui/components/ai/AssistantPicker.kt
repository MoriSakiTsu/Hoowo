package io.github.moriskakitsu.hoowo.ui.components.ai

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.dokar.sonner.ToastType
import kotlinx.coroutines.launch
import me.rerere.hugeicons.HugeIcons
import me.rerere.hugeicons.stroke.Add01
import me.rerere.hugeicons.stroke.Edit03
import me.rerere.hugeicons.stroke.LookTop
import io.github.moriskakitsu.hoowo.R
import io.github.moriskakitsu.hoowo.Screen
import io.github.moriskakitsu.hoowo.data.datastore.Settings
import io.github.moriskakitsu.hoowo.data.model.Assistant
import io.github.moriskakitsu.hoowo.data.model.PresetAssistant
import io.github.moriskakitsu.hoowo.ui.components.ui.UIAvatar
import io.github.moriskakitsu.hoowo.ui.context.LocalNavController
import io.github.moriskakitsu.hoowo.ui.context.LocalToaster
import io.github.moriskakitsu.hoowo.ui.hooks.rememberAssistantState
import io.github.moriskakitsu.hoowo.ui.pages.assistant.PresetAssistantPickerSheet
import io.github.moriskakitsu.hoowo.ui.pages.assistant.displayName
import kotlin.uuid.Uuid

@Composable
fun AssistantPicker(
    settings: Settings,
    onUpdateSettings: (Settings) -> Unit,
    modifier: Modifier = Modifier,
    onClickSetting: () -> Unit,
) {
    val state = rememberAssistantState(settings, onUpdateSettings)
    val defaultAssistantName = stringResource(R.string.assistant_page_default_assistant)
    var showPicker by remember { mutableStateOf(false) }
    var showPresetPicker by remember { mutableStateOf(false) }
    val toaster = LocalToaster.current

    NavigationDrawerItem(
        icon = {
            Icon(HugeIcons.LookTop, contentDescription = null)
        },
        label = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = state.currentAssistant.name.ifEmpty { defaultAssistantName },
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(Modifier.weight(1f))

                UIAvatar(
                    name = state.currentAssistant.name.ifEmpty { defaultAssistantName },
                    value = state.currentAssistant.avatar,
                    onClick = onClickSetting
                )
            }
        },
        onClick = {
            showPicker = true
        },
        modifier = modifier,
        selected = false,
    )

    if (showPicker) {
        AssistantPickerSheet(
            settings = settings,
            currentAssistant = state.currentAssistant,
            onAssistantSelected = { assistant ->
                showPicker = false
                state.setSelectAssistant(assistant)
            },
            onDismiss = {
                showPicker = false
            },
            onOpenPresetPicker = {
                showPresetPicker = true
            },
        )
    }

    if (showPresetPicker) {
        val context = LocalContext.current
        // 记录待创建的预设, 非空时弹出改名对话框
        var pendingPreset by remember { mutableStateOf<PresetAssistant?>(null) }
        var draftName by remember { mutableStateOf("") }

        if (pendingPreset == null) {
            PresetAssistantPickerSheet(
                onPick = { preset, name ->
                    pendingPreset = preset
                    draftName = name
                },
                onDismiss = { showPresetPicker = false },
            )
        }

        pendingPreset?.let { preset ->
            // 提到 composable 作用域外, 不能在 onClick 里调用 @Composable
            val presetDefaultName = preset.displayName()
            AlertDialog(
                onDismissRequest = {
                    // 取消改名则退回预设列表, 而不是整个关掉
                    pendingPreset = null
                },
                title = { Text(stringResource(R.string.assistant_preset_picker_title)) },
                text = {
                    OutlinedTextField(
                        value = draftName,
                        onValueChange = { draftName = it },
                        label = { Text(stringResource(R.string.assistant_page_name)) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            val finalName = draftName.ifBlank { presetDefaultName }
                            val assistant = preset.toAssistant(finalName)
                            // 必须「新增助手」与「切换当前助手」在同一次写入里完成:
                            // AssistantState 持有的是构造时的 settings 快照, 先写一次再调
                            // state.setSelectAssistant() 会用旧快照覆盖, 新助手会丢失。
                            onUpdateSettings(
                                settings.copy(
                                    assistants = settings.assistants + assistant,
                                    assistantId = assistant.id,
                                )
                            )
                            toaster.show(
                                context.getString(R.string.assistant_preset_picker_created, finalName),
                                type = ToastType.Success,
                            )
                            pendingPreset = null
                            showPresetPicker = false
                        }
                    ) {
                        Text(stringResource(R.string.assistant_preset_created))
                    }
                },
                dismissButton = {
                    TextButton(onClick = { pendingPreset = null }) {
                        Text(stringResource(R.string.cancel))
                    }
                },
            )
        }
    }
}

@Composable
private fun AssistantPickerSheet(
    settings: Settings,
    currentAssistant: Assistant,
    onAssistantSelected: (Assistant) -> Unit,
    onDismiss: () -> Unit,
    onOpenPresetPicker: () -> Unit,
) {
    val sheetState = rememberBottomSheetState(initialValue = SheetValue.Hidden, enabledValues = setOf(SheetValue.Hidden, SheetValue.Expanded))
    val scope = rememberCoroutineScope()
    val defaultAssistantName = stringResource(R.string.assistant_page_default_assistant)

    // 标签过滤状态
    var selectedTagIds by remember { mutableStateOf(emptySet<Uuid>()) }

    // 根据选中的标签过滤助手
    val filteredAssistants = remember(settings.assistants, selectedTagIds) {
        if (selectedTagIds.isEmpty()) {
            settings.assistants
        } else {
            settings.assistants.filter { assistant ->
                assistant.tags.any { tagId -> tagId in selectedTagIds }
            }
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.8f)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = stringResource(R.string.assistant_page_title),
                    style = MaterialTheme.typography.titleLarge,
                )
                // 面向普通用户的主要创建入口: 从学科预设里挑一个, 不必自己写提示词
                TextButton(
                    onClick = {
                        scope.launch {
                            sheetState.hide()
                            onDismiss()
                            onOpenPresetPicker()
                        }
                    }
                ) {
                    Icon(
                        imageVector = HugeIcons.Add01,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                    )
                    Spacer(Modifier.size(4.dp))
                    Text(stringResource(R.string.assistant_preset_picker_title))
                }
            }

            // 标签过滤器
            if (settings.assistantTags.isNotEmpty()) {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(bottom = 8.dp)
                ) {
                    items(settings.assistantTags, key = { tag -> tag.id }) { tag ->
                        FilterChip(
                            onClick = {
                                selectedTagIds = if (tag.id in selectedTagIds) {
                                    selectedTagIds - tag.id
                                } else {
                                    selectedTagIds + tag.id
                                }
                            },
                            label = { Text(tag.name) },
                            selected = tag.id in selectedTagIds,
                            shape = RoundedCornerShape(50),
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            // 助手列表
            val navController = LocalNavController.current
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(filteredAssistants, key = { it.id }) { assistant ->
                    val checked = assistant.id == currentAssistant.id
                    Card(
                        onClick = { onAssistantSelected(assistant) },
                        modifier = Modifier.animateItem(),
                        shape = MaterialTheme.shapes.large,
                        colors = CardDefaults.cardColors(
                            containerColor = if (checked) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface,
                            contentColor = if (checked) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface,
                        ),
                    ) {
                        AssistantItem(
                            assistant = assistant,
                            defaultAssistantName = defaultAssistantName,
                            onEdit = {
                                scope.launch {
                                    sheetState.hide()
                                    onDismiss()
                                    navController.navigate(Screen.AssistantDetail(assistant.id.toString()))
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AssistantItem(
    assistant: Assistant,
    defaultAssistantName: String,
    onEdit: () -> Unit
) {
    ListItem(
        headlineContent = {
            Text(
                text = assistant.name.ifEmpty { defaultAssistantName },
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        leadingContent = {
            UIAvatar(
                name = assistant.name.ifEmpty { defaultAssistantName },
                value = assistant.avatar,
                modifier = Modifier.size(32.dp)
            )
        },
        trailingContent = {
            IconButton(
                onClick = {
                    onEdit()
                }
            ) {
                Icon(
                    imageVector = HugeIcons.Edit03,
                    contentDescription = null
                )
            }
        },
        colors = ListItemDefaults.colors(containerColor = Color.Transparent),
    )
}
