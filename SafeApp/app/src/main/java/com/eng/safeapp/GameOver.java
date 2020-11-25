package com.eng.safeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

/* javadoc?
 * what does this class do, what is it used for?
 */
public class GameOver extends AppCompatActivity {
    // clearer and more consistent var names would be preferred
    public static int reward = 100; // why not "reward?"
    public static int penalty = 30; // why not "penalty?"

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facial_expression_game_over);

        findViewById(R.id.highScoreImage).setVisibility(View.INVISIBLE);

        Button mainMenuBtn = findViewById(R.id.gameOverMainMenuBtn);
        mainMenuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (getApplicationContext(), MainMenu.class);
                startActivity(intent);
                finish();
            }
        });

        int correct = getIntent().getIntExtra("correct", 0);
        int incorrect = getIntent().getIntExtra("incorrect", 0);
        final int score = calculateScore(correct, incorrect);
        TextView scoreTextView = findViewById(R.id.scoreTextView);
        scoreTextView.setText(score + "");

        mAuth = FirebaseAuth.getInstance();
        DatabaseReference user = FirebaseDatabase.getInstance().getReference("Users").child(mAuth.getCurrentUser().getUid());
        String game = getIntent().getStringExtra("game");
        if (game.equals("facialExpression")) {
            user.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    User userProfile = dataSnapshot.getValue(User.class);

                    if(userProfile != null) {
                        if (userProfile.facialExpressionScoreHistory == null) {
                            userProfile.facialExpressionScoreHistory = new ArrayList<>();
                        }
                        if (score > Collections.max(userProfile.facialExpressionScoreHistory)) {
                            findViewById(R.id.highScoreImage).setVisibility(View.VISIBLE);
                        }
                        userProfile.facialExpressionScoreHistory.add(score);
                        dataSnapshot.getRef().setValue(userProfile);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast t = new Toast(getApplicationContext());
                    View v = LayoutInflater.from(getApplicationContext()).inflate(R.layout.toast_layout_something_wrong_happened, null);
                    t.setView(v);
                    t.setDuration(Toast.LENGTH_LONG);
                    t.setGravity(Gravity.CENTER, 0, 0);
                    t.show();
                }
            });
        } else {
            // Sound Game
            user.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    User userProfile = dataSnapshot.getValue(User.class);

                    if(userProfile != null) {
                        if (userProfile.soundScoreHistory == null) {
                            userProfile.soundScoreHistory = new ArrayList<>();
                        }
                        if (score > Collections.max(userProfile.soundScoreHistory)) {
                            findViewById(R.id.highScoreImage).setVisibility(View.VISIBLE);
                        }
                        userProfile.soundScoreHistory.add(score);
                        dataSnapshot.getRef().setValue(userProfile);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast t = new Toast(getApplicationContext());
                    View v = LayoutInflater.from(getApplicationContext()).inflate(R.layout.toast_layout_something_wrong_happened, null);
                    t.setView(v);
                    t.setDuration(Toast.LENGTH_LONG);
                    t.setGravity(Gravity.CENTER, 0, 0);
                    t.show();
                }
            });
        }


    }

    // what about "caculateTotal"?
    private int calculateScore(int correct, int incorrect) {
        int score = (correct * reward) - (incorrect * penalty);
        if (score < 0) {
            score = 0;
        }
        return score;
    }
}
