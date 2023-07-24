package com.example.handoutlms.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.handoutlms.R;
import com.example.handoutlms.adapters.TotalTaskAdapter;

import java.util.ArrayList;

public class TaskManagerClick extends AppCompatActivity {

    ImageView back, image;
    TextView category;
    String cat, email;
    ListView listView;
    TextView taskCount;
    LinearLayout noTask;

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
                mineS();
            }
        });
        noTask = findViewById(R.id.no_task);

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

        if(cat.equals("games")){
            category.setText("Games");
            image.setImageResource(R.drawable.tm6);
            image.setImageTintMode(PorterDuff.Mode.MULTIPLY);

            for(int j=0; j<arr_task_name.size(); j++){
                if(arr_task_category.get(j).equals("Games")){
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
                if(arr_task_category.get(j).equals("Tutorial")){
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
                if(arr_task_category.get(j).equals("Gig")){
                    new_arr_task_name.add(arr_task_name.get(j));
                    new_arr_task_date.add(arr_task_date.get(j));
                    new_arr_task_category.add(arr_task_category.get(j));
                    new_arr_task_description.add(arr_task_description.get(j));
                    new_arr_task_time.add(arr_task_time.get(j));
                }
            }
            taskCount.setText(new_arr_task_name.size()+" Tasks");
        }

//        Toast.makeText(this, "Length = "+new_arr_task_name.size(), Toast.LENGTH_SHORT).show();

        if (new_arr_task_name.size() < 1){
            noTask.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        }else{
            TotalTaskAdapter myAdapter=new TotalTaskAdapter(getApplicationContext(),new_arr_task_name,new_arr_task_date,new_arr_task_category, new_arr_task_description, new_arr_task_time);
            listView.setAdapter(myAdapter);
        }




        arr_task_name.clear();
        arr_task_date.clear();
        arr_task_description.clear();
        arr_task_category.clear();
        arr_task_time.clear();


    }

    public void mineS(){
        Intent i = new Intent(TaskManagerClick.this, FeedsDashboard.class);
        i.putExtra("email", email);
        i.putExtra("sent from", "task manager click");
        startActivity(i);
//        TaskManager1 taskManager1 = new TaskManager1();
////        FragmentTransaction transaction = getFragmentManager().beginTransaction();
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.replace(R.id.frame_taskManager, taskManager1);
//        transaction.commit();

    }

    public interface OnFragmentInteractionListener {
    }
}