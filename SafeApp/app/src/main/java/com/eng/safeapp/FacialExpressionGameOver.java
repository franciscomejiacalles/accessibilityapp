package com.eng.safeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/* javadoc?
 * what does this class do, what is it used for?
 */
public class FacialExpressionGameOver extends AppCompatActivity {
    // clearer and more consistent var names would be preferred
    public static int correctPoints = 100; // why not "reward?"
    public static int incorrectPenalty = 30; // why not "penalty?"

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facial_expression_game_over);

        // what's this bit doing?
        Button mainMenuBtn = findViewById(R.id.gameOverMainMenuBtn);
        mainMenuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (getApplicationContext(), MainMenu.class);
                startActivity(intent);
            }
        });

        // what's this bit doing?
        int correct = getIntent().getIntExtra("correct", 0);
        int incorrect = getIntent().getIntExtra("incorrect", 0);
        int score = calculateScore(correct, incorrect);
        TextView scoreTextView = findViewById(R.id.scoreTextView);
        scoreTextView.setText(score + "");
    }

    // what about "caculateTotal"?
    private int calculateScore(int correct, int incorrect) {
        int score = (correct * 100) - (incorrect * 30);
        if (score < 0) {
            score = 0;
        }
        return score;
    }
}
