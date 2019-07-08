package tv.teads.teadssdkdemo.format.example

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

import butterknife.ButterKnife
import butterknife.OnClick
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
        ButterKnife.bind(this, rootView)
        return rootView
    }

    @OnClick(R.id.programmaticallyExampleButton)
    fun programmaticalyExample() {
        EventBus.getDefault().post(ChangeFragmentEvent(ProgrammaticallyExampleFragment()))
    }

    @OnClick(R.id.mopubButton)
    fun mopubExample() {
        EventBus.getDefault().post(ChangeFragmentEvent(MoPubRepeatableFragment()))
    }

    @OnClick(R.id.webviewWrapButton)
    fun webViewWrapExample() {
        EventBus.getDefault().post(ChangeFragmentEvent(InReadWebViewWrapFragment()))
    }

    @OnClick(R.id.admobButton)
    fun adMobExample() {
        EventBus.getDefault().post(ChangeFragmentEvent(AdMobBannerFragment()))
    }

    @OnClick(R.id.mediationWebViewButton)
    fun mediationWebView() {
        EventBus.getDefault().post(ChangeFragmentEvent(AdMobWebViewFragment()))
    }

    @Subscribe
    fun onReloadEvent(event: ReloadEvent) {
        // do nothing
    }
}
