import org.jetbrains.compose.resources.ResourcesExtension
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinSerialization)
}

compose.resources {
    publicResClass = true
    packageOfResClass = "com.ulyup.tier_list.resources"
    generateResClass = ResourcesExtension.ResourceClassGeneration.Always
}

kotlin {
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser {
            commonWebpackConfig {
                devServer = (devServer ?: KotlinWebpackConfig.DevServer()).apply {
                    port = 8081
                    proxy = mutableListOf(
                        KotlinWebpackConfig.DevServer.Proxy(
                            // Keep "/uploads" in sync with LocalImageStorage.URL_PREFIX (server module)
                            context = mutableListOf("/api", "/uploads"),
                            target = "http://localhost:8080",
                        )
                    )
                }
            }
        }
        binaries.executable()
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation(libs.ktor.clientCore)
            implementation(libs.ktor.clientContentNegotiation)
            implementation(libs.ktor.clientJs)
            implementation(libs.ktor.clientLogging)
            implementation(libs.ktor.clientSerializationJson)
            implementation(libs.kotlinx.serializationJson)
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.composeViewmodel)
            implementation(libs.coil.compose)
            implementation(libs.coil.networkKtor3)
            implementation(libs.filekit.core)
            implementation(libs.filekit.dialogs)
            implementation(libs.filekit.dialogsCompose)
            implementation(libs.navigation.compose)
            implementation(projects.shared)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}


