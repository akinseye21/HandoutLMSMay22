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

public class TutorialProfileAdapter extends BaseAdapter {

    private Context context;
    ArrayList<String> arr_tutName;
    ArrayList<String> arr_tutCategory;
    ArrayList<String> arr_tutDescription;
    ArrayList<String> arr_tutMode;
    ArrayList<String> arr_tutId;
    String from;

    public TutorialProfileAdapter(Context context, ArrayList<String> tutName, ArrayList<String> tutCategory, ArrayList<String> tutDescription, ArrayList<String> tutMode, ArrayList<String> tutId, String from){
        //Getting all the values
        this.context = context;
        this.arr_tutName = tutName;
        this.arr_tutCategory = tutCategory;
        this.arr_tutDescription = tutDescription;
        this.arr_tutMode = tutMode;
        this.arr_tutId = tutId;
        this.from = from;
    }


    @Override
    public int getCount() {
        return arr_tutName.size();
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
    public View getView(int i, View convertView, ViewGroup parent) {
        LayoutInflater inflaInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = inflaInflater.inflate(R.layout.list_tutorial, parent, false);
        }

        LinearLayout linLay = convertView.findViewById(R.id.lin_lay);
        TextView tutname = convertView.findViewById(R.id.tut_name);
        TextView tutcategory = convertView.findViewById(R.id.tut_category);
        TextView tutdescription = convertView.findViewById(R.id.tut_description);
        TextView tutmode = convertView.findViewById(R.id.tut_mode);

        tutname.setText(arr_tutName.get(i));
        tutcategory.setText(arr_tutCategory.get(i));
        tutdescription.setText(arr_tutDescription.get(i));
        tutmode.setText(arr_tutMode.get(i));

        linLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(from.equals("joined")) {
                    //send to ViewOnlineJoinedTutorial
                    if(arr_tutMode.get(i).equals("online")){
                        //send to online
                        Intent intent = new Intent(context, ViewOnlineJoinedTutorial.class);
                        intent.putExtra("name", arr_tutName.get(i));
                        intent.putExtra("category", arr_tutCategory.get(i));
                        intent.putExtra("description", arr_tutDescription.get(i));
                        intent.putExtra("mode", arr_tutMode.get(i));
                        intent.putExtra("id", arr_tutId.get(i));
                        context.startActivity(intent);
                    }else{
                        //send to offline
                        Intent intent = new Intent(context, ViewOfflineJoinedTutorial.class);
                        intent.putExtra("name", arr_tutName.get(i));
                        intent.putExtra("category", arr_tutCategory.get(i));
                        intent.putExtra("description", arr_tutDescription.get(i));
                        intent.putExtra("mode", arr_tutMode.get(i));
                        intent.putExtra("id", arr_tutId.get(i));
                        context.startActivity(intent);
                    }

                }

                if(from.equals("created")){
                    //send to ViewOnlineJoinedTutorial
                    Intent intent = new Intent(context, ViewTutorCreatedGroup.class);
                    intent.putExtra("name", arr_tutName.get(i));
                    intent.putExtra("category", arr_tutCategory.get(i));
                    intent.putExtra("description", arr_tutDescription.get(i));
                    intent.putExtra("mode", arr_tutMode.get(i));
                    intent.putExtra("id", arr_tutId.get(i));
                    context.startActivity(intent);
                }
            }
        });

        return convertView;
    }
}
