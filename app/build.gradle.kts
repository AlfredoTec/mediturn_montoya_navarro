plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("kotlin-kapt")
}

android {
    namespace = "com.tecsup.mediturn"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.tecsup.mediturn"
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        compose = true
    }
}

dependencies {
    // === DataStore: Para almacenar el theme ===
    implementation("androidx.datastore:datastore-preferences:1.1.7")

    // ===== Room (Base de datos local) =====
    val roomVersion = "2.6.1"
    implementation("androidx.room:room-runtime:$roomVersion")
    kapt("androidx.room:room-compiler:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")

    // ===== Lifecycle + ViewModel + StateFlow =====
    val lifecycleVersion = "2.8.6"
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:$lifecycleVersion")

    // ===== Coroutines =====
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1")

    // ===== Core Android =====
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.activity:activity-compose:1.9.2")

    // ===== Jetpack Compose (BOM centralizado) =====
    implementation(platform("androidx.compose:compose-bom:2024.09.02"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material3:material3-window-size-class")

    // ===== Navegación =====
    implementation("androidx.navigation:navigation-compose:2.8.0")

    // ===== Material Icons =====
    implementation("androidx.compose.material:material-icons-extended")

    // ===== Testing =====
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2024.09.02"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")

    // ===== Herramientas de Depuración =====
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    // Gson para TypeConverters - NUEVO ⭐
    implementation("com.google.code.gson:gson:2.10.1")
}
