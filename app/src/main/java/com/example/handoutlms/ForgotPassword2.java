package com.example.handoutlms;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

public class ForgotPassword2 extends AppCompatActivity {

    ImageView back;
    EditText edt1, edt2, edt3, edt4;
    Button verify;
    String email, code;
    ProgressBar progressBar;

    public static final String VALIDATE_OTP = "http://35.84.44.203/handouts/handout_validate_otp";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password2);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Intent i = getIntent();
        email = i.getStringExtra("email");
        code = i.getStringExtra("code");


        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ForgotPassword1.class);
                startActivity(i);
            }
        });

        progressBar = findViewById(R.id.progressBar);

        edt1 = findViewById(R.id.edt1);
        edt2 = findViewById(R.id.edt2);
        edt3 = findViewById(R.id.edt3);
        edt4 = findViewById(R.id.edt4);
        verify = findViewById(R.id.verify);

        edt1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (edt1.getText().toString().length()==1){
                    edt2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edt2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (edt2.getText().toString().length()==1){
                    edt3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edt3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (edt3.getText().toString().length()==1){
                    edt4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressBar.setVisibility(View.VISIBLE);
                //verify OTP
                StringRequest stringRequest = new StringRequest(Request.Method.POST, VALIDATE_OTP,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                JSONObject jsonObject;
                                try {
                                    jsonObject = new JSONObject(response);
                                    String status = jsonObject.getString("status");

                                    if(status.equals("reset")){
                                        progressBar.setVisibility(View.GONE);
                                        //send to next
                                        Intent i = new Intent(getApplicationContext(), ForgotPassword3.class);
                                        i.putExtra("email", email);
                                        startActivity(i);
                                    }else{
                                        progressBar.setVisibility(View.GONE);
                                        Toast.makeText(getApplicationContext(), "Sorry! Password can not be reset", Toast.LENGTH_LONG).show();
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
                        params.put("code", code);
                        return params;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(ForgotPassword2.this);
                requestQueue.add(stringRequest);

            }
        });
    }
}
