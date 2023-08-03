package tv.teads.teadssdkdemo.format.mediation.smart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_recyclerview.*
import tv.teads.teadssdkdemo.R
import tv.teads.teadssdkdemo.format.mediation.adapter.AppLovinNativeRecyclerViewAdapter
import tv.teads.teadssdkdemo.format.mediation.adapter.SmartNativeRecyclerViewAdapter
import tv.teads.teadssdkdemo.utils.BaseFragment
import tv.teads.teadssdkdemo.utils.MarginItemDecoration

class SmartNativeRecyclerViewFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_recyclerview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setRecyclerViewAdapter(recyclerView)
    }

    private fun setRecyclerViewAdapter(recyclerView: RecyclerView) {
        recyclerView.layoutManager = LinearLayoutManager(activity)

        val adapter = SmartNativeRecyclerViewAdapter(context, getTitle())
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(MarginItemDecoration(resources.getDimensionPixelSize(R.dimen.item_margin)))
    }

    override fun getTitle(): String = "Smart Native RecyclerView"
}