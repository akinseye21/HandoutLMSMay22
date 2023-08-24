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
import android.widget.Toast;

import com.example.handoutlms.R;

import java.util.ArrayList;

public class CreateGig4 extends AppCompatActivity {

    CardView standard, agent;
    LinearLayout next;
    String standard_update="unselected";
    String agent_update = "unselected";
    ImageView back;

    String startDate, endDate;
    String projectName, projectDescription, paymentMode, budgetCategory, projectType = "";
    ArrayList<String> Array_requiredSkills;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_gig4);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Intent i = getIntent();
        projectName = i.getStringExtra("Project name");
        projectDescription = i.getStringExtra("Project description");
        Array_requiredSkills  = i.getExtras().getStringArrayList("Required skills");
        paymentMode = i.getStringExtra("Payment mode");
        budgetCategory = i.getStringExtra("Budget category");
        startDate = i.getStringExtra("Start date");
        endDate = i.getStringExtra("End date");

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        standard = findViewById(R.id.standard);
        agent = findViewById(R.id.agent);

        standard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(standard_update.equals("unselected")){
                    standard.setCardBackgroundColor(Color.parseColor("#2800ff00"));
                    agent.setCardBackgroundColor(Color.TRANSPARENT);
                    standard_update = "selected";
                    agent_update = "unselected";

                    projectType = "standard";
                }
                else if (standard_update.equals("selected")){
                    standard.setCardBackgroundColor(Color.TRANSPARENT);
                    agent.setCardBackgroundColor(Color.parseColor("#2800ff00"));
                    standard_update = "unselected";
                    agent_update = "selected";

                    projectType = "agent";
                }
            }
        });

        agent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(agent_update.equals("unselected")){
                    agent.setCardBackgroundColor(Color.parseColor("#2800ff00"));
                    standard.setCardBackgroundColor(Color.TRANSPARENT);
                    agent_update = "selected";
                    standard_update = "unselected";

                    projectType = "agent";
                }
                else if (agent_update.equals("selected")){
                    agent.setCardBackgroundColor(Color.TRANSPARENT);
                    standard.setCardBackgroundColor(Color.parseColor("#2800ff00"));
                    agent_update = "unselected";
                    standard_update = "selected";

                    projectType = "standard";
                }
            }
        });

        next = findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(projectType.equals("agent")){
                    Intent i = new Intent(getApplicationContext(), CreateGig6.class);
                    i.putExtra("Project name", projectName);
                    i.putExtra("Project description", projectDescription);
                    i.putStringArrayListExtra("Required skills", Array_requiredSkills);
                    i.putExtra("Payment mode", paymentMode);
                    i.putExtra("Budget category", budgetCategory );
                    i.putExtra("Project type", projectType );
                    i.putExtra("Start date", startDate );
                    i.putExtra("End date", endDate );
                    startActivity(i);
                } else if(projectType.equals("standard")){
                    Intent i = new Intent(getApplicationContext(), CreateGig5.class);
                    i.putExtra("Project name", projectName);
                    i.putExtra("Project description", projectDescription);
                    i.putStringArrayListExtra("Required skills", Array_requiredSkills);
                    i.putExtra("Payment mode", paymentMode);
                    i.putExtra("Budget category", budgetCategory );
                    i.putExtra("Project type", projectType );
                    i.putExtra("Start date", startDate );
                    i.putExtra("End date", endDate );
                    startActivity(i);
                } else{
                    Toast.makeText(getApplicationContext(), "Kindly select one card", Toast.LENGTH_LONG).show();
                }

            }
        });
    }
}
