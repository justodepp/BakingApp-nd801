package com.deeper.bakingapp;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.deeper.bakingapp.ui.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void mainRecipeTest() {
        // Check that the recyclerview containing the recipes is displayed
        onView(withId(R.id.rv_main)).check(matches(isDisplayed()));
        // Click on the first recipe in the recyclerview
        onView(withId(R.id.rv_main)).perform(actionOnItemAtPosition(0, click()));

        // Check that the recyclerview containing the steps is displayed
        onView(withId(R.id.rv_main)).check(matches(isDisplayed()));
        // Check that the favourite fab is displayed
        onView(withId(R.id.favorite_fab)).check(matches(isDisplayed()));
        // Click on the first recipe in the recyclerview
        onView(withId(R.id.rv_main)).perform(actionOnItemAtPosition(0, click()));
    }
}
