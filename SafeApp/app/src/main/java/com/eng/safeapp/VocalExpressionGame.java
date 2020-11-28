package com.eng.safeapp;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/*
    Author: Jacobus Burger <therealjacoburger@gmail.com>
    Last Modified: 2020-11-27

    Descripion: This class represents the vocal expression game. Which plays voice clips that the
    player must guess the emotionality of.

    Details:
    (talk about the data structures used, how it uses them, etc)

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
 */
public class VocalExpressionGame extends AppCompatActivity implements View.OnClickListener {
    // helper values
    final int startTime = 60; // amount of time to give player in seconds
    Random rand = new Random(); // random number generator
    // game state values
    emotions answer;
    MediaPlayer voice; // voice depends on answer
    final Intent game = new Intent(getApplicationContext(), VocalExpressionGame.class);
    final Intent gameEnd = new Intent(getApplicationContext(), VocalExpressionGameOver.class);
    final int correctPoints = 100;
    final int incorrectPoints = -100;
    int score = 0;

    enum emotions {
        Anger,
        Confused,
        Disgust,
        Happy,
        Neutral,
        Sad,
        Surprised,
        Fear, // must be last in enum, due to having irregular number of files
    }

    // array of emotion voice acting snippets
    // note: must correspond to the order of emotions enum
    final MediaPlayer[] voices = new MediaPlayer[] {
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

    // note: must correspond to the order of emotions enum
    Integer[] images = new Integer[]{
            R.drawable.anger_word,
            R.drawable.confused_word,
            R.drawable.disgust_word,
            R.drawable.happy_word,
            R.drawable.neutral_word,
            R.drawable.sad_word,
            R.drawable.surprised_word,
            R.drawable.fear_word,
    };

    ImageButton[] buttons = new ImageButton[]{
            ((ImageButton) findViewById(R.id.button0)),
            ((ImageButton) findViewById(R.id.button1)),
            ((ImageButton) findViewById(R.id.button2)),
            ((ImageButton) findViewById(R.id.button3)),
            ((ImageButton) findViewById(R.id.button4)),
            ((ImageButton) findViewById(R.id.button5)),
    };

    Map<ImageButton, emotions> buttonEmotion = new HashMap<ImageButton, emotions>();


    // assigns random emotion to button (ensuring no repeats) and assigns a corresponding image
    public void setButtons() {
        // assign random emotions to each answer button, ensuring no repeats
        List<emotions> chosenEmotions = null;
        buttonEmotion.clear();
        for (ImageButton button : buttons) {
            emotions emotion = emotions.values()[rand.nextInt(emotions.values().length)];
            while (!chosenEmotions.contains(emotion)) {
                buttonEmotion.put(button, emotion);
                chosenEmotions.add(emotion);
            }

            // assign image to match button emotion
            button.setImageResource(images[emotion.ordinal()]);
        }

        // pick the answer emotion randomly from one of those chosen
        answer = chosenEmotions.get(rand.nextInt(emotions.values().length));
    }

    // note: depends on emotions being grouped together in voices[]
    // selects random voice file that corresponds to current answer emotion, and assign it to global voice
    public void setVoice() {
        emotions emotion = answer;
        if (emotion == emotions.Fear) { // fear is a special case because it has 3 instead of 4 files
            voice = voices[rand.nextInt(3) + 4 * emotions.Fear.ordinal()];
        } else {
            voice = voices[rand.nextInt(4) + 4 * emotion.ordinal()];
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.vocalButton:
                // replay voice file
                voice.reset();
                voice.start();
                break;
            case R.id.quitVocalExpressionGameButton:
                startActivity(new Intent(getApplicationContext(), MainMenu.class));
                finish();
                break;
            default:
                // determine correctness, and act accordingly
                emotions chosenEmotion = buttonEmotion.get((ImageButton) v);
                if (chosenEmotion == answer) {
                    score += correctPoints;
                    v.setBackgroundColor(Color.GREEN);
                } else {
                    score += incorrectPoints;
                    v.setBackgroundColor(Color.RED);
                }

                // reset buttons and continue to next round
                setButtons();
                setVoice();

                // reset buttons color after 200ms
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        for (ImageButton button : buttons) {
                            button.setBackgroundColor(Color.WHITE);
                        }
                    }
                }, 200);
                break;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vocal_expression_game);
        startActivity(game);

        // game start functions
        setButtons();
        setVoice();

        // end the game once the time runs out
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                gameEnd.putExtra("score", score);
                startActivity(gameEnd);
                finish();
            }
        }, startTime * 1000);
    }
}
