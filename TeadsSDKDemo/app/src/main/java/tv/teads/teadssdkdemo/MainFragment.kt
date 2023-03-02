package tv.teads.teadssdkdemo

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import tv.teads.teadssdkdemo.adapter.IntegrationItemAdapter
import tv.teads.teadssdkdemo.data.FormatType
import tv.teads.teadssdkdemo.data.IntegrationType
import tv.teads.teadssdkdemo.data.PidStore
import tv.teads.teadssdkdemo.data.ProviderType
import tv.teads.teadssdkdemo.format.inread.InReadGridRecyclerViewFragment
import tv.teads.teadssdkdemo.format.inread.InReadRecyclerViewFragment
import tv.teads.teadssdkdemo.format.inread.InReadScrollViewFragment
import tv.teads.teadssdkdemo.format.inread.InReadWebViewFragment
import tv.teads.teadssdkdemo.format.inread.identifier.DirectIdentifier
import tv.teads.teadssdkdemo.format.mediation.admob.*
import tv.teads.teadssdkdemo.format.mediation.applovin.*
import tv.teads.teadssdkdemo.format.infeed.InFeedGridRecyclerViewFragment
import tv.teads.teadssdkdemo.format.infeed.InFeedRecyclerViewFragment
import tv.teads.teadssdkdemo.format.mediation.smart.SmartNativeRecyclerViewFragment
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
            0 -> InFeedRecyclerViewFragment()
            1 -> InFeedGridRecyclerViewFragment()
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
            1 -> AppLovinNativeGridRecyclerViewFragment()
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

            setIntegrationItems(inReadIntegrationList)
            setCreativeSizeChecked(FormatType.INREAD)
            setProviderSelected()
            showCurrentPid()

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

    private fun onIntegrationClicked(position: Int) {
        when (PidStore.selectedFormat) {
            FormatType.INREAD -> changeFragmentForInRead(position)
            FormatType.INFEED -> changeFragmentForNative(position)
        }
    }

    private fun changeFragmentForInRead(position: Int) {
        when (PidStore.selectedProvider) {
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
        when (PidStore.selectedProvider) {
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
        PidStore.selectedFormat = availableFormatsMap[id] ?: return

        when (PidStore.selectedFormat) {
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
    }


    private fun setProviderSelected() {
        when (PidStore.selectedProvider) {
            ProviderType.DIRECT -> radioGroupProvider.check(R.id.directButton)
            ProviderType.ADMOB -> radioGroupProvider.check(R.id.admobButton)
            ProviderType.SMART -> radioGroupProvider.check(R.id.smartButton)
            ProviderType.APPLOVIN -> radioGroupProvider.check(R.id.applovinButton)
        }

        setPidButtonConstraints()
    }

    private fun setCreativeSizePid(group: RadioGroup, id: Int) {
        when (PidStore.selectedFormat) {
            FormatType.INREAD -> {
                val radioPid = group.findViewById<View>(id) as? RadioButton

                if (radioPid != null && radioPid.isChecked) {
                    val pid: Int = radioPid.tag.toString().toInt()
                    PidStore.setPid(requireContext(), pid, PidStore.selectedFormat)
                    showCurrentPid()
                }
            }
            else -> {
            }
        }
    }

    private fun setCreativeSizeChecked(formatType: FormatType) {
        val pid = PidStore.getPid(requireContext(), formatType)

        val child =
            radioGroupCreativeSizes.getChildAt(DirectIdentifier.getPositionByPid(pid)) as? RadioButton

        if (child == null) {
            radioGroupCreativeSizes.clearCheck()
        } else {
            child.isChecked = child.tag.toString().toInt() == pid
        }
    }

    private fun changePidDialog() {
        val directFormats = arrayOf(PidStore.selectedFormat)
        val stringfiedDirectFormats = directFormats.map { it.value }.toTypedArray()
        val checkedItem = directFormats.indexOf(PidStore.selectedFormat)
        var nextFormat = PidStore.selectedFormat // init with current

        @SuppressLint("InflateParams") val view = layoutInflater.inflate(R.layout.dialog_pid_content, null)
        val input = view.findViewById<EditText>(R.id.pidEditText)
        input.setLines(1)
        input.setSingleLine(true)
        input.hint = "type your custom pid"
        input.setText(PidStore.getPid(requireContext(), PidStore.selectedFormat).toString())

        AlertDialog.Builder(requireActivity())
            .setTitle("Set custom PID")
            .setView(view)
            .setSingleChoiceItems(stringfiedDirectFormats, checkedItem) { _, selected ->
                nextFormat = directFormats[selected]
                input.setText(PidStore.getPid(requireContext(), nextFormat).toString())
            }
            .setPositiveButton("Save") { _, _ ->
                val pidString = input.text.toString().takeIf { it.isNotBlank() }
                val pid = pidString?.let { Integer.parseInt(it) } ?: nextFormat.toDefaultPid()

                PidStore.setPid(requireContext(), pid, nextFormat)
                showCurrentPid()
                setCreativeSizeChecked(nextFormat)
            }
            .setNegativeButton("Cancel") { _, _ -> }
            .setNeutralButton("Set Default") { _, _ ->
                PidStore.setPid(requireContext(), nextFormat.toDefaultPid(), nextFormat)
                showCurrentPid()
                setCreativeSizeChecked(nextFormat)
            }
            .show()
    }

    private fun setPidButtonConstraints() {
        val status = if (PidStore.selectedProvider == ProviderType.DIRECT) {
            View.VISIBLE
        } else {
            View.INVISIBLE
        }

        customPid.visibility = status
    }

    private fun showCurrentPid() {
        customPid.text = "Change PID: ${PidStore.getPid(requireContext(), PidStore.selectedFormat)}"
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
                PidStore.selectedProvider = when (id) {
                    R.id.directButton -> ProviderType.DIRECT
                    R.id.smartButton -> ProviderType.SMART
                    R.id.applovinButton -> ProviderType.APPLOVIN
                    else -> ProviderType.ADMOB
                }

                setPidButtonConstraints()
            }
        }
    }
}
