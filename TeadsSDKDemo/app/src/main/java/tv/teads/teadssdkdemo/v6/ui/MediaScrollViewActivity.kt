package tv.teads.teadssdkdemo.v6.ui

import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TextView
import tv.teads.teadssdkdemo.R

class MediaScrollViewActivity : BaseActivity() {
    
    private val TAG = "Media ScrollView Integration"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        setupScrollViewContent()
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
            setTextColor(0xFF6200EA.toInt()) // Purple color similar to Material Design
        }

        val descriptionText = TextView(this).apply {
            text = "This would contain media content in a scrollview layout"
            textSize = 16f
            gravity = Gravity.CENTER
            setPadding(0, 32, 0, 64)
            setTextColor(0xFF000000.toInt())
        }

        linearLayout.addView(titleText)
        linearLayout.addView(descriptionText)
        
        // Add content to the container
        contentContainer.addView(linearLayout)
    }
}
