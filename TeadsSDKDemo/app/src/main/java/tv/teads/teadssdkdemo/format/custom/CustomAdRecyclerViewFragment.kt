package tv.teads.teadssdkdemo.format.custom

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_custom_ad_recyclerview.*

import org.greenrobot.eventbus.Subscribe

import java.util.ArrayList

import tv.teads.sdk.android.AdFailedReason
import tv.teads.sdk.android.CustomAdView
import tv.teads.sdk.android.InReadAdView
import tv.teads.sdk.android.TeadsListener
import tv.teads.teadssdkdemo.R
import tv.teads.teadssdkdemo.format.adapter.SimpleRecyclerViewAdapter
import tv.teads.teadssdkdemo.utils.BaseFragment
import tv.teads.teadssdkdemo.utils.ReloadEvent

/**
 * Custom ad format within a RecyclerView
 *
 *
 * Created by Hugo Gresse on 30/03/15.
 */
class CustomAdRecyclerViewFragment : BaseFragment() {

    private val teadsListener = object : TeadsListener() {

        override fun onAdFailedToLoad(adFailedReason: AdFailedReason?) {
            Toast.makeText(this@CustomAdRecyclerViewFragment.activity, getString(R.string.didfail), Toast.LENGTH_SHORT).show()
        }

        override fun onError(s: String?) {
            Toast.makeText(this@CustomAdRecyclerViewFragment.activity, getString(R.string.didfail_playback), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_custom_ad_recyclerview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // Set RecyclerView basic adapter
        setRecyclerViewAdapter(recyclerView)

        // Instanciate Teads Ad in custom format
        teadsAdView.setPid(pid)
        teadsAdView.listener = teadsListener
        teadsAdView.load()
    }

    override fun onDestroy() {
        super.onDestroy()
        teadsAdView.clean()
    }

    private fun setRecyclerViewAdapter(recyclerView: RecyclerView) {
        val data = ArrayList<String>()

        for (i in 0..49) {
            data.add("Teads $i")
        }

        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = SimpleRecyclerViewAdapter(activity, data, pid, -1)
    }

    @Subscribe
    fun onReloadEvent(event: ReloadEvent) {
        teadsAdView.load()
    }
}
