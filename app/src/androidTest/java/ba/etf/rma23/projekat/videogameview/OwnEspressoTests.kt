package com.example.videogameview

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.view.View
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.PositionAssertions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import ba.etf.rma23.projekat.GameData
import ba.etf.rma23.projekat.HomeActivity
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TestLayout {
    private lateinit var navController: NavController

    @get:Rule
    var activityRule: ActivityTestRule<HomeActivity> = ActivityTestRule(HomeActivity::class.java)

    fun withIndex(matcher: Matcher<View?>, index: Int): Any {
        return object : TypeSafeMatcher<View>() {
            var currentIndex = 0
            var viewObjHash = 0

            @SuppressLint("DefaultLocale")
            override fun describeTo(description: Description) {
                description.appendText(String.format("with index: %d ", index))
                matcher.describeTo(description)
            }

            override fun matchesSafely(view: View): Boolean {
                if (matcher.matches(view) && currentIndex++ == index) {
                    viewObjHash = view.hashCode()
                }
                return view.hashCode() == viewObjHash
            }
        }
    }

    /*
    Testiranje osnovnih funkcionalnosti aplikacije u portrait orijentaciji

    Prilikom pokretanja aplikacije, details dugme u navigation komponenti treba biti onemoguceno.
    Klik na home dugme ne bi trebao nista promijeniti. Nakon klika na igricu, detalji o istoj trebaju biti prikazani.
    Klik na home dugme bi nas trebao vratiti na home fragment.
    Sada details dugme u navigation komponenti treba biti omoguceno, te prilikom klika treba prikazati detalje
    o zadnje prikazanoj igrici.

    U ovom testu testiramo da li su dugmadi u navigation komponenti omoguceni odnosno onemoguceni u ispravnim trenucima.
    Takodjer testiramo da li klik na igricu, povratak na home, pa klik na details dugme prikazuje istu igricu.
    Ovu proceduru ponavljamo za prve tri igrice.
     */
    @Test
    fun testNavigateToDetailsFragment() {
        val scenario = ActivityScenario.launch(HomeActivity::class.java)
        scenario.onActivity { activity ->
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
        onView(withId(R.id.gameDetailsItem)).check(matches(CoreMatchers.not(isEnabled())))
        onView(withId(R.id.homeItem)).perform(click())
        for (i in 0..2) {
            var igra = GameData.getAll().get(i)
            onView(withId(R.id.game_list)).perform(
                RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(
                    allOf(
                        hasDescendant(withText(igra.title)),
                        hasDescendant(withText(igra.releaseDate)),
                        hasDescendant(withText(igra.rating.toString()))
                    ), click()
                )
            )
            onView(withText(igra.title)).check(matches(isCompletelyDisplayed()))
            onView(withId(R.id.homeItem)).perform(click())
            onView(withId(R.id.gameDetailsItem)).check(matches(isEnabled()))
            onView(withId(R.id.gameDetailsItem)).perform(click())
            onView(withText(igra.title)).check(matches(isCompletelyDisplayed()))
            onView(withId(R.id.homeItem)).perform(click())
        }
        scenario.close()
    }

    /*
    Testiranje osnovnih funkcionalnosti aplikacije u landscape orijentaciji

    Aplikacija se pokrece u portrait orijentaciji pa se okrece u landscape.
    U landscape orijentaciji detalji prve igrice trebaju automatski biti prikazani u details fragmentu.
    Nakon klika na drugu igricu, detalji o istoj trebaju biti prikazani.

    U ovom testu testiramo da li su detalji prve igrice prikazani prilikom okretanja u landscape mode,
    te da li se detalji prikazu prilikom klika na druge igrice. Testiran je klik na igrice sa indeksom od 1 do 5,
    kao i klik na istu igricu dva puta.
     */
    @Test
    fun landscapeTest() {
        val scenario = ActivityScenario.launch(HomeActivity::class.java)
        scenario.onActivity { activity ->
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        }
        var igra = GameData.getAll().get(0)
        onView(withId(R.id.details_fragment_container)).check(matches(isDisplayed()))
        onView(withIndex(withText(igra.title), 1) as Matcher<View>?).check(
            matches(
                isCompletelyDisplayed()
            )
        )
        for (i in 0..5) {
            igra = GameData.getAll().get(i)
            onView(withId(R.id.game_list)).perform(
                RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(
                    allOf(
                        hasDescendant(withText(igra.title)),
                        hasDescendant(withText(igra.releaseDate)),
                        hasDescendant(withText(igra.rating.toString()))
                    ), click()
                )
            )
            onView(withIndex(withText(igra.title), 1) as Matcher<View>?).check(
                matches(
                    isCompletelyDisplayed()
                )
            )
        }
        scenario.close()
    }

    /*
    Testiranje promjene orijentacije tokom koristenja aplikacije

    Aplikacija se pokrece u portrait orijentaciji. Nakon promjene orijentacije u landscape, prva igrica treba
    biti prikazana u details fragmentu neovisno o igri u portrait orijentaciji. Nakon povratka u portrait
    orijentaciju, igra koja je bila prikazana prije promjene u landscape treba ponovo biti prikizana.
     */
    @Test
    fun orientationChangeTest() {
        val scenario = ActivityScenario.launch(HomeActivity::class.java)
        scenario.onActivity { activity ->
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
        var prvaIgra = GameData.getAll().get(5)
        onView(withId(R.id.game_list)).perform(
            RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(
                allOf(
                    hasDescendant(withText(prvaIgra.title)),
                    hasDescendant(withText(prvaIgra.releaseDate)),
                    hasDescendant(withText(prvaIgra.rating.toString()))
                ), click()
            )
        )
        onView(withText(prvaIgra.title)).check(matches(isCompletelyDisplayed()))
        scenario.onActivity { activity ->
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        }
        var igra = GameData.getAll().get(0)
        onView(withId(R.id.details_fragment_container)).check(matches(isDisplayed()))
        onView(withIndex(withText(igra.title), 1) as Matcher<View>?).check(
            matches(
                isCompletelyDisplayed()
            )
        )
        igra = GameData.getAll().get(6)
        onView(withId(R.id.game_list)).perform(
            RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(
                allOf(
                    hasDescendant(withText(igra.title)),
                    hasDescendant(withText(igra.releaseDate)),
                    hasDescendant(withText(igra.rating.toString()))
                ), click()
            )
        )
        scenario.onActivity { activity ->
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
        onView(withText(prvaIgra.title)).check(matches(isCompletelyDisplayed()))
        scenario.close()
    }
}