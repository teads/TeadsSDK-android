package tv.teads.teadssdkdemo.format.inread

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_custom_ad_recyclerview.teadsAdView
import kotlinx.android.synthetic.main.fragment_inread_scrollview.*
import org.greenrobot.eventbus.Subscribe
import tv.teads.sdk.android.AdFailedReason
import tv.teads.sdk.android.TeadsListener
import tv.teads.teadssdkdemo.R
import tv.teads.teadssdkdemo.utils.BaseFragment
import tv.teads.teadssdkdemo.utils.ReloadEvent

/**
 * InRead format within a ScrollView
 */
class InReadScrollViewFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_inread_scrollview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        teadsAdView.setPid(pid)
        teadsAdView.listener = object : TeadsListener() {
            override fun onAdFailedToLoad(reason: AdFailedReason?) {
                Toast.makeText(this@InReadScrollViewFragment.activity, getString(R.string.didfail), Toast.LENGTH_SHORT).show()
            }

            override fun onError(error: String?) {
                Toast.makeText(this@InReadScrollViewFragment.activity, getString(R.string.didfail_playback), Toast.LENGTH_SHORT).show()
            }
        }
        teadsAdView.setAdContainerView(adContainer)
        teadsAdView.load()
    }

    @Suppress("UNUSED_PARAMETER")
    @Subscribe
    fun onReloadEvent(event: ReloadEvent) {
        teadsAdView.clean()
        teadsAdView.setAdContainerView(adContainer)
        teadsAdView.load()
    }

}
