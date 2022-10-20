package com.example.handoutlms;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class NotificationAdapter extends BaseAdapter {

    Context context;
    ArrayList<String> notification_title;
    ArrayList<String> notification_message;
    ArrayList<String> notification_status;
    LayoutInflater inflter;

    public NotificationAdapter(Context applicationContext, ArrayList<String> notification_title, ArrayList<String> notification_message, ArrayList<String> notification_status){
        this.context = applicationContext;
        this.notification_title = notification_title;
        this.notification_message = notification_message;
        this.notification_status = notification_status;
        inflter = (LayoutInflater.from(applicationContext));
    }



    @Override
    public int getCount() {
        return notification_title.size();
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
        convertView = inflter.inflate(R.layout.list_notification, null);

        TextView title = convertView.findViewById(R.id.notification_title);
        TextView message = convertView.findViewById(R.id.notification_message);

        title.setText(notification_title.get(i));
        message.setText(notification_message.get(i));

        return convertView;
    }
}
