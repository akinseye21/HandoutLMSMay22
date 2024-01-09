package com.example.handoutlms.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.example.handoutlms.R;
import com.example.handoutlms.activities.WatchVideo;

import java.util.ArrayList;

public class FileAdapter extends BaseAdapter {

    Context context;
    ArrayList<String> file;
    ArrayList<Integer> id;
    ArrayList<String> filename;
    ArrayList<String> thumbnail;

    public FileAdapter(Context context, ArrayList<String> file, ArrayList<Integer> id, ArrayList<String> filename, ArrayList<String> thumbnail) {
        this.context = context;
        this.file = file;
        this.id = id;
        this.filename = filename;
        this.thumbnail = thumbnail;
    }

    @Override
    public int getCount() {
        return file.size();
    }

    @Override
    public Object getItem(int i) {
        return file.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        LayoutInflater inflaInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = inflaInflater.inflate(R.layout.list_file, viewGroup, false);
        }

        LinearLayout fileclick = convertView.findViewById(R.id.folderclick);
        ImageView thumbnaild = convertView.findViewById(R.id.thumbnail);
        TextView filenamed = convertView.findViewById(R.id.filename);

        if (filename.get(i) == "null") {
            filenamed.setText("No name");
        }else{
            filenamed.setText(filename.get(i));
        }

        Glide.with(context).load(thumbnail.get((i))).into(thumbnaild);
        fileclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent j = new Intent(context, WatchVideo.class);
                j.putExtra("link", file.get(i));
                j.putExtra("name", filename.get(i));
                j.putExtra("from", "handout_page");
                context.startActivity(j);
            }
        });

        return convertView;
    }
}
