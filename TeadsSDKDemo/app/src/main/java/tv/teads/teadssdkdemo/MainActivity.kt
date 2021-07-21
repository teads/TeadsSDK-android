package tv.teads.teadssdkdemo

import android.app.Activity
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import android.text.TextUtils
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_main.*
import tv.teads.teadssdkdemo.utils.BaseFragment


class MainActivity : AppCompatActivity() {

    var isWebViewDarkTheme: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setDefaultDayNightTheme(applicationContext.resources.configuration)

        val toolbar: Toolbar = findViewById(R.id.toolbar)

        toolbar.title = ""
        setSupportActionBar(toolbar)
        setToolBar(true)

        if (!TextUtils.isEmpty(intent.getStringExtra(INTENT_EXTRA_PID))) {
            PreferenceManager
                .getDefaultSharedPreferences(this@MainActivity)
                .edit()
                .putInt(
                    SHAREDPREF_PID,
                    Integer.parseInt(intent.getStringExtra(INTENT_EXTRA_PID))
                )
                .apply()
        }

        if (savedInstanceState == null) {
            val transaction = supportFragmentManager.beginTransaction()
            val fragment = MainFragment()
            transaction.replace(R.id.fragment_container, fragment, MainFragment::class.java.simpleName)
            transaction.commit()
        }
    }

    /**
     * Return the pid, if not one is set, the default one
     *
     * @return pid
     */
    fun getPid(): Int {
        return PreferenceManager
            .getDefaultSharedPreferences(this)
            .getInt(SHAREDPREF_PID, SHAREDPREF_PID_DEFAULT)
    }

    fun setPid(pid: Int) {
        PreferenceManager.getDefaultSharedPreferences(this@MainActivity).edit()
            .putInt(SHAREDPREF_PID, pid)
            .apply()
    }

    /**
     * Return the Webview url, if not one is set, the default one
     *
     * @return an url
     */
    fun getWebViewUrl(): String = SHAREDPREF_WEBVIEW_DEFAULT

    fun changeFragment(frag: BaseFragment) {
        if ((supportFragmentManager.findFragmentById(R.id.fragment_container) as Fragment).javaClass == frag.javaClass) {
            return
        }

        val backStateName = (frag as Any).javaClass.name

        try {
            val manager = supportFragmentManager
            //fragment not in back stack, create it.
            val transaction = manager.beginTransaction()
            transaction.replace(R.id.fragment_container, frag, (frag as Any).javaClass.name)
            transaction.addToBackStack(backStateName)
            transaction.commit()
            setToolBar(false)
        } catch (exception: IllegalStateException) {
            Log.e(LOG_TAG, "Unable to commit fragment, could be activity as been killed in background. $exception")
        }
    }

    private fun setToolBar(isMainFragment: Boolean) {
        supportActionBar?.setDisplayHomeAsUpEnabled(!isMainFragment)

        when (isMainFragment) {
            true -> {
                toolbar_logo.setImageResource(R.drawable.teads_demo)
                status_bar_view.setBackgroundColor(ContextCompat.getColor(this, R.color.background))
                toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.background))
            }
            else -> {
                toolbar_logo.setImageResource(R.drawable.teads_demo_white)
                status_bar_view.background = ContextCompat.getDrawable(this, R.drawable.gradient_teads)
                toolbar.background = ContextCompat.getDrawable(this, R.drawable.gradient_teads)
            }
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        setDefaultDayNightTheme(newConfig)
    }

    private fun setDefaultDayNightTheme(config: Configuration) {
        when (config.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_NO -> {
                isWebViewDarkTheme = false
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            Configuration.UI_MODE_NIGHT_YES -> {
                isWebViewDarkTheme = true
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                supportFragmentManager.popBackStack()
                setToolBar(true)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        supportFragmentManager.popBackStack()
        setToolBar(true)
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            hideSystemUIAndNavigation(this)
            adjustToolbarMarginForNotch()
        }
    }

    private fun hideSystemUIAndNavigation(activity: Activity) {
        val decorView: View = activity.window.decorView
        decorView.systemUiVisibility =
            (View.SYSTEM_UI_FLAG_IMMERSIVE
                    // Set the content to appear under the system bars so that the
                    // content doesn't resize when the system bars hide and show.
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN // Hide the nav bar and status bar
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_FULLSCREEN)
    }

    private fun adjustToolbarMarginForNotch() {
        // Notch is only supported by >= Android 9
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val windowInsets = window.decorView.rootWindowInsets
            if (windowInsets != null) {
                val displayCutout = windowInsets.displayCutout
                if (displayCutout != null) {
                    val safeInsetTop = displayCutout.safeInsetTop
                    val newLayoutParams = toolbar.layoutParams as ViewGroup.MarginLayoutParams
                    newLayoutParams.setMargins(0, safeInsetTop, 0, 0)
                    toolbar.layoutParams = newLayoutParams
                }
            }
        }
    }

    companion object {
        private const val LOG_TAG = "MainActivity"
        private const val INTENT_EXTRA_PID = "ext_pid"
        const val SHAREDPREF_PID = "sp_pid"
        const val SHAREDPREF_PID_DEFAULT = 84242
        private const val SHAREDPREF_WEBVIEW_DEFAULT = "file:///android_asset/demo.html"
        private const val SHAREDPREF_WEBVIEW_NIGHT = "file:///android_asset/demo_night.html"

    }
}
