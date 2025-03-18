// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    //Firebase Google Services
    id("com.google.gms.google-services") version "4.4.2" apply false
    //Firebase Crashlytics
    id("com.google.firebase.crashlytics") version "3.0.3" apply false
    //Dager Hilt DI
    id("com.google.dagger.hilt.android") version "2.55" apply false

    kotlin("plugin.serialization") version "2.1.10" apply false
}