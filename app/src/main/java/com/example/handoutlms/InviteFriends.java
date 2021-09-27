package com.example.handoutlms;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ListView;

public class InviteFriends extends AppCompatActivity {

    LinearLayout next;
    ListView listView;
    String group_name, category, date, time, university, description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_friends);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Intent i = getIntent();
        group_name = i.getStringExtra("group_name");
        category = i.getStringExtra("category");
        date = i.getStringExtra("date");
        time = i.getStringExtra("time");
        university = i.getStringExtra("university");
        description = i.getStringExtra("description");

        next = findViewById(R.id.next);
        listView = findViewById(R.id.listview);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(InviteFriends.this, AlmostDone.class);
                i.putExtra("group_name", group_name);
                i.putExtra("category", category);
                i.putExtra("date", date);
                i.putExtra("time", time);
                i.putExtra("university", university);
                i.putExtra("description", description);
                startActivity(i);
            }
        });
    }
}
