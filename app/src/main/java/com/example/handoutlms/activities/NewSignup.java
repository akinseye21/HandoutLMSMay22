package com.example.handoutlms.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.handoutlms.R;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.database.DatabaseReference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NewSignup extends AppCompatActivity {

    ImageView back;
    Spinner institution;
    LinearLayout login;

    EditText fullname, emailaddress,  phonenum,  password, confirmpassword;
    String name, e_mail, phone, pass, confirmpass, school;

    ProgressBar progressBar;
    Button signup;
    Dialog myDialog;

    private ArrayList<String> inst2 = new ArrayList<>();

    SharedPreferences preferences;

    public static final String SIGNUP = "https://handoutng.com/handouts/handout_registration";
    public static final String GET_UNIVERSITY = "https://handoutng.com/handouts/handout_get_universities";

//    FirebaseAuth auth;
//    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_signup);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        preferences = getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        final SharedPreferences.Editor myEdit = preferences.edit();

        institution = findViewById(R.id.spinnerinstitution);
        fullname = findViewById(R.id.edtname);
        emailaddress = findViewById(R.id.edtemail);
        phonenum = findViewById(R.id.edtphonenumber);
        password = findViewById(R.id.edtpassword);
        confirmpassword = findViewById(R.id.edtconfirmpassword);
        progressBar = findViewById(R.id.progressBar);
        signup = findViewById(R.id.signup);

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), LoginSignup.class);
                startActivity(i);
            }
        });

        inst2.add("Select an institution");
        inst2.add("");

        //GET UNIVERSITY
        StringRequest stringRequest2 = new StringRequest(Request.Method.GET, GET_UNIVERSITY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            int ArrayLength = jsonArray.length();
                            for(int i=0; i<ArrayLength; i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String university = jsonObject.getString("university");

                                inst2.add(university);
                            }

                            String[] institutions = new String[inst2.size()];
                            for (int i = 0; i < inst2.size(); i++) {
                                institutions[i] = inst2.get(i);
                            }

                            ArrayAdapter<String> institutionadapter = new ArrayAdapter<>(NewSignup.this, R.layout.simple_spinner_small_whitebg, R.id.tx, institutions);
                            institution.setAdapter(institutionadapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }


                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        RequestQueue requestQueue2 = Volley.newRequestQueue(getApplicationContext());
        DefaultRetryPolicy retryPolicy2 = new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest2.setRetryPolicy(retryPolicy2);
        requestQueue2.add(stringRequest2);
        requestQueue2.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
            @Override
            public void onRequestFinished(Request<Object> request) {
                requestQueue2.getCache().clear();
            }
        });



        login = findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Login.class);
                startActivity(i);
            }
        });


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = fullname.getText().toString().trim();
                e_mail = emailaddress.getText().toString().trim();
                phone = phonenum.getText().toString().trim();
                pass = password.getText().toString().trim();
                confirmpass = confirmpassword.getText().toString().trim();
                school = institution.getSelectedItem().toString().trim();

                //check if fields are empty
                if(name.isEmpty()){
                    fullname.setError("Name can not be empty");
                }else{
                    if (!e_mail.contains("@")){
                        emailaddress.setError("wrong email");
                    }else{
                        if(phone.isEmpty()){
                            phonenum.setError("Phone number required");
                        }else{
                            if(pass.equals(confirmpass)){
                                if(school.equals("Select an institution") || school.equals("")){
                                    Toast.makeText(getApplicationContext(), "Please select an institution", Toast.LENGTH_LONG).show();
                                }else{
                                    if(pass.length()>=6){

                                        myDialog = new Dialog(NewSignup.this);
                                        myDialog.setContentView(R.layout.custom_popup_signing_up_loading);
                                        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                        myDialog.setCanceledOnTouchOutside(false);
                                        myDialog.show();


                                        StringRequest stringRequest = new StringRequest(Request.Method.POST, SIGNUP,
                                                new Response.Listener<String>() {
                                                    @Override
                                                    public void onResponse(String response) {

                                                        System.out.println("Signup Response = "+response);

                                                        try{
                                                            JSONObject jsonObject = new JSONObject(response);

                                                            String status = jsonObject.getString("status");
                                                            if(status.equals("successful")){

                                                                Toast.makeText(getApplicationContext(), "Registration Successful", Toast.LENGTH_LONG).show();
//
                                                                myEdit.putString( "email", e_mail);
                                                                myEdit.putString("fullname", name);
                                                                myEdit.putString("password", pass);
                                                                myEdit.commit();

                                                                myDialog.dismiss();

                                                                Intent i = new Intent(NewSignup.this, WelcomePage.class);
                                                                i.putExtra("fullname", name);
                                                                i.putExtra("email", e_mail);
                                                                i.putExtra("phone", phone);
                                                                i.putExtra("password", pass);
                                                                i.putExtra("school", school);
                                                                startActivity(i);

                                                            }else{
                                                                myDialog.dismiss();
                                                                Toast.makeText(NewSignup.this, "Signup failed, please try again", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                        catch (JSONException e){
                                                            e.printStackTrace();
                                                            myDialog.dismiss();

                                                            try {
                                                                JSONArray jsonArray = new JSONArray(response);
                                                                JSONObject jsonObject = jsonArray.getJSONObject(0);
                                                                String status = jsonObject.getString("status");
                                                                if(status.equals("User email exists")){
                                                                    Toast.makeText(NewSignup.this, "Signup failed. User email already exist", Toast.LENGTH_LONG).show();
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
                                                        myDialog.dismiss();
                                                        if(volleyError == null){
                                                            return;
                                                        }
                                                        System.out.println("Error = "+volleyError.getCause());
                                                    }
                                                }){
                                            @Override
                                            protected Map<String, String> getParams(){
                                                Map<String, String> params = new HashMap<>();
                                                params.put("fullname", name);
                                                params.put("email", e_mail);
                                                params.put("phone", phone);
                                                params.put("password", pass);
                                                params.put("institution", school);
                                                return params;
                                            }
                                        };

                                        RequestQueue requestQueue = Volley.newRequestQueue(NewSignup.this);
                                        DefaultRetryPolicy retryPolicy = new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                                        stringRequest.setRetryPolicy(retryPolicy);
                                        requestQueue.add(stringRequest);
                                        requestQueue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
                                            @Override
                                            public void onRequestFinished(Request<Object> request) {
                                                requestQueue.getCache().clear();
                                            }
                                        });

                                    }else{
                                        password.setError("Password must be greater than 5 characters");
                                    }
                                }
                            }else{
                                password.setError("Password do not match");
                                confirmpassword.setError("Password do not match");
                            }
                        }
                    }
                }
            }
        });
    }
}
