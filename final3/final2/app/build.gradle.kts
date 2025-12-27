plugins {
    // build.gradle.kts (Module :app)


        id("com.google.devtools.ksp") version "1.9.22-1.0.17" // 檢查或加上這行


    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.example.final2"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.final2"
        minSdk = 24
        targetSdk = 36
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    // build.gradle.kts (Module :app)
//1225
    implementation("androidx.work:work-runtime-ktx:2.9.0")
 //1225       // --- ↓↓ 把下面這幾行加進去 ↓↓ ---
        val room_version = "2.6.1"
        implementation("androidx.room:room-runtime:$room_version")
        ksp("androidx.room:room-compiler:$room_version") // 注意這裡是 ksp
        implementation("androidx.room:room-ktx:$room_version")
        // --- ↑↑ 加到這裡 ↑↑ ---

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}