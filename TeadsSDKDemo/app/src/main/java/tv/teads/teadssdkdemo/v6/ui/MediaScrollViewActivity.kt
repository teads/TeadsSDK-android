package tv.teads.teadssdkdemo.v6.ui

import android.content.res.Configuration
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.widget.Toolbar
import tv.teads.teadssdkdemo.R

class MediaScrollViewActivity : BaseActivity() {
    
    private val TAG = "Media ScrollView Integration"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        window.statusBarColor = getColor(R.color.background)
        
        setupScrollViewContent()
        
        // Setup toolbar to match MediaColumnScreen TopAppBar styling
        findViewById<Toolbar>(R.id.toolbar).apply {
            title = TAG
            
            // Set colors to match Material 3 theme
            setTitleTextColor(getColor(R.color.titleTextColor))
            
            // Set as support action bar (like TopAppBar in MainActivityV6)
            setSupportActionBar(this)
            supportActionBar?.apply {
                setDisplayHomeAsUpEnabled(true)
                setDisplayShowHomeEnabled(true)
                // Use default arrow - it will be themed automatically
            }
            
            // Handle navigation click (same as onBackClick in MediaColumnScreen)
            setNavigationOnClickListener {
                handleBackPress()
            }
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

    private fun setupScrollViewContent() {
        setContentView(R.layout.activity_base)
        
        // Get the content container
        val contentContainer = findViewById<LinearLayout>(R.id.content_container)
        
        val linearLayout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setPadding(64, 64, 64, 64)
        }

        val titleText = TextView(this).apply {
            text = "Media ScrollView Integration"
            textSize = 24f
            setTypeface(null, Typeface.BOLD)
            gravity = Gravity.CENTER
            setTextColor(getColor(R.color.primaryDef)) // Use theme-aware primary color
        }

        val descriptionText = TextView(this).apply {
            text = "This would contain media content in a scrollview layout"
            textSize = 16f
            gravity = Gravity.CENTER
            setPadding(0, 32, 0, 64)
            setTextColor(getColor(R.color.titleTextColor)) // Use theme-aware text color
        }

        linearLayout.addView(titleText)
        linearLayout.addView(descriptionText)
        
        // Add content to the container
        contentContainer.addView(linearLayout)
    }
}
