package tv.teads.teadssdkdemo.format.inread

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import tv.teads.teadssdkdemo.databinding.FragmentRecyclerviewBinding
import tv.teads.teadssdkdemo.format.inread.adapter.SimpleRecyclerViewAdapter
import tv.teads.teadssdkdemo.utils.BaseFragment
/**
 *
 * inRead format within a RecyclerView
 *
 */
class InReadGridRecyclerViewFragment : BaseFragment() {
    private lateinit var binding: FragmentRecyclerviewBinding
    private lateinit var adapter: SimpleRecyclerViewAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentRecyclerviewBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setRecyclerViewAdapter(binding.recyclerView)
    }

    private fun setRecyclerViewAdapter(recyclerView: RecyclerView) {
        recyclerView.layoutManager = GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false)

        adapter = SimpleRecyclerViewAdapter(context, pid, getTitle())
        recyclerView.adapter = adapter
    }

    override fun getTitle(): String = "InRead Direct RecyclerView Grid"
}
