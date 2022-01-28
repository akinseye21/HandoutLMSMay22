package com.example.handoutlms;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

public class CreateGig3 extends AppCompatActivity {

    CardView fixed, perhour;
    Button next;
    String fixed_update="unselected";
    String perhour_update = "unselected";
    ImageView back;
    Spinner budget;
    String selected_budget_category = "nill";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_gig3);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        String[] budget_category = {"Select a category...",
                "",
                "Very small project (N1,000 - N9,999)",
                "Medium cost project (N10,000 - N99,999)",
                "Large cost project (N100,000 - Above)"};
        budget = findViewById(R.id.spinner_budget);
        ArrayAdapter<String> budgetadapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.simple_gig_spinner, R.id.my_tx, budget_category) ;
        budget.setAdapter(budgetadapter);
        budget.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0 || position == 1){
                    selected_budget_category = "nill";
                }else{
                    selected_budget_category = budget.getSelectedItem().toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), CreateGig2.class);
                startActivity(i);
            }
        });

        fixed = findViewById(R.id.fixed);
        perhour = findViewById(R.id.perhour);

        fixed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fixed_update.equals("unselected")){
                    fixed.setCardBackgroundColor(Color.parseColor("#2800ff00"));
                    perhour.setCardBackgroundColor(Color.TRANSPARENT);
                    fixed_update = "selected";
                    perhour_update = "unselected";
                }
                else if (fixed_update.equals("selected")){
                    fixed.setCardBackgroundColor(Color.TRANSPARENT);
                    perhour.setCardBackgroundColor(Color.parseColor("#2800ff00"));
                    fixed_update = "unselected";
                    perhour_update = "selected";
                }
            }
        });

        perhour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(perhour_update.equals("unselected")){
                    perhour.setCardBackgroundColor(Color.parseColor("#2800ff00"));
                    fixed.setCardBackgroundColor(Color.TRANSPARENT);
                    perhour_update = "selected";
                    fixed_update = "unselected";
                }
                else if (perhour_update.equals("selected")){
                    perhour.setCardBackgroundColor(Color.TRANSPARENT);
                    fixed.setCardBackgroundColor(Color.parseColor("#2800ff00"));
                    perhour_update = "unselected";
                    fixed_update = "selected";
                }
            }
        });

        next = findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selected_budget_category != "nill" && fixed_update.equals("selected") || perhour_update.equals("selected")){
                    Intent i = new Intent(getApplicationContext(), CreateGig4.class);
                    i.putExtra("selected_category", selected_budget_category );
                    startActivity(i);
                } else{
                    Toast.makeText(getApplicationContext(), "Please select a card and a budget category", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        // do nothing
    }
}
