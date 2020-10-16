package tv.teads.teadssdkdemo.format.mediation.admob

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_inread_recyclerview.*
import tv.teads.helper.TeadsBannerAdapterListener
import tv.teads.teadssdkdemo.R
import tv.teads.teadssdkdemo.format.mediation.adapter.AdMobRecyclerViewAdapter
import tv.teads.teadssdkdemo.utils.BaseFragment
import java.util.ArrayList

/**
 * Display inRead as Banner within a RecyclerView using AdMob Mediation.
 */
class AdMobRecyclerViewFragment : BaseFragment() {
    private lateinit var mListener: TeadsBannerAdapterListener

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_inread_recyclerview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // Set RecyclerView basic adapter
        setRecyclerViewAdapter(recyclerView)
    }

    private fun setRecyclerViewAdapter(recyclerView: RecyclerView) {
        val data = ArrayList<String>()

        for (i in 0..5) {
            data.add("")
        }

        recyclerView.layoutManager = LinearLayoutManager(activity)

        recyclerView.adapter = AdMobRecyclerViewAdapter(data, context)
    }

    override fun getTitle(): String = "AdMob RecyclerView"
}
