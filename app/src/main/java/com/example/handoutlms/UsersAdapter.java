package com.example.handoutlms;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class UsersAdapter extends BaseAdapter {

    Context context;
    ArrayList<String> arr_key;
    ArrayList<String> arr_id;
    ArrayList<String> arr_name;
    ArrayList<String> arr_email;
    ArrayList<String> arr_institution;
    String senderKey;
    LayoutInflater inflter;
    ArrayList<String> arr_status;

    public UsersAdapter(Context context, ArrayList<String> arr_key, ArrayList<String> arr_id, ArrayList<String> arr_name, ArrayList<String> arr_email, ArrayList<String> arr_institution, String senderKey, ArrayList<String> arr_status){
        this.context = context;
        this.arr_key = arr_key;
        this.arr_id = arr_id;
        this.arr_name = arr_name;
        this.arr_email = arr_email;
        this.arr_institution = arr_institution;
        this.senderKey = senderKey;
        this.arr_status = arr_status;
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
        TextView onoff = convertView.findViewById(R.id.txtonoff);
        CircleImageView img_on = convertView.findViewById(R.id.img_on);
        CircleImageView img_off = convertView.findViewById(R.id.img_off);

        if(arr_status.get(i).equals("online")){
            img_on.setVisibility(View.VISIBLE);
            img_off.setVisibility(View.GONE);
            onoff.setText("online");
            onoff.setTextColor(Color.parseColor("#05df29"));
        }else{
            img_on.setVisibility(View.GONE);
            img_off.setVisibility(View.VISIBLE);
            onoff.setText("offline");
            onoff.setTextColor(Color.parseColor("#bfbfbf"));
        }

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
//                j.putExtra("senderKey", senderKey);
                context.startActivity(j);
            }
        });

        return convertView;
    }
}
