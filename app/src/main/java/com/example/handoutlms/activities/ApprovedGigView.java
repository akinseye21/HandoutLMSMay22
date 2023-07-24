package com.example.handoutlms.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.handoutlms.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class ApprovedGigView extends AppCompatActivity {

    String biddername, bidderemail,bidamount, cv, pp, gigName, gigdescription, gigskills, gigid, gigprice, startdate, enddate;
    String fullname;
    CircleImageView profpic;
    TextView username, gigname, bidderName, bidderEmail, bidAmount, startDate, endDate;
    Button close;
    ImageView back;

    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approved_gig_view);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        preferences = getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        fullname = preferences.getString("fullname", "not available");

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
                onBackPressed();
            }
        });
        close = findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        Glide.with(ApprovedGigView.this).load(pp).into(profpic);
        username.setText(fullname);
        gigname.setText(gigName);
        bidderName.setText(biddername);
        bidderEmail.setText(bidderemail);
        bidAmount.setText(bidamount);
        startDate.setText(startdate);
        endDate.setText(enddate);
    }
}