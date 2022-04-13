package tv.teads.teadssdkdemo.format.native

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_inread_recyclerview.*
import tv.teads.teadssdkdemo.R
import tv.teads.teadssdkdemo.utils.MarginItemDecoration
import tv.teads.teadssdkdemo.format.native.adapter.NativeRecyclerViewAdapter
import tv.teads.teadssdkdemo.utils.BaseFragment

/**
 * Native format within a RecyclerView
 *
 */
class NativeGridRecyclerViewFragment : BaseFragment() {

    private lateinit var adapter: NativeRecyclerViewAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_native_recyclerview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setRecyclerViewAdapter(recyclerView)
    }

    private fun setRecyclerViewAdapter(recyclerView: RecyclerView) {
        recyclerView.layoutManager = GridLayoutManager(activity, 2)

        adapter = NativeRecyclerViewAdapter(context, 124859, getTitle(), true)
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(MarginItemDecoration(resources.getDimensionPixelSize(R.dimen.item_margin)))
    }

    override fun getTitle(): String = "Native Grid RecyclerView"
}
