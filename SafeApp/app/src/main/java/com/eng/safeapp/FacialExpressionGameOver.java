package com.eng.safeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class FacialExpressionGameOver extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facial_expression_game_over);

        int correct = getIntent().getIntExtra("correct", 0);
        int incorrect = getIntent().getIntExtra("incorrect", 0);
        int score = (correct * 100) - (incorrect * 30);
        if (score < 0) {
            score = 0;
        }

        TextView scoreTextView = findViewById(R.id.scoreTextView);
        scoreTextView.setText(score + "");
    }
}