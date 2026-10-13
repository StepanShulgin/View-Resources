package otus.gpb.homework.viewandresources

import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.graphics.Bitmap
import android.os.SystemClock
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.NestedScrollView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.isPlatformPopup
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import java.io.File
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LayoutTest {
    private val instrumentation = InstrumentationRegistry.getInstrumentation()
    private val originalNightMode = AppCompatDelegate.getDefaultNightMode()

    @After
    fun restoreTheme() {
        instrumentation.runOnMainSync {
            AppCompatDelegate.setDefaultNightMode(originalNightMode)
        }
    }

    @Test
    fun contactsBothThemesAndOrientations() {
        forEachConfiguration { name, orientation ->
            ActivityScenario.launch(ContactsActivity::class.java).use { scenario ->
                orient(scenario, orientation)
                onView(withId(R.id.name_input)).check(matches(withText("Ivan Petrov")))
                onView(withId(R.id.phone_type)).check(matches(withText("Mobile")))
                onView(withId(R.id.notes_input)).perform(scrollTo()).check(matches(isDisplayed()))
                onView(withId(R.id.save_button)).check(matches(isDisplayed()))
                onView(withId(R.id.name_input)).perform(scrollTo())
                screenshot("contacts_$name")
            }
        }
    }

    @Test
    fun cartBothThemesAndOrientations() {
        forEachConfiguration { name, orientation ->
            ActivityScenario.launch(CartActivity::class.java).use { scenario ->
                orient(scenario, orientation)
                onView(withText("List item 4")).perform(scrollTo()).check(matches(isDisplayed()))
                onView(withText("$175.50")).check(matches(isDisplayed()))
                onView(withId(R.id.place_order_button)).check(matches(isDisplayed()))
                scenario.onActivity { activity ->
                    val item = activity.findViewById<View>(R.id.cart_item_1)
                    val title = item.findViewById<TextView>(R.id.item_title)
                    val price = item.findViewById<TextView>(R.id.item_price)
                    assertEquals(title.top + title.baseline, price.top + price.baseline)
                    assertEquals(activity.getColor(R.color.price), price.currentTextColor)
                    activity.findViewById<NestedScrollView>(R.id.items_scroll)
                        .smoothScrollTo(0, 0)
                }
                screenshot("cart_$name")
            }
        }
    }

    @Test
    fun loginBothThemesAndOrientations() {
        forEachConfiguration { name, orientation ->
            ActivityScenario.launch(MainActivity::class.java).use { scenario ->
                orient(scenario, orientation)
                onView(withId(R.id.signin_button)).perform(click())
                onView(withId(R.id.login_input)).check(matches(withText("ivanpetrov")))
                onView(withId(R.id.cancel_button)).perform(scrollTo()).check(matches(isDisplayed()))
                screenshot("login_$name")
                onView(withId(R.id.cancel_button)).perform(click())
                onView(withId(R.id.signin_button)).check(matches(isDisplayed()))
            }
        }
    }

    @Test
    fun editedLoginSurvivesRecreation() {
        ActivityScenario.launch(MainActivity::class.java).use { scenario ->
            orient(scenario, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
            onView(withId(R.id.signin_button)).perform(click())
            onView(withId(R.id.login_input)).perform(replaceText("updated_login"), closeSoftKeyboard())
            scenario.recreate()
            onView(withId(R.id.login_input)).check(matches(withText("updated_login")))
            onView(withId(R.id.cancel_button)).perform(scrollTo(), click())
            screenshot("login_after_recreation_dismissed")
            onView(withId(R.id.signin_button)).check(matches(isDisplayed()))
        }
    }

    @Test
    fun phoneTypeSelectionSurvivesRecreation() {
        ActivityScenario.launch(ContactsActivity::class.java).use { scenario ->
            orient(scenario, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
            onView(withId(R.id.phone_type)).perform(click())
            screenshot("contacts_phone_type_menu")
            onView(withText("Home")).inRoot(isPlatformPopup()).perform(click())
            scenario.recreate()
            onView(withId(R.id.phone_type)).check(matches(withText("Home")))
        }
    }

    @Test
    fun loginRemainsUsableWithLandscapeKeyboard() {
        ActivityScenario.launch(MainActivity::class.java).use { scenario ->
            orient(scenario, ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)
            onView(withId(R.id.signin_button)).perform(click())
            onView(withId(R.id.login_input)).perform(click(), typeText("x"))
            awaitKeyboardVisible(scenario)
            onView(withId(R.id.cancel_button)).perform(scrollTo()).check(matches(isDisplayed()))
            screenshot("login_landscape_keyboard")
            onView(withId(R.id.login_input)).perform(closeSoftKeyboard())
            onView(withId(R.id.cancel_button)).perform(scrollTo(), click())
        }
    }

    @Test
    fun repeatedLoginRequestDoesNotCreateDuplicateDialogs() {
        ActivityScenario.launch(MainActivity::class.java).use { scenario ->
            scenario.onActivity { activity ->
                val button = activity.findViewById<View>(R.id.signin_button)
                button.performClick()
                button.performClick()
                assertEquals(
                    1,
                    activity.supportFragmentManager.fragments.filterIsInstance<SignInDialog>().size,
                )
            }
            onView(withId(R.id.cancel_button)).perform(scrollTo(), click())
            onView(withId(R.id.signin_button)).check(matches(isDisplayed()))
        }
    }

    private fun awaitKeyboardVisible(scenario: ActivityScenario<MainActivity>) {
        val deadline = SystemClock.uptimeMillis() + 5_000
        var visible = false
        do {
            instrumentation.waitForIdleSync()
            scenario.onActivity { activity ->
                val fragment = activity.supportFragmentManager
                    .findFragmentByTag(SignInDialog.TAG) as SignInDialog
                val decor = requireNotNull(fragment.requireDialog().window).decorView
                visible = ViewCompat.getRootWindowInsets(decor)
                    ?.isVisible(WindowInsetsCompat.Type.ime()) == true
            }
            if (visible) return
            Thread.sleep(100)
        } while (SystemClock.uptimeMillis() < deadline)
        assertTrue("The IME must actually be visible during the keyboard test", visible)
    }

    private fun forEachConfiguration(block: (String, Int) -> Unit) {
        for ((theme, mode) in listOf(
            "day" to AppCompatDelegate.MODE_NIGHT_NO,
            "night" to AppCompatDelegate.MODE_NIGHT_YES,
        )) {
            instrumentation.runOnMainSync { AppCompatDelegate.setDefaultNightMode(mode) }
            for ((rotation, orientation) in listOf(
                "portrait" to ActivityInfo.SCREEN_ORIENTATION_PORTRAIT,
                "landscape" to ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE,
            )) {
                block("${theme}_$rotation", orientation)
            }
        }
    }

    private fun <T : AppCompatActivity> orient(
        scenario: ActivityScenario<T>,
        requested: Int,
    ) {
        scenario.onActivity { it.requestedOrientation = requested }
        val expected = if (requested == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            Configuration.ORIENTATION_PORTRAIT
        } else {
            Configuration.ORIENTATION_LANDSCAPE
        }
        val deadline = SystemClock.uptimeMillis() + 5_000
        var current = 0
        do {
            instrumentation.waitForIdleSync()
            scenario.onActivity { current = it.resources.configuration.orientation }
            if (current == expected) return
            Thread.sleep(100)
        } while (SystemClock.uptimeMillis() < deadline)
        assertEquals(expected, current)
    }

    private fun screenshot(name: String) {
        instrumentation.waitForIdleSync()
        instrumentation.uiAutomation.waitForIdle(250, 3_000)
        val bitmap = requireNotNull(instrumentation.uiAutomation.takeScreenshot())
        val directory = File(instrumentation.targetContext.getExternalFilesDir(null), "screenshots")
        assertTrue(directory.isDirectory || directory.mkdirs())
        File(directory, "$name.png").outputStream().use {
            assertTrue(bitmap.compress(Bitmap.CompressFormat.PNG, 100, it))
        }
        bitmap.recycle()
    }
}
