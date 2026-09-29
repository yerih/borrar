import java.util.Properties

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.mivuelto.core.data"
    compileSdk = 34
    defaultConfig {
        minSdk = 26
        // Base URL del api-gateway (ver documentation/"API corpocredit"/API.md § Convenciones).
        // Orden de precedencia:
        //   1. -PapiBaseUrl=... por línea de comandos
        //   2. apiBaseUrl=... en gradle.properties (raíz o ~/.gradle)
        //   3. apiBaseUrl=... en local.properties (gitignored, por máquina — ver constitution)
        //   4. default: emulador apuntando a localhost:8080
        // NetworkModule la lee vía BuildConfig.BASE_URL, no hay URLs hardcodeadas en código.
        val localProperties = Properties().apply {
            rootProject.file("local.properties").takeIf { it.exists() }
                ?.inputStream()?.use { load(it) }
        }
        val apiBaseUrl = providers.gradleProperty("apiBaseUrl").orNull
            ?: localProperties.getProperty("apiBaseUrl")
            ?: "http://10.0.2.2:8080/"
        buildConfigField("String", "BASE_URL", "\"$apiBaseUrl\"")
    }
    buildFeatures {
        buildConfig = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    kotlinOptions {
        jvmTarget = "21"
    }
}

dependencies {
    implementation(project(":core"))
    implementation(libs.retrofit.core)
    implementation(libs.retrofit.gson)
    implementation(libs.okhttp.logging)
    implementation(libs.okhttp.profiler)
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    // Unit test dependencies
    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.turbine)
    testImplementation(libs.mockk)
    testImplementation(libs.hilt.android.testing)
    kspTest(libs.hilt.android.compiler)
}
