<div align="center">
  <h1>Hoowo</h1>

A native Android LLM chat client focused on homework & study assistance for students 🤖💬

Based on [RikkaHub](https://github.com/rikkahub/rikkahub).

[简体中文](README_ZH_CN.md) | [繁體中文](README_ZH_TW.md) | English
</div>

## ✨ Features

- 🎨 Material You Design and 🌙 Dark mode
- 🔄 Multiple AI Provider Support: custom API / URL / models (all OpenAI, Google, Anthropic compatible api)
- 🖼️ Multimodal input support (Image, Text Documentation, PDF, Docx)
- 🖥️ Web access for multi-platform use
- 🛠️ MCP support
- 📝 Markdown Rendering (with code highlighting, Latex formulas, tables, Mermaid)
- 🪾 Message Branching
- 🔍 Search capabilities (Exa, Tavily, Zhipu, LinkUp, Brave, Perplexity, etc.)
- 🧩 Prompt variables (model name, time, etc.)
- 🤖 Agent customization
- 🧠 ChatGPT-like memory feature
- 📝 AI Translation
- 🌐 Custom HTTP request headers and request bodies
- 💌 Silly Tavern character card import

## 💻 Development

This project is developed using [Android Studio](https://developer.android.com/studio).

Technology stack:

- [Kotlin](https://kotlinlang.org/) (Development language)
- [Koin](https://insert-koin.io/) (Dependency Injection)
- [Jetpack Compose](https://developer.android.com/jetpack/compose) (UI framework)
- [DataStore](https://developer.android.com/topic/libraries/architecture/datastore) (Preference data
  storage)
- [Room](https://developer.android.com/training/data-storage/room) (Database)
- [Coil](https://coil-kt.github.io/coil/) (Image loading)
- [Material You](https://m3.material.io/) (UI design)
- [Navigation 3](https://developer.android.com/guide/navigation/navigation-3) (Navigation)
- [Okhttp](https://square.github.io/okhttp/) (HTTP client)
- [kotlinx.serialization](https://github.com/Kotlin/kotlinx.serialization) (JSON serialization)

Build commands:

```bash
./gradlew assembleDebug          # Build debug APK
./gradlew test                   # Run JVM unit tests
./gradlew lint                   # Run Android Lint
```

> [!NOTE]
> If your project path contains non-ASCII characters on Windows, add
> `android.overridePathCheck=true` to `gradle.properties`. Building the APK still
> works, but `./gradlew test` is broken there: the test worker cannot resolve the
> classpath entries and fails with `ClassNotFoundException`. Move the project to an
> ASCII-only path (for example `E:\Projects\Hoowo`) to run the unit tests.

## 🙏 Acknowledgements

Hoowo is a fork of the amazing open-source project
[RikkaHub](https://github.com/rikkahub/rikkahub) by [re-ovo](https://github.com/re-ovo). Thanks for
the great foundation.

## 📄 License

This project is licensed under the [GNU Affero General Public License v3.0](LICENSE) (AGPL-3.0).
