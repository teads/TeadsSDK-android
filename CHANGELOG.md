# Changelog

v2.2.8
- minor bug fix

v2.2.7
- webview reuse enchancement
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
