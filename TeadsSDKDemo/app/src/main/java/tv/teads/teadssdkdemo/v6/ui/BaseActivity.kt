package tv.teads.teadssdkdemo.v6.ui

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import tv.teads.teadssdkdemo.v6.navigation.NavigationHandler

/**
 * Base activity with common functionality for back navigation
 */
abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBackButtonHandling()
        
        // Don't call setContentView here - let subclasses handle it
    }

    private fun setupBackButtonHandling() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                handleBackPress()
            }
        })
    }

    /**
     * Handle back button press - override in subclasses for custom behavior
     */
    protected open fun handleBackPress() {
        NavigationHandler.navigateBackToDemo(this)
    }

    /**
     * Enable edge to edge display
     */
    private fun enableEdgeToEdge() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
    }
}
