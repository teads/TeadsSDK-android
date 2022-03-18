package tv.teads.teadssdkdemo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.applovin.sdk.AppLovinSdk
import com.mopub.common.MoPub
import com.mopub.common.SdkConfiguration
import tv.teads.teadssdkdemo.format.mediation.identifier.MoPubIdentifier


class SplashScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MoPub.initializeSdk(this, SdkConfiguration.Builder(MoPubIdentifier.MOPUB_ID).build()) {
            Log.d("SplashScreen", "Mopub init")
        }
        AppLovinSdk.getInstance(this).mediationProvider = "max"
        AppLovinSdk.getInstance(this).initializeSdk {}

        val mainIntent = Intent(this@SplashScreen, MainActivity::class.java)
        this@SplashScreen.startActivity(mainIntent)
        this@SplashScreen.finish()
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }
}