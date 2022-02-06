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

import java.util.ArrayList;

public class CreateGig3 extends AppCompatActivity {

    CardView fixed, perhour;
    Button next;
    String fixed_update="unselected";
    String perhour_update = "unselected";
    ImageView back;
    Spinner budget_fixed, budget_hour;
    String selected_budget_category = "nill";

    String projectName, projectDescription, paymentMode;
    ArrayList<String> Array_requiredSkills;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_gig3);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Intent i = getIntent();
        projectName = i.getStringExtra("Project name");
        projectDescription = i.getStringExtra("Project description");
        Array_requiredSkills  = i.getExtras().getStringArrayList("Required skills");

        final String[] budget_category_fixed = {"Select a category...",
                "",
                "Micro Project (N5,000 - 16,500 Naira)",
                "Simple project (N16,500 - 137,500 Naira)",
                "Very small project (N137,500  - 412,500 Naira)",
                "Small project (N412,500  - 825,000 Naira)",
                "Medium project (N825,000 - 1,650,000 Naira)",
                "Large project (N1,650,000 - 2,750,000 Naira)",
                "Larger project (N2,750,000 - 5,500,000 Naira)",
                "Very Large project (N5,500,000 - 11,000,000 Naira)",
                "Huge project (N11,000,000  - 27,500,000 Naira)",
                "Major project (N27,500,000)",
                "Customize budget"
        };

        final String[] budget_category_hour = {"Select a category...",
                "",
                "Basic (N1,000  - 4,000 per hour)",
                "Moderate (N4,000 - 8,000 per hour)",
                "Standard (N8,000  - 16,000 per hour)",
                "Skilled (N16,000 - 32,000 per hour)",
                "Expert (N32,000+ per hour)",
                "Customize budget"
        };


        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), CreateGig2.class);
                i.putExtra("Project name", projectName);
                i.putExtra("Project description", projectDescription);
                i.putStringArrayListExtra("Required skills", Array_requiredSkills);
                i.putExtra("Payment mode", paymentMode);
                startActivity(i);
            }
        });

        budget_hour = findViewById(R.id.spinner_budget_hour);
        budget_fixed = findViewById(R.id.spinner_budget_fixed);

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

                    paymentMode = "fixed";
                    budget_fixed.setVisibility(View.VISIBLE);
                    budget_hour.setVisibility(View.GONE);

                    //set Adapter
                    ArrayAdapter<String> budgetadapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.simple_gig_spinner, R.id.my_tx, budget_category_fixed) ;
                    budget_fixed.setAdapter(budgetadapter);
                    budget_fixed.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if (position == 0 || position == 1){
                                selected_budget_category = "nill";
                            }else{
                                selected_budget_category = budget_fixed.getSelectedItem().toString();
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
                else if (fixed_update.equals("selected")){
                    fixed.setCardBackgroundColor(Color.TRANSPARENT);
                    perhour.setCardBackgroundColor(Color.parseColor("#2800ff00"));
                    fixed_update = "unselected";
                    perhour_update = "selected";

                    paymentMode = "hourly";
                    budget_hour.setVisibility(View.VISIBLE);
                    budget_fixed.setVisibility(View.GONE);

                    //set Adapter
                    ArrayAdapter<String> budgetadapter2 = new ArrayAdapter<String>(getApplicationContext(), R.layout.simple_gig_spinner, R.id.my_tx, budget_category_hour) ;
                    budget_hour.setAdapter(budgetadapter2);
                    budget_hour.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if (position == 0 || position == 1){
                                selected_budget_category = "nill";
                            }else{
                                selected_budget_category = budget_hour.getSelectedItem().toString();
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
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

                    paymentMode = "hourly";
                    budget_hour.setVisibility(View.VISIBLE);
                    budget_fixed.setVisibility(View.GONE);

                    //set Adapter
                    ArrayAdapter<String> budgetadapter2 = new ArrayAdapter<String>(getApplicationContext(), R.layout.simple_gig_spinner, R.id.my_tx, budget_category_hour) ;
                    budget_hour.setAdapter(budgetadapter2);
                    budget_hour.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if (position == 0 || position == 1){
                                selected_budget_category = "nill";
                            }else{
                                selected_budget_category = budget_hour.getSelectedItem().toString();
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
                else if (perhour_update.equals("selected")){
                    perhour.setCardBackgroundColor(Color.TRANSPARENT);
                    fixed.setCardBackgroundColor(Color.parseColor("#2800ff00"));
                    perhour_update = "unselected";
                    fixed_update = "selected";

                    paymentMode = "fixed";
                    budget_fixed.setVisibility(View.VISIBLE);
                    budget_hour.setVisibility(View.GONE);

                    //set Adapter
                    ArrayAdapter<String> budgetadapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.simple_gig_spinner, R.id.my_tx, budget_category_fixed) ;
                    budget_fixed.setAdapter(budgetadapter);
                    budget_fixed.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if (position == 0 || position == 1){
                                selected_budget_category = "nill";
                            }else{
                                selected_budget_category = budget_fixed.getSelectedItem().toString();
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
            }
        });

        next = findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selected_budget_category != "nill" && fixed_update.equals("selected") || perhour_update.equals("selected")){
                    Intent i = new Intent(getApplicationContext(), CreateGig4.class);
                    i.putExtra("Project name", projectName);
                    i.putExtra("Project description", projectDescription);
                    i.putStringArrayListExtra("Required skills", Array_requiredSkills);
                    i.putExtra("Payment mode", paymentMode);
                    i.putExtra("Budget category", selected_budget_category );
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
