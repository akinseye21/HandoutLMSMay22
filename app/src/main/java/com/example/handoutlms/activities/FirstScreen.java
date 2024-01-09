package com.example.handoutlms.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.example.handoutlms.R;

public class FirstScreen extends AppCompatActivity {

    SharedPreferences preferences;
    String email;
    Button continu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_screen);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        continu = findViewById(R.id.continu);
        continu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(FirstScreen.this, LoginSignup.class);
                startActivity(i);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        preferences = getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        email = preferences.getString("email", "");
        if (!email.equals("")){
            Intent i = new Intent(FirstScreen.this, FeedsDashboard.class);
            i.putExtra("email", email);
            i.putExtra("sent from", "Login");
            startActivity(i);
        }else{
            //do nothing
        }
    }
}
