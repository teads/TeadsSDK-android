package tv.teads.teadssdkdemo.format.inread

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_inread_recyclerview.*
import org.greenrobot.eventbus.Subscribe
import tv.teads.teadssdkdemo.R
import tv.teads.teadssdkdemo.format.adapter.SimpleRecyclerViewAdapter
import tv.teads.teadssdkdemo.utils.BaseFragment
import tv.teads.teadssdkdemo.utils.ReloadEvent
import java.util.*

/**
 * InRead format within a RecyclerView
 *
 */
class InReadRecyclerViewFragment : BaseFragment() {

    private lateinit var adapter: SimpleRecyclerViewAdapter

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

        for (i in 0..49) {
            data.add("Teads $i")
        }

        recyclerView.layoutManager = LinearLayoutManager(activity)

        adapter = SimpleRecyclerViewAdapter(data, 10, context, pid)
        recyclerView.adapter = adapter
    }
}
