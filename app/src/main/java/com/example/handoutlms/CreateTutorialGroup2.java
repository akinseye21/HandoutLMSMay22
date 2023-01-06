package com.example.handoutlms;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class CreateTutorialGroup2 extends AppCompatActivity {

    LinearLayout online_tut, offline_tut;
    String email;
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_tutorial_group2);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Intent j = getIntent();
        email = j.getStringExtra("email");

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        online_tut = findViewById(R.id.lin_online_tutorial);
        online_tut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CreateTutorialGroup2.this, CreateTutorialGroupOnline.class);
                i.putExtra("email", email);
                startActivity(i);
            }
        });

        offline_tut = findViewById(R.id.lin_offline_tutorial);
        offline_tut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CreateTutorialGroup2.this, CreateTutorialGroupOffline.class);
                i.putExtra("email", email);
                startActivity(i);
            }
        });
    }
}
