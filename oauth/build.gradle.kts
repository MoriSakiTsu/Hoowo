plugins {
    id("hoowo.android.library")
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "io.github.moriskakitsu.oauth"
}

dependencies {
    api(libs.okhttp)

    implementation(libs.androidx.browser)
    implementation(libs.androidx.core.ktx)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.cio)

    testImplementation(libs.junit)
}
