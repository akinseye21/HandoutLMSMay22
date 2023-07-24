package com.example.handoutlms.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.handoutlms.R;

import java.util.ArrayList;

public class SimpleGigSetAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<String> skills;

    public SimpleGigSetAdapter(Context context, ArrayList<String> skills){
        //Getting all the values
        this.context = context;
        this.skills = skills;
    }

    @Override
    public int getCount() {
        return skills.size();
    }

    @Override
    public Object getItem(int position) {
        return skills.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflaInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = inflaInflater.inflate(R.layout.list_skills, parent, false);
        }

        TextView name = convertView.findViewById(R.id.mytext);

        name.setText(skills.get(position));

        return convertView;
    }
}
