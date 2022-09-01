package com.example.handoutlms;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class TotalTaskAdapter extends BaseAdapter {

    Context context;
    //    ArrayList<String> image;
    ArrayList<String> task_name;
    ArrayList<String> task_date;
    ArrayList<String> task_category;
    ArrayList<String> task_description;
    ArrayList<String> task_time;
    LayoutInflater inflter;

    public TotalTaskAdapter(Context applicationContext, ArrayList<String> task_name, ArrayList<String> task_date, ArrayList<String> task_category, ArrayList<String> task_description, ArrayList<String> task_time ){
        this.context = applicationContext;
        this.task_name = task_name;
        this.task_date = task_date;
        this.task_category = task_category;
        this.task_description = task_description;
        this.task_time = task_time;
        inflter = (LayoutInflater.from(applicationContext));
    }


    @Override
    public int getCount() {
        return task_name.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        convertView = inflter.inflate(R.layout.list_add_task, null);
        ImageView circle = convertView.findViewById(R.id.circle);
        View line = convertView.findViewById(R.id.line);
        TextView name = convertView.findViewById(R.id.taskName);
        TextView description = convertView.findViewById(R.id.taskDescription);
        TextView time = convertView.findViewById(R.id.taskTime);

        if(task_category.get(i).equals("Exam")){
            circle.setBackgroundResource(R.drawable.circle_dark_green);
            line.setBackgroundResource(R.drawable.circle_dark_green);
            name.setText(task_name.get(i));
            name.setTextColor(Color.parseColor("#4fc8e5"));
            description.setText(task_description.get(i));
            time.setText(task_time.get(i));
        }else if(task_category.get(i).equals("Test")){
            circle.setBackgroundResource(R.drawable.circle_orange);
            line.setBackgroundResource(R.drawable.circle_orange);
            name.setText(task_name.get(i));
            name.setTextColor(Color.parseColor("#e69b12"));
            description.setText(task_description.get(i));
            time.setText(task_time.get(i));
        }else if(task_category.get(i).equals("Assignment")){
            circle.setBackgroundResource(R.drawable.circle_red);
            line.setBackgroundResource(R.drawable.circle_red);
            name.setText(task_name.get(i));
            name.setTextColor(Color.parseColor("#d3145a"));
            description.setText(task_description.get(i));
            time.setText(task_time.get(i));
        }else{
            circle.setBackgroundResource(R.drawable.circle_yellow);
            line.setBackgroundResource(R.drawable.circle_yellow);
            name.setText(task_name.get(i));
            name.setTextColor(Color.parseColor("#ffc460"));
            description.setText(task_description.get(i));
            time.setText(task_time.get(i));
        }
        return convertView;
    }
}
