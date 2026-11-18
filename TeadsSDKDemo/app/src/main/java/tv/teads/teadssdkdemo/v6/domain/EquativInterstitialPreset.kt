package tv.teads.teadssdkdemo.v6.domain

import com.equativ.displaysdk.model.SASAdPlacement

enum class EquativInterstitialPreset(val displayName: String) {
    TEADS("Teads Ad"),
    TEST("Equativ Test Ad");

    val placement: SASAdPlacement
        get() = when (this) {
            TEADS -> SASAdPlacement(siteId = 787047L, pageId = 2231145L, formatId = 158215L)
            TEST -> SASAdPlacement.TestPlacement.TEST_PLACEMENT_INTERSTITIAL_HTML
        }
}
