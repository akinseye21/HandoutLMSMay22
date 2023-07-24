package com.example.handoutlms.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.handoutlms.R;

public class ViewOfflineJoinedTutorial extends AppCompatActivity {

    TextView groupName;
    String name, category, description, mode, g_id;
    ImageView img, back;
    LinearLayout video, pdf, address, task;

    public static final String ALL_TUTORIAL = "https://handoutng.com/handouts/handout_get_all_tutorials";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_offline_joined_tutorial);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Intent i = getIntent();
        name = i.getStringExtra("name");
        category = i.getStringExtra("category");
        description = i.getStringExtra("description");
        mode = i.getStringExtra("mode");
        g_id = i.getStringExtra("id");

        back = findViewById(R.id.back);
        groupName = findViewById(R.id.groupNAME);
        img = findViewById(R.id.img);
        video = findViewById(R.id.video);
        pdf = findViewById(R.id.pdf);
        address = findViewById(R.id.location);
        task = findViewById(R.id.addTask);

        groupName.setText(name);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}