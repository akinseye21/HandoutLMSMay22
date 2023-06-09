package com.example.handoutlms;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class TodayTaskAdapter extends BaseAdapter {

    Context context;
    //    ArrayList<String> image;
    ArrayList<String> task_name;
    ArrayList<String> task_date;
    ArrayList<String> task_category;
    ArrayList<String> today;
    LayoutInflater inflter;


    public TodayTaskAdapter(Context applicationContext, ArrayList<String> task_name, ArrayList<String> task_date, ArrayList<String> task_category, ArrayList<String> today ){
        this.context = applicationContext;
        this.task_name = task_name;
        this.task_date = task_date;
        this.task_category = task_category;
        this.today = today;
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
        convertView = inflter.inflate(R.layout.list_today_task_view, null);

//        TextView no_notification = convertView.findViewById(R.id.no_notification);
        LinearLayout dot = convertView.findViewById(R.id.lin_dot);
        RelativeLayout overall = convertView.findViewById(R.id.lin_overall);
        TextView notification_name = convertView.findViewById(R.id.notification_name);
        TextView notification_category = convertView.findViewById(R.id.notification_category);

        overall.setVisibility(View.VISIBLE);
        if(task_category.get(i).equals("Exam")){
            dot.setBackgroundResource(R.drawable.circle_dark_green);
            notification_name.setText(task_name.get(i));
            notification_category.setText(task_category.get(i));
        }else if(task_category.get(i).equals("Test")){
            dot.setBackgroundResource(R.drawable.circle_orange);
            notification_name.setText(task_name.get(i));
            notification_category.setText(task_category.get(i));
        }else if(task_category.get(i).equals("Assignment")){
            dot.setBackgroundResource(R.drawable.circle_red);
            notification_name.setText(task_name.get(i));
            notification_category.setText(task_category.get(i));
        }else{
            dot.setBackgroundResource(R.drawable.circle_yellow);
            notification_name.setText(task_name.get(i));
            notification_category.setText(task_category.get(i));
        }


        return convertView;
    }
}
