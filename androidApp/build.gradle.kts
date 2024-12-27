plugins {
    id("com.android.application")
    kotlin("plugin.serialization") version "1.9.25"
    kotlin("android")
}

android {
    namespace = "com.picprogress"
    compileSdk = 34
    defaultConfig {
        applicationId = "com.picprogress"
        minSdk = 26
        targetSdk = 34
        versionCode = 12
        versionName = "1.0.6"
    }
    buildFeatures {
        compose = true
        dataBinding = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.15"
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
        getByName("debug") {
            applicationIdSuffix = ".dev"
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

dependencies {
    implementation(project(":shared"))
    implementation("androidx.compose.ui:ui:1.7.6")
    implementation("androidx.compose.ui:ui-tooling:1.7.6")
    implementation("androidx.compose.ui:ui-tooling-preview:1.7.6")
    implementation("androidx.compose.ui:ui-text-google-fonts:1.7.6")
    implementation("androidx.compose.foundation:foundation:1.7.6")
    implementation("androidx.compose.material3:material3:1.3.1")
    implementation("androidx.compose.material:material:1.7.6")
    implementation("androidx.constraintlayout:constraintlayout-compose:1.1.0")

    implementation("androidx.activity:activity-compose:1.9.3")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.7")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.8.7")
    implementation("androidx.navigation:navigation-compose:2.8.5")
    implementation("com.google.accompanist:accompanist-navigation-material:0.33.2-alpha")
    implementation("com.google.accompanist:accompanist-permissions:0.31.5-beta")
    implementation("io.insert-koin:koin-android:3.4.2")
    implementation("io.insert-koin:koin-androidx-compose:3.4.5")
    implementation("io.coil-kt:coil-compose:2.4.0")

    implementation("androidx.camera:camera-camera2:1.4.1")
    implementation("androidx.camera:camera-lifecycle:1.4.1")
    implementation("androidx.camera:camera-view:1.4.1")
    implementation("androidx.camera:camera-extensions:1.4.1")
    implementation("androidx.camera:camera-core:1.4.1")
    implementation("androidx.core:core-splashscreen:1.0.1")
    implementation("androidx.documentfile:documentfile:1.0.1")
    implementation("androidx.constraintlayout:constraintlayout:2.2.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("com.github.bumptech.glide:glide:4.16.0")
    implementation("androidx.exifinterface:exifinterface:1.3.7")
    implementation("com.google.android.play:review-ktx:2.0.2")
}