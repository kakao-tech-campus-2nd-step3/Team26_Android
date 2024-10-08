plugins {
    `kotlin-dsl`
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
}

gradlePlugin {
    gradlePlugin {
        plugins {
            register("androidApplication") {
                id = "wouldyouin.android.application"
                implementationClass = "AndroidApplicationConventionPlugin"
            }

            register("androidBase") {
                id = "wouldyouin.android.library"
                implementationClass = "AndroidBaseConventionPlugin"
            }

            register("dependencyManagement") {
                id = "wouldyouin.android.dependency"
                implementationClass = "DependencyManagementPlugin"
            }
        }
    }
}