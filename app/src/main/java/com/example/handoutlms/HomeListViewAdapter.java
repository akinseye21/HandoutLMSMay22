package com.example.handoutlms;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;

import java.util.ArrayList;

public class HomeListViewAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<String> type;
    private ArrayList<String> created_by;
    private ArrayList<String> created_by_name;
    private ArrayList<String> group_name;
    private ArrayList<String> university;
    private ArrayList<String> mode;
    private ArrayList<String> group_name_inside;
    private ArrayList<String> description;
    private ArrayList<String> time;
    private ArrayList<String> date;
    private ArrayList<String> card_mode;
    private ArrayList<String> category;
    private ArrayList<String> id;

    Dialog myDialog, myDialog2;
    AlertDialog.Builder builder;


    public HomeListViewAdapter (Context context, ArrayList<String> type, ArrayList<String> created_by, ArrayList<String> created_by_name, ArrayList<String> group_name, ArrayList<String> university, ArrayList<String> mode, ArrayList<String> group_name_inside, ArrayList<String> description, ArrayList<String> time, ArrayList<String> date, ArrayList<String> card_mode, ArrayList<String> category, ArrayList<String> id){
        //Getting all the values
        this.context = context;
        this.type = type;
        this.created_by = created_by;
        this.created_by_name = created_by_name;
        this.group_name = group_name;
        this.university = university;
        this.mode = mode;
        this.group_name_inside = group_name_inside;
        this.description = description;
        this.time = time;
        this.date = date;
        this.card_mode = card_mode;
        this.category = category;
        this.id = id;
    }

    @Override
    public int getCount() {
        return created_by.size();
    }

    @Override
    public Object getItem(int position) {
        return created_by.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflaInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = inflaInflater.inflate(R.layout.list_homeview, parent, false);
        }

        //get views for gig card
        CardView card_gig = convertView.findViewById(R.id.card_gig);
        TextView name_gig = convertView.findViewById(R.id.group_name_inside_gig);
        TextView desc_gig = convertView.findViewById(R.id.description_gig);
        TextView budget_gig = convertView.findViewById(R.id.budget_category_gig);
        TextView crt_by_gig = convertView.findViewById(R.id.created_by_gig);
        TextView dept_gig = convertView.findViewById(R.id.dept_gig);
        TextView uni_gig = convertView.findViewById(R.id.uni_gig);


        //get views for tutorials
        CardView card_tutorial = convertView.findViewById(R.id.card_tutors);
        TextView name_tutorial = convertView.findViewById(R.id.group_name_inside_tutor);
        TextView category_tutorial = convertView.findViewById(R.id.category_tutor);
        TextView desc_tutorial = convertView.findViewById(R.id.description_tutor);
        TextView date_tutorial = convertView.findViewById(R.id.date_tutor);
        TextView crt_by_tutorial = convertView.findViewById(R.id.created_by_tutor);
        TextView dept_tutorial = convertView.findViewById(R.id.dept_tutor);
        TextView uni_tutorial = convertView.findViewById(R.id.uni_tutor);


        //get views for game
        CardView card_game = convertView.findViewById(R.id.card_game);
        TextView name_game = convertView.findViewById(R.id.name_game);
        TextView location_game = convertView.findViewById(R.id.game_location);
        TextView date_game = convertView.findViewById(R.id.game_date);
        TextView time_game = convertView.findViewById(R.id.game_time);
        TextView crt_by_game = convertView.findViewById(R.id.created_by_game);
        TextView dept_gamel = convertView.findViewById(R.id.dept_game);
        TextView uni_game = convertView.findViewById(R.id.uni_game);


        if (type.get(position).equals("group")){
            //set tutorial cardview visible
            card_gig.setVisibility(View.GONE);
            card_tutorial.setVisibility(View.VISIBLE);
            card_game.setVisibility(View.GONE);

            name_tutorial.setText(group_name_inside.get(position));
            category_tutorial.setText(category.get(position));
            crt_by_tutorial.setText(created_by_name.get(position));
            uni_tutorial.setText(university.get(position));
            desc_tutorial.setText(description.get(position));
            date_tutorial.setText(date.get(position));
            dept_tutorial.setText(mode.get(position));
        }


        if (type.get(position).equals("gigs")){
            card_gig.setVisibility(View.VISIBLE);
            card_tutorial.setVisibility(View.GONE);
            card_game.setVisibility(View.GONE);

            name_gig.setText(group_name.get(position));
            desc_gig.setText(description.get(position));
            budget_gig.setText(category.get(position));
            crt_by_gig.setText(created_by_name.get(position));
            dept_gig.setText(time.get(position));
            uni_gig.setText(university.get(position));
        }

        card_gig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //load the custom dialog box
                myDialog = new Dialog(context);
                myDialog.setContentView(R.layout.card_gig_click_popup);
                //get views in the popup page
                LinearLayout profile = myDialog.findViewById(R.id.profile);
                profile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //move to the user profile page
                        Intent i = new Intent(context, ProfileOthers.class);
                        //pass the email of the selected user
                        i.putExtra("email", created_by.get(position));
                        context.startActivity(i);
                    }
                });
                TextView pop_name = myDialog.findViewById(R.id.created_by_gig);
                pop_name.setText(created_by_name.get(position));
                TextView pop_department = myDialog.findViewById(R.id.dept_gig);
                pop_department.setText(time.get(position));
                TextView pop_school = myDialog.findViewById(R.id.uni_gig);
                pop_school.setText(university.get(position));
                TextView pop_gigname = myDialog.findViewById(R.id.gig_name);
                pop_gigname.setText(group_name.get(position));
                TextView pop_gigdescription = myDialog.findViewById(R.id.gig_description);
                pop_gigdescription.setText(description.get(position));
                TextView pop_gigcategory = myDialog.findViewById(R.id.gig_category);
                pop_gigcategory.setText(category.get(position));
                Button bid = myDialog.findViewById(R.id.bid);
                bid.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //move to the next gig page
                        Intent i = new Intent(context, CardGigClick2.class);
                        i.putExtra("name", created_by_name.get(position));
                        i.putExtra("department", time.get(position));
                        i.putExtra("school", university.get(position));
                        i.putExtra("gig_name", group_name.get(position));
                        i.putExtra("gig_description", description.get(position));
                        i.putExtra("payment_structure", category.get(position));
                        i.putExtra("id", id.get(position));
                        context.startActivity(i);
                    }
                });

                myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                myDialog.setCanceledOnTouchOutside(true);
                myDialog.show();
            }
        });

        card_tutorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //load the custom dialog box
                myDialog = new Dialog(context);
                myDialog.setContentView(R.layout.card_tutorial_click_popup);
                //get views in the popup page
                LinearLayout profile = myDialog.findViewById(R.id.profile);
                profile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //

                        //move to the user profile page
                        Intent i = new Intent(context, ProfileOthers.class);
                        //pass the email of the selected user
                        i.putExtra("email", created_by.get(position));
                        context.startActivity(i);
                    }
                });
                TextView pop_name = myDialog.findViewById(R.id.created_by);
                pop_name.setText(created_by_name.get(position));
                TextView pop_department = myDialog.findViewById(R.id.dept);
                pop_department.setText(mode.get(position));
                TextView pop_university = myDialog.findViewById(R.id.uni_gig);
                pop_university.setText(university.get(position));
                TextView pop_gigname = myDialog.findViewById(R.id.gig_name);
                pop_gigname.setText(group_name.get(position));
                TextView pop_gigcategory = myDialog.findViewById(R.id.gig_category);
                pop_gigcategory.setText(category.get(position));
                TextView pop_gigdescription = myDialog.findViewById(R.id.gig_desc);
                pop_gigdescription.setText(description.get(position));
                TextView pop_gigdate = myDialog.findViewById(R.id.tut_date);
                pop_gigdate.setText(date.get(position));
                TextView pop_cardmode = myDialog.findViewById(R.id.mode);
                pop_cardmode.setText(card_mode.get(position));
                if(card_mode.get(position).equals("Online")){
                    //set the location of the tutorial
                }else{
                    //do not set the location of the tutorial
                }


                builder = new AlertDialog.Builder(context);
                Button join = myDialog.findViewById(R.id.join);
                join.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //show 'done'
                        builder.setMessage("Do you want to join '"+group_name.get(position)+"'?")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        //show popUpWindow
                                        myDialog2 = new Dialog(context);
                                        myDialog2.setContentView(R.layout.custom_popup_tutorial_joined);
                                        TextView popup_tutName = myDialog2.findViewById(R.id.tutName);
                                        Button close = myDialog2.findViewById(R.id.close);
                                        popup_tutName.setText(group_name.get(position));
                                        close.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                myDialog2.dismiss();
                                            }
                                        });
                                        myDialog2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                        myDialog2.setCanceledOnTouchOutside(false);
                                        myDialog2.show();
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        //  Action for 'NO' Button
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alert = builder.create();
                        //Setting the title manually
                        alert.setTitle("Join Tutorial - "+group_name.get(position));
                        alert.show();
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
