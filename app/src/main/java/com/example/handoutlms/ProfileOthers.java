package com.example.handoutlms;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProfileOthers extends AppCompatActivity implements
        tutorial_on_profile.OnFragmentInteractionListener,
        Games.OnFragmentInteractionListener,
        gig_on_profile.OnFragmentInteractionListener{

    TabLayout tabLayout;
    ViewPager viewPager;
    LinearLayout lintut, linpost, lingame, lingig, con;
    TextView email, username, dept, school, location, date, connect_txt;
    String got_fullname, got_dept, got_institution, got_dob, connected;
    String myEmail;
    SharedPreferences preferences;
    String got_email;
    ImageView back;

    public static final String USER_PROFILE = "http://35.84.44.203/handouts/handout_get_other_users";
    public static final String CONNECT_USER = "http://35.84.44.203/handouts/handout_connect_users";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_others);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Intent i = getIntent();
        got_email = i.getStringExtra("email");

        preferences = getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        myEmail = preferences.getString("email", "not available");


        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        viewPager =findViewById(R.id.viewpager2);
        tabLayout = findViewById(R.id.tabs);

        lintut = findViewById(R.id.lintut);
        lingame = findViewById(R.id.lingame);
        lingig = findViewById(R.id.lingig);


        email = findViewById(R.id.email);
        username = findViewById(R.id.user_name);
        dept = findViewById(R.id.department);
        school = findViewById(R.id.school);
        location = findViewById(R.id.location);
        date = findViewById(R.id.date);
        connect_txt = findViewById(R.id.connect_txt);

        addTabs(viewPager);

        connect_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (connect_txt.getText().equals("Connect")){
                    //connect to the user
                    connectUser();
                }
                else if (connect_txt.getText().equals("Connected")){
                    Toast.makeText(getApplicationContext(), "You are already connected to "+got_fullname, Toast.LENGTH_LONG).show();
                }
            }
        });




        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setOnTabSelectedListener(
                new TabLayout.ViewPagerOnTabSelectedListener(viewPager) {

                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        if (tab.getPosition() == 0){
                            lintut.setVisibility(View.VISIBLE);
                            lingame.setVisibility(View.GONE);
                            lingig.setVisibility(View.GONE);
                        }
                        else if(tab.getPosition() == 1){
                            lintut.setVisibility(View.GONE);
                            lingame.setVisibility(View.VISIBLE);
                            lingig.setVisibility(View.GONE);
                        }
                        else if(tab.getPosition() == 2){
                            lintut.setVisibility(View.GONE);
                            lingame.setVisibility(View.GONE);
                            lingig.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {
                        super.onTabReselected(tab);
                    }
                }
        );



        //get user profile
        StringRequest stringRequest = new StringRequest(Request.Method.POST, USER_PROFILE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("Profile = "+response);

                        try{
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject profile = jsonArray.getJSONObject(0);
//                                String gotten_email = profile.getString("email");
                            got_fullname = profile.getString("fullname");
                            got_dob = profile.getString("dob");
                            got_institution = profile.getString("institution");
//                            got_faculty = profile.getString("faculty");
                            got_dept = profile.getString("department");
                            connected = profile.getString("connected");

                            email.setText(got_email);
                            username.setText(got_fullname);
                            dept.setText(got_dept);
                            school.setText(got_institution);
                            date.setText(got_dob);

                            if (connected.equals("yes")){
                                connect_txt.setText("Connected");
                                connect_txt.setTextColor(Color.WHITE);
                                connect_txt.setBackgroundResource(R.drawable.rounded_white_transparent_100);
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
                        volleyError.printStackTrace();
                    }
                }){
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("myemail", myEmail);
                params.put("email", got_email);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    private void connectUser() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, CONNECT_USER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try{
                            JSONObject check = new JSONObject(response);
                            String stats = check.getString("status");

                            if (stats.equals("connected")){
                                connect_txt.setText("Connected");
                                connect_txt.setTextColor(Color.WHITE);
                                connect_txt.setBackgroundResource(R.drawable.rounded_white_transparent_100);
                                Toast.makeText(getApplicationContext(), "You are now connected with user - "+got_fullname, Toast.LENGTH_LONG).show();
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
                        volleyError.printStackTrace();
                    }
                }){
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("myemail", "jdoe@me.com");
                params.put("email", got_email);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    private void addTabs(ViewPager viewPager) {
        Profile2.ViewPagerAdapter adapter = new Profile2.ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new tutorial_on_profile(), "");
        adapter.addFrag(new Games(), "");
        adapter.addFrag(new gig_on_profile(), "");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
