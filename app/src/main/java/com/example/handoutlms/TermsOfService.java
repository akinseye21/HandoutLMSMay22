package com.example.handoutlms;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class TermsOfService extends AppCompatActivity {

    ImageView back;
    String from;
    Button accept, decline;
    TextView lastupdate, tos;
    ProgressBar progressBar;
    public static final String PRIVACY_POLICY = "https://handout.com.ng/handouts/handout_policy";
    public static final String TOS = "https://handout.com.ng/handouts/handout_tor";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_of_service);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Intent i = getIntent();
        from = i.getStringExtra("from");

        back = findViewById(R.id.back);
        accept = findViewById(R.id.accept);
        decline = findViewById(R.id.decline);
        progressBar = findViewById(R.id.progressBar);
        lastupdate = findViewById(R.id.lastupdate);
        tos = findViewById(R.id.tands);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), LoginSignup.class);
                startActivity(i);
            }
        });

        if(from.equals("privacy")){
            StringRequest stringRequest = new StringRequest(Request.Method.GET, PRIVACY_POLICY,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            int ArrayLength;
                            progressBar.setVisibility(View.GONE);
                            try{
                                JSONObject jsonObject = new JSONObject(response);
                                String privacy_policy = jsonObject.getString("policy");
                                String updated = jsonObject.getString("updated");


                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                                    tos.setText(Html.fromHtml(privacy_policy, Html.FROM_HTML_MODE_LEGACY));
                                } else {
                                    tos.setText(Html.fromHtml(privacy_policy));
                                }
                                lastupdate.setText(updated);

                            }
                            catch (JSONException e){
                                e.printStackTrace();
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                        }
                    }){
                @Override
                protected Map<String, String> getParams(){
                    Map<String, String> params = new HashMap<>();
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);
        }else if(from.equals("tos")){
            StringRequest stringRequest2 = new StringRequest(Request.Method.GET, TOS,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            int ArrayLength;
                            progressBar.setVisibility(View.GONE);
                            try{
                                JSONObject jsonObject = new JSONObject(response);
                                String tos2 = jsonObject.getString("tor");
                                String updated = jsonObject.getString("updated");

                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                                    tos.setText(Html.fromHtml(tos2, Html.FROM_HTML_MODE_LEGACY));
                                } else {
                                    tos.setText(Html.fromHtml(tos2));
                                }

                                lastupdate.setText(updated);
                            }
                            catch (JSONException e){
                                e.printStackTrace();
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                        }
                    }){
                @Override
                protected Map<String, String> getParams(){
                    Map<String, String> params = new HashMap<>();
                    return params;
                }
            };
            RequestQueue requestQueue2 = Volley.newRequestQueue(getApplicationContext());
            requestQueue2.add(stringRequest2);
        }


        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), NewSignup.class);
                startActivity(i);
            }
        });
        decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), FirstScreen.class);
                startActivity(i);
            }
        });
    }
}
