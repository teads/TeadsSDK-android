package tv.teads.teadssdkdemo.format.infeed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import tv.teads.teadssdkdemo.R
import tv.teads.teadssdkdemo.databinding.FragmentGridRecyclerviewBinding
import tv.teads.teadssdkdemo.format.infeed.adapter.InFeedRecyclerViewAdapter
import tv.teads.teadssdkdemo.utils.BaseFragment
import tv.teads.teadssdkdemo.utils.MarginItemDecoration

/**
 * Native format within a RecyclerView
 *
 */
class InFeedGridRecyclerViewFragment : BaseFragment() {
    private lateinit var binding: FragmentGridRecyclerviewBinding
    private lateinit var adapter: InFeedRecyclerViewAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentGridRecyclerviewBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.findViewById<TextView>(R.id.integration_header).text = getTitle()
        setRecyclerViewAdapter(binding.recyclerView)
    }

    private fun setRecyclerViewAdapter(recyclerView: RecyclerView) {
        recyclerView.layoutManager = GridLayoutManager(activity, 2)

        adapter = InFeedRecyclerViewAdapter(requireContext(), pid, "", true)
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(MarginItemDecoration(resources.getDimensionPixelSize(R.dimen.item_margin)))
    }

    override fun getTitle(): String = "Native Grid RecyclerView"
}
