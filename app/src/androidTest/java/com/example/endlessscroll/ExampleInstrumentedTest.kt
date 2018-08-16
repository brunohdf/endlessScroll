package com.example.endlessscroll

import android.support.test.espresso.*
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.support.v7.widget.RecyclerView

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Rule
import android.support.test.espresso.IdlingResource.ResourceCallback
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.view.View
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf

@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    @get:Rule
    var mActivityRule: ActivityTestRule<MainActivity> = ActivityTestRule(MainActivity::class.java)

    @Test
    fun testPagination() {
        val ir = RecyclerViewScrollingIdlingResource(mActivityRule.activity.findViewById(R.id.recyclerView) as RecyclerView)
        val idlingRegistry = IdlingRegistry.getInstance()
        idlingRegistry.register(ir);
        onView(withId(R.id.recyclerView)).perform(smoothScrollTo(10))
        onView(withText("1 -> 10")).check(matches(isDisplayed()))
        onView(withId(R.id.recyclerView)).perform(smoothScrollTo(20))
        onView(withText("2 -> 19")).check(matches(isDisplayed()))
        IdlingRegistry.getInstance().register(ir);
        idlingRegistry.unregister(ir)
    }
}

fun smoothScrollTo(position: Int) = ScrollToPositionViewAction(position)

class ScrollToPositionViewAction constructor(private val position: Int) : ViewAction {
    override fun getConstraints(): Matcher<View> {
        return allOf(isAssignableFrom(RecyclerView::class.java), isDisplayed())
    }

    override fun getDescription(): String {
        return "smooth scroll RecyclerView to position: $position"
    }

    override fun perform(uiController: UiController, view: View) {
        val recyclerView = view as RecyclerView
        recyclerView.smoothScrollToPosition(position)
    }
}

class RecyclerViewScrollingIdlingResource(private val recyclerView: RecyclerView) : IdlingResource {

    private var resourceCallback: ResourceCallback? = null

    override fun getName() = RecyclerViewScrollingIdlingResource::class.java.name

    override fun isIdleNow(): Boolean {
        val isIdle = !recyclerView.layoutManager.isSmoothScrolling
        if (isIdle) {
            resourceCallback!!.onTransitionToIdle()
        }
        return isIdle
    }

    override fun registerIdleTransitionCallback(resourceCallback: ResourceCallback) {
        this.resourceCallback = resourceCallback
    }
}
