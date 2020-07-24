package tv.teads.teadssdkdemo.format.example

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import tv.teads.teadssdkdemo.R
import tv.teads.teadssdkdemo.format.mediation.AdMobBannerFragment
import tv.teads.teadssdkdemo.format.mediation.MoPubRepeatableFragment
import tv.teads.teadssdkdemo.utils.BaseFragment
import tv.teads.teadssdkdemo.utils.ReloadEvent
import tv.teads.teadssdkdemo.utils.event.ChangeFragmentEvent
import tv.teads.teadssdkdemo.format.mediation.AdMobWebViewFragment
import tv.teads.teadssdkdemo.format.mediation.MopubBannerFragment

/**
 * Different integration example
 */
class ExampleFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_example, container, false)

        rootView.findViewById<View>(R.id.programmaticallyExampleButton).setOnClickListener{
            EventBus.getDefault().post(ChangeFragmentEvent(ProgrammaticallyExampleFragment()))
        }
        rootView.findViewById<View>(R.id.mopubButton).setOnClickListener{
            EventBus.getDefault().post(ChangeFragmentEvent(MoPubRepeatableFragment()))
        }
        rootView.findViewById<View>(R.id.webviewWrapButton).setOnClickListener{
            EventBus.getDefault().post(ChangeFragmentEvent(InReadWebViewWrapFragment()))
        }
        rootView.findViewById<View>(R.id.admobButton).setOnClickListener{
            EventBus.getDefault().post(ChangeFragmentEvent(AdMobBannerFragment()))
        }
        rootView.findViewById<View>(R.id.mopubMediation).setOnClickListener {
            EventBus.getDefault().post(ChangeFragmentEvent(MopubBannerFragment()))
        }
        rootView.findViewById<View>(R.id.mediationWebViewButton).setOnClickListener{
            EventBus.getDefault().post(ChangeFragmentEvent(AdMobWebViewFragment()))
        }
        return rootView
    }

    @Subscribe
    fun onReloadEvent(event: ReloadEvent) {
        // do nothing
    }
}
