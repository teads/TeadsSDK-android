# =============================================================================
# ProGuard Consumer Rules for Teads Android SDK
# These rules are applied to apps that include the Teads SDK
# =============================================================================

# =============================================================================
# Debugging & Development Support
# =============================================================================

# Keep source file names and line numbers for better debugging
-keepattributes SourceFile,LineNumberTable

# Keep exception information for better error reporting
-keepattributes Exceptions

# Keep generic signature information for better debugging
-keepattributes Signature

# Keep annotation information
-keepattributes Annotation

# Keep inner classes information
-keepattributes InnerClasses

# Keep enclosing method information
-keepattributes EnclosingMethod

# =============================================================================
# WebView & JavaScript Interface
# =============================================================================

# Keep JavaScript interface methods for WebView communication
-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}

# Keep original package names to avoid obfuscation conflicts
-keepnames class tv.teads.sdk.**

# =============================================================================
# Network & SSL Configuration
# =============================================================================

# Keep Socket Factory following https://github.com/square/okhttp/issues/2323#issuecomment-292036362
-keepclassmembers class * implements javax.net.ssl.SSLSocketFactory {
    private final javax.net.ssl.SSLSocketFactory delegate;
}

# =============================================================================
# Public SDK Views & UI Components
# =============================================================================

# Main ad view classes
-keep class tv.teads.sdk.renderer.InReadAdView { *; }
-keep class tv.teads.sdk.renderer.NativeAdView { *; }
-keep class tv.teads.sdk.renderer.MediaView { *; }
-keep class tv.teads.sdk.renderer.MediaScale { *; }
-keep class tv.teads.sdk.renderer.InReadAdViewFactory { *; }
-keep class tv.teads.sdk.AdOpportunityTrackerView { *; }

# =============================================================================
# Public SDK Configuration Classes
# =============================================================================

# Ad placement settings
-keep class tv.teads.sdk.AdPlacementSettings { *; }
-keep class tv.teads.sdk.AdPlacementSettings$Builder { *; }
-keep class tv.teads.sdk.AdPlacementSettings$Companion { *; }
-keep class tv.teads.sdk.AdPlacementSettingsBuilder { *; }

# Ad request settings
-keep class tv.teads.sdk.AdRequestSettings { *; }
-keep class tv.teads.sdk.AdRequestSettings$Builder { *; }
-keep class tv.teads.sdk.AdRequestSettings$Companion { *; }
-keep class tv.teads.sdk.AdRequestSettingsBuilder { *; }

# Mediation settings
-keep class tv.teads.sdk.TeadsMediationSettings { *; }
-keep class tv.teads.sdk.TeadsMediationSettings$Builder { *; }
-keep class tv.teads.sdk.TeadsMediationSettings$Companion { *; }

# User consent and TCF
-keep class tv.teads.sdk.utils.userConsent.TCFVersion { *; }
-keep class tv.teads.sdk.utils.userConsent.TCFVersion$Companion { *; }
-keep class tv.teads.sdk.utils.userConsent.TCFVersionAdapter { *; }

# =============================================================================
# Public SDK Core Classes
# =============================================================================

# Ad loading and core functionality
-keep class tv.teads.sdk.loader.AdSlotVisible { *; }
-keep class tv.teads.sdk.loader.AdLoaderError { *; }
-keep class tv.teads.sdk.loader.AdLoaderResult$AdLoaderError { *; }
-keep class tv.teads.sdk.loader.AdLoaderResult$AdLoaderSuccess { *; }
-keep class tv.teads.sdk.core.AdCore$FullscreenControl { *; }
-keep class tv.teads.sdk.core.model.AssetType { *; }
-keep class tv.teads.sdk.core.TeadsAd { *; }
-keep class tv.teads.sdk.AdRatio { *; }

# =============================================================================
# Public SDK Listeners & Interfaces
# =============================================================================

# InRead ad listeners
-keep class tv.teads.sdk.InReadAdBaseListener { *; }
-keep class tv.teads.sdk.InReadAdBaseListener$DefaultImpls { *; }
-keep class tv.teads.sdk.InReadAdModelListener { *; }
-keep class tv.teads.sdk.InReadAdModelListener$DefaultImpls { *; }
-keep class tv.teads.sdk.InReadAdViewListener { *; }
-keep class tv.teads.sdk.InReadAdViewListener$DefaultImpls { *; }

# Native ad listeners
-keep class tv.teads.sdk.NativeAdListener { *; }
-keep class tv.teads.sdk.NativeAdListener$DefaultImpls { *; }

# =============================================================================
# Public SDK Placement Classes
# =============================================================================

-keep class tv.teads.sdk.InReadAdPlacement { *; }
-keep class tv.teads.sdk.PrebidAdPlacement { *; }
-keep class tv.teads.sdk.PrebidAdPlacement$DefaultImpls { *; }
-keep class tv.teads.sdk.NativeAd { *; }
-keep class tv.teads.sdk.NativeAdPlacement { *; }

# =============================================================================
# Public SDK Main Classes
# =============================================================================

-keep class tv.teads.sdk.TeadsSDK { *; }
-keep class tv.teads.sdk.utils.logger.TeadsLog { *; }
-keep class tv.teads.sdk.utils.ViewUtils { *; }

# =============================================================================
# Mediation & Adapter Support
# =============================================================================

# Teads mediation helper
-keep class tv.teads.sdk.mediation.TeadsHelper { *; }
-keep class tv.teads.sdk.mediation.TeadsHelper$Companion { *; }
-keep class tv.teads.sdk.mediation.TeadsAdapterListener { *; }

# =============================================================================
# Internal SDK Components (Required for Functionality)
# =============================================================================

# Core components
-keep class tv.teads.sdk.core.components.TextComponent { *; }
-keep class tv.teads.sdk.core.components.ImageComponent { *; }
-keep class tv.teads.sdk.core.components.AssetComponent { *; }

# Image management
-keep class tv.teads.sdk.utils.imageManager.ImageDownloader { *; }
-keep class tv.teads.sdk.utils.imageManager.ImageDownloader$ImageDownloaderCallback { *; }
-keep class tv.teads.sdk.utils.imageManager.ImageRequestHandler { *; }
-keep class tv.teads.sdk.utils.imageManager.ImageValidator { *; }
-keep class tv.teads.sdk.utils.imageManager.ImageLoader { *; }
-keep class tv.teads.sdk.utils.imageManager.ImageAction { *; }

# Engine bridges
-keep class tv.teads.sdk.engine.bridges.** { *; }
-keep class tv.teads.sdk.engine.bridges.network.** { *; }

# =============================================================================
# Third-party SDK Integration
# =============================================================================

# Huawei HMS Ads SDK
-keep class com.huawei.hms.ads.** { *; }
-keep interface com.huawei.hms.ads.** { *; }

# =============================================================================
# Combined SDK Package (Teads Integration)
# =============================================================================

# Combined SDK core classes
-keep class tv.teads.sdk.combinedsdk.TeadsAdPlacementContainerView { *; }
-keep class tv.teads.sdk.combinedsdk.TeadsAdPlacementEventName { *; }

# Combined SDK ad placement classes
-keep class tv.teads.sdk.combinedsdk.adplacement.TeadsAdPlacementFeed { *; }
-keep class tv.teads.sdk.combinedsdk.adplacement.TeadsAdPlacementFeed$Companion { *; }
-keep class tv.teads.sdk.combinedsdk.adplacement.TeadsAdPlacementMedia { *; }
-keep class tv.teads.sdk.combinedsdk.adplacement.TeadsAdPlacementMediaNative { *; }
-keep class tv.teads.sdk.combinedsdk.adplacement.TeadsAdPlacementRecommendations { *; }
-keep class tv.teads.sdk.combinedsdk.adplacement.TeadsAdPlacementRecommendations$Companion { *; }

# Combined SDK configuration classes
-keep class tv.teads.sdk.combinedsdk.adplacement.config.TeadsAdPlacementFeedConfig { *; }
-keep class tv.teads.sdk.combinedsdk.adplacement.config.TeadsAdPlacementMediaConfig { *; }
-keep class tv.teads.sdk.combinedsdk.adplacement.config.TeadsAdPlacementMediaNativeConfig { *; }
-keep class tv.teads.sdk.combinedsdk.adplacement.config.TeadsAdPlacementRecommendationsConfig { *; }

# Combined SDK interfaces
-keep class tv.teads.sdk.combinedsdk.adplacement.interfaces.TeadsAdPlacementEventsDelegate { *; }
-keep class tv.teads.sdk.combinedsdk.adplacement.interfaces.TeadsAdPlacementEventsDelegateFactory { *; }
-keep class tv.teads.sdk.combinedsdk.adplacement.interfaces.TeadsAdPlacementFeedListener { *; }
-keep class tv.teads.sdk.combinedsdk.adplacement.interfaces.DarkModeTogglable { *; }
-keep class tv.teads.sdk.combinedsdk.adplacement.interfaces.core.TeadsAdPlacement { *; }
-keep class tv.teads.sdk.combinedsdk.adplacement.interfaces.core.TeadsAdPlacementCallback { *; }
-keep class tv.teads.sdk.combinedsdk.adplacement.interfaces.core.TeadsAdPlacementObservable { *; }
-keep class tv.teads.sdk.combinedsdk.adplacement.interfaces.core.TeadsAdPlacementSuspendable { *; }

# Combined SDK privacy classes
-keep class tv.teads.sdk.combinedsdk.privacy.TeadsPrivacy { *; }
-keep class tv.teads.sdk.combinedsdk.privacy.AdvertisingIDCallback { *; }

# Combined SDK extensions
-keep class tv.teads.sdk.combinedsdk.adplacement.extensions.** { *; }

# Google Mobile Ads SDK
-keep public class com.google.android.gms.ads.** {
    public *;
}

-keep public class com.google.ads.** {
    public *;
}

-keep class com.google.android.gms.** { *; }
-dontwarn com.google.android.gms.**

# Discrete ScrollView library
-keep class com.yarolegovich.discretescrollview.** { *; }

# OkHttp networking library
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }
-dontwarn okhttp3.**
-dontwarn okio.**

# Apache Commons Text library
-dontwarn org.apache.commons.text.**