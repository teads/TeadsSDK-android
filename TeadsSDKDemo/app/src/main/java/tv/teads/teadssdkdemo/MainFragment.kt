package tv.teads.teadssdkdemo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import tv.teads.teadssdkdemo.data.FormatType
import tv.teads.teadssdkdemo.data.IntegrationType
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
    private val mIntegrationList = listOf(
            IntegrationType("ScrollView", R.drawable.scrollview),
            IntegrationType("RecyclerView", R.drawable.tableview),
            IntegrationType("RecyclerView Grid", R.drawable.collectionview),
            IntegrationType("WebView", R.drawable.webview)
    )
    //private val mFragmentsNative = mapOf<>()

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
    private lateinit var mContainerFormat: RadioGroup
    private lateinit var mContainerProvider: RadioGroup

    private var mFormatSelected: FormatType = FormatType.INREAD
    private var mProviderSelected: ProviderType = ProviderType.DIRECT

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

    override fun onCheckedChanged(group: RadioGroup?, id: Int) {
        if (group?.id == R.id.container_format) {
            mFormatSelected = if (id == R.id.inreadButton) FormatType.INREAD else FormatType.NATIVE
        } else {
            mProviderSelected = when (id) {
                R.id.directButton -> ProviderType.DIRECT
                R.id.mopubButton -> ProviderType.MOPUB
                else -> ProviderType.ADMOB
            }
        }
    }
}
