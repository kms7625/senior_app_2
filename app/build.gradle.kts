plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.mose.seniorgame"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.mose.seniorgame"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "0.1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
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
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.14"
    }

    // .tflite는 압축하지 않아야 mmap으로 바로 읽을 수 있다(DifficultyModel 참고).
    androidResources {
        noCompress += "tflite"
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.activity:activity-compose:1.9.0")
    implementation(platform("androidx.compose:compose-bom:2024.06.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.navigation:navigation-compose:2.7.7")

    // 온디바이스 AI 난이도 조절(가점 요소, 기획안 12.2). 완전 오프라인 추론이며 모델은
    // tools/train_difficulty_model.py로 로컬에서 미리 학습·변환해 assets/에 번들한다.
    implementation("org.tensorflow:tensorflow-lite:2.16.1")

    // 온보딩/선호 테마 영구 저장(GameSession.kt 참고).
    implementation("androidx.datastore:datastore-preferences:1.1.1")

    testImplementation("junit:junit:4.13.2")

    // 계측 테스트(에뮬레이터/실기기 필요) — 실제 화면 전환·클릭까지 검증한다.
    androidTestImplementation(platform("androidx.compose:compose-bom:2024.06.00"))
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}
