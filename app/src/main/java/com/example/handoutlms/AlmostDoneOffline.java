package com.example.handoutlms;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
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

public class AlmostDoneOffline extends AppCompatActivity {

    LinearLayout done;
    String group_name, category, date, time, university, description, tut_type, tutorial_mode, email, location_st;
    TextView gp_name, cat, dat, tim, uni, desc, location;
    ProgressBar progressBar;
    ImageView back;
    String CHANNEL_ID = "channelID";
    String notification;
    Dialog myDialog;
    String usertype;

    public static final String TUTORIAL_GROUP = "https://handoutng.com/handouts/handout_tutorial_groups";
    public static final String UPDATE = "https://handoutng.com/handouts/handout_usertype";
    public static final String GET_PROFILE = "https://handoutng.com/handouts/handout_get_user_profile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_almost_done_offline);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Intent i = getIntent();
        group_name = i.getStringExtra("group_name");
        category = i.getStringExtra("category");
        date = i.getStringExtra("date");
        time = i.getStringExtra("time");
        university = i.getStringExtra("university");
        description = i.getStringExtra("description");
        email = i.getStringExtra("email");
        location_st = i.getStringExtra("location");

        gp_name = findViewById(R.id.tutorial_group_name);
        cat = findViewById(R.id.category);
        dat = findViewById(R.id.date);
        tim = findViewById(R.id.time);
        uni = findViewById(R.id.university);
        desc = findViewById(R.id.description);
        progressBar = findViewById(R.id.progressBar);
        back = findViewById(R.id.back);
        location = findViewById(R.id.location);

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
        location.setText(location_st);

        done = findViewById(R.id.next);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressBar.setVisibility(View.VISIBLE);

                //get profile
                StringRequest stringRequest = new StringRequest(Request.Method.POST, GET_PROFILE,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                System.out.println("Checking usertype = "+response);

                                try{
                                    JSONObject jsonObject = new JSONObject(response);
                                    usertype = (String) jsonObject.getString("usertype");

                                    createTutorial();
                                }
                                catch (JSONException e){
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
                        params.put("email", email);
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

            }
        });
    }

    private void createTutorial() {

        if (usertype.equals("student")){
            //send to server
            StringRequest stringRequest = new StringRequest(Request.Method.POST, TUTORIAL_GROUP,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressBar.setVisibility(View.GONE);

                            System.out.println("Response = "+response);

                            try{
                                JSONObject jsonObject = new JSONObject(response);

                                String status = jsonObject.getString("status");
                                notification = jsonObject.getString("notification");
                                System.out.println(response);

                                if(status.equals("successful")){
                                    Toast.makeText(getApplicationContext(), "Offline Group created successfully", Toast.LENGTH_LONG).show();
                                    System.out.println(jsonObject);

                                    //update usertype
                                    updateUser();

                                    //show popup window
                                    myDialog = new Dialog(AlmostDoneOffline.this);
                                    myDialog.setContentView(R.layout.custom_popup_upload_successful);
                                    TextView text = myDialog.findViewById(R.id.text);
                                    Button ok = myDialog.findViewById(R.id.addmore);
                                    Button viewgroup = myDialog.findViewById(R.id.viewgroup);
                                    viewgroup.setVisibility(View.GONE);
                                    ok.setText("OK");
                                    text.setText("Tutorial created successfully");
                                    ok.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            myDialog.dismiss();

                                            Intent i = new Intent(AlmostDoneOffline.this, CreateOnlineTutPhase1.class);
                                            i.putExtra("Group_name", group_name);
                                            i.putExtra("notification", notification);
                                            startActivity(i);
                                        }
                                    });
                                    myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                    myDialog.setCanceledOnTouchOutside(false);
                                    myDialog.show();


                                }
                                else{
                                    Toast.makeText(getApplicationContext(), "Group Creation failed.", Toast.LENGTH_LONG).show();
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
                    params.put("tutorial_type", "public");
                    params.put("date", date);
                    params.put("time", time);
                    params.put("institution", university);
                    params.put("short_description", description);
                    params.put("tutorial_mode", "offline");
                    params.put("venue", location_st);
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
        }
        else{
            //send to server
            progressBar.setVisibility(View.VISIBLE);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, TUTORIAL_GROUP,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressBar.setVisibility(View.GONE);

                            System.out.println("Response = "+response);

                            try{
                                JSONObject jsonObject = new JSONObject(response);

//                                        String signed_name = jsonObject.getString("fullname");
                                String status = jsonObject.getString("status");
                                notification = jsonObject.getString("notification");
                                System.out.println(response);

                                if(status.equals("successful")){
                                    Toast.makeText(getApplicationContext(), "Offline Group created successfully", Toast.LENGTH_LONG).show();
                                    System.out.println(jsonObject);

                                    //show popup window
                                    myDialog = new Dialog(AlmostDoneOffline.this);
                                    myDialog.setContentView(R.layout.custom_popup_upload_successful);
                                    TextView text = myDialog.findViewById(R.id.text);
                                    Button ok = myDialog.findViewById(R.id.addmore);
                                    Button viewgroup = myDialog.findViewById(R.id.viewgroup);
                                    viewgroup.setVisibility(View.GONE);
                                    ok.setText("OK");
                                    text.setText("Tutorial created successfully");
                                    ok.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            myDialog.dismiss();

                                            Intent i = new Intent(AlmostDoneOffline.this, CreateOnlineTutPhase1.class);
                                            i.putExtra("Group_name", group_name);
                                            i.putExtra("notification", notification);
                                            startActivity(i);
                                        }
                                    });
                                    myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                    myDialog.setCanceledOnTouchOutside(false);
                                    myDialog.show();



                                }
                                else{
                                    Toast.makeText(getApplicationContext(), "Group Creation failed.", Toast.LENGTH_LONG).show();
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
                    params.put("tutorial_type", "public");
                    params.put("date", date);
                    params.put("time", time);
                    params.put("institution", university);
                    params.put("short_description", description);
                    params.put("tutorial_mode", "offline");
                    params.put("venue", location_st);
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
        }


    }

    public void updateUser(){
        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, UPDATE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try{
                            JSONObject jsonObject = new JSONObject(response);

                            String status2 = jsonObject.getString("status");
                            if(status2.equals("success")){
                                //you are now a tutor on Handout


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
                    }
                }){
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("usertype", "tutor");
                return params;
            }
        };
        RequestQueue requestQueue2 = Volley.newRequestQueue(getApplicationContext());
        DefaultRetryPolicy retryPolicy = new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest2.setRetryPolicy(retryPolicy);
        requestQueue2.add(stringRequest2);
    }
}