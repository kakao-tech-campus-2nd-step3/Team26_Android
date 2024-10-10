plugins {
    id("wouldyouin.android.library")
    id("wouldyouin.android.dependency")
    id("kotlin-kapt")
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
}