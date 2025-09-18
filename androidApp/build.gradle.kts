plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
//    alias(libs.plugins.gmsGoogleServices)
//    alias(libs.plugins.firebaseCrashlytics)
    alias(libs.plugins.kotlinSerialization)
}

android {
    namespace = "com.highfive.app.android"
    compileSdk = 35
    defaultConfig {
        applicationId = "com.highfive.app.android"
        minSdk = 30
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
    }
    buildFeatures {
        buildConfig = true
        viewBinding = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    flavorDimensions += listOf("environments")
    productFlavors {
        create("dev") {
            dimension = "environments"
            applicationIdSuffix = ".dev"
            buildConfigField("String", "API_BASE_URL", "\"https://dummyjson.com/\"")
        }
        create("uat") {
            dimension = "environments"
            applicationIdSuffix = ".uat"
            buildConfigField("String", "API_BASE_URL", "\"https://dummyjson.com/\"")
        }
        create("prod") {
            dimension = "environments"
            buildConfigField("String", "API_BASE_URL", "\"https://dummyjson.com/\"")
        }
    }
}

dependencies {
    implementation(projects.shared)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.serialization.core)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.androidx.core)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.swiperefreshlayout)
    implementation(libs.ktor.client.core)
//    implementation(platform(libs.google.firebase.bom))
//    implementation(libs.google.firebase.analytics)
//    implementation(libs.google.firebase.crashlytics)
//    implementation(libs.google.firebase.messaging)
//    implementation(libs.google.firebase.config)
    implementation(libs.androidx.work)
    implementation(libs.material)
    constraints {
        implementation("androidx.browser:browser:1.8.0") {
            version { strictly("1.8.0") }
            because("Browser 1.9.0 requires AGP 8.9.1 and compileSdk 36+, we’re on AGP 8.7.3 / compileSdk 35.")
        }
    }
}
