package com.example.handoutlms;

import androidx.annotation.NonNull;
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

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

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

    FirebaseAuth auth;

    public static final String UPDATE_PASSWORD = "https://handoutng.com/handouts/handout_update_password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password3);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        auth = FirebaseAuth.getInstance();

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


                                            //update on firebase DB also
                                            auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
//                                                    loadingBar.dismiss();
                                                    if(task.isSuccessful())
                                                    {
                                                        // if isSuccessful then done message will be shown
                                                        // and you can change the password
                                                        //show dialog box to user to check email box or spam

                                                        Toast.makeText(ForgotPassword3.this,"Check your email for confirmation",Toast.LENGTH_LONG).show();

                                                        //send to next
                                                        Intent i = new Intent(getApplicationContext(), Login.class);
                                                        startActivity(i);
                                                    }
                                                    else {
                                                        Toast.makeText(ForgotPassword3.this,"Error Occurred",Toast.LENGTH_LONG).show();
                                                    }
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
//                                                    loadingBar.dismiss();
                                                    Toast.makeText(ForgotPassword3.this,"Error Failed",Toast.LENGTH_LONG).show();
                                                }
                                            });





                                        }else{
                                            progressBar.setVisibility(View.GONE);
                                            Toast.makeText(getApplicationContext(), "Sorry! Reset was unsuccessful, try again", Toast.LENGTH_LONG).show();
                                        }

                                    } catch (JSONException e) {
                                        Toast.makeText(getApplicationContext(), "Sorry! Reset was unsuccessful, try changing your password", Toast.LENGTH_LONG).show();
                                        password.setError("change password");
                                        confrimPassword.setError("change password");
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
                    DefaultRetryPolicy retryPolicy = new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                    stringRequest.setRetryPolicy(retryPolicy);
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
