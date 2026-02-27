package tv.teads.teadssdkdemo.v6.ui.base

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import org.prebid.mobile.Host
import org.prebid.mobile.PrebidMobile
import org.prebid.mobile.api.data.InitializationStatus
import tv.teads.teadssdkdemo.v6.data.DemoSessionConfiguration.FAKE_TEADS_PREBID_TEST_SERVER


class SplashScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Simulate CMP writing IAB TCF v2.3 consent signals to SharedPreferences
        writeFakeCmpSignals()

        // Init Prebid SDK
        PrebidMobile.initializeSdk(this) { status ->
            Log.d("PrebidMobile", "initializeSdk result: $status")
            if (status == InitializationStatus.SUCCEEDED) {
                PrebidMobile.setPrebidServerHost(Host.createCustomHost(FAKE_TEADS_PREBID_TEST_SERVER))
            }
        }

        val mainIntent = Intent(this@SplashScreen, MainActivity::class.java)
        this@SplashScreen.startActivity(mainIntent)
        this@SplashScreen.finish()
    }

    /**
     * Writes fake IAB TCF v2.3 consent signals to default SharedPreferences,
     * exactly as a real CMP SDK would do per IAB spec.
     *
     * Keys and values follow:
     * https://github.com/InteractiveAdvertisingBureau/GDPR-Transparency-and-Consent-Framework/blob/master/TCFv2/IAB%20Tech%20Lab%20-%20CMP%20API%20v2.md#in-app-details
     */
    private fun writeFakeCmpSignals() {
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        prefs.edit().apply {
            // CMP metadata
            putInt("IABTCF_CmpSdkID", 300)          // Fake CMP SDK ID
            putInt("IABTCF_CmpSdkVersion", 3)        // Fake CMP SDK version
            putInt("IABTCF_PolicyVersion", 5)         // 5 = TCF v2.2+ / v2.3
            putInt("IABTCF_gdprApplies", 1)           // 1 = GDPR applies
            putString("IABTCF_PublisherCC", "FR")     // Publisher country code
            putInt("IABTCF_PurposeOneTreatment", 0)   // 0 = no special treatment
            putInt("IABTCF_UseNonStandardTexts", 0)   // 0 = CMP uses standard IAB texts

            // TC String: Core + DisclosedVendors + PublisherTC segments (dot-separated)
            // Generated with @iabtcf/core using GVL v148 (policyVersion=5, Teads=vendor 132)
            // Segment 1 (Core): purposes 1-10 consent, purpose 2-10 LI, feature 1 opt-in, vendor 132 consent+LI
            // Segment 2 (DisclosedVendors): full GVL disclosed vendors bitfield
            // Segment 3 (PublisherTC): publisher purposes 1-10 consent, 2-10 LI
            putString(
                "IABTCF_TCString",
                "CQgSLEAQgSLEAEsADBENCUFoAP_AAH_AAAqIBCQAQBCAEJABAEIAAAAA" +
                    ".IMENR_G__bXlv-bb36ftkeYxf9_hr7sQxBgbJs24FzLvW7JwH32E7NEzatqYKmRIAu3TBIQNtHIjURUChKIgVrzDsaE2U4TtKJ-BkiHMZY2tYCFxvm4tjWQCZ4vr_91d9mT-t7dr-2dzy27hnv3a9f-S1UJidKYetHfv8ZBOT-_IU9_x-_4v4_MbpEm-eS1v_tWtt43d64vP_dpuxt-Tyff7____73_e7X__e__33_-_Xf_7_____________f______8AAA" +
                    ".f_gAD_gAAAAA"
            )

            // Pre-parsed binary strings (as CMP writes for convenience)
            // Purpose consents: purposes 1-10 granted (10 bits)
            putString("IABTCF_PurposeConsents", "1111111111")
            // Purpose legitimate interests: purposes 2-10 (purpose 1 cannot be LI)
            putString("IABTCF_PurposeLegitimateInterests", "0111111111")
            // Vendor consents: bit 132 (Teads) set to 1
            // 131 zeros + "1" at position 132
            putString("IABTCF_VendorConsents", "0".repeat(131) + "1")
            // Vendor legitimate interests: bit 132 (Teads) set to 1
            putString("IABTCF_VendorLegitimateInterests", "0".repeat(131) + "1")
            // Special features opt-ins: feature 1 (precise geolocation) opted in
            putString("IABTCF_SpecialFeaturesOptIns", "10")
            // Disclosed vendors: bit 132 (Teads) disclosed
            putString("IABTCF_DisclosedVendors", "0".repeat(131) + "1")

            apply()
        }
        Log.d("FakeCMP", "IAB TCF v2.3 consent signals written to SharedPreferences")
    }
}