package com.example.handoutlms.uniabuja;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.handoutlms.R;
import com.example.handoutlms.activities.FeedsDashboard;
import com.example.handoutlms.activities.NigerianUniversities;
import com.example.handoutlms.activities.PastQuestion;

public class GetStarted extends AppCompatActivity {

    SharedPreferences preferences;
    String email;
    ImageView back;
    Button getStarted;
    String uniname;
    TextView txt1, txt2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_started);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        preferences = getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        email = preferences.getString("email", "not available");

        Intent i = getIntent();
        uniname = i.getStringExtra("uniname");

        txt1 = findViewById(R.id.school_name1);
        txt1.setText(uniname);
        txt2 = findViewById(R.id.school_name2);
        txt2.setText(uniname);
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(GetStarted.this, NigerianUniversities.class);
                startActivity(i);
            }
        });
        getStarted = findViewById(R.id.getstarted);
        getStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(GetStarted.this, Page1.class);
                i.putExtra("uniname", uniname);
                startActivity(i);
            }
        });
    }
}