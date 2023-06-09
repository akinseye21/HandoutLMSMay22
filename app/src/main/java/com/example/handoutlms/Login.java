package com.example.handoutlms;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
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

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Logger;

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
    Dialog myDialog;

    SharedPreferences preferences;
    String sent_from = "Login";

    public static final String LOGIN = "https://handoutng.com/handouts/handout_login";

    String CHANNEL_ID = "channelID";

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        auth = FirebaseAuth.getInstance();


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
                gotten_email = email.getText().toString().trim();
                gotten_pass = pass.getText().toString().trim();

                myDialog = new Dialog(Login.this);
                myDialog.setContentView(R.layout.custom_popup_login_loading);
                myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                myDialog.setCanceledOnTouchOutside(false);
                myDialog.show();

                StringRequest stringRequest = new StringRequest(Request.Method.POST, LOGIN,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                try{
                                    JSONObject jsonObject = new JSONObject(response);

                                    String status = jsonObject.getString("status");
                                    String email = jsonObject.getString("email");

                                    if(status.equals("login successful")){

                                        String login_email = jsonObject.getString("email");
                                        String fullname = jsonObject.getString("fullname");
                                        String pics = jsonObject.getString("pics");
                                        myEdit.putString( "email", login_email);
                                        myEdit.putString("fullname", fullname);
                                        myEdit.putString("pics", pics);
                                        myEdit.putString("password", gotten_pass);
                                        myEdit.commit();

                                        auth.signInWithEmailAndPassword(gotten_email, gotten_pass)
                                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<AuthResult> task) {

                                                        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://handout-lms-default-rtdb.firebaseio.com/");
                                                        firebaseDatabase.setLogLevel(Logger.Level.DEBUG);

                                                        myDialog.dismiss();

                                                        if(task.isSuccessful()){

                                                            createNotificationChannel();

                                                            Toast.makeText(Login.this, "Login Successful", Toast.LENGTH_LONG).show();
                                                            Intent i = new Intent(Login.this, FeedsDashboard.class);
                                                            i.putExtra("email", email);
                                                            i.putExtra("sent from", sent_from);
                                                            startActivity(i);
                                                        }else{
                                                            Toast.makeText(getApplicationContext(), "Login failed1. Please try again", Toast.LENGTH_LONG).show();
                                                        }

                                                    }
                                                });


                                    }else{
                                        myDialog.dismiss();

                                        Toast.makeText(getApplicationContext(), "Login failed2. Please try again", Toast.LENGTH_LONG).show();
                                    }
                                }
                                catch (JSONException e){
                                    e.printStackTrace();
                                    myDialog.dismiss();
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

//                                progressBar.setVisibility(View.GONE);
                                myDialog.dismiss();
                                Toast.makeText(Login.this,  "Network Error", Toast.LENGTH_LONG).show();
                                System.out.println("Network Error "+volleyError);
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
                DefaultRetryPolicy retryPolicy = new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                stringRequest.setRetryPolicy(retryPolicy);
                requestQueue.add(stringRequest);

            }
        });
    }

    private void createNotificationChannel() {
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = manager.getNotificationChannel(CHANNEL_ID);
            if(channel == null){
                channel = new NotificationChannel(CHANNEL_ID, "Handout Login", NotificationManager.IMPORTANCE_DEFAULT);
                //config notification channel
                channel.setDescription("[Channel Description]");
                channel.enableVibration(true);
                channel.enableLights(true);
                channel.setVibrationPattern(new long[]{100, 1000, 200, 340});
                channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
                manager.createNotificationChannel(channel);
            }
        }
        Intent notificationIntent = new Intent(this, FeedsDashboard.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent penIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.logo)
                .setStyle(new NotificationCompat.BigTextStyle())
                .setContentTitle("Handout Login")
                .setContentText("You have successfully logged into your account")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(false)
                .setTicker("Notification");
        builder.setContentIntent(penIntent);
        NotificationManagerCompat m = NotificationManagerCompat.from(Login.this);
        m.notify(1, builder.build());
    }
}
