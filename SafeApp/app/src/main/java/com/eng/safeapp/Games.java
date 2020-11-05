package com.eng.safeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/* javadoc?
 * what does this class do, what is it used for?
 */
public class Games extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_games);

        Button facialExpressionBtn = findViewById(R.id.facialExpressionButton);
        facialExpressionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (getApplicationContext(), FacialExpressionGame.class);
                startActivity(intent);
            }
        });
    }
}
