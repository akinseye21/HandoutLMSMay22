package com.example.handoutlms;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

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

public class TutorOrStudent extends AppCompatActivity {
    CheckBox tutor, student;
    Button next;
    String email;
    String usertype;

    SharedPreferences preferences;

    public static final String UPDATE = "http://35.84.44.203/handouts/handout_usertype";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_or_student);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        preferences = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        email = preferences.getString("email", "not available");

        tutor = findViewById(R.id.tutor);
        student = findViewById(R.id.student);
        next = findViewById(R.id.next);

        tutor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    student.setChecked(false);
                    usertype = "tutor";
                }
            }
        });
        student.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    tutor.setChecked(false);
                    usertype = "student";
                }
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(student.isChecked() || tutor.isChecked()){

                    //update profile
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, UPDATE,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

                                    System.out.println("Response = "+response);
                                    Toast.makeText(getApplicationContext(), "Response = "+response, Toast.LENGTH_LONG).show();



                                    try{
                                        JSONObject jsonObject = new JSONObject(response);

                                        String status = jsonObject.getString("status");
                                        if(status.equals("successful")){
                                            //go to dashboard
                                            Intent i = new Intent(TutorOrStudent.this, FeedsDashboard.class);
                                            startActivity(i);
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
                                    System.out.println("Error = "+volleyError.getMessage());
                                }
                            }){
                        @Override
                        protected Map<String, String> getParams(){
                            Map<String, String> params = new HashMap<>();
                            params.put("email", email);
                            params.put("usertype", usertype);
                            return params;
                        }
                    };


                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                    requestQueue.add(stringRequest);
                }
                else{
                    //prompt user to check a box
                    Toast.makeText(getApplicationContext(), "Please check a box", Toast.LENGTH_LONG).show();
                }

            }
        });
    }
}
