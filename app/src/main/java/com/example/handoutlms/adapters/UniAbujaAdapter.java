package com.example.handoutlms.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.handoutlms.R;
import com.example.handoutlms.uniabuja.Page2;
import com.example.handoutlms.uniabuja.Page3;

import java.util.ArrayList;

public class UniAbujaAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<String> courses;
    private ArrayList<String> images;
    String level;
    String faculty;
    String department;
    String uniname;

    public UniAbujaAdapter(Context context, ArrayList<String> courses, ArrayList<String> images, String level, String faculty, String department, String uniname) {
        this.context = context;
        this.courses = courses;
        this.images = images;
        this.level = level;
        this.faculty = faculty;
        this.department = department;
        this.uniname = uniname;
    }

    @Override
    public int getCount() {
        return courses.size();
    }

    @Override
    public Object getItem(int i) {
        return courses.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflaInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = inflaInflater.inflate(R.layout.list_uniabj_trivia, parent, false);
        }

        TextView txtcourse = (TextView) convertView.findViewById(R.id.txtcourse);
        ImageView img = (ImageView) convertView.findViewById(R.id.img);
        RelativeLayout card = (RelativeLayout) convertView.findViewById(R.id.card);
        txtcourse.setText(courses.get(position));
        Glide.with(context).load(images.get(position)).into(img);
        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, Page3.class);
                i.putExtra("level", level);
                i.putExtra("faculty", faculty);
                i.putExtra("department", department);
                i.putExtra("course", courses.get(position));
                i.putExtra("uniname", uniname);
                context.startActivity(i);
            }
        });


        return  convertView;
    }
}
