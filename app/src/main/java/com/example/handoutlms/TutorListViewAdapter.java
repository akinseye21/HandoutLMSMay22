package com.example.handoutlms;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class TutorListViewAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<String> tutorName;
    private ArrayList<String> tutorFaculty;

    public TutorListViewAdapter(Context context, ArrayList<String> tutorName, ArrayList<String> tutorFaculty){
        //Getting all the values
        this.context = context;
        this.tutorName = tutorName;
        this.tutorFaculty = tutorFaculty;
    }

    @Override
    public int getCount() {
        return tutorName.size();
    }

    @Override
    public Object getItem(int position) {
        return tutorFaculty.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflaInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = inflaInflater.inflate(R.layout.list_tutors, parent, false);
        }

        TextView name = convertView.findViewById(R.id.fullname);
        TextView fac = convertView.findViewById(R.id.faculty);

        name.setText(tutorName.get(position));
        fac.setText(tutorFaculty.get(position));

        return convertView;
    }
}
