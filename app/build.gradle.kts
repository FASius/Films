@file:Suppress("UnstableApiUsage")

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    kotlin("kapt")
    alias(libs.plugins.dagger.hilt)
}

fun getApiKey(): String {
    val items = HashMap<String, String>()
    val localPropertiesFile = rootProject.file("local.properties")

    localPropertiesFile.forEachLine {
        items[it.substringBefore('=')] = it.substringAfter('=')
    }

    return items["api.key"] ?: throw NoSuchElementException()
}

android {
    namespace = "com.example.films"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.example.films"
        minSdk = 23
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        resValue("string", "api_key", getApiKey())
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(libs.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.material)

    // coroutines
    implementation(libs.kotlinx.coroutines)

    // navigation component
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)

    // lifecycle
    implementation(libs.androidx.lifecycle)

    // room
    implementation(libs.room.runtime)
    kapt(libs.room.compiler)
    implementation(libs.room.ktx)

    // paging library
    implementation(libs.paging.library)

    // retrofit
    implementation(libs.retrofit2.retrofit)
    implementation(libs.retrofit2.gson.converter)

    //hilt
    kapt(libs.hilt.compiler)
    implementation(libs.hilt)

    // glide
    implementation(libs.glide)

    // gson
    implementation(libs.gson)

    // view binding delegate
    implementation(libs.viewbinding.delegate)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
}

kapt {
    correctErrorTypes = true
}