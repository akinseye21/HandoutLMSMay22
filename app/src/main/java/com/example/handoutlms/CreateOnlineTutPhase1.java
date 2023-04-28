package com.example.handoutlms;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CreateOnlineTutPhase1 extends AppCompatActivity {

    LinearLayout video, pdf;
    String name, group_name, notification;
    TextView grpNAME;
    LinearLayout view_resources;
    RelativeLayout relBack;
    Dialog myDialog;
    SharedPreferences preferences;
    String got_email;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_online_tut_phase1);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        preferences = getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        got_email = preferences.getString("email", "not available");

//        audio = findViewById(R.id.audio);
        video = findViewById(R.id.video);
        pdf = findViewById(R.id.pdf);
//        quiz = findViewById(R.id.quiz);
        grpNAME = findViewById(R.id.groupNAME);
        view_resources = findViewById(R.id.view_resources);
        relBack = findViewById(R.id.rel1);

        Intent i = getIntent();
        group_name = i.getStringExtra("Group_name");
//        notification = i.getStringExtra("notification");

//        Intent intent = new Intent(this, AddOptions.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
//
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
//                .setSmallIcon(R.drawable.logo)
//                .setContentTitle("Handout LMS")
//                .setContentText(notification)
//                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//                // Set the intent that will fire when the user taps the notification
//                .setContentIntent(pendingIntent)
//                .setAutoCancel(true);
//
//        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
//        notificationManager.notify(9, builder.build());



        relBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               //show popUp to exit
                myDialog = new Dialog(CreateOnlineTutPhase1.this);
                myDialog.setContentView(R.layout.custom_popup_exit);
                TextView text1 = myDialog.findViewById(R.id.text1);
                TextView text2 = myDialog.findViewById(R.id.text2);
                text1.setText("");
                text2.setText("Are you sure you want to go back home?");
                Button yes = myDialog.findViewById(R.id.yes);
                Button no = myDialog.findViewById(R.id.no);
                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(CreateOnlineTutPhase1.this, FeedsDashboard.class);
                        i.putExtra("email", got_email);
                        i.putExtra("sent from", "online_tutorial");
                        startActivity(i);
                    }
                });

                no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        myDialog.dismiss();
                    }
                });
                myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                myDialog.setCanceledOnTouchOutside(true);
                myDialog.show();
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

        video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = "video";

                Intent i = new Intent(CreateOnlineTutPhase1.this, VideoLinks.class);
                i.putExtra("name", name);
                i.putExtra("email", got_email);
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
//        quiz.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                name = "quiz";
//            }
//        });
    }

    @Override
    public void onBackPressed() {
        //do nothing
    }
}
