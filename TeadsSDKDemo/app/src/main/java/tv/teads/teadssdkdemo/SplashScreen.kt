package tv.teads.teadssdkdemo

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity


class SplashScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val mainIntent = Intent(this@SplashScreen, MainActivity::class.java)
        this@SplashScreen.startActivity(mainIntent)
        this@SplashScreen.finish()
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }
}