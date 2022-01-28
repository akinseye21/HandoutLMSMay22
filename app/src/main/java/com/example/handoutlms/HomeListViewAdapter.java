package com.example.handoutlms;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class HomeListViewAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<String> created_by;
    private ArrayList<String> created_by_name;
    private ArrayList<String> group_name;
    private ArrayList<String> university;
    private ArrayList<String> mode;
    private ArrayList<String> group_name_inside;
    private ArrayList<String> description;
    private ArrayList<String> time;
    private ArrayList<String> category;

    public HomeListViewAdapter (Context context, ArrayList<String> created_by, ArrayList<String> created_by_name, ArrayList<String> group_name, ArrayList<String> university, ArrayList<String> mode, ArrayList<String> group_name_inside, ArrayList<String> description, ArrayList<String> time, ArrayList<String> category){
        //Getting all the values
        this.context = context;
        this.created_by = created_by;
        this.created_by_name = created_by_name;
        this.group_name = group_name;
        this.university = university;
        this.mode = mode;
        this.group_name_inside = group_name_inside;
        this.description = description;
        this.time = time;
        this.category = category;
    }

    @Override
    public int getCount() {
        return created_by.size();
    }

    @Override
    public Object getItem(int position) {
        return created_by.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflaInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = inflaInflater.inflate(R.layout.list_homeview, parent, false);
        }

        TextView crt_by = convertView.findViewById(R.id.created_by);
        TextView grp_name = convertView.findViewById(R.id.group_name);
        TextView univ = convertView.findViewById(R.id.uni);
//        TextView mod = convertView.findViewById(R.id.mode);
        TextView grp_name_inside = convertView.findViewById(R.id.group_name_inside);
        TextView desc = convertView.findViewById(R.id.description);
        TextView tme = convertView.findViewById(R.id.time);
//        RelativeLayout rellay_bg = convertView.findViewById(R.id.rellay_bg);

        crt_by.setText(created_by_name.get(position));
        grp_name.setText(category.get(position));
        univ.setText(university.get(position));
//        mod.setText(mode.get(position));
        grp_name_inside.setText(group_name_inside.get(position));
        desc.setText(description.get(position));
        tme.setText(time.get(position));

        if (category.get(position).equals("")){
            //change the background image
        }





        return convertView;
    }
}
