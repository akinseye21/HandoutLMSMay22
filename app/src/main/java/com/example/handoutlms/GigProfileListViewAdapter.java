package com.example.handoutlms;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class GigProfileListViewAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<String> gigName;
    private ArrayList<String> gigPrice;
    private ArrayList<String> gigType;
    private ArrayList<String> gigSkills;
    private ArrayList<String> gigPaymentMode;
    private ArrayList<String> gigDescription;
    private ArrayList<String> gigId;
    private String info;

    public GigProfileListViewAdapter(Context context, ArrayList<String> gigName, ArrayList<String> gigPrice, ArrayList<String> gigType, ArrayList<String> gigSkills, ArrayList<String> gigPaymentMode, ArrayList<String> gigDescription, ArrayList<String> gigId, String info){
        //Getting all the values
        this.context = context;
        this.gigName = gigName;
        this.gigPrice = gigPrice;
        this.gigType = gigType;
        this.gigSkills = gigSkills;
        this.gigPaymentMode = gigPaymentMode;
        this.gigDescription = gigDescription;
        this.gigId = gigId;
        this.info = info;
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
            convertView = inflaInflater.inflate(R.layout.list_gig, parent, false);
        }

        TextView name = convertView.findViewById(R.id.gig_name);
        TextView price = convertView.findViewById(R.id.gig_price);
        TextView typ = convertView.findViewById(R.id.gig_type);
        LinearLayout linlay = convertView.findViewById(R.id.lin_lay);

        name.setText(gigName.get(position));
        price.setText(gigPrice.get(position));
        typ.setText(gigType.get(position));

        if(info.equals("myProfile")){
            linlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, ShowCreatedGigs.class);
                    i.putExtra("gigname", gigName.get(position));
                    i.putExtra("gigdescription", gigDescription.get(position));
                    i.putExtra("gigskills", gigSkills.get(position));
                    i.putExtra("gigprice", gigPrice.get(position));
                    i.putExtra("gigId", gigId.get(position));
                    context.startActivity(i);
                }
            });

        }else if(info.equals("otherProfile")){

        }else if(info.equals("myBid")){
            linlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "You have placed a bid already. Please check back to know your approval status", Toast.LENGTH_LONG).show();
                }
            });

        }


        return convertView;
    }
}
