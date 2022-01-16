package com.example.handoutlms;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class GroupListViewAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<String> groupName;
    private ArrayList<String> groupTime;
    private ArrayList<String> groupTutor;

    public GroupListViewAdapter(Context context, ArrayList<String> groupName, ArrayList<String> groupTime, ArrayList<String> groupTutor){
        //Getting all the values
        this.context = context;
        this.groupName = groupName;
        this.groupTime = groupTime;
        this.groupTutor = groupTutor;
    }
    @Override
    public int getCount() {
        return groupName.size();
    }

    @Override
    public Object getItem(int position) {
        return groupName.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflaInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = inflaInflater.inflate(R.layout.list_tutors_group, parent, false);
        }

        TextView name = convertView.findViewById(R.id.groupName);
        TextView time = convertView.findViewById(R.id.groupTime);
        TextView tutor = convertView.findViewById(R.id.groupTutor);

        name.setText(groupName.get(position));
        time.setText(groupTime.get(position));
        tutor.setText("Tutor: "+groupTutor.get(position));

        return convertView;
    }
}
