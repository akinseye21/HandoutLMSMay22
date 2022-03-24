package com.example.handoutlms;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class GigListViewAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<String> gigName;
    private ArrayList<String> gigPrice;
    private ArrayList<String> gigTime;

    Dialog myDialog;

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
            convertView = inflaInflater.inflate(R.layout.list_gig_home, parent, false);
        }

        LinearLayout lin_lay = convertView.findViewById(R.id.lin_lay);
        TextView name = convertView.findViewById(R.id.gig_name);
        TextView price = convertView.findViewById(R.id.gig_price);
        TextView time = convertView.findViewById(R.id.gig_time);

        name.setText(gigName.get(position));
        price.setText(gigPrice.get(position));
        time.setText(gigTime.get(position));

        lin_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //load the custom dialog box
                myDialog = new Dialog(context);
                myDialog.setContentView(R.layout.card_gig_click_popup);
                //get views in the popup page
                TextView pop_name = myDialog.findViewById(R.id.created_by_gig);
//                pop_name.setText(created_by_name.get(position));
                TextView pop_department = myDialog.findViewById(R.id.dept_gig);
//                pop_department.setText(time.get(position));
                TextView pop_school = myDialog.findViewById(R.id.uni_gig);
//                pop_school.setText(university.get(position));
                TextView pop_gigname = myDialog.findViewById(R.id.gig_name);
//                pop_gigname.setText(group_name.get(position));
                TextView pop_gigdescription = myDialog.findViewById(R.id.gig_description);
//                pop_gigdescription.setText(description.get(position));
                TextView pop_gigcategory = myDialog.findViewById(R.id.gig_category);
//                pop_gigcategory.setText(category.get(position));
                Button bid = myDialog.findViewById(R.id.bid);
                bid.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //move to the next gig page
                        Intent i = new Intent(context, CardGigClick2.class);
                        context.startActivity(i);
                    }
                });

                myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                myDialog.setCanceledOnTouchOutside(true);
                myDialog.show();
            }
        });

        return convertView;
    }
}
