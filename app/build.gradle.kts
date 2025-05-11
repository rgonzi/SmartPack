plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    //Firebase Google Services
    id("com.google.gms.google-services")
    // Crashlytics Gradle plugin
    id("com.google.firebase.crashlytics")

    kotlin("plugin.serialization")
    //Dagger Hilt DI
    id("com.google.dagger.hilt.android")
    id("kotlin-kapt")
    //Dokka
    id("org.jetbrains.dokka")
}

tasks.dokkaHtml {
    outputDirectory.set(layout.buildDirectory.dir("documentation"))
}

android {
    namespace = "com.inovatech.smartpack"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.inovatech.smartpack"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    //Navigation compose
    implementation(libs.androidx.navigation.compose)
    //Serialization
    implementation(libs.kotlinx.serialization.json)

    //Firebase BoM
    implementation(platform(libs.firebase.bom))
    //Firebase Analytics
    implementation(libs.firebase.analytics)
    //Firebase Crashlytics
    implementation(libs.firebase.crashlytics)

    // Retrofit
    implementation(libs.retrofit)
    // Gson converter
    implementation(libs.converter.gson)
    //OkHttp
    implementation (libs.okhttp3.logging.interceptor)

    //Criptografia per a EncryptedSharedPreferences
    implementation(libs.androidx.security.crypto)
    implementation(libs.core.ktx)

    //Dagger Hilt DI
    implementation (libs.hilt.android)
    kapt (libs.hilt.compiler)
    implementation(libs.androidx.hilt.navigation.compose)

    //SplashScreen
    implementation("androidx.core:core-splashscreen:1.0.1")

    testImplementation(libs.junit)
    testImplementation (libs.kotlinx.coroutines.test)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // For instrumentation tests
    androidTestImplementation  (libs.hilt.android.testing)
    kaptAndroidTest (libs.hilt.compiler)

    // For local unit tests with hilt
    testImplementation (libs.hilt.android.testing)
    kaptTest (libs.hilt.compiler)

    //Mocking amb Mockito
    testImplementation(libs.mockito.core)
    testImplementation(libs.mockito.kotlin)
    androidTestImplementation(libs.mockito.android)
    testImplementation(kotlin("test"))
}

kapt {
    correctErrorTypes = true
}