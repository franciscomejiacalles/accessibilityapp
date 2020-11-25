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
public class Games extends AppCompatActivity implements View.OnClickListener {

    private TextView gamesTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_games);

        gamesTitle = findViewById(R.id.gamesTitle);
        gamesTitle.setOnClickListener(this);

        Button facialExpressionBtn = findViewById(R.id.facialExpressionButton);
        facialExpressionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (getApplicationContext(), FacialExpressionGame.class);
                startActivity(intent);
            }
        });

        Button soundBtn = findViewById(R.id.soundButton);
        soundBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (getApplicationContext(), VocalExpressionGame.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.gamesTitle:
                startActivity(new Intent(this, MainMenu.class));
                break;
        }
    }
}
