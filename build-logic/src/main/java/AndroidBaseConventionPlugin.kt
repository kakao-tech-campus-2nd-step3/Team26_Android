import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

class AndroidBaseConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.library")
                apply("org.jetbrains.kotlin.android")
                apply("kotlin-kapt")
            }

            extensions.configure<LibraryExtension> {
                configureAndroidCommon(this)
            }

            tasks.withType<KotlinCompile>().configureEach {
                kotlinOptions {
                    jvmTarget = JavaVersion.VERSION_1_8.toString()
                }
            }

            configurations.configureEach {
                resolutionStrategy {
                    force(getLibs().findLibrary("androidx.core.ktx").get())
                    force(getLibs().findLibrary("androidx.appcompat").get())
                    // Add other forced dependencies here
                }
            }
        }
    }
}

internal fun Project.configureAndroidCommon(
    commonExtension: LibraryExtension
) {
    commonExtension.apply {
        compileSdk = 34

        defaultConfig {
            minSdk = 26
            testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        }

        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_1_8
            targetCompatibility = JavaVersion.VERSION_1_8
        }

        buildFeatures {
            dataBinding = true
            buildConfig = true
        }
    }
}

internal fun Project.getLibs(): VersionCatalog {
    return extensions.getByType<VersionCatalogsExtension>().named("libs")
}