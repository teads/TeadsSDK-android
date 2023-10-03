package tv.teads.teadssdkdemo.format.mediation.applovin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import tv.teads.teadssdkdemo.databinding.FragmentRecyclerviewBinding
import tv.teads.teadssdkdemo.format.mediation.adapter.AppLovinRecyclerViewAdapter
import tv.teads.teadssdkdemo.format.mediation.identifier.AppLovinIdentifier
import tv.teads.teadssdkdemo.utils.BaseFragment

/**
 * Display inRead as Banner within a RecyclerView using AdMob Mediation.
 */
class AppLovinRecyclerViewFragment : BaseFragment() {
    private lateinit var binding: FragmentRecyclerviewBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentRecyclerviewBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setRecyclerViewAdapter(binding.recyclerView)
    }

    private fun setRecyclerViewAdapter(recyclerView: RecyclerView) {
        recyclerView.layoutManager = LinearLayoutManager(activity)

        val adUnit = AppLovinIdentifier.getAdUnitFromPid(pid)

        recyclerView.adapter = AppLovinRecyclerViewAdapter(adUnit, context, getTitle())
    }

    override fun getTitle(): String = "InRead AdMob RecyclerView"
}
