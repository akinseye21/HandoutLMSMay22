package com.example.handoutlms.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.handoutlms.R;

import java.util.ArrayList;

public class ExperienceAdapter extends BaseAdapter {

    Context context;
    ArrayList<String> arr_jobposition;
    ArrayList<String> arr_jobdescription;
    ArrayList<String> arr_joborganization;
    ArrayList<String> arr_jobyear;
    LayoutInflater inflter;

    public ExperienceAdapter(Context context, ArrayList<String> arr_jobposition , ArrayList<String> arr_jobdescription , ArrayList<String> arr_joborganization, ArrayList<String> arr_jobyear){
        this.context = context;
        this.arr_jobposition = arr_jobposition;
        this.arr_jobdescription = arr_jobdescription;
        this.arr_joborganization = arr_joborganization;
        this.arr_jobyear = arr_jobyear;
        inflter = (LayoutInflater.from(context));
    }

    @Override
    public int getCount() {
        return arr_jobposition.size();
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
        convertView = inflter.inflate(R.layout.list_experience, null);

        TextView position = convertView.findViewById(R.id.position);
        TextView description = convertView.findViewById(R.id.description);
        TextView organization = convertView.findViewById(R.id.organization);
        TextView year = convertView.findViewById(R.id.year);

        position.setText(arr_jobposition.get(i));
        description.setText(arr_jobdescription.get(i));
        organization.setText(arr_joborganization.get(i));
        year.setText(arr_jobyear.get(i));

        return convertView;
    }
}
