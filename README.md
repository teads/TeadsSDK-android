# TeadsSDK-android

Teads allows you to integrate a single SDK into your app, and serve premium branded "outstream" video ads from Teads SSP ad server. This sample app includes the Teads Android Library and is showing integration examples.

## Requirements 
* Android compile SDK version to 28 or higher
* Build gradle version to v3.2.1 or higher
* AndroidX support ([Migrate to AndroidX](https://developer.android.com/jetpack/androidx/migrate))

## Run the sample app and discover how we integrate our SDK
The best way to see the working integration is to clone this repository, open it with Android Studio. The sample contains multiples kinds of integrations from direct integration to integrations using mediations partners such as AdMob and Mopub.

## Download the Teads SDK Android library

The Teads SDK is distributed as a gradle dependencie that you have to add to your gradle file. It include everything you need to serve "outstream" video ads.

**Short integration using gradle :**
Add our Maven Repository and the dependency: 
```groovy

repositories{
    maven {
        url  "https://teads.jfrog.io/artifactory/SDKAndroid-maven-prod"
    }
}
dependencies {
    implementation("tv.teads.sdk.android:sdk:5.0.4@aar") {
        transitive = true
    }
}
```


## Integration Documentation
Integration instructions are available on [Teads SDK Documentation](https://support.teads.tv/support/solutions/articles/36000314755).

## Releases

See [releases here](https://github.com/teads/TeadsSDK-android/releases). 
