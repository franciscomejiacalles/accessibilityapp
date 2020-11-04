package com.eng.safeapp;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class FacialExpressionGameIntegrationTest {
    @Rule
    public ActivityTestRule<FacialExpressionGame> activityRule =
            new ActivityTestRule<>(FacialExpressionGame.class);


    @Test
    public void facialExpressionGameLaunch() throws InterruptedException {
        // Check that clicking the start game button takes the user to the game screen.

        FacialExpressionGame game = activityRule.getActivity();

        // Test that there are six buttons on the screen
        assertEquals("There should be 6 buttons on the screen", 6, game.buttons.size());
        onView(withId(R.id.button0)).check(matches(isDisplayed()));
        onView(withId(R.id.button1)).check(matches(isDisplayed()));
        onView(withId(R.id.button2)).check(matches(isDisplayed()));
        onView(withId(R.id.button3)).check(matches(isDisplayed()));
        onView(withId(R.id.button4)).check(matches(isDisplayed()));
        onView(withId(R.id.button5)).check(matches(isDisplayed()));

        // Test that current emotion changes after clicking a button
        String currentEmotion = game.getCurrentEmotion();
        onView(withId(R.id.button0)).perform((click()));
        assertNotEquals("Current emotion should change after clicking button", currentEmotion, game.getCurrentEmotion());

        Thread.sleep(65000);
        onView(withId(R.id.gameOverTextView)).check(matches(isDisplayed()));

    }
}
