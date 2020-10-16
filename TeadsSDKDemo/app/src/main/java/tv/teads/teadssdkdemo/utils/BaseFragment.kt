package tv.teads.teadssdkdemo.utils

import androidx.fragment.app.Fragment

import org.greenrobot.eventbus.EventBus

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


    abstract fun getTitle(): String
}
