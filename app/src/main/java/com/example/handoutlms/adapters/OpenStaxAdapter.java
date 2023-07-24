package com.example.handoutlms.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.handoutlms.R;

import java.util.ArrayList;

public class OpenStaxAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<String> generalTitle;
    private ArrayList<String> generalCoverUrl;
    private ArrayList<String> generalPdfUrl;
    private ArrayList<String> generalSalesforceName;
    private ArrayList<String> generalSubject;
    private ArrayList<String> generalPosition;

    public OpenStaxAdapter(Context context, ArrayList<String> generalTitle, ArrayList<String> generalCoverUrl, ArrayList<String> generalPdfUrl,
                                ArrayList<String> generalSalesforceName, ArrayList<String> generalSubject, ArrayList<String> generalPosition){
        //Getting all the values
        this.context = context;
        this.generalTitle = generalTitle;
        this.generalCoverUrl = generalCoverUrl;
        this.generalPdfUrl = generalPdfUrl;
        this.generalSalesforceName = generalSalesforceName;
        this.generalSubject = generalSubject;
        this.generalPosition = generalPosition;
    }

    @Override
    public int getCount() {
        return generalTitle.size();
    }

    @Override
    public Object getItem(int i) {
        return generalTitle.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflaInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = inflaInflater.inflate(R.layout.list_openstax_lib, parent, false);
        }

        ImageView image = convertView.findViewById(R.id.image);
        Glide.with(context).load(generalCoverUrl.get(position)).into(image);

        return convertView;
    }
}
