package com.eng.safeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/* javadoc?
 * what does this class do, what is it used for?
 */
public class MainMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        Button gamesBtn = findViewById(R.id.gamesBtn);
        gamesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (getApplicationContext(), Games.class);
                startActivity(intent);
            }
        });

        Button profileBtn = findViewById(R.id.profileBtn);
        profileBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (getApplicationContext(), UserProfile.class);
                startActivity(intent);
            }
        });

        Button analyticsBtn = findViewById(R.id.analyticsBtn);
        analyticsBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (getApplicationContext(), Analytics.class);
                startActivity(intent);
            }
        });
    }
}
