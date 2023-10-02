enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven(url = "https://teads.jfrog.io/artifactory/SDKAndroid-maven-prod")
        maven(url = "https://teads.jfrog.io/artifactory/SDKAndroid-maven-earlyAccess")
        maven(url = "https://maven.google.com")
        maven(url = "https://s3.amazonaws.com/moat-sdk-builds")

        //Huawei maven repository
        maven(url = "https://developer.huawei.com/repo/")
        maven(url = "https://packagecloud.io/smartadserver/android/maven2")
    }
}

rootProject.name = "TeadsSDK_Demo"
include(":app", ":webviewhelper")