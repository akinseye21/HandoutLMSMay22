package com.example.handoutlms;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class UsersAdapter extends BaseAdapter {

    Context context;
    ArrayList<String> arr_key;
    ArrayList<String> arr_id;
    ArrayList<String> arr_name;
    ArrayList<String> arr_email;
    ArrayList<String> arr_institution;
    String senderKey;
    LayoutInflater inflter;

    public UsersAdapter(Context context, ArrayList<String> arr_key, ArrayList<String> arr_id, ArrayList<String> arr_name, ArrayList<String> arr_email, ArrayList<String> arr_institution, String senderKey){
        this.context = context;
        this.arr_key = arr_key;
        this.arr_id = arr_id;
        this.arr_name = arr_name;
        this.arr_email = arr_email;
        this.arr_institution = arr_institution;
        this.senderKey = senderKey;
        inflter = (LayoutInflater.from(context));
    }

    @Override
    public int getCount() {
        return arr_id.size();
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
        convertView = inflter.inflate(R.layout.list_chat, null);

        LinearLayout myList = convertView.findViewById(R.id.myList);
        TextView name = convertView.findViewById(R.id.name);
        TextView description = convertView.findViewById(R.id.description);

        name.setText(arr_name.get(i));
        description.setText(arr_institution.get(i));
        myList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //move to the chat page
                Intent j = new Intent(context, ChatMessagingPage.class);
                j.putExtra("key", arr_key.get(i));
                j.putExtra("id", arr_id.get(i));
                j.putExtra("name", arr_name.get(i));
                j.putExtra("email", arr_email.get(i));
                j.putExtra("institution", arr_institution.get(i));
                j.putExtra("senderKey", senderKey);
                context.startActivity(j);
            }
        });

        return convertView;
    }
}
