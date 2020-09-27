import Dependencies.androidX
import Dependencies.designComponent
import Dependencies.github
import Dependencies.google
import Dependencies.lifecycle
import Dependencies.retrofit
import Dependencies.test
import org.jetbrains.kotlin.config.KotlinCompilerVersion

//Import these lines
import org.gradle.kotlin.dsl.kotlin
import org.jetbrains.kotlin.storage.CacheResetOnProcessCanceled.enabled

plugins {
    id("com.android.application")
//    id("com.google.gms.google-services")
    kotlin("android")
    kotlin("kapt")
    kotlin("android.extensions")
    id("kotlin-android")
}

android {
    compileSdkVersion(29)
    defaultConfig {
        minSdkVersion(17)
        targetSdkVersion(29)
        versionCode = 1
        versionName = "1.0"
        multiDexEnabled = true
        viewBinding {
            isEnabled = true
        }
        androidExtensions {
            isExperimental = true
        }

        testInstrumentationRunner = Dependencies.Android.testInstrumentationRunner
    }
    kapt {
        generateStubs = true
    }
    dexOptions {
        incremental = false
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }

    lintOptions {
        isCheckReleaseBuilds = false
        // Or, if you prefer, you can continue to check for errors in release builds,
        // but continue the build even when errors are found:
        isAbortOnError = false
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(kotlin("stdlib-jdk7", KotlinCompilerVersion.VERSION))
    implementation("com.facebook.android:facebook-share:[5,6)")

    designComponent()
    lifecycle()
    androidX()
    retrofit()
    google()
    github()
    test()
}
