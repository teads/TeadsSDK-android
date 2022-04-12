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
import androidx.recyclerview.widget.RecyclerView
import tv.teads.teadssdkdemo.adapter.IntegrationItemAdapter
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
import tv.teads.teadssdkdemo.format.mediation.applovin.AppLovinGridRecyclerViewFragment
import tv.teads.teadssdkdemo.format.mediation.applovin.AppLovinRecyclerViewFragment
import tv.teads.teadssdkdemo.format.mediation.applovin.AppLovinScrollViewFragment
import tv.teads.teadssdkdemo.format.mediation.applovin.AppLovinWebViewFragment
import tv.teads.teadssdkdemo.format.native.NativeRecyclerViewFragment
import tv.teads.teadssdkdemo.utils.BaseFragment
import java.lang.IllegalStateException


/**
 * Empty fragment helping opening the navigation drawer
 */
class MainFragment : Fragment(), RadioGroup.OnCheckedChangeListener {

    private lateinit var mainView: View
    private lateinit var customPid: RadioButton
    private lateinit var containerCreativeSizes: View
    private lateinit var radioGroupCreativeSizes: RadioGroup
    private lateinit var radioGroupProvider: RadioGroup
    private lateinit var integrationsRecyclerView: RecyclerView

    private val inReadIntegrationList = listOf(
        IntegrationType("ScrollView", R.drawable.scrollview),
        IntegrationType("RecyclerView", R.drawable.tableview),
        IntegrationType("RecyclerView Grid", R.drawable.collectionview),
        IntegrationType("WebView", R.drawable.webview)
    )

    private val nativeIntegrationList = listOf(
        IntegrationType("RecyclerView", R.drawable.tableview),
        IntegrationType("RecyclerView Grid", R.drawable.collectionview),
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

    private fun getFragmentNativeDirect(position: Int): BaseFragment {
        return when (position) {
            0 -> NativeRecyclerViewFragment()
            1 -> InReadGridRecyclerViewFragment() // todo change to native
            else -> throw IllegalStateException()
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

    private fun getFragmentInReadAppLovin(position: Int): BaseFragment {
        return when (position) {
            0 -> AppLovinScrollViewFragment()
            1 -> AppLovinRecyclerViewFragment()
            2 -> AppLovinGridRecyclerViewFragment()
            3 -> AppLovinWebViewFragment()
            else -> AppLovinScrollViewFragment()
        }
    }

    private var mFormatSelected: FormatType = FormatType.INREAD
    private var mProviderSelected: ProviderType = ProviderType.DIRECT

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mainView = inflater.inflate(R.layout.fragment_main, container, false)

        mainView.apply {
            val containerFormat: RadioGroup = this.findViewById(R.id.container_format)
            radioGroupProvider = this.findViewById(R.id.container_provider)
            radioGroupCreativeSizes = this.findViewById(R.id.radiogroup_creative_size)
            containerCreativeSizes = this.findViewById(R.id.container_creative_size)
            customPid = this.findViewById(R.id.customButton)
            integrationsRecyclerView = this.findViewById(R.id.integrations_recycler_view)

            setIntegrationItems(inReadIntegrationList)
            setCreativeSizeChecked()
            setProviderSelected()

            containerFormat.setOnCheckedChangeListener(this@MainFragment)
            radioGroupProvider.setOnCheckedChangeListener(this@MainFragment)
            radioGroupCreativeSizes.setOnCheckedChangeListener(this@MainFragment)
            customPid.setOnClickListener { changePidDialog() }
        }

        return mainView
    }

    private fun setIntegrationItems(integratiosList: List<IntegrationType>) {
        integrationsRecyclerView.adapter = IntegrationItemAdapter(integratiosList) { position ->
            onIntegrationClicked(position)
        }
    }

    private fun onIntegrationClicked(position: Int) {
        when (mFormatSelected) {
            FormatType.INREAD -> changeFragmentForInRead(position)
            FormatType.NATIVE -> changeFragmentForNative(position)
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
            ProviderType.APPLOVIN -> {
                (activity as MainActivity).changeFragment(getFragmentInReadAppLovin(position))
            }
        }
    }

    private fun changeFragmentForNative(position: Int) {
        if (mProviderSelected == ProviderType.DIRECT) {
                (activity as MainActivity).changeFragment(getFragmentNativeDirect(position))
        }
    }

    private fun setFormatSelected(id: Int) {
        val availableFormatsMap = mapOf(
            R.id.inreadButton to FormatType.INREAD,
            R.id.nativeButton to FormatType.NATIVE
        )
        mFormatSelected = availableFormatsMap[id] ?: return

        radioGroupProvider.setAvailableOptions(mFormatSelected)

        when (mFormatSelected) {
            FormatType.INREAD -> {
                setIntegrationItems(inReadIntegrationList)
                containerCreativeSizes.visibility = View.VISIBLE

            }
            FormatType.NATIVE -> {
                setIntegrationItems(nativeIntegrationList)
                containerCreativeSizes.visibility = View.GONE
            }
        }
    }


    private fun setProviderSelected() {
        when (mProviderSelected) {
            ProviderType.DIRECT -> radioGroupProvider.check(R.id.directButton)
            ProviderType.ADMOB -> radioGroupProvider.check(R.id.admobButton)
            ProviderType.APPLOVIN -> radioGroupProvider.check(R.id.applovinButton)
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

        val child =
            radioGroupCreativeSizes.getChildAt(DirectIdentifier.getPositionByPid(pid)) as? RadioButton

        if (child == null) {
            radioGroupCreativeSizes.clearCheck()
            customPid.isChecked = true
        } else {
            customPid.isChecked = false
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
            }.setNegativeButton("Cancel") { _, _ -> customPid.isChecked = false }.show()
    }

    private fun RadioGroup.setAvailableOptions(formatType: FormatType) {
        when (formatType) {
            FormatType.INREAD -> {
                getChildAt(0).visibility = View.VISIBLE
                getChildAt(1).visibility = View.VISIBLE
                getChildAt(2).visibility = View.VISIBLE
            }
            FormatType.NATIVE -> {
                getChildAt(0)
                    .let { it as RadioButton }
                    .apply {
                        visibility = View.VISIBLE
                        isChecked = true
                    }
                getChildAt(1).visibility = View.INVISIBLE
                getChildAt(2).visibility = View.INVISIBLE
            }
        }
    }

    override fun onCheckedChanged(group: RadioGroup?, id: Int) {
        when (group?.id) {
            R.id.container_format -> {
                setFormatSelected(id)
            }
            R.id.radiogroup_creative_size -> {
                customPid.isChecked = false
                setCreativeSizePid(group, id)
            }
            else -> {
                mProviderSelected = when (id) {
                    R.id.directButton -> ProviderType.DIRECT
                    R.id.applovinButton -> ProviderType.APPLOVIN
                    else -> ProviderType.ADMOB
                }
            }
        }
    }
}
