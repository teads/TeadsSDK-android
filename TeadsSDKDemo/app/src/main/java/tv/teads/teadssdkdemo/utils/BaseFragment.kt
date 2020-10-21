package tv.teads.teadssdkdemo.utils

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import androidx.fragment.app.Fragment
import tv.teads.teadssdkdemo.MainActivity


/**
 * The base fragment
 */
abstract class BaseFragment : Fragment() {

    /**
     * Return the pid from activity
     *
     */
    protected val pid: Int
        get() {
            val activity = activity as MainActivity? ?: return 0
            return activity.getPid(this.activity!!)
        }

    /**
     * Return the webview url to display
     *
     * @return an url
     */
    val webViewUrl: String
        get() {
            val activity = activity as MainActivity? ?: return ""
            return activity.getWebViewUrl(this.activity!!)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
    }

    abstract fun getTitle(): String
}
