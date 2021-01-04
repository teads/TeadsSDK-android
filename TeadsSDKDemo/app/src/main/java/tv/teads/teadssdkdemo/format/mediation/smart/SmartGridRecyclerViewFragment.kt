package tv.teads.teadssdkdemo.format.mediation.smart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_inread_recyclerview.*
import tv.teads.teadssdkdemo.R
import tv.teads.teadssdkdemo.format.mediation.adapter.SmartRecyclerViewAdapter
import tv.teads.teadssdkdemo.format.mediation.identifier.SmartIdentifier
import tv.teads.teadssdkdemo.utils.BaseFragment

class SmartGridRecyclerViewFragment : BaseFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_inread_recyclerview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setRecyclerViewAdapter(recyclerView)
    }

    private fun setRecyclerViewAdapter(recyclerView: RecyclerView) {
        recyclerView.layoutManager = GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false)

        val adapter = SmartRecyclerViewAdapter(context!!, SmartIdentifier.SITE_ID,
                SmartIdentifier.PAGE_NAME, SmartIdentifier.SUPPLY_CHAIN, SmartIdentifier.getFormatFromPid(pid), getTitle())
        recyclerView.adapter = adapter
    }

    override fun getTitle(): String = "InRead Smart RecyclerView Grid"
}