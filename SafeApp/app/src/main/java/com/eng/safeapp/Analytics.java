package com.eng.safeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Analytics extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analytics);

        TextView t = findViewById(R.id.analyticsTitle);
        t.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Analytics.this, MainMenu.class));
                finish();
            }
        });

        Button facialExpressionBtn = findViewById(R.id.analyticsFacialExpressionBtn);
        facialExpressionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (getApplicationContext(), FacialExpressionAnalytics.class);
                intent.putExtra("game", "facialExpression");
                startActivity(intent);
            }
        });

        Button soundBtn = findViewById(R.id.analyticsSoundBtn);
        soundBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (getApplicationContext(), FacialExpressionAnalytics.class);
                intent.putExtra("game", "sound");
                startActivity(intent);
            }
        });
    }
}