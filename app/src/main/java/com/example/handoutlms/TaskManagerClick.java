package com.example.handoutlms;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class TaskManagerClick extends AppCompatActivity {

    ImageView back, image;
    TextView category;
    String cat, email;
    ListView listView;
    TextView taskCount;

    ArrayList<String> arr_task_name = new ArrayList<>();
    ArrayList<String> arr_task_date = new ArrayList<>();
    ArrayList<String> arr_task_category = new ArrayList<>();
    ArrayList<String> arr_task_description = new ArrayList<>();
    ArrayList<String> arr_task_time = new ArrayList<>();

    ArrayList<String> new_arr_task_name = new ArrayList<>();
    ArrayList<String> new_arr_task_date = new ArrayList<>();
    ArrayList<String> new_arr_task_category = new ArrayList<>();
    ArrayList<String> new_arr_task_description = new ArrayList<>();
    ArrayList<String> new_arr_task_time = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_manager_click);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Intent i = getIntent();
        cat = i.getStringExtra("category");
        email = i.getStringExtra("email");
        arr_task_name = i.getStringArrayListExtra("task name");
        arr_task_date = i.getStringArrayListExtra("task date");
        arr_task_category = i.getStringArrayListExtra("task category");
        arr_task_description = i.getStringArrayListExtra("task description");
        arr_task_time = i.getStringArrayListExtra("task time");


        taskCount = findViewById(R.id.taskCount);
        image = findViewById(R.id.image);
        category = findViewById(R.id.category);
        listView = findViewById(R.id.listview);
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                finish();

//                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//                fragmentTransaction.replace(R.id.frame_taskManager, new TaskManager1()).commit();

//                Intent i = new Intent(getApplicationContext(), TaskManager1.class);
//                startActivity(i);
                onBackPressed();
            }
        });

        new_arr_task_name.clear();
        new_arr_task_date.clear();
        new_arr_task_description.clear();
        new_arr_task_category.clear();
        new_arr_task_time.clear();

        if(cat.equals("exam")){
            category.setText("Exam");
            image.setImageResource(R.drawable.tm9);

            for(int j=0; j<arr_task_name.size(); j++){
                if(arr_task_category.get(j).equals("Exam")){
                    new_arr_task_name.add(arr_task_name.get(j));
                    new_arr_task_date.add(arr_task_date.get(j));
                    new_arr_task_category.add(arr_task_category.get(j));
                    new_arr_task_description.add(arr_task_description.get(j));
                    new_arr_task_time.add(arr_task_time.get(j));
                }
            }
            taskCount.setText(new_arr_task_name.size()+" Tasks");
        }

        if(cat.equals("test")){
            category.setText("Test");
            image.setImageResource(R.drawable.tm6);

            for(int j=0; j<arr_task_name.size(); j++){
                if(arr_task_category.get(j).equals("Test")){
                    new_arr_task_name.add(arr_task_name.get(j));
                    new_arr_task_date.add(arr_task_date.get(j));
                    new_arr_task_category.add(arr_task_category.get(j));
                    new_arr_task_description.add(arr_task_description.get(j));
                    new_arr_task_time.add(arr_task_time.get(j));
                }
            }
            taskCount.setText(new_arr_task_name.size()+" Tasks");
        }

        if(cat.equals("assignment")){
            category.setText("Assignment");
            image.setImageResource(R.drawable.tm8);

            for(int j=0; j<arr_task_name.size(); j++){
                if(arr_task_category.get(j).equals("Assignment")){
                    new_arr_task_name.add(arr_task_name.get(j));
                    new_arr_task_date.add(arr_task_date.get(j));
                    new_arr_task_category.add(arr_task_category.get(j));
                    new_arr_task_description.add(arr_task_description.get(j));
                    new_arr_task_time.add(arr_task_time.get(j));
                }
            }
            taskCount.setText(new_arr_task_name.size()+" Tasks");
        }

        if(cat.equals("others")){
            category.setText("Others");
            image.setImageResource(R.drawable.tm6);
            image.setImageTintMode(PorterDuff.Mode.MULTIPLY);

            for(int j=0; j<arr_task_name.size(); j++){
                if(!arr_task_category.get(j).equals("Assignment") || !arr_task_category.get(j).equals("Test") || !arr_task_category.get(j).equals("Exam")){
                    new_arr_task_name.add(arr_task_name.get(j));
                    new_arr_task_date.add(arr_task_date.get(j));
                    new_arr_task_category.add(arr_task_category.get(j));
                    new_arr_task_description.add(arr_task_description.get(j));
                    new_arr_task_time.add(arr_task_time.get(j));
                }
            }
            taskCount.setText(new_arr_task_name.size()+" Tasks");

        }

        if(cat.equals("tutorials")){
            category.setText("Tutorials");
            image.setImageResource(R.drawable.tm8);

            for(int j=0; j<arr_task_name.size(); j++){
                if(arr_task_category.get(j).equals("Tutorials")){
                    new_arr_task_name.add(arr_task_name.get(j));
                    new_arr_task_date.add(arr_task_date.get(j));
                    new_arr_task_category.add(arr_task_category.get(j));
                    new_arr_task_description.add(arr_task_description.get(j));
                    new_arr_task_time.add(arr_task_time.get(j));
                }
            }
            taskCount.setText(new_arr_task_name.size()+" Tasks");
        }

        if(cat.equals("gigs")){
            category.setText("Gigs");
            image.setImageResource(R.drawable.tm9);

            for(int j=0; j<arr_task_name.size(); j++){
                if(arr_task_category.get(j).equals("Gigs")){
                    new_arr_task_name.add(arr_task_name.get(j));
                    new_arr_task_date.add(arr_task_date.get(j));
                    new_arr_task_category.add(arr_task_category.get(j));
                    new_arr_task_description.add(arr_task_description.get(j));
                    new_arr_task_time.add(arr_task_time.get(j));
                }
            }
            taskCount.setText(new_arr_task_name.size()+" Tasks");
        }

        TotalTaskAdapter myAdapter=new TotalTaskAdapter(getApplicationContext(),new_arr_task_name,new_arr_task_date,new_arr_task_category, new_arr_task_description, new_arr_task_time);
        listView.setAdapter(myAdapter);


        arr_task_name.clear();
        arr_task_date.clear();
        arr_task_description.clear();
        arr_task_category.clear();
        arr_task_time.clear();


    }

//    @Override
//    public void onFragmentInteraction(Uri uri) {
//
//    }
}