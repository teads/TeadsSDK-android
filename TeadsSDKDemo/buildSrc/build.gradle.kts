plugins {
    `kotlin-dsl`
}

repositories {
    google()
    mavenCentral()
}

dependencies {
    implementation("com.android.tools.build:gradle:8.6.0")
    // Pinned to 1.9.24 to simulate a publisher on that Kotlin version.
    // The compose-compiler-gradle-plugin does NOT exist for 1.9.x — we use
    // the legacy composeOptions { kotlinCompilerExtensionVersion = ... } in the app module instead.
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.24")
}