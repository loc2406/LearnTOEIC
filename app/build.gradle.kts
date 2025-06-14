plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.compose.compiler)
    id("kotlin-kapt")
    id("androidx.navigation.safeargs.kotlin")
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.locnguyen.toeicexercises"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.locnguyen.toeicexercises"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
    buildFeatures {
        compose = true
        viewBinding = true
        dataBinding = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
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
    implementation(libs.androidx.appcompat)
    implementation(libs.firebase.database)
    implementation(libs.firebase.auth.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation(libs.material) //bottom nav
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.recyclerview)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.cardview)
    implementation(libs.glide)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    //retrofit
    implementation(libs.gson)
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.converter.scalars)
    implementation(libs.adapter.rxjava2)
    implementation(libs.logging.interceptor)

    //splash screen
    implementation (libs.androidx.core.splashscreen)

    //room
//    implementation(libs.androidx.room.runtime)
//    kapt(libs.androidx.room.compiler)
//    implementation(libs.androidx.room.ktx)
//    androidTestImplementation(libs.androidx.room.testing)

    //lifecycle
    implementation(libs.androidx.lifecycle.extensions)
    implementation(libs.androidx.lifecycle.common.java8)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)

    implementation(libs.translate) // dịch văn bản bằng mlkit
    implementation(libs.text.recognition) // nhận dạng văn bản chữ latin
    implementation(libs.text.recognition.chinese) // nhận dạng văn bản tiếng trung
    implementation(libs.text.recognition.japanese)  // nhận dạng văn bản tiếng nhật
    implementation(libs.text.recognition.korean)  // nhận dạng văn bản tiếng Hàn
    implementation(libs.play.services.mlkit.text.recognition.common) // Đọc

    //flash card
//    implementation(libs.card.stack.view)

    //circle progress bar
    implementation(libs.circularprogressbar)

    //gg ads
    implementation(libs.play.services.ads)

    // circle indicator
    implementation(libs.circleindicator)

    // kotlin coroutine
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)

    //lifecycle
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)

    //các thư viện không được chuyển sang kiểu khai báo mới vì sẽ không nhận
    implementation("com.github.yarolegovich:DiscreteScrollView:1.5.1")
        // get Image from gallery
        implementation("io.github.ParkSangGwon:tedimagepicker:1.6.1")

    // upload ảnh lên cloud qua API
    implementation("com.cloudinary:cloudinary-android:3.0.2")
    implementation("com.squareup.okhttp3:okhttp:4.9.1")
}