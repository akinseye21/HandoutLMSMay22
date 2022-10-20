package com.example.handoutlms;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
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

public class Login extends AppCompatActivity {
    Button login;
    EditText email, pass;
    String gotten_email, gotten_pass;
    ProgressBar progressBar;
    ImageView back;
    LinearLayout signup;
    TextView forgotPassword;

    SharedPreferences preferences;
    String sent_from = "Login";

    public static final String LOGIN = "http://handout.com.ng/handouts/handout_login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), LoginSignup.class);
                startActivity(i);
            }
        });

        signup = findViewById(R.id.signup);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), NewSignup.class);
                startActivity(i);
            }
        });

        forgotPassword = findViewById(R.id.forgot_password);
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ForgotPassword1.class);
                startActivity(i);
            }
        });

        email = findViewById(R.id.edtemail);
        pass = findViewById(R.id.edtpassword);
        progressBar = findViewById(R.id.progressBar);

        preferences = getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        final SharedPreferences.Editor myEdit = preferences.edit();

        login = findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotten_email = email.getText().toString();
                gotten_pass = pass.getText().toString();

                //pass email and string to server endpoint
                progressBar.setVisibility(View.VISIBLE);

                StringRequest stringRequest = new StringRequest(Request.Method.POST, LOGIN,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                progressBar.setVisibility(View.GONE);

                                System.out.println("Login Response = "+response);

                                try{
                                    JSONObject jsonObject = new JSONObject(response);

                                    String status = jsonObject.getString("status");
                                    String email = jsonObject.getString("email");



                                    if(status.equals("login successful")){
                                        Toast.makeText(Login.this, "Login Successful", Toast.LENGTH_LONG).show();
                                        String login_email = jsonObject.getString("email");
                                        String fullname = jsonObject.getString("fullname");
                                        String pics = jsonObject.getString("pics");
                                        myEdit.putString("email", login_email);
                                        myEdit.putString("fullname", fullname);
                                        myEdit.putString("pics", pics);
                                        myEdit.commit();
                                        Intent i = new Intent(Login.this, FeedsDashboard.class);
                                        i.putExtra("email", email);
                                        i.putExtra("sent from", sent_from);
                                        startActivity(i);
                                    }else{
                                        Toast.makeText(getApplicationContext(), "Login failed. Please try again", Toast.LENGTH_LONG).show();
                                    }
                                }
                                catch (JSONException e){
                                    e.printStackTrace();
                                    Toast.makeText(getApplicationContext(), "Login failed. Please try again", Toast.LENGTH_LONG).show();
                                }

                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {

                                if(volleyError == null){
                                    return;
                                }

                                progressBar.setVisibility(View.GONE);
//                                    Toast.makeText(getContext(),  volleyError.getMessage(), Toast.LENGTH_LONG).show();
                                System.out.println("Error");
//                                    NetworkResponse statusCode = volleyError.networkResponse;
//                                    System.out.println("Codigo " + statusCode);
                            }
                        }){
                    @Override
                    protected Map<String, String> getParams(){
                        Map<String, String> params = new HashMap<>();
                        params.put("email", gotten_email);
                        params.put("password", gotten_pass);
                        return params;
                    }
                };

                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                requestQueue.add(stringRequest);

            }
        });
    }
}
