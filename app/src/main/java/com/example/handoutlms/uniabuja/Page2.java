package com.example.handoutlms.uniabuja;

import static java.security.AccessController.getContext;

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
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.handoutlms.R;
import com.example.handoutlms.activities.FeedsDashboard;
import com.example.handoutlms.activities.Login;
import com.example.handoutlms.activities.VideoLinks;
import com.example.handoutlms.adapters.HomeListViewAdapter;
import com.example.handoutlms.adapters.UniAbujaAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class Page2 extends AppCompatActivity {

    SharedPreferences preferences;
    String fullname, pics;
    ImageView back;
    TextView txtfullname;
    CircleImageView pp;

    Intent i;
    String level, faculty, department, uniname;
    TextView txtlevel, txtfaculty, txtdepartment;
    GridView gridView;

    ArrayList<String> arr_courses = new ArrayList();
    ArrayList<String> arr_images = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page2);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        preferences = getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        fullname = preferences.getString("fullname", "");
        pics = preferences.getString("pics", "");

        txtfullname = findViewById(R.id.fullname);
        txtfullname.setText("Hi, "+fullname);
        pp = findViewById(R.id.pp);
        Glide.with(Page2.this).load(pics).into(pp);

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Page2.this, Page1.class);
                i.putExtra("uniname", uniname);
                startActivity(i);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        Dialog myDialog = new Dialog(Page2.this);
        myDialog.setContentView(R.layout.custom_popup_login_loading);
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView text = myDialog.findViewById(R.id.text);
        text.setText("Fetching courses, please wait...");
        myDialog.setCanceledOnTouchOutside(false);
        myDialog.show();

        i = getIntent();
        level = i.getStringExtra("level");
        faculty = i.getStringExtra("faculty");
        department = i.getStringExtra("department");
        gridView = findViewById(R.id.gridview);
        uniname = i.getStringExtra("uniname");

        txtlevel = findViewById(R.id.level);
        txtfaculty = findViewById(R.id.faculty);
        txtdepartment = findViewById(R.id.department);
        txtlevel.setText(level);
        txtfaculty.setText(faculty);
        txtdepartment.setText(department);

        //get the images
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://handoutng.com/handouts/handout_get_courses",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try{
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String course = jsonObject.getString("course");
                                String image = jsonObject.getString("image");

                                arr_courses.add(course);
                                arr_images.add(image);
                            }

                            //populate values on the gridview
                            UniAbujaAdapter uniAbujaAdapter = new UniAbujaAdapter(Page2.this, arr_courses, arr_images, level, faculty, department, uniname);
                            gridView.setAdapter(uniAbujaAdapter);
                            myDialog.dismiss();
                        }
                        catch (JSONException e){
                            e.printStackTrace();
                            myDialog.dismiss();
                            Toast.makeText(Page2.this, "No categories in the selected department, please check back", Toast.LENGTH_SHORT).show();
                            onBackPressed();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(Page2.this, "No categories in the selected department, please check back", Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    }
                }){
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("university", uniname);
                params.put("level", level);
                params.put("faculty", faculty);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        DefaultRetryPolicy retryPolicy = new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);
        requestQueue.add(stringRequest);
        requestQueue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
            @Override
            public void onRequestFinished(Request<Object> request) {
                requestQueue.getCache().clear();
            }
        });


        arr_courses.clear();
        arr_images.clear();
    }
}