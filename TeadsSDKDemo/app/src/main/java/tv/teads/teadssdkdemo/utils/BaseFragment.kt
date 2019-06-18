package tv.teads.teadssdkdemo.utils

import android.support.v4.app.Fragment

import org.greenrobot.eventbus.EventBus

import tv.teads.teadssdkdemo.MainActivity

/**
 * The base fragment
 * Created by Hugo Gresse on 03/04/15.
 */
abstract class BaseFragment : Fragment() {

    /**
     * Return the pid from activity
     *
     * @return the pid
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

    override fun onResume() {
        super.onResume()
        EventBus.getDefault().register(this)
    }

    override fun onPause() {
        super.onPause()
        EventBus.getDefault().unregister(this)
    }
}
