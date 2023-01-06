package com.example.handoutlms;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;

public class VideoLinkAdapter extends BaseAdapter {


    Context context;
    int counter;
    String groupName;
    String got_email;
    LayoutInflater inflter;


    public VideoLinkAdapter(Context applicationContext, int counter, String groupName, String got_email){
        this.context = applicationContext;
        this.counter = counter;
        this.groupName = groupName;
        this.got_email = got_email;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return counter;
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

        convertView = inflter.inflate(R.layout.list_video_links, null);

        EditText groupName1 = convertView.findViewById(R.id.edtGroupName);
        EditText email = convertView.findViewById(R.id.edtEmail);
        EditText description = convertView.findViewById(R.id.edtDescription);
        EditText link = convertView.findViewById(R.id.edtLink);

        groupName1.setText(groupName);
        email.setText(got_email);
        String myLink = link.getText().toString().trim();
        String myDescription = description.getText().toString().trim();

        link.setText(myLink);
        description.setText(myDescription);

        return convertView;
    }
}
