# TeadsSDK-android

Teads allows you to integrate a single SDK into your app, and serve premium branded "outstream" video ads from Teads SSP ad server. This iOS Demo App includes the Teads Android Library and is showing integration examples.

## Run the sample app

## Download the Teads SDK Android library

The Teads SDK is distributed as a gradle dependencies that you have to add to your gradle file. It includes everything you need to serve "outstream" video ads.

For complete Integration documentation, [see gradle integration](https://github.com/teads/TeadsSDK-android/wiki/Integration-gradle)

**Short integration using gradle :**
Add our Maven Repository and the dependencies : 
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
    compile 'tv.teads.sdk:androidsdk:1.0.1'
}
```


## Integration Documentation
Integration instructions are available on the [wiki](https://github.com/teads/TeadsSDK-android/wiki).

## Changelog
v1.0.2:
- First version

## Roadmap
v1.1.0 : Ad Factory