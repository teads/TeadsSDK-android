package tv.teads.teadssdkdemo.format.mediation.applovin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import tv.teads.teadssdkdemo.databinding.FragmentRecyclerviewBinding
import tv.teads.teadssdkdemo.format.mediation.adapter.AppLovinRecyclerViewAdapter
import tv.teads.teadssdkdemo.format.mediation.identifier.AppLovinIdentifier
import tv.teads.teadssdkdemo.utils.BaseFragment

/**
 * inRead format within a RecyclerView
 *
 */
class AppLovinGridRecyclerViewFragment : BaseFragment() {
    private lateinit var binding: FragmentRecyclerviewBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentRecyclerviewBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setRecyclerViewAdapter(binding.recyclerView)
    }

    private fun setRecyclerViewAdapter(recyclerView: RecyclerView) {
        recyclerView.layoutManager = GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false)

        val adUnit = AppLovinIdentifier.getAdUnitFromPid(pid)

        recyclerView.adapter = AppLovinRecyclerViewAdapter(adUnit, requireContext(), getTitle())
    }

    override fun getTitle(): String = "InRead AppLovin RecyclerView Grid"
}
