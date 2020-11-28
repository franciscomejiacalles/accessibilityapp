package com.eng.safeapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

// this class simply takes the user to the game over screen and shows them their score
public class VocalExpressionGameOver extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vocal_expression_game_over);

        Button mainMenuBtn = findViewById(R.id.gameOverMainMenuBtn);
        mainMenuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (getApplicationContext(), MainMenu.class);
                startActivity(intent);
            }
        });

        int score = getIntent().getIntExtra("score", 0);
        TextView scoreTextView = findViewById(R.id.scoreTextView);
        scoreTextView.setText(score + "");
    }
}
