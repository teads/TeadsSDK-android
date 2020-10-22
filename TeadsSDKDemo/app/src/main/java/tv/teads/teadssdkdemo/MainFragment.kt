package tv.teads.teadssdkdemo

import android.annotation.SuppressLint
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import tv.teads.teadssdkdemo.data.FormatType
import tv.teads.teadssdkdemo.data.ProviderType
import tv.teads.teadssdkdemo.format.inread.InReadGridRecyclerViewFragment
import tv.teads.teadssdkdemo.format.inread.InReadRecyclerViewFragment
import tv.teads.teadssdkdemo.format.inread.InReadScrollViewFragment
import tv.teads.teadssdkdemo.format.inread.InReadWebViewFragment
import tv.teads.teadssdkdemo.format.inread.identifier.DirectIdentifier
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
    private lateinit var mMainView: View
    private lateinit var mCustomPid: RadioButton
    private lateinit var mContainerCreativeSizes: RadioGroup

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
        mMainView = inflater.inflate(R.layout.fragment_main, container, false)

        mMainView.apply {
            mRecycler = this.findViewById(R.id.integrations)
            val containerFormat: RadioGroup = this.findViewById(R.id.container_format)
            val containerProvider: RadioGroup = this.findViewById(R.id.container_provider)
            mContainerCreativeSizes = this.findViewById(R.id.container_creative_size)
            mCustomPid = this.findViewById(R.id.customButton)

            containerFormat.setOnCheckedChangeListener(this@MainFragment)
            containerProvider.setOnCheckedChangeListener(this@MainFragment)
            mContainerCreativeSizes.setOnCheckedChangeListener(this@MainFragment)
            mCustomPid.setOnClickListener { changePidDialog() }

            setCreativeSizeChecked()
        }

        mRecycler.layoutManager = GridLayoutManager(activity, 2, GridLayoutManager.VERTICAL, false)
        mRecycler.adapter = IntegrationAdapter(this, activity!!)

        return mMainView
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

    private fun setCreativeSizePid(group: RadioGroup, id: Int) {
        when (mFormatSelected) {
            FormatType.INREAD -> {
                val radioPid = group.findViewById<View>(id) as? RadioButton

                if (radioPid != null && radioPid.isChecked) {
                    val pid: Int = radioPid.tag.toString().toInt()
                    (activity as MainActivity).setPid(pid)
                }
            }
        }
    }

    private fun setCreativeSizeChecked() {
        val pid = (activity as MainActivity).getPid()

        val child = mContainerCreativeSizes.getChildAt(DirectIdentifier.getPositionByPid(pid)) as? RadioButton

        if (child == null) {
            mContainerCreativeSizes.clearCheck()
            mCustomPid.isChecked = true
        } else {
            mCustomPid.isChecked = false
            child.isChecked = child.tag.toString().toInt() == pid
        }
    }

    private fun changePidDialog() {
        @SuppressLint("InflateParams") val view = layoutInflater.inflate(R.layout.dialog_pid_content, null)
        val input = view.findViewById<EditText>(R.id.pidEditText)
        input.setText((activity as MainActivity).getPid().toString())
        input.setLines(1)
        input.setSingleLine(true)

        AlertDialog.Builder(activity!!)
                .setTitle("Set custom PID")
                .setView(view)
                .setPositiveButton("Save") { _, _ ->
                    val pidString = input.text.toString()
                    val pid = if (pidString.isEmpty()) {
                        MainActivity.SHAREDPREF_PID_DEFAULT
                    } else Integer.parseInt(pidString)
                    Toast.makeText(activity, "Setting custom PID is for Direct only", Toast.LENGTH_SHORT).show()
                    PreferenceManager.getDefaultSharedPreferences(activity).edit()
                            .putInt(MainActivity.SHAREDPREF_PID, pid)
                            .apply()
                    setCreativeSizeChecked()
                }.setNegativeButton("Cancel") { _, _ -> mCustomPid.isChecked = false }.show()
    }

    override fun onCheckedChanged(group: RadioGroup?, id: Int) {
        when (group?.id) {
            R.id.container_format -> setFormatSelected(id)
            R.id.container_creative_size -> {
                mCustomPid.isChecked = false
                setCreativeSizePid(group, id)
            }
            else -> {
                mProviderSelected = when (id) {
                    R.id.directButton -> ProviderType.DIRECT
                    R.id.mopubButton -> ProviderType.MOPUB
                    else -> ProviderType.ADMOB
                }
            }
        }
    }
}
