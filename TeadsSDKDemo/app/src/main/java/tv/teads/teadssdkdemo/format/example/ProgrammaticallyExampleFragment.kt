package tv.teads.teadssdkdemo.format.example

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import kotlinx.android.synthetic.main.list_row_adview.*
import org.greenrobot.eventbus.Subscribe
import tv.teads.sdk.android.AdFailedReason
import tv.teads.sdk.android.CustomAdView
import tv.teads.sdk.android.TeadsListener
import tv.teads.teadssdkdemo.R
import tv.teads.teadssdkdemo.utils.BaseFragment
import tv.teads.teadssdkdemo.utils.ReloadEvent

/**
 * Different integration example
 */
class ProgrammaticallyExampleFragment : BaseFragment() {

    private lateinit var adView: CustomAdView

    private val teadsListener = object : TeadsListener() {

        override fun onAdFailedToLoad(adFailedReason: AdFailedReason?) {
            Toast.makeText(this@ProgrammaticallyExampleFragment.activity, getString(R.string.didfail), Toast.LENGTH_SHORT).show()
        }

        override fun onError(s: String?) {
            Toast.makeText(this@ProgrammaticallyExampleFragment.activity, getString(R.string.didfail_playback), Toast.LENGTH_SHORT).show()
        }
    }

    init {
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_example_programmatically, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val container = view.findViewById<FrameLayout>(R.id.teadsContainer)

        // Instanciate Teads Ad in inReadTop format
        adView = CustomAdView(context)
        adView.setPid(pid)
        adView.layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT)
        adView.enableDebug()
        adView.listener = teadsListener
        adView.load()
        container.addView(adView)
    }

    override fun onDestroy() {
        super.onDestroy()
        adView.clean()
    }

    @Subscribe
    fun onReloadEvent(event: ReloadEvent) {
        adView.load()
    }

}
