package tv.teads.teadssdkdemo

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import tv.teads.teadssdkdemo.adapter.IntegrationItemAdapter
import tv.teads.teadssdkdemo.data.FormatType
import tv.teads.teadssdkdemo.data.IntegrationType
import tv.teads.teadssdkdemo.data.ProviderType
import tv.teads.teadssdkdemo.data.SessionDataSource
import tv.teads.teadssdkdemo.databinding.FragmentMainBinding
import tv.teads.teadssdkdemo.format.headerbidding.prebid.PluginRendererScrollViewFragment
import tv.teads.teadssdkdemo.format.headerbidding.prebid.StandaloneIntegrationScrollViewFragment
import tv.teads.teadssdkdemo.format.infeed.InFeedGridRecyclerViewFragment
import tv.teads.teadssdkdemo.format.infeed.InFeedRecyclerViewFragment
import tv.teads.teadssdkdemo.format.infeed.InFeedScrollViewFragment
import tv.teads.teadssdkdemo.format.inread.InReadRecyclerViewFragment
import tv.teads.teadssdkdemo.format.inread.InReadWebViewFragment
import tv.teads.teadssdkdemo.format.inread.MultipleSlotsInReadScrollViewFragment
import tv.teads.teadssdkdemo.format.inread.identifier.DirectIdentifier
import tv.teads.teadssdkdemo.format.mediation.admob.*
import tv.teads.teadssdkdemo.format.mediation.applovin.*
import tv.teads.teadssdkdemo.format.mediation.smart.*
import tv.teads.teadssdkdemo.utils.BaseFragment
import tv.teads.teadssdkdemo.utils.toDefaultPid

class MainFragment : BaseFragment(), RadioGroup.OnCheckedChangeListener {

    private lateinit var binding: FragmentMainBinding

    private lateinit var customPid: Button
    private lateinit var containerCreativeSizes: View
    private lateinit var radioGroupCreativeSizes: RadioGroup
    private lateinit var radioGroupProvider: RadioGroup
    private lateinit var integrationsRecyclerView: RecyclerView

    private val inReadIntegrationList = listOf(
        IntegrationType("Multiple ad slots test", R.drawable.scrollview),
//        IntegrationType("ScrollView", R.drawable.scrollview),
//        IntegrationType("RecyclerView", R.drawable.tableview),
//        IntegrationType("WebView", R.drawable.webview),
    )

    private val nativeIntegrationList = listOf(
        IntegrationType("ScrollView", R.drawable.scrollview),
        IntegrationType("RecyclerView", R.drawable.tableview),
        IntegrationType("RecyclerView Grid", R.drawable.collectionview),
    )

    private val prebidInReadIntegrationList = listOf(
        IntegrationType("Standalone Integration", R.drawable.scrollview),
        IntegrationType("Plugin Renderer", R.drawable.scrollview)
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        customPid = binding.customButton
        customPid.setOnClickListener { changePidDialog() }
        val containerFormat: RadioGroup = binding.containerFormat
        radioGroupProvider = binding.containerProvider
        radioGroupCreativeSizes = binding.radiogroupCreativeSize
        containerCreativeSizes = binding.containerCreativeSize
        integrationsRecyclerView = binding.integrationsRecyclerView

        setIntegrationList()
        setCreativeSizeChecked()
        setProviderSelected()
        showCurrentPid()
        setMediationIntegrationConstraints()

        containerFormat.setOnCheckedChangeListener(this@MainFragment)
        radioGroupProvider.setOnCheckedChangeListener(this@MainFragment)
        radioGroupCreativeSizes.setOnCheckedChangeListener(this@MainFragment)
    }

    private fun getFragmentInReadDirect(position: Int): BaseFragment {
        return when (position) {
//            0 -> InReadScrollViewFragment()
            0 -> MultipleSlotsInReadScrollViewFragment()
            1 -> InReadRecyclerViewFragment()
            2 -> InReadWebViewFragment()
            3 -> StandaloneIntegrationScrollViewFragment()
            4 -> PluginRendererScrollViewFragment()
//            else -> InReadScrollViewFragment()
            else -> MultipleSlotsInReadScrollViewFragment()
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
            else -> throw IllegalStateException()
        }
    }

    private fun getFragmentInReadSmart(position: Int): BaseFragment {
        return when (position) {
            0 -> SmartScrollViewFragment()
            1 -> SmartRecyclerViewFragment()
            2 -> SmartWebViewFragment()
            else -> throw IllegalStateException()
        }
    }

    private fun getFragmentInReadPrebid(position: Int): BaseFragment {
        return when (position) {
            0 -> StandaloneIntegrationScrollViewFragment()
            1 -> PluginRendererScrollViewFragment()
            else -> throw IllegalStateException()
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

    private fun setIntegrationItems(integratiosList: List<IntegrationType>) {
        integrationsRecyclerView.adapter = IntegrationItemAdapter(integratiosList) { position ->
            onIntegrationClicked(position)
        }
    }

    private fun setIntegrationList() {
        val format = SessionDataSource.selectedFormat
        val provider = SessionDataSource.selectedProvider

        val integrationTypeList = when {
            format == FormatType.INREAD && provider == ProviderType.PREBID -> prebidInReadIntegrationList
            format == FormatType.INFEED && provider == ProviderType.PREBID -> emptyList()
            format == FormatType.INREAD -> inReadIntegrationList
            format == FormatType.INFEED -> nativeIntegrationList
            else -> emptyList()
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
            ProviderType.PREBID -> {
                (activity as MainActivity).changeFragment(getFragmentInReadPrebid(position))
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
            else -> {}
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
        setIntegrationList()
    }


    private fun setProviderSelected() {
        when (SessionDataSource.selectedProvider) {
            ProviderType.DIRECT -> radioGroupProvider.check(R.id.directButton)
            ProviderType.ADMOB -> radioGroupProvider.check(R.id.admobButton)
            ProviderType.SMART -> radioGroupProvider.check(R.id.smartButton)
            ProviderType.APPLOVIN -> radioGroupProvider.check(R.id.applovinButton)
            ProviderType.PREBID -> radioGroupProvider.check(R.id.prebidButton)
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
        val limitedProviders = listOf(ProviderType.SMART, ProviderType.PREBID)
        if (limitedProviders.contains(SessionDataSource.selectedProvider)) {
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
                    R.id.prebidButton -> ProviderType.PREBID
                    else -> throw IllegalStateException()
                }

                setDirectIntegrationConstraints()
                setMediationIntegrationConstraints()
                setIntegrationList()
            }
        }
    }
}
