package com.example.handoutlms.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.handoutlms.R;
import com.example.handoutlms.adapters.RemoteJobAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import de.hdodenhof.circleimageview.CircleImageView;

public class RemotePage2 extends AppCompatActivity {

    ImageView back, plus;
    CircleImageView pp;
    TextView username, useremail;
    MultiAutoCompleteTextView search;
    ListView listView;
    SwipeRefreshLayout swipeRefreshLayout;
    LinearLayout loading;
    String email, fullname;
    SharedPreferences preferences;

    ArrayList<String> arr_date = new ArrayList<>();
    ArrayList<String> arr_company = new ArrayList<>();
    ArrayList<String> arr_company_logo = new ArrayList<>();
    ArrayList<String> arr_position = new ArrayList<>();
    ArrayList<String> arr_logo = new ArrayList<>();
    ArrayList<String> arr_description = new ArrayList<>();
    ArrayList<String> arr_location = new ArrayList<>();
    ArrayList<String> arr_salary_min = new ArrayList<>();
    ArrayList<String> arr_salary_max = new ArrayList<>();
    ArrayList<String> arr_url = new ArrayList<>();
    ArrayList<String> arr_apply_url = new ArrayList<>();
    ArrayList<String> arr_tags = new ArrayList<>();

    public static final String USER_PROFILE = "https://handoutng.com/handouts/handout_get_user_profile";
    public static final String REMOTE_WORK = "https://handoutng.com/handouts/jobs.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remote_page2);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Intent i = getIntent();
        email = i.getStringExtra("email");

        preferences = getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        fullname = preferences.getString("fullname", "not available");

        back = findViewById(R.id.back);
        plus = findViewById(R.id.plus);
        pp = findViewById(R.id.pp);
        username = findViewById(R.id.username);
        useremail = findViewById(R.id.useremail);
        search = findViewById(R.id.auto2);
        listView = findViewById(R.id.my_list);
        swipeRefreshLayout = findViewById(R.id.swiperefresh);
        loading = findViewById(R.id.loading);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        useremail.setText(email);
        username.setText(fullname);

        load();
        getRemoteWork();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                listView.setVisibility(View.GONE);
                loading.setVisibility(View.VISIBLE);
                getRemoteWork();
            }
        });
    }

    private void getRemoteWork() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, REMOTE_WORK,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            int ArrayLength = jsonArray.length();

                            for (int i = 0; i < ArrayLength; i++) {
                                JSONObject section = jsonArray.getJSONObject(i);

                                String date = section.getString("date");
                                String company = section.getString("company");
                                String company_logo = section.getString("company_logo");
                                String position = section.getString("position");
                                String logo = section.getString("logo");
                                String description = section.getString("description");
                                String location = section.getString("location");
                                String salary_min = section.getString("salary_min");
                                String salary_max = section.getString("salary_max");
                                String url = section.getString("url");
                                String apply_url = section.getString("apply_url");
                                String tags = section.getString("tags");

                                JSONArray ja = new JSONArray(tags);
                                for (int j=0; j< ja.length(); j++){
                                    String me = (String) ja.get(j);
                                    arr_tags.add(me);

                                    for (int k=0; k<arr_tags.size(); k++){
                                        //REMOVE DUPLICATE ITEMS
                                        // create a stream from arraylist
                                        Stream<String> stream = arr_tags.stream();
                                        // call the distinct() of Stream
                                        // to remove duplicate elements
                                        stream = stream.distinct();
                                        // convert the stream to arraylist
                                        arr_tags = (ArrayList<String>)stream.collect(Collectors.toList());
                                    }
                                }

                                arr_date.add(date);
                                arr_company.add(company);
                                arr_company_logo.add(company_logo);
                                arr_position.add(position);
                                arr_logo.add(logo);
                                arr_description.add(description);
                                arr_location.add(location);
                                arr_salary_min.add(salary_min);
                                arr_salary_max.add(salary_max);
                                arr_url.add(url);
                                arr_apply_url.add(apply_url);
                            }
                            //populate values on the listview
                            RemoteJobAdapter remoteJobAdapter = new RemoteJobAdapter(RemotePage2.this, arr_date, arr_company, arr_company_logo, arr_position, arr_logo, arr_description, arr_location, arr_salary_min, arr_salary_max, arr_url, arr_apply_url, arr_tags);
                            listView.setAdapter(remoteJobAdapter);
                            listView.setVisibility(View.VISIBLE);
                            //hide progressBar and progressText
                            loading.setVisibility(View.GONE);

                        } catch (JSONException e) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String status = jsonObject.getString("status");
                                if(status.equals("no group")){
                                    //hide progressBar and progressText
                                    loading.setVisibility(View.GONE);
                                    swipeRefreshLayout.setVisibility(View.GONE);
                                }
                            } catch (JSONException ex) {
                                ex.printStackTrace();
                            }
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(RemotePage2.this);
        DefaultRetryPolicy retryPolicy = new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);
        requestQueue.add(stringRequest);
        requestQueue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
            @Override
            public void onRequestFinished(Request<Object> request) {
                requestQueue.getCache().clear();
            }
        });

        swipeRefreshLayout.setRefreshing(false);
    }

    private void load() {
        //get user profile
        StringRequest stringRequest = new StringRequest(Request.Method.POST, USER_PROFILE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject profile = new JSONObject(response);
                            String got_pics = profile.getString("pics");

                            if(got_pics.equals("")){
                                //do nothing
                            }else{
                                Glide
                                        .with(RemotePage2.this)
                                        .load(got_pics)
                                        .override(600,200)
                                        .fitCenter()
                                        .into(pp);
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
                params.put("email", email);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(RemotePage2.this);
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