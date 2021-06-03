package tv.teads.teadssdkdemo

import android.annotation.SuppressLint
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.helper.widget.Flow
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import tv.teads.teadssdkdemo.data.FormatType
import tv.teads.teadssdkdemo.data.IntegrationType
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
import tv.teads.teadssdkdemo.format.mediation.smart.SmartGridRecyclerViewFragment
import tv.teads.teadssdkdemo.format.mediation.smart.SmartRecyclerViewFragment
import tv.teads.teadssdkdemo.format.mediation.smart.SmartScrollViewFragment
import tv.teads.teadssdkdemo.format.mediation.smart.SmartWebViewFragment
import tv.teads.teadssdkdemo.utils.BaseFragment


/**
 * Empty fragment helping opening the navigation drawer
 */
class MainFragment : Fragment(), RadioGroup.OnCheckedChangeListener {
    //private val mFragmentsNative = mapOf<>()
    private lateinit var mMainView: View
    private lateinit var mCustomPid: RadioButton
    private lateinit var mContainerCreativeSizes: RadioGroup

    private val mIntegrationList = listOf(
            IntegrationType("ScrollView", R.drawable.scrollview),
            IntegrationType("RecyclerView", R.drawable.tableview),
            IntegrationType("RecyclerView Grid", R.drawable.collectionview),
            IntegrationType("WebView", R.drawable.webview)
    )

    private fun getFragmentInReadDirect(position: Int): BaseFragment {
        return when (position) {
            0 -> InReadScrollViewFragment()
            1 -> InReadRecyclerViewFragment()
            2 -> InReadGridRecyclerViewFragment()
            3 -> InReadWebViewFragment()
            else -> InReadScrollViewFragment()
        }
    }

    private fun getFragmentInReadAdmob(position: Int): BaseFragment {
        return when (position) {
            0 -> AdMobScrollViewFragment()
            1 -> AdMobRecyclerViewFragment()
            2 -> AdMobGridRecyclerViewFragment()
            3 -> AdMobWebViewFragment()
            else -> AdMobScrollViewFragment()
        }
    }

    private fun getFragmentInReadMopub(position: Int): BaseFragment {
        return when (position) {
            0 -> MopubScrollViewFragment()
            1 -> MoPubRecyclerViewFragment()
            2 -> MoPubGridRecyclerViewFragment()
            3 -> MopubWebViewFragment()
            else -> MopubScrollViewFragment()
        }
    }

    private fun getFragmentInReadSmart(position: Int): BaseFragment {
        return when (position) {
            0 -> SmartScrollViewFragment()
            1 -> SmartRecyclerViewFragment()
            2 -> SmartGridRecyclerViewFragment()
            3 -> SmartWebViewFragment()
            else -> SmartScrollViewFragment()
        }
    }

    private var mFormatSelected: FormatType = FormatType.INREAD
    private var mProviderSelected: ProviderType = ProviderType.DIRECT

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        mMainView = inflater.inflate(R.layout.fragment_main, container, false)

        mMainView.apply {
            val containerFormat: RadioGroup = this.findViewById(R.id.container_format)
            val containerProvider: RadioGroup = this.findViewById(R.id.container_provider)
            val containerIntegration: ConstraintLayout = this.findViewById(R.id.integration_container)
            mContainerCreativeSizes = this.findViewById(R.id.container_creative_size)
            mCustomPid = this.findViewById(R.id.customButton)

            setIntegrationItems(containerIntegration)
            setCreativeSizeChecked()

            containerFormat.setOnCheckedChangeListener(this@MainFragment)
            containerProvider.setOnCheckedChangeListener(this@MainFragment)
            mContainerCreativeSizes.setOnCheckedChangeListener(this@MainFragment)
            mCustomPid.setOnClickListener { changePidDialog() }
        }

        return mMainView
    }

    private fun setIntegrationItems(container: ConstraintLayout) {
        val inflater = LayoutInflater.from(activity)
        val flow = container.getChildAt(0) as Flow
        val ids = IntArray(mIntegrationList.size)

        mIntegrationList.forEachIndexed { index, it ->
            val view = inflater.inflate(R.layout.item_integration_type, container, false)

            view.id = it.image
            view.findViewById<ImageView>(R.id.image_integration).setImageResource(it.image)
            view.findViewById<TextView>(R.id.title_integration).text = it.name
            view.setOnClickListener { onIntegrationClicked(index) }

            ids[index] = view.id

            container.addView(view)
        }

        flow.referencedIds = ids
    }

    private fun onIntegrationClicked(position: Int) {
        when (mFormatSelected) {
            FormatType.INREAD -> changeFragmentForInRead(position)
            else -> {
            }
        }
    }

    private fun changeFragmentForInRead(position: Int) {
        when (mProviderSelected) {
            ProviderType.DIRECT -> {
                (activity as MainActivity).changeFragment(getFragmentInReadDirect(position))
            }
            ProviderType.ADMOB -> {
                (activity as MainActivity).changeFragment(getFragmentInReadAdmob(position))
            }
            ProviderType.MOPUB -> {
                (activity as MainActivity).changeFragment(getFragmentInReadMopub(position))
            }
            ProviderType.SMART -> {
                (activity as MainActivity).changeFragment(getFragmentInReadSmart(position))
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
            else -> {
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

        AlertDialog.Builder(requireActivity())
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

    private fun showDialogSoon() {
        AlertDialog.Builder(requireContext())
                .setTitle("Coming soon!")
                .setPositiveButton(android.R.string.yes) { _, _ -> }
                .show()
    }

    override fun onCheckedChanged(group: RadioGroup?, id: Int) {
        when (group?.id) {
            R.id.container_format -> {
                when (id) {
                    R.id.inreadButton -> setFormatSelected(id)
                    R.id.nativeButton -> {
                        // TODO COMING SOON
                        group.findViewById<RadioButton>(id)
                                .setTextColor(ContextCompat.getColor(requireContext(), R.color.textColorNoBg))
                        group.check(R.id.inreadButton)
                        showDialogSoon()
                    }
                }
            }
            R.id.container_creative_size -> {
                mCustomPid.isChecked = false
                setCreativeSizePid(group, id)
            }
            else -> {
                mProviderSelected = when (id) {
                    R.id.directButton -> ProviderType.DIRECT
                    R.id.mopubButton -> ProviderType.MOPUB
                    R.id.smartButton -> ProviderType.SMART
                    else -> ProviderType.ADMOB
                }
            }
        }
    }
}
