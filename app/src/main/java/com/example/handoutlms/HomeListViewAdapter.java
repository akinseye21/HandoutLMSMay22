package com.example.handoutlms;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import java.util.ArrayList;

public class HomeListViewAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<String> created_by;
    private ArrayList<String> created_by_name;
    private ArrayList<String> group_name;
    private ArrayList<String> university;
    private ArrayList<String> mode;
    private ArrayList<String> group_name_inside;
    private ArrayList<String> description;
    private ArrayList<String> time;
    private ArrayList<String> category;

    public HomeListViewAdapter (Context context, ArrayList<String> created_by, ArrayList<String> created_by_name, ArrayList<String> group_name, ArrayList<String> university, ArrayList<String> mode, ArrayList<String> group_name_inside, ArrayList<String> description, ArrayList<String> time, ArrayList<String> category){
        //Getting all the values
        this.context = context;
        this.created_by = created_by;
        this.created_by_name = created_by_name;
        this.group_name = group_name;
        this.university = university;
        this.mode = mode;
        this.group_name_inside = group_name_inside;
        this.description = description;
        this.time = time;
        this.category = category;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflaInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = inflaInflater.inflate(R.layout.list_homeview, parent, false);
        }

        //get views for gig card
        CardView card_gig = convertView.findViewById(R.id.card_gig);
        TextView name_gig = convertView.findViewById(R.id.group_name_inside_gig);
        TextView desc_gig = convertView.findViewById(R.id.description_gig);
        TextView budget_gig = convertView.findViewById(R.id.description_gig);
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



        name_tutorial.setText(group_name_inside.get(position));
        category_tutorial.setText(group_name.get(position));
        crt_by_tutorial.setText(created_by_name.get(position));
        uni_tutorial.setText(university.get(position));
        desc_tutorial.setText(description.get(position));
        date_tutorial.setText(time.get(position));
        dept_tutorial.setText(mode.get(position));

        card_gig.setVisibility(View.GONE);
        card_tutorial.setVisibility(View.VISIBLE);
        card_game.setVisibility(View.GONE);

        if (category.get(position).equals("")){
            //change the background image
        }


        return convertView;
    }
}
