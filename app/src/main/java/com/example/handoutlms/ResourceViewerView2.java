package com.example.handoutlms;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ResourceViewerView2 extends AppCompatActivity {

    ImageView back;
    TextView fullname, groupName;
    TextView joinedUsers, date;
    ImageView recentlyWatched1, recentlyWatched2, recentlyWatched3, recentlyWatched4;
    TextView txtRecentlyWatched1, txtRecentlyWatched2, txtRecentlyWatched3, txtRecentlyWatched4;
    LinearLayout card11, card21, card31, card41;

    ImageView mostViewed1, mostViewed2, mostViewed3, mostViewed4;
    TextView txtMostlyWatched1, txtMostlyWatched2, txtMostlyWatched3, txtMostlyWatched4;
    LinearLayout card12, card22, card32, card42;

    ImageView recentlyAdded1, recentlyAdded2, recentlyAdded3, recentlyAdded4;
    TextView txtRecentlyAdded1, txtRecentlyAdded2, txtRecentlyAdded3, txtRecentlyAdded4;
    LinearLayout card13, card23, card33, card43;

    LinearLayout Loading1, Loading2, Loading3;
    HorizontalScrollView horizontalScrollView1, horizontalScrollView2, horizontalScrollView3;
    TextView noViews, noMostViewed, noRecentlyAdded;

    private static final String TOP_4 = "https://handoutng.com/handouts/handout_get_top_resource_hits";
    public static final String ALL_RESOURCE_FOR_A_GROUP = "https://handoutng.com/handouts/handout_get_all_tutorials";
    public static final String GET_RESOURCE_VIEWED = "https://handoutng.com/handouts/handout_get_recent_resource_viewed";


    //array for recently watched
    ArrayList<String> Array_fileDescription1 = new ArrayList<>();
    ArrayList<String> Array_fileURL1 = new ArrayList<>();
    ArrayList<String> Array_thumbnail1 = new ArrayList<>();
    ArrayList<String> Array_fileName1 = new ArrayList<>();
    ArrayList<String> Array_resID1 = new ArrayList<>();

    //array for most viewed
    ArrayList<String> Array_fileDescription2 = new ArrayList<>();
    ArrayList<String> Array_fileURL2 = new ArrayList<>();
    ArrayList<String> Array_thumbnail2 = new ArrayList<>();
    ArrayList<String> Array_fileName2 = new ArrayList<>();
    ArrayList<String> Array_resID2 = new ArrayList<>();
    ArrayList<String> Array_totalHit2 = new ArrayList<>();

    //array for top 4
    ArrayList<String> Array_fileDescription3 = new ArrayList<>();
    ArrayList<String> Array_fileURL3 = new ArrayList<>();
    ArrayList<String> Array_thumbnail3 = new ArrayList<>();
    ArrayList<String> Array_fileName3 = new ArrayList<>();
    ArrayList<String> Array_resID3 = new ArrayList<>();
//    ArrayList<String> Array_totalHit3 = new ArrayList<>();

    String name, category, description, status, id, dated;
    Button viewResources;
    SharedPreferences preferences;
    String fullnamed, got_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resource_viewer_view2);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        preferences = getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        fullnamed = preferences.getString("fullname", "");
        got_email = preferences.getString("email", "");

        Intent i = getIntent();
        name = i.getStringExtra("name");
        category = i.getStringExtra("category");
        description = i.getStringExtra("description");
        status = i.getStringExtra("mode");
        id  = i.getStringExtra("id");
        dated = i.getStringExtra("date");

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        fullname = findViewById(R.id.fullname);
        fullname.setText(fullnamed);
        groupName = findViewById(R.id.groupName);
        groupName.setText(name);
        joinedUsers = findViewById(R.id.joinedUsers);
        date = findViewById(R.id.date);
        date.setText(dated);
        viewResources = findViewById(R.id.view);
        viewResources.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ResourceViewerView2.this, ResourceViewerView.class);
                i.putExtra("name", name);
                i.putExtra("category", category);
                i.putExtra("description", description);
                i.putExtra("status", status);
                i.putExtra("id", id);
                startActivity(i);
            }
        });

        card11 = findViewById(R.id.card11);
        card21 = findViewById(R.id.card21);
        card31 = findViewById(R.id.card31);
        card41 = findViewById(R.id.card41);
        recentlyWatched1 = findViewById(R.id.img11);
        recentlyWatched2 = findViewById(R.id.img21);
        recentlyWatched3 = findViewById(R.id.img31);
        recentlyWatched4 = findViewById(R.id.img41);
        txtRecentlyWatched1 = findViewById(R.id.name11);
        txtRecentlyWatched2 = findViewById(R.id.name21);
        txtRecentlyWatched3 = findViewById(R.id.name31);
        txtRecentlyWatched4 = findViewById(R.id.name41);
        Loading1 = findViewById(R.id.loading1);
        horizontalScrollView1 = findViewById(R.id.horizontalScrollView);
        noViews = findViewById(R.id.noviews);

        card12 = findViewById(R.id.card12);
        card22 = findViewById(R.id.card22);
        card32 = findViewById(R.id.card32);
        card42 = findViewById(R.id.card42);
        mostViewed1 = findViewById(R.id.img12);
        mostViewed2 = findViewById(R.id.img22);
        mostViewed3 = findViewById(R.id.img32);
        mostViewed4 = findViewById(R.id.img42);
        txtMostlyWatched1 = findViewById(R.id.name12);
        txtMostlyWatched2 = findViewById(R.id.name22);
        txtMostlyWatched3 = findViewById(R.id.name32);
        txtMostlyWatched4 = findViewById(R.id.name42);
        Loading2 = findViewById(R.id.loading2);
        horizontalScrollView2 = findViewById(R.id.horizontalScrollView2);
        noMostViewed = findViewById(R.id.nomostviewed);

        card13 = findViewById(R.id.card13);
        card23 = findViewById(R.id.card23);
        card33 = findViewById(R.id.card33);
        card43 = findViewById(R.id.card43);
        recentlyAdded1 = findViewById(R.id.img13);
        recentlyAdded2 = findViewById(R.id.img23);
        recentlyAdded3 = findViewById(R.id.img33);
        recentlyAdded4 = findViewById(R.id.img43);
        txtRecentlyAdded1 = findViewById(R.id.name13);
        txtRecentlyAdded2 = findViewById(R.id.name23);
        txtRecentlyAdded3 = findViewById(R.id.name33);
        txtRecentlyAdded4 = findViewById(R.id.name43);
        Loading3 = findViewById(R.id.loading3);
        horizontalScrollView3 = findViewById(R.id.horizontalScrollView3);
        noRecentlyAdded = findViewById(R.id.recentlyadded);


        getRecentlyWatched();
        getMostViewed();
        getRecentlyAdded();

    }

    private void getRecentlyWatched(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, GET_RESOURCE_VIEWED,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try{
                            JSONArray jsonArray = new JSONArray(response);
                            int size = jsonArray.length();

                            if(size>0){
                                for(int j = 0; j < size; j++){
                                    JSONObject section1 = jsonArray.getJSONObject(j);
                                    String resID = section1.getString("resID");
                                    String fileUrl = section1.getString("fileURL");
                                    String filedescription = section1.getString("fileDescription");
                                    String thumbnail = section1.getString("thumbnail");
                                    String filename = section1.getString("fileName");

                                    Array_fileURL1.add(fileUrl);
                                    Array_fileDescription1.add(filedescription);
                                    Array_thumbnail1.add(thumbnail);
                                    Array_fileName1.add(filename);
                                    Array_resID1.add(resID);

                                }

                                Loading1.setVisibility(View.GONE);
                                horizontalScrollView1.setVisibility(View.VISIBLE);
                                noViews.setVisibility(View.GONE);

                                if (size == 1){
                                    //visual for card 1
                                    card11.setVisibility(View.VISIBLE);
                                    card21.setVisibility(View.GONE);
                                    card31.setVisibility(View.GONE);
                                    card41.setVisibility(View.GONE);
                                    Glide.with(ResourceViewerView2.this).load(Array_thumbnail1.get(0)).into(recentlyWatched1);
                                    txtRecentlyWatched1.setText(Array_fileName1.get(0));
                                } else if (size == 2){
                                    //visual for card 1
                                    card11.setVisibility(View.VISIBLE);
                                    card21.setVisibility(View.VISIBLE);
                                    card31.setVisibility(View.GONE);
                                    card41.setVisibility(View.GONE);
                                    Glide.with(ResourceViewerView2.this).load(Array_thumbnail1.get(0)).into(recentlyWatched1);
                                    txtRecentlyWatched1.setText(Array_fileName1.get(0));

                                    //visual for card 2
                                    Glide.with(ResourceViewerView2.this).load(Array_thumbnail1.get(1)).into(recentlyWatched2);
                                    txtRecentlyWatched2.setText(Array_fileName1.get(1));
                                } else if (size == 3){
                                    //visual for card 1
                                    card11.setVisibility(View.VISIBLE);
                                    card21.setVisibility(View.VISIBLE);
                                    card31.setVisibility(View.VISIBLE);
                                    card41.setVisibility(View.GONE);
                                    Glide.with(ResourceViewerView2.this).load(Array_thumbnail1.get(0)).into(recentlyWatched1);
                                    txtRecentlyWatched1.setText(Array_fileName1.get(0));

                                    //visual for card 2
                                    Glide.with(ResourceViewerView2.this).load(Array_thumbnail1.get(1)).into(recentlyWatched2);
                                    txtRecentlyWatched2.setText(Array_fileName1.get(1));

                                    //visual for card 3
                                    Glide.with(ResourceViewerView2.this).load(Array_thumbnail1.get(2)).into(recentlyWatched3);
                                    txtRecentlyWatched3.setText(Array_fileName1.get(2));
                                } else if (size == 4) {
                                    //visual for card 1
                                    card11.setVisibility(View.VISIBLE);
                                    card21.setVisibility(View.VISIBLE);
                                    card31.setVisibility(View.VISIBLE);
                                    card41.setVisibility(View.VISIBLE);
                                    Glide.with(ResourceViewerView2.this).load(Array_thumbnail1.get(0)).into(recentlyWatched1);
                                    txtRecentlyWatched1.setText(Array_fileName1.get(0));

                                    //visual for card 2
                                    Glide.with(ResourceViewerView2.this).load(Array_thumbnail1.get(1)).into(recentlyWatched2);
                                    txtRecentlyWatched2.setText(Array_fileName1.get(1));

                                    //visual for card 3
                                    Glide.with(ResourceViewerView2.this).load(Array_thumbnail1.get(2)).into(recentlyWatched3);
                                    txtRecentlyWatched3.setText(Array_fileName1.get(2));

                                    //visual for card 4
                                    Glide.with(ResourceViewerView2.this).load(Array_thumbnail1.get(3)).into(recentlyWatched4);
                                    txtRecentlyWatched4.setText(Array_fileName1.get(3));
                                }
                            }
                            else{
                                Loading1.setVisibility(View.GONE);
                                horizontalScrollView1.setVisibility(View.GONE);
                                noViews.setVisibility(View.VISIBLE);
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
                params.put("groupname", name);
                params.put("email", got_email);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(ResourceViewerView2.this);
        DefaultRetryPolicy retryPolicy = new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);
        requestQueue.add(stringRequest);

        Array_fileDescription1.clear();
        Array_fileURL1.clear();
        Array_thumbnail1.clear();
        Array_fileName1.clear();
        Array_resID1.clear();
    }

    private void getMostViewed() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, TOP_4,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try{
                            JSONArray update = new JSONArray(response);
                            int size = update.length();
                            System.out.println("The size = "+size);
                            System.out.println("The array = "+response);
                            if (size > 0){
                                for (int j = 0; j < size; j++){
                                    JSONObject jsonObject = update.getJSONObject(j);
                                    String fileURL = jsonObject.getString("fileURL");
                                    String fileDescription = jsonObject.getString("fileDescription");
                                    String fileName = jsonObject.getString("fileName");
                                    String thumbnail = jsonObject.getString("thumbnail");
                                    String resID = jsonObject.getString("resID");

                                    Array_fileURL2.add(fileURL);
                                    Array_fileDescription2.add(fileDescription);
                                    Array_fileName2.add(fileName);
                                    Array_thumbnail2.add(thumbnail);
                                    Array_resID2.add(resID);
                                }



                                Loading2.setVisibility(View.GONE);
                                horizontalScrollView2.setVisibility(View.VISIBLE);
                                noMostViewed.setVisibility(View.GONE);

                                if (size == 1){
                                    //visual for card 1
                                    card12.setVisibility(View.VISIBLE);
                                    card22.setVisibility(View.GONE);
                                    card32.setVisibility(View.GONE);
                                    card42.setVisibility(View.GONE);
                                    Glide.with(ResourceViewerView2.this).load(Array_thumbnail2.get(0)).into(mostViewed1);
                                    txtMostlyWatched1.setText(Array_fileName2.get(0));
                                } else if (size == 2){
                                    //visual for card 1
                                    card12.setVisibility(View.VISIBLE);
                                    card22.setVisibility(View.VISIBLE);
                                    card32.setVisibility(View.GONE);
                                    card42.setVisibility(View.GONE);
                                    Glide.with(ResourceViewerView2.this).load(Array_thumbnail2.get(0)).into(mostViewed1);
                                    txtMostlyWatched1.setText(Array_fileName2.get(0));

                                    //visual for card 2
                                    Glide.with(ResourceViewerView2.this).load(Array_thumbnail2.get(1)).into(mostViewed2);
                                    txtMostlyWatched2.setText(Array_fileName2.get(1));
                                } else if (size == 3){
                                    //visual for card 1
                                    card13.setVisibility(View.VISIBLE);
                                    card23.setVisibility(View.VISIBLE);
                                    card33.setVisibility(View.VISIBLE);
                                    card43.setVisibility(View.GONE);
                                    Glide.with(ResourceViewerView2.this).load(Array_thumbnail2.get(0)).into(mostViewed1);
                                    txtMostlyWatched1.setText(Array_fileName2.get(0));

                                    //visual for card 2
                                    Glide.with(ResourceViewerView2.this).load(Array_thumbnail2.get(1)).into(mostViewed2);
                                    txtMostlyWatched2.setText(Array_fileName2.get(1));

                                    //visual for card 3
                                    Glide.with(ResourceViewerView2.this).load(Array_thumbnail2.get(2)).into(mostViewed3);
                                    txtMostlyWatched3.setText(Array_fileName2.get(2));
                                } else if (size == 4){
                                    //visual for card 1
                                    card13.setVisibility(View.VISIBLE);
                                    card23.setVisibility(View.VISIBLE);
                                    card33.setVisibility(View.VISIBLE);
                                    card43.setVisibility(View.VISIBLE);
                                    Glide.with(ResourceViewerView2.this).load(Array_thumbnail2.get(0)).into(mostViewed1);
                                    txtMostlyWatched1.setText(Array_fileName2.get(0));

                                    //visual for card 2
                                    Glide.with(ResourceViewerView2.this).load(Array_thumbnail2.get(1)).into(mostViewed2);
                                    txtMostlyWatched2.setText(Array_fileName2.get(1));

                                    //visual for card 3
                                    Glide.with(ResourceViewerView2.this).load(Array_thumbnail2.get(2)).into(mostViewed3);
                                    txtMostlyWatched3.setText(Array_fileName2.get(2));

                                    //visual for card 4
                                    Glide.with(ResourceViewerView2.this).load(Array_thumbnail2.get(3)).into(mostViewed4);
                                    txtMostlyWatched4.setText(Array_fileName2.get(3));
                                }



                            }else{
                                Loading2.setVisibility(View.GONE);
                                horizontalScrollView2.setVisibility(View.GONE);
                                noMostViewed.setVisibility(View.VISIBLE);
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
                params.put("groupname", name);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(ResourceViewerView2.this);
        DefaultRetryPolicy retryPolicy = new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);
        requestQueue.add(stringRequest);


        //clear the array
        Array_fileDescription2.clear();
        Array_thumbnail2.clear();
        Array_fileURL2.clear();
        Array_resID2.clear();
        Array_totalHit2.clear();
        Array_fileName2.clear();
    }

    private void getRecentlyAdded(){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, ALL_RESOURCE_FOR_A_GROUP,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try{
                            JSONArray jsonArray = new JSONArray(response);
                            int ArrayLength = jsonArray.length();

                            for(int j = 0; j < ArrayLength; j++){
                                JSONObject section1 = jsonArray.getJSONObject(j);
                                String tutName = section1.getString("groupname");
                                String tutResources = section1.getString("tutorial_resource");

                                if(name.equals(tutName)){
                                    JSONArray jsonArray1 = new JSONArray(tutResources);
                                    int size = jsonArray1.length();

                                    if (size>0){
                                        for(int k=size-1; k >= 0; k--){
                                            JSONObject section2 = jsonArray1.getJSONObject(k);
                                            String resID = section2.getString("resID");
                                            String fileUrl = section2.getString("fileURL");
                                            String filedescription = section2.getString("fileDescription");
                                            String thumbnail = section2.getString("thumbnail");
                                            String filename = section2.getString("fileName");

                                            System.out.println("fileurl = "+fileUrl+"\nfiledescription = "+filedescription);

                                            Array_fileURL3.add(fileUrl);
                                            Array_fileDescription3.add(filedescription);
                                            Array_thumbnail3.add(thumbnail);
                                            Array_fileName3.add(filename);
                                            Array_resID3.add(resID);
                                        }

                                        Loading3.setVisibility(View.GONE);
                                        horizontalScrollView3.setVisibility(View.VISIBLE);
                                        noRecentlyAdded.setVisibility(View.GONE);

                                        if (size == 1){
                                            //visual for card 1
                                            horizontalScrollView3.setVisibility(View.VISIBLE);
                                            card13.setVisibility(View.VISIBLE);
                                            card23.setVisibility(View.GONE);
                                            card33.setVisibility(View.GONE);
                                            card43.setVisibility(View.GONE);
                                            Glide.with(ResourceViewerView2.this).load(Array_thumbnail3.get(0)).into(recentlyAdded1);
                                            txtRecentlyAdded1.setText(Array_fileName3.get(0));
                                        } else if (size == 2){
                                            //visual for card 1
                                            horizontalScrollView3.setVisibility(View.VISIBLE);
                                            card13.setVisibility(View.VISIBLE);
                                            card23.setVisibility(View.VISIBLE);
                                            card33.setVisibility(View.GONE);
                                            card43.setVisibility(View.GONE);
                                            Glide.with(ResourceViewerView2.this).load(Array_thumbnail3.get(0)).into(recentlyAdded1);
                                            txtRecentlyAdded1.setText(Array_fileName3.get(0));

                                            //visual for card 2
                                            Glide.with(ResourceViewerView2.this).load(Array_thumbnail3.get(1)).into(recentlyAdded2);
                                            txtRecentlyAdded2.setText(Array_fileName3.get(1));
                                        } else if (size == 3){
                                            //visual for card 1
                                            horizontalScrollView3.setVisibility(View.VISIBLE);
                                            card13.setVisibility(View.VISIBLE);
                                            card23.setVisibility(View.VISIBLE);
                                            card33.setVisibility(View.VISIBLE);
                                            card43.setVisibility(View.GONE);
                                            Glide.with(ResourceViewerView2.this).load(Array_thumbnail3.get(0)).into(recentlyAdded1);
                                            txtRecentlyAdded1.setText(Array_fileName3.get(0));

                                            //visual for card 2
                                            Glide.with(ResourceViewerView2.this).load(Array_thumbnail3.get(1)).into(recentlyAdded2);
                                            txtRecentlyAdded2.setText(Array_fileName3.get(1));

                                            //visual for card 3
                                            Glide.with(ResourceViewerView2.this).load(Array_thumbnail3.get(2)).into(recentlyAdded3);
                                            txtRecentlyAdded3.setText(Array_fileName3.get(2));
                                        } else {
                                            //visual for card 1
                                            horizontalScrollView3.setVisibility(View.VISIBLE);
                                            card13.setVisibility(View.VISIBLE);
                                            card23.setVisibility(View.VISIBLE);
                                            card33.setVisibility(View.VISIBLE);
                                            card43.setVisibility(View.VISIBLE);
                                            Glide.with(ResourceViewerView2.this).load(Array_thumbnail3.get(0)).into(recentlyAdded1);
                                            txtRecentlyAdded1.setText(Array_fileName3.get(0));

                                            //visual for card 2
                                            Glide.with(ResourceViewerView2.this).load(Array_thumbnail3.get(1)).into(recentlyAdded2);
                                            txtRecentlyAdded2.setText(Array_fileName3.get(1));

                                            //visual for card 3
                                            Glide.with(ResourceViewerView2.this).load(Array_thumbnail3.get(2)).into(recentlyAdded3);
                                            txtRecentlyAdded3.setText(Array_fileName3.get(2));

                                            //visual for card 4
                                            Glide.with(ResourceViewerView2.this).load(Array_thumbnail3.get(3)).into(recentlyAdded4);
                                            txtRecentlyAdded4.setText(Array_fileName3.get(3));
                                        }


                                    }

                                    else {
                                        Loading3.setVisibility(View.GONE);
                                        horizontalScrollView3.setVisibility(View.GONE);
                                        noRecentlyAdded.setVisibility(View.VISIBLE);
                                    }


                                }
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
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(ResourceViewerView2.this);
        DefaultRetryPolicy retryPolicy = new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);
        requestQueue.add(stringRequest);

        Array_fileDescription3.clear();
        Array_fileURL3.clear();
        Array_thumbnail3.clear();
        Array_fileName3.clear();
        Array_resID3.clear();
    }

}