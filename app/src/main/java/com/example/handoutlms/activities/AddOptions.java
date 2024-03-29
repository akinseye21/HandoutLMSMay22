package com.example.handoutlms.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.handoutlms.R;

public class AddOptions extends AppCompatActivity {

    LinearLayout tutorials, games, gigs;
    String email;
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_options);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Intent j = getIntent();
        email = j.getStringExtra("email");

        tutorials = findViewById(R.id.tutorials);
        games = findViewById(R.id.games);
        gigs = findViewById(R.id.gig);
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        tutorials.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), CreateTutorialGroup2.class);
                i.putExtra("email", email);
                startActivity(i);
            }
        });
        games.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), CreateGames.class);
                i.putExtra("email", email);
                startActivity(i);
            }
        });
        gigs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), CreateGig1.class);
                startActivity(i);
            }
        });
    }

}
