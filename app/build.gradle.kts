plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.mivuelto"
    compileSdk = 34

    signingConfigs {
        create("release") {
            storeFile = file("corpocredit.jks")
            storePassword = "Corpo.123"
            keyAlias = "corpocredit_key"
            keyPassword = "Corpo.123"
        }
    }

    defaultConfig {
        applicationId = "com.mivuelto"
        minSdk = 26
        targetSdk = 34
        versionCode = 3
        versionName = "1.1.2"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }
    }

    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.8"
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
    implementation(project(":core-data"))
    implementation(project(":core-ui"))
    implementation(project(":feature-purchase"))

    implementation(files("libs\\ysdk_5.91.c221d74_24092716.jar"))
    
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.material3)
    
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
}

tasks.register<Exec>("generateReleaseKeystore") {
    val keystoreFile = file("corpocredit.jks")
    onlyIf { !keystoreFile.exists() }
    
    executable = "keytool"
    args(
        "-genkeypair",
        "-v",
        "-keystore", keystoreFile.absolutePath,
        "-alias", "corpocredit_key",
        "-keyalg", "RSA",
        "-keysize", "2048",
        "-validity", "10000",
        "-storepass", "Corpo.123",
        "-keypass", "Corpo.123",
        "-dname", "CN=MiVuelto, OU=Dev, O=CorpoCredit, L=City, S=State, C=AR",
        "-noprompt"
    )
}

tasks.matching { it.name.startsWith("packageRelease") }.configureEach {
    dependsOn("generateReleaseKeystore")
}
