package com.eng.safeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;

public class FacialExpressionAnalytics extends AppCompatActivity {

    private static final String TAG = "FacialExpressionAnalytics";
    private LineChart chart;

    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facial_expression_analytics);

        TextView t = findViewById(R.id.analyticsFacialExpressionTitle);
        t.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FacialExpressionAnalytics.this, MainMenu.class));
                finish();
            }
        });

        final String game = getIntent().getStringExtra("game");

        if (game.equals("sound")) {
            TextView title = findViewById(R.id.gameAnalyticsTitle);
            title.setText("Sound Analytics");
        }
        
        user = FirebaseAuth.getInstance().getCurrentUser();
        String userID = user.getUid();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");

        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User userProfile = dataSnapshot.getValue(User.class);

                if(userProfile != null) {
                    // Update chart
                    chart = findViewById(R.id.facialExpressionSHLineChart);
                    chart.setDragEnabled(true);
                    chart.setScaleEnabled(false);
                    XAxis xAxis = chart.getXAxis();
                    xAxis.setGranularity(1f);
                    xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                    xAxis.setDrawGridLines(false);
                    YAxis rightAxis = chart.getAxisRight();
                    rightAxis.setEnabled(false);
                    chart.getAxisLeft().setDrawGridLines(false);
                    
                    ArrayList<Entry> dataPoints = new ArrayList<>();
                    ArrayList<Integer> scoreHistory = userProfile.facialExpressionScoreHistory;
                    if (game.equals("sound")) {
                        scoreHistory = userProfile.soundScoreHistory;
                    }
                    for (int i = 1; i < scoreHistory.size(); i++) {
                        float yVal = scoreHistory.get(i);
                        dataPoints.add(new Entry(i-1, yVal));
                    }
                    LineDataSet dataSet = new LineDataSet(dataPoints, "Score History");
                    dataSet.setFillAlpha(110);
                    ArrayList<ILineDataSet> dataSets = new ArrayList<>();
                    dataSets.add(dataSet);
                    LineData lineData = new LineData(dataSets);
                    chart.getDescription().setEnabled(false);
                    chart.setData(lineData);
                    chart.invalidate();

                    // Set high score
                    TextView hsTextView = findViewById(R.id.analyticsHSTextView);
                    hsTextView.setText(Collections.max(scoreHistory) + "");

                    // Set minutes played
                    TextView minutesPlayedTextView = findViewById(R.id.analyticsMinutesPlayedTextView);
                    int minutesPlayed = scoreHistory.size()-1;
                    minutesPlayedTextView.setText(minutesPlayed + "");
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