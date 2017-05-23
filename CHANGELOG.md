# Changelog
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

----------
### Old version (SDK v1.x)

v1.6.7:
- Fix two issues related to db and progressBar

v1.6.6:
- Fix an issue related to the tracking user agent 

v1.6.5:
- Fix an issue related to WebView clean

v1.6.4:
- Improve handle of new Play Services version
- Improve backward compatibility check

v1.6.3:
- New `TeadsConfiguration.preventWebViewAutoClean` to prevent WebView Ad to be auto cleaned by Teads SDK
- Fixes

v1.6.1:
- Drop support for RichHtml. v2.1 will add support for VPAID content
- Minor Webview improvements

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
