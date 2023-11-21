plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")

    id("org.jetbrains.kotlin.plugin.serialization") // Apply Kotlin Serialization plugin
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
}

android {
    namespace = "com.example.androidapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.androidapp"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        //The resValue line you add here will create a string resource named
        // GOOGLE_MAPS_API_KEY with a value that is taken from your gradle.properties file.
//        resValue("string", "GOOGLE_MAPS_API_KEY", ((project.findProperty("GOOGLE_MAPS_API_KEY") ?: "No Key").toString()))
        buildConfigField("String", "MAPS_API_KEY", "\"${properties["MAPS_API_KEY"]}\"")
        manifestPlaceholders["MAPS_API_KEY"] = "Any Text Here"


        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true

        }
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
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {


    // add google map dependecy. I'm not sure whether it should be here or not!
    implementation("com.google.android.gms:play-services-maps:18.2.0")
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.activity:activity-compose:1.8.1")
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    //Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    //Kotlin serialization Converter for Retrofit
    implementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:1.0.0")
    // Kotlin Serialization library
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")

    // OkHttp3. is used for handling HTTP requests and responses.
    // The logging interceptor is particularly useful during development as it can log the details of network requests and responses.
    implementation("com.squareup.okhttp3:okhttp:4.11.0")

    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.compose.ui:ui-graphics-android:1.5.4") // Use the latest version

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

//    new

    implementation("androidx.compose.foundation:foundation:1.5.4") // Use the appropriate version
    implementation("androidx.compose.material3:material3:1.1.2")




}