package tv.teads.teadssdkdemo

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_main.*
import tv.teads.teadssdkdemo.adapter.IntegrationItemAdapter
import tv.teads.teadssdkdemo.data.FormatType
import tv.teads.teadssdkdemo.data.IntegrationType
import tv.teads.teadssdkdemo.data.SessionDataSource
import tv.teads.teadssdkdemo.data.ProviderType
import tv.teads.teadssdkdemo.format.inread.InReadRecyclerViewFragment
import tv.teads.teadssdkdemo.format.inread.InReadScrollViewFragment
import tv.teads.teadssdkdemo.format.inread.InReadWebViewFragment
import tv.teads.teadssdkdemo.format.inread.identifier.DirectIdentifier
import tv.teads.teadssdkdemo.format.mediation.admob.*
import tv.teads.teadssdkdemo.format.mediation.applovin.*
import tv.teads.teadssdkdemo.format.infeed.InFeedGridRecyclerViewFragment
import tv.teads.teadssdkdemo.format.infeed.InFeedRecyclerViewFragment
import tv.teads.teadssdkdemo.format.infeed.InFeedScrollViewFragment
import tv.teads.teadssdkdemo.format.mediation.smart.*
import tv.teads.teadssdkdemo.utils.BaseFragment
import tv.teads.teadssdkdemo.utils.toDefaultPid


/**
 * Empty fragment helping opening the navigation drawer
 */
class MainFragment : BaseFragment(), RadioGroup.OnCheckedChangeListener {

    private lateinit var mainView: View
    private lateinit var customPid: Button
    private lateinit var containerCreativeSizes: View
    private lateinit var radioGroupCreativeSizes: RadioGroup
    private lateinit var radioGroupProvider: RadioGroup
    private lateinit var integrationsRecyclerView: RecyclerView

    private val inReadIntegrationList = listOf(
        IntegrationType("ScrollView", R.drawable.scrollview),
        IntegrationType("RecyclerView", R.drawable.tableview),
        IntegrationType("WebView", R.drawable.webview)
    )

    private val nativeIntegrationList = listOf(
        IntegrationType("ScrollView", R.drawable.scrollview),
        IntegrationType("RecyclerView", R.drawable.tableview),
        IntegrationType("RecyclerView Grid", R.drawable.collectionview),
    )

    private fun getFragmentInReadDirect(position: Int): BaseFragment {
        return when (position) {
            0 -> InReadScrollViewFragment()
            1 -> InReadRecyclerViewFragment()
            2 -> InReadWebViewFragment()
            else -> InReadScrollViewFragment()
        }
    }

    private fun getFragmentNativeDirect(position: Int): BaseFragment {
        return when (position) {
            0 -> InFeedScrollViewFragment()
            1 -> InFeedRecyclerViewFragment()
            2 -> InFeedGridRecyclerViewFragment()
            else -> throw IllegalStateException()
        }
    }

    private fun getFragmentAdMobNative(position: Int): BaseFragment {
        return when (position) {
            0 -> AdMobNativeRecyclerViewFragment()
            1 -> AdMobNativeGridRecyclerViewFragment()
            else -> throw IllegalStateException()
        }
    }

    private fun getFragmentSmartNative(position: Int): BaseFragment {
        return when (position) {
            0 -> SmartNativeRecyclerViewFragment()
            1 -> SmartNativeGridRecyclerViewFragment()
            else -> throw IllegalStateException()
        }
    }

    private fun getFragmentAppLovinNative(position: Int): BaseFragment {
        return when (position) {
            0 -> AppLovinNativeRecyclerViewFragment()
            1 -> AppLovinNativeGridRecyclerViewFragment()
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

    private fun getFragmentInReadSmart(position: Int): BaseFragment {
        return when (position) {
            0 -> SmartScrollViewFragment()
            1 -> SmartRecyclerViewFragment()
            2 -> SmartWebViewFragment()
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

    override fun getTitle(): String {
        return ""
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mainView = inflater.inflate(R.layout.fragment_main, container, false)

        mainView.apply {
            customPid = this.findViewById(R.id.customButton)
            customPid.setOnClickListener { changePidDialog() }
            val containerFormat: RadioGroup = this.findViewById(R.id.container_format)
            radioGroupProvider = this.findViewById(R.id.container_provider)
            radioGroupCreativeSizes = this.findViewById(R.id.radiogroup_creative_size)
            containerCreativeSizes = this.findViewById(R.id.container_creative_size)
            integrationsRecyclerView = this.findViewById(R.id.integrations_recycler_view)

            initIntegrationItems()
            setCreativeSizeChecked()
            setProviderSelected()
            showCurrentPid()
            setMediationIntegrationConstraints()

            containerFormat.setOnCheckedChangeListener(this@MainFragment)
            radioGroupProvider.setOnCheckedChangeListener(this@MainFragment)
            radioGroupCreativeSizes.setOnCheckedChangeListener(this@MainFragment)
        }

        return mainView
    }

    private fun setIntegrationItems(integratiosList: List<IntegrationType>) {
        integrationsRecyclerView.adapter = IntegrationItemAdapter(integratiosList) { position ->
            onIntegrationClicked(position)
        }
    }

    private fun initIntegrationItems() {
        val integrationTypeList = when (SessionDataSource.selectedFormat) {
            FormatType.INREAD -> inReadIntegrationList
            FormatType.INFEED -> nativeIntegrationList
        }
        setIntegrationItems(integrationTypeList)
    }

    private fun onIntegrationClicked(position: Int) {
        when (SessionDataSource.selectedFormat) {
            FormatType.INREAD -> changeFragmentForInRead(position)
            FormatType.INFEED -> changeFragmentForNative(position)
        }
    }

    private fun changeFragmentForInRead(position: Int) {
        when (SessionDataSource.selectedProvider) {
            ProviderType.DIRECT -> {
                (activity as MainActivity).changeFragment(getFragmentInReadDirect(position))
            }
            ProviderType.ADMOB -> {
                (activity as MainActivity).changeFragment(getFragmentInReadAdmob(position))
            }
            ProviderType.SMART -> {
                (activity as MainActivity).changeFragment(getFragmentInReadSmart(position))
            }
            ProviderType.APPLOVIN -> {
                (activity as MainActivity).changeFragment(getFragmentInReadAppLovin(position))
            }
        }
    }

    private fun changeFragmentForNative(position: Int) {
        when (SessionDataSource.selectedProvider) {
            ProviderType.DIRECT -> {
                (activity as MainActivity).changeFragment(getFragmentNativeDirect(position))
            }
            ProviderType.ADMOB -> {
                (activity as MainActivity).changeFragment(getFragmentAdMobNative(position))
            }
            ProviderType.SMART -> {
                (activity as MainActivity).changeFragment(getFragmentSmartNative(position))
            }
            ProviderType.APPLOVIN -> {
                (activity as MainActivity).changeFragment(getFragmentAppLovinNative(position))
            }
        }
    }

    private fun setFormatSelected(id: Int) {
        val availableFormatsMap = mapOf(
            R.id.inreadButton to FormatType.INREAD,
            R.id.infeedButton to FormatType.INFEED
        )
        SessionDataSource.selectedFormat = availableFormatsMap[id] ?: return

        when (SessionDataSource.selectedFormat) {
            FormatType.INREAD -> {
                setIntegrationItems(inReadIntegrationList)
                containerCreativeSizes.visibility = View.VISIBLE
            }
            FormatType.INFEED -> {
                setIntegrationItems(nativeIntegrationList)
                containerCreativeSizes.visibility = View.GONE
            }
        }

        showCurrentPid()
        setDirectIntegrationConstraints()
        setMediationIntegrationConstraints()
    }


    private fun setProviderSelected() {
        when (SessionDataSource.selectedProvider) {
            ProviderType.DIRECT -> radioGroupProvider.check(R.id.directButton)
            ProviderType.ADMOB -> radioGroupProvider.check(R.id.admobButton)
            ProviderType.SMART -> radioGroupProvider.check(R.id.smartButton)
            ProviderType.APPLOVIN -> radioGroupProvider.check(R.id.applovinButton)
        }

        setDirectIntegrationConstraints()
    }

    private fun setCreativeSizePid(group: RadioGroup, id: Int) {
        when (SessionDataSource.selectedFormat) {
            FormatType.INREAD -> {
                val radioPid = group.findViewById<View>(id) as? RadioButton

                if (radioPid != null && radioPid.isChecked) {
                    val pid: Int = radioPid.tag.toString().toInt()
                    SessionDataSource.setPid(requireContext(), pid, SessionDataSource.selectedFormat)
                    showCurrentPid()
                }
            }
            else -> {
            }
        }
    }

    private fun setCreativeSizeChecked(formatType: FormatType? = null) {
        val pid = SessionDataSource.getPid(requireContext(), formatType)

        val child =
            radioGroupCreativeSizes.getChildAt(DirectIdentifier.getPositionByPid(pid)) as? RadioButton

        if (child == null) {
            radioGroupCreativeSizes.clearCheck()
        } else {
            child.isChecked = child.tag.toString().toInt() == pid
        }
    }

    private fun changePidDialog() {
        val directFormats = arrayOf(SessionDataSource.selectedFormat)
        val stringfiedDirectFormats = directFormats.map { it.value }.toTypedArray()
        val checkedItem = directFormats.indexOf(SessionDataSource.selectedFormat)
        var nextFormat = SessionDataSource.selectedFormat // init with current

        @SuppressLint("InflateParams") val view = layoutInflater.inflate(R.layout.dialog_pid_content, null)
        val input = view.findViewById<EditText>(R.id.pidEditText)
        input.setLines(1)
        input.setSingleLine(true)
        input.hint = "type your custom pid"
        input.setText(SessionDataSource.getPid(requireContext(), SessionDataSource.selectedFormat).toString())

        AlertDialog.Builder(requireActivity())
            .setTitle("Set custom PID")
            .setView(view)
            .setSingleChoiceItems(stringfiedDirectFormats, checkedItem) { _, selected ->
                nextFormat = directFormats[selected]
                input.setText(SessionDataSource.getPid(requireContext(), nextFormat).toString())
            }
            .setPositiveButton("Save") { _, _ ->
                val pidString = input.text.toString().takeIf { it.isNotBlank() }
                val pid = pidString?.let { Integer.parseInt(it) } ?: nextFormat.toDefaultPid()

                SessionDataSource.setPid(requireContext(), pid, nextFormat)
                showCurrentPid()
                setCreativeSizeChecked(nextFormat)
            }
            .setNegativeButton("Cancel") { _, _ -> }
            .setNeutralButton("Set Default") { _, _ ->
                SessionDataSource.setPid(requireContext(), nextFormat.toDefaultPid(), nextFormat)
                showCurrentPid()
                setCreativeSizeChecked(nextFormat)
            }
            .show()
    }

    private fun setDirectIntegrationConstraints() {
        customPid.visibility = if (SessionDataSource.selectedProvider == ProviderType.DIRECT) {
            View.VISIBLE
        } else {
            View.INVISIBLE
        }
        when {
            SessionDataSource.selectedProvider != ProviderType.DIRECT && SessionDataSource.selectedFormat == FormatType.INFEED -> {
                setIntegrationItems(nativeIntegrationList.filter { it.name != "ScrollView" })
            }
            SessionDataSource.selectedProvider == ProviderType.DIRECT && SessionDataSource.selectedFormat == FormatType.INFEED -> {
                setIntegrationItems(nativeIntegrationList)
            }
        }
    }

    private fun setMediationIntegrationConstraints() {
        if (SessionDataSource.selectedProvider == ProviderType.SMART) {
            containerCreativeSizes.visibility = View.GONE
        } else {
            containerCreativeSizes.visibility = View.VISIBLE
        }
    }

    private fun showCurrentPid() {
        customPid.text = "Change PID: ${SessionDataSource.getPid(requireContext(), SessionDataSource.selectedFormat)}"
    }

    override fun onCheckedChanged(group: RadioGroup?, id: Int) {
        when (group?.id) {
            R.id.container_format -> {
                setFormatSelected(id)
            }
            R.id.radiogroup_creative_size -> {
                setCreativeSizePid(group, id)
            }
            R.id.container_provider -> {
                SessionDataSource.selectedProvider = when (id) {
                    R.id.directButton -> ProviderType.DIRECT
                    R.id.applovinButton -> ProviderType.APPLOVIN
                    R.id.smartButton -> ProviderType.SMART
                    R.id.admobButton -> ProviderType.ADMOB
                    else -> throw IllegalStateException()
                }

                setDirectIntegrationConstraints()
                setMediationIntegrationConstraints()
            }
        }
    }
}
