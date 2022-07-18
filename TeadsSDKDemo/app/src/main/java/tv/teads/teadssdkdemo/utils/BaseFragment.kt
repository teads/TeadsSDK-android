package tv.teads.teadssdkdemo.utils

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import androidx.fragment.app.Fragment
import tv.teads.teadssdkdemo.MainActivity
import tv.teads.teadssdkdemo.data.FormatType
import tv.teads.teadssdkdemo.data.PidStore
import tv.teads.teadssdkdemo.data.ProviderType


/**
 * The base fragment
 */
abstract class BaseFragment : Fragment() {

    /**
     * Return the pid
     *
     */
    val pid: Int
        get() {
            return PidStore.getPid(requireContext(), selectedFormat)
        }

    /**
     * Return current selected format type
     *
     */
    var selectedFormat: FormatType = FormatType.INREAD

    /**
     * Return current selected provider type
     *
     */
    var selectedProvider: ProviderType = ProviderType.DIRECT

    /**
     * Return the webview url to display
     *
     * @return an url
     */
    val webViewUrl: String
        get() {
            val activity = activity as MainActivity? ?: return ""
            return activity.getWebViewUrl()
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
