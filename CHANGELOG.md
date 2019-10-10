# Changelog

### v4.3.2
_2019-10-09_
- `setAdContainerView` method in `CustomAdView` and `InReadAdView` is now **deprecated**.
- Improvement on scroller creative
- Add `PoolConfig` class in proguard consumer rules

### v4.3.0
_2019-09-19_
- `adContainerId` is now **deprecated** on mediation adapter, the adapter now clean itself when asked by the mediation SDK to prevent leakage.
- `TeadsWebViewClientOverride` is now **deprecated**. The SDK now handle `WebView#onRenderProcessGone` correctly to clean the ad automatically.
- Report proper error when client is offline while loading a new ad in SDK debug mode.
- Fix and minors improvements.

### v4.2.4
_2019-06-11_
- Fix and minors improvements
 
### v4.2.3
_2019-04-04_
- HTTPS is now forced by default for all Teads trackings
- Add TeadsWebViewClientOverride to let the host application handle the Chrome crash. 
- Fix and minors improvements

### v4.2.1
_2019-03-05_
- New `closeFullscreen` API 
- Fix and minors improvements

### v4.2.0
_2019-02-14_
- Add the method `setAdContainerView` in the InReadAdView to provide the ad container View to Teads in order to provide accurate stats and optimize your revenue
- Add aditionnal log in the logcat to help find if the ad is hidden by an overlay and doesn’t start
- Improve VPAID performances
- Expose SDK version
- Fix and minors improvements

**Breaking change: The `InReadAdView#setAdContainerView` need to be implemented to have correct AdCall metrics**

### v4.1.2
_2018-12-06_
- Disable SDK for API under 19
- Fix ad view player size
- Improve performances

### v4.1.1
_2018-11-19_
- fix `CustomAdView` & `InReadAdView` state restoration
- fix `onAdVolumeChange` not fired on `TeadsListener` 

### v4.1.0
_2018-10-23_
- improve load performance
- improve VPAID creative support
- add userConsent api for MoPub and AdMob adapters

### v4
- Major API change. The SDK is now view based. The view can be added to your layout or inside a ViewHolder very easily **without** having to call unecessary method such as `onResume`. A migration guide will be published before final release.
- New ad format supported and improved VPAID support
- New logic behind the hood to reduce SDK update count
- Official mediation adapter for AdMob and MoPub
- GDPR compliant

-------

[v2 Changelog here](https://github.com/teads/TeadsSDK-android/blob/v2.5.12/CHANGELOG.md)
