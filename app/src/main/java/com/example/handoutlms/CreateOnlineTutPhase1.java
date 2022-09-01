package com.example.handoutlms;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CreateOnlineTutPhase1 extends AppCompatActivity {

    LinearLayout audio, video, pdf, quiz;
    String name, group_name, notification;
    TextView grpNAME;
    TextView view_resources;
    ImageView back;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_online_tut_phase1);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        audio = findViewById(R.id.audio);
        video = findViewById(R.id.video);
        pdf = findViewById(R.id.pdf);
        quiz = findViewById(R.id.quiz);
        grpNAME = findViewById(R.id.groupNAME);
        back = findViewById(R.id.back);
        view_resources = findViewById(R.id.view_resources);

        Intent i = getIntent();
        group_name = i.getStringExtra("Group_name");
        notification = i.getStringExtra("notification");

        Intent intent = new Intent(this, AddOptions.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.logo)
                .setContentTitle("Handout LMS")
                .setContentText(notification)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                // Set the intent that will fire when the user taps the notification
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(9, builder.build());



        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent i = new Intent(CreateOnlineTutPhase1.this, CreateTutorialGroup2.class);
               startActivity(i);
            }
        });

        view_resources.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CreateOnlineTutPhase1.this, ViewGroupResources.class);
                i.putExtra("name", group_name);
                startActivity(i);
            }
        });

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
    }
}
