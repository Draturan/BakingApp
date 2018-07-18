package com.example.simone.bakingapp;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by Simone on 18/07/2018 for BakingApp project
 */
@RunWith(AndroidJUnit4.class)
public class DescriptionActivityTest {

    private static final String THIRD_SWEET_NAME = "Yellow Cake";
    private static final String SECOND_SWEET_STEP = "2. Combine the cake flour, 400 grams " +
            "(2 cups) of sugar, baking powder, and 1 teaspoon of salt in the bowl of a stand " +
            "mixer. Using the paddle attachment, beat at low speed until the dry ingredients " +
            "are mixed together, about one minute";

    @Rule
    public ActivityTestRule<MainActivity> mainActivityActivityTestRule =
            new ActivityTestRule<>(MainActivity.class);

    @Test
    public void stepDataTest(){
        //Find the RecyclerView and scroll the second item
        onView(ViewMatchers.withId(R.id.rv_sweets_list)).perform(RecyclerViewActions.actionOnItemAtPosition(2,scrollTo()));
        //check the name of the sweet
        onView(withText(THIRD_SWEET_NAME)).check(matches(isDisplayed()));

        //Find and click on the third item
        onView(ViewMatchers.withId(R.id.rv_sweets_list)).perform(RecyclerViewActions.actionOnItemAtPosition(2,click()));
        //Scroll to the third ingredient
        onView(ViewMatchers.withId(R.id.rv_steps_list)).perform(RecyclerViewActions.actionOnItemAtPosition(2,scrollTo()));
        //find the ingredient inside ingredients recyclerView
        onView(withText(SECOND_SWEET_STEP)).check(matches(isDisplayed()));
    }

}
