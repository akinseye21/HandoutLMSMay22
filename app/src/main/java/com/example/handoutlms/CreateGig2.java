package com.example.handoutlms;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.snackbar.Snackbar;

public class CreateGig2 extends AppCompatActivity {

    String status = "unselected";

    CardView mcv;
    Button next;
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_gig2);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), CreateGig1.class);
                startActivity(i);
            }
        });

        mcv = findViewById(R.id.mcv);
        mcv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(status.equals("unselected")){
                    mcv.setCardBackgroundColor(Color.parseColor("#2800ff00"));
                    status = "selected";
                }
                else if (status.equals("selected")){
                    mcv.setCardBackgroundColor(Color.TRANSPARENT);
                    status = "unselected";
                }
            }
        });

        next = findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), CreateGig3.class);
                startActivity(i);
            }
        });

    }

    @Override
    public void onBackPressed() {
        // do nothing
    }
}
