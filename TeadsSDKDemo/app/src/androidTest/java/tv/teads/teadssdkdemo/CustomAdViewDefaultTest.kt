/*
 * Copyright (c) Teads 2019.
 */

package tv.teads.teadssdkdemo

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.DrawerActions
import androidx.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.rule.ActivityTestRule
import android.view.Gravity
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import tv.teads.sdk.android.CustomAdView
import tv.teads.sdk.android.PublicInterface


/**
 * Test that ad at least are displayed and did played entirely
 */
class CustomAdViewDefaultTest {

    @get:Rule
    val activityTestRule = ActivityTestRule(MainActivity::class.java, true, true)

    @Test
    fun customAdViewDidOpenAndReachEndscreen() {
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open(Gravity.START))
        onView(withId(R.id.custom_scrollview)).perform(click())
        Thread.sleep(6000)
        onView(withId(R.id.teadsAdView)).check(matches(isCompletelyDisplayed()))

        val customAdView: CustomAdView = activityTestRule.activity.findViewById(R.id.teadsAdView)

        assertEquals("Ad is loaded", PublicInterface.LOADED, customAdView.state)
        assertTrue("Ad is playing", customAdView.isPlaying)
        Thread.sleep(35000)
        assertEquals("Ad is loaded", PublicInterface.LOADED, customAdView.state)
        onView(withId(R.id.teads_center_content)).check(matches(isCompletelyDisplayed()))
    }
}
