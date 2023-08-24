package com.example.handoutlms.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.handoutlms.R;
import com.example.handoutlms.adapters.SeeBidsAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SeeBids extends AppCompatActivity {

    ImageView back;

    String username, userdept, userinstitution, gigname, gigid, gigdescription, gigskills, gigprice, startdate, enddate;
    TextView fullname, depart, school, gigName, notification_text;
    ListView my_list;
    LinearLayout loading;

    ArrayList<String> Array_biddername = new ArrayList<>();
    ArrayList<String> Array_bidderemail = new ArrayList<>();
    ArrayList<String> Array_bidstatus = new ArrayList<>();
    ArrayList<String> Array_bidamount = new ArrayList<>();
    ArrayList<String> Array_bidid = new ArrayList<>();
    ArrayList<String> Array_gigid = new ArrayList<>();
    ArrayList<String> Array_cv = new ArrayList<>();
    ArrayList<String> Array_pp = new ArrayList<>();


    public static final String GET_ALL_BIDS = "https://handoutng.com/handouts/handout_get_all_bids_for_gig";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_bids);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent i = getIntent();
        username = i.getStringExtra("userName");
        userdept = i.getStringExtra("userDept");
        userinstitution = i.getStringExtra("userSchool");
        gigname = i.getStringExtra("gigName");
        gigid = i.getStringExtra("gigId");
        gigdescription = i.getStringExtra("gigDescription");
        gigskills = i.getStringExtra("gigSkills");
        gigprice = i.getStringExtra("gigPrice");
        startdate = i.getStringExtra("startDate");
        enddate = i.getStringExtra("endDate");

        fullname = findViewById(R.id.created_by_gig);
        gigName = findViewById(R.id.gig_name);
        my_list = findViewById(R.id.my_list);
        notification_text = findViewById(R.id.notification_text);
        loading = findViewById(R.id.loading);

        fullname.setText(username);
        gigName.setText(gigname);
    }


    @Override
    protected void onStart() {
        super.onStart();

        proceed();

    }

    public void proceed(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, GET_ALL_BIDS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("All Bids = "+response);

                        String biddername = "";
                        String bidderemail ="";
                        String bidstatus="";
                        String bidamount="";
                        String bidid="";
                        String got_gigid="";
                        String CV="";
                        String profile_pic="";

                        try{
                            JSONArray jsonArray = new JSONArray(response);
                            int bid_len = jsonArray.length();
                            for(int j=0; j<bid_len; j++){
                                JSONObject jsonObject = jsonArray.getJSONObject(j);
                                biddername = jsonObject.getString("biddername");
                                bidderemail = jsonObject.getString("biddermail");
                                bidstatus = jsonObject.getString("bidstatus");
                                bidamount = jsonObject.getString("bidamount");
                                bidid = jsonObject.getString("bidid");
                                got_gigid = jsonObject.getString("gigid");
                                CV = jsonObject.getString("cv");
                                profile_pic = jsonObject.getString("profile_pic");

                                Array_biddername.add(biddername);
                                Array_bidderemail.add(bidderemail);
                                Array_bidstatus.add(bidstatus);
                                Array_bidamount.add(bidamount);
                                Array_bidid.add(bidid);
                                Array_gigid.add(gigid);
                                Array_cv.add(CV);
                                Array_pp.add(profile_pic);
                            }
                            int checker = 0;
                            for(int w=0; w<bid_len; w++){

                                if (Array_bidstatus.get(w).contains("approved")){
                                    //increase checker
                                    checker = checker + 1;
                                }else{
                                    //do nothing
                                }
                            }

                            if (checker>=1){
                                //go to next screen
                                Intent intent = new Intent(SeeBids.this, ApprovedGigUserView.class);
                                intent.putExtra("biddername", biddername);
                                intent.putExtra("bidderemail", bidderemail);
                                intent.putExtra("bidamount", bidamount);
                                intent.putExtra("cv", CV);
                                intent.putExtra("pp", profile_pic);
                                intent.putExtra("gigname", gigname);
                                intent.putExtra("gigdescription", gigdescription);
                                intent.putExtra("gigskills", gigskills);
                                intent.putExtra("gigprice", gigprice);
                                intent.putExtra("gigid", gigid);
                                intent.putExtra("startdate", startdate);
                                intent.putExtra("enddate", enddate);
                                startActivity(intent);
                            }else{
                                //send to adapter
                                loading.setVisibility(View.GONE);
                                //populate values on the listview
                                SeeBidsAdapter seeBidsAdapter = new SeeBidsAdapter(SeeBids.this, Array_biddername, Array_bidderemail, Array_bidstatus, Array_bidamount, Array_bidid, Array_gigid, Array_cv, Array_pp, gigname);
                                my_list.setAdapter(seeBidsAdapter);
                            }


                        }
                        catch (JSONException e){
//                            e.printStackTrace();
                            loading.setVisibility(View.GONE);
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String status = jsonObject.getString("status");

                                if (status.equals("no gig found")){
                                    notification_text.setVisibility(View.VISIBLE);
                                    my_list.setVisibility(View.GONE);
//                                    radioGroup.setVisibility(View.GONE);
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
                        loading.setVisibility(View.GONE);
                    }
                }){
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("gigid", gigid);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
        DefaultRetryPolicy retryPolicy = new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);
        requestQueue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
            @Override
            public void onRequestFinished(Request<Object> request) {
                requestQueue.getCache().clear();
            }
        });

        Array_biddername.clear();
        Array_bidderemail.clear();
        Array_bidstatus.clear();
        Array_bidamount.clear();
        Array_bidid.clear();
        Array_gigid.clear();
        Array_cv.clear();
        Array_pp.clear();

    }
}
