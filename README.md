# TeadsSDK-android

Teads allows you to integrate a single SDK into your app, and serve premium branded "outstream" video ads from Teads SSP ad server. This demo app includes the Teads Android Library and is showing integration examples.

## Run the sample app
Clone this repository to your favorite folder, and open it with Android Studio. Once Android Studio and gradle build is finished, run the app.

## Download the Teads SDK Android library

The Teads SDK is distributed as a gradle dependencie that you have to add to your gradle file. It include everything you need to serve "outstream" video ads.

For complete Integration documentation, [see gradle integration](https://github.com/teads/TeadsSDK-android/wiki/Integration-gradle)

**Short integration using gradle :**
Add our Maven Repository and the dependencie : 
```groovy
android {
    ...
    packagingOptions {
        exclude 'META-INF/NOTICE'
        exclude 'META-INF/LICENSE'
    }
}

repositories{
    maven {
        url  "http://dl.bintray.com/teads/TeadsSDK-android"
    }
}
dependencies {
    compile ('tv.teads.sdk:androidsdk:1.0.8@aar') {
        transitive = true;
    }
}
```


## Integration Documentation
Integration instructions are available on the [wiki](https://github.com/teads/TeadsSDK-android/wiki).

## Changelog
v1.0.8:
- Correct some listeners
- Minor improvements

v1.0.5:
- First version
