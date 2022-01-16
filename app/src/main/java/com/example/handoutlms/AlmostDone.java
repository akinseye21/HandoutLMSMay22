package com.example.handoutlms;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AlmostDone extends AppCompatActivity {

    LinearLayout done;
    String group_name, category, date, time, university, description, tut_type, location, tutorial_mode;
    TextView gp_name, cat, dat, tim, uni, desc;
    CheckBox share;
    SharedPreferences preferences;
    ProgressBar progressBar;
    ImageView back;

    public static final String TUTORIAL_GROUP = "http://35.84.44.203/handouts/handout_tutorial_groups";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_almost_done);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //get sharedpreference
        preferences = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        final String email = preferences.getString("email", "not available");

        Intent i = getIntent();
        group_name = i.getStringExtra("group_name");
        category = i.getStringExtra("category");
        date = i.getStringExtra("date");
        time = i.getStringExtra("time");
        university = i.getStringExtra("university");
        description = i.getStringExtra("description");
        location = i.getStringExtra("location");

        if(location.equals("")){
            tutorial_mode = "online";
        }
        else if(!location.equals("")){
            tutorial_mode = "offline";
        }

        gp_name = findViewById(R.id.tutorial_group_name);
        cat = findViewById(R.id.category);
        dat = findViewById(R.id.date);
        tim = findViewById(R.id.time);
        uni = findViewById(R.id.university);
        desc = findViewById(R.id.description);
        share = findViewById(R.id.share);
        progressBar = findViewById(R.id.progressBar);
        back = findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        gp_name.setText(group_name);
        cat.setText(category);
        dat.setText(date);
        tim.setText(time);
        uni.setText(university);
        desc.setText(description);

        done = findViewById(R.id.next);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(share.isChecked()){
                    tut_type = "public";
                }else{
                    tut_type = "private";
                }


                if(!group_name.equals("") && !category.equals("") && !date.equals("") && !time.equals("") && !university.equals("") && !description.equals("")){
                    //send to server
                    progressBar.setVisibility(View.VISIBLE);

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, TUTORIAL_GROUP,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    progressBar.setVisibility(View.GONE);

                                    System.out.println("Response = "+response);
//                                    Toast.makeText(getApplicationContext(), "Response = "+response, Toast.LENGTH_LONG).show();


                                    try{
                                        JSONObject jsonObject = new JSONObject(response);

//                                        String signed_name = jsonObject.getString("fullname");
                                        String status = jsonObject.getString("status");

                                        if(status.equals("successful") && location.equals("")){
                                            Toast.makeText(getApplicationContext(), "Online Group created successfully", Toast.LENGTH_LONG).show();
                                            System.out.println(jsonObject);

                                            Intent i = new Intent(AlmostDone.this, CreateOnlineTutPhase1.class);
                                            i.putExtra("Group_name", group_name);
                                            startActivity(i);
                                        }
                                        else if(status.equals("successful") && !location.equals("")){
                                            Toast.makeText(getApplicationContext(), "Offline Group created successfully", Toast.LENGTH_LONG).show();
                                            System.out.println(jsonObject);
                                        }
                                        else{
                                            Toast.makeText(getApplicationContext(), "Group creation failed. Please try again", Toast.LENGTH_LONG).show();
                                            System.out.println("Error sending group info: "+jsonObject);
                                        }
                                    }
                                    catch (JSONException e){
                                        e.printStackTrace();
                                    }

                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError volleyError) {

                                    if(volleyError == null){
                                        return;
                                    }

                                    progressBar.setVisibility(View.GONE);
//                                    Toast.makeText(getContext(),  volleyError.getMessage(), Toast.LENGTH_LONG).show();
                                    System.out.println("Error = "+volleyError.getMessage());
                                }
                            }){
                        @Override
                        protected Map<String, String> getParams(){
                            Map<String, String> params = new HashMap<>();
                            params.put("email", email);
                            params.put("tutorial_group_name", group_name);
                            params.put("category", category);
                            params.put("tutorial_type", tut_type);
                            params.put("date", date);
                            params.put("time", time);
                            params.put("institution", university);
                            params.put("short_description", description);
                            params.put("tutorial_mode", tutorial_mode);
                            params.put("venue", location);
                            return params;
                        }
                    };

                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                    requestQueue.add(stringRequest);
                }else{
                    Toast.makeText(getApplicationContext(), "One or more field is empty.", Toast.LENGTH_LONG).show();
                }

            }
        });
    }
}
