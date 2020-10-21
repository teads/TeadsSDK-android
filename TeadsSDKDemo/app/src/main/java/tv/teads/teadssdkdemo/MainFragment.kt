package tv.teads.teadssdkdemo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import tv.teads.teadssdkdemo.data.FormatType
import tv.teads.teadssdkdemo.data.ProviderType
import tv.teads.teadssdkdemo.format.inread.InReadGridRecyclerViewFragment
import tv.teads.teadssdkdemo.format.inread.InReadRecyclerViewFragment
import tv.teads.teadssdkdemo.format.inread.InReadScrollViewFragment
import tv.teads.teadssdkdemo.format.inread.InReadWebViewFragment
import tv.teads.teadssdkdemo.format.mediation.admob.AdMobGridRecyclerViewFragment
import tv.teads.teadssdkdemo.format.mediation.admob.AdMobRecyclerViewFragment
import tv.teads.teadssdkdemo.format.mediation.admob.AdMobScrollViewFragment
import tv.teads.teadssdkdemo.format.mediation.admob.AdMobWebViewFragment
import tv.teads.teadssdkdemo.format.mediation.mopub.MoPubGridRecyclerViewFragment
import tv.teads.teadssdkdemo.format.mediation.mopub.MoPubRecyclerViewFragment
import tv.teads.teadssdkdemo.format.mediation.mopub.MopubScrollViewFragment
import tv.teads.teadssdkdemo.format.mediation.mopub.MopubWebViewFragment

/**
 * Empty fragment helping opening the navigation drawer
 */
class MainFragment : Fragment(), IntegrationAdapter.OnIntegrationClickedListener, RadioGroup.OnCheckedChangeListener {
    //private val mFragmentsNative = mapOf<>()
    private lateinit var mView: View

    private val mFragmentsInReadDirect = mapOf(
            0 to InReadScrollViewFragment(),
            1 to InReadRecyclerViewFragment(),
            2 to InReadGridRecyclerViewFragment(),
            3 to InReadWebViewFragment()
    )

    private val mFragmentsInReadAdmob = mapOf(
            0 to AdMobScrollViewFragment(),
            1 to AdMobRecyclerViewFragment(),
            2 to AdMobGridRecyclerViewFragment(),
            3 to AdMobWebViewFragment()
    )

    private val mFragmentsInReadMopub = mapOf(
            0 to MopubScrollViewFragment(),
            1 to MoPubRecyclerViewFragment(),
            2 to MoPubGridRecyclerViewFragment(),
            3 to MopubWebViewFragment()
    )

    private lateinit var mRecycler: RecyclerView

    private var mFormatSelected: FormatType = FormatType.INREAD
    private var mProviderSelected: ProviderType = ProviderType.DIRECT

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_main, container, false)

        mView.apply {
            mRecycler = this.findViewById(R.id.integrations)
            val containerFormat: RadioGroup = this.findViewById(R.id.container_format)
            val containerProvider: RadioGroup = this.findViewById(R.id.container_provider)
            val containerAdTypes: RadioGroup = this.findViewById(R.id.container_creative_size)

            containerFormat.setOnCheckedChangeListener(this@MainFragment)
            containerProvider.setOnCheckedChangeListener(this@MainFragment)
            containerAdTypes.setOnCheckedChangeListener(this@MainFragment)

            setDirectCreativeSizeChecked(containerAdTypes)
        }

        mRecycler.layoutManager = GridLayoutManager(activity, 2, GridLayoutManager.VERTICAL, false)
        mRecycler.adapter = IntegrationAdapter(this, activity!!)

        return mView
    }

    override fun onIntegrationClicked(position: Int) {
        when (mFormatSelected) {
            FormatType.INREAD -> changeFragmentForInRead(position)
        }
    }

    private fun changeFragmentForInRead(position: Int) {
        when (mProviderSelected) {
            ProviderType.DIRECT -> {
                (activity as MainActivity).changeFragment(mFragmentsInReadDirect[position]!!)
            }
            ProviderType.ADMOB -> {
                (activity as MainActivity).changeFragment(mFragmentsInReadAdmob[position]!!)
            }
            ProviderType.MOPUB -> {
                (activity as MainActivity).changeFragment(mFragmentsInReadMopub[position]!!)
            }
        }
    }

    private fun setFormatSelected(id: Int) {
        mFormatSelected = when (id) {
            R.id.inreadButton -> FormatType.INREAD
            else -> FormatType.INREAD
        }
    }

    private fun setDirectCreativeSizePid(group: RadioGroup, id: Int) {
        when (mFormatSelected) {
            FormatType.INREAD -> {
                val pid: Int = group.findViewById<View>(id).tag.toString().toInt()
                (activity as MainActivity).setPid(pid)
            }
        }
    }

    private fun setDirectCreativeSizeChecked(container: RadioGroup) {
        val pid = (activity as MainActivity).getPid(context!!)
        for (i in 0 until container.childCount) {
            val child = container.getChildAt(i) as RadioButton
            if (child.tag.toString().toInt() == pid)
                child.isChecked = true
        }
    }

    override fun onCheckedChanged(group: RadioGroup?, id: Int) {
        when (group?.id) {
            R.id.container_format -> setFormatSelected(id)
            R.id.container_creative_size -> setDirectCreativeSizePid(group, id)
            else -> {
                mView.findViewById<View>(R.id.ad_types_block).visibility = View.GONE
                mProviderSelected = when (id) {
                    R.id.directButton -> {
                        mView.findViewById<View>(R.id.ad_types_block).visibility = View.VISIBLE
                        ProviderType.DIRECT
                    }
                    R.id.mopubButton -> ProviderType.MOPUB
                    else -> ProviderType.ADMOB
                }
            }
        }
    }
}
