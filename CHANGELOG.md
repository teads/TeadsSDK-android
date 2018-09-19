# Changelog

**v4**
- major API change. The SDK is now view based. The view can be added to your layout or inside a ViewHolder very easily **without** having to call unecessary method such as `onResume`. A migration guide will be published before final release.
- improve VPAID support
- new logic behind the hood to reduce SDK update count
- official mediation adapter (AdMob and MoPub) will be out before final release.
- new ad format supported, more on that soon.
- 4.0.34 allow us to disable the SDK usage remotely
- 4.0.36 bug fixes
- 4.0.37 initial support for AdMob and Mopub + bug fixes
- 4.0.39 improve memory management & other minor improvement
- 4.0.40 minor improvement
- 4.0.43 improve intern stability and delivery
- 4.0.44 reduce memory usage on new instance, improve internal mechanism and third party compatibility
- 4.0.45 improvements and bug fixes
- 4.0.46 improvements

----------
### Old version (SDK v2.x)

v2.5.12
- Bug fixes on custom integration

v2.5.11
- Improvements and bug fixes
 
v2.5.10
- Improvements and bug fixes

v2.5.9
- Add support for brandsafety. If you have news content in your app, you should use `teadsConfiguration.pageUrl = "https://news.com/myArticle";` where the url is you equivalent http url of your article. More information [here](https://mobile.teads.tv/sdk/documentation/android/integration/inread/scrollview#brand-safety). 

v2.5.8
- Remove event service to prevent issue on Android 8.x

v2.5.7
- Disable tracking recovery by default to prevent issue on Android 8.x

v2.5.6
- bug fix and improvements

v2.5.5
- bug fix and improvements

v2.5.4
- bug fix and improvements

v2.5.3
- bug fix and improvements

v2.5.2
- bug fix

v2.5.1
- minSdkVersion = 15 (ICE_CREAM_SANDWICH_MR1 4.0.3)
- optimize OkHttp cancel method (FD)
- improve tracking metrics

v2.4.14
- use ExoPlayer 2

v2.4.13
- improve stability
- improve bad VAST management
- improve deeplink management

v2.4.12
- improve memory management
- improve faulty VPAID creative management
- bug fix

v2.4.11
- Remove WRITE_EXTERNAL_STORAGE permission
- bug fix

v2.4.10
- Support x-javascript MIME type for VPAID creative
- bug fix

v2.4.9
- No need to pass Activity at TeadsAd object anymore, a context is enough 
- bug fix and memory improvement

v2.4.6
- improves memory management  

v2.4.4
- minor improvement and bug fix

v2.4.2
- minor improvement and bug fix

v2.4.1
- Support display VPAID

v2.3.17
- minor bug fix

v2.3.16
- minor bug fix

v2.3.15
- minor bug fix

v2.3.14
- improve impression and start tracking

v2.3.13
- support NestedScrollView

v2.3.12
- remove `android.permission.ACCESS_WIFI_STATE`

v2.3.11
- minor bug fix

v2.3.10
- minor bug fix and improvement

v2.3.9
- better play services management
- minor bug fix

v2.3.7
- add behavior on equalizer click 
- minor bug fix

v2.3.6
- support vpaid creatives
- remove deprecated TeadsVideo object, you should now use TeadsAd 
- minor bug fix

v2.2.16
- support java 8 language features
- fix memory leak


v2.2.15
- fix an issue with the complete tracking

v2.2.14
- Add the possibility to disable application visibility receiver in
  TeadsConfiguration
- leak memory fix on webview container
- bug fix

v2.2.13
- improve tracking reliability
- bug fix

v2.2.11
- funnel improvement
- bug fix

v2.2.10
- brand logo
- new endscreen
- bug fix

v2.2.8
- minor bug fix

v2.2.7
- webview reuse improvement
- minor improvements
- minor bug fix

v2.2.6
- memory leak fix
- minor bug fix

v2.2.5
- add close button on engage format
- minor bug fix 

v2.2.4
- minor bug fix 

v2.2.3
- new engage format
- implement light an dark mode on endscreen
- minor bug fix 

v2.1.4:
- fix memory leaks
- minor bug fix 

v2.1.3:
- fix video not resuming sometimes
- minor bug fix

v2.1.2:
- new behaviors on video touch
- add support for setting maximum height
- minor bug fix

v2.1.0:
- internal dependency change
- use OkHttp 3.2.0
- minor bug fix

v2.0.32:
- improve memory management
- fix issues when cleaning `TeadsVideo` with null views

v2.0.30:
- allow new load() after a no slot on WebViews
- allow option to disable Chrome custom tabs via `teadsConfiguration.disableChromeCustomTab`
- minor bug fix

v2.0.29:
- improve landscape behavior
- minor bug fix

2.0.28:
- Webview fix

v2.0.27:
- change ExoPlayer dependency to prevent conflict
- minor fix

v2.0.25:
- RecyclerView fix 
- minor bug fix and improvements

v2.0.23:
- new ad design and behaviors
- Whole new version of SDK that brings breaking changes. Read [Migration guide from SDK v1.6 to v2](http://mobile.teads.tv/sdk/documentation/android/migration-guide-from-v1-6-x)
