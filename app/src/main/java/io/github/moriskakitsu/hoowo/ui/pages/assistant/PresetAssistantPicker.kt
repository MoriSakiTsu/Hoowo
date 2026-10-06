package io.github.moriskakitsu.hoowo.ui.pages.assistant

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import io.github.moriskakitsu.hoowo.R
import io.github.moriskakitsu.hoowo.data.model.AssistantSubject
import io.github.moriskakitsu.hoowo.data.model.PRESET_SUBJECTS
import io.github.moriskakitsu.hoowo.data.model.PresetAssistant
import io.github.moriskakitsu.hoowo.data.model.presetsOfSubject
import io.github.moriskakitsu.hoowo.ui.components.ui.UIAvatar

/**
 * 预设助手选择器。
 *
 * 两步式: 先用学科 chip 收窄范围, 再从列表里挑具体任务。学科 chip 兼作浏览入口,
 * 默认「全部」时按学科分组展示。
 *
 * 选中某一条不会直接创建, 而是交回 [onPick], 由调用方决定后续流程(通常是先让用户
 * 确认名字再保存)。
 */
@Composable
fun PresetAssistantPickerSheet(
    onPick: (PresetAssistant, String) -> Unit,
    onDismiss: () -> Unit,
) {
    val sheetState = rememberBottomSheetState(
        initialValue = SheetValue.Hidden,
        enabledValues = setOf(SheetValue.Hidden, SheetValue.Expanded),
    )

    // null 表示「全部」
    var selectedSubject by remember { mutableStateOf<AssistantSubject?>(null) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.85f)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = stringResource(R.string.assistant_preset_picker_title),
                style = MaterialTheme.typography.titleLarge,
            )
            Text(
                text = stringResource(R.string.assistant_preset_picker_subtitle),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            // 学科筛选
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(vertical = 8.dp),
            ) {
                item {
                    FilterChip(
                        onClick = { selectedSubject = null },
                        label = { Text(stringResource(R.string.assistant_preset_picker_all_subjects)) },
                        selected = selectedSubject == null,
                        shape = RoundedCornerShape(50),
                    )
                }
                items(PRESET_SUBJECTS, key = { it.name }) { subject ->
                    FilterChip(
                        onClick = { selectedSubject = subject },
                        label = { Text("${subject.emoji} ${stringResource(subject.nameRes())}") },
                        selected = selectedSubject == subject,
                        shape = RoundedCornerShape(50),
                    )
                }
            }

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(bottom = 24.dp),
            ) {
                if (selectedSubject == null) {
                    // 「全部」: 按学科分组展示, 每组带一个小标题
                    PRESET_SUBJECTS.forEach { subject ->
                        val presets = presetsOfSubject(subject)
                        item(key = "header_${subject.name}") {
                            Text(
                                text = "${subject.emoji} ${stringResource(subject.nameRes())}",
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(top = 8.dp, bottom = 2.dp),
                            )
                        }
                        items(presets, key = { it.id }) { preset ->
                            PresetAssistantItem(preset = preset, onPick = onPick)
                        }
                    }
                } else {
                    items(presetsOfSubject(selectedSubject!!), key = { it.id }) { preset ->
                        PresetAssistantItem(preset = preset, onPick = onPick)
                    }
                }
            }
        }
    }
}

@Composable
private fun PresetAssistantItem(
    preset: PresetAssistant,
    onPick: (PresetAssistant, String) -> Unit,
) {
    val name = preset.displayName()
    Card(
        onClick = { onPick(preset, name) },
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface,
        ),
    ) {
        ListItem(
            supportingContent = {
                Text(
                    text = preset.displayDescription(),
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
            },
            leadingContent = {
                UIAvatar(
                    name = name,
                    value = preset.toAssistant(name).avatar,
                    modifier = Modifier.size(40.dp),
                )
            },
            colors = ListItemDefaults.colors(
                containerColor = Color.Transparent,
            ),
        ) {
            Text(
                text = name,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}
