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
import android.widget.ImageView;
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
    ArrayList<String> arr_tutDate;
    String from;
    Dialog myDialog;

    public TutorialProfileAdapter(Context context, ArrayList<String> tutName, ArrayList<String> tutCategory, ArrayList<String> tutDescription, ArrayList<String> tutMode, ArrayList<String> tutId, ArrayList<String> tutDate, String from){
        //Getting all the values
        this.context = context;
        this.arr_tutName = tutName;
        this.arr_tutCategory = tutCategory;
        this.arr_tutDescription = tutDescription;
        this.arr_tutMode = tutMode;
        this.arr_tutId = tutId;
        this.arr_tutDate = tutDate;
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

        if (arr_tutMode.get(i).equals("pending")){
            linLay.setBackgroundResource(R.drawable.rounded_grey2);
        }

        linLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(from.equals("joined")) {

                    if (arr_tutMode.get(i).equals("pending")){
                        myDialog = new Dialog(context);
                        myDialog.setContentView(R.layout.custom_popup_upload_successful);
                        ImageView img = myDialog.findViewById(R.id.img);
                        TextView text = myDialog.findViewById(R.id.text);
                        Button ok = myDialog.findViewById(R.id.addmore);
                        Button two = myDialog.findViewById(R.id.viewgroup);
                        two.setVisibility(View.GONE);
                        ok.setText("OK");
                        ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                myDialog.dismiss();
                            }
                        });
                        text.setText("Sorry, your approval is still pending.");
                        img.setImageResource(R.drawable.notification);
                        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        myDialog.setCanceledOnTouchOutside(true);
                        myDialog.show();
                    }
                    else if (arr_tutMode.get(i).equals("approved")){
                        Intent intent = new Intent(context, ResourceViewerView2.class);
                        intent.putExtra("name", arr_tutName.get(i));
                        intent.putExtra("category", arr_tutCategory.get(i));
                        intent.putExtra("description", arr_tutDescription.get(i));
                        intent.putExtra("mode", arr_tutMode.get(i));
                        intent.putExtra("date", arr_tutDate.get(i));
                        intent.putExtra("id", arr_tutId.get(i));
                        context.startActivity(intent);
                    }
                    else{
                        myDialog = new Dialog(context);
                        myDialog.setContentView(R.layout.custom_popup_upload_successful);
                        ImageView img = myDialog.findViewById(R.id.img);
                        TextView text = myDialog.findViewById(R.id.text);
                        Button ok = myDialog.findViewById(R.id.addmore);
                        Button two = myDialog.findViewById(R.id.viewgroup);
                        two.setVisibility(View.GONE);
                        ok.setText("OK");
                        ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                myDialog.dismiss();
                            }
                        });
                        text.setText("Sorry, your approval is still pending.");
                        img.setImageResource(R.drawable.notification);
                        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        myDialog.setCanceledOnTouchOutside(true);
                        myDialog.show();
                    }

                }

                if(from.equals("created")){
                    Intent intent2 = new Intent(context, ClickTutOnProfile.class);
                    intent2.putExtra("groupName", arr_tutName.get(i));
                    intent2.putExtra("name", arr_tutName.get(i));
                    intent2.putExtra("category", arr_tutCategory.get(i));
                    intent2.putExtra("description", arr_tutDescription.get(i));
                    intent2.putExtra("mode", arr_tutMode.get(i));
                    intent2.putExtra("id", arr_tutId.get(i));
                    intent2.putExtra("date", arr_tutDate.get(i));
                    context.startActivity(intent2);
                }
            }
        });

        return convertView;
    }
}
