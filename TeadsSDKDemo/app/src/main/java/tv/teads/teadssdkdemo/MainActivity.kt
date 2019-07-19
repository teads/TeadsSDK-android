package tv.teads.teadssdkdemo

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import butterknife.ButterKnife
import butterknife.OnClick
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import tv.teads.teadssdkdemo.format.custom.CustomAdRecyclerViewFragment
import tv.teads.teadssdkdemo.format.custom.CustomAdScrollViewFragment
import tv.teads.teadssdkdemo.format.custom.CustomAdWebViewFragment
import tv.teads.teadssdkdemo.format.example.ExampleFragment
import tv.teads.teadssdkdemo.format.inread.InReadRecyclerViewFragment
import tv.teads.teadssdkdemo.format.inread.InReadRepeatableRecyclerViewFragment
import tv.teads.teadssdkdemo.format.inread.InReadScrollViewFragment
import tv.teads.teadssdkdemo.format.inread.InReadWebViewFragment
import tv.teads.teadssdkdemo.utils.ReloadEvent
import tv.teads.teadssdkdemo.utils.event.ChangeFragmentEvent


class MainActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout

    override fun onNewIntent(intent: Intent) {
        if (!TextUtils.isEmpty(intent.getStringExtra(INTENT_EXTRA_PID))) {
            PreferenceManager
                    .getDefaultSharedPreferences(this@MainActivity)
                    .edit()
                    .putInt(
                            SHAREDPREF_PID,
                            Integer.parseInt(intent.getStringExtra(INTENT_EXTRA_PID)))
                    .apply()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)

        if (!TextUtils.isEmpty(intent.getStringExtra(INTENT_EXTRA_PID))) {
            PreferenceManager
                    .getDefaultSharedPreferences(this@MainActivity)
                    .edit()
                    .putInt(
                            SHAREDPREF_PID,
                            Integer.parseInt(intent.getStringExtra(INTENT_EXTRA_PID)))
                    .apply()
        }

        if (savedInstanceState == null) {
            val transaction = supportFragmentManager.beginTransaction()
            val fragment = MainFragment()
            transaction.replace(R.id.fragment_container, fragment, MainFragment::class.java.simpleName)
            transaction.commit()
        }

        val toolbar = findViewById<Toolbar>(R.id.toolbar)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            toolbar.setTitleTextColor(resources.getColor(R.color.accent, null))
        } else {
            @Suppress("DEPRECATION")
            toolbar.setTitleTextColor(resources.getColor(R.color.accent))
        }
        drawerLayout = findViewById(R.id.drawer_layout)

        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

        val drawerToggle = object : ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.drawer_open,
                R.string.drawer_close) {

            /** Called when a drawer has settled in a completely closed state.  */
            override fun onDrawerClosed(view: View) {
                super.onDrawerClosed(view)

                invalidateOptionsMenu() // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state.  */
            override fun onDrawerOpened(drawerView: View) {
                super.onDrawerOpened(drawerView)

                invalidateOptionsMenu() // creates call to onPrepareOptionsMenu()
            }
        }

        // Set the drawer toggle as the DrawerListener
        drawerLayout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()
    }

    /**
     * Return the pid, if not one is set, the default one
     *
     * @param context current context
     * @return pid
     */
    fun getPid(context: Context): Int {
        return PreferenceManager
                .getDefaultSharedPreferences(context)
                .getInt(
                        SHAREDPREF_PID, SHAREDPREF_PID_DEFAULT)
    }

    /**
     * Return the Webview url, if not one is set, the default one
     *
     * @param context current context
     * @return an url
     */
    fun getWebViewUrl(context: Context): String {
        return PreferenceManager
                .getDefaultSharedPreferences(context)
                .getString(SHAREDPREF_WEBVIEWURL, SHAREDPREF_WEBVIEW_DEFAULT) ?: return SHAREDPREF_WEBVIEW_DEFAULT
    }

    private fun changeFragment(frag: Fragment) {
        if (supportFragmentManager.findFragmentById(R.id.fragment_container).javaClass == frag.javaClass) {
            closeDrawerAndReloadAd()
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
            // Close drawer
            drawerLayout.closeDrawer(GravityCompat.START)
        } catch (exception: IllegalStateException) {
            Log.e(LOG_TAG, "Unable to commit fragment, could be activity as been killed in background. $exception")
        }

    }

    private fun closeDrawerAndReloadAd() {
        drawerLayout.closeDrawer(GravityCompat.START)
        EventBus.getDefault().post(ReloadEvent())
    }

    public override fun onResume() {
        super.onResume()
        EventBus.getDefault().register(this)
    }

    public override fun onPause() {
        super.onPause()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe
    fun onPostFragmentChangeEvent(event: ChangeFragmentEvent) {
        changeFragment(event.fragment)
    }

    /*----------------------------------------
     * NavigationDrawer button listener
     */

    @OnClick(R.id.inread_scrollview)
    fun inReadScrollView() {
        changeFragment(InReadScrollViewFragment())
    }

    @OnClick(R.id.inread_recyclerview)
    fun inReadRecyclerView() {
        changeFragment(InReadRecyclerViewFragment())
    }

    @OnClick(R.id.inread_repeatable_recyclerview)
    fun inReadRepeatableRecyclerView() {
        changeFragment(InReadRepeatableRecyclerViewFragment())
    }

    @OnClick(R.id.inread_webview)
    fun inReadWebView() {
        changeFragment(InReadWebViewFragment())
    }

    @OnClick(R.id.custom_scrollview)
    fun inReadTopScrollView() {
        changeFragment(CustomAdScrollViewFragment())
    }

    @OnClick(R.id.custom_recyclerview)
    fun inReadTopRecyclerView() {
        changeFragment(CustomAdRecyclerViewFragment())
    }

    @OnClick(R.id.custom_webview)
    fun inReadTopWebView() {
        changeFragment(CustomAdWebViewFragment())
    }

    @OnClick(R.id.exampleButton)
    fun example() {
        changeFragment(ExampleFragment())
    }

    @SuppressLint("SetTextI18n")
    @OnClick(R.id.action_pid)
    fun changePidDialog() {
        // Set an EditText view to get user input
        @SuppressLint("InflateParams") val view = layoutInflater.inflate(R.layout.dialog_pid_content, null)
        val input = view.findViewById<EditText>(R.id.pidEditText)
        input.setText(getPid(this).toString())
        input.setLines(1)
        input.setSingleLine(true)

        AlertDialog.Builder(this)
                .setTitle("Pid")
                .setMessage("Change saved pid")
                .setView(view)
                .setPositiveButton("Save") { _, _ ->
                    val pidString = input.text.toString()
                    val pid: Int
                    pid = if (pidString.isEmpty()) {
                        Toast.makeText(this@MainActivity, "Setting default pid", Toast.LENGTH_SHORT).show()
                        SHAREDPREF_PID_DEFAULT
                    } else {
                        Integer.parseInt(pidString)
                    }
                    PreferenceManager
                            .getDefaultSharedPreferences(this@MainActivity)
                            .edit()
                            .putInt(
                                    SHAREDPREF_PID,
                                    pid)
                            .apply()
                }
                .setNegativeButton("Cancel") { _, _ ->
                    // Do nothing.
                }.show()

    }

    @OnClick(R.id.action_webviewurl)
    fun changeWebviewUrlDialog() {
        // Set an EditText view to get user input
        @SuppressLint("InflateParams") val view = layoutInflater.inflate(R.layout.dialog_webview_content, null)
        val input = view.findViewById<EditText>(R.id.webViewEditText)
        input.setText(getWebViewUrl(this))

        AlertDialog.Builder(this)
                .setTitle("WebView Url")
                .setMessage("Change WebView url")
                .setView(view)
                .setPositiveButton("Save") { _, _ ->
                    var pidString = input.text.toString()
                    if (pidString.isEmpty()) {
                        Toast.makeText(this@MainActivity, "Setting default webview url", Toast.LENGTH_SHORT).show()
                        pidString = SHAREDPREF_WEBVIEW_DEFAULT
                    }
                    PreferenceManager
                            .getDefaultSharedPreferences(this@MainActivity)
                            .edit()
                            .putString(
                                    SHAREDPREF_WEBVIEWURL,
                                    pidString)
                            .apply()
                }
                .setNegativeButton("Cancel") { _, _ ->
                    // Do nothing.
                }.show()
    }

    companion object {
        private const val LOG_TAG = "MainActivity"
        private const val INTENT_EXTRA_PID = "ext_pid"
        private const val SHAREDPREF_PID = "sp_pid"
        private const val SHAREDPREF_WEBVIEWURL = "sp_wvurl"
        private const val SHAREDPREF_PID_DEFAULT = 84242
        private const val SHAREDPREF_WEBVIEW_DEFAULT = "http://sample.teads.net/demo/sdk/demo.html"
    }
}
