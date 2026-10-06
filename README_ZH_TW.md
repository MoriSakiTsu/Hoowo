<div align="center">
  <h1>Hoowo</h1>

一個面向學生的原生 Android LLM 聊天客戶端，專注於作業與學習輔助 🤖💬

基於 [RikkaHub](https://github.com/rikkahub/rikkahub) 二次開發。

[简体中文](README_ZH_CN.md) | [繁體中文](README_ZH_TW.md) | [English](README.md)
</div>

## ✨ 功能

- 🎨 Material You 設計與 🌙 深色模式
- 🔄 多 AI 提供商支援：自訂 API / 位址 / 模型（相容所有 OpenAI、Google、Anthropic 格式介面）
- 🖼️ 多模態輸入（圖片、文字文件、PDF、Docx）
- 🖥️ Web 端存取，多平台使用
- 🛠️ MCP 支援
- 📝 Markdown 渲染（程式碼高亮、LaTeX 公式、表格、Mermaid）
- 🪾 訊息分支
- 🔍 聯網搜尋（Exa、Tavily、智譜、LinkUp、Brave、Perplexity 等）
- 🧩 提示詞變數（模型名、時間等）
- 🤖 助手自訂
- 🧠 類 ChatGPT 記憶功能
- 📝 AI 翻譯
- 🌐 自訂請求頭與請求體
- 💌 Silly Tavern 角色卡匯入

## 💻 開發

本專案使用 [Android Studio](https://developer.android.com/studio) 開發。

技術堆疊：

- [Kotlin](https://kotlinlang.org/)（開發語言）
- [Koin](https://insert-koin.io/)（依賴注入）
- [Jetpack Compose](https://developer.android.com/jetpack/compose)（UI 框架）
- [DataStore](https://developer.android.com/topic/libraries/architecture/datastore)（偏好儲存）
- [Room](https://developer.android.com/training/data-storage/room)（資料庫）
- [Coil](https://coil-kt.github.io/coil/)（圖片載入）
- [Material You](https://m3.material.io/)（UI 設計）
- [Navigation 3](https://developer.android.com/guide/navigation/navigation-3)（導航）
- [Okhttp](https://square.github.io/okhttp/)（網路請求）
- [kotlinx.serialization](https://github.com/Kotlin/kotlinx.serialization)（JSON 序列化）

建置命令：

```bash
./gradlew assembleDebug          # 建置 Debug APK
./gradlew test                   # 執行 JVM 單元測試
./gradlew lint                   # 執行 Android Lint
```

> [!IMPORTANT]
> `gradle/wrapper/gradle-wrapper.properties` 中的 Gradle 下載位址指向騰訊雲鏡像
> （`mirrors.cloud.tencent.com/gradle`），而非官方的 `services.gradle.org` —— 因為
> Gradle 本體約 134 MB，而官方位址在中國大陸無法存取。鏡像提供的檔案與官方完全一致。
> 如果你在海外、或鏡像不可用，請把該行改回：
> `https\://services.gradle.org/distributions/gradle-<版本>-bin.zip`
> Gradle 依下載位址分別快取發行版，因此切換後會重新下載一次。

> [!NOTE]
> Windows 上若專案路徑包含非 ASCII 字元，請在 `gradle.properties` 中新增
> `android.overridePathCheck=true`。APK 建置可正常進行，但 `./gradlew test`
> 在該路徑下會失效：測試程序無法解析 classpath 條目並報
> `ClassNotFoundException`。請將專案移動至純 ASCII 路徑
> （例如 `E:\Projects\Hoowo`）再執行單元測試。

## 🙏 致謝

Hoowo 基於 [re-ovo](https://github.com/re-ovo) 的優秀開源專案
[RikkaHub](https://github.com/rikkahub/rikkahub) 二次開發而來，感謝其打下的堅實基礎。

## 📄 授權條款

本專案基於 [GNU Affero General Public License v3.0](LICENSE) (AGPL-3.0) 授權條款開源。