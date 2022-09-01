package com.example.handoutlms;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
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

public class AlmostDone extends AppCompatActivity {

    LinearLayout done;
    String group_name, category, date, time, university, description, tut_type, location, tutorial_mode, email;
    TextView gp_name, cat, dat, tim, uni, desc;
    CheckBox share;
//    SharedPreferences preferences;
    ProgressBar progressBar;
    ImageView back;
    TextView domlocation, reallocation;

    public static final String TUTORIAL_GROUP = "http://handout.com.ng/handouts/handout_tutorial_groups";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_almost_done);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //get sharedpreference
//        preferences = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
//        final String email = preferences.getString("email", "not available");

        //if the android version is greater than OREO
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel("My Notification", "My Notification", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }



        Intent i = getIntent();
        group_name = i.getStringExtra("group_name");
        category = i.getStringExtra("category");
        date = i.getStringExtra("date");
        time = i.getStringExtra("time");
        university = i.getStringExtra("university");
        description = i.getStringExtra("description");
        location = i.getStringExtra("location");
        email = i.getStringExtra("email");

        gp_name = findViewById(R.id.tutorial_group_name);
        cat = findViewById(R.id.category);
        dat = findViewById(R.id.date);
        tim = findViewById(R.id.time);
        uni = findViewById(R.id.university);
        desc = findViewById(R.id.description);
        share = findViewById(R.id.share);
        progressBar = findViewById(R.id.progressBar);
        back = findViewById(R.id.back);
        domlocation = findViewById(R.id.domlocation);
        reallocation = findViewById(R.id.location);

        if(location.equals("")){
            tutorial_mode = "online";
            domlocation.setVisibility(View.GONE);
            reallocation.setVisibility(View.GONE);
        }
        else if(!location.equals("")){
            tutorial_mode = "offline";
            reallocation.setText(location);
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        gp_name.setText(group_name);
        cat.setText(category);
        dat.setText(date);
        tim.setText(time);
        uni.setText(university);
        desc.setText(description);

        done = findViewById(R.id.next);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(share.isChecked()){
                    tut_type = "public";
                }else{
                    tut_type = "private";
                }




                if(!group_name.equals("") && !category.equals("") && !date.equals("") && !time.equals("") && !university.equals("") && !description.equals("")){
                    //send to server
                    progressBar.setVisibility(View.VISIBLE);

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, TUTORIAL_GROUP,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    progressBar.setVisibility(View.GONE);

                                    System.out.println("Response = "+response);

                                    try{
                                        JSONObject jsonObject = new JSONObject(response);

//                                        String signed_name = jsonObject.getString("fullname");
                                        String status = jsonObject.getString("status");
                                        String notification = jsonObject.getString("notification");
                                        System.out.println(response);

                                        if(status.equals("successful") && location.equals("")){
                                            Toast.makeText(getApplicationContext(), "Online Group created successfully", Toast.LENGTH_LONG).show();
                                            System.out.println(jsonObject);

//                                            NotificationGenerator notificationGenerator = new NotificationGenerator();
//                                            notificationGenerator.setString(notification);
//                                            NotificationGenerator.openActivityNotification(getApplicationContext());

                                            NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext())
                                                    .setSmallIcon(R.drawable.logo)
                                                    .setContentTitle("Handout LMS")
                                                    .setContentText(notification)
                                                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                                    // Set the intent that will fire when the user taps the notification
                                                    .setAutoCancel(true);

                                            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
                                            notificationManager.notify(9, builder.build());

                                            Intent i = new Intent(AlmostDone.this, CreateOnlineTutPhase1.class);
                                            i.putExtra("Group_name", group_name);
                                            i.putExtra("notification", notification);
                                            startActivity(i);

                                        }
                                        else if(status.equals("successful") && !location.equals("")){
                                            Toast.makeText(getApplicationContext(), "Offline Group created successfully", Toast.LENGTH_LONG).show();
                                            System.out.println(jsonObject);

                                            //show notification and add to notification array
//                                            NotificationGenerator notificationGenerator = new NotificationGenerator();
//                                            notificationGenerator.setString(notification);
//                                            NotificationGenerator.openActivityNotification(getApplicationContext());

                                            Intent intent = new Intent(getApplicationContext(), AddOptions.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_IMMUTABLE);

                                            NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext())
                                                    .setSmallIcon(R.drawable.logo)
                                                    .setContentTitle("Handout LMS")
                                                    .setContentText(notification)
                                                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                                    // Set the intent that will fire when the user taps the notification
                                                    .setContentIntent(pendingIntent)
                                                    .setAutoCancel(true);

                                            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
                                            notificationManager.notify(9, builder.build());

//                                            Intent i = new Intent(AlmostDone.this, AddOptions.class);
//                                            i.putExtra("email", email);
////                                            i.putExtra("notification", notification);
//                                            startActivity(i);

//                                            NotificationCompat.Builder builder = new NotificationCompat.Builder(AlmostDone.this)
//                                                    .setSmallIcon(R.drawable.notification)
//                                                    .setContentTitle("Handout LMS")
//                                                    .setContentText(notification)
//                                                    .setAutoCancel(true)
//                                                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);
//
//                                            Intent intent = new Intent(AlmostDone.this, Notification.class);
//                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                            intent.putExtra("notification", notification);
//
//                                            PendingIntent pendingIntent = PendingIntent.getActivity(AlmostDone.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//                                            builder.setContentIntent(pendingIntent);
//
//                                            NotificationManager manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
//                                            manager.notify(0, builder.build());
                                        }
                                        else{
                                            Toast.makeText(getApplicationContext(), "Group creation failed. Please try again", Toast.LENGTH_LONG).show();
                                            System.out.println("Error sending group info: "+jsonObject);
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

//                                    if(volleyError == null){
//                                        return;
//                                    }

                                    progressBar.setVisibility(View.GONE);
//                                    Toast.makeText(getContext(),  volleyError.getMessage(), Toast.LENGTH_LONG).show();
                                    System.out.println("Error = "+volleyError.getMessage());
                                }
                            }){
                        @Override
                        protected Map<String, String> getParams(){
                            Map<String, String> params = new HashMap<>();
                            params.put("email", email);
                            params.put("tutorial_group_name", group_name);
                            params.put("category", category);
                            params.put("tutorial_type", tut_type);
                            params.put("date", date);
                            params.put("time", time);
                            params.put("institution", university);
                            params.put("short_description", description);
                            params.put("tutorial_mode", tutorial_mode);
                            params.put("venue", location);
                            return params;
                        }
                    };

                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                    requestQueue.add(stringRequest);
                }else{
                    Toast.makeText(getApplicationContext(), "One or more field is empty.", Toast.LENGTH_LONG).show();
                }

            }
        });
    }
}
