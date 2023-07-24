package com.example.handoutlms.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.example.handoutlms.R;

public class VirtualLibrary extends AppCompatActivity {

    ImageView back;
    CardView openStax, oer;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_virtual_library);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Intent i = getIntent();
        email = i.getStringExtra("email");

        back = findViewById(R.id.back);
        openStax = findViewById(R.id.openstax);
        oer = findViewById(R.id.oer);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(VirtualLibrary.this, FeedsDashboard.class);
                i.putExtra("email", email);
                i.putExtra("sent from", "virtual library");
                startActivity(i);
            }
        });

        openStax.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent i = new Intent(VirtualLibrary.this, OpenStax.class);
//                i.putExtra("email", email);
//                startActivity(i);

                Dialog myDialog = new Dialog(VirtualLibrary.this);
                myDialog.setContentView(R.layout.custom_popup_comingsoon);
                ImageView img = myDialog.findViewById(R.id.img);
                img.setImageResource(R.drawable.im2);
                Button ok = myDialog.findViewById(R.id.ok);
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        myDialog.dismiss();
                    }
                });
                myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                myDialog.setCanceledOnTouchOutside(true);
                myDialog.show();
            }
        });
         oer.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 //show coming soon
                 Dialog myDialog = new Dialog(VirtualLibrary.this);
                 myDialog.setContentView(R.layout.custom_popup_comingsoon);
                 Button ok = myDialog.findViewById(R.id.ok);
                 ok.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View view) {
                         myDialog.dismiss();
                     }
                 });
                 myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                 myDialog.setCanceledOnTouchOutside(true);
                 myDialog.show();
             }
         });
    }
}