package com.example.handoutlms.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.example.handoutlms.R;
import com.example.handoutlms.activities.FolderFilePage;
import com.example.handoutlms.fragments.FragmentHandout;

import java.util.ArrayList;

public class FolderAdapter extends BaseAdapter {

    Context context;
    ArrayList<String> name;
    ArrayList<Integer> count;

    public FolderAdapter(Context context, ArrayList<String> name, ArrayList<Integer> count) {
        this.context = context;
        this.name = name;
        this.count = count;
    }

    @Override
    public int getCount() {
        return name.size();
    }

    @Override
    public Object getItem(int i) {
        return name.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        LayoutInflater inflaInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = inflaInflater.inflate(R.layout.list_folder, viewGroup, false);
        }

        LinearLayout folderclick = convertView.findViewById(R.id.folderclick);
        TextView foldername = convertView.findViewById(R.id.foldername);
        TextView foldercount = convertView.findViewById(R.id.foldercount);
        CardView plus = convertView.findViewById(R.id.plus);

        foldername.setText(name.get(i));
        foldercount.setText("File No: "+count.get(i));
        folderclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendToIntent(name.get(i), count.get(i));
            }
        });
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendToIntent(name.get(i), count.get(i));
            }
        });

        return convertView;
    }

    private void sendToIntent(String name, int count) {
        Intent intent = new Intent(context, FolderFilePage.class);
        intent.putExtra("foldername", name);
        intent.putExtra("foldercount", count);
        context.startActivity(intent);
    }

}
