package com.example.handoutlms;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CreateOnlineTutPhase1 extends AppCompatActivity {

    LinearLayout audio, video, pdf, quiz, location;
    String name, group_name;
    TextView grpNAME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_online_tut_phase1);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        audio = findViewById(R.id.audio);
        video = findViewById(R.id.video);
        pdf = findViewById(R.id.pdf);
        quiz = findViewById(R.id.quiz);
        location = findViewById(R.id.location);
        grpNAME = findViewById(R.id.groupNAME);

        Intent i = getIntent();
        group_name = i.getStringExtra("Group_name");

        grpNAME.setText(group_name);

        audio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = "audio";

                Intent i = new Intent(CreateOnlineTutPhase1.this, DocumentPaged.class);
                i.putExtra("name", name);
                i.putExtra("group_name", group_name);
                startActivity(i);
            }
        });
        video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = "video";

                Intent i = new Intent(CreateOnlineTutPhase1.this, DocumentPaged.class);
                i.putExtra("name", name);
                i.putExtra("group_name", group_name);
                startActivity(i);
            }
        });
        pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = "pdf";

                Intent i = new Intent(CreateOnlineTutPhase1.this, DocumentPaged.class);
                i.putExtra("name", name);
                i.putExtra("group_name", group_name);
                startActivity(i);
            }
        });
        quiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = "quiz";
            }
        });
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = "location";
            }
        });
    }
}
