package com.example.handoutlms;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class CreateTutorialGroupOnline extends AppCompatActivity {

    LinearLayout next;
    EditText dte, grp_name, descrip;
    EditText tim;
    DatePickerDialog datePickerDialog;
    TimePickerDialog picker;
    Spinner uni, cat;
    String group_name, category, date, time, university, description, email;
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_tutorial_group_online);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Intent j = getIntent();
        email = j.getStringExtra("email");

        String[] institutions = {"Select University..."," ","Ahmadu Bello University Zaria", "Adekunle Ajasin Unversity Akungba", "Adeleke University",
                "Babcock University", "Federal University of Tech. Minna", "Federal University of Tech. Akure", "University of Lagos", "University of Abuja",
                "Obafemi Awolowo University Ife"};
        final String[] category = {"Select Category..."," ","Architecture","Biology","Information Technology","Politics", "Science", "Social Sciences", "Zoology"};

        ArrayAdapter<String> institutionadapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.simple_spinner_small_whitebg, R.id.tx, institutions) ;
        ArrayAdapter<String> categoryadapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.simple_spinner_small_whitebg, R.id.tx, category) ;


        grp_name = findViewById(R.id.group_name);
        descrip = findViewById(R.id.description);
        uni = findViewById(R.id.spinneruni);
        cat = findViewById(R.id.spinnercategory);
        dte = findViewById(R.id.mydate);
        tim = findViewById(R.id.time);
        next = findViewById(R.id.next);
        back = findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        uni.setAdapter(institutionadapter);
        cat.setAdapter(categoryadapter);


        dte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // calender class's instance and get current date , month and year from calender
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day

                // date picker dialog
                datePickerDialog = new DatePickerDialog(CreateTutorialGroupOnline.this,
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

                                dte.setText(dy + "/"
                                        + mt + "/" + year);



                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
        tim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int hour = cldr.get(Calendar.HOUR_OF_DAY);
                int minutes = cldr.get(Calendar.MINUTE);
                // time picker dialog
                picker = new TimePickerDialog(CreateTutorialGroupOnline.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                                tim.setText(sHour + ":" + sMinute);
                            }
                        }, hour, minutes, true);
                picker.show();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                group_name = grp_name.getText().toString();
                date = dte.getText().toString();
                time = tim.getText().toString();
                description = descrip.getText().toString();

                if(group_name.equals("") || date.equals("") || time.equals("") || description.equals("") || cat.getSelectedItem().toString().equals("") ||
                uni.getSelectedItem().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "One or more field is empty", Toast.LENGTH_LONG).show();
                }
                else{
                    Intent i = new Intent(CreateTutorialGroupOnline.this, InviteFriends.class);
                    i.putExtra("group_name", group_name);
                    i.putExtra("category", cat.getSelectedItem().toString());
                    i.putExtra("date", date);
                    i.putExtra("time", time);
                    i.putExtra("university", uni.getSelectedItem().toString());
                    i.putExtra("description", description);
                    i.putExtra("location", "");
                    i.putExtra("email", email);
                    startActivity(i);
                }


            }
        });
    }
}
