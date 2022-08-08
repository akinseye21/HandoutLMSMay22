package com.example.handoutlms;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
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

public class ForgotPassword3 extends AppCompatActivity {

    ImageView back;
    EditText password, confrimPassword;
    ProgressBar progressBar;
    String email;
    Button submit;

    public static final String UPDATE_PASSWORD = "http://handout.com.ng/handouts/handout_update_password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password3);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Intent i = getIntent();
        email = i.getStringExtra("email");

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ForgotPassword2.class);
                startActivity(i);
            }
        });

        password = findViewById(R.id.edtpassword);
        confrimPassword = findViewById(R.id.edtconfirmpassword);
        progressBar = findViewById(R.id.progressBar);
        submit = findViewById(R.id.submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(password.getText().toString().trim().equals(confrimPassword.getText().toString())){
                    progressBar.setVisibility(View.VISIBLE);
                    //send to DB
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, UPDATE_PASSWORD,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    JSONObject jsonObject;
                                    try {
                                        jsonObject = new JSONObject(response);
                                        String status = jsonObject.getString("status");

                                        if(status.equals("reset successful")){
                                            progressBar.setVisibility(View.GONE);
                                            //send to next
                                            Intent i = new Intent(getApplicationContext(), Login.class);
                                            startActivity(i);
                                        }else{
                                            progressBar.setVisibility(View.GONE);
                                            Toast.makeText(getApplicationContext(), "Sorry! Reset was unsuccessful, try again", Toast.LENGTH_LONG).show();
                                        }

                                    } catch (JSONException e) {
                                        progressBar.setVisibility(View.GONE);
                                        e.printStackTrace();
                                    }

                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(getApplicationContext(), "Network connectivity problem", Toast.LENGTH_LONG).show();
                                }
                            }){
                        @Override
                        protected Map<String, String> getParams(){
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("email", email);
                            params.put("password", password.getText().toString().trim());
                            return params;
                        }
                    };
                    RequestQueue requestQueue = Volley.newRequestQueue(ForgotPassword3.this);
                    requestQueue.add(stringRequest);
                }
                else{
                    password.setError("Password do not match");
                    confrimPassword.setError("Password do not match");
                }
            }
        });
    }
}
