package io.github.moriskakitsu.hoowo.data.model

import io.github.moriskakitsu.hoowo.data.ai.prompts.BIOLOGY_PROMPTS
import io.github.moriskakitsu.hoowo.data.ai.prompts.CHEMISTRY_PROMPTS
import io.github.moriskakitsu.hoowo.data.ai.prompts.CHINESE_PROMPTS
import io.github.moriskakitsu.hoowo.data.ai.prompts.ENGLISH_PROMPTS
import io.github.moriskakitsu.hoowo.data.ai.prompts.GEOGRAPHY_PROMPTS
import io.github.moriskakitsu.hoowo.data.ai.prompts.HISTORY_PROMPTS
import io.github.moriskakitsu.hoowo.data.ai.prompts.JAPANESE_PROMPTS
import io.github.moriskakitsu.hoowo.data.ai.prompts.MATH_PROMPTS
import io.github.moriskakitsu.hoowo.data.ai.prompts.PHYSICS_PROMPTS
import io.github.moriskakitsu.hoowo.data.ai.prompts.POLITICS_PROMPTS

/**
 * 预设助手所属学科。
 *
 * 学科只用于「创建助手」时的分组与展示, 不会被写入 [Assistant] —— 选中的预设会被
 * 实例化成一个普通的助手, 之后与手动创建的助手完全一致, 不受学科约束。
 */
enum class AssistantSubject(
    /** 展示用 emoji, 同时作为该学科下预设助手的默认头像 */
    val emoji: String,
) {
    CHINESE("📖"),
    MATH("📐"),
    ENGLISH("🔤"),
    PHYSICS("🧭"),
    CHEMISTRY("🧪"),
    BIOLOGY("🧬"),
    POLITICS("⚖"),
    HISTORY("📜"),
    GEOGRAPHY("🌍"),
    JAPANESE("🎌"),
}

/**
 * 一个可创建的预设助手。
 *
 * 提示词正文集中在 `data/ai/prompts/SubjectAssistants.kt`, 便于集中修改。
 *
 * @param id 稳定标识, 仅用于列表 key 与排查问题, 不要重命名
 * @param subject 所属学科, 决定在创建列表中的分组
 * @param emoji 头像 emoji; 为 null 时使用所属学科的 emoji
 * @param systemPrompt 助手的系统提示词, 支持 Pebble 模板变量
 */
data class PresetAssistant(
    val id: String,
    val subject: AssistantSubject,
    val emoji: String? = null,
    val systemPrompt: String,
) {
    /** 实际使用的头像 emoji */
    val avatarEmoji: String get() = emoji ?: subject.emoji

    /**
     * 生成待保存的 [Assistant]。
     *
     * 只设置名字、头像与系统提示词, 其余字段保持与手动创建一致的默认值,
     * 避免把高级配置一次性塞给普通用户。
     */
    fun toAssistant(name: String): Assistant = Assistant(
        name = name,
        avatar = Avatar.Emoji(avatarEmoji),
        systemPrompt = systemPrompt,
    )
}

/** 全部预设助手 */
val PRESET_ASSISTANTS: List<PresetAssistant> = buildList {
    addAll(CHINESE_PROMPTS)
    addAll(MATH_PROMPTS)
    addAll(ENGLISH_PROMPTS)
    addAll(PHYSICS_PROMPTS)
    addAll(CHEMISTRY_PROMPTS)
    addAll(BIOLOGY_PROMPTS)
    addAll(POLITICS_PROMPTS)
    addAll(HISTORY_PROMPTS)
    addAll(GEOGRAPHY_PROMPTS)
    addAll(JAPANESE_PROMPTS)
}

/** 取某个学科下的全部预设, 保持声明顺序 */
fun presetsOfSubject(subject: AssistantSubject): List<PresetAssistant> =
    PRESET_ASSISTANTS.filter { it.subject == subject }

/** 有预设的学科, 用于创建列表的分组展示 */
val PRESET_SUBJECTS: List<AssistantSubject> =
    AssistantSubject.entries.filter { subject -> presetsOfSubject(subject).isNotEmpty() }
