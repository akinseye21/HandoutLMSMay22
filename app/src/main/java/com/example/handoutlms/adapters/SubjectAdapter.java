package com.example.handoutlms.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.handoutlms.R;
import com.example.handoutlms.activities.TriviaInstruction;

import java.util.ArrayList;

public class SubjectAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<String> subject;
    private  String category;



    public SubjectAdapter(Context context, ArrayList<String> subject, String category){
        //Getting all the values
        this.context = context;
        this.subject = subject;
        this.category = category;
    }

    @Override
    public int getCount() {
        return subject.size();
    }

    @Override
    public Object getItem(int i) {
        return subject.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        LayoutInflater inflaInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = inflaInflater.inflate(R.layout.list_subject, parent, false);
        }

        LinearLayout myLin = convertView.findViewById(R.id.myLin);
        TextView name = convertView.findViewById(R.id.text);
//        ImageView img = convertView.findViewById(R.id.image);

        name.setText(subject.get(i));

        myLin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent j = new Intent(context, TriviaInstruction.class);
                j.putExtra("Text", subject.get(i));
                j.putExtra("From", "9ja");
                j.putExtra("ExamType", category);
                j.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(j);
            }
        });


        return convertView;
    }

}
