package com.example.handoutlms.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.handoutlms.R;

import java.util.ArrayList;

public class EducationAdapter extends BaseAdapter {

    Context context;
    ArrayList<String> arr_uni;
    ArrayList<String> arr_qualification;
    ArrayList<String> arr_department;
    ArrayList<String> arr_year;
    LayoutInflater inflter;

    public EducationAdapter(Context context, ArrayList<String> arr_uni, ArrayList<String> arr_qualification, ArrayList<String> arr_department, ArrayList<String> arr_year){
        this.context = context;
        this.arr_uni = arr_uni;
        this.arr_qualification = arr_qualification;
        this.arr_department = arr_department;
        this.arr_year = arr_year;
        inflter = (LayoutInflater.from(context));
    }


    @Override
    public int getCount() {
        return arr_uni.size();
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
        convertView = inflter.inflate(R.layout.list_education, null);

        TextView uni = convertView.findViewById(R.id.uni);
        TextView department = convertView.findViewById(R.id.department);
        TextView qualification = convertView.findViewById(R.id.qualification);
        TextView year = convertView.findViewById(R.id.year);

        uni.setText(arr_uni.get(i));
        department.setText(arr_department.get(i));
        qualification.setText(arr_qualification.get(i));
        year.setText(arr_year.get(i));

        return convertView;
    }
}
