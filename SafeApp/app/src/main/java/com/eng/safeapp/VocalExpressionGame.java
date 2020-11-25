package com.eng.safeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class VocalExpressionGame extends AppCompatActivity implements View.OnClickListener {
    // helper values
    final int startTime = 10; // amount of time to give player in seconds
    Random rand = new Random(); // random number generator

    // game state values
    emotions answer;
    MediaPlayer voice; // voice depends on answer
    private int correct = 0;
    private int incorrect = 0;

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
    private MediaPlayer[] voices;

    // note: must correspond to the order of emotions enum
    private Integer[] images;

    private ImageButton[] buttons;

    Map<ImageButton, emotions> buttonEmotion = new HashMap<ImageButton, emotions>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vocal_expression_game);

        initializeVoices();
        initializeImages();
        initializeButtons();

        // Set on click listener
        findViewById(R.id.vocalButton).setOnClickListener(this);
        findViewById(R.id.quitVocalExpressionGameButton).setOnClickListener(this);
        for (ImageButton button : buttons) {
            button.setOnClickListener(this);
        }

        // game start functions
        setButtons();
        setVoice();

        // end the game once the time runs out
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                Intent gameEnd = new Intent(getApplicationContext(), GameOver.class);
                gameEnd.putExtra("correct", correct);
                gameEnd.putExtra("incorrect", incorrect);
                gameEnd.putExtra("game", "vocalExpression");
                startActivity(gameEnd);
                finish();
            }
        }, startTime * 1000);
    }

    public void initializeButtons() {
        buttons = new ImageButton[]{
                ((ImageButton) findViewById(R.id.sound_button_1)),
                ((ImageButton) findViewById(R.id.sound_button_2)),
                ((ImageButton) findViewById(R.id.sound_button_3)),
                ((ImageButton) findViewById(R.id.sound_button_4)),
                ((ImageButton) findViewById(R.id.sound_button_5)),
                ((ImageButton) findViewById(R.id.sound_button_6)),
        };
    }

    public void initializeImages() {
        images = new Integer[]{
                R.drawable.anger_word,
                R.drawable.confused_word,
                R.drawable.disgust_word,
                R.drawable.happy_word,
                R.drawable.neutral_word,
                R.drawable.sad_word,
                R.drawable.surprised_word,
                R.drawable.fear_word,
        };
    }

    public void initializeVoices() {
        voices = new MediaPlayer[] {
                MediaPlayer.create(this, R.raw.anger_audio_1),
                MediaPlayer.create(this, R.raw.anger_audio_2),
                MediaPlayer.create(this, R.raw.anger_audio_3),
                MediaPlayer.create(this, R.raw.anger_audio_4),
                MediaPlayer.create(this, R.raw.confused_audio_1),
                MediaPlayer.create(this, R.raw.confused_audio_2),
                MediaPlayer.create(this, R.raw.confused_audio_3),
                MediaPlayer.create(this, R.raw.confused_audio_4),
                MediaPlayer.create(this, R.raw.disgust_audio_1),
                MediaPlayer.create(this, R.raw.disgust_audio_2),
                MediaPlayer.create(this, R.raw.disgust_audio_3),
                MediaPlayer.create(this, R.raw.disgust_audio_4),
                MediaPlayer.create(this, R.raw.happy_audio_1),
                MediaPlayer.create(this, R.raw.happy_audio_2),
                MediaPlayer.create(this, R.raw.happy_audio_3),
                MediaPlayer.create(this, R.raw.happy_audio_4),
                MediaPlayer.create(this, R.raw.neutral_audio_1),
                MediaPlayer.create(this, R.raw.neutral_audio_2),
                MediaPlayer.create(this, R.raw.neutral_audio_3),
                MediaPlayer.create(this, R.raw.neutral_audio_4),
                MediaPlayer.create(this, R.raw.sad_audio_1),
                MediaPlayer.create(this, R.raw.sad_audio_2),
                MediaPlayer.create(this, R.raw.sad_audio_3),
                MediaPlayer.create(this, R.raw.sad_audio_4),
                MediaPlayer.create(this, R.raw.surprised_audio_1),
                MediaPlayer.create(this, R.raw.surprised_audio_2),
                MediaPlayer.create(this, R.raw.surprised_audio_3),
                MediaPlayer.create(this, R.raw.surprised_audio_4),
                MediaPlayer.create(this, R.raw.fear_audio_1),
                MediaPlayer.create(this, R.raw.fear_audio_2),
                MediaPlayer.create(this, R.raw.fear_audio_3),
        };
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.vocalButton:
                // replay voice file
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

                    correct++;
                    v.setBackgroundColor(Color.GREEN);
                } else {
                    incorrect++;
                    v.setBackgroundColor(Color.RED);
                }

                // reset buttons color after 200ms
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        for (ImageButton button : buttons) {
                            button.setBackgroundColor(Color.WHITE);
                        }
                        // reset buttons and continue to next round
                        setButtons();
                        setVoice();
                    }
                }, 200);
                break;
        }
    }

    // assigns random emotion to button (ensuring no repeats) and assigns a corresponding image
    public void setButtons() {
        // assign random emotions to each answer button, ensuring no repeats
        ArrayList<emotions> chosenEmotions = new ArrayList<>();
        buttonEmotion.clear();
        for (ImageButton button : buttons) {
            emotions emotion = emotions.values()[rand.nextInt(emotions.values().length)];
            while (chosenEmotions.contains(emotion)) {
                emotion = emotions.values()[rand.nextInt(emotions.values().length)];
            }
            buttonEmotion.put(button, emotion);
            chosenEmotions.add(emotion);
            // assign image to match button emotion
            button.setImageResource(images[emotion.ordinal()]);
        }

        // pick the answer emotion randomly from one of those chosen
        answer = chosenEmotions.get(rand.nextInt(chosenEmotions.size()));
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
        voice.start();
    }
}