package tv.teads.teadssdkdemo.format.custom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_custom_ad_scrollview.*
import org.greenrobot.eventbus.Subscribe
import tv.teads.sdk.android.AdFailedReason
import tv.teads.sdk.android.TeadsListener
import tv.teads.teadssdkdemo.R
import tv.teads.teadssdkdemo.utils.BaseFragment
import tv.teads.teadssdkdemo.utils.ReloadEvent

/**
 * Custom ad format within a ScrollView
 */
class CustomAdScrollViewFragment : BaseFragment() {

    private val teadsListener = object : TeadsListener() {
        override fun onAdFailedToLoad(adFailedReason: AdFailedReason?) {
            Toast.makeText(this@CustomAdScrollViewFragment.activity, getString(R.string.didfail)+ adFailedReason.errorMessage, Toast.LENGTH_SHORT).show()
        }

        override fun onError(s: String?) {
            Toast.makeText(this@CustomAdScrollViewFragment.activity, getString(R.string.didfail_playback), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_custom_ad_scrollview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        // Instanciate Teads Ad in custom ad format
        teadsAdView.setPid(pid)
        teadsAdView.listener = teadsListener
        teadsAdView.load()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        teadsAdView.clean()
    }

    @Subscribe
    fun onReloadEvent(event: ReloadEvent) {
        teadsAdView.load()
    }
}
