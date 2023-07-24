package com.example.handoutlms.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ApprovedGigUserView extends AppCompatActivity {

    String biddername, bidderemail,bidamount, cv, pp, gigName, gigdescription, gigskills, gigid, gigprice, startdate, enddate;
    String fullname, email;
    CircleImageView profpic;
    TextView username, gigname, bidderName, bidderEmail, bidAmount, startDate, endDate;
    Button close;
    ImageView back;
    LinearLayout terminate, extend;
    String selectedOption = "";
    SharedPreferences preferences;
    Dialog myDialog, myDialog2;

    public static final String TERMINATE = "https://handoutng.com/handouts/handout_terminate_gig";
    public static final String EXTEND_TIMELINE = "https://handoutng.com/handouts/handout_update_gig_dates";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approved_gig_user_view);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        preferences = getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        fullname = preferences.getString("fullname", "not available");
        email = preferences.getString("email", "not available");

        Intent i = getIntent();
        biddername = i.getStringExtra("biddername");
        bidderemail = i.getStringExtra("bidderemail");
        bidamount = i.getStringExtra("bidamount");
        cv = i.getStringExtra("cv");
        pp = i.getStringExtra("pp");
        gigName = i.getStringExtra("gigname");
        gigdescription = i.getStringExtra("gigdescription");
        gigskills = i.getStringExtra("gigskills");
        gigprice = i.getStringExtra("gigprice");
        gigid = i.getStringExtra("gigid");
        startdate = i.getStringExtra("startdate");
        enddate = i.getStringExtra("enddate");

        profpic = findViewById(R.id.pp);
        username = findViewById(R.id.created_by_gig);
        gigname = findViewById(R.id.gig_name);
        bidderName = findViewById(R.id.bidderName);
        bidderEmail = findViewById(R.id.bidderEmail);
        bidAmount = findViewById(R.id.bidAmount);
        startDate = findViewById(R.id.startdate);
        endDate = findViewById(R.id.enddate);
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ApprovedGigUserView.this, ShowCreatedGigs.class);
                intent.putExtra("gigname", gigName);
                intent.putExtra("gigdescription", gigdescription);
                intent.putExtra("gigskills", gigskills);
                intent.putExtra("gigprice", gigprice);
                intent.putExtra("gigId", gigid);
                intent.putExtra("startDate", startdate);
                intent.putExtra("endDate", enddate);
                startActivity(intent);
            }
        });
        close = findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ApprovedGigUserView.this, ShowCreatedGigs.class);
                intent.putExtra("gigname", gigName);
                intent.putExtra("gigdescription", gigdescription);
                intent.putExtra("gigskills", gigskills);
                intent.putExtra("gigprice", gigprice);
                intent.putExtra("gigId", gigid);
                intent.putExtra("startDate", startdate);
                intent.putExtra("endDate", enddate);
                startActivity(intent);
            }
        });

        Glide.with(ApprovedGigUserView.this).load(pp).into(profpic);
        username.setText(fullname);
        gigname.setText(gigName);
        bidderName.setText(biddername);
        bidderEmail.setText(bidderemail);
        bidAmount.setText(bidamount);
        startDate.setText(startdate);
        endDate.setText(enddate);

        terminate = findViewById(R.id.terminate);
        terminate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog myDialog = new Dialog(ApprovedGigUserView.this);
                myDialog.setContentView(R.layout.custom_popup_termination);
                RadioGroup radioGroup = myDialog.findViewById(R.id.radioGroup);
                LinearLayout loading = myDialog.findViewById(R.id.loading);
                radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        // Handle radio button selection changes here
                        RadioButton completed = myDialog.findViewById(R.id.completed);
                        RadioButton unsatisfied = myDialog.findViewById(R.id.unsatisfied);

                        if (completed.isChecked()){
                            selectedOption = completed.getText().toString();
                        }else if (unsatisfied.isChecked()){
                            selectedOption = unsatisfied.getText().toString();
                        }
                        Toast.makeText(ApprovedGigUserView.this, "Selected: " + selectedOption, Toast.LENGTH_SHORT).show();
                    }
                });
                Button ok = myDialog.findViewById(R.id.ok);
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        myDialog.dismiss();
                        loading.setVisibility(View.VISIBLE);
                        //terminate from DB
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, TERMINATE,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {

                                        System.out.println("Response = "+response);
                                        try{
                                            JSONObject jsonObject = new JSONObject(response);
                                            String status = jsonObject.getString("status");

                                            if (status.equals("success")){
                                                loading.setVisibility(View.GONE);
                                                myDialog.dismiss();
                                                Toast.makeText(ApprovedGigUserView.this, "Termination of project "+gigname+" was successfull", Toast.LENGTH_SHORT).show();
                                            }
                                            else if (status.equals("failed")){
                                                loading.setVisibility(View.GONE);
                                                myDialog.dismiss();
                                                Toast.makeText(ApprovedGigUserView.this, "Termination of project "+gigname+" failed", Toast.LENGTH_SHORT).show();
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

                                        if(volleyError == null){
                                            return;
                                        }
                                        System.out.println("Error = "+volleyError.getMessage());
                                        myDialog.dismiss();
                                        loading.setVisibility(View.GONE);
                                    }
                                }){
                            @Override
                            protected Map<String, String> getParams(){
                                Map<String, String> params = new HashMap<>();
                                params.put("gigname", gigName);
                                params.put("note", selectedOption);
                                return params;
                            }
                        };


                        RequestQueue requestQueue = Volley.newRequestQueue(ApprovedGigUserView.this);
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
                });
                myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                myDialog.setCanceledOnTouchOutside(true);
                myDialog.show();
            }
        });

        extend = findViewById(R.id.extend);
        extend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //show loader
                myDialog2 = new Dialog(ApprovedGigUserView.this);
                myDialog2.setContentView(R.layout.custom_popup_extend_timeline);
                EditText startD = myDialog2.findViewById(R.id.startD);
                startD.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // calender class's instance and get current date , month and year from calender
                        final Calendar c = Calendar.getInstance();
                        int mYear = c.get(Calendar.YEAR); // current year
                        int mMonth = c.get(Calendar.MONTH); // current month
                        int mDay = c.get(Calendar.DAY_OF_MONTH); // current day

                        // date picker dialog
                        DatePickerDialog datePickerDialog = new DatePickerDialog(ApprovedGigUserView.this,
                                new DatePickerDialog.OnDateSetListener() {

                                    @Override
                                    public void onDateSet(DatePicker view, int year,
                                                          int monthOfYear, int dayOfMonth) {
                                        monthOfYear+=1;
                                        // set day of month , month and year value in the edit text
                                        String mt;
                                        if(monthOfYear<10){
                                            mt = "0"+monthOfYear;
                                        }
                                        else mt = String.valueOf(monthOfYear);
                                        String dy;
                                        if(dayOfMonth<10)
                                            dy = "0"+dayOfMonth;
                                        else dy = String.valueOf(dayOfMonth);

//                                dte.setText(dy + "/" + mt + "/" + year);
                                        startD.setText(year + "-" + mt + "-" + dy);
                                    }
                                }, mYear, mMonth, mDay);
                        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                        datePickerDialog.show();
                    }
                });
                TextView gigN = myDialog2.findViewById(R.id.gigN);
                gigN.setText(gigName);
                EditText endD = myDialog2.findViewById(R.id.endD);
                endD.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // calender class's instance and get current date , month and year from calender
                        final Calendar c = Calendar.getInstance();
                        int mYear = c.get(Calendar.YEAR); // current year
                        int mMonth = c.get(Calendar.MONTH); // current month
                        int mDay = c.get(Calendar.DAY_OF_MONTH); // current day

                        // date picker dialog
                        DatePickerDialog datePickerDialog = new DatePickerDialog(ApprovedGigUserView.this,
                                new DatePickerDialog.OnDateSetListener() {

                                    @Override
                                    public void onDateSet(DatePicker view, int year,
                                                          int monthOfYear, int dayOfMonth) {
                                        monthOfYear+=1;
                                        // set day of month , month and year value in the edit text
                                        String mt;
                                        if(monthOfYear<10){
                                            mt = "0"+monthOfYear;
                                        }
                                        else mt = String.valueOf(monthOfYear);
                                        String dy;
                                        if(dayOfMonth<10)
                                            dy = "0"+dayOfMonth;
                                        else dy = String.valueOf(dayOfMonth);

//                                dte.setText(dy + "/" + mt + "/" + year);
                                        endD.setText(year + "-" + mt + "-" + dy);
                                    }
                                }, mYear, mMonth, mDay);
                        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                        datePickerDialog.show();
                    }
                });
                Button ok = myDialog2.findViewById(R.id.ok);
                LinearLayout loading = myDialog2.findViewById(R.id.loading);
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //show loading
                        loading.setVisibility(View.VISIBLE);
                        //send to DB
                        // share to API Extend
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, EXTEND_TIMELINE,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        System.out.println("Response = "+response);

                                        JSONObject jo = null;
                                        try {
                                            jo = new JSONObject(response);
                                            String status = jo.getString("status");

                                            if (status.equals("successful")){
                                                myDialog2.dismiss();
                                                //load the custom dialog box
                                                myDialog = new Dialog(ApprovedGigUserView.this);
                                                myDialog.setContentView(R.layout.custom_popup_successful_taskmanager);
                                                Button home = myDialog.findViewById(R.id.home);
                                                TextView stat = myDialog.findViewById(R.id.status);
                                                stat.setText("You have successfully extended timeline for  "+gigName);

                                                home.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        myDialog.dismiss();
                                                    }
                                                });
                                                myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                                myDialog.setCanceledOnTouchOutside(false);
                                                myDialog.show();
                                            }else {
                                                myDialog2.dismiss();
                                                Toast.makeText(ApprovedGigUserView.this, "Failed!!", Toast.LENGTH_LONG).show();
                                            }
                                        } catch (JSONException e) {
                                            myDialog2.dismiss();
                                            Toast.makeText(ApprovedGigUserView.this, "Failed!!", Toast.LENGTH_LONG).show();
                                            e.printStackTrace();
                                        }
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError volleyError) {
                                        myDialog2.dismiss();
                                        Toast.makeText(ApprovedGigUserView.this, "Error!", Toast.LENGTH_LONG).show();
                                        volleyError.printStackTrace();
                                    }
                                }){
                            @Override
                            protected Map<String, String> getParams(){
                                Map<String, String> params = new HashMap<>();
                                params.put("email", email);
                                params.put("gigname", gigName);
                                params.put("start_date", startD.getText().toString().trim());
                                params.put("end_date", endD.getText().toString().trim());
                                return params;
                            }
                        };

                        RequestQueue requestQueue = Volley.newRequestQueue(ApprovedGigUserView.this);
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
                });
                myDialog2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                myDialog2.setCanceledOnTouchOutside(false);
                myDialog2.show();


            }
        });



    }
}