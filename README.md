# TeadsSDK-android

Teads allows you to integrate a single SDK into your app, and serve premium branded "outstream" video ads from Teads SSP ad server. This sample app includes the Teads Android Library and is showing integration examples.

## Requirements
* Android minimum SDK version 19 or higher
* Android compile SDK version 28 or higher
* Android Gradle plugin version 4.2.2 or higher
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
    implementation("tv.teads.sdk.android:sdk:5.1.10@aar") {
        transitive = true
    }
}
```


## Integration Documentation
Integration instructions are available on [Teads SDK Documentation](https://support.teads.tv/support/solutions/articles/36000314755).

## Certifications

Teads SDK supports the [IAB](https://www.iabcertification.com/) [Open Measurement](https://iabtechlab.com/standards/open-measurement-sdk/) SDK, also known as OM SDK. The OM SDK brings transparency to the advertising world, giving a way to standardize the viewability and verification measurement for the ads served through mobile apps.  Teads is part of the [IAB's compliant companies](https://iabtechlab.com/compliance-programs/compliant-companies/). 

![iab certification badge](images/OMCompliant.png)

## Releases

See [releases here](https://github.com/teads/TeadsSDK-android/releases). 
