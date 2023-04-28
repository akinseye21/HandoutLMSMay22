package com.example.handoutlms;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ClickTutOnProfile extends AppCompatActivity {

    TextView fullname, groupName, joinedUsers, pendingRequest, date;
    Button editBtn;
    String got_groupName, got_name, got_category, got_description, got_mode, got_id, got_date;
    String fullnamed;
    ImageView back;
    TextView noviews;
    HorizontalScrollView horizontalScrollView;
    LinearLayout loading;
    LinearLayout linApproved, linPending;
    SharedPreferences preferences;

    LinearLayout card1, card2, card3, card4;
    ImageView img1, img2, img3, img4;
    TextView tutName1, tutName2, tutName3, tutName4;
    TextView count1, count2, count3, count4;

    // array for pending request
    ArrayList<String> arr_email = new ArrayList<>();
    ArrayList<String> arr_name = new ArrayList<>();
    ArrayList<String> arr_picture = new ArrayList<>();

    //array for approved request
    ArrayList<String> arr_email_approved = new ArrayList<>();
    ArrayList<String> arr_name_approved = new ArrayList<>();
    ArrayList<String> arr_picture_approved = new ArrayList<>();

    //array for top 4
    ArrayList<String> Array_fileDescription = new ArrayList<>();
    ArrayList<String> Array_fileURL = new ArrayList<>();
    ArrayList<String> Array_thumbnail = new ArrayList<>();
    ArrayList<String> Array_fileName = new ArrayList<>();
    ArrayList<String> Array_resID = new ArrayList<>();
    ArrayList<String> Array_totalHit = new ArrayList<>();

    private static final String TOP_4 = "https://handoutng.com/handouts/handout_get_top_resource_hits";
    private static final String USERS_TO_JOIN = "https://handoutng.com/handouts/handout_group_join_all";
    private static final String GET_APPROVED = "https://handoutng.com/handouts/handout_group_join_all_approved";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_click_tut_on_profile);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        preferences = getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        fullnamed = preferences.getString("fullname", "");

        fullname = findViewById(R.id.fullname);
        groupName = findViewById(R.id.groupName);
        joinedUsers = findViewById(R.id.joinedUsers);
        pendingRequest = findViewById(R.id.pendingRequest);
        date = findViewById(R.id.date);
        editBtn = findViewById(R.id.edit);
        fullname.setText(fullnamed);
        back = findViewById(R.id.back);
        noviews = findViewById(R.id.noviews);
        loading = findViewById(R.id.loading);
        linApproved = findViewById(R.id.lin1);
        linPending = findViewById(R.id.lin3);
        horizontalScrollView = findViewById(R.id.horizontalScrollView);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        Intent i = getIntent();
        got_groupName = i.getStringExtra("groupName");
        got_name = i.getStringExtra("name");
        got_category = i.getStringExtra("category");
        got_description = i.getStringExtra("description");
        got_mode = i.getStringExtra("mode");
        got_id = i.getStringExtra("id");
        got_date = i.getStringExtra("date");

        card1 = findViewById(R.id.card1);
        card2 = findViewById(R.id.card2);
        card3 = findViewById(R.id.card3);
        card4 = findViewById(R.id.card4);
        img1 = findViewById(R.id.img1);
        img2 = findViewById(R.id.img2);
        img3 = findViewById(R.id.img3);
        img4 = findViewById(R.id.img4);
        tutName1 = findViewById(R.id.name1);
        tutName2 = findViewById(R.id.name2);
        tutName3 = findViewById(R.id.name3);
        tutName4 = findViewById(R.id.name4);
        count1 = findViewById(R.id.count1);
        count2 = findViewById(R.id.count2);
        count3 = findViewById(R.id.count3);
        count4 = findViewById(R.id.count4);


        linPending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // goto the new view
                Intent i = new Intent(ClickTutOnProfile.this, PendingList4Tutorial.class);
                i.putStringArrayListExtra("name", arr_name);
                i.putStringArrayListExtra("email", arr_email);
                i.putStringArrayListExtra("picture", arr_picture);
                i.putExtra("id", got_id);
                i.putExtra("from", "pending");
                startActivity(i);
            }
        });

        linApproved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // goto the new view
                Intent i = new Intent(ClickTutOnProfile.this, PendingList4Tutorial.class);
                i.putStringArrayListExtra("name", arr_name_approved);
                i.putStringArrayListExtra("email", arr_email_approved);
                i.putStringArrayListExtra("picture", arr_picture_approved);
                i.putExtra("id", got_id);
                i.putExtra("from", "approved");
                startActivity(i);
            }
        });

        groupName.setText(got_groupName);
        date.setText(got_date);

        //get users already approved
        getApprovedUsers();
        // get users who wants to join my tutorial
        getUsersToJoin();
        // get top 4 views
        getTop4();

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ClickTutOnProfile.this, VideoCreatorView.class);
                i.putExtra("groupName", got_groupName);
                startActivity(i);
            }
        });

    }

    private void getApprovedUsers() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, GET_APPROVED,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try{
                            JSONArray jsonArray = new JSONArray(response);
                            int len = jsonArray.length();
                            joinedUsers.setText(String.valueOf(len));
                            for(int i=0; i<len; i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String memberID = jsonObject.getString("memberID");
                                String member = jsonObject.getString("member");
                                String picture = jsonObject.getString("picture");

                                arr_email_approved.add(memberID);
                                arr_name_approved.add(member);
                                arr_picture_approved.add(picture);
                            }

                        }
                        catch (JSONException e){
                            joinedUsers.setText("0");
                            System.out.println("Output = "+e.toString());
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
                params.put("tid", "group_"+got_id);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(ClickTutOnProfile.this);
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

    private void getTop4() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, TOP_4,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try{
                            JSONArray update = new JSONArray(response);
                            int size = update.length();
                            if (size >= 1){
                                for (int j=0; j<size; j++){
                                    JSONObject jsonObject = update.getJSONObject(j);
                                    String totalHit = jsonObject.getString("total_hit");
                                    String fileURL = jsonObject.getString("fileURL");
                                    String fileDescription = jsonObject.getString("fileDescription");
                                    String fileName = jsonObject.getString("fileName");
                                    String thumbnail = jsonObject.getString("thumbnail");
                                    String resID = jsonObject.getString("resID");

                                    Array_totalHit.add(totalHit);
                                    Array_fileURL.add(fileURL);
                                    Array_fileDescription.add(fileDescription);
                                    Array_fileName.add(fileName);
                                    Array_thumbnail.add(thumbnail);
                                    Array_resID.add(resID);
                                }



                                loading.setVisibility(View.GONE);
                                horizontalScrollView.setVisibility(View.VISIBLE);
                                noviews.setVisibility(View.GONE);

                                if (size == 1){
                                    //visual for card 1
                                    card1.setVisibility(View.VISIBLE);
                                    card2.setVisibility(View.GONE);
                                    card3.setVisibility(View.GONE);
                                    card4.setVisibility(View.GONE);
                                    Glide.with(ClickTutOnProfile.this).load(Array_thumbnail.get(0)).into(img1);
                                    tutName1.setText(Array_fileName.get(0));
                                    count1.setText(Array_totalHit.get(0));
                                } else if (size == 2){
                                    //visual for card 1
                                    card1.setVisibility(View.VISIBLE);
                                    card2.setVisibility(View.VISIBLE);
                                    card3.setVisibility(View.GONE);
                                    card4.setVisibility(View.GONE);
                                    Glide.with(ClickTutOnProfile.this).load(Array_thumbnail.get(0)).into(img1);
                                    tutName1.setText(Array_fileName.get(0));
                                    count1.setText(Array_totalHit.get(0));

                                    //visual for card 2
                                    Glide.with(ClickTutOnProfile.this).load(Array_thumbnail.get(1)).into(img2);
                                    tutName2.setText(Array_fileName.get(1));
                                    count2.setText(Array_totalHit.get(1));
                                } else if (size == 3){
                                    //visual for card 1
                                    card1.setVisibility(View.VISIBLE);
                                    card2.setVisibility(View.VISIBLE);
                                    card3.setVisibility(View.VISIBLE);
                                    card4.setVisibility(View.GONE);
                                    Glide.with(ClickTutOnProfile.this).load(Array_thumbnail.get(0)).into(img1);
                                    tutName1.setText(Array_fileName.get(0));
                                    count1.setText(Array_totalHit.get(0));

                                    //visual for card 2
                                    Glide.with(ClickTutOnProfile.this).load(Array_thumbnail.get(1)).into(img2);
                                    tutName2.setText(Array_fileName.get(1));
                                    count2.setText(Array_totalHit.get(1));

                                    //visual for card 3
                                    Glide.with(ClickTutOnProfile.this).load(Array_thumbnail.get(2)).into(img3);
                                    tutName3.setText(Array_fileName.get(2));
                                    count3.setText(Array_totalHit.get(2));
                                } else if (size == 4){
                                    //visual for card 1
                                    card1.setVisibility(View.VISIBLE);
                                    card2.setVisibility(View.VISIBLE);
                                    card3.setVisibility(View.VISIBLE);
                                    card4.setVisibility(View.VISIBLE);
                                    Glide.with(ClickTutOnProfile.this).load(Array_thumbnail.get(0)).into(img1);
                                    tutName1.setText(Array_fileName.get(0));
                                    count1.setText(Array_totalHit.get(0));

                                    //visual for card 2
                                    Glide.with(ClickTutOnProfile.this).load(Array_thumbnail.get(1)).into(img2);
                                    tutName2.setText(Array_fileName.get(1));
                                    count2.setText(Array_totalHit.get(1));

                                    //visual for card 3
                                    Glide.with(ClickTutOnProfile.this).load(Array_thumbnail.get(2)).into(img3);
                                    tutName3.setText(Array_fileName.get(2));
                                    count3.setText(Array_totalHit.get(2));

                                    //visual for card 4
                                    Glide.with(ClickTutOnProfile.this).load(Array_thumbnail.get(3)).into(img4);
                                    tutName4.setText(Array_fileName.get(3));
                                    count4.setText(Array_totalHit.get(3));
                                }



                            }else{
                                loading.setVisibility(View.GONE);
                                horizontalScrollView.setVisibility(View.GONE);
                                noviews.setVisibility(View.VISIBLE);
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
                params.put("groupname", got_groupName);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(ClickTutOnProfile.this);
        DefaultRetryPolicy retryPolicy = new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);
        requestQueue.add(stringRequest);


        //clear the array
        Array_fileDescription.clear();
        Array_thumbnail.clear();
        Array_fileURL.clear();
        Array_resID.clear();
        Array_totalHit.clear();
        Array_fileName.clear();
    }

    private void getUsersToJoin() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, USERS_TO_JOIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try{
                            JSONArray update = new JSONArray(response);
                            int size = update.length();
                            pendingRequest.setText(String.valueOf(size));
                            for(int j=0; j<size; j++){
                                JSONObject jsonObject = update.getJSONObject(j);
                                String member_email = jsonObject.getString("memberID");
                                String member_name = jsonObject.getString("member");
                                String picture = jsonObject.getString("picture");

                                arr_email.add(member_email);
                                arr_name.add(member_name);
                                arr_picture.add(picture);
                            }

                        }
                        catch (JSONException e){
                            pendingRequest.setText("0");
                            System.out.println("Output = "+e.toString());
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
                params.put("tid", "group_"+got_id);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(ClickTutOnProfile.this);
        DefaultRetryPolicy retryPolicy = new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);
        requestQueue.add(stringRequest);

        //clear the array
        arr_email.clear();
        arr_name.clear();
    }
}