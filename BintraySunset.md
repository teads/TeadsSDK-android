**JFrog** announced the Bintray shut down from the May 1st, 2021

https://jfrog.com/blog/into-the-sunset-bintray-jcenter-gocenter-and-chartcenter/

Teads used this service to deliver our Android artifacts through a maven repository. From May 1, this repository will no longer be available and applications importing our artifacts will no longer build.

The following dependencies are impacted:
- **sdk**
- **adapterhelper**
- **admobadapter**
- **mopubadapter**
- **smartadapter**


## The Plan
In order to manage the transition, Teads decided to migrate to JFrog Artifactory.
All Teads Android assets have been migrated to a new repository. This concerns both our latest SDK version and all previous versions since v4.

The only thing to do to continue using Teads libraries is to replace, in the gradle file, the old maven repository url: 
```groovy
maven { url "http://dl.bintray.com/teads/TeadsSDK-android" }
```


by the new one:
```groovy
maven { url "https://teads.jfrog.io/artifactory/SDKAndroid-maven-prod" }
```

