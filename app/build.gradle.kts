plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.example.lensleap"
    compileSdk = 34

    buildFeatures {
        viewBinding=true
    }

    defaultConfig {
        applicationId = "com.example.lensleap"
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
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation("de.hdodenhof:circleimageview:3.1.0")
    implementation(platform("com.google.firebase:firebase-bom:32.8.1"))
    implementation("com.squareup.picasso:picasso:2.8")
    implementation("com.github.bumptech.glide:glide:4.16.0")
    implementation("androidx.media3:media3-exoplayer:1.3.1")
    implementation("androidx.media3:media3-exoplayer-dash:1.3.1")
    implementation("androidx.media3:media3-ui:1.3.1")
//    implementation("com.google.android.exoplayer:exoplayer:r2.19.1")
//    implementation("com.google.android.exoplayer:exoplayer-core:r2.19.1")
//    implementation("com.google.android.exoplayer:exoplayer-dash:r2.19.1")
//    implementation("com.google.android.exoplayer:exoplayer-hls:r2.19.1")
//    implementation("com.google.android.exoplayer:exoplayer-smoothstreaming:r2.19.1")
//    implementation("com.google.android.exoplayer:exoplayer-ui:r2.19.1")

    implementation("com.airbnb.android:lottie:6.4.0")
    implementation(libs.firebase.auth)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.storage)
    implementation(libs.media3.exoplayer)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}