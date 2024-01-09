package com.example.handoutlms.uniabuja;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.handoutlms.R;
import com.example.handoutlms.activities.CreateTutorialGroupOnline;
import com.example.handoutlms.activities.PastQuestion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class Page1 extends AppCompatActivity {

    SharedPreferences preferences;
    String fullname, pics;
    Button continu;
    ImageView back;
    TextView txtfullname;
    CircleImageView pp;
    String uniname;

    RadioButton radio100, radio200, radio300, radio400, radio500, radio600;
    String level = "", selectedFaculty = "", selectedDepartment = "";
    Spinner spinnerfaculty, spinnerdepartment;
    private ArrayList<String> arr_faculty = new ArrayList<>();
    private ArrayList<String> arr_department = new ArrayList<>();

    public static final String GET_FACULTY = "https://handoutng.com/handouts/handout_get_faculty";
    public static final String GET_DEPARTMENT = "https://handoutng.com/handouts/handout_get_departments";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page1);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        preferences = getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        fullname = preferences.getString("fullname", "");
        pics = preferences.getString("pics", "");

        Intent i = getIntent();
        uniname = i.getStringExtra("uniname");

        txtfullname = findViewById(R.id.fullname);
        txtfullname.setText("Hi, "+fullname);
        pp = findViewById(R.id.pp);
        Glide.with(Page1.this).load(pics).into(pp);
        radio100 = findViewById(R.id.radio100);
        radio200 = findViewById(R.id.radio200);
        radio300 = findViewById(R.id.radio300);
        radio400 = findViewById(R.id.radio400);
        radio500 = findViewById(R.id.radio500);
        radio600 = findViewById(R.id.radio600);
        spinnerfaculty = findViewById(R.id.spinnerfaculty);
        spinnerdepartment = findViewById(R.id.spinnerdepartment);

        getfaculty();

        radio100.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radio100.setChecked(true);
                radio200.setChecked(false);
                radio300.setChecked(false);
                radio400.setChecked(false);
                radio500.setChecked(false);
                radio600.setChecked(false);
                level = "100";
            }
        });
        radio200.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radio100.setChecked(false);
                radio200.setChecked(true);
                radio300.setChecked(false);
                radio400.setChecked(false);
                radio500.setChecked(false);
                radio600.setChecked(false);
                level = "200";
            }
        });
        radio300.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radio100.setChecked(false);
                radio200.setChecked(false);
                radio300.setChecked(true);
                radio400.setChecked(false);
                radio500.setChecked(false);
                radio600.setChecked(false);
                level = "300";
            }
        });
        radio400.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radio100.setChecked(false);
                radio200.setChecked(false);
                radio300.setChecked(false);
                radio400.setChecked(true);
                radio500.setChecked(false);
                radio600.setChecked(false);
                level = "400";
            }
        });
        radio500.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radio100.setChecked(false);
                radio200.setChecked(false);
                radio300.setChecked(false);
                radio400.setChecked(false);
                radio500.setChecked(true);
                radio600.setChecked(false);
                level = "500";
            }
        });
        radio600.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radio100.setChecked(false);
                radio200.setChecked(false);
                radio300.setChecked(false);
                radio400.setChecked(false);
                radio500.setChecked(false);
                radio600.setChecked(true);
                level = "600";
            }
        });

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Page1.this, GetStarted.class);
                i.putExtra("uniname", uniname);
                startActivity(i);
            }
        });
        continu = findViewById(R.id.continu);
        continu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!level.equals("") && !selectedFaculty.equals("") && !selectedDepartment.equals("")) {
                    Intent i = new Intent(Page1.this, Page2.class);
                    i.putExtra("level", level);
                    i.putExtra("faculty", selectedFaculty);
                    i.putExtra("department", selectedDepartment);
                    i.putExtra("uniname", uniname);
                    startActivity(i);
                }else{
                    Toast.makeText(Page1.this, "Please select appropriate values", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void getfaculty() {
        //GET UNIVERSITY
        StringRequest stringRequest2 = new StringRequest(Request.Method.GET, GET_FACULTY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        arr_faculty.add("Select a faculty");
                        arr_faculty.add("");
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            int ArrayLength = jsonArray.length();
                            for(int i=0; i<ArrayLength; i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String faculty = jsonObject.getString("faculty");

                                arr_faculty.add(faculty);
                            }

                            String[] fac = new String[arr_faculty.size()];
                            for (int i = 0; i < arr_faculty.size(); i++) {
                                fac[i] = arr_faculty.get(i);
                            }

                            ArrayAdapter<String> facultyadapter = new ArrayAdapter<>(Page1.this, R.layout.sim_spinner, R.id.tx, fac);
                            spinnerfaculty.setAdapter(facultyadapter);

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
        requestQueue2.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
            @Override
            public void onRequestFinished(Request<Object> request) {
                requestQueue2.getCache().clear();
            }
        });

        spinnerfaculty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                arr_department.clear();
                selectedFaculty = (String) spinnerfaculty.getSelectedItem();
                if (!selectedFaculty.equals("") || !selectedFaculty.equals("Select a faculty")){
                    getdepartments();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void getdepartments() {
        //GET DEPARTMENT
        StringRequest stringRequest = new StringRequest(Request.Method.POST, GET_DEPARTMENT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            int ArrayLength = jsonArray.length();
                            for(int i=0; i<ArrayLength; i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String department = jsonObject.getString("department");

                                arr_department.add(department);
                            }

                            String[] dept = new String[arr_department.size()];
                            for (int i = 0; i < arr_department.size(); i++) {
                                dept[i] = arr_department.get(i);
                            }

                            ArrayAdapter<String> departmentadapter = new ArrayAdapter<>(Page1.this, R.layout.sim_spinner, R.id.tx, dept);
                            spinnerdepartment.setAdapter(departmentadapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                    }
                }){
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json");
                params.put("faculty", selectedFaculty);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        DefaultRetryPolicy retryPolicy = new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);
        requestQueue.add(stringRequest);
        requestQueue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
            @Override
            public void onRequestFinished(Request<Object> request) {
                requestQueue.getCache().clear();
            }
        });

        spinnerdepartment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedDepartment = (String) spinnerdepartment.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
}