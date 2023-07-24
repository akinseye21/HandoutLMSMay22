package com.example.handoutlms.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.handoutlms.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class TutorOrStudent extends AppCompatActivity {
    CheckBox tutor, student;
    Button next;
    String email;
    String usertype;
    String sent_from = "Register";

    Dialog myDialog;


    public static final String UPDATE = "http://handoutng.com/handouts/handout_usertype";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_or_student);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Intent i = getIntent();
        email = i.getStringExtra("email");

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

                    myDialog = new Dialog(TutorOrStudent.this);
                    myDialog.setContentView(R.layout.custom_popup_login_loading);
                    TextView text = myDialog.findViewById(R.id.text);
                    text.setText("Updating user, please wait");
                    myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    myDialog.setCanceledOnTouchOutside(false);
                    myDialog.show();

                    //update profile
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, UPDATE,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

                                    System.out.println("Response = "+response);
//                                    Toast.makeText(getApplicationContext(), "Response = "+response, Toast.LENGTH_LONG).show();



                                    try{
                                        JSONObject jsonObject = new JSONObject(response);

                                        String status = jsonObject.getString("status");
                                        if(status.equals("success")){
                                            myDialog.dismiss();
                                            //go to dashboard
                                            Intent i = new Intent(TutorOrStudent.this, FeedsDashboard.class);
                                            i.putExtra("email", email);
                                            i.putExtra("sent from", sent_from);
                                            startActivity(i);
                                        }else{
                                            myDialog.dismiss();
                                            Toast.makeText(TutorOrStudent.this, "Updating failed", Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                    catch (JSONException e){
                                        myDialog.dismiss();
                                        Toast.makeText(TutorOrStudent.this, "Error", Toast.LENGTH_SHORT).show();
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
                                    myDialog.dismiss();
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


                    RequestQueue requestQueue = Volley.newRequestQueue(TutorOrStudent.this);
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
                else{
                    //prompt user to check a box
                    Toast.makeText(getApplicationContext(), "Please check a box", Toast.LENGTH_LONG).show();
                }

            }
        });
    }
}
