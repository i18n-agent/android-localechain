plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.i18nagent.localechain.sample"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.i18nagent.localechain.sample"
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(project(":locale-chain"))
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
}
