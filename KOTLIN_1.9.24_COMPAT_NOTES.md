# Consuming the Teads SDK from a Kotlin 1.9.24 project

This branch demonstrates how to build and use the Teads Android SDK (6.1.0) from an
application module that is pinned to **Kotlin 1.9.24**.

## Why a workaround is needed

Since June 2025 (SDK 6.1.0 onwards), the Teads SDK is compiled with Kotlin Gradle
Plugin **2.1.21**. Two things propagate to consumers:

1. The published artifact declares `org.jetbrains.kotlin:kotlin-stdlib:2.1.21` as a
   transitive `compile`-scope dependency — so Gradle will resolve stdlib 2.1.21 on
   your classpath.
2. The SDK's `.class` files carry **Kotlin metadata version 2.1.0**.

Kotlin's compatibility rules are asymmetric:

| Direction | Compatible? |
|---|---|
| **Runtime stdlib** newer than the consumer's compiler | ✅ (forward-compatible: stdlib >= compiler version) |
| **Kotlin metadata** newer than the consumer's compiler | ❌ — the compiler refuses to read it by default |

Kotlin 1.9.x can read metadata up to version **2.0** out of the box. Reading
metadata 2.1 requires an explicit opt-in.

Without the workaround, building our sample app on Kotlin 1.9.24 fails with
dozens of errors like:

```
Class 'tv.teads.sdk.TeadsSDK' was compiled with an incompatible version of Kotlin.
The actual metadata version is 2.1.0, but the compiler version 1.9.0 can read versions up to 2.0.0.
```

## The workaround (one compiler flag)

Add the following to your **app module's** `build.gradle.kts`:

```kotlin
tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions {
        freeCompilerArgs = freeCompilerArgs + "-Xskip-metadata-version-check"
    }
}
```

Or in Groovy (`build.gradle`):

```groovy
tasks.withType(org.jetbrains.kotlin.gradle.tasks.KotlinCompile).configureEach {
    kotlinOptions {
        freeCompilerArgs += "-Xskip-metadata-version-check"
    }
}
```

That's the only change required. No need to upgrade the application's Kotlin
compiler, and no `resolutionStrategy` / stdlib pinning is needed — Gradle's
default resolution already picks the higher stdlib (2.1.21) pulled in by the
Teads SDK, which is the correct outcome (stdlib is forward-compatible with
code compiled by a lower Kotlin version).

## Is this safe?

Yes, in the practical sense. `-Xskip-metadata-version-check` tells the 1.9
compiler to trust metadata newer than what it was designed to read. The Teads
SDK's public API does **not** use any Kotlin 2.x-only language feature (e.g.
context receivers) — so the symbols exposed are all representable in Kotlin
1.9. The flag is the standard JetBrains-recommended approach for this exact
situation.

`kotlin-stdlib:2.1.21` on the classpath is forward-compatible with code
compiled by Kotlin 1.9.24 — stdlib never breaks backwards-binary-compat.

## Long-term recommendation

This workaround is intended as a **bridge** for publishers who can't upgrade
their Kotlin compiler immediately. The clean long-term solution is for the app
to upgrade to Kotlin **1.9.x -> 2.0.x or 2.1.x**. Kotlin 2.x is source-compatible
with virtually all 1.9 code and the upgrade is usually a one-line change in
the plugin version plus a Gradle sync.

## What this branch changed vs. `master`

| File | Change |
|---|---|
| `TeadsSDKDemo/buildSrc/src/main/java/tv/teads/Versions.kt` | Kotlin `2.1.21` -> `1.9.24`; Compose BOM `2025.09.01` -> `2024.06.00`; added `composeCompiler = "1.5.14"` |
| `TeadsSDKDemo/buildSrc/build.gradle.kts` | Kotlin Gradle Plugin `2.1.21` -> `1.9.24`; removed `compose-compiler-gradle-plugin` (Kotlin 2.0+ only) |
| `TeadsSDKDemo/app/build.gradle.kts` | Removed `org.jetbrains.kotlin.plugin.compose`; added `composeOptions { kotlinCompilerExtensionVersion = "1.5.14" }`; added the `-Xskip-metadata-version-check` compiler flag |

## Verified build

```
./gradlew :app:assembleDebug
...
BUILD SUCCESSFUL in 12s
```

---

*This branch (`test/publisher-kotlin-1.9.24-compat`) is for demonstration only.*
*It will not be merged and will be deleted once the referencing publisher has
integrated the workaround on their side.*
