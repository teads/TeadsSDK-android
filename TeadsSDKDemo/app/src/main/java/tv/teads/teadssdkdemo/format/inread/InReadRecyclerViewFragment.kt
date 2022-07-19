package tv.teads.teadssdkdemo.format.inread

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_recyclerview.*
import tv.teads.teadssdkdemo.R
import tv.teads.teadssdkdemo.format.inread.adapter.SimpleRecyclerViewAdapter
import tv.teads.teadssdkdemo.utils.BaseFragment

/**
 * InRead format within a RecyclerView
 *
 */
class InReadRecyclerViewFragment : BaseFragment() {

    private lateinit var adapter: SimpleRecyclerViewAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_recyclerview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setRecyclerViewAdapter(recyclerView)
    }

    private fun setRecyclerViewAdapter(recyclerView: RecyclerView) {
        recyclerView.layoutManager = LinearLayoutManager(activity)

        adapter = SimpleRecyclerViewAdapter(context, pid, getTitle())
        recyclerView.adapter = adapter
    }

    override fun getTitle(): String = "InRead Direct RecyclerView"
}
