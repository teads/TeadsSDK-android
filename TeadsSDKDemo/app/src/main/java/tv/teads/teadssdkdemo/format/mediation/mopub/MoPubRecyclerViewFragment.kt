package tv.teads.teadssdkdemo.format.mediation.mopub

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_inread_recyclerview.*
import tv.teads.teadssdkdemo.R
import tv.teads.teadssdkdemo.format.mediation.adapter.MoPubRecyclerViewAdapter
import tv.teads.teadssdkdemo.format.mediation.data.MoPubIdentifier.MOPUB_ID
import tv.teads.teadssdkdemo.utils.BaseFragment
import java.util.*

class MoPubRecyclerViewFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_inread_recyclerview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setRecyclerViewAdapter(recyclerView)
    }

    private fun setRecyclerViewAdapter(recyclerView: RecyclerView) {
        val data = ArrayList<String>()

        for (i in 0..5) {
            data.add("")
        }

        recyclerView.layoutManager = LinearLayoutManager(activity)

        val adapter = MoPubRecyclerViewAdapter(data, MOPUB_ID, context)
        recyclerView.adapter = adapter
    }

    override fun getTitle(): String = "MoPub RecyclerView"
}
