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

public class ForgotPassword1 extends AppCompatActivity {

    ImageView back;
    EditText email;
    Button submit;
    ProgressBar progressBar;

    public static final String PASSWORD_RESET = "http://handout.com.ng/handouts/handout_reset_password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password1);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Login.class);
                startActivity(i);
            }
        });

        email = findViewById(R.id.edtemail);
        progressBar = findViewById(R.id.progressBar);

        submit = findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressBar.setVisibility(View.VISIBLE);
                //send inputted email to DB
                StringRequest stringRequest = new StringRequest(Request.Method.POST, PASSWORD_RESET,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                JSONObject jsonObject;
                                try {
                                    jsonObject = new JSONObject(response);
                                    String status = jsonObject.getString("status");
                                    String code = jsonObject.getString("code");

                                    if(status.equals("success")){
                                        progressBar.setVisibility(View.GONE);
                                        System.out.println("Code = "+code);
                                        //send to next
                                        Intent i = new Intent(getApplicationContext(), ForgotPassword2.class);
                                        i.putExtra("code", code);
                                        i.putExtra("email", email.getText().toString().trim());
                                        startActivity(i);
                                    }else{
                                        progressBar.setVisibility(View.GONE);
                                        email.setError("Please check your email again");
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
                        params.put("email", email.getText().toString().trim());
                        return params;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(ForgotPassword1.this);
                requestQueue.add(stringRequest);

            }
        });
    }
}
