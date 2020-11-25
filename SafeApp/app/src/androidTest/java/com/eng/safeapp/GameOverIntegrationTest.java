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

@RunWith(AndroidJUnit4.class)
@LargeTest
public class GameOverIntegrationTest {
    @Rule
    public ActivityTestRule<GameOver> activityRule =
            new ActivityTestRule<>(GameOver.class);


    @Test
    public void gameOverLaunch() {
        // Check that clicking the start game button takes the user to the game screen.
        onView(withId(R.id.gameOverTextView)).check(matches(isDisplayed()));
        onView(withId(R.id.gameOverMainMenuBtn)).check(matches(isDisplayed()));
        onView(withId(R.id.scoreTitleTextView)).check(matches(isDisplayed()));
        onView(withId(R.id.scoreTextView)).check(matches(isDisplayed()));

        onView(withId(R.id.gameOverMainMenuBtn))
                .perform(click());
        onView(withId(R.id.safeAppTextView)).check(matches(isDisplayed()));
    }
}
