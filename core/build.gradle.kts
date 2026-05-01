plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.mivuelto.core"
    compileSdk = 34
    defaultConfig {
        minSdk = 26
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
    implementation(libs.androidx.core.ktx)
    api(libs.kotlinx.coroutines.android)
    api(libs.androidx.compose.constraintlayout)
    api(libs.androidx.activity.compose)
    api(libs.androidx.navigation.compose)

    // Dagger Hilt
    api(libs.hilt.android)
    ksp(libs.hilt.compiler)

    // Retrofit
    api(libs.retrofit.core)
    api(libs.retrofit.gson)

    // Room
    api(libs.room.runtime)
    api(libs.room.ktx)

    // Architecture Components
    api(libs.androidx.lifecycle.runtime.ktx)
    api(libs.androidx.lifecycle.viewmodel.ktx)
    api(libs.androidx.lifecycle.viewmodel.compose)
}
