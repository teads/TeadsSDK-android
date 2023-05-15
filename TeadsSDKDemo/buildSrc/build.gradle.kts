plugins {
    `kotlin-dsl`
    id("org.jetbrains.kotlin.jvm") version "1.4.32"
}

repositories {
    gradlePluginPortal()
    google()
    mavenCentral()
}

dependencies {
    implementation("com.android.tools.build:gradle:7.0.0") // minimum for your case
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.5.20")
}