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
    compile ('tv.teads.sdk:androidsdk:1.5.12@aar') {
        transitive = true;
    }
}
```


## Integration Documentation
Integration instructions are available on the [wiki](https://github.com/teads/TeadsSDK-android/wiki).

## Changelog

v1.5.12:
- Hotfixes 

v1.5.11:
- Fix issue with reusing TeadsNativeVideo on WebView
- Other general fixes

v1.5.8:
- Implement isVisible(int offset) method in TeadsVideoView

v1.5.7:
- Prevent video from being played while the activity is in background
- Enable TeadsNativeVideo reusing
- ListView and RecyclerView animation fixe
- Other general fixes

v1.5.4:
- Remove Google Play Services from sdk (but steel needed)
- Implement reset() method
- Fixes

v1.4.3:
- Implement GridLayoutManager for RecyclerView
- Fixes

v1.4.2:
- Implement RecyclerView Adapter with LinearLayoutManager
- Implement TeadsVideoView
- Fixes

v1.3.5:
- Add insertionParent config for WebView/inRead/InBoard
- Fix for WebView inRead/inBoard scroll on lolipop


v1.3.2:
- Fix for WebView inRead/inBoard scroll
- External View to report ACTION_MOVE option
- Replace RenderScript to use software solution
- General fixes and improvments

v1.2.4:
- Implement AdFactory
- Fixes and improvements

v1.1.3:
- Minor improvement and fixes

v1.1.2:
- Improve VAST parsing
- Implement new media files validation
- Improve handling of clean functions
- Various fix & improvement

v1.0.8:
- Correct some listeners
- Minor improvements

v1.0.5:
- First version
