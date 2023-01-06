package com.example.handoutlms;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class VideoLinks extends AppCompatActivity {

    ImageView back;
//    LinearLayout addlink;
//    ListView listView;
    String group_name, email;
    Button save1, save2, save3;
    Dialog myDialog;
    EditText groupName1, email1, link1;
    EditText groupName2, email2, link2;
    EditText groupName3, email3, link3;

    String my_groupName1, my_email1, my_link1;
    String my_groupName2, my_email2, my_link2;
    String my_groupName3, my_email3, my_link3;


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

        groupName1 = findViewById(R.id.edtGroupName1);
        email1 = findViewById(R.id.edtEmail1);
        link1 = findViewById(R.id.edtLink1);
        groupName1.setText(group_name);
        email1.setText(email);

        groupName2 = findViewById(R.id.edtGroupName2);
        email2 = findViewById(R.id.edtEmail2);
        link2 = findViewById(R.id.edtLink2);
        groupName2.setText(group_name);
        email2.setText(email);

        groupName3 = findViewById(R.id.edtGroupName3);
        email3 = findViewById(R.id.edtEmail3);
        link3 = findViewById(R.id.edtLink3);
        groupName3.setText(group_name);
        email3.setText(email);

        save1 = findViewById(R.id.save1);
        save2 = findViewById(R.id.save2);
        save3 = findViewById(R.id.save3);

        save1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                my_groupName1 = groupName1.getText().toString().trim();
                my_email1 = email1.getText().toString().trim();
                my_link1 = link1.getText().toString().trim();

                if (my_link1.equals("")){
                    Toast.makeText(VideoLinks.this, "You have not included a link", Toast.LENGTH_LONG).show();
                }else{
                    //send to the database
                    myDialog = new Dialog(VideoLinks.this);
                    myDialog.setContentView(R.layout.custom_popup_login_loading);
                    myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    TextView text = myDialog.findViewById(R.id.text);
                    text.setText("Updating Video Link 1, please wait");
                    myDialog.setCanceledOnTouchOutside(false);
                    myDialog.show();

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, VIDEO_LINK,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
//                                progressBar.setVisibility(View.GONE);

                                    System.out.println("Login Response = "+response);

                                    try{
                                        JSONObject jsonObject = new JSONObject(response);
                                        String status = jsonObject.getString("status");

                                        if(status.equals("successful")){
                                            link1.setText("");
                                            myDialog.dismiss();
                                            Toast.makeText(VideoLinks.this, "Successfully Updated link 1", Toast.LENGTH_SHORT).show();
                                        }else{
                                            //something went wrong
                                            link1.setText("");
                                            myDialog.dismiss();
                                            Toast.makeText(VideoLinks.this, "Error updating, please try again", Toast.LENGTH_SHORT).show();
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
                            params.put("tutorial_group_name", my_groupName1);
                            params.put("email", my_email1);
                            params.put("fileurl", my_link1);
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

        save2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                my_groupName2 = groupName2.getText().toString().trim();
                my_email2 = email2.getText().toString().trim();
                my_link2 = link2.getText().toString().trim();

                if (my_link2.equals("")){
                    Toast.makeText(VideoLinks.this, "You have not included a link", Toast.LENGTH_LONG).show();
                }else{
                    //send to the database
                    myDialog = new Dialog(VideoLinks.this);
                    myDialog.setContentView(R.layout.custom_popup_login_loading);
                    myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    TextView text = myDialog.findViewById(R.id.text);
                    text.setText("Updating Video Link 2, please wait");
                    myDialog.setCanceledOnTouchOutside(false);
                    myDialog.show();

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, VIDEO_LINK,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
//                                progressBar.setVisibility(View.GONE);

                                    System.out.println("Login Response = "+response);

                                    try{
                                        JSONObject jsonObject = new JSONObject(response);
                                        String status = jsonObject.getString("status");

                                        if(status.equals("successful")){
                                            link2.setText("");
                                            myDialog.dismiss();
                                            Toast.makeText(VideoLinks.this, "Successfully Updated link 2", Toast.LENGTH_SHORT).show();
                                        }else{
                                            //something went wrong
                                            link2.setText("");
                                            myDialog.dismiss();
                                            Toast.makeText(VideoLinks.this, "Error updating, please try again", Toast.LENGTH_SHORT).show();
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
                            params.put("tutorial_group_name", my_groupName2);
                            params.put("email", my_email2);
                            params.put("fileurl", my_link2);
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

        save3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                my_groupName3 = groupName3.getText().toString().trim();
                my_email3 = email3.getText().toString().trim();
                my_link3 = link3.getText().toString().trim();

                if (my_link3.equals("")){
                    Toast.makeText(VideoLinks.this, "You have not included a link", Toast.LENGTH_LONG).show();
                }else{
                    //send to the database
                    myDialog = new Dialog(VideoLinks.this);
                    myDialog.setContentView(R.layout.custom_popup_login_loading);
                    myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    TextView text = myDialog.findViewById(R.id.text);
                    text.setText("Updating Video Link 3, please wait");
                    myDialog.setCanceledOnTouchOutside(false);
                    myDialog.show();

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, VIDEO_LINK,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
//                                progressBar.setVisibility(View.GONE);

                                    System.out.println("Login Response = "+response);

                                    try{
                                        JSONObject jsonObject = new JSONObject(response);
                                        String status = jsonObject.getString("status");

                                        if(status.equals("successful")){
                                            link3.setText("");
                                            myDialog.dismiss();
                                            Toast.makeText(VideoLinks.this, "Successfully Updated link 3", Toast.LENGTH_SHORT).show();
                                        }else{
                                            //something went wrong
                                            link3.setText("");
                                            myDialog.dismiss();
                                            Toast.makeText(VideoLinks.this, "Error updating, please try again", Toast.LENGTH_SHORT).show();
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
                            params.put("tutorial_group_name", my_groupName3);
                            params.put("email", my_email3);
                            params.put("fileurl", my_link3);
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

//        addlink = findViewById(R.id.addlink);
//        listView = findViewById(R.id.listview);

//        addlink.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                counter = counter + 1;
//
//                VideoLinkAdapter adapter = new VideoLinkAdapter(getApplicationContext(), counter, group_name, email);
////                listView.setAdapter(adapter);
//            }
//        });

//        VideoLinkAdapter adapter = new VideoLinkAdapter(getApplicationContext(), counter, group_name, email);
//        listView.setAdapter(adapter);


//        save.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                my_groupName1 = groupName1.getText().toString().trim();
//                my_email1 = email1.getText().toString().trim();
//                my_link1 = link1.getText().toString().trim();
//
//                my_groupName2 = groupName2.getText().toString().trim();
//                my_email2 = email2.getText().toString().trim();
//                my_link2 = link2.getText().toString().trim();
//
//                my_groupName3 = groupName3.getText().toString().trim();
//                my_email3 = email3.getText().toString().trim();
//                my_link3 = link3.getText().toString().trim();
//
//                if(my_link1.equals("") && my_link2.equals("") && my_link3.equals("")){
//                    Toast.makeText(VideoLinks.this, "You have not added any video link", Toast.LENGTH_SHORT).show();
//                }else if(!my_link1.equals("") || !my_link2.equals("") || !my_link3.equals("")){
//                    //send the one's available to the database
//
//                }
//
//                myDialog = new Dialog(VideoLinks.this);
//                myDialog.setContentView(R.layout.custom_popup_login_loading);
//                myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                TextView text = myDialog.findViewById(R.id.text);
//                text.setText("Updating Video Links, please wait");
//                myDialog.setCanceledOnTouchOutside(false);
//                myDialog.show();

//                Toast.makeText(getApplicationContext(), "Array = "+listView.getAdapter().getCount(), Toast.LENGTH_LONG).show();
//                View v = null;
//                for(int i=0; i<listView.getAdapter().getCount(); i++){
//                    v = adapter.getView(i, v, listView);
//
//                    EditText groupName1 = v.findViewById(R.id.edtGroupName);
//                    EditText email = v.findViewById(R.id.edtEmail);
//                    EditText description = v.findViewById(R.id.edtDescription);
//                    EditText link = v.findViewById(R.id.edtLink);
//
//                    String groupname = groupName1.getText().toString().trim();
//                    String email_ = email.getText().toString().trim();
//                    String description_ = description.getText().toString().trim();
//                    String link_ = link.getText().toString().trim();
//
//                    Toast.makeText(VideoLinks.this, "link = "+link_+"\nEmail = "+email_, Toast.LENGTH_SHORT).show();
//                    System.out.println("link = "+link_+"\nEmail = "+email_);

//                    if (link_.isEmpty()){
//                        Toast.makeText(VideoLinks.this, "No URL provided, please check and try again", Toast.LENGTH_SHORT).show();
//                    }else{
//                        //send the strings to the API
//                        StringRequest stringRequest = new StringRequest(Request.Method.POST, VIDEO_LINK,
//                                new Response.Listener<String>() {
//                                    @Override
//                                    public void onResponse(String response) {
////                                progressBar.setVisibility(View.GONE);
//
//                                        System.out.println("Login Response = "+response);
//
//                                        try{
//                                            JSONObject jsonObject = new JSONObject(response);
//                                            String status = jsonObject.getString("status");
//
//                                            if(status.equals("successful")){
//
//                                            }else{
//                                                //something went wrong
//                                            }
//
//                                        }
//                                        catch (JSONException e){
//                                            e.printStackTrace();
//                                            Toast.makeText(getApplicationContext(), "Updating failed. Please try again", Toast.LENGTH_LONG).show();
//                                        }
//
//                                    }
//                                },
//                                new Response.ErrorListener() {
//                                    @Override
//                                    public void onErrorResponse(VolleyError volleyError) {
//
//                                        if(volleyError == null){
//                                            return;
//                                        }
//
//                                    }
//                                }){
//                            @Override
//                            protected Map<String, String> getParams(){
//                                Map<String, String> params = new HashMap<>();
//                                params.put("tutorial_group_name", groupname);
//                                params.put("email", email_);
//                                params.put("fileurl", link_);
//                                return params;
//                            }
//                        };
//
//                        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
//                        DefaultRetryPolicy retryPolicy = new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
//                        stringRequest.setRetryPolicy(retryPolicy);
//                        requestQueue.add(stringRequest);
//                    }
//                }

//                myDialog.dismiss();
//            }
//        });

    }
}