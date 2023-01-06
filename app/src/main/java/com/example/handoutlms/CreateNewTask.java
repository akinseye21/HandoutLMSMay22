package com.example.handoutlms;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class CreateNewTask extends AppCompatActivity {

    ImageView back;
    Spinner tasks, team;
    String[] myTasks = {"Choose a task", "", "Tutorial", "Gig", "Games", "Exam", "Test", "Assignment"};
    String[] myTeam = {"Choose who's participating", "", "School", "Friends", "Family"};
    EditText title, dte, tme, description;
    Button createTask;
    DatePickerDialog datePickerDialog;
    TimePickerDialog picker;
    String task_title, task_category,task_date, task_time, task_description;
    ProgressBar progressBar;
    Dialog myDialog;
    String email;

    Button home;
    TextView stat;

    public static final String TASK_MANAGER = "https://handoutng.com/handouts/handout_task_manager";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_task);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Intent i = getIntent();
        email = i.getStringExtra("email");

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        title = findViewById(R.id.title);
        dte = findViewById(R.id.date);
        tme = findViewById(R.id.startTime);
        description = findViewById(R.id.description);
        progressBar = findViewById(R.id.progressBar);

        tasks = findViewById(R.id.spinnertask);
        ArrayAdapter<String> taskAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.simple_spinner_small_whitebg, R.id.tx, myTasks);
        tasks.setAdapter(taskAdapter);
        tasks.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                ((LinearLayout)view).setTextColor(Color.WHITE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        team = findViewById(R.id.spinnerteam);
        ArrayAdapter<String> teamAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.simple_spinner_small_whitebg, R.id.tx, myTeam);
        team.setAdapter(teamAdapter);
        team.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                ((TextView)view).setTextColor(Color.WHITE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        dte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // calender class's instance and get current date , month and year from calender
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                datePickerDialog = new DatePickerDialog(CreateNewTask.this,
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
        tme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int hour = cldr.get(Calendar.HOUR_OF_DAY);
                int minutes = cldr.get(Calendar.MINUTE);
                // time picker dialog
                picker = new TimePickerDialog(CreateNewTask.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                                tme.setText(sHour + ":" + sMinute);
                            }
                        }, hour, minutes, true);
                picker.show();
            }
        });

        createTask = findViewById(R.id.createTask);
        createTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                //get all values in Edit Test
                task_title = title.getText().toString();
                task_category = tasks.getSelectedItem().toString();
                task_date = dte.getText().toString();
                task_time = tme.getText().toString();
                task_description = description.getText().toString();
                //check for empty fields
                if(task_title.isEmpty() || task_category.isEmpty() || task_date.isEmpty()
                || task_time.isEmpty() || task_description.isEmpty()){
                    progressBar.setVisibility(View.GONE);
                    title.setError("Check inputs");
                    dte.setError("Check inputs");
                    tme.setError("Check inputs");
                    description.setError("Check inputs");
                }else{
                    //send the values to the endpoint.
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, TASK_MANAGER,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    System.out.println("Response = "+response);

                                    progressBar.setVisibility(View.GONE);

                                    JSONObject jo = null;
                                    try {
                                        jo = new JSONObject(response);
                                        String status = jo.getString("status");
                                        String notification = jo.getString("notification");

                                        if (status.equals("successful")){
                                            //load the custom dialog box
                                            myDialog = new Dialog(CreateNewTask.this);
                                            myDialog.setContentView(R.layout.custom_popup_successful_taskmanager);
                                            home = myDialog.findViewById(R.id.home);
                                            stat = myDialog.findViewById(R.id.status);
                                            stat.setText(notification);
                                            home.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    Intent i = new Intent(getApplicationContext(), FeedsDashboard.class);
                                                    startActivity(i);
                                                }
                                            });
                                            myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                            myDialog.setCanceledOnTouchOutside(false);
                                            myDialog.show();
                                        }
                                    } catch (JSONException e) {
                                        progressBar.setVisibility(View.GONE);
                                        e.printStackTrace();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError volleyError) {
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(getApplicationContext(), "Error!", Toast.LENGTH_LONG).show();
                                    volleyError.printStackTrace();
                                }
                            }){
                        @Override
                        protected Map<String, String> getParams(){
                            Map<String, String> params = new HashMap<>();
                            params.put("email", email);
                            params.put("task_name", task_title);
                            params.put("short_description", task_description);
                            params.put("task_category", task_category);
                            params.put("task_date", task_date);
                            params.put("task_time", task_time);
                            return params;
                        }
                    };

                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                    requestQueue.add(stringRequest);
                }

            }
        });


    }
}