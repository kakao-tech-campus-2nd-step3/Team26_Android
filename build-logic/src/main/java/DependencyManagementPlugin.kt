import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.project

class DependencyManagementPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            configureDependencies()
        }
    }
}

internal fun Project.configureDependencies() {
    val libs = getLibs()
    dependencies {
        "implementation"(libs.findLibrary("androidx.appcompat").get())
        "implementation"(libs.findLibrary("material").get())
        "implementation"(libs.findLibrary("androidx.activity").get())
        "implementation"(libs.findLibrary("androidx.constraintlayout").get())

        // Lifecycle libraries
        "implementation"(libs.findLibrary("androidx.lifecycle.viewmodel.ktx").get())
        "implementation"(libs.findLibrary("androidx.lifecycle.livedata.ktx").get())
        "implementation"(libs.findLibrary("androidx.lifecycle.runtime.ktx").get())
        "implementation"(libs.findLibrary("androidx.lifecycle.viewmodel.savedstate").get())

        // Other libraries
        "implementation"(libs.findLibrary("androidx.core.ktx").get())
        "implementation"(libs.findLibrary("squareup.retrofit").get())
        "implementation"(libs.findLibrary("bumptech.glide").get())
        "implementation"(libs.findLibrary("com.tbuonomo.dotsindicator").get())
        "implementation"(libs.findLibrary("philjay.mpandroidchart").get())

        // Testing dependencies
        "testImplementation"(libs.findLibrary("junit").get())
        "androidTestImplementation"(libs.findLibrary("androidx.junit").get())
        "androidTestImplementation"(libs.findLibrary("androidx.espresso.core").get())

        "implementation"(project(":core"))
    }
}