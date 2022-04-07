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
import tv.teads.teadssdkdemo.format.mediation.admob.*
import tv.teads.teadssdkdemo.format.mediation.applovin.AppLovinGridRecyclerViewFragment
import tv.teads.teadssdkdemo.format.mediation.applovin.AppLovinRecyclerViewFragment
import tv.teads.teadssdkdemo.format.mediation.applovin.AppLovinScrollViewFragment
import tv.teads.teadssdkdemo.format.mediation.applovin.AppLovinWebViewFragment
import tv.teads.teadssdkdemo.utils.BaseFragment


/**
 * Empty fragment helping opening the navigation drawer
 */
class MainFragment : Fragment(), RadioGroup.OnCheckedChangeListener {

    private lateinit var mainView: View
    private lateinit var customPid: RadioButton
    private lateinit var containerCreativeSizes: RadioGroup
    private lateinit var containerProvider: RadioGroup

    private val integrationList = listOf(
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
            containerProvider = this.findViewById(R.id.container_provider)
            val containerIntegration: ConstraintLayout = this.findViewById(R.id.integration_container)
            containerCreativeSizes = this.findViewById(R.id.radiogroup_creative_size)
            customPid = this.findViewById(R.id.customButton)

            setIntegrationItems(containerIntegration)
            setCreativeSizeChecked()
            setProviderSelected()

            containerFormat.setOnCheckedChangeListener(this@MainFragment)
            containerProvider.setOnCheckedChangeListener(this@MainFragment)
            containerCreativeSizes.setOnCheckedChangeListener(this@MainFragment)
            customPid.setOnClickListener { changePidDialog() }
        }

        return mainView
    }

    private fun setIntegrationItems(container: ConstraintLayout) {
        val inflater = LayoutInflater.from(activity)
        val flow = container.getChildAt(0) as Flow
        val ids = IntArray(integrationList.size)

        integrationList.forEachIndexed { index, it ->
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
            ProviderType.APPLOVIN -> {
                (activity as MainActivity).changeFragment(getFragmentInReadAppLovin(position))
            }
        }
    }

    private fun setFormatSelected(id: Int) {
        mFormatSelected = when (id) {
            R.id.inreadButton -> FormatType.INREAD
            else -> FormatType.INREAD
        }
    }


    private fun setProviderSelected() {
        when (mProviderSelected) {
            ProviderType.DIRECT -> containerProvider.check(R.id.directButton)
            ProviderType.ADMOB -> containerProvider.check(R.id.admobButton)
            ProviderType.APPLOVIN -> containerProvider.check(R.id.applovinButton)
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

        val child = containerCreativeSizes.getChildAt(DirectIdentifier.getPositionByPid(pid)) as? RadioButton

        if (child == null) {
            containerCreativeSizes.clearCheck()
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
