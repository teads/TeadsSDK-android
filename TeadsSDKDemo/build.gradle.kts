//tasks.register("clean", Delete::class) {
//    delete(rootProject.buildDir)
//}

plugins {
    `kotlin-dsl`
    id("org.jetbrains.kotlin.jvm") version "1.4.32"
}

dependencies {
    implementation("com.android.tools.build:gradle:3.5.4")
}

allprojects {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
        maven(url = "https://teads.jfrog.io/artifactory/SDKAndroid-maven-prod")
        maven(url = "https://maven.google.com")
        maven(url = "https://s3.amazonaws.com/moat-sdk-builds")

        //Huawei maven repository
        maven(url = "https://developer.huawei.com/repo/")
    }
}
