plugins {
    `kotlin-dsl`
}

repositories {
    google()
    mavenCentral()
}

dependencies {
    implementation("com.android.tools.build:gradle:8.5.0")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:2.1.21")
    implementation("org.jetbrains.kotlin:compose-compiler-gradle-plugin:2.1.21")
}