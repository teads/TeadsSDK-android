plugins {
    `kotlin-dsl`
    id("org.jetbrains.kotlin.jvm") version "1.4.32"
}

dependencies {
    implementation("com.android.tools.build:gradle:3.5.4")
}


repositories {
    gradlePluginPortal()
    google()
    mavenCentral()
}

