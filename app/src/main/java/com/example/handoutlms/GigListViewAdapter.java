package com.example.handoutlms;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class GigListViewAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<String> gigName;
    private ArrayList<String> gigPrice;
    private ArrayList<String> gigTime;

    public GigListViewAdapter(Context context, ArrayList<String> gigName, ArrayList<String> gigPrice, ArrayList<String> gigTime){
        //Getting all the values
        this.context = context;
        this.gigName = gigName;
        this.gigPrice = gigPrice;
        this.gigTime = gigTime;
    }

    @Override
    public int getCount() {
        return gigName.size();
    }

    @Override
    public Object getItem(int position) {
        return gigName.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflaInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = inflaInflater.inflate(R.layout.list_gig, parent, false);
        }

        TextView name = convertView.findViewById(R.id.gig_name);
        TextView price = convertView.findViewById(R.id.gig_price);
        TextView time = convertView.findViewById(R.id.gig_time);

        name.setText(gigName.get(position));
        price.setText(gigPrice.get(position));
        time.setText(gigTime.get(position));

        return convertView;
    }
}
