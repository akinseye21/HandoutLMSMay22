package com.example.handoutlms;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class ChatAdapter extends BaseAdapter {

    Context context;
    ArrayList<String> arr_message;
    ArrayList<String> arr_sender;
    ArrayList<String> arr_receiver;
    String key;
    LayoutInflater inflter;

    public ChatAdapter(Context context, ArrayList<String> arr_message, ArrayList<String> arr_sender, ArrayList<String> arr_receiver, String key){
        this.context = context;
        this.arr_message = arr_message;
        this.arr_sender = arr_sender;
        this.arr_receiver = arr_receiver;
        this.key = key;
        inflter = (LayoutInflater.from(context));
    }

    @Override
    public int getCount() {
        return arr_message.size();
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
        convertView = inflter.inflate(R.layout.chat_item_left, null);

//        RelativeLayout relReceived = convertView.findViewById(R.id.one);
//        RelativeLayout relSent = convertView.findViewById(R.id.two);
//        TextView txtReceived = convertView.findViewById(R.id.MessageReceived);
//        TextView txtSent = convertView.findViewById(R.id.MessageSent);
//
//        if (arr_receiver.get(i).equals(key)){
//            relSent.setVisibility(View.VISIBLE);
//            relReceived.setVisibility(View.GONE);
//            txtSent.setText(arr_message.get(i));
//        }
//        if(arr_sender.get(i).equals(key)){
//            relSent.setVisibility(View.GONE);
//            relReceived.setVisibility(View.VISIBLE);
//            txtReceived.setText(arr_message.get(i));
//        }


        return convertView;
    }
}
