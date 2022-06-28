package tv.teads.teadssdkdemo.format.nativead

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_inread_recyclerview.*
import tv.teads.teadssdkdemo.R
import tv.teads.teadssdkdemo.utils.MarginItemDecoration
import tv.teads.teadssdkdemo.format.nativead.adapter.NativeRecyclerViewAdapter
import tv.teads.teadssdkdemo.utils.BaseFragment

/**
 * Native format within a RecyclerView
 *
 */
class NativeRecyclerViewFragment : BaseFragment() {

    private lateinit var adapter: NativeRecyclerViewAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_native_recyclerview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setRecyclerViewAdapter(recyclerView)
    }

    private fun setRecyclerViewAdapter(recyclerView: RecyclerView) {
        recyclerView.layoutManager = LinearLayoutManager(activity)

        adapter = NativeRecyclerViewAdapter(context, pid, getTitle())
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(MarginItemDecoration(resources.getDimensionPixelSize(R.dimen.item_margin)))
    }

    override fun getTitle(): String = "Native RecyclerView"
}
