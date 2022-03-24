package com.example.handoutlms;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

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
import java.util.HashMap;
import java.util.Map;

public class SeeBids extends AppCompatActivity {

    ImageView back;

    String username, userdept, userinstitution, gigname, gigid;
    TextView fullname, depart, school, gigName, notification_text, bid_count;
    ListView my_list;
    ProgressBar progressBar;
    TextView loading_text;

    ArrayList<String> Array_biddername = new ArrayList<>();
    ArrayList<String> Array_bidderemail = new ArrayList<>();
    ArrayList<String> Array_bidstatus = new ArrayList<>();
    ArrayList<String> Array_bidamount = new ArrayList<>();
    ArrayList<String> Array_bidid = new ArrayList<>();
    ArrayList<String> Array_gigid = new ArrayList<>();
    ArrayList<String> Array_cv = new ArrayList<>();

    public static final String GET_ALL_BIDS = "http://35.84.44.203/handouts/handout_get_all_bids_for_gig";

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

        fullname = findViewById(R.id.created_by_gig);
        depart = findViewById(R.id.dept_gig);
        school = findViewById(R.id.uni_gig);
        gigName = findViewById(R.id.gig_name);
        my_list = findViewById(R.id.my_list);
        notification_text = findViewById(R.id.notification_text);
        progressBar = findViewById(R.id.progressBar);
        loading_text = findViewById(R.id.loading_text);
        bid_count = findViewById(R.id.bid_count);

        fullname.setText(username);
        depart.setText(userdept);
        school.setText(userinstitution);
        gigName.setText(gigname);


        StringRequest stringRequest = new StringRequest(Request.Method.POST, GET_ALL_BIDS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("All Bids = "+response);

                        progressBar.setVisibility(View.GONE);
                        loading_text.setVisibility(View.GONE);

                        try{
                            JSONArray jsonArray = new JSONArray(response);
                            int bid_len = jsonArray.length();
                            bid_count.setText(String.valueOf(bid_len));

                            for(int j=0; j<bid_len; j++){
                                    JSONObject jsonObject = jsonArray.getJSONObject(j);
                                    String biddername = jsonObject.getString("biddername");
                                    String bidderemail = jsonObject.getString("biddermail");
                                    String bidstatus = jsonObject.getString("bidstatus");
                                    String bidamount = jsonObject.getString("bidamount");
                                    String bidid = jsonObject.getString("bidid");
                                    String got_gigid = jsonObject.getString("gigid");
                                    String CV = jsonObject.getString("cv");

                                    Array_biddername.add(biddername);
                                    Array_bidderemail.add(bidderemail);
                                    Array_bidstatus.add(bidstatus);
                                    Array_bidamount.add(bidamount);
                                    Array_bidid.add(bidid);
                                    Array_gigid.add(gigid);
                                    Array_cv.add(CV);
                            }



                            //populate values on the listview
                            SeeBidsAdapter seeBidsAdapter = new SeeBidsAdapter(getApplicationContext(), Array_biddername, Array_bidderemail, Array_bidstatus, Array_bidamount, Array_bidid, Array_gigid, Array_cv);
                            my_list.setAdapter(seeBidsAdapter);
                        }
                        catch (JSONException e){
//                            e.printStackTrace();
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String status = jsonObject.getString("status");

                                if (status.equals("no gig found")){
                                    notification_text.setVisibility(View.VISIBLE);
                                    my_list.setVisibility(View.GONE);
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

                        progressBar.setVisibility(View.GONE);
                        loading_text.setVisibility(View.GONE);
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
    }
}
