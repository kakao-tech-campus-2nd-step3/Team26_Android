plugins {
    id("wouldyouin.android.library")
    id("wouldyouin.android.dependency")
    id("kotlin-kapt")
    id("kotlin-parcelize")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "org.ktc2.cokaen.wouldyouin.data"

    defaultConfig {
        consumerProguardFiles("consumer-rules.pro")
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


}

dependencies {
    implementation(project(":core"))
    implementation(libs.gson)

    implementation(libs.transport.runtime)
    androidTestImplementation(libs.junit.v412)
    kapt(libs.google.dagger.hilt.compiler)
    implementation(libs.google.dagger.hilt.android)

    implementation(libs.androidx.room.common)
    implementation(libs.androidx.room.ktx)
    implementation(libs.room.runtime)
    kapt(libs.room.compiler)

    implementation(libs.androidx.security.crypto.ktx)

}