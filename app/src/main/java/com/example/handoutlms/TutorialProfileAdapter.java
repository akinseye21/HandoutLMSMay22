package com.example.handoutlms;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class TutorialProfileAdapter extends BaseAdapter {

    private Context context;
    ArrayList<String> arr_tutName;
    ArrayList<String> arr_tutCategory;
    ArrayList<String> arr_tutDescription;
    ArrayList<String> arr_tutMode;

    public TutorialProfileAdapter(Context context, ArrayList<String> tutName, ArrayList<String> tutCategory, ArrayList<String> tutDescription, ArrayList<String> tutMode){
        //Getting all the values
        this.context = context;
        this.arr_tutName = tutName;
        this.arr_tutCategory = tutCategory;
        this.arr_tutDescription = tutDescription;
        this.arr_tutMode = tutMode;
    }


    @Override
    public int getCount() {
        return arr_tutName.size();
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
    public View getView(int i, View convertView, ViewGroup parent) {
        LayoutInflater inflaInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = inflaInflater.inflate(R.layout.list_tutorial, parent, false);
        }

        TextView tutname = convertView.findViewById(R.id.tut_name);
        TextView tutcategory = convertView.findViewById(R.id.tut_category);
        TextView tutdescription = convertView.findViewById(R.id.tut_description);
        TextView tutmode = convertView.findViewById(R.id.tut_mode);

        tutname.setText(arr_tutName.get(i));
        tutcategory.setText(arr_tutCategory.get(i));
        tutdescription.setText(arr_tutDescription.get(i));
        tutmode.setText(arr_tutMode.get(i));

        return convertView;
    }
}
