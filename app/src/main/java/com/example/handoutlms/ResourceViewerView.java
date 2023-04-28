package com.example.handoutlms;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ResourceViewerView extends AppCompatActivity {

    ImageView back;
    TextView groupName;
    GridView gridView;
    TextView fullname, noresource;
    LinearLayout lin_loading;
    String name, category, description, status, id;
    String name_got;
    String fullnamed;
    SharedPreferences preferences;

    ArrayList<String> arr_resID = new ArrayList<>();
    ArrayList<String> arr_fileURL = new ArrayList<>();
    ArrayList<String> arr_fileDesc = new ArrayList<>();
    ArrayList<String> arr_fileName = new ArrayList<>();
    ArrayList<String> arr_thumbnail = new ArrayList<>();

    public static final String PARTICULAR_TUTORIAL = "https://handoutng.com/handouts/handout_get_a_tutorial";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resource_viewer_view);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        preferences = getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        fullnamed = preferences.getString("fullname", "");

        Intent i = getIntent();
        name = i.getStringExtra("name");
        category = i.getStringExtra("category");
        description = i.getStringExtra("description");
        status = i.getStringExtra("status");
        id  = i.getStringExtra("id");

        back = findViewById(R.id.back);
        groupName = findViewById(R.id.groupName);
        gridView = findViewById(R.id.gridview);
        fullname = findViewById(R.id.fullname);
        lin_loading = findViewById(R.id.lin_loading);
        noresource = findViewById(R.id.noresource);

        fullname.setText(fullnamed);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        //get for a particular group
        StringRequest stringRequest = new StringRequest(Request.Method.POST, PARTICULAR_TUTORIAL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("Response for particular tutorial= "+response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            name_got = jsonObject.getString("groupname");
                            groupName.setText(name_got);
                            String tutorial_resource = jsonObject.getString("tutorial_resource");
                            JSONArray jsonArray = new JSONArray(tutorial_resource);
                            int len = jsonArray.length();
                            if (len<1){
                                noresource.setVisibility(View.VISIBLE);
                                lin_loading.setVisibility(View.GONE);
                                gridView.setVisibility(View.GONE);
                            }else{

                                noresource.setVisibility(View.GONE);
                                lin_loading.setVisibility(View.GONE);
                                gridView.setVisibility(View.VISIBLE);

                                for (int j=0; j<len; j++){
                                    JSONObject newjson = jsonArray.getJSONObject(j);
                                    String resID = newjson.getString("resID");
                                    String fileURL = newjson.getString("fileURL");
                                    String fileDescription = newjson.getString("fileDescription");
                                    String fileName = newjson.getString("fileName");
                                    String thumbnail = newjson.getString("thumbnail");

                                    arr_resID.add(resID);
                                    arr_fileURL.add(fileURL);
                                    arr_fileDesc.add(fileDescription);
                                    arr_fileName.add(fileName);
                                    arr_thumbnail.add(thumbnail);
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        VideoLinkAdapter videoLinkAdapter = new VideoLinkAdapter(ResourceViewerView.this, name_got, arr_fileURL, arr_fileDesc, arr_thumbnail, arr_fileName, arr_resID, "viewer");
                        gridView.setAdapter(videoLinkAdapter);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        volleyError.printStackTrace();
                    }
                }){
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("groupname", name);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(ResourceViewerView.this);
        DefaultRetryPolicy retryPolicy = new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);
        requestQueue.add(stringRequest);
    }
}