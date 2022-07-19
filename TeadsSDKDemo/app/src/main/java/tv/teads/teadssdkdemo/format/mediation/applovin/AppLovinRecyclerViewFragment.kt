package tv.teads.teadssdkdemo.format.mediation.applovin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_recyclerview.*
import tv.teads.teadssdkdemo.R
import tv.teads.teadssdkdemo.format.mediation.adapter.AppLovinRecyclerViewAdapter
import tv.teads.teadssdkdemo.format.mediation.identifier.AdMobIdentifier
import tv.teads.teadssdkdemo.utils.BaseFragment

/**
 * Display inRead as Banner within a RecyclerView using AdMob Mediation.
 */
class AppLovinRecyclerViewFragment : BaseFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_recyclerview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setRecyclerViewAdapter(recyclerView)
    }

    private fun setRecyclerViewAdapter(recyclerView: RecyclerView) {
        recyclerView.layoutManager = LinearLayoutManager(activity)

        val adUnit = AdMobIdentifier.getAdUnitFromPid(pid)

        recyclerView.adapter = AppLovinRecyclerViewAdapter(adUnit, context, getTitle())
    }

    override fun getTitle(): String = "InRead AdMob RecyclerView"
}
