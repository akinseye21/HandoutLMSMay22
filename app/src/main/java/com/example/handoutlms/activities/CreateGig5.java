package com.example.handoutlms.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
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
import com.example.handoutlms.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CreateGig5 extends AppCompatActivity {

    ImageView back;

    String projectName, projectDescription, paymentMode, budgetCategory, projectType, startDate, endDate;
    ArrayList<String> Array_requiredSkills;
    int len_skill;
    String all_skills = "";

    TextView txt_prjname, txt_prjdesc, txt_projecttyp, txt_budgetcat;
    ProgressBar progressBar;
    LinearLayout next;
    SharedPreferences preferences;
    Dialog myDialog;
    Button home;

    RecyclerView recyclerView;
    Adapter madapter;
    String CHANNEL_ID = "channelID6";

    public static final String CREATE_GIGS = "http://handoutng.com/handouts/handout_create_gig";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_gig5);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Intent i = getIntent();
        projectName = i.getStringExtra("Project name");
        projectDescription = i.getStringExtra("Project description");
        Array_requiredSkills  = i.getExtras().getStringArrayList("Required skills");
        paymentMode = i.getStringExtra("Payment mode");
        budgetCategory = i.getStringExtra("Budget category");
        projectType = i.getStringExtra("Project type");
        startDate = i.getStringExtra("Start date");
        endDate = i.getStringExtra("End date");

        preferences = getApplicationContext().getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        final String got_email = preferences.getString("email", "not available");

        len_skill = Array_requiredSkills.size();

        LinearLayoutManager layoutManager= new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false);
        recyclerView = findViewById(R.id.recycler_view);

        txt_prjname = findViewById(R.id.project_name);
        txt_prjdesc = findViewById(R.id.project_desc);
        txt_projecttyp = findViewById(R.id.project_type);
        txt_budgetcat = findViewById(R.id.budget_category);
        next = findViewById(R.id.next);
        progressBar = findViewById(R.id.progressBar);


        final StringBuilder sb = new StringBuilder(all_skills);
        for (int j=0; j<len_skill; j++){

            if (j == len_skill - 1){
                sb.append(Array_requiredSkills.get(j));
            }else{
                sb.append(Array_requiredSkills.get(j)+", ");
            }
        }

        //populate values  of skill on the gridview
        madapter = new Adapter(Array_requiredSkills);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(madapter);

        txt_prjname.setText(projectName);
        txt_prjdesc.setText(projectDescription);
        txt_projecttyp.setText(projectType);
        txt_budgetcat.setText(budgetCategory);

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                // send gig to DB
                StringRequest stringRequest = new StringRequest(Request.Method.POST, CREATE_GIGS,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                System.out.println("Create Gig Response = "+response);

                                progressBar.setVisibility(View.GONE);

                                JSONObject jo = null;
                                try {
                                    jo = new JSONObject(response);
                                    String status = jo.getString("status");

                                    if (status.equals("successful")){
                                        createNotificationChannel();
                                        //load the custom dialog box
                                        myDialog = new Dialog(CreateGig5.this);
                                        myDialog.setContentView(R.layout.custom_popup1);
                                        home = myDialog.findViewById(R.id.home);
                                        home.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent i = new Intent(getApplicationContext(), FeedsDashboard.class);
                                                i.putExtra("email", got_email);
                                                i.putExtra("sent from", "gig created");
                                                startActivity(i);
                                            }
                                        });
                                        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                        myDialog.setCanceledOnTouchOutside(false);
                                        myDialog.show();
                                    }
                                } catch (JSONException e) {
                                    progressBar.setVisibility(View.GONE);
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(getApplicationContext(), "Error!", Toast.LENGTH_LONG).show();
                                volleyError.printStackTrace();
                            }
                        }){
                    @Override
                    protected Map<String, String> getParams(){
                        Map<String, String> params = new HashMap<>();
                        params.put("gig_name", projectName);
                        params.put("short_description", projectDescription);
                        params.put("skills", String.valueOf(sb));
                        params.put("payment_mode", paymentMode);
                        params.put("budget_category", budgetCategory);
                        params.put("project_type", projectType);
                        params.put("starts", startDate);
                        params.put("ends", endDate);
                        params.put("email", got_email);
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

    class Adapter extends RecyclerView.Adapter<Adapter.MyHolder> {
        ArrayList<String> named;

        public Adapter(ArrayList<String> named) {
            this.named = named;
        }

        @NonNull
        @Override
        public Adapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(CreateGig5.this).inflate(R.layout.list_gig_category, parent, false);
            return new Adapter.MyHolder(view);
        }



        @Override
        public void onBindViewHolder(@NonNull Adapter.MyHolder holder, @SuppressLint("RecyclerView") int position) {
            holder.name.setText(named.get(position));
        }

        @Override
        public int getItemCount() {
            return named.size();
        }

        class MyHolder extends RecyclerView.ViewHolder {
            TextView name;

            public MyHolder(@NonNull View itemView) {
                super(itemView);
                name = itemView.findViewById(R.id.category_name);
            }
        }

    }

    private void createNotificationChannel() {
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = manager.getNotificationChannel(CHANNEL_ID);
            if(channel == null){
                channel = new NotificationChannel(CHANNEL_ID, "Gig Created", NotificationManager.IMPORTANCE_DEFAULT);
                //config notification channel
                channel.setDescription("[Channel Description]");
                channel.enableVibration(true);
                channel.enableLights(true);
                channel.setVibrationPattern(new long[]{100, 1000, 200, 340});
                channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
                manager.createNotificationChannel(channel);
            }
        }
        Intent notificationIntent = new Intent(CreateGig5.this, VideoCreatorView.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent penIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.logo)
                .setStyle(new NotificationCompat.BigTextStyle())
                .setContentTitle("Gig Created")
                .setContentText("You have successfully created a gig - \""+projectName+"\"")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(false)
                .setTicker("Notification");
        builder.setContentIntent(penIntent);
        NotificationManagerCompat m = NotificationManagerCompat.from(CreateGig5.this);
        m.notify(7, builder.build());
    }

}
