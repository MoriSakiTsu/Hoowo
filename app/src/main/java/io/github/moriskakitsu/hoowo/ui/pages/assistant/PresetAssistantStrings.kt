package io.github.moriskakitsu.hoowo.ui.pages.assistant

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import io.github.moriskakitsu.hoowo.R
import io.github.moriskakitsu.hoowo.data.model.AssistantSubject
import io.github.moriskakitsu.hoowo.data.model.PresetAssistant

/**
 * 预设助手的显示名与描述的字符串资源映射。
 *
 * 单独放在 UI 层的原因: [PresetAssistant] 属于 data 层, 不应持有 Android 资源 ID。
 * 提示词正文在 `data/ai/prompts/SubjectAssistants.kt`, 这里只负责"叫什么名字"。
 *
 * 新增预设时必须同时在这里登记, 否则 [presetNameRes] 会抛异常, 便于及早发现遗漏。
 */
@StringRes
private fun presetNameRes(presetId: String): Int = when (presetId) {
    // 语文
    "chinese_reading_notes" -> R.string.assistant_preset_chinese_reading_notes_name
    "chinese_writing" -> R.string.assistant_preset_chinese_writing_name
    "chinese_comprehension" -> R.string.assistant_preset_chinese_comprehension_name
    "chinese_exam" -> R.string.assistant_preset_chinese_exam_name
    // 数学
    "math_practice" -> R.string.assistant_preset_math_practice_name
    "math_thinking" -> R.string.assistant_preset_math_thinking_name
    // 英语
    "english_weekly_review" -> R.string.assistant_preset_english_weekly_review_name
    "english_self_expansion" -> R.string.assistant_preset_english_self_expansion_name
    "english_practice" -> R.string.assistant_preset_english_practice_name
    "english_writing" -> R.string.assistant_preset_english_writing_name
    // 物理
    "physics_practice" -> R.string.assistant_preset_physics_practice_name
    "physics_thinking" -> R.string.assistant_preset_physics_thinking_name
    // 化学
    "chemistry_practice" -> R.string.assistant_preset_chemistry_practice_name
    "chemistry_thinking" -> R.string.assistant_preset_chemistry_thinking_name
    // 生物
    "biology_practice" -> R.string.assistant_preset_biology_practice_name
    "biology_thinking" -> R.string.assistant_preset_biology_thinking_name
    // 政治
    "politics_practice" -> R.string.assistant_preset_politics_practice_name
    "politics_subjective" -> R.string.assistant_preset_politics_subjective_name
    "politics_writing" -> R.string.assistant_preset_politics_writing_name
    // 历史
    "history_practice" -> R.string.assistant_preset_history_practice_name
    "history_thinking" -> R.string.assistant_preset_history_thinking_name
    "history_source" -> R.string.assistant_preset_history_source_name
    // 地理
    "geography_practice" -> R.string.assistant_preset_geography_practice_name
    "geography_thinking" -> R.string.assistant_preset_geography_thinking_name
    "geography_chart" -> R.string.assistant_preset_geography_chart_name
    // 日语
    "japanese_kana" -> R.string.assistant_preset_japanese_kana_name
    "japanese_grammar" -> R.string.assistant_preset_japanese_grammar_name
    "japanese_reading" -> R.string.assistant_preset_japanese_reading_name
    "japanese_writing" -> R.string.assistant_preset_japanese_writing_name
    "japanese_vocabulary" -> R.string.assistant_preset_japanese_vocabulary_name
    "japanese_translation" -> R.string.assistant_preset_japanese_translation_name
    else -> error("预设助手 '$presetId' 未在 presetNameRes 中登记名称资源")
}

@StringRes
private fun presetDescRes(presetId: String): Int = when (presetId) {
    // 语文
    "chinese_reading_notes" -> R.string.assistant_preset_chinese_reading_notes_desc
    "chinese_writing" -> R.string.assistant_preset_chinese_writing_desc
    "chinese_comprehension" -> R.string.assistant_preset_chinese_comprehension_desc
    "chinese_exam" -> R.string.assistant_preset_chinese_exam_desc
    // 数学
    "math_practice" -> R.string.assistant_preset_math_practice_desc
    "math_thinking" -> R.string.assistant_preset_math_thinking_desc
    // 英语
    "english_weekly_review" -> R.string.assistant_preset_english_weekly_review_desc
    "english_self_expansion" -> R.string.assistant_preset_english_self_expansion_desc
    "english_practice" -> R.string.assistant_preset_english_practice_desc
    "english_writing" -> R.string.assistant_preset_english_writing_desc
    // 物理
    "physics_practice" -> R.string.assistant_preset_physics_practice_desc
    "physics_thinking" -> R.string.assistant_preset_physics_thinking_desc
    // 化学
    "chemistry_practice" -> R.string.assistant_preset_chemistry_practice_desc
    "chemistry_thinking" -> R.string.assistant_preset_chemistry_thinking_desc
    // 生物
    "biology_practice" -> R.string.assistant_preset_biology_practice_desc
    "biology_thinking" -> R.string.assistant_preset_biology_thinking_desc
    // 政治
    "politics_practice" -> R.string.assistant_preset_politics_practice_desc
    "politics_subjective" -> R.string.assistant_preset_politics_subjective_desc
    "politics_writing" -> R.string.assistant_preset_politics_writing_desc
    // 历史
    "history_practice" -> R.string.assistant_preset_history_practice_desc
    "history_thinking" -> R.string.assistant_preset_history_thinking_desc
    "history_source" -> R.string.assistant_preset_history_source_desc
    // 地理
    "geography_practice" -> R.string.assistant_preset_geography_practice_desc
    "geography_thinking" -> R.string.assistant_preset_geography_thinking_desc
    "geography_chart" -> R.string.assistant_preset_geography_chart_desc
    // 日语
    "japanese_kana" -> R.string.assistant_preset_japanese_kana_desc
    "japanese_grammar" -> R.string.assistant_preset_japanese_grammar_desc
    "japanese_reading" -> R.string.assistant_preset_japanese_reading_desc
    "japanese_writing" -> R.string.assistant_preset_japanese_writing_desc
    "japanese_vocabulary" -> R.string.assistant_preset_japanese_vocabulary_desc
    "japanese_translation" -> R.string.assistant_preset_japanese_translation_desc
    else -> error("预设助手 '$presetId' 未在 presetDescRes 中登记描述资源")
}

@StringRes
internal fun AssistantSubject.nameRes(): Int = when (this) {
    AssistantSubject.CHINESE -> R.string.assistant_preset_subject_chinese
    AssistantSubject.MATH -> R.string.assistant_preset_subject_math
    AssistantSubject.ENGLISH -> R.string.assistant_preset_subject_english
    AssistantSubject.PHYSICS -> R.string.assistant_preset_subject_physics
    AssistantSubject.CHEMISTRY -> R.string.assistant_preset_subject_chemistry
    AssistantSubject.BIOLOGY -> R.string.assistant_preset_subject_biology
    AssistantSubject.POLITICS -> R.string.assistant_preset_subject_politics
    AssistantSubject.HISTORY -> R.string.assistant_preset_subject_history
    AssistantSubject.GEOGRAPHY -> R.string.assistant_preset_subject_geography
    AssistantSubject.JAPANESE -> R.string.assistant_preset_subject_japanese
}

/** 预设助手的本地化显示名, 同时用作创建出来的助手名字 */
@Composable
internal fun PresetAssistant.displayName(): String = stringResource(presetNameRes(id))

/** 预设助手的本地化描述 */
@Composable
internal fun PresetAssistant.displayDescription(): String = stringResource(presetDescRes(id))
