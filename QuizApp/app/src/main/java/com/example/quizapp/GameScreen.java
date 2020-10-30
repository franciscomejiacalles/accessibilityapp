package com.example.quizapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class GameScreen extends AppCompatActivity {

    Timer timer;
    private String currentEmotion;
    private int correct;
    private int incorrect;
    private ArrayList<String> emotions = new ArrayList<>(Arrays.asList("Happy", "Sad", "Optimistic", "Scared", "Worried", "Indifferent", "Jealousy", "Pride"));
    private HashMap<String, ArrayList<Integer>> emotionToFacialExpression= new HashMap<>();
    private ArrayList<ImageButton> buttons = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_screen);

        initializeButtons();
        initializeFacialExpressionMapping();
        registerOnClickListeners();
        loadNextMatch();
        startTimer();
    }

    private void startTimer() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), GameOver.class);
                intent.putExtra("correct", correct);
                intent.putExtra("incorrect", incorrect);
                startActivity(intent);
                finish();
            }
        }, 20000);
    }

    private void loadNextMatch() {
        // Select facial expressions (1 expression per emotion)
        ArrayList<String> emotionsToChooseFrom = selectEmotionsToChooseFrom();

        currentEmotion = pickRandomString(emotionsToChooseFrom);
        TextView currentEmotionTextView = findViewById(R.id.currentEmotionTextView);
        currentEmotionTextView.setText(currentEmotion);

        // 2. Update buttons with facial expression images
        for (int i = 0; i < buttons.size(); i++) {
            ImageButton button = buttons.get(i);
            String emotion = emotionsToChooseFrom.get(i);
            ArrayList<Integer> facialExpressions = emotionToFacialExpression.get(emotion);

            Random r = new Random();
            int randomIndex = r.nextInt(facialExpressions.size());
            Integer currentExpression = facialExpressions.get(randomIndex);
            button.setImageResource(currentExpression);

            if (emotion == currentEmotion) {
                button.setTag(true);
            } else {
                button.setTag(false);
            }
        }

    }

    private void registerOnClickListeners() {
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isCorrectAnswer = (boolean) v.getTag();
                if (isCorrectAnswer) {
                    System.out.println("Correct Answer");
                    correct += 1;
                    v.setTag(false);
                } else {
                    System.out.println("Incorrect Answer");
                    incorrect += 1;
                }
                System.out.println("Total correct: " + correct);
                System.out.println("Total incorrect: " + incorrect);
                loadNextMatch();
            }
        };

        for (ImageButton button : buttons) {
            button.setOnClickListener(onClickListener);
        }
    }

    private void initializeFacialExpressionMapping() {
        // Initialize emotionToFacialExpression hashMap.
        ArrayList<Integer> happyFacialExpressions = new ArrayList<>();
        happyFacialExpressions.add(R.drawable.happy_001);
        happyFacialExpressions.add(R.drawable.happy_002);
        emotionToFacialExpression.put("Happy", happyFacialExpressions);

        ArrayList<Integer> sadFacialExpressions = new ArrayList<>();
        sadFacialExpressions.add(R.drawable.sad_001);
        sadFacialExpressions.add(R.drawable.sad_002);
        emotionToFacialExpression.put("Sad", sadFacialExpressions);

        ArrayList<Integer> optimisticFacialExpressions = new ArrayList<>();
        optimisticFacialExpressions.add(R.drawable.optimistic_001);
        optimisticFacialExpressions.add(R.drawable.optimistic_002);
        emotionToFacialExpression.put("Optimistic", optimisticFacialExpressions);

        ArrayList<Integer> scaredFacialExpressions = new ArrayList<>();
        scaredFacialExpressions.add(R.drawable.scared_001);
        scaredFacialExpressions.add(R.drawable.scared_002);
        emotionToFacialExpression.put("Scared", scaredFacialExpressions);

        ArrayList<Integer> worriedFacialExpressions = new ArrayList<>();
        worriedFacialExpressions.add(R.drawable.worried_001);
        worriedFacialExpressions.add(R.drawable.worried_002);
        emotionToFacialExpression.put("Worried", worriedFacialExpressions);

        ArrayList<Integer> indifferentFacialExpressions = new ArrayList<>();
        indifferentFacialExpressions.add(R.drawable.indifferent_001);
        indifferentFacialExpressions.add(R.drawable.indifferent_002);
        emotionToFacialExpression.put("Indifferent", indifferentFacialExpressions);

        ArrayList<Integer> jealousyFacialExpressions = new ArrayList<>();
        jealousyFacialExpressions.add(R.drawable.jealousy_001);
        jealousyFacialExpressions.add(R.drawable.jealousy_002);
        emotionToFacialExpression.put("Jealousy", jealousyFacialExpressions);

        ArrayList<Integer> prideFacialExpressions = new ArrayList<>();
        prideFacialExpressions.add(R.drawable.pride_001);
        prideFacialExpressions.add(R.drawable.pride_002);
        emotionToFacialExpression.put("Pride", prideFacialExpressions);
    }

    private void initializeButtons() {
        buttons.add((ImageButton) findViewById(R.id.button0));
        buttons.add((ImageButton) findViewById(R.id.button1));
        buttons.add((ImageButton) findViewById(R.id.button2));
        buttons.add((ImageButton) findViewById(R.id.button3));
        buttons.add((ImageButton) findViewById(R.id.button4));
        buttons.add((ImageButton) findViewById(R.id.button5));
    }

    private String pickRandomString(ArrayList<String> s) {
        Random r = new Random();
        int randomIndex = r.nextInt(s.size());
        return s.get(randomIndex);
    }

    private ArrayList<String> selectEmotionsToChooseFrom() {
        Collections.shuffle(emotions);
        ArrayList<String> emotionsToChooseFrom = new ArrayList<>();
        for (int i = 0; i < buttons.size(); i++) {
            emotionsToChooseFrom.add(emotions.get(i));
        }
        return emotionsToChooseFrom;
    }
}