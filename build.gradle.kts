// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    id("com.google.gms.google-services") version "4.4.1" apply false
}

// OneDrive convierte algunos archivos generados en puntos de reanálisis y Gradle
// no puede tratarlos como archivos normales. En Windows, las compilaciones se
// generan fuera de OneDrive; en otros sistemas se conserva la ubicación estándar.
val localAppData = System.getenv("LOCALAPPDATA")
if (!localAppData.isNullOrBlank()) {
    val externalBuildRoot = file("$localAppData/AndroidBuilds/${rootProject.name}")
    layout.buildDirectory.set(externalBuildRoot.resolve("root"))
    subprojects {
        layout.buildDirectory.set(externalBuildRoot.resolve(project.name))
    }
}
