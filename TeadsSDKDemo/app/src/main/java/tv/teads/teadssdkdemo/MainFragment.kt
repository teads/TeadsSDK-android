package tv.teads.teadssdkdemo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import tv.teads.teadssdkdemo.data.FORMAT_TYPE
import tv.teads.teadssdkdemo.data.IntegrationType
import tv.teads.teadssdkdemo.data.PROVIDER_TYPE
import tv.teads.teadssdkdemo.format.inread.InReadRecyclerViewFragment
import tv.teads.teadssdkdemo.format.inread.InReadScrollViewFragment
import tv.teads.teadssdkdemo.format.inread.InReadWebViewFragment
import tv.teads.teadssdkdemo.utils.BaseFragment

/**
 * Empty fragment helping opening the navigation drawer
 */
class MainFragment : Fragment(), IntegrationAdapter.OnIntegrationClickedListener, RadioGroup.OnCheckedChangeListener {
    private val mIntegrationList = listOf(
            IntegrationType("ScrollView", R.drawable.scrollview),
            IntegrationType("RecyclerView", R.drawable.tableview),
            IntegrationType("CollectionView", R.drawable.collectionview),
            IntegrationType("WebView", R.drawable.webview)
    )
    //private val mFragmentsNative = mapOf<>()

    private val mFragmentsInReadDirect = mapOf(
            0 to InReadScrollViewFragment(),
            1 to InReadRecyclerViewFragment(),
            2 to InReadRecyclerViewFragment(),
            3 to InReadWebViewFragment()
    )

    private val mFragmentsInReadAdmob = mapOf(
            0 to InReadScrollViewFragment(),
            1 to InReadRecyclerViewFragment(),
            2 to InReadRecyclerViewFragment(),
            3 to InReadWebViewFragment()
    )

    private val mFragmentsInReadMopub = mapOf(
            0 to InReadScrollViewFragment(),
            1 to InReadRecyclerViewFragment(),
            2 to InReadRecyclerViewFragment(),
            3 to InReadWebViewFragment()
    )

    private lateinit var mRecycler: RecyclerView
    private lateinit var mContainerFormat: RadioGroup
    private lateinit var mContainerProvider: RadioGroup

    private var mFormatSelected: FORMAT_TYPE = FORMAT_TYPE.INREAD
    private var mProviderSelected: PROVIDER_TYPE = PROVIDER_TYPE.DIRECT

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_main, container, false)

        mRecycler = view.findViewById(R.id.integrations)
        mContainerFormat = view.findViewById(R.id.container_format)
        mContainerProvider = view.findViewById(R.id.container_provider)

        mContainerFormat.setOnCheckedChangeListener(this)
        mContainerProvider.setOnCheckedChangeListener(this)

        mRecycler.layoutManager = GridLayoutManager(activity, 2, GridLayoutManager.VERTICAL, false)
        mRecycler.adapter = IntegrationAdapter(this, activity!!, mIntegrationList)

        return view
    }

    override fun onIntegrationClicked(position: Int) {
        when (mFormatSelected) {
            FORMAT_TYPE.INREAD -> changeFragmentForInRead(position)
        }
    }

    private fun changeFragmentForInRead(position: Int) {
        when (mProviderSelected) {
            PROVIDER_TYPE.DIRECT -> {
                (activity as MainActivity).changeFragment(mFragmentsInReadDirect[position]!!)
            }
            PROVIDER_TYPE.ADMOB -> {
                (activity as MainActivity).changeFragment(mFragmentsInReadAdmob[position]!!)
            }
            PROVIDER_TYPE.MOPUB -> {
                (activity as MainActivity).changeFragment(mFragmentsInReadMopub[position]!!)
            }
        }
    }

    override fun onCheckedChanged(group: RadioGroup?, id: Int) {
        if (group?.id == R.id.container_format) {
            mFormatSelected = if (id == R.id.inreadButton) FORMAT_TYPE.INREAD else FORMAT_TYPE.NATIVE
        } else {
            mProviderSelected = when (id) {
                R.id.directButton -> PROVIDER_TYPE.DIRECT
                R.id.mopubButton -> PROVIDER_TYPE.MOPUB
                else -> PROVIDER_TYPE.ADMOB
            }
        }
    }
}
