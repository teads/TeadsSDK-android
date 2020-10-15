package tv.teads.teadssdkdemo.component

import android.content.Context
import android.util.AttributeSet
import android.widget.CompoundButton
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import tv.teads.teadssdkdemo.R

class TeadsRadioButton(private val mContext: Context, attribute: AttributeSet) : androidx.appcompat.widget.AppCompatRadioButton(mContext, attribute),
        CompoundButton.OnCheckedChangeListener {

    init {
        setOnCheckedChangeListener(this)
    }

    override fun onCheckedChanged(button: CompoundButton?, checked: Boolean): Unit {
        if (checked)
            setTextColor(ContextCompat.getColor(mContext, android.R.color.white))
        else
            setTextColor(ContextCompat.getColor(mContext, R.color.primaryDarkDef))
    }
}