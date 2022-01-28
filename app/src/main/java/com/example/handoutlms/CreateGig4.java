package com.example.handoutlms;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class CreateGig4 extends AppCompatActivity {

    CardView standard, agent;
    Button next;
    String standard_update="unselected";
    String agent_update = "unselected";
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_gig4);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), CreateGig3.class);
                startActivity(i);
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
                }
                else if (standard_update.equals("selected")){
                    standard.setCardBackgroundColor(Color.TRANSPARENT);
                    agent.setCardBackgroundColor(Color.parseColor("#2800ff00"));
                    standard_update = "unselected";
                    agent_update = "selected";
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
                }
                else if (agent_update.equals("selected")){
                    agent.setCardBackgroundColor(Color.TRANSPARENT);
                    standard.setCardBackgroundColor(Color.parseColor("#2800ff00"));
                    agent_update = "unselected";
                    standard_update = "selected";
                }
            }
        });

        next = findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(agent_update.equals("selected") && standard_update.equals("unselected")){
                    Intent i = new Intent(getApplicationContext(), CreateGig6.class);
                    startActivity(i);
                } else if(agent_update.equals("unselected") && standard_update.equals("selected")){
                    Intent i = new Intent(getApplicationContext(), CreateGig5.class);
                    startActivity(i);
                } else{
                    Toast.makeText(getApplicationContext(), "Kindly select one card", Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    @Override
    public void onBackPressed() {
        // do nothing
    }
}
