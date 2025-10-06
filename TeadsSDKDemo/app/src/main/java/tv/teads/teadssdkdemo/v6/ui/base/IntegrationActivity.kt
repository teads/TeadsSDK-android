package tv.teads.teadssdkdemo.v6.ui.base

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import tv.teads.teadssdkdemo.R
import tv.teads.teadssdkdemo.v6.ui.base.navigation.NavigationHandler
import tv.teads.teadssdkdemo.v6.ui.base.navigation.Route
import tv.teads.teadssdkdemo.v6.ui.base.navigation.getFragmentClass
import tv.teads.teadssdkdemo.v6.ui.base.navigation.getFragmentTag
import tv.teads.teadssdkdemo.v6.ui.base.utils.AnimationHelper

class IntegrationActivity : AppCompatActivity() {
    
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
        setupBackButtonHandling()
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

    private fun setupBackButtonHandling() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                handleBackPress()
            }
        })
    }

    /**
     * Handle back button press - navigate back to demo
     */
    private fun handleBackPress() {
        NavigationHandler.navigateBackToDemo(this)
    }

    private fun loadRouteFromIntent() {
        val routeName = intent.getStringExtra(EXTRA_ROUTE)
        currentRoute = when (routeName) {
            "MediaScrollView" -> Route.MediaScrollView
            "MediaRecyclerView" -> Route.MediaRecyclerView
            "MediaColumn" -> Route.MediaColumn
            else -> null
        }
    }

    private fun loadFragment() {
        currentRoute?.let { route ->
            try {
                val fragmentClass = route.getFragmentClass()
                val fragment = fragmentClass.getDeclaredConstructor().newInstance()
                
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
        recreate()
    }

    override fun finish() {
        super.finish()
        AnimationHelper.applyFadeCloseTransition(this)
    }
}
