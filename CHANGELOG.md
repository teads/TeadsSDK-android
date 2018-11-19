# Changelog

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
