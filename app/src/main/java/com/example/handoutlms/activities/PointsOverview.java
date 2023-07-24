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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
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
import com.example.handoutlms.adapters.OpenStaxAdapter;
import com.example.handoutlms.adapters.VoucherListAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class PointsOverview extends AppCompatActivity {

    ImageView incomeimage, voucherimage;
    Button incomebutton, voucherbutton;
    LinearLayout linIncome, linVoucher;
    TextView totalpoints, usedpoints, balancepoints, totalBalance;
    TextView voucherBalance, voucherPoint;
    Button claim;
    ImageView back;
    ListView voucherList;
    LinearLayout noVoucher;

    SharedPreferences preferences;
    String myEmail, myFullname, myPic;

    CircleImageView profilePic;
    TextView username, email;
    ArrayList<String> voucherCount = new ArrayList<>();

    ArrayList<String> voucherCode = new ArrayList<>();
    ArrayList<String> voucherStatus = new ArrayList<>();

    String[] array_Voucher;
    ArrayAdapter<String> institutionadapter;

    public static final String GET_POINTS = "https://handoutng.com/handouts/handout_get_user_points";
    public static final String GET_VOUCHERS = "https://handoutng.com/handouts/handout_get_my_vouchers";
    public static final String REDEEM_VOUCHER = "https://handoutng.com/handouts/handout_update_voucher_status";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_points_overview);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


    }

    public void showIncome(View view) {
        incomeimage.setImageResource(R.drawable.wallet1);
        incomebutton.setBackgroundResource(R.drawable.corner_income);
        voucherimage.setImageResource(R.drawable.vou2);
        voucherbutton.setBackgroundResource(R.drawable.corner_voucher);
        linIncome.setVisibility(View.VISIBLE);
        linVoucher.setVisibility(View.GONE);
        voucherList.setVisibility(View.GONE);
    }

    public void showVoucher(View view) {
        incomeimage.setImageResource(R.drawable.wallet2);
        incomebutton.setBackgroundResource(R.drawable.corner_voucher);
        voucherimage.setImageResource(R.drawable.vou1);
        voucherbutton.setBackgroundResource(R.drawable.corner_income);
        linIncome.setVisibility(View.GONE);
        linVoucher.setVisibility(View.VISIBLE);
        voucherList.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onStart() {
        super.onStart();

        preferences = getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        myEmail = preferences.getString("email", "not available");
        myFullname = preferences.getString("fullname", "not available");
        myPic = preferences.getString("pics", "not available");

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        noVoucher = findViewById(R.id.noVoucher);
        voucherList = findViewById(R.id.voucherList);
        totalpoints = findViewById(R.id.txttotalpoint);
        usedpoints = findViewById(R.id.txtusedpoints);
        balancepoints = findViewById(R.id.txtbalancepoint);
        totalBalance = findViewById(R.id.totalBalance);
        voucherBalance = findViewById(R.id.voucherBalance);
        voucherPoint = findViewById(R.id.voucherPoint);
        profilePic = findViewById(R.id.profilepic);
        username = findViewById(R.id.fullname);
        claim = findViewById(R.id.claim);
        email = findViewById(R.id.email);
        Glide.with(PointsOverview.this).load(myPic).into(profilePic);
        username.setText(myFullname);
        email.setText(myEmail);
        incomeimage = findViewById(R.id.incomeimage);
        voucherimage = findViewById(R.id.voucherimage);
        incomebutton = findViewById(R.id.incomebutton);
        voucherbutton = findViewById(R.id.voucherbutton);
        linIncome = findViewById(R.id.linIncome);
        linVoucher = findViewById(R.id.linVoucher);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, GET_POINTS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        System.out.println("Response = "+response);

                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            String summary = jsonObject.getString("summary");
                            String group = jsonObject.getString("action_group");
                            JSONObject jsonObject1 = new JSONObject(summary);
                            int balancePoints = jsonObject1.getInt("balance_points");
                            String totalAccruedPoints = jsonObject1.getString("total_accrued_points");
                            String totalUsedPoints = jsonObject1.getString("total_used_points");

                            //set the views
                            totalpoints.setText(totalAccruedPoints);
                            usedpoints.setText(totalUsedPoints);
                            balancepoints.setText(String.valueOf(balancePoints));
                            totalBalance.setText("N "+totalAccruedPoints);
                            voucherBalance.setText("N "+totalAccruedPoints);
                            voucherPoint.setText(totalAccruedPoints+" points");

                            JSONArray jsonArray = new JSONArray(group);
                            for (int i=0; i< jsonArray.length(); i++){
                                JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                                String category = jsonObject2.getString("category");
                                String value = jsonObject2.getString("value");
                                String percent = jsonObject2.getString("percent");
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
                params.put("email", myEmail);
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


        showPopUp();
        getVouchers();
    }

    private void getVouchers() {
        voucherCount.add("Select Voucher");
        voucherCount.add("");

        StringRequest stringRequest = new StringRequest(Request.Method.POST, GET_VOUCHERS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        System.out.println("Vouchers = "+response);

                        try{
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i=0; i< jsonArray.length(); i++){
                                JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                                String status = jsonObject2.getString("status");
                                if (status.equals("no voucher found") && jsonArray.length() == 1){
                                    // don't add anything to the spinner
                                    noVoucher.setVisibility(View.VISIBLE);
                                    voucherList.setVisibility(View.GONE);
                                }else if (status.equals("unused")){
                                    //add the vouchers to the array for spinner
                                    String voucher = jsonObject2.getString("voucher");
                                    voucherCount.add(voucher);

                                    voucherCode.add(voucher);
                                    voucherStatus.add(status);
                                }
                            }

                            array_Voucher = new String[voucherCount.size()];
                            for (int i = 0; i < voucherCount.size(); i++) {
                                array_Voucher[i] = voucherCount.get(i);
                            }

                            institutionadapter = new ArrayAdapter<>(PointsOverview.this, R.layout.simple_spinner_small_whitebg, R.id.tx, array_Voucher);

                            VoucherListAdapter voucherListAdapter = new VoucherListAdapter(PointsOverview.this, voucherStatus, voucherCode);
                            voucherList.setAdapter(voucherListAdapter);

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
                params.put("email", myEmail);
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
    }

    private void showPopUp() {
        claim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Dialog myDialog = new Dialog(PointsOverview.this);
                myDialog.setContentView(R.layout.custom_popup_giftupdate);
                ImageView close = myDialog.findViewById(R.id.close);
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        myDialog.dismiss();
                    }
                });
                Spinner voucherSpinner = myDialog.findViewById(R.id.voucherSpinner);
                voucherSpinner.setAdapter(institutionadapter);
                Button redeem = myDialog.findViewById(R.id.redeem);
                redeem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        redeemVoucher(voucherSpinner.getSelectedItem().toString().trim());
                    }
                });

                myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                myDialog.setCanceledOnTouchOutside(false);
                myDialog.show();
            }
        });
    }

    private void redeemVoucher(String code) {
        
        if (code.equals("") || code.equals("Select Voucher")){
            Toast.makeText(this, "You have not selected a voucher", Toast.LENGTH_SHORT).show();
        }else{

            Dialog myDialogLoading = new Dialog(PointsOverview.this);
            myDialogLoading.setContentView(R.layout.custom_popup_login_loading);
            TextView text = myDialogLoading.findViewById(R.id.text);
            text.setText("Redeeming Voucher, Please wait...");
            myDialogLoading.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            myDialogLoading.setCanceledOnTouchOutside(false);
            myDialogLoading.show();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, REDEEM_VOUCHER,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            System.out.println("Redeem Voucher = "+response);

                            try{
                                JSONObject jsonObject = new JSONObject(response);
                                String status = jsonObject.getString("status");
                                if (status.equals("success")) {
                                    //show popup of redemption
                                    myDialogLoading.dismiss();
                                    Toast.makeText(PointsOverview.this, "You have successfully Redeemed your voucher", Toast.LENGTH_LONG).show();
                                }else{
                                    //show error redeeming
                                    myDialogLoading.dismiss();
                                    Toast.makeText(PointsOverview.this, "There was an error redeemed your voucher", Toast.LENGTH_LONG).show();
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
                    params.put("email", myEmail);
                    params.put("code", code);
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
        }
    }
}