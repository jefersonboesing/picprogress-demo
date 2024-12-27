plugins {
    kotlin("multiplatform")
    id("dev.mokkery") version "1.9.25-1.7.0"
    id("com.android.library")
    kotlin("plugin.serialization") version "1.9.25"
    id("app.cash.sqldelight") version "2.0.0"
    id("kotlin-parcelize")
}

kotlin {
    android {
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "shared"
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.1")
                implementation("io.ktor:ktor-client-core:2.2.4")
                implementation("io.ktor:ktor-client-content-negotiation:2.2.4")
                implementation("io.ktor:ktor-serialization-kotlinx-json:2.2.4")
                api("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")
                implementation("io.insert-koin:koin-core:3.4.2")
                api("dev.icerock.moko:parcelize:0.9.0")
                api("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val androidMain by getting {
            dependencies {
                implementation("io.ktor:ktor-client-android:2.2.4")
                implementation("app.cash.sqldelight:android-driver:2.0.0-rc02")
                implementation("io.insert-koin:koin-androidx-compose:3.4.5")
                implementation("androidx.constraintlayout:constraintlayout:2.1.4")
                implementation("com.google.android.material:material:1.10.0")
                implementation("com.github.bumptech.glide:glide:4.16.0")
            }
        }
        val androidUnitTest by getting
        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)

            dependencies {
                implementation("io.ktor:ktor-client-darwin:2.2.4")
                implementation("app.cash.sqldelight:native-driver:2.0.0-rc02")
            }
        }
        val iosX64Test by getting
        val iosArm64Test by getting
        val iosSimulatorArm64Test by getting
        val iosTest by creating {
            dependsOn(commonTest)
            iosX64Test.dependsOn(this)
            iosArm64Test.dependsOn(this)
            iosSimulatorArm64Test.dependsOn(this)
        }
    }
}

android {
    namespace = "com.picprogress"
    compileSdk = 33
    defaultConfig {
        minSdk = 26
    }
}

sqldelight {
    databases {
        create("PicProgressDatabase") {
            packageName.set("com.picprogress.cache")
        }
    }
}