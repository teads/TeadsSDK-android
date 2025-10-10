package tv.teads.teadssdkdemo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import org.prebid.mobile.Host
import org.prebid.mobile.PrebidMobile
import org.prebid.mobile.api.data.InitializationStatus


class SplashScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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

    companion object {
        private const val FAKE_TEADS_PREBID_TEST_SERVER =
            "https://tm3zwelt7nhxurh4rgapwm5smm0gywau.lambda-url.eu-west-1.on.aws/openrtb2/auction?verbose=true"
    }
}