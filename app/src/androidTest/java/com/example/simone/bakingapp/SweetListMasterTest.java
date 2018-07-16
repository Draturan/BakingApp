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
 * Created by Simone on 28/06/2018 for BakingApp project
 */
@RunWith(AndroidJUnit4.class)
public class SweetListMasterTest {

    private static final String FIRST_SWEET = "Nutella Pie";
    private static final String THIRD_SWEET_INGEDIENT = "baking powder";

    @Rule
    public ActivityTestRule<MainActivity> mainActivityActivityTestRule = new ActivityTestRule<>(MainActivity.class);


    @Test
    public void mainSweetListTest(){
//        onView(withRecyclerView(R.id.rv_sweets_list)
//            .atPositionOnView(0, R.id.tv_sweet_name)
//            .check(matches(withText(FIRST_SWEET))));

        //Find the RecyclerView and scroll the second item
        onView(ViewMatchers.withId(R.id.rv_sweets_list)).perform(RecyclerViewActions.actionOnItemAtPosition(1,scrollTo()));
        //check the name of the sweet
        onView(withText(FIRST_SWEET)).check(matches(isDisplayed()));

        //Find and click on the third item
        onView(ViewMatchers.withId(R.id.rv_sweets_list)).perform(RecyclerViewActions.actionOnItemAtPosition(2,click()));
        //Scroll to the third ingredient
        onView(ViewMatchers.withId(R.id.rv_ingredients_list)).perform(RecyclerViewActions.actionOnItemAtPosition(2,scrollTo()));
        //find the ingredient inside ingredients recyclerView
        onView(withText(THIRD_SWEET_INGEDIENT)).check(matches(isDisplayed()));
    }


}
