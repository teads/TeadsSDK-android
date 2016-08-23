# TeadsSDK-android         [![Build Status](https://jenkins.teadstesting.tv/buildStatus/icon?job=TeadsSDK-android_master)](https://jenkins.teadstesting.tv/job/TeadsSDK-android_master)  



Teads allows you to integrate a single SDK into your app, and serve premium branded "outstream" video ads from Teads SSP ad server. This demo app includes the Teads Android Library and is showing integration examples.

## Run the sample app
Clone this repository to your favorite folder, and open it with Android Studio. Once Android Studio and gradle build is finished, run the app.

## Download the Teads SDK Android library

The Teads SDK is distributed as a gradle dependencie that you have to add to your gradle file. It include everything you need to serve "outstream" video ads.

For complete Integration documentation, [see gradle integration](http://mobile.teads.tv/sdk/documentation/android/android-project-setup/gradle)

**Short integration using gradle :**
Add our Maven Repository and the dependency: 
```groovy

repositories{
    maven {
        url  "http://dl.bintray.com/teads/TeadsSDK-android"
    }
}
dependencies {
    compile ('tv.teads.sdk:androidsdk:2.2.8:fullRelease@aar') {
        transitive = true;
    }
}
```


## Integration Documentation
Integration instructions are available on [Teads SDK Documentation](http://mobile.teads.tv/sdk/documentation/).

## Changelog

See [changelog here](https://github.com/teads/TeadsSDK-android/blob/master/CHANGELOG.md). 
