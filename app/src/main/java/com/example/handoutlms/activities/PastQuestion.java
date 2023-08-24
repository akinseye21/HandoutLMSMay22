package com.example.handoutlms.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.handoutlms.R;
import com.example.handoutlms.adapters.NigeriaExamAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PastQuestion extends AppCompatActivity {

    GridView gridView;

    ArrayList<String> Array_examType = new ArrayList<>();
    ProgressBar progressBar;
    ImageView back;

    public static final String NIGERIA_EXAMS = "https://handoutng.com/handouts/handout_examtype";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_question);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //get sharedpreference
        SharedPreferences sp = getSharedPreferences("LoginDetails", MODE_PRIVATE);
        final String email = sp.getString("email", "");

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PastQuestion.this, FeedsDashboard.class);
                i.putExtra("email", email);
                i.putExtra("sent from", "trivia");
                startActivity(i);
            }
        });
        gridView = findViewById(R.id.simpleGridView);
        progressBar = findViewById(R.id.progressBar);

        //get Nigerian Exams
        StringRequest stringRequest = new StringRequest(Request.Method.GET, NIGERIA_EXAMS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            int len = jsonArray.length();

                            for(int i =0; i<len; i++){
                                JSONObject myResp = jsonArray.getJSONObject(i);
                                String examType = myResp.getString("examType");

                                Array_examType.add(examType);
                            }


                            //populate values on the gridview
                            NigeriaExamAdapter nigeriaExamAdapter = new NigeriaExamAdapter(getApplicationContext(), Array_examType);
                            gridView.setAdapter(nigeriaExamAdapter);

                            progressBar.setVisibility(View.GONE);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressBar.setVisibility(View.GONE);
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        progressBar.setVisibility(View.GONE);
                    }
                }){
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(PastQuestion.this);
        DefaultRetryPolicy retryPolicy = new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);
        requestQueue.add(stringRequest);
    }
}