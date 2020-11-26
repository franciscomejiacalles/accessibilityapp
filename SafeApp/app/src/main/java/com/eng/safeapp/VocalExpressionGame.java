package com.eng.safeapp;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/*
    Author: Jacobus Burger <therealjacoburger@gmail.com>
    Last Modified: 2020-11-25
 * what does this class do, what is it used for?
 */
public class VocalExpressionGame extends AppCompatActivity {
    final int startTime = 60; // amount of time to give player in seconds
    Random rand = new Random(); // random number generator

    //  unfortunately I'm unaware of any nicer way to do this
    // array of emotion voice acting snippets
    MediaPlayer[] vocalEmotions = new MediaPlayer[] {
            MediaPlayer.create(this, R.raw.anger1),
            MediaPlayer.create(this, R.raw.anger2),
            MediaPlayer.create(this, R.raw.anger3),
            MediaPlayer.create(this, R.raw.anger4),
            MediaPlayer.create(this, R.raw.confused1),
            MediaPlayer.create(this, R.raw.confused2),
            MediaPlayer.create(this, R.raw.confused3),
            MediaPlayer.create(this, R.raw.confused4),
            MediaPlayer.create(this, R.raw.disgust1),
            MediaPlayer.create(this, R.raw.disgust2),
            MediaPlayer.create(this, R.raw.disgust3),
            MediaPlayer.create(this, R.raw.disgust4),
            MediaPlayer.create(this, R.raw.fear1),
            MediaPlayer.create(this, R.raw.fear2),
            MediaPlayer.create(this, R.raw.fear3),
            MediaPlayer.create(this, R.raw.happy1),
            MediaPlayer.create(this, R.raw.happy2),
            MediaPlayer.create(this, R.raw.happy3),
            MediaPlayer.create(this, R.raw.happy4),
            MediaPlayer.create(this, R.raw.neutral1),
            MediaPlayer.create(this, R.raw.neutral2),
            MediaPlayer.create(this, R.raw.neutral3),
            MediaPlayer.create(this, R.raw.neutral4),
            MediaPlayer.create(this, R.raw.sad1),
            MediaPlayer.create(this, R.raw.sad2),
            MediaPlayer.create(this, R.raw.sad3),
            MediaPlayer.create(this, R.raw.sad4),
            MediaPlayer.create(this, R.raw.surprised1),
            MediaPlayer.create(this, R.raw.surprised2),
            MediaPlayer.create(this, R.raw.surprised3),
            MediaPlayer.create(this, R.raw.surprised4),
    };

    /*
        @  while (game)
            * sound = select random voice recording
            * generate images for `button[0-5]`
            * speak(sound)
            if pressed `voiceButton`:
                speak(sound)
            if pressed `button[0-5]`:
                give score
                go back to @
        !  go to game over screen
        END
    */


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

        final Intent intent = new Intent(getApplicationContext(), VocalExpressionGame.class);
        startActivity(intent);

        // start a timer to stop the game
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                final Intent endGame = new Intent(getApplicationContext(), FacialExpressionGameOver.class); // temporary substitute
                startActivity(endGame);
                // endGame.putExtra("variable", present_value); -- if i understand this throws values to new intents
                finish();
            }
        }, startTime * 1000);

        MediaPlayer emotion = getRandomEmotion();
        // ImageButton playEmotion = this.findViewById(R.id.voiceButton);
        /* TODO
        - create and use click event listeners in program
        - ...
         */

    }

    // returns a random voice file from vocalEmotions
    public MediaPlayer getRandomEmotion() {
        return vocalEmotions[rand.nextInt(vocalEmotions.length)];
    }

    /*
    Timer timer;
    private String currentEmotion; // could this be represented as
                                   // a num with a corresponding enum?
    private int correct;    // why are these integers?
    private int incorrect;  // ~

    private ArrayList<String> emotions = new ArrayList<>(Arrays.asList("Happy", 
            "Sad", "Surprised", "Anger", "Fear", "Disgust", "Confused", "Neutral"));
    private HashMap<String, ArrayList<Integer>> emotionToFacialExpression= new HashMap<>();
    public ArrayList<ImageButton> buttons = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facial_expression_game);

        ImageButton quitBtn = findViewById(R.id.quitFacialExpressionGameBtn);
        quitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainMenu.class));
                finish();
            }
        });

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
        // there must be a better way!
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
        // could this be done differently?
        buttons.add((ImageButton) findViewById(R.id.button0));
        buttons.add((ImageButton) findViewById(R.id.button1));
        buttons.add((ImageButton) findViewById(R.id.button2));
        buttons.add((ImageButton) findViewById(R.id.button3));
        buttons.add((ImageButton) findViewById(R.id.button4));
        buttons.add((ImageButton) findViewById(R.id.button5));
    }

    // a clearer name would be preferred, for eg: chooseRandomEmotion
    // that said, this can be removed if/once we implement an emotions enum
    public String pickRandomString(ArrayList<String> s) {
        Random r = new Random();
        int randomIndex = r.nextInt(s.size());
        return s.get(randomIndex);
    }

    // could be "selectEmotion" instead
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

    public String getCurrentEmotion() {
        return currentEmotion;
    }

    public void setCurrentEmotion(String currentEmotion) {
        this.currentEmotion = currentEmotion;
    }
     */
}
