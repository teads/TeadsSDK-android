/*
 * Copyright (c) Teads 2018.
 */

package tv.teads.teadssdkdemo

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.contrib.DrawerActions
import android.support.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.rule.ActivityTestRule
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
