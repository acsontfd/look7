plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.look7"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.look7"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
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
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation ("com.airbnb.android:lottie:3.4.0")
        // ... other androidx dependencies

        // add the dependency for the Google AI client SDK for Android
        implementation("com.google.ai.client.generativeai:generativeai:0.6.0")

        // Required for one-shot operations (to use `ListenableFuture` from Guava Android)
        implementation("com.google.guava:guava:31.0.1-android")

        // Required for streaming operations (to use `Publisher` from Reactive Streams)
        implementation("org.reactivestreams:reactive-streams:1.0.4")


}