package com.example.handoutlms;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.commons.io.IOUtils;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class VideoLinks extends AppCompatActivity {

    ImageView back;
//    LinearLayout addlink;
//    ListView listView;
    String group_name, email;
    Button submit;
    Dialog myDialog, myDialog2;
    EditText videoName, description, link;
    TextView groupName, viewResources, title;
    String vidName, vidDesc, vidLink;
    String data = "";

//    int counter = 1;


    public static final String VIDEO_LINK = "https://handoutng.com/handouts/handout_update_tutorial_group_resource";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_links);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        Intent i = getIntent();
        group_name = i.getStringExtra("group_name");
        email = i.getStringExtra("email");

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        groupName = findViewById(R.id.groupNAME);
        videoName = findViewById(R.id.edtVideoName1);
        description = findViewById(R.id.edtDescription1);
        title = findViewById(R.id.title);
        link = findViewById(R.id.edtLink1);
        viewResources = findViewById(R.id.viewResources);
        submit = findViewById(R.id.submit);

        groupName.setText(group_name);

        viewResources.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), VideoCreatorView.class);
                i.putExtra("groupName", group_name);
                startActivity(i);
            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                vidName = videoName.getText().toString().trim();
                vidDesc = description.getText().toString().trim();
                vidLink = link.getText().toString().trim();

                if (vidLink.equals("")){
                    Toast.makeText(VideoLinks.this, "You have not included a link", Toast.LENGTH_LONG).show();
                }else {
                    //send to the database
                    myDialog = new Dialog(VideoLinks.this);
                    myDialog.setContentView(R.layout.custom_popup_login_loading);
                    myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    TextView text = myDialog.findViewById(R.id.text);
                    text.setText("Updating Video Link, please wait");
                    myDialog.setCanceledOnTouchOutside(false);
                    myDialog.show();


                    StringRequest stringRequest = new StringRequest(Request.Method.POST, VIDEO_LINK,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

                                    System.out.println("Upload Response = "+response);

                                    try{
                                        JSONObject jsonObject = new JSONObject(response);
                                        String status = jsonObject.getString("status");

                                        if(status.equals("successful")){
                                            link.setText("");
                                            description.setText("");
                                            description.setText("");
                                            myDialog.dismiss();
                                            Toast.makeText(VideoLinks.this, "Successfully Updated", Toast.LENGTH_LONG).show();

                                            // create a new dialog popup to add more files or view tutorial group
                                            myDialog2 = new Dialog(VideoLinks.this);
                                            myDialog2.setContentView(R.layout.custom_popup_upload_successful);
                                            Button addmore = myDialog2.findViewById(R.id.addmore);
                                            Button viewgroup = myDialog2.findViewById(R.id.viewgroup);
                                            addmore.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    onBackPressed();
                                                    myDialog2.dismiss();
                                                }
                                            });
                                            viewgroup.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    // go to the view resources page
                                                    Intent i = new Intent(VideoLinks.this, VideoCreatorView.class);
                                                    i.putExtra("groupName", group_name);
                                                    startActivity(i);
                                                }
                                            });
                                            myDialog2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                            myDialog2.setCanceledOnTouchOutside(true);
                                            myDialog2.show();

                                        }else{
                                            //something went wrong
                                            myDialog.dismiss();
                                            Toast.makeText(VideoLinks.this, "Error updating, please try again", Toast.LENGTH_LONG).show();
                                        }

                                    }
                                    catch (JSONException e){
                                        e.printStackTrace();
                                        myDialog.dismiss();
                                        Toast.makeText(getApplicationContext(), "Updating failed. Please try again", Toast.LENGTH_LONG).show();
                                    }

                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError volleyError) {

                                    if(volleyError == null){
                                        return;
                                    }

                                }
                            }){
                        @Override
                        protected Map<String, String> getParams(){
                            Map<String, String> params = new HashMap<>();
                            params.put("tutorial_group_name", group_name);
                            params.put("email", email);
                            params.put("fileurl", vidLink);
                            params.put("fileDescription", vidDesc);
                            params.put("fileName", vidName);
                            return params;
                        }
                    };

                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                    DefaultRetryPolicy retryPolicy = new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                    stringRequest.setRetryPolicy(retryPolicy);
                    requestQueue.add(stringRequest);
                }

            }
        });


    }

}