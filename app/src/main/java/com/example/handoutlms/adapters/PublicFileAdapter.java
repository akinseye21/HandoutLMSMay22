package com.example.handoutlms.adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.handoutlms.R;
import com.example.handoutlms.activities.WatchVideo;

import java.util.ArrayList;

public class PublicFileAdapter extends BaseAdapter {

    Context context;
    ArrayList<Integer> id;
    ArrayList<String> folder;
    ArrayList<String> description;
    ArrayList<String> file;
    ArrayList<String> fullname;
    ArrayList<String> filename;
    ArrayList<String> thumbnail;


    public PublicFileAdapter(
            Context context,
            ArrayList<Integer> id,
            ArrayList<String> folder,
            ArrayList<String> description,
            ArrayList<String> file,
            ArrayList<String> fullname,
            ArrayList<String> filename,
            ArrayList<String> thumbnail) {
        this.context = context;
        this.id = id;
        this.folder = folder;
        this.description = description;
        this.file = file;
        this.fullname = fullname;
        this.filename = filename;
        this.thumbnail = thumbnail;
    }


    @Override
    public int getCount() {
        return id.size();
    }

    @Override
    public Object getItem(int i) {
        return id.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        LayoutInflater inflaInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = inflaInflater.inflate(R.layout.list_public_handout, viewGroup, false);
        }

        RelativeLayout folderclick = convertView.findViewById(R.id.folderclick);
        TextView filenamed = convertView.findViewById(R.id.filename);
        TextView username = convertView.findViewById(R.id.username);
        TextView foldername = convertView.findViewById(R.id.foldername);
        ImageView mythumbnail = convertView.findViewById(R.id.thumbnail);

        if (filename.get(i) != "null") {
            filenamed.setText(filename.get(i));
        }else{
            filenamed.setText("No name uploaded");
        }

        Glide.with(context).load(thumbnail.get(i)).into(mythumbnail);
        username.setText(fullname.get(i));
        foldername.setText(folder.get(i));
        folderclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog myDialog = new Dialog(context);
                myDialog.setContentView(R.layout.custom_popup_fileinformation);
                myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                TextView close = myDialog.findViewById(R.id.close);
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        myDialog.dismiss();
                    }
                });
                TextView filenames = myDialog.findViewById(R.id.filename);
                filenames.setText("File name: "+filename.get(i));
                TextView filedescription = myDialog.findViewById(R.id.filedescription);
                filedescription.setText(description.get(i));
                TextView uploadedby = myDialog.findViewById(R.id.uploadedby);
                uploadedby.setText("Uploaded by: "+fullname.get(i));
                ImageView thumbnail = myDialog.findViewById(R.id.thumbnail);
                thumbnail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent j = new Intent(context, WatchVideo.class);
                        j.putExtra("link", file.get(i));
                        j.putExtra("name", filename.get(i));
                        j.putExtra("from", "handout_page");
                        context.startActivity(j);
                    }
                });

                myDialog.setCanceledOnTouchOutside(true);
                myDialog.show();

            }
        });

        return convertView;
    }
}
