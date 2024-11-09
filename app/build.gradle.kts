plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "edu.utsa.cs3443.skyboltecommerceapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "edu.utsa.cs3443.skyboltecommerceapp"
        minSdk = 21
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
}

buildscript{
    repositories{
        google()
        mavenCentral()
    }

    dependencies{
        classpath(libs.gradle)
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    //Loading button
    implementation(libs.loading.button.android)

    //Glide
    implementation(libs.glide)

    //Circular images
    implementation(libs.circleimageview)

    //View page Indicator
    //implementation(libs.viewpagerindicator)

    //Step Viewer
    implementation(libs.stepview)

    //Android Ktx
    implementation(libs.androidx.navigation.fragment.ktx)

    //Dagger hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)
}

kapt{
    correctErrorTypes = true
}