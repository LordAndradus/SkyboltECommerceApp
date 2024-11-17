plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("androidx.navigation.safeargs.kotlin")
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
    alias(libs.plugins.google.gms.google.services)
    id("kotlin-parcelize")
    id("kotlin-kapt")
}

android {
    namespace = "edu.utsa.cs3443.skyboltecommerceapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "edu.utsa.cs3443.skyboltecommerceapp"
        minSdk = 24
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures{
        viewBinding = true
    }
}

buildscript{
    repositories{
        google()
        mavenCentral()

        @Suppress("DEPRECATION")
        jcenter()
    }

    dependencies{
        classpath(libs.hilt.android.gradle.plugin)
        classpath(libs.gradle)
        classpath(libs.kotlin.gradle.plugin)
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.storage)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    //Navigation Component
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    //Loading button
    implementation(libs.loading.button.android)

    //Glide
    implementation(libs.glide)

    //Circular images
    implementation(libs.circleimageview)

    //View page Indicator
    //implementation(libs.dxslin.viewpagerindicator) - Deprecated. Only for version 1
    implementation("com.github.zhpanvip:viewpagerindicator:latestVersion")

    //Step Viewer
    implementation(libs.stepview)

    //Android Ktx
    implementation(libs.androidx.navigation.fragment.ktx)

    //Dagger hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)

    //Firebase
    implementation(libs.firebase.auth)

    //Google play services
    implementation("com.google.android.gms:play-services:12.0.1")

    //Coroutines with firebase teehee
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.5.1")
}

kapt{
    correctErrorTypes = true
}