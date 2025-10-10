package tv.teads.teadssdkdemo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import org.prebid.mobile.Host
import org.prebid.mobile.PrebidMobile
import org.prebid.mobile.api.data.InitializationStatus
import tv.teads.teadssdkdemo.v6.data.DemoSessionConfiguration.FAKE_TEADS_PREBID_TEST_SERVER
import tv.teads.teadssdkdemo.v6.ui.base.MainActivity


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
}