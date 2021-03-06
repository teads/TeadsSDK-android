package tv.teads.teadssdkdemo.format.mediation.mopub

import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.fragment_inread_recyclerview.*
import tv.teads.teadssdkdemo.MainActivity
import tv.teads.teadssdkdemo.R
import tv.teads.teadssdkdemo.format.mediation.adapter.MoPubRecyclerViewAdapter
import tv.teads.teadssdkdemo.format.mediation.identifier.MoPubIdentifier
import tv.teads.teadssdkdemo.format.mediation.identifier.MoPubIdentifier.MOPUB_ID
import tv.teads.teadssdkdemo.utils.BaseFragment
import java.util.*

class MoPubGridRecyclerViewFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_inread_recyclerview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (context == null)
            return
        setRecyclerViewAdapter(recyclerView)
    }

    private fun setRecyclerViewAdapter(recyclerView: RecyclerView) {
        recyclerView.layoutManager = GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false)

        val adUnit = MoPubIdentifier.getAdUnitFromPid(pid)

        val adapter = MoPubRecyclerViewAdapter(adUnit, context, getTitle())
        recyclerView.adapter = adapter
    }

    override fun getTitle(): String = "InRead MoPub RecyclerView Grid"
}
