package com.eng.safeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class FacialExpressionGame extends AppCompatActivity {

    Timer timer;
    private String currentEmotion;
    private int correct;
    private int incorrect;

    private ArrayList<String> emotions = new ArrayList<>(Arrays.asList("Happy", "Sad", "Surprised", "Anger", "Fear", "Disgust", "Confused", "Neutral"));
    private HashMap<String, ArrayList<Integer>> emotionToFacialExpression= new HashMap<>();
    public ArrayList<ImageButton> buttons = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facial_expression_game);

        initializeButtons();
        initializeFacialExpressionMapping();
        registerOnClickListeners();
        loadNextMatch();
        startTimer();
    }

    public void startTimer() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), FacialExpressionGameOver.class);
                intent.putExtra("correct", correct);
                intent.putExtra("incorrect", incorrect);
                startActivity(intent);
                finish();
            }
        }, 60000);
    }

    public void loadNextMatch() {
        // Select facial expressions (1 expression per emotion)
        ArrayList<String> emotionsToChooseFrom = selectEmotionsToChooseFrom(emotions, buttons.size());

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
            button.setBackgroundColor(Color.WHITE);
        }

    }

    public void registerOnClickListeners() {
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isCorrectAnswer = (boolean) v.getTag();
                if (isCorrectAnswer) {
                    System.out.println("Correct Answer");
                    correct += 1;
                    v.setTag(false);
                    v.setBackgroundColor(Color.GREEN);
                } else {
                    System.out.println("Incorrect Answer");
                    incorrect += 1;
                    v.setBackgroundColor(Color.RED);
                }
                System.out.println("Total correct: " + correct);
                System.out.println("Total incorrect: " + incorrect);

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        loadNextMatch();
                    }
                }, 200);
            }
        };

        for (ImageButton button : buttons) {
            button.setOnClickListener(onClickListener);
        }
    }

    public void initializeFacialExpressionMapping() {
        // Initialize emotionToFacialExpression hashMap.
        ArrayList<Integer> happyFacialExpressions = new ArrayList<>();
        happyFacialExpressions.add(R.drawable.happy_001);
        happyFacialExpressions.add(R.drawable.happy_002);
        happyFacialExpressions.add(R.drawable.happy_003);
        emotionToFacialExpression.put("Happy", happyFacialExpressions);

        ArrayList<Integer> sadFacialExpressions = new ArrayList<>();
        sadFacialExpressions.add(R.drawable.sad_001);
        sadFacialExpressions.add(R.drawable.sad_002);
        emotionToFacialExpression.put("Sad", sadFacialExpressions);

        ArrayList<Integer> surprisedFacialExpressions = new ArrayList<>();
        surprisedFacialExpressions.add(R.drawable.surprised_001);
        surprisedFacialExpressions.add(R.drawable.surprised_002);
        surprisedFacialExpressions.add(R.drawable.surprised_003);
        emotionToFacialExpression.put("Surprised", surprisedFacialExpressions);

        ArrayList<Integer> angerFacialExpression = new ArrayList<>();
        angerFacialExpression.add(R.drawable.anger_001);
        angerFacialExpression.add(R.drawable.anger_002);
        angerFacialExpression.add(R.drawable.anger_003);
        emotionToFacialExpression.put("Anger", angerFacialExpression);

        ArrayList<Integer> fearFacialExpressions = new ArrayList<>();
        fearFacialExpressions.add(R.drawable.fear_001);
        fearFacialExpressions.add(R.drawable.fear_002);
        fearFacialExpressions.add(R.drawable.fear_003);
        emotionToFacialExpression.put("Fear", fearFacialExpressions);

        ArrayList<Integer> disgustFacialExpressions = new ArrayList<>();
        disgustFacialExpressions.add(R.drawable.disgust_001);
        disgustFacialExpressions.add(R.drawable.disgust_002);
        disgustFacialExpressions.add(R.drawable.disgust_003);
        emotionToFacialExpression.put("Disgust", disgustFacialExpressions);

        ArrayList<Integer> confusedFacialExpressions = new ArrayList<>();
        confusedFacialExpressions.add(R.drawable.confused_001);
        confusedFacialExpressions.add(R.drawable.confused_002);
        confusedFacialExpressions.add(R.drawable.confused_003);
        emotionToFacialExpression.put("Confused", confusedFacialExpressions);

        ArrayList<Integer> neutralFacialExpressions = new ArrayList<>();
        neutralFacialExpressions.add(R.drawable.neutral_001);
        neutralFacialExpressions.add(R.drawable.neutral_002);
        neutralFacialExpressions.add(R.drawable.neutral_003);
        emotionToFacialExpression.put("Neutral", neutralFacialExpressions);
    }

    public void initializeButtons() {
        buttons.add((ImageButton) findViewById(R.id.button0));
        buttons.add((ImageButton) findViewById(R.id.button1));
        buttons.add((ImageButton) findViewById(R.id.button2));
        buttons.add((ImageButton) findViewById(R.id.button3));
        buttons.add((ImageButton) findViewById(R.id.button4));
        buttons.add((ImageButton) findViewById(R.id.button5));
    }

    public String pickRandomString(ArrayList<String> s) {
        Random r = new Random();
        int randomIndex = r.nextInt(s.size());
        return s.get(randomIndex);
    }

    public ArrayList<String> selectEmotionsToChooseFrom(ArrayList<String> emotions, int numberOfEmotions) {
        Collections.shuffle(emotions);
        ArrayList<String> emotionsToChooseFrom = new ArrayList<>();
        for (int i = 0; i < numberOfEmotions; i++) {
            emotionsToChooseFrom.add(emotions.get(i));
        }
        return emotionsToChooseFrom;
    }

    public ArrayList<String> getEmotions() {
        return emotions;
    }

    public void setEmotions(ArrayList<String> emotions) {
        this.emotions = emotions;
    }
}