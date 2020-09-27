import Dependencies.AndroidX.appcompat
import Dependencies.AndroidX.constraintlayout
import Dependencies.AndroidX.legacySupport
import Dependencies.Github.glide
import Dependencies.Github.glideCompiler
import Dependencies.Github.viewPagerIndicator
import Dependencies.Google.androidDagger
import Dependencies.Google.dagger
import Dependencies.Google.daggerSuppress
import Dependencies.Google.kaptDagger
import Dependencies.Google.material
import Dependencies.Lifecycle.lifecycleCompiler
import Dependencies.Lifecycle.lifecycleExtensions
import Dependencies.Lifecycle.lifecycleRuntime
import Dependencies.Lifecycle.lifecycleViewModel
import Dependencies.Test.testEspresso
import Dependencies.Test.testRunner
import Dependencies.Versions.appcompatVersion
import Dependencies.Versions.constraintLayoutVersion
import Dependencies.Versions.daggerVersion
import Dependencies.Versions.glideVersion
import Dependencies.Versions.junitVersion
import Dependencies.Versions.lifecycleVersion
import Dependencies.Versions.testEspressoVersion
import Dependencies.Versions.testRunnerVersion
import Dependencies.Versions.viewPagerIndicatorVersion
import org.gradle.api.artifacts.dsl.DependencyHandler

object Dependencies {
    object Android {
        const val minSdkVersion = 17
        const val targetSdkVersion = 28
        const val compileSdkVersion = 28
        const val versionCode = 1
        const val versionName = "1.0"
        const val testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    object Versions {
        const val constraintLayoutVersion = "2.0.0-alpha3"
        const val appcompatVersion = "1.0.0"
        const val viewPagerIndicatorVersion = "2.4.1"
        const val junitVersion = "4.13"
        const val testRunnerVersion = "1.2.0"
        const val testEspressoVersion = "3.2.0"
        const val lifecycleVersion = "2.2.0"
        const val glideVersion = "4.10.0"
        const val daggerVersion = "2.12"
    }

    object Retrofit {
        private const val version = "2.7.1"
        const val retrofit = "com.squareup.retrofit2:retrofit:$version"
        const val retrofit_rxjava_adapter = "com.squareup.retrofit2:adapter-rxjava2:$version"
        const val gsonConverter = "com.squareup.retrofit2:converter-gson:$version"
    }

    fun DependencyHandler.retrofit() {
        implementation(Retrofit.retrofit)
        implementation(Retrofit.retrofit_rxjava_adapter)
        implementation(Retrofit.gsonConverter)
    }

    object DesignComponent {
        const val constrainLayout = "androidx.constraintlayout:constraintlayout:$constraintLayoutVersion"
        const val appcompat = "androidx.appcompat:appcompat:$appcompatVersion"
        const val recyclerView = "androidx.recyclerview:recyclerview:$appcompatVersion"
        const val cardView = "androidx.cardview:cardview:$appcompatVersion"
        const val legacy = "androidx.legacy:legacy-support-v4:$appcompatVersion"

    }

    fun DependencyHandler.designComponent() {
        implementation(DesignComponent.constrainLayout)
        implementation(DesignComponent.appcompat)
        implementation(DesignComponent.recyclerView)
        implementation(DesignComponent.cardView)
        implementation(DesignComponent.legacy)

    }

    object Test {
        const val junit = "junit:junit:$junitVersion"
        const val testRunner = "androidx.test:runner:$testRunnerVersion"
        const val testEspresso = "androidx.test.espresso:espresso-core:$testEspressoVersion"
    }

    fun DependencyHandler.test() {
        testImplementation(Test.junit)
        androidTestImplementation(testRunner)
        androidTestImplementation(testEspresso)
    }


    object Lifecycle {
        const val lifecycleExtensions = "androidx.lifecycle:lifecycle-extensions:$lifecycleVersion"
        const val lifecycleViewModel = "androidx.lifecycle:lifecycle-viewmodel:$lifecycleVersion"
        const val lifecycleRuntime = "androidx.lifecycle:lifecycle-runtime:$lifecycleVersion"
        const val lifecycleCompiler = "androidx.lifecycle:lifecycle-compiler:$lifecycleVersion"
    }

    fun DependencyHandler.lifecycle() {
        implementation(lifecycleExtensions)
        implementation(lifecycleViewModel)
        implementation(lifecycleRuntime)
        annotationProcessor(lifecycleCompiler)
    }


    object Github {
        const val viewPagerIndicator =
            "com.github.JakeWharton:ViewPagerIndicator:$viewPagerIndicatorVersion"
        const val glide = "com.github.bumptech.glide:glide:$glideVersion"
        const val glideCompiler = "com.github.bumptech.glide:compiler:$glideVersion"
    }

    fun DependencyHandler.github() {
        implementation(viewPagerIndicator)
        implementation(glide)
        annotationProcessor(glideCompiler)

    }

    object Google {
        const val material = "com.google.android.material:material:1.2.0-alpha06"
        const val dagger = "com.google.dagger:dagger:$daggerVersion"
        const val kaptDagger = "com.google.dagger:dagger-compiler:$daggerVersion"
        const val daggerSuppress = "com.google.dagger:dagger-android-support:$daggerVersion"
        const val androidDagger = "com.google.dagger:dagger-android:$daggerVersion"


    }
    fun DependencyHandler.google() {
        implementation(material)
        implementation(dagger)
        implementation(androidDagger)
        implementation(daggerSuppress)

        kapt(kaptDagger)

    }

    object AndroidX {
        const val appcompat = "androidx.appcompat:appcompat:1.2.0"
        const val legacySupport = "androidx.legacy:legacy-support-v4:1.0.0"
        const val constraintlayout = "androidx.constraintlayout:constraintlayout:2.0.1"
    }
    fun DependencyHandler.androidX() {
        implementation(appcompat)
        implementation(legacySupport)
        implementation(constraintlayout)
    }

    private fun DependencyHandler.implementation(depName: String) {
        add("implementation", depName)
    }

    private fun DependencyHandler.testImplementation(depName: String) {
        add("testImplementation", depName)
    }

    private fun DependencyHandler.androidTestImplementation(depName: String) {
        add("androidTestImplementation", depName)
    }

    private fun DependencyHandler.annotationProcessor(depName: String) {
        add("annotationProcessor", depName)
    }

    private fun DependencyHandler.kapt(depName: String) {
        add("kapt", depName)
    }


}