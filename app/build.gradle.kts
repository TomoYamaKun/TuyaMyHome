plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {

    namespace = "com.tuya.myhome.papa"

    compileSdk = 34


    defaultConfig {

        applicationId = "com.tuya.myhome.papa"

        minSdk = 23

        targetSdk = 34

        versionCode = 1

        versionName = "1.01-05"


        testInstrumentationRunner =
            "androidx.test.runner.AndroidJUnitRunner"


        ndk {

            abiFilters += listOf(
                "armeabi-v7a",
                "arm64-v8a"
            )
        }
    }


    buildTypes {

        debug {

            isMinifyEnabled = false
        }


        release {

            isMinifyEnabled = false


            proguardFiles(

                getDefaultProguardFile(
                    "proguard-android-optimize.txt"
                ),

                "proguard-rules.pro"
            )
        }
    }


    compileOptions {

        sourceCompatibility =
            JavaVersion.VERSION_11

        targetCompatibility =
            JavaVersion.VERSION_11
    }


    kotlinOptions {

        jvmTarget = "11"
    }


    packaging {

        jniLibs {

            pickFirsts += setOf(

                "lib/armeabi-v7a/libc++_shared.so",

                "lib/arm64-v8a/libc++_shared.so"
            )
        }
    }
}


configurations.all {

    exclude(

        group = "com.thingclips.smart",

        module = "thingsmart-modularCampAnno"
    )
}


dependencies {

    // ============================================================
    // Local AAR
    // ============================================================

    implementation(

        fileTree(
            mapOf(
                "dir" to "libs",
                "include" to listOf("*.aar")
            )
        )
    )


    // ============================================================
    // AndroidX
    // ============================================================

    implementation(
        "androidx.core:core-ktx:1.12.0"
    )

    implementation(
        "androidx.appcompat:appcompat:1.6.1"
    )

    implementation(
        "com.google.android.material:material:1.11.0"
    )

    implementation(
        "androidx.constraintlayout:constraintlayout:2.1.4"
    )


    // ============================================================
    // Tuya Smart Life App SDK
    // ============================================================

    implementation(
        "com.thingclips.smart:thingsmart:7.8.0"
    )


    // ============================================================
    // Tuya Required Dependencies
    // ============================================================

    implementation(
        "com.alibaba:fastjson:1.1.67.android"
    )

    implementation(
        "com.squareup.okhttp3:okhttp-urlconnection:3.14.9"
    )


    // ============================================================
    // Test
    // ============================================================

    testImplementation(
        "junit:junit:4.13.2"
    )


    androidTestImplementation(
        "androidx.test.ext:junit:1.1.5"
    )


    androidTestImplementation(
        "androidx.test.espresso:espresso-core:3.5.1"
    )
}