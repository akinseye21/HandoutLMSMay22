package com.example.handoutlms;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class CreateGig3 extends AppCompatActivity {

    CardView fixed, perhour;
    LinearLayout next;
    String fixed_update="unselected";
    String perhour_update = "unselected";
    ImageView back;
    Spinner budget_fixed, budget_hour;
    String selected_budget_category = "nill";
    EditText customAmount, startdate, enddate;
    DatePickerDialog datePickerDialog;

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
                onBackPressed();
            }
        });

        budget_hour = findViewById(R.id.spinner_budget_hour);
        budget_fixed = findViewById(R.id.spinner_budget_fixed);
        customAmount = findViewById(R.id.customAmount);
        startdate = findViewById(R.id.startdate);
        enddate = findViewById(R.id.enddate);

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
                    ArrayAdapter<String> budgetadapter = new ArrayAdapter<String>(CreateGig3.this, R.layout.simple_gig_spinner, R.id.my_tx, budget_category_fixed) ;
                    budget_fixed.setAdapter(budgetadapter);
                    budget_fixed.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if (position == 0 || position == 1){
                                selected_budget_category = "nill";
                                customAmount.setVisibility(View.GONE);
                            }else if (budget_fixed.getSelectedItem().toString().equals("Customize budget")){
                                customAmount.setVisibility(View.VISIBLE);
                            }else{
                                customAmount.setVisibility(View.GONE);
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
                                customAmount.setVisibility(View.GONE);
                                selected_budget_category = "nill";
                            }else if (budget_hour.getSelectedItem().toString().equals("Customize budget")){
                                customAmount.setVisibility(View.VISIBLE);
                            }
                            else{
                                customAmount.setVisibility(View.GONE);
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
                    ArrayAdapter<String> budgetadapter2 = new ArrayAdapter<String>(CreateGig3.this, R.layout.simple_gig_spinner, R.id.my_tx, budget_category_hour) ;
                    budget_hour.setAdapter(budgetadapter2);
                    budget_hour.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if (position == 0 || position == 1){
                                customAmount.setVisibility(View.GONE);
                                selected_budget_category = "nill";
                            }else if (budget_hour.getSelectedItem().toString().equals("Customize budget")){
                                customAmount.setVisibility(View.VISIBLE);
                            }
                            else{
                                customAmount.setVisibility(View.GONE);
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
                    ArrayAdapter<String> budgetadapter = new ArrayAdapter<String>(CreateGig3.this, R.layout.simple_gig_spinner, R.id.my_tx, budget_category_fixed) ;
                    budget_fixed.setAdapter(budgetadapter);
                    budget_fixed.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if (position == 0 || position == 1){
                                customAmount.setVisibility(View.GONE);
                                selected_budget_category = "nill";
                            }else if (budget_fixed.getSelectedItem().toString().equals("Customize budget")){
                                customAmount.setVisibility(View.VISIBLE);
                            }
                            else{
                                customAmount.setVisibility(View.GONE);
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

        startdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // calender class's instance and get current date , month and year from calender
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day

                // date picker dialog
                datePickerDialog = new DatePickerDialog(CreateGig3.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                monthOfYear+=1;
                                // set day of month , month and year value in the edit text
                                String mt;
                                if(monthOfYear<10){
                                    mt = "0"+monthOfYear;
                                }
                                else mt = String.valueOf(monthOfYear);
                                String dy;
                                if(dayOfMonth<10)
                                    dy = "0"+dayOfMonth;
                                else dy = String.valueOf(dayOfMonth);

//                                dte.setText(dy + "/" + mt + "/" + year);
                                startdate.setText(year + "-" + mt + "-" + dy);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
            }
        });
        enddate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // calender class's instance and get current date , month and year from calender
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day

                // date picker dialog
                datePickerDialog = new DatePickerDialog(CreateGig3.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                monthOfYear+=1;
                                // set day of month , month and year value in the edit text
                                String mt;
                                if(monthOfYear<10){
                                    mt = "0"+monthOfYear;
                                }
                                else mt = String.valueOf(monthOfYear);
                                String dy;
                                if(dayOfMonth<10)
                                    dy = "0"+dayOfMonth;
                                else dy = String.valueOf(dayOfMonth);

//                                dte.setText(dy + "/" + mt + "/" + year);
                                enddate.setText(year + "-" + mt + "-" + dy);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
            }
        });

        next = findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check if customAmount is empty
                if (customAmount.getText().toString().equals("")){
                    //the amount is not custom
                    selected_budget_category = selected_budget_category;
                }else{
                    //the amount is custom
                    selected_budget_category = "Custom Amount - N"+customAmount.getText().toString();
                }

                if( (!selected_budget_category.equals("nill") && fixed_update.equals("selected")    ||
                        !selected_budget_category.equals("nill") && perhour_update.equals("selected")) &&
                        !startdate.getText().toString().equals("") && !enddate.getText().toString().equals("")
                ){
                    Intent i = new Intent(CreateGig3.this, CreateGig4.class);
                    i.putExtra("Project name", projectName);
                    i.putExtra("Project description", projectDescription);
                    i.putStringArrayListExtra("Required skills", Array_requiredSkills);
                    i.putExtra("Payment mode", paymentMode);
                    i.putExtra("Budget category", selected_budget_category );
                    i.putExtra("Start date", startdate.getText().toString() );
                    i.putExtra("End date", enddate.getText().toString() );
                    startActivity(i);
                }
                else if (startdate.getText().toString().equals("")){
                    startdate.setError("Select a start date");
                }else if (enddate.getText().toString().equals("")){
                    enddate.setError("Select a end date");
                }else{
                    Toast.makeText(CreateGig3.this, "Please select a card and a budget category", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

//    @Override
//    public void onBackPressed() {
//        // do nothing
//    }
}
