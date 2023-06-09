package com.example.handoutlms;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
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

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

public class CreateTutorialGroupOnline extends AppCompatActivity {

    LinearLayout next;
    EditText dte, grp_name, descrip;
    EditText tim;
    DatePickerDialog datePickerDialog;
    TimePickerDialog picker;
    Spinner uni, cat;
    String group_name, date, time, description, email;
    ImageView back;
    String hour_d, minute_d;


    private ArrayList<String> cat2 = new ArrayList<>();
    private ArrayList<String> inst2 = new ArrayList<>();


    public static final String GET_CATEGORIES = "https://handoutng.com/handouts/handout_get_tutorial_groups_categories";
    public static final String GET_UNIVERSITY = "https://handoutng.com/handouts/handout_get_universities";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_tutorial_group_online);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Intent j = getIntent();
        email = j.getStringExtra("email");

//        String sub1 = "Select Subject";
//        SpannableString spannableString = new SpannableString(sub1);
//        ForegroundColorSpan gray = new ForegroundColorSpan(Color.GRAY);
//        spannableString.setSpan(gray, 0, 14, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        //GET CATEGORIES
        StringRequest stringRequest = new StringRequest(Request.Method.GET, GET_CATEGORIES,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        cat2.add("Select Subject");
                        cat2.add("");

                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            int ArrayLength = jsonArray.length();

                            for(int i=0; i<ArrayLength; i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String mycategory = jsonObject.getString("category");

                                cat2.add(mycategory);
                            }


                            String[] category = new String[cat2.size()];
                            for (int i = 0; i < cat2.size(); i++) {
                                category[i] = cat2.get(i);
                            }

                            ArrayAdapter<String> categoryadapter = new ArrayAdapter<>(getApplicationContext(), R.layout.simple_spinner_small_whitebg2, R.id.tx, category);
                            cat.setAdapter(categoryadapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }


                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        DefaultRetryPolicy retryPolicy = new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);
        requestQueue.add(stringRequest);


        //GET UNIVERSITY
        StringRequest stringRequest2 = new StringRequest(Request.Method.GET, GET_UNIVERSITY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        inst2.add("Select University");
                        inst2.add("");

                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            int ArrayLength = jsonArray.length();
                            for(int i=0; i<ArrayLength; i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String university = jsonObject.getString("university");

                                inst2.add(university);
                            }

                            String[] institutions = new String[inst2.size()];
                            for (int i = 0; i < inst2.size(); i++) {
                                institutions[i] = inst2.get(i);
                            }

                            ArrayAdapter<String> institutionadapter = new ArrayAdapter<>(CreateTutorialGroupOnline.this, R.layout.simple_spinner_small_whitebg2, R.id.tx, institutions);
                            uni.setAdapter(institutionadapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }


                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        RequestQueue requestQueue2 = Volley.newRequestQueue(getApplicationContext());
        DefaultRetryPolicy retryPolicy2 = new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest2.setRetryPolicy(retryPolicy2);
        requestQueue2.add(stringRequest2);

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

//                                dte.setText(dy + "/" + mt + "/" + year);
                                dte.setText(year + "-" + mt + "-" + dy);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
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
                                if(sHour<10){
                                    hour_d = "0"+String.valueOf(sHour);
                                }else{
                                    hour_d = String.valueOf(sHour);
                                }
                                if(sMinute<10){
                                    minute_d = "0"+String.valueOf(sMinute);
                                }else{
                                    minute_d = String.valueOf(sMinute);
                                }

//                                tim.setText(sHour + ":" + sMinute);
                                tim.setText(hour_d+":"+minute_d);
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

                if(group_name.equals("")){
                    grp_name.setError("Groupname is empty");
                } else if(date.equals("")){
                    dte.setError("Date is empty");
                } else if(time.equals("")){
                    tim.setError("Time is empty");
                } else if(description.equals("")){
                    descrip.setError("Description is empty");
                }
//                if(group_name.equals("") || date.equals("") || time.equals("") || description.equals("") || cat.getSelectedItem().toString().equals("") ||
//                uni.getSelectedItem().toString().equals("")){
//                    Toast.makeText(getApplicationContext(), "One or more field is empty", Toast.LENGTH_LONG).show();
//                }
                else{
                    Intent i = new Intent(CreateTutorialGroupOnline.this, AlmostDoneOnline.class);
                    i.putExtra("group_name", group_name);
                    i.putExtra("category", cat.getSelectedItem().toString());
                    i.putExtra("date", date);
                    i.putExtra("time", time);
                    i.putExtra("university", uni.getSelectedItem().toString());
                    i.putExtra("description", description);
                    i.putExtra("email", email);
                    startActivity(i);
                }
            }
        });

    }
}
