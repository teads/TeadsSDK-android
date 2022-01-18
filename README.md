> :warning:  **Bintray shut down**: From May 1, Teads dependencies will no longer be available on our old maven repository, [see more](https://github.com/teads/TeadsSDK-android/blob/master/BintraySunset.md).

# TeadsSDK-android

Teads allows you to integrate a single SDK into your app, and serve premium branded "outstream" video ads from Teads SSP ad server. This sample app includes the Teads Android Library and is showing integration examples.

## Run the sample app
Clone this repository to your favorite folder, and open it with Android Studio. Once Android Studio and gradle build is finished, run the app.

## Download the Teads SDK Android library

The Teads SDK is distributed as a gradle dependencie that you have to add to your gradle file. It include everything you need to serve "outstream" video ads.

For complete Integration documentation, [see the documentation](https://support.teads.tv/support/solutions/articles/36000165909)

**Short integration using gradle :**
Add our Maven Repository and the dependency: 
```groovy

repositories{
    maven {
        url  "https://teads.jfrog.io/artifactory/SDKAndroid-maven-prod"
    }
}
dependencies {
    implementation("tv.teads.sdk.android:sdk:4.9.2@aar") {
        transitive = true
    }
}
```


## Integration Documentation
Integration instructions are available on [Teads SDK Documentation](https://support.teads.tv/support/solutions/articles/36000165909).

## Changelog

See [changelog here](https://github.com/teads/TeadsSDK-android/blob/master/CHANGELOG.md). 
