package com.eng.safeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class FacialExpressionGameOver extends AppCompatActivity {

    public static int correctPoints = 100;
    public static int incorrectPenalty = 30;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facial_expression_game_over);

        Button mainMenuBtn = findViewById(R.id.gameOverMainMenuBtn);
        mainMenuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (getApplicationContext(), MainMenu.class);
                startActivity(intent);
            }
        });

        int correct = getIntent().getIntExtra("correct", 0);
        int incorrect = getIntent().getIntExtra("incorrect", 0);
        int score = calculateScore(correct, incorrect);

        TextView scoreTextView = findViewById(R.id.scoreTextView);
        scoreTextView.setText(score + "");
    }

    private int calculateScore(int correct, int incorrect) {
        int score = (correct * 100) - (incorrect * 30);
        if (score < 0) {
            score = 0;
        }
        return score;
    }
}