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

/*
    Author: Jacobus Burger (AKA: LordUbuntu) <therealjacoburger@gmail.com>
    Last Modified: 2020-12-01
    Descripion: This class represents the vocal expression game. Which plays voice clips that the
    player must guess the emotionality of by selecting corresponding emotion buttons.


    Data(types/structures):
        - `MediaPlayer[] voices` collects all voice files into an array that can be accessed with the setVoice method using
            the emotions enum as a navigational index. This works because the order of its groups corresponds mathematically
            to the order of entries in the emotions enum.
        - `Integer[] images` keeps a record of each of the 8 emotion images with the emotions enum acting as a navigaitonal
            index. This works because the order of its entries corresponds to the order of entries in the emotions enum.
        - `ImageButton[] buttons` groups the user input buttons into an array for easy manipulation.
        - `Map<ImageButton, emotions> buttonEmotion` records the mapping of each button to an assigned emotion. This information is
            then used to deduce what image to assign to each button, and to determine whether the button has the correct emotion
            corresponding to the answer.
            
    Operational Details:
    VocalExpressionGame uses an `emotions` enum to track which voice files to use, which images
    to use, and what the correct answer for the round is.

    The OnCreate method is used to setup the initial gamestate. Once called it begins a concurrent
    timer that will switch the player over to the VocalExpressionGameOver class after it has run
    for startTime seconds.

    The OnClick method is used to determine what button was pressed and act accordingly.
        If that button is the voiceButton, then it will replay the voice file.
        If that button is the quitButton, then it will quit to main menu.
        If that button is a choice button, then it will check if it is correct.
            If it is, it will increment the correct score, turn the button green, and move to next round.
            If it is not, it will increment the incorrect score, turn the button red, and move to next round.

    The setButtons method chooses as many random emotions as there are buttons (with no repeats),
    recording that association in the buttonEmotion mapping. Then each button is given an image
    that corresponds with their given emotion, and finally the answer emotion is chosen from among
    them at random.

    The setVoice method chooses a random voice file that corresponds to the current global answer
    emotion using the following formula (with the assumption that all of the files in voices
    are ordered in the same order as in the emotions enum, and that all similar emotions are
    clumped together into inorder groups):
            current voice = voices at index random [0, 4) + 4 * emotional offset
        The formula has a special exception for fear, since it only has 3 files. Due to its special
        circumstance it must come last in the voices array and be selected with a specialized formula
        or the program is likely to not work as intended.
 */
public class VocalExpressionGame extends AppCompatActivity implements View.OnClickListener {
    // helper values
    final int startTime = 10; // amount of time to give player for the game in seconds
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

    // note: must correspond to the order of emotions enum upon initialization
    private MediaPlayer[] voices;
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
