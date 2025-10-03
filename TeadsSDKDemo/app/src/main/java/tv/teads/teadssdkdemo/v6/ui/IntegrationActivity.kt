package tv.teads.teadssdkdemo.v6.ui

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.widget.Toolbar
import tv.teads.teadssdkdemo.R
import tv.teads.teadssdkdemo.v6.navigation.Route
import tv.teads.teadssdkdemo.v6.navigation.getFragmentClass
import tv.teads.teadssdkdemo.v6.navigation.getFragmentTag

class IntegrationActivity : BaseActivity() {
    
    companion object {
        const val EXTRA_ROUTE = "route"
        
        /**
         * Launch IntegrationActivity with specific route (pass route name as string)
         */
        fun launch(context: Context, route : Route) {
            val intent = Intent(context, IntegrationActivity::class.java).apply {
                putExtra(EXTRA_ROUTE, route::class.java.simpleName)
            }
            context.startActivity(intent)
        }
    }
    
    private var currentRoute: Route? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        window.statusBarColor = getColor(R.color.background)
        
        setContentView(R.layout.activity_base)
        setupToolbar()
        loadRouteFromIntent()
        
        if (savedInstanceState == null) {
            loadFragment()
        }
    }

    private fun setupToolbar() {
        findViewById<Toolbar>(R.id.toolbar).apply {
            title = currentRoute?.javaClass?.simpleName ?: "Integration Sample"
            
            setTitleTextColor(getColor(R.color.titleTextColor))
            
            setSupportActionBar(this)
            supportActionBar?.apply {
                setDisplayHomeAsUpEnabled(true)
                setDisplayShowHomeEnabled(true)
            }
            
            setNavigationOnClickListener {
                handleBackPress()
            }
        }
    }

    private fun loadRouteFromIntent() {
        val routeName = intent.getStringExtra(EXTRA_ROUTE)
        currentRoute = when (routeName) {
            "MediaScrollView" -> Route.MediaScrollView
            "MediaColumn" -> Route.MediaColumn
            else -> null
        }
    }

    private fun loadFragment() {
        currentRoute?.let { route ->
            try {
                val fragmentClass = route.getFragmentClass()
                val fragment = fragmentClass.newInstance()
                
                supportFragmentManager.beginTransaction()
                    .replace(R.id.content_container, fragment, route.getFragmentTag())
                    .commit()
                
                supportActionBar?.title = route.javaClass.simpleName
            } catch (e: Exception) {
                finish()
            }
        } ?: run {
            // No route provided, go back
            finish()
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        
        // Update status bar color when theme changes
        window.statusBarColor = getColor(R.color.background)
        
        // Refresh toolbar colors
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.setTitleTextColor(getColor(R.color.titleTextColor))
        
        // Recreate activity to apply new theme completely
        recreate()
    }

    override fun finish() {
        super.finish()
        // Add exit animation when activity finishes
        overridePendingTransition(
            android.R.anim.fade_in,
            android.R.anim.fade_out
        )
    }
}
