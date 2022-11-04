package com.example.handoutlms;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class GameAdapter extends BaseAdapter {

    Context context;
    ArrayList<String> name;
    ArrayList<String> date;
    ArrayList<String> time;
    ArrayList<String> location;
    ArrayList<String> createdby;
//    LayoutInflater inflter;

    public GameAdapter(Context applicationContext, ArrayList<String> name, ArrayList<String> date, ArrayList<String> time, ArrayList<String> location, ArrayList<String> createdby) {
        this.context = applicationContext;
        this.name = name;
        this.date = date;
        this.time = time;
        this.location = location;
        this.createdby = createdby;
//        inflter = (LayoutInflater.from(applicationContext));
    }


    @Override
    public int getCount() {
        return name.size();
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
        LayoutInflater inflaInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = inflaInflater.inflate(R.layout.list_game, viewGroup, false);
        }

//        convertView = inflter.inflate(R.layout.list_game, null);

        RelativeLayout card = convertView.findViewById(R.id.card);
        TextView gameName = convertView.findViewById(R.id.gameName);
        TextView gameDate = convertView.findViewById(R.id.gameDate);
        TextView gameTime = convertView.findViewById(R.id.gameTime);

        gameName.setText(name.get(i));
        gameDate.setText(date.get(i));
        gameTime.setText(time.get(i));

        return convertView;
    }
}
