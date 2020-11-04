package com.eng.safeapp;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;

public class FacialExpressionGameOverUnitTest {
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
}
