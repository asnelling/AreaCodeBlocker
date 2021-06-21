package io.asnell.prefixscreener

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragment
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NewPrefixDialogFragmentTest {
    private lateinit var scenario: FragmentScenario<NewPrefixDialogFragment>
    @Before
    fun setUp() {
        scenario = launchFragment(themeResId = R.style.Theme_PrefixScreener)
    }

    @Test
    fun inputPrefix_clickBlock_dismissesDialog() {
        scenario.onFragment { fragment ->
            Assert.assertNotNull(fragment.dialog)
            Assert.assertTrue(fragment.requireDialog().isShowing)
        }

        onView(withId(R.id.edit_prefix))
            .inRoot(isDialog())
            .check(matches(isDisplayed()))
            .perform(typeText("1222"), closeSoftKeyboard())

        onView(withId(R.id.button_block))
            .inRoot(isDialog())
            .perform(click())

        onView(withId(R.id.new_prefix_dialog))
            .check(doesNotExist())

        scenario.recreate()
    }

    @Test
    fun inputPrefix_clickReject_dismissesDialog() {
        scenario.onFragment { fragment ->
            Assert.assertNotNull(fragment.dialog)
            Assert.assertTrue(fragment.requireDialog().isShowing)
        }

        onView(withId(R.id.edit_prefix))
            .inRoot(isDialog())
            .check(matches(isDisplayed()))
            .perform(typeText("1222"), closeSoftKeyboard())

        onView(withId(R.id.button_reject))
            .inRoot(isDialog())
            .perform(click())

        onView(withId(R.id.new_prefix_dialog))
            .check(doesNotExist())

        scenario.recreate()
    }

    @Test
    fun inputPrefix_clickSilence_dismissesDialog() {
        scenario.onFragment { fragment ->
            Assert.assertNotNull(fragment.dialog)
            Assert.assertTrue(fragment.requireDialog().isShowing)
        }

        onView(withId(R.id.edit_prefix))
            .inRoot(isDialog())
            .check(matches(isDisplayed()))
            .perform(typeText("1222"), closeSoftKeyboard())

        onView(withId(R.id.button_silence))
            .inRoot(isDialog())
            .perform(click())

        onView(withId(R.id.new_prefix_dialog))
            .check(doesNotExist())

        scenario.recreate()
    }

    @Test
    fun inputPrefix_clickBlock_setsResult() {
        var actualResultAction: String? = null
        var actualResultPrefix: String? = null
        scenario.onFragment { fragment ->
            fragment.parentFragmentManager.setFragmentResultListener(NewPrefixDialogFragment.TAG, fragment.viewLifecycleOwner) { requestKey, bundle ->
                actualResultAction = bundle.getString("action")
                actualResultPrefix = bundle.getString("prefix")
            }
        }

        onView(withId(R.id.edit_prefix))
            .inRoot(isDialog())
            .perform(typeText("1222"), closeSoftKeyboard())

        onView(withId(R.id.button_block))
            .inRoot(isDialog())
            .perform(click())

        Assert.assertEquals("DISALLOW", actualResultAction)
        Assert.assertEquals("+1222", actualResultPrefix)

        scenario.recreate()
    }

    @Test
    fun inputPrefix_clickReject_setsResult() {
        var actualResultAction: String? = null
        var actualResultPrefix: String? = null
        scenario.onFragment { fragment ->
            fragment.parentFragmentManager.setFragmentResultListener(NewPrefixDialogFragment.TAG, fragment.viewLifecycleOwner) { requestKey, bundle ->
                actualResultAction = bundle.getString("action")
                actualResultPrefix = bundle.getString("prefix")
            }
        }

        onView(withId(R.id.edit_prefix))
            .inRoot(isDialog())
            .perform(typeText("1222"), closeSoftKeyboard())

        onView(withId(R.id.button_reject))
            .inRoot(isDialog())
            .perform(click())

        Assert.assertEquals("REJECT", actualResultAction)
        Assert.assertEquals("+1222", actualResultPrefix)

        scenario.recreate()
    }

    @Test
    fun inputPrefix_clickSilence_setsResult() {
        var actualResultAction: String? = null
        var actualResultPrefix: String? = null
        scenario.onFragment { fragment ->
            fragment.parentFragmentManager.setFragmentResultListener(NewPrefixDialogFragment.TAG, fragment.viewLifecycleOwner) { requestKey, bundle ->
                actualResultAction = bundle.getString("action")
                actualResultPrefix = bundle.getString("prefix")
            }
        }

        onView(withId(R.id.edit_prefix))
            .inRoot(isDialog())
            .perform(typeText("1222"), closeSoftKeyboard())

        onView(withId(R.id.button_silence))
            .inRoot(isDialog())
            .perform(click())

        Assert.assertEquals("SILENCE", actualResultAction)
        Assert.assertEquals("+1222", actualResultPrefix)

        scenario.recreate()
    }
}