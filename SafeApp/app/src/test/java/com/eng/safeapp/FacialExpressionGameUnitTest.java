package com.eng.safeapp;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class FacialExpressionGameUnitTest {
    private FacialExpressionGame gameScreen;

    @Before //This is executed before the @Test executes
    public void setUp(){
        gameScreen = new FacialExpressionGame();
        gameScreen.setEmotions(new ArrayList<>(Arrays.asList("Happy", "Sad", "Scared", "Worried", "Pride")));
        System.out.println("Ready for testing");
    }

    @After //This is executed after the @Test executes
    public void tearDown(){
        System.out.println("Done with testing");
    }

    @Test
    public void testPickRandomString() {
        ArrayList<String> strings = new ArrayList<>(gameScreen.getEmotions());
        ArrayList<String> randomStrings = new ArrayList<>();
        HashMap<String, Integer> randomStringsFrequencyCount = new HashMap<>();

        int totalIterations = 10000;
        for (int i = 0; i < totalIterations; i++) {
            randomStrings.add(gameScreen.pickRandomString(strings));
        }
        for (int i = 0; i < totalIterations-1; i++) {
            String randomString = randomStrings.get(i);
            if (!randomStringsFrequencyCount.containsKey(randomString)) {
                randomStringsFrequencyCount.put(randomString, 0);
            }
            int frequency = randomStringsFrequencyCount.get(randomString);
            randomStringsFrequencyCount.put(randomString, frequency + 1);
        }
        String assertMessage = "Pick Random String is not picking strings at random";
        assertEquals(assertMessage, randomStringsFrequencyCount.size(), strings.size());
        //The message here is displayed iff the test fails
    }

    @Test
    public void testSelectEmotionsToChooseFrom() {
        ArrayList<String> emotions = gameScreen.getEmotions();
        int numberOfEmotionsToChoose = 3;
        ArrayList<String> selectedEmotions = gameScreen.selectEmotionsToChooseFrom(emotions, numberOfEmotionsToChoose);
        // Check the size of the returned list is correct
        String assertMessage = "Incorrect number of emotions selected";
        assertEquals(assertMessage, selectedEmotions.size(), numberOfEmotionsToChoose);
        // Check that there are no duplicate emotions
        HashMap<String, Integer> emotionFrequency = new HashMap<>();
        for (int i = 0; i < selectedEmotions.size(); i++) {
            String emotion = selectedEmotions.get(i);
            assertFalse(emotionFrequency.containsKey(emotion));
            emotionFrequency.put(emotion, 1);
        }
    }
}
