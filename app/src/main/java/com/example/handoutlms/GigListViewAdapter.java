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
    private ArrayList<String> gigType;
    private ArrayList<String> gigSkills;
    private ArrayList<String> gigPaymentMode;
    private ArrayList<String> gigDescription;
    private ArrayList<String> gigId;
    private ArrayList<String> fullname;
    private ArrayList<String> institution;
    private ArrayList<String> department;

    Dialog myDialog;

    public GigListViewAdapter(Context context, ArrayList<String> gigName, ArrayList<String> gigPrice, ArrayList<String> gigType, ArrayList<String> gigSkills, ArrayList<String> gigPaymentMode, ArrayList<String> gigDescription, ArrayList<String> gigId, ArrayList<String> fullname, ArrayList<String> institution, ArrayList<String> department){
        //Getting all the values
        this.context = context;
        this.gigName = gigName;
        this.gigPrice = gigPrice;
        this.gigType = gigType;
        this.gigSkills = gigSkills;
        this.gigPaymentMode = gigPaymentMode;
        this.gigDescription = gigDescription;
        this.gigId = gigId;
        this.fullname = fullname;
        this.institution = institution;
        this.department = department;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflaInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = inflaInflater.inflate(R.layout.list_gig_home, parent, false);
        }

        LinearLayout lin_lay = convertView.findViewById(R.id.lin_lay);
        TextView name = convertView.findViewById(R.id.gig_name);
        TextView price = convertView.findViewById(R.id.gig_price);
        TextView type = convertView.findViewById(R.id.gig_type);

        name.setText(gigName.get(position));
        price.setText(gigPrice.get(position));
        type.setText(gigType.get(position));

        lin_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //load the custom dialog box
                myDialog = new Dialog(context);
                myDialog.setContentView(R.layout.card_gig_click_popup);
                //get views in the popup page
                TextView pop_name = myDialog.findViewById(R.id.created_by_gig);
                pop_name.setText(fullname.get(position));
                TextView pop_department = myDialog.findViewById(R.id.dept_gig);
                pop_department.setText(department.get(position));
                TextView pop_school = myDialog.findViewById(R.id.uni_gig);
                pop_school.setText(institution.get(position));
                TextView pop_gigname = myDialog.findViewById(R.id.gig_name);
                pop_gigname.setText(gigName.get(position));
                TextView pop_gigdescription = myDialog.findViewById(R.id.gig_description);
                pop_gigdescription.setText(gigDescription.get(position));
                TextView pop_gigskills = myDialog.findViewById(R.id.skills);
                pop_gigskills.setText(gigSkills.get(position));
                TextView pop_gigcategory = myDialog.findViewById(R.id.gig_category);
                pop_gigcategory.setText(gigPrice.get(position));
                Button bid = myDialog.findViewById(R.id.bid);
                bid.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //move to the next gig page
                        Intent i = new Intent(context, CardGigClick2.class);
                        i.putExtra("gig_name", gigName.get(position));
                        i.putExtra("gig_description", gigDescription.get(position));
                        i.putExtra("payment_structure", gigPrice.get(position));
                        i.putExtra("id", gigId.get(position));
                        i.putExtra("name", fullname.get(position));
                        i.putExtra("school", institution.get(position));
                        i.putExtra("department", department.get(position));
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
