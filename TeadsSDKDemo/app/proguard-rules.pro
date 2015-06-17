# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/hugogresse/Documents/Dev/lib/android-sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Butter Knife
-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewInjector { *; }

-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}

-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}


# ============= Teads

# For WebView JavascriptInterface
-keepattributes JavascriptInterface
-keep public class tv.teads.sdk.adContainer.layout.webview.** { *; }

# Jackson
-dontwarn org.w3c.dom.bootstrap.DOMImplementationRegistry
-keepnames class com.fasterxml.jackson.** { *; }
-keep public class tv.teads.sdk.adServer.parser.json.** {
    public protected *;
    public void set*(***);
    public *** get*();
}
-keepattributes *Annotation*,EnclosingMethod,Signature
-keep class com.fasterxml.jackson.databind.ObjectMapper {
    public <methods>;
    protected <methods>;
}
-keep class com.fasterxml.jackson.databind.ObjectWriter {
    public ** writeValueAsString(**);
}

# Okio
-dontwarn okio.**

# Enums
-keepattributes InnerClasses
-keep public enum  tv.teads.sdk.adContent.AdContent$** {
    **[] $VALUES;
    public *;
}
-keep public enum  tv.teads.sdk.publisher.TeadsLog$** {
    **[] $VALUES;
    public *;
}
