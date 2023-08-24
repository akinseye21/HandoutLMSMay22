package com.example.handoutlms.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.handoutlms.R;

public class CreateOnlineTutPhase1 extends AppCompatActivity {

    LinearLayout video, pdf;
    String name, group_name, notification;
    TextView grpNAME;
    LinearLayout view_resources;
    RelativeLayout relBack;
    Dialog myDialog;
    SharedPreferences preferences;
    String got_email;
    String got_name, got_category, got_description, got_mode, got_id, got_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_online_tut_phase1);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        preferences = getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        got_email = preferences.getString("email", "not available");

        video = findViewById(R.id.video);
        pdf = findViewById(R.id.pdf);
        grpNAME = findViewById(R.id.groupNAME);
        view_resources = findViewById(R.id.view_resources);
        relBack = findViewById(R.id.rel1);

        Intent i = getIntent();
        group_name = i.getStringExtra("Group_name");
        got_name = i.getStringExtra("name");
        got_category = i.getStringExtra("category");
        got_description = i.getStringExtra("description");
        got_mode = i.getStringExtra("mode");
        got_id = i.getStringExtra("id");
        got_date = i.getStringExtra("date");


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
                i.putExtra("category", got_category);
                i.putExtra("description", got_description);
                i.putExtra("mode", got_mode);
                i.putExtra("id", got_id);
                i.putExtra("date", got_date);
                startActivity(i);
            }
        });
        pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = "pdf";

                Intent i = new Intent(CreateOnlineTutPhase1.this, DocumentPaged.class);
                i.putExtra("name", name);
                i.putExtra("email", got_email);
                i.putExtra("group_name", group_name);
                i.putExtra("category", got_category);
                i.putExtra("description", got_description);
                i.putExtra("mode", got_mode);
                i.putExtra("id", got_id);
                i.putExtra("date", got_date);
                startActivity(i);
            }
        });
    }

    @Override
    public void onBackPressed() {
        //do nothing
    }

    @Override
    protected void onStart() {
        super.onStart();

    }
}
