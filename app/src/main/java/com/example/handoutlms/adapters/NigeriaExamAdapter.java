package com.example.handoutlms.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.handoutlms.R;
import com.example.handoutlms.activities.Subjects;

import java.util.ArrayList;

public class NigeriaExamAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<String> examType;
    private ArrayList<Drawable> images;



    public NigeriaExamAdapter(Context context, ArrayList<String> examType, ArrayList<Drawable> images){
        //Getting all the values
        this.context = context;
        this.examType = examType;
        this.images = images;
    }

    @Override
    public int getCount() {
        return examType.size();
    }

    @Override
    public Object getItem(int i) {
        return examType.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        LayoutInflater inflaInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = inflaInflater.inflate(R.layout.list_nigeriaexam, parent, false);
        }

        LinearLayout exam = convertView.findViewById(R.id.exam);
        TextView name = convertView.findViewById(R.id.text);
        ImageView img = convertView.findViewById(R.id.image);

        name.setText(examType.get(i));
        img.setImageDrawable(images.get(i));

        exam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent j = new Intent(context, Subjects.class);
                j.putExtra("category", examType.get(i));
                j.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(j);
            }
        });


        return convertView;
    }
}
