package com.example.handoutlms.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.handoutlms.R;
import com.example.handoutlms.adapters.VideoLinkAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class VideoCreatorView extends AppCompatActivity {

    ImageView back;
    TextView groupName;
    GridView gridView;
    TextView noresources, fullname;
    LinearLayout uploadmore;
    LinearLayout lin_loading;
    LinearLayout noResources;
    SwipeRefreshLayout swipeRefreshLayout;

    String group_name, from;
    String classSize, type;
    String fullnamed, email;
    int ArrayLength, len2;
    String got_name, got_category, got_description, got_mode, got_id, got_date;
    Dialog myDialog;

    SharedPreferences preferences;

    ArrayList<String> Array_fileDescription = new ArrayList<>();
    ArrayList<String> Array_fileURL = new ArrayList<>();
    ArrayList<String> Array_thumbnail = new ArrayList<>();
    ArrayList<String> Array_fileName = new ArrayList<>();
    ArrayList<String> Array_resID = new ArrayList<>();

    public static final String ALL_TUTORIAL = "https://handoutng.com/handouts/handout_get_all_tutorials";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_creator_view);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Intent i = getIntent();
        group_name = i.getStringExtra("groupName");
        got_name = i.getStringExtra("name");
        got_category = i.getStringExtra("category");
        got_description = i.getStringExtra("description");
        got_mode = i.getStringExtra("mode");
        got_id = i.getStringExtra("id");
        got_date = i.getStringExtra("date");
        from = i.getStringExtra("from");

        preferences = getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        fullnamed = preferences.getString("fullname", "");
        email = preferences.getString("email", "");

        System.out.println("The group name = "+group_name);

        back = findViewById(R.id.back);
        groupName = findViewById(R.id.groupNAME);
        gridView = findViewById(R.id.gridview);
        noresources = findViewById(R.id.noresources);
        uploadmore = findViewById(R.id.uploadmore);
        groupName.setText(group_name);
        lin_loading = findViewById(R.id.lin_loading);
        fullname = findViewById(R.id.fullname);
        swipeRefreshLayout = findViewById(R.id.swiperefresh);
        noResources = findViewById(R.id.no_resource);


        fullname.setText(fullnamed);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (from.equals("justCreatedResources")){

                    //ask if you want to go back home
                    //show popUp to exit
                    myDialog = new Dialog(VideoCreatorView.this);
                    myDialog.setContentView(R.layout.custom_popup_exit);
                    TextView text1 = myDialog.findViewById(R.id.text1);
                    TextView text2 = myDialog.findViewById(R.id.text2);
                    text1.setText("");
                    text2.setText("Are you sure you want to go back home?");
                    Button yes = myDialog.findViewById(R.id.yes);
                    Button no = myDialog.findViewById(R.id.no);
                    yes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent i = new Intent(VideoCreatorView.this, FeedsDashboard.class);
                            i.putExtra("email", email);
                            i.putExtra("sent from", "online_tutorial");
                            startActivity(i);
                        }
                    });

                    no.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            myDialog.dismiss();
                        }
                    });
                    myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    myDialog.setCanceledOnTouchOutside(true);
                    myDialog.show();
                }else{
                    onBackPressed();
                }

            }
        });


        StringRequest stringRequest = new StringRequest(Request.Method.GET, ALL_TUTORIAL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("Response = "+response);

                        try{
                            JSONArray jsonArray = new JSONArray(response);
                            ArrayLength = jsonArray.length();

                            for(int j = ArrayLength - 1; j >= 0; j--){
                                JSONObject section1 = jsonArray.getJSONObject(j);
                                String tutName = section1.getString("groupname");
                                String tutResources = section1.getString("tutorial_resource");
                                classSize = section1.getString("class_size");
                                type = section1.getString("tutorial_type");

                                if(group_name.equals(tutName)){
                                    JSONArray jsonArray1 = new JSONArray(tutResources);
                                    len2 = jsonArray1.length();

                                    if (len2>=1){
                                        for(int k=0; k<=len2; k++){
                                            JSONObject section2 = jsonArray1.getJSONObject(k);
                                            String resID = section2.getString("resID");
                                            String fileUrl = section2.getString("fileURL");
                                            String filedescription = section2.getString("fileDescription");
                                            String thumbnail = section2.getString("thumbnail");
                                            String filename = section2.getString("fileName");

                                            System.out.println("fileurl = "+fileUrl+"\nfiledescription = "+filedescription);

                                            Array_fileURL.add(fileUrl);
                                            Array_fileDescription.add(filedescription);
                                            Array_thumbnail.add(thumbnail);
                                            Array_fileName.add(filename);
                                            Array_resID.add(resID);
                                        }
                                    }

                                    else {
                                        noResources.setVisibility(View.VISIBLE);
                                    }
                                }
                            }
                        }
                        catch (JSONException e){
                            e.printStackTrace();
                        }

                        if (Array_fileURL.isEmpty()){
                            lin_loading.setVisibility(View.GONE);
                            gridView.setVisibility(View.GONE);
                            noresources.setVisibility(View.VISIBLE);
                        }else{
                            lin_loading.setVisibility(View.GONE);
                            gridView.setVisibility(View.VISIBLE);
                            noresources.setVisibility(View.GONE);
                            VideoLinkAdapter videoLinkAdapter = new VideoLinkAdapter(VideoCreatorView.this, group_name, Array_fileURL, Array_fileDescription, Array_thumbnail, Array_fileName, Array_resID, "creator", got_mode);
                            gridView.setAdapter(videoLinkAdapter);
                        }
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
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(VideoCreatorView.this);
        DefaultRetryPolicy retryPolicy = new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);
        requestQueue.add(stringRequest);

        Array_fileDescription.clear();
        Array_fileURL.clear();
        Array_thumbnail.clear();

        uploadmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(VideoCreatorView.this, CreateOnlineTutPhase1.class);
                i.putExtra("Group_name", group_name);
                i.putExtra("name", fullnamed);
                i.putExtra("category", got_category);
                i.putExtra("description", got_description);
                i.putExtra("mode", got_mode);
                i.putExtra("id", got_id);
                i.putExtra("date", got_date);
                startActivity(i);
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                StringRequest stringRequest = new StringRequest(Request.Method.GET, ALL_TUTORIAL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                System.out.println("Response = "+response);

                                try{
                                    JSONArray jsonArray = new JSONArray(response);
                                    ArrayLength = jsonArray.length();

                                    for(int j = ArrayLength - 1; j >= 0; j--){
                                        JSONObject section1 = jsonArray.getJSONObject(j);
                                        String tutName = section1.getString("groupname");
                                        String tutResources = section1.getString("tutorial_resource");
                                        classSize = section1.getString("class_size");
                                        type = section1.getString("tutorial_type");

                                        if(group_name.equals(tutName)){
                                            JSONArray jsonArray1 = new JSONArray(tutResources);
                                            len2 = jsonArray1.length();

                                            for(int k=0; k<=len2; k++){
                                                JSONObject section2 = jsonArray1.getJSONObject(k);
                                                String fileUrl = section2.getString("fileURL");
                                                String filedescription = section2.getString("fileDescription");
                                                String thumbnail = section2.getString("thumbnail");
                                                String filename = section2.getString("fileName");
                                                String resID = section2.getString("resID");

                                                System.out.println("fileurl = "+fileUrl+"\nfiledescription = "+filedescription);

                                                Array_fileURL.add(fileUrl);
                                                Array_fileDescription.add(filedescription);
                                                Array_thumbnail.add(thumbnail);
                                                Array_fileName.add(filename);
                                                Array_resID.add(resID);
                                            }
                                        }
                                    }
                                }
                                catch (JSONException e){
                                    e.printStackTrace();
                                }

                                if (Array_fileURL.isEmpty()){
                                    lin_loading.setVisibility(View.GONE);
                                    gridView.setVisibility(View.GONE);
                                    noresources.setVisibility(View.VISIBLE);
                                }else{
                                    lin_loading.setVisibility(View.GONE);
                                    gridView.setVisibility(View.VISIBLE);
                                    noresources.setVisibility(View.GONE);
                                    VideoLinkAdapter videoLinkAdapter = new VideoLinkAdapter(VideoCreatorView.this, group_name, Array_fileURL, Array_fileDescription, Array_thumbnail, Array_fileName, Array_resID, "creator", got_mode);
                                    gridView.setAdapter(videoLinkAdapter);
                                }
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
                        return params;
                    }
                };

                RequestQueue requestQueue = Volley.newRequestQueue(VideoCreatorView.this);
                DefaultRetryPolicy retryPolicy = new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                stringRequest.setRetryPolicy(retryPolicy);
                requestQueue.add(stringRequest);

                Array_fileDescription.clear();
                Array_fileURL.clear();
                Array_thumbnail.clear();
                swipeRefreshLayout.setRefreshing(false);
            }
        });


    }
}