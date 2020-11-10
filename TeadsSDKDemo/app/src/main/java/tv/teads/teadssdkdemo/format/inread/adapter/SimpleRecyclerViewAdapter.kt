package tv.teads.teadssdkdemo.format.inread.adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import tv.teads.sdk.android.AdSettings
import tv.teads.sdk.android.InReadAdView
import tv.teads.teadssdkdemo.component.GenericRecyclerViewAdapter
import tv.teads.teadssdkdemo.data.RecyclerItemType

/**
 * Simple RecyclerView adapter
 */
class SimpleRecyclerViewAdapter(context: Context?, pid: Int, title: String)
    : GenericRecyclerViewAdapter(title) {

    // 1. Create the InReadAdView
    private val adView: InReadAdView = InReadAdView(context)

    init {
        // 2. Setup the AdView
        adView.setPid(pid)

        // 3. Create an AdSettings and setup your AdView if needed
        val settings = AdSettings.Builder()
                .enableDebug()
                .build()

        // 4. Load the ad with the created settings
        //    You can still load without settings.
        adView.load(settings)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            RecyclerItemType.TYPE_TEADS.value -> ViewHolderDemo(adView)
            else -> super.onCreateViewHolder(parent, viewType)
        }
    }
}
