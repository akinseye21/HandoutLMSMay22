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
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Subjects extends AppCompatActivity {

    ImageView back;
    TextView subject;
    ListView listView;
    String category;
    ProgressBar progressBar;

    ArrayList<String> Array_subjects = new ArrayList<>();

    public static final String SUBJECTS = "https://handoutng.com/handouts/handout_examtype_course";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subjects);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Intent i = getIntent();
        category = i.getStringExtra("category");

        subject = findViewById(R.id.sub);
        back = findViewById(R.id.back);
        listView = findViewById(R.id.listview);
        progressBar = findViewById(R.id.progressBar);

        subject.setText(category);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        //get the subjects in the category
        StringRequest stringRequest = new StringRequest(Request.Method.POST, SUBJECTS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("Profile = "+response);

                        try{
                            JSONArray jsonArray = new JSONArray(response);
                            int len = jsonArray.length();

                            for(int i=0; i<len; i++){
                                JSONObject resp = jsonArray.getJSONObject(i);
                                String subject = resp.getString("course");

                                Array_subjects.add(subject);
                            }

                            SubjectAdapter subjectAdapter = new SubjectAdapter(getApplicationContext(), Array_subjects, category);
                            listView.setAdapter(subjectAdapter);
                            progressBar.setVisibility(View.GONE);

                        }
                        catch (JSONException e){
                            e.printStackTrace();
                            progressBar.setVisibility(View.GONE);
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        volleyError.printStackTrace();
                        progressBar.setVisibility(View.GONE);
                    }
                }){
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("examtype", category);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(Subjects.this);
        requestQueue.add(stringRequest);
    }
}