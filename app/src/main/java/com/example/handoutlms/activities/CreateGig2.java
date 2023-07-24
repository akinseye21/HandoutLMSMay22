package com.example.handoutlms.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.handoutlms.R;

import java.util.ArrayList;

public class CreateGig2 extends AppCompatActivity {

    String status = "unselected";

    CardView mcv;
    LinearLayout next;
    ImageView back;

    String projectName, projectDescription;
    ArrayList<String> Array_requiredSkills;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_gig2);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Intent i = getIntent();
        projectName = i.getStringExtra("Project name");
        projectDescription = i.getStringExtra("Project description");
        Array_requiredSkills  = i.getExtras().getStringArrayList("Required skills");

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mcv = findViewById(R.id.mcv);
        mcv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(status.equals("unselected")){
                    mcv.setCardBackgroundColor(Color.parseColor("#2800ff00"));
                    status = "selected";
                }
                else if (status.equals("selected")){
                    mcv.setCardBackgroundColor(Color.TRANSPARENT);
                    status = "unselected";
                }
            }
        });

        next = findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CreateGig2.this, CreateGig3.class);
                i.putExtra("Project name", projectName);
                i.putExtra("Project description", projectDescription);
                i.putStringArrayListExtra("Required skills", Array_requiredSkills);
                startActivity(i);
            }
        });

    }

//    @Override
//    public void onBackPressed() {
//        // do nothing
//    }
}
