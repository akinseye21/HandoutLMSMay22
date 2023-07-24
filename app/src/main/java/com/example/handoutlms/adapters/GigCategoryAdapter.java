package com.example.handoutlms.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.handoutlms.R;

import java.util.ArrayList;
import java.util.Collections;

public class GigCategoryAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<String> category_name;

    public GigCategoryAdapter (Context context, ArrayList<String> category_name){
        //Getting all the values
        this.context = context;
        this.category_name = category_name;
    }

    @Override
    public int getCount() {
        return category_name.size();
    }

    @Override
    public Object getItem(int position) {
        return category_name.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater= (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (view == null) {
            view = inflater.inflate(R.layout.list_gig_category, parent, false);
        }

        TextView category = view.findViewById(R.id.category_name);
//        TextView close = view.findViewById(R.id.close);

        category.setText(category_name.get(position));

        return view;

    };
}
