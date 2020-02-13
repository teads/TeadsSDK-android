package tv.teads.teadssdkdemo.format.example

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_example.*

import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import tv.teads.teadssdkdemo.R
import tv.teads.teadssdkdemo.format.mediation.AdMobBannerFragment
import tv.teads.teadssdkdemo.format.mediation.MoPubRepeatableFragment
import tv.teads.teadssdkdemo.utils.BaseFragment
import tv.teads.teadssdkdemo.utils.ReloadEvent
import tv.teads.teadssdkdemo.utils.event.ChangeFragmentEvent
import tv.teads.teadssdkdemo.format.mediation.AdMobWebViewFragment

/**
 * Different integration example
 */
class ExampleFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_example, container, false)
        programmaticallyExampleButton.setOnClickListener{
            EventBus.getDefault().post(ChangeFragmentEvent(ProgrammaticallyExampleFragment()))
        }
        mopubButton.setOnClickListener{
            EventBus.getDefault().post(ChangeFragmentEvent(MoPubRepeatableFragment()))
        }
        webviewWrapButton.setOnClickListener{
            EventBus.getDefault().post(ChangeFragmentEvent(InReadWebViewWrapFragment()))
        }
        admobButton.setOnClickListener{
            EventBus.getDefault().post(ChangeFragmentEvent(AdMobBannerFragment()))
        }
        mediationWebViewButton.setOnClickListener{
            EventBus.getDefault().post(ChangeFragmentEvent(AdMobWebViewFragment()))
        }
        return rootView
    }

    @Subscribe
    fun onReloadEvent(event: ReloadEvent) {
        // do nothing
    }
}
