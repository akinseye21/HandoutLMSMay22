package com.example.handoutlms.activities;

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
import android.widget.EditText;
import android.widget.HorizontalScrollView;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ClickTutOnProfile extends AppCompatActivity {

    TextView fullname, groupName, joinedUsers, pendingRequest, date;
    Button editBtn;
    String got_groupName, got_name, got_category, got_description, got_mode, got_id, got_date, got_type, got_classsize, from;
    String fullnamed, got_email;
    ImageView back;
    TextView noviews;
    HorizontalScrollView horizontalScrollView;
    LinearLayout loading;
    LinearLayout linApproved, linPending;
    SharedPreferences preferences;

    LinearLayout linClassSize, linEditClassSize;
    LinearLayout card1, card2, card3, card4;
    ImageView img1, img2, img3, img4;
    TextView tutName1, tutName2, tutName3, tutName4;
    TextView count1, count2, count3, count4;
    TextView classSize;
    View viewDem;
    Dialog myDialog, myDialog2;
    String size;
    int approvedUsers;

    // array for pending request
    ArrayList<String> arr_email = new ArrayList<>();
    ArrayList<String> arr_name = new ArrayList<>();
    ArrayList<String> arr_picture = new ArrayList<>();

    //array for approved request
    ArrayList<String> arr_email_approved = new ArrayList<>();
    ArrayList<String> arr_name_approved = new ArrayList<>();
    ArrayList<String> arr_picture_approved = new ArrayList<>();

    //array for top 4
    ArrayList<String> Array_fileDescription = new ArrayList<>();
    ArrayList<String> Array_fileURL = new ArrayList<>();
    ArrayList<String> Array_thumbnail = new ArrayList<>();
    ArrayList<String> Array_fileName = new ArrayList<>();
    ArrayList<String> Array_resID = new ArrayList<>();
    ArrayList<String> Array_totalHit = new ArrayList<>();

    private static final String TOP_4 = "https://handoutng.com/handouts/handout_get_top_resource_hits";
    private static final String USERS_TO_JOIN = "https://handoutng.com/handouts/handout_group_join_all";
    private static final String GET_APPROVED = "https://handoutng.com/handouts/handout_group_join_all_approved";
    private static final String UPDATE_CLASS_SIZE = "https://handoutng.com/handouts/handout_update_tutorial_class_size";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_click_tut_on_profile);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        preferences = getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        fullnamed = preferences.getString("fullname", "");
        got_email = preferences.getString("email", "");

        fullname = findViewById(R.id.fullname);
        groupName = findViewById(R.id.groupName);
        joinedUsers = findViewById(R.id.joinedUsers);
        pendingRequest = findViewById(R.id.pendingRequest);
        date = findViewById(R.id.date);
        editBtn = findViewById(R.id.edit);
        fullname.setText(fullnamed);
        back = findViewById(R.id.back);
        noviews = findViewById(R.id.noviews);
        loading = findViewById(R.id.loading);
        linApproved = findViewById(R.id.lin1);
        linPending = findViewById(R.id.lin3);
        horizontalScrollView = findViewById(R.id.horizontalScrollView);
        linClassSize = findViewById(R.id.linClassSize);
        linEditClassSize = findViewById(R.id.editClassSize);
        classSize = findViewById(R.id.classSize);
        viewDem = findViewById(R.id.viewdem);


        Intent i = getIntent();
        got_groupName = i.getStringExtra("groupName");
        got_name = i.getStringExtra("name");
        got_category = i.getStringExtra("category");
        got_description = i.getStringExtra("description");
        got_mode = i.getStringExtra("mode");
        got_id = i.getStringExtra("id");
        got_date = i.getStringExtra("date");
        got_type = i.getStringExtra("type");
        got_classsize = i.getStringExtra("classsize");
        from = i.getStringExtra("from");

        if (got_mode.equals("offline")){
            viewDem.setVisibility(View.VISIBLE);
            linEditClassSize.setVisibility(View.VISIBLE);
            linClassSize.setVisibility(View.VISIBLE);
            if (got_classsize.equals("")){
                classSize.setText("nil.");
            }else{
                classSize.setText(got_classsize);
            }


            linEditClassSize.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //show popup
                    myDialog = new Dialog(ClickTutOnProfile.this);
                    myDialog.setContentView(R.layout.custom_popup_edit_class_size);
                    TextView text = myDialog.findViewById(R.id.classSize);
                    text.setText(got_classsize);
                    EditText input = myDialog.findViewById(R.id.input);
                    Button save = myDialog.findViewById(R.id.save);

                    save.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //check the new input
                            String new_size = input.getText().toString().trim();
                            //if it is less than the already approved number, show error
                            if (new_size.equals("") || new_size.equals("0")){
                                input.setError("New size can not be empty");
                            }else if (Integer.parseInt(new_size) < approvedUsers){
                                input.setError("New size can not be less than approved number");
                            }else{
                                //else
                                //update the size
                                updateClass(new_size);
                            }
                        }
                    });

                    myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    myDialog.setCanceledOnTouchOutside(true);
                    myDialog.show();
                }
            });
        }



        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (from.equals("justCreatedResources")){
                    Intent i = new Intent(ClickTutOnProfile.this, FeedsDashboard.class);
                    i.putExtra("email", got_email);
                    i.putExtra("sent from", from);
                    startActivity(i);
                }else{
                    onBackPressed();
                }

            }
        });

        card1 = findViewById(R.id.card1);
        card2 = findViewById(R.id.card2);
        card3 = findViewById(R.id.card3);
        card4 = findViewById(R.id.card4);
        img1 = findViewById(R.id.img1);
        img2 = findViewById(R.id.img2);
        img3 = findViewById(R.id.img3);
        img4 = findViewById(R.id.img4);
        tutName1 = findViewById(R.id.name1);
        tutName2 = findViewById(R.id.name2);
        tutName3 = findViewById(R.id.name3);
        tutName4 = findViewById(R.id.name4);
        count1 = findViewById(R.id.count1);
        count2 = findViewById(R.id.count2);
        count3 = findViewById(R.id.count3);
        count4 = findViewById(R.id.count4);


        linPending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // goto the new view
                Intent i = new Intent(ClickTutOnProfile.this, PendingList4Tutorial.class);
                i.putStringArrayListExtra("name", arr_name);
                i.putStringArrayListExtra("email", arr_email);
                i.putStringArrayListExtra("picture", arr_picture);
                i.putExtra("id", got_id);
                i.putExtra("from", "pending");
                i.putExtra("groupName", got_groupName);
                i.putExtra("category", got_category);
                i.putExtra("description", got_description);
                i.putExtra("mode", got_mode);
                i.putExtra("date", got_date);
                i.putExtra("named", got_name);
                startActivity(i);
            }
        });

        linApproved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // goto the new view
                Intent i = new Intent(ClickTutOnProfile.this, PendingList4Tutorial.class);
                i.putStringArrayListExtra("name", arr_name_approved);
                i.putStringArrayListExtra("email", arr_email_approved);
                i.putStringArrayListExtra("picture", arr_picture_approved);
                i.putExtra("id", got_id);
                i.putExtra("from", "approved");
                i.putExtra("groupName", got_groupName);
                i.putExtra("category", got_category);
                i.putExtra("description", got_description);
                i.putExtra("mode", got_mode);
                i.putExtra("date", got_date);
                i.putExtra("named", got_name);
                startActivity(i);
            }
        });

        groupName.setText(got_groupName);
        date.setText(got_date);

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ClickTutOnProfile.this, VideoCreatorView.class);
                i.putExtra("groupName", got_groupName);
                i.putExtra("from", "ClickTutOnProfile");
                i.putStringArrayListExtra("name", arr_name_approved);
                i.putStringArrayListExtra("email", arr_email_approved);
                i.putStringArrayListExtra("picture", arr_picture_approved);
                i.putExtra("id", got_id);
                i.putExtra("category", got_category);
                i.putExtra("description", got_description);
                i.putExtra("mode", got_mode);
                i.putExtra("date", got_date);
                i.putExtra("named", got_name);
                startActivity(i);
            }
        });

    }

    private void updateClass(String new_size) {
        //show loader
        myDialog2 = new Dialog(ClickTutOnProfile.this);
        myDialog2.setContentView(R.layout.custom_popup_login_loading);

        TextView txt = myDialog2.findViewById(R.id.text);
        txt.setText("Updating class size, please wait");

        myDialog2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog2.setCanceledOnTouchOutside(false);
        myDialog2.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPDATE_CLASS_SIZE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("Click Response = "+response);
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            if (status.equals("success")){
                                myDialog2.dismiss();
                                Toast.makeText(ClickTutOnProfile.this, "Class Size updated successfully!", Toast.LENGTH_SHORT).show();
                                classSize.setText(new_size);
                                myDialog.dismiss();
                            }else if (status.equals("failed")){
                                myDialog2.dismiss();
                                Toast.makeText(ClickTutOnProfile.this, "Class Size update failed!", Toast.LENGTH_SHORT).show();
                            }else{
                                myDialog2.dismiss();
                                Toast.makeText(ClickTutOnProfile.this, "There was a problem updating, Please try again", Toast.LENGTH_SHORT).show();
                            }

                        }
                        catch (JSONException e){
                            myDialog2.dismiss();
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        myDialog2.dismiss();
                        System.out.println("Click Error = "+volleyError);
                        Toast.makeText(ClickTutOnProfile.this, "Network Error, Please try again", Toast.LENGTH_SHORT).show();
                        volleyError.printStackTrace();
                    }
                }){
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("email", got_email);
                params.put("groupname", got_groupName);
                params.put("class_size", new_size);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(ClickTutOnProfile.this);
        DefaultRetryPolicy retryPolicy = new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);
        requestQueue.add(stringRequest);
        requestQueue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
            @Override
            public void onRequestFinished(Request<Object> request) {
                requestQueue.getCache().clear();
            }
        });
    }

    private void getApprovedUsers() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, GET_APPROVED,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try{
                            JSONArray jsonArray = new JSONArray(response);
                            approvedUsers = jsonArray.length();
                            joinedUsers.setText(String.valueOf(approvedUsers));
                            for(int i=0; i<approvedUsers; i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String memberID = jsonObject.getString("memberID");
                                String member = jsonObject.getString("member");
                                String picture = jsonObject.getString("picture");
                                size = jsonObject.getString("size");

                                arr_email_approved.add(memberID);
                                arr_name_approved.add(member);
                                arr_picture_approved.add(picture);
                            }

                        }
                        catch (JSONException e){
                            joinedUsers.setText("0");
                            System.out.println("Output = "+e.toString());
                            e.printStackTrace();
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
                params.put("tid", "group_"+got_id);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(ClickTutOnProfile.this);
        DefaultRetryPolicy retryPolicy = new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);
        requestQueue.add(stringRequest);
        requestQueue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
            @Override
            public void onRequestFinished(Request<Object> request) {
                requestQueue.getCache().clear();
            }
        });

        //clear the array
        arr_email_approved.clear();
        arr_name_approved.clear();
        arr_picture_approved.clear();
    }

    private void getTop4() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, TOP_4,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try{
                            JSONArray update = new JSONArray(response);
                            int size = update.length();
                            if (size >= 1){
                                for (int j=0; j<size; j++){
                                    JSONObject jsonObject = update.getJSONObject(j);
                                    String totalHit = jsonObject.getString("total_hit");
                                    String fileURL = jsonObject.getString("fileURL");
                                    String fileDescription = jsonObject.getString("fileDescription");
                                    String fileName = jsonObject.getString("fileName");
                                    String thumbnail = jsonObject.getString("thumbnail");
                                    String resID = jsonObject.getString("resID");

                                    Array_totalHit.add(totalHit);
                                    Array_fileURL.add(fileURL);
                                    Array_fileDescription.add(fileDescription);
                                    Array_fileName.add(fileName);
                                    Array_thumbnail.add(thumbnail);
                                    Array_resID.add(resID);
                                }

                                loading.setVisibility(View.GONE);
                                horizontalScrollView.setVisibility(View.VISIBLE);
                                noviews.setVisibility(View.GONE);

                                if (size == 1){
                                    //visual for card 1
                                    card1.setVisibility(View.VISIBLE);
                                    card2.setVisibility(View.GONE);
                                    card3.setVisibility(View.GONE);
                                    card4.setVisibility(View.GONE);
                                    Glide.with(ClickTutOnProfile.this).load(Array_thumbnail.get(0)).into(img1);
                                    tutName1.setText(Array_fileName.get(0));
                                    count1.setText(Array_totalHit.get(0));
                                } else if (size == 2){
                                    //visual for card 1
                                    card1.setVisibility(View.VISIBLE);
                                    card2.setVisibility(View.VISIBLE);
                                    card3.setVisibility(View.GONE);
                                    card4.setVisibility(View.GONE);
                                    Glide.with(ClickTutOnProfile.this).load(Array_thumbnail.get(0)).into(img1);
                                    tutName1.setText(Array_fileName.get(0));
                                    count1.setText(Array_totalHit.get(0));

                                    //visual for card 2
                                    Glide.with(ClickTutOnProfile.this).load(Array_thumbnail.get(1)).into(img2);
                                    tutName2.setText(Array_fileName.get(1));
                                    count2.setText(Array_totalHit.get(1));
                                } else if (size == 3){
                                    //visual for card 1
                                    card1.setVisibility(View.VISIBLE);
                                    card2.setVisibility(View.VISIBLE);
                                    card3.setVisibility(View.VISIBLE);
                                    card4.setVisibility(View.GONE);
                                    Glide.with(ClickTutOnProfile.this).load(Array_thumbnail.get(0)).into(img1);
                                    tutName1.setText(Array_fileName.get(0));
                                    count1.setText(Array_totalHit.get(0));

                                    //visual for card 2
                                    Glide.with(ClickTutOnProfile.this).load(Array_thumbnail.get(1)).into(img2);
                                    tutName2.setText(Array_fileName.get(1));
                                    count2.setText(Array_totalHit.get(1));

                                    //visual for card 3
                                    Glide.with(ClickTutOnProfile.this).load(Array_thumbnail.get(2)).into(img3);
                                    tutName3.setText(Array_fileName.get(2));
                                    count3.setText(Array_totalHit.get(2));
                                } else if (size == 4){
                                    //visual for card 1
                                    card1.setVisibility(View.VISIBLE);
                                    card2.setVisibility(View.VISIBLE);
                                    card3.setVisibility(View.VISIBLE);
                                    card4.setVisibility(View.VISIBLE);
                                    Glide.with(ClickTutOnProfile.this).load(Array_thumbnail.get(0)).into(img1);
                                    tutName1.setText(Array_fileName.get(0));
                                    count1.setText(Array_totalHit.get(0));

                                    //visual for card 2
                                    Glide.with(ClickTutOnProfile.this).load(Array_thumbnail.get(1)).into(img2);
                                    tutName2.setText(Array_fileName.get(1));
                                    count2.setText(Array_totalHit.get(1));

                                    //visual for card 3
                                    Glide.with(ClickTutOnProfile.this).load(Array_thumbnail.get(2)).into(img3);
                                    tutName3.setText(Array_fileName.get(2));
                                    count3.setText(Array_totalHit.get(2));

                                    //visual for card 4
                                    Glide.with(ClickTutOnProfile.this).load(Array_thumbnail.get(3)).into(img4);
                                    tutName4.setText(Array_fileName.get(3));
                                    count4.setText(Array_totalHit.get(3));
                                }

                            }else{
                                loading.setVisibility(View.GONE);
                                horizontalScrollView.setVisibility(View.GONE);
                                noviews.setVisibility(View.VISIBLE);
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
                        volleyError.printStackTrace();
                    }
                }){
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("groupname", got_groupName);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(ClickTutOnProfile.this);
        DefaultRetryPolicy retryPolicy = new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);
        requestQueue.add(stringRequest);

        //clear the array
        Array_fileDescription.clear();
        Array_thumbnail.clear();
        Array_fileURL.clear();
        Array_resID.clear();
        Array_totalHit.clear();
        Array_fileName.clear();
    }

    private void getUsersToJoin() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, USERS_TO_JOIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try{
                            JSONArray update = new JSONArray(response);
                            int size = update.length();
                            pendingRequest.setText(String.valueOf(size));
                            for(int j=0; j<size; j++){
                                JSONObject jsonObject = update.getJSONObject(j);
                                String member_email = jsonObject.getString("memberID");
                                String member_name = jsonObject.getString("member");
                                String picture = jsonObject.getString("picture");

                                arr_email.add(member_email);
                                arr_name.add(member_name);
                                arr_picture.add(picture);
                            }

                        }
                        catch (JSONException e){
                            pendingRequest.setText("0");
                            System.out.println("Output = "+e.toString());
                            e.printStackTrace();
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
                params.put("tid", "group_"+got_id);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(ClickTutOnProfile.this);
        DefaultRetryPolicy retryPolicy = new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);
        requestQueue.add(stringRequest);

        //clear the array
        arr_email.clear();
        arr_name.clear();
        arr_picture.clear();
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        getApprovedUsers();
        getTop4();
        getUsersToJoin();
    }
}