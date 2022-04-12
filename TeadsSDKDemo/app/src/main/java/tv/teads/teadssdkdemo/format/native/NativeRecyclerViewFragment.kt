package tv.teads.teadssdkdemo.format.native

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import tv.teads.sdk.*
import tv.teads.sdk.renderer.NativeAdView
import tv.teads.teadssdkdemo.R
import tv.teads.teadssdkdemo.utils.BaseFragment

/**
 * Native format within a RecyclerView
 *
 */
class NativeRecyclerViewFragment : BaseFragment(), NativeAdListener {

    private lateinit var adNativeView: NativeAdView
    private lateinit var adPlacement: NativeAdPlacement

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_native_recyclerview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adNativeView = view.findViewById(R.id.nativeAdView)
        setNativeAd()
    }

    private fun setNativeAd() {
        val placementSettings = AdPlacementSettings.Builder().build()
        val requestSettings = AdRequestSettings.Builder().build()

        adPlacement = TeadsSDK.createNativePlacement(requireContext(), 124859, placementSettings)
        adPlacement.requestAd(requestSettings, this)
    }

    override fun onAdReceived(nativeAd: NativeAd) {
        adNativeView.bind(nativeAd)
    }

    override fun getTitle(): String = "Native Direct RecyclerView"
}
