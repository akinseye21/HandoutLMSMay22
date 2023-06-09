package com.example.handoutlms;

import static com.example.handoutlms.SeeBids.GET_ALL_BIDS;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ShowCreatedGigs extends AppCompatActivity {

    ImageView back;
    Button seeBids;
    TextView fullname, numOfBidders, startdate, enddate;
    TextView gigName, gigPaymentMode;

    SharedPreferences preferences;
    String got_email;
    String passedName, passedDescription, passedSkills, passedPrice, passedId, startDate, endDate;
    String got_fullname, got_institution, got_dept;
    public static final String USER_PROFILE = "http://handoutng.com/handouts/handout_get_user_profile";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_created_gigs);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        preferences = getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        got_email = preferences.getString("email", "not available");

        Intent i = getIntent();
        passedName = i.getStringExtra("gigname");
        passedDescription = i.getStringExtra("gigdescription");
        passedSkills = i.getStringExtra("gigskills");
        passedPrice = i.getStringExtra("gigprice");
        passedId = i.getStringExtra("gigId");
        startDate = i.getStringExtra("startDate");
        endDate = i.getStringExtra("endDate");

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShowCreatedGigs.this, FeedsDashboard.class);
                intent.putExtra("email", got_email);
                intent.putExtra("sent from", "checking gigs");
                startActivity(intent);
            }
        });

        fullname = findViewById(R.id.created_by_gig);
        numOfBidders = findViewById(R.id.numOfBidders);
        startdate = findViewById(R.id.startdate);
        enddate = findViewById(R.id.enddate);

        startdate.setText(startDate);
        enddate.setText(endDate);
//        depart = findViewById(R.id.dept_gig);
//        school = findViewById(R.id.uni_gig);
        gigName = findViewById(R.id.gig_name);
//        gigDescription = findViewById(R.id.gig_description);
//        gigSkills = findViewById(R.id.skills);
        gigPaymentMode = findViewById(R.id.gig_category);

        gigName.setText(passedName);
//        gigDescription.setText(passedDescription);
//        gigSkills.setText(passedSkills);
        gigPaymentMode.setText(passedPrice);


        //get user profile
        StringRequest stringRequest = new StringRequest(Request.Method.POST, USER_PROFILE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("Profile = "+response);

                        try{
                            JSONObject profile = new JSONObject(response);
//                                String got_email = profile.getString("email");
                            got_fullname = profile.getString("fullname");
                            got_institution = profile.getString("institution");
//                            got_faculty = profile.getString("faculty");
                            got_dept = profile.getString("department");

                            fullname.setText(got_fullname);
//                            depart.setText(got_dept);
//                            school.setText(got_institution);

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
                params.put("email", got_email);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);



        seeBids = findViewById(R.id.see_bid);
        seeBids.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), SeeBids.class);
                i.putExtra("userName", got_fullname);
                i.putExtra("userDept", got_dept);
                i.putExtra("userSchool", got_institution);
                i.putExtra("gigName", passedName);
                i.putExtra("gigId", passedId);
                i.putExtra("gigDescription", passedDescription);
                i.putExtra("gigSkills", passedSkills);
                i.putExtra("gigPrice", passedPrice);
                i.putExtra("startDate", startDate);
                i.putExtra("endDate", endDate);
                startActivity(i);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, GET_ALL_BIDS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONArray jsonArray = new JSONArray(response);
                            int bid_len = jsonArray.length();
                            numOfBidders.setText(String.valueOf(bid_len));
                            numOfBidders.setTextColor(Color.parseColor("#65dc9a"));
                        }
                        catch (JSONException e){
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String status = jsonObject.getString("status");

                                if (status.equals("no gig found")){
                                    numOfBidders.setText("0");
                                    numOfBidders.setTextColor(Color.parseColor("#888888"));
                                }
                            } catch (JSONException ex) {
                                ex.printStackTrace();
                            }
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        volleyError.printStackTrace();
                        numOfBidders.setText("0");
                        numOfBidders.setTextColor(Color.parseColor("#888888"));
                    }
                }){
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("gigid", passedId);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }
}
