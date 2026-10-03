<div align="center">
  <h1>Hoowo</h1>

一个面向学生的原生 Android LLM 聊天客户端，专注于作业与学习辅助 🤖💬

基于 [RikkaHub](https://github.com/rikkahub/rikkahub) 二次开发。

[简体中文](README_ZH_CN.md) | [繁體中文](README_ZH_TW.md) | [English](README.md)
</div>

## ✨ 功能

- 🎨 Material You 设计与 🌙 深色模式
- 🔄 多 AI 提供商支持：自定义 API / 地址 / 模型（兼容所有 OpenAI、Google、Anthropic 格式接口）
- 🖼️ 多模态输入（图片、文本文档、PDF、Docx）
- 🖥️ Web 端访问，多平台使用
- 🛠️ MCP 支持
- 📝 Markdown 渲染（代码高亮、LaTeX 公式、表格、Mermaid）
- 🪾 消息分支
- 🔍 联网搜索（Exa、Tavily、智谱、LinkUp、Brave、Perplexity 等）
- 🧩 提示词变量（模型名、时间等）
- 🤖 助手自定义
- 🧠 类 ChatGPT 记忆功能
- 📝 AI 翻译
- 🌐 自定义请求头与请求体
- 💌 Silly Tavern 角色卡导入

## 💻 开发

本项目使用 [Android Studio](https://developer.android.com/studio) 开发。

技术栈：

- [Kotlin](https://kotlinlang.org/)（开发语言）
- [Koin](https://insert-koin.io/)（依赖注入）
- [Jetpack Compose](https://developer.android.com/jetpack/compose)（UI 框架）
- [DataStore](https://developer.android.com/topic/libraries/architecture/datastore)（偏好存储）
- [Room](https://developer.android.com/training/data-storage/room)（数据库）
- [Coil](https://coil-kt.github.io/coil/)（图片加载）
- [Material You](https://m3.material.io/)（UI 设计）
- [Navigation 3](https://developer.android.com/guide/navigation/navigation-3)（导航）
- [Okhttp](https://square.github.io/okhttp/)（网络请求）
- [kotlinx.serialization](https://github.com/Kotlin/kotlinx.serialization)（JSON 序列化）

构建命令：

```bash
./gradlew assembleDebug          # 构建 Debug APK
./gradlew test                   # 运行 JVM 单元测试
./gradlew lint                   # 运行 Android Lint
```

> [!NOTE]
> Windows 上若项目路径包含非 ASCII 字符，请在 `gradle.properties` 中添加
> `android.overridePathCheck=true`。APK 构建可以正常进行，但 `./gradlew test`
> 在该路径下会失效：测试进程无法解析 classpath 条目并报
> `ClassNotFoundException`。请将项目移动到纯 ASCII 路径
> （例如 `E:\Projects\Hoowo`）再运行单元测试。

## 🙏 致谢

Hoowo 基于 [re-ovo](https://github.com/re-ovo) 的优秀开源项目
[RikkaHub](https://github.com/rikkahub/rikkahub) 二次开发而来，感谢其打下的坚实基础。

## 📄 许可证

本项目基于 [GNU Affero General Public License v3.0](LICENSE) (AGPL-3.0) 许可证开源。