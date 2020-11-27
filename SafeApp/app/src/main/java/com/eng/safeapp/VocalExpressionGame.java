package com.eng.safeapp;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
public class VocalExpressionGame extends AppCompatActivity implements View.OnClickListener {
    // helper values
    final int startTime = 60; // amount of time to give player in seconds
    Random rand = new Random(); // random number generator

    // game state values
    emotions answer;
    MediaPlayer voice;

    final Intent game = new Intent(getApplicationContext(), VocalExpressionGame.class);
    final Intent gameEnd = new Intent(getApplicationContext(), VocalExpressionGameOver.class);

    // array of emotion voice acting snippets
    // getEmotion depends on emotions being grouped together, or it cannot retrieve the correct emotion
    MediaPlayer[] voices = new MediaPlayer[] {
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
            MediaPlayer.create(this, R.raw.fear1),
            MediaPlayer.create(this, R.raw.fear2),
            MediaPlayer.create(this, R.raw.fear3),
    };

    enum emotions {
        Anger,
        Confused,
        Disgust,
        Happy,
        Neutral,
        Sad,
        Surprised,
        Fear,
    }



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

        startActivity(game);

        // start a timer to stop the game
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                startActivity(gameEnd);
                // gameEnd.putExtra("variable", present_value); -- if i understand this throws values to new intents
                finish();
            }
        }, startTime * 1000);


    }

    // sets global current answer emotion
    public void setAnswer() {
        answer = emotions.values()[rand.nextInt()];
    }

    // selects random emotion that corresponds to current emotion
    public MediaPlayer getVoice(emotions emotion) {
        if (emotion == emotions.Fear) // fear is a special case because it has 3 instead of 4 files
            return voices[rand.nextInt(3) + 4 * emotions.Fear.ordinal()];
        return voices[rand.nextInt(4) + 4 * emotion.ordinal()];
    }

    // checks given user answer against current answer
    public void checkAnswer(emotions emotion) { }

    // each new round choose a new random number corresponding to an emotion
    // select from those emotions, choosing a random voice
    // assign random emotions to each answer button, keeping in mind the answer emotion
    // score based on if answer matches answer emotion


    // voice button is assigned current voice file
    // - when clicked, reset and play the sound file
    // answer buttons are assigned a random number
    // - when clicked, that number is checked against the current emotion
    //      if its correct: the button goes green and a positive score is given
    //      if its not: the button goes red and a negative score is given
    // .. after click, pick a new emotion, pick a new voice file, assign new
    //    random pictures to answer buttons, and start again until time runs out




    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.vocalButton:
                voice.reset();
                voice.start();
                break;
            // for other cases, get emotion enum assigned to answer button and checkAnswer()
        }
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
