package tv.teads.teadssdkdemo.utils.event


import androidx.fragment.app.Fragment

/**
 * Event posted when an inner activity's fragment wan't to change the displayed fragment
 */
class ChangeFragmentEvent(val fragment: Fragment)
