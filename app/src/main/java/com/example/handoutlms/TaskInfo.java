package com.example.handoutlms;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

public class TaskInfo extends AppCompatActivity {

    String taskName, taskTime, taskDescription, taskCategory, taskDate;
    ImageView back;
    TextView name, description, date, time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_info);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Intent i = getIntent();
        taskName = i.getStringExtra("task_name");
        taskTime = i.getStringExtra("task_time");
        taskDescription = i.getStringExtra("task_description");
        taskCategory = i.getStringExtra("task_category");
        taskDate = i.getStringExtra("task_date");

        back = findViewById(R.id.back);
        name = findViewById(R.id.name);
        description = findViewById(R.id.description);
        date = findViewById(R.id.date);
        time = findViewById(R.id.time);

        name.setText(taskName);
        description.setText(taskDescription);
        date.setText(taskDate);
        time.setText(taskTime);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}