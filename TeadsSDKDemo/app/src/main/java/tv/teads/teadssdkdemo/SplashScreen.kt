package tv.teads.teadssdkdemo

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.applovin.sdk.AppLovinSdk
import org.prebid.mobile.Host
import org.prebid.mobile.PrebidMobile


class SplashScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AppLovinSdk.getInstance(this).mediationProvider = "max"
        AppLovinSdk.getInstance(this).initializeSdk {}

        PrebidMobile.initializeSdk(this) {}
        PrebidMobile.setPrebidServerHost(Host.createCustomHost(FAKE_TEADS_PREBID_TEST_SERVER))

        val mainIntent = Intent(this@SplashScreen, MainActivity::class.java)
        this@SplashScreen.startActivity(mainIntent)
        this@SplashScreen.finish()
    }

    companion object {
        private const val FAKE_TEADS_PREBID_TEST_SERVER =
            "https://tm3zwelt7nhxurh4rgapwm5smm0gywau.lambda-url.eu-west-1.on.aws/openrtb2/auction?verbose=true"
    }
}