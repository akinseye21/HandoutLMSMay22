package com.example.handoutlms;

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
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CreateGig6 extends AppCompatActivity {

    ImageView back;

    String projectName, projectDescription, paymentMode, budgetCategory, projectType;
    ArrayList<String> Array_requiredSkills;
    int len_skill;
    String all_skills = "";

    TextView txt_prjname, txt_prjdesc, txt_projecttyp, txt_budgetcat;
    GridView mygrid;
    ProgressBar progressBar;
    Button next;
    SharedPreferences preferences;
    Dialog myDialog;
    Button home;

    public static final String CREATE_GIGS = "http://35.84.44.203/handouts/handout_create_user_gig";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_gig6);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Intent i = getIntent();
        projectName = i.getStringExtra("Project name");
        projectDescription = i.getStringExtra("Project description");
        Array_requiredSkills  = i.getExtras().getStringArrayList("Required skills");
        paymentMode = i.getStringExtra("Payment mode");
        budgetCategory = i.getStringExtra("Budget category");
        projectType = i.getStringExtra("Project type");

        preferences = getApplicationContext().getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        final String got_email = preferences.getString("email", "not available");

        len_skill = Array_requiredSkills.size();

        txt_prjname = findViewById(R.id.project_name);
        txt_prjdesc = findViewById(R.id.project_desc);
        txt_projecttyp = findViewById(R.id.project_type);
        txt_budgetcat = findViewById(R.id.budget_category);
        mygrid = findViewById(R.id.mygrid);
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

//        Snackbar.make(findViewById(android.R.id.content), "Skills = "+sb, Snackbar.LENGTH_LONG).show();

        //populate values  of skill on the gridview
        SimpleGigSetAdapter simpleGigSetAdapter = new SimpleGigSetAdapter(getApplicationContext(), Array_requiredSkills);
        mygrid.setAdapter(simpleGigSetAdapter);

        txt_prjname.setText(projectName);
        txt_prjdesc.setText(projectDescription);
        txt_projecttyp.setText(projectType);
        txt_budgetcat.setText(budgetCategory);

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), CreateGig4.class);
                i.putExtra("Project name", projectName);
                i.putExtra("Project description", projectDescription);
                i.putStringArrayListExtra("Required skills", Array_requiredSkills);
                i.putExtra("Payment mode", paymentMode);
                i.putExtra("Budget category", budgetCategory );
                i.putExtra("Project type", projectType );
                startActivity(i);
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
                                System.out.println("Response = "+response);

                                progressBar.setVisibility(View.GONE);

                                JSONObject jo = null;
                                try {
                                    jo = new JSONObject(response);
                                    String status = jo.getString("status");

                                    if (status.equals("successful")){
                                        //load the custom dialog box
                                        myDialog = new Dialog(CreateGig6.this);
                                        myDialog.setContentView(R.layout.custom_popup1);
                                        home = myDialog.findViewById(R.id.home);
                                        home.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent i = new Intent(getApplicationContext(), FeedsDashboard.class);
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
                        params.put("email", got_email);
                        return params;
                    }
                };

                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                requestQueue.add(stringRequest);
            }
        });
    }

    @Override
    public void onBackPressed() {
        // do nothing
    }
}
