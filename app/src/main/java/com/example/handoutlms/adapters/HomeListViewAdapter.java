package com.example.handoutlms.adapters;

import static android.content.Context.NOTIFICATION_SERVICE;

import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.handoutlms.R;
import com.example.handoutlms.activities.CardGigClick2;
import com.example.handoutlms.activities.FeedsDashboard;
import com.example.handoutlms.activities.ProfileOthers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

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
    private ArrayList<String> pic;
    private ArrayList<String> total_approved;

    ArrayList<String> gig_id_bid = new ArrayList<>();
    ArrayList<String> gig_creator = new ArrayList<>();

    Dialog myDialog, myDialog2, myDialog3, myDialog4;
    AlertDialog.Builder builder;
    SharedPreferences preferences;
    String got_email;
    String tutname, status;
    String CHANNEL_ID = "channelID3";

    public static final String TASK_MANAGER = "https://handoutng.com/handouts/handout_task_manager";
    public static final String JOIN_TUTORIAL = "https://handoutng.com/handouts/handout_group_join";
    public static final String CHECK_STATUS = "https://handoutng.com/handouts/handout_user_joined_groups";
    public static final String GIGS_BIDDED = "https://handoutng.com/handouts/handout_get_user_bids_for_gigs";

    public HomeListViewAdapter (Context context, ArrayList<String> type, ArrayList<String> created_by, ArrayList<String> created_by_name, ArrayList<String> group_name, ArrayList<String> university, ArrayList<String> mode, ArrayList<String> group_name_inside, ArrayList<String> description, ArrayList<String> time, ArrayList<String> date, ArrayList<String> card_mode, ArrayList<String> category, ArrayList<String> id, ArrayList<String> pic, ArrayList<String> total_approved){
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
        this.pic = pic;
        this.total_approved = total_approved;
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

        preferences = context.getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        got_email = preferences.getString("email", "not available");

        //get views for gig card
        RelativeLayout card_gig = convertView.findViewById(R.id.card_gig);
        TextView name_gig = convertView.findViewById(R.id.group_name_inside_gig);
        TextView desc_gig = convertView.findViewById(R.id.description_gig);
        TextView budget_gig = convertView.findViewById(R.id.budget_category_gig);
        TextView crt_by_gig = convertView.findViewById(R.id.created_by_gig);
        TextView dept_gig = convertView.findViewById(R.id.dept_gig);
        TextView uni_gig = convertView.findViewById(R.id.uni_gig);
        ImageView plus_gig = convertView.findViewById(R.id.plus_gig);
        CircleImageView imggig = convertView.findViewById(R.id.imggig);
        LinearLayout showUserProfile2 = convertView.findViewById(R.id.showUserProfile2);


        //get views for tutorials
        RelativeLayout card_tutorial = convertView.findViewById(R.id.card_tutors);
        TextView name_tutorial = convertView.findViewById(R.id.group_name_inside_tutor);
        TextView category_tutorial = convertView.findViewById(R.id.category_tutor);
        TextView desc_tutorial = convertView.findViewById(R.id.description_tutor);
        TextView date_tutorial = convertView.findViewById(R.id.date_tutor);
        TextView crt_by_tutorial = convertView.findViewById(R.id.created_by_tutor);
        TextView dept_tutorial = convertView.findViewById(R.id.dept_tutor);
        TextView uni_tutorial = convertView.findViewById(R.id.uni_tutor);
        ImageView task_tutorial = convertView.findViewById(R.id.task);
        ImageView plus_tutorial = convertView.findViewById(R.id.plus_tutorial);
        LinearLayout showUserProfile = convertView.findViewById(R.id.showUserProfile);
        ImageView on_off_icon = convertView.findViewById(R.id.on_or_offline);
        CircleImageView imgtut = convertView.findViewById(R.id.imgtut);
        TextView on_off_text = convertView.findViewById(R.id.on_or_offline_text);



        showUserProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(got_email.equals(created_by.get(position))){
                    //show this is your profile
                    Toast.makeText(context, "To view your profile, click on Profile tab", Toast.LENGTH_SHORT).show();
                }else{
                    //move to the user profile page
                    Intent i = new Intent(context, ProfileOthers.class);
                    //pass the email of the selected user
                    i.putExtra("email", created_by.get(position));
                    context.startActivity(i);
                }
            }
        });

        showUserProfile2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(got_email.equals(created_by.get(position))){
                    //show this is your profile
                    Toast.makeText(context, "To view your profile, click on Profile tab", Toast.LENGTH_SHORT).show();
                }else{
                    //move to the user profile page
                    Intent i = new Intent(context, ProfileOthers.class);
                    //pass the email of the selected user
                    i.putExtra("email", created_by.get(position));
                    context.startActivity(i);
                }
            }
        });

        if (type.get(position).equals("group")){
            //set tutorial cardview visible
            card_gig.setVisibility(View.GONE);
            card_tutorial.setVisibility(View.VISIBLE);
//            card_game.setVisibility(View.GONE);

            name_tutorial.setText(group_name_inside.get(position));
            category_tutorial.setText(category.get(position));
            crt_by_tutorial.setText(created_by_name.get(position));
            uni_tutorial.setText(university.get(position));
            desc_tutorial.setText(description.get(position));
            date_tutorial.setText(date.get(position));
            dept_tutorial.setText(mode.get(position));
            on_off_text.setText(card_mode.get(position));
            Glide.with(context).load(pic.get(position)).into(imgtut);

            if(card_mode.get(position).equals("online")){
                //set the location of the tutorial
                on_off_icon.setBackgroundResource(R.drawable.online);
            }else{
                //do not set the location of the tutorial
                on_off_icon.setBackgroundResource(R.drawable.offline);

            }
        }

        if (type.get(position).equals("gigs")){
            card_gig.setVisibility(View.VISIBLE);
            card_tutorial.setVisibility(View.GONE);
//            card_game.setVisibility(View.GONE);

            name_gig.setText(group_name.get(position));
            desc_gig.setText(description.get(position));
            budget_gig.setText(category.get(position));
            crt_by_gig.setText(created_by_name.get(position));
            dept_gig.setText(time.get(position));
            uni_gig.setText(university.get(position));
            Glide.with(context).load(pic.get(position)).into(imggig);
        }

        plus_tutorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ask to join tutorial
                myDialog = new Dialog(context);
                myDialog.setContentView(R.layout.card_join_tutorial);
                Button yes = myDialog.findViewById(R.id.yes);
                Button no = myDialog.findViewById(R.id.no);
                ProgressBar progressBar = myDialog.findViewById(R.id.progressBar);
                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(created_by.get(position).equals(got_email)) {
                            Toast.makeText(context, "Sorry you can not join tutorial created by yourself", Toast.LENGTH_SHORT).show();
                            myDialog.dismiss();
                        }else{
                            //join tutorial
                            StringRequest stringRequest = new StringRequest(Request.Method.POST, JOIN_TUTORIAL,
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            System.out.println("Response = "+response);

                                            progressBar.setVisibility(View.GONE);

                                            JSONObject jo = null;
                                            try {
                                                jo = new JSONObject(response);
                                                String status = jo.getString("status");

                                                if (status.equals("success")){
                                                    //load the custom dialog box
                                                    myDialog2 = new Dialog(context);
                                                    myDialog2.setContentView(R.layout.custom_popup_successful_taskmanager);
                                                    Button home = myDialog2.findViewById(R.id.home);
                                                    TextView stat = myDialog2.findViewById(R.id.status);
                                                    stat.setText("You have successfully requested to join "+group_name.get(position));
                                                    home.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            Intent i = new Intent(context, FeedsDashboard.class);
                                                            i.putExtra("email", got_email);
                                                            i.putExtra("sent from", "");
                                                            context.startActivity(i);
                                                        }
                                                    });
                                                    myDialog2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                                    myDialog2.setCanceledOnTouchOutside(false);
                                                    myDialog2.show();
                                                }else if(status.equals("request to join group already sent")){
                                                    Toast.makeText(context, "You have requested already to join group, Please wait for approval from group admin", Toast.LENGTH_LONG).show();
                                                }
                                            } catch (JSONException e) {
                                                progressBar.setVisibility(View.GONE);
                                                e.printStackTrace();
                                            }
                                        }
                                    },
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError volleyError) {
                                            progressBar.setVisibility(View.GONE);
                                            Toast.makeText(context, "Error!", Toast.LENGTH_LONG).show();
                                            volleyError.printStackTrace();
                                        }
                                    }){
                                @Override
                                protected Map<String, String> getParams(){
                                    Map<String, String> params = new HashMap<>();
                                    params.put("email", got_email);
                                    params.put("tid", id.get(position));
                                    return params;
                                }
                            };

                            RequestQueue requestQueue = Volley.newRequestQueue(context);
                            requestQueue.add(stringRequest);
                        }



                    }
                });

                no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        myDialog.dismiss();
                    }
                });
                myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                myDialog.setCanceledOnTouchOutside(true);
                myDialog.show();
            }
        });

        card_gig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //load the custom dialog box
                myDialog = new Dialog(context);
                myDialog.setContentView(R.layout.card_gig_click_popup);
                //get views in the popup page
                CircleImageView pp = myDialog.findViewById(R.id.image);
                Glide.with(context).load(pic.get(position)).into(pp);
                LinearLayout profile = myDialog.findViewById(R.id.profile);
                profile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(got_email.equals(created_by.get(position))){
                            //show this is your profile
                            Toast.makeText(context, "To view your profile, click on Profile tab", Toast.LENGTH_SHORT).show();
                        }else{
                            //move to the user profile page
                            Intent i = new Intent(context, ProfileOthers.class);
                            //pass the email of the selected user
                            i.putExtra("email", created_by.get(position));
                            context.startActivity(i);
                        }
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
                TextView pop_gigskills = myDialog.findViewById(R.id.skills);
                pop_gigskills.setText(mode.get(position));
                TextView pop_gigdescription = myDialog.findViewById(R.id.gig_description);
                pop_gigdescription.setText(description.get(position));
                TextView pop_gigcategory = myDialog.findViewById(R.id.gig_category);
                pop_gigcategory.setText(category.get(position));
                LinearLayout loader = myDialog.findViewById(R.id.linloader);
                Button bid = myDialog.findViewById(R.id.bid);

                // check API to see if Gig is approved, pending or rejected
                StringRequest stringRequest2 = new StringRequest(Request.Method.POST, GIGS_BIDDED,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                System.out.println("Response = "+response);

                                try{
                                    JSONArray jsonArray = new JSONArray(response);
                                    int ArrayLength = jsonArray.length();

                                    int count = 0;

                                    if(ArrayLength >= 1){
                                        for(int j = ArrayLength - 1; j >= 0; j--){
                                            JSONObject section1 = jsonArray.getJSONObject(j);
                                            String gigname = section1.getString("gigname");
                                            String picture = section1.getString("picture");


                                            if (gigname.equals(group_name.get(position))){
                                                status = section1.getString("status");

                                                count = count+1;

                                                System.out.println("Status - "+status);

                                                if (status.equals("approved")){
                                                    loader.setVisibility(View.GONE);
                                                    bid.setText("Bid Approved");
                                                    bid.setEnabled(false);
                                                    bid.setBackgroundResource(R.drawable.rounded_grey);
                                                }else if (status.equals("pending")){
                                                    loader.setVisibility(View.GONE);
                                                    bid.setText("Bid Pending");
                                                    bid.setEnabled(false);
                                                    bid.setBackgroundResource(R.drawable.rounded_grey);
                                                }else if (status.equals("rejected")){
                                                    loader.setVisibility(View.GONE);
                                                    bid.setText("Bid rejected");
                                                    bid.setEnabled(false);
                                                    bid.setBackgroundResource(R.drawable.rounded_grey);
                                                }else if (status.equals("")){
                                                    loader.setVisibility(View.GONE);
                                                    bid.setEnabled(true);
                                                    bid.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            myDialog.dismiss();
                                                            //move to the next gig page
                                                            Intent i = new Intent(context, CardGigClick2.class);
                                                            i.putExtra("name", created_by_name.get(position));
                                                            i.putExtra("department", time.get(position));
                                                            i.putExtra("school", university.get(position));
                                                            i.putExtra("gig_name", group_name.get(position));
                                                            i.putExtra("picture", pic.get(position));
                                                            i.putExtra("gig_description", description.get(position));
                                                            i.putExtra("payment_structure", category.get(position));
                                                            i.putExtra("id", id.get(position));
                                                            context.startActivity(i);
                                                        }
                                                    });
                                                }

                                            }

                                        }
                                    }else{
                                        loader.setVisibility(View.GONE);
                                        bid.setText("Place Bid");
                                        bid.setEnabled(true);
                                        bid.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                myDialog.dismiss();
                                                //move to the next gig page
                                                Intent i = new Intent(context, CardGigClick2.class);
                                                i.putExtra("name", created_by_name.get(position));
                                                i.putExtra("department", time.get(position));
                                                i.putExtra("school", university.get(position));
                                                i.putExtra("gig_name", group_name.get(position));
                                                i.putExtra("picture", pic.get(position));
                                                i.putExtra("gig_description", description.get(position));
                                                i.putExtra("payment_structure", category.get(position));
                                                i.putExtra("id", id.get(position));
                                                context.startActivity(i);
                                            }
                                        });
                                    }

                                    if (count<1){
                                        if (created_by.get(position).equals(got_email)){
                                            loader.setVisibility(View.GONE);
                                            bid.setEnabled(true);
                                            bid.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    myDialog.dismiss();
                                                    // you created the gig
                                                    Toast.makeText(context, "Sorry you created this Gig", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }else{
                                            loader.setVisibility(View.GONE);
                                            bid.setEnabled(true);
                                            bid.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    myDialog.dismiss();
                                                    //move to the next gig page
                                                    Intent i = new Intent(context, CardGigClick2.class);
                                                    i.putExtra("name", created_by_name.get(position));
                                                    i.putExtra("department", time.get(position));
                                                    i.putExtra("school", university.get(position));
                                                    i.putExtra("gig_name", group_name.get(position));
                                                    i.putExtra("picture", pic.get(position));
                                                    i.putExtra("gig_description", description.get(position));
                                                    i.putExtra("payment_structure", category.get(position));
                                                    i.putExtra("id", id.get(position));
                                                    context.startActivity(i);
                                                }
                                            });
                                        }
                                        
                                        
                                        
                                    }

                                }
                                catch (JSONException e) {
                                    try {
                                        JSONObject jsonObject = new JSONObject(response);
                                        String stats = jsonObject.getString("status");
                                        if(stats.equals("no gig bids")){
                                            //show "no gig bidded"
                                        }

                                    } catch (JSONException ee) {
                                        ee.printStackTrace();
                                    }
                                    e.printStackTrace();
                                }

                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                                volleyError.printStackTrace();
                            }
                        }){
                    @Override
                    protected Map<String, String> getParams(){
                        Map<String, String> params = new HashMap<>();
                        params.put("email", got_email);
                        return params;
                    }
                };

                RequestQueue requestQueue2 = Volley.newRequestQueue(context);
                requestQueue2.add(stringRequest2);
                DefaultRetryPolicy retryPolicy = new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                stringRequest2.setRetryPolicy(retryPolicy);
                requestQueue2.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
                    @Override
                    public void onRequestFinished(Request<Object> request) {
                        requestQueue2.getCache().clear();
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
                TextView totalClass = myDialog.findViewById(R.id.total_approved);
                totalClass.setText(total_approved.get(position));
                Button join = myDialog.findViewById(R.id.join);
                LinearLayout linloader = myDialog.findViewById(R.id.linloader);
                // check API to see if approved, pending or rejected
                StringRequest stringRequest2 = new StringRequest(Request.Method.POST, CHECK_STATUS,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                System.out.println("Response Joined Tutorial = "+response);

                                try{
                                    JSONArray jsonArray = new JSONArray(response);
                                     int ArrayLength = jsonArray.length();

                                     linloader.setVisibility(View.GONE);

                                    if(ArrayLength > 0){
                                        for(int j = ArrayLength - 1; j >= 0; j--){
                                            JSONObject section1 = jsonArray.getJSONObject(j);
                                            tutname = section1.getString("groupname");

                                            if (tutname.equals(group_name.get(position))){
                                                status = section1.getString("status");

                                                System.out.println("Status - "+status);

                                                if (status.equals("approved")){
                                                    join.setText("You are a member");
                                                    join.setEnabled(false);
                                                    join.setBackgroundResource(R.drawable.rounded_grey);
                                                }else if (status.equals("pending")){
                                                    join.setText("Pending request");
                                                    join.setEnabled(false);
                                                    join.setBackgroundResource(R.drawable.rounded_grey);
                                                }else if (status.equals("rejected")){
                                                    join.setText("Request rejected");
                                                    join.setEnabled(false);
                                                    join.setBackgroundResource(R.drawable.rounded_grey);
                                                }else if (status.equals("")){
                                                    join.setEnabled(true);
                                                    join.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            if(created_by.get(position).equals(got_email)){
                                                                Toast.makeText(context, "Sorry you can not join tutorial created by yourself", Toast.LENGTH_SHORT).show();
                                                                myDialog.dismiss();
                                                            }else{
                                                                myDialog2 = new Dialog(context);
                                                                myDialog2.setContentView(R.layout.card_join_tutorial);
                                                                Button yes = myDialog2.findViewById(R.id.yes);
                                                                Button no = myDialog2.findViewById(R.id.no);
                                                                ProgressBar progressBar = myDialog2.findViewById(R.id.progressBar);
                                                                yes.setOnClickListener(new View.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(View view) {
                                                                        //show loader
                                                                        myDialog4 = new Dialog(context);
                                                                        myDialog4.setContentView(R.layout.custom_popup_signing_up_loading);
                                                                        TextView text = myDialog4.findViewById(R.id.text);
                                                                        text.setText("Adding you to group, Please wait.");
                                                                        myDialog4.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                                                        myDialog4.setCanceledOnTouchOutside(false);
                                                                        myDialog4.show();

                                                                        //join tutorial
                                                                        StringRequest stringRequest = new StringRequest(Request.Method.POST, JOIN_TUTORIAL,
                                                                                new Response.Listener<String>() {
                                                                                    @Override
                                                                                    public void onResponse(String response) {
                                                                                        System.out.println("Response = "+response);

                                                                                        JSONObject jo = null;
                                                                                        try {
                                                                                            jo = new JSONObject(response);
                                                                                            String status = jo.getString("status");

                                                                                            if (status.equals("success")){
                                                                                                myDialog4.dismiss();
                                                                                                //load the custom dialog box
                                                                                                myDialog3 = new Dialog(context);
                                                                                                myDialog3.setContentView(R.layout.custom_popup_successful_taskmanager);
                                                                                                Button home = myDialog3.findViewById(R.id.home);
                                                                                                TextView stat = myDialog3.findViewById(R.id.status);
                                                                                                stat.setText("You have successfully requested to join "+group_name.get(position));

                                                                                                createNotificationChannel(group_name.get(position));

                                                                                                home.setOnClickListener(new View.OnClickListener() {
                                                                                                    @Override
                                                                                                    public void onClick(View v) {
                                                                                                        Intent i = new Intent(context, FeedsDashboard.class);
                                                                                                        i.putExtra("email", got_email);
                                                                                                        i.putExtra("sent from", "");
                                                                                                        context.startActivity(i);
                                                                                                    }
                                                                                                });
                                                                                                myDialog3.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                                                                                myDialog3.setCanceledOnTouchOutside(false);
                                                                                                myDialog3.show();
                                                                                            }else if(status.equals("request to join group already sent")){
                                                                                                myDialog4.dismiss();
                                                                                                Toast.makeText(context, "You have requested already to join group, Please wait for approval from group admin", Toast.LENGTH_LONG).show();
                                                                                            }
                                                                                        } catch (JSONException e) {
                                                                                            myDialog4.dismiss();
//                                                        progressBar.setVisibility(View.GONE);
                                                                                            e.printStackTrace();
                                                                                        }
                                                                                    }
                                                                                },
                                                                                new Response.ErrorListener() {
                                                                                    @Override
                                                                                    public void onErrorResponse(VolleyError volleyError) {
                                                                                        myDialog4.dismiss();
//                                                    progressBar.setVisibility(View.GONE);
                                                                                        Toast.makeText(context, "Error!", Toast.LENGTH_LONG).show();
                                                                                        volleyError.printStackTrace();
                                                                                    }
                                                                                }){
                                                                            @Override
                                                                            protected Map<String, String> getParams(){
                                                                                Map<String, String> params = new HashMap<>();
                                                                                params.put("email", got_email);
                                                                                params.put("tid", id.get(position));
                                                                                return params;
                                                                            }
                                                                        };

                                                                        RequestQueue requestQueue = Volley.newRequestQueue(context);
                                                                        DefaultRetryPolicy retryPolicy = new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                                                                        stringRequest.setRetryPolicy(retryPolicy);
                                                                        requestQueue.add(stringRequest);
                                                                        requestQueue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
                                                                            @Override
                                                                            public void onRequestFinished(Request<Object> request) {
                                                                                requestQueue.getCache().clear();
                                                                            }
                                                                        });
                                                                    }
                                                                });

                                                                no.setOnClickListener(new View.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(View view) {
                                                                        myDialog2.dismiss();
                                                                    }
                                                                });
                                                                myDialog2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                                                myDialog2.setCanceledOnTouchOutside(true);
                                                                myDialog2.show();

                                                            }

                                                        }
                                                    });
                                                }

                                            }

                                        }

                                    }
                                }
                                catch (JSONException e) {
                                    linloader.setVisibility(View.GONE);
                                }

                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                                volleyError.printStackTrace();
                            }
                        }){
                    @Override
                    protected Map<String, String> getParams(){
                        Map<String, String> params = new HashMap<>();
                        params.put("email", got_email);
                        return params;
                    }
                };

                RequestQueue requestQueue2 = Volley.newRequestQueue(context);
                DefaultRetryPolicy retryPolicy = new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                stringRequest2.setRetryPolicy(retryPolicy);
                requestQueue2.add(stringRequest2);
                requestQueue2.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
                    @Override
                    public void onRequestFinished(Request<Object> request) {
                        requestQueue2.getCache().clear();
                    }
                });

                join.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(created_by.get(position).equals(got_email)){
                            Toast.makeText(context, "Sorry you can not join tutorial created by yourself", Toast.LENGTH_SHORT).show();
                            myDialog.dismiss();
                        }else{
                            myDialog2 = new Dialog(context);
                            myDialog2.setContentView(R.layout.card_join_tutorial);
                            Button yes = myDialog2.findViewById(R.id.yes);
                            Button no = myDialog2.findViewById(R.id.no);
                            ProgressBar progressBar = myDialog2.findViewById(R.id.progressBar);
                            yes.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    //show loader
                                    myDialog4 = new Dialog(context);
                                    myDialog4.setContentView(R.layout.custom_popup_signing_up_loading);
                                    TextView text = myDialog4.findViewById(R.id.text);
                                    text.setText("Adding you to group, Please wait.");
                                    myDialog4.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                    myDialog4.setCanceledOnTouchOutside(false);
                                    myDialog4.show();

                                    //join tutorial
                                    StringRequest stringRequest = new StringRequest(Request.Method.POST, JOIN_TUTORIAL,
                                            new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {
                                                    System.out.println("Response = "+response);

                                                    JSONObject jo = null;
                                                    try {
                                                        jo = new JSONObject(response);
                                                        String status = jo.getString("status");

                                                        if (status.equals("success")){
                                                            myDialog4.dismiss();
                                                            //load the custom dialog box
                                                            myDialog3 = new Dialog(context);
                                                            myDialog3.setContentView(R.layout.custom_popup_successful_taskmanager);
                                                            Button home = myDialog3.findViewById(R.id.home);
                                                            TextView stat = myDialog3.findViewById(R.id.status);
                                                            stat.setText("You have successfully requested to join "+group_name.get(position));
                                                            home.setOnClickListener(new View.OnClickListener() {
                                                                @Override
                                                                public void onClick(View v) {
                                                                    Intent i = new Intent(context, FeedsDashboard.class);
                                                                    i.putExtra("email", got_email);
                                                                    i.putExtra("sent from", "");
                                                                    context.startActivity(i);
                                                                }
                                                            });
                                                            myDialog3.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                                            myDialog3.setCanceledOnTouchOutside(false);
                                                            myDialog3.show();
                                                        }else if(status.equals("request to join group already sent")){
                                                            myDialog4.dismiss();
                                                            Toast.makeText(context, "You have requested already to join group, Please wait for approval from group admin", Toast.LENGTH_LONG).show();
                                                        }
                                                    } catch (JSONException e) {
                                                        myDialog4.dismiss();
//                                                        progressBar.setVisibility(View.GONE);
                                                        e.printStackTrace();
                                                    }
                                                }
                                            },
                                            new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError volleyError) {
                                                    myDialog4.dismiss();
//                                                    progressBar.setVisibility(View.GONE);
                                                    Toast.makeText(context, "Error!", Toast.LENGTH_LONG).show();
                                                    volleyError.printStackTrace();
                                                }
                                            }){
                                        @Override
                                        protected Map<String, String> getParams(){
                                            Map<String, String> params = new HashMap<>();
                                            params.put("email", got_email);
                                            params.put("tid", id.get(position));
                                            return params;
                                        }
                                    };

                                    RequestQueue requestQueue = Volley.newRequestQueue(context);
                                    DefaultRetryPolicy retryPolicy = new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                                    stringRequest.setRetryPolicy(retryPolicy);
                                    requestQueue.add(stringRequest);
                                    requestQueue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
                                        @Override
                                        public void onRequestFinished(Request<Object> request) {
                                            requestQueue.getCache().clear();
                                        }
                                    });
                                }
                            });

                            no.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    myDialog2.dismiss();
                                }
                            });
                            myDialog2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            myDialog2.setCanceledOnTouchOutside(true);
                            myDialog2.show();

                        }

                    }
                });



                CircleImageView pp = myDialog.findViewById(R.id.image);
                Glide.with(context).load(pic.get(position)).into(pp);
                LinearLayout profile = myDialog.findViewById(R.id.profile);
                profile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(got_email.equals(created_by.get(position))){
                            //show this is your profile
                            Toast.makeText(context, "To view your profile, click on Profile tab", Toast.LENGTH_SHORT).show();
                        }else{
                            //move to the user profile page
                            Intent i = new Intent(context, ProfileOthers.class);
                            //pass the email of the selected user
                            i.putExtra("email", created_by.get(position));
                            context.startActivity(i);
                        }
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
                TextView pop_gigtime = myDialog.findViewById(R.id.tut_time);
                pop_gigtime.setText(time.get(position));
                TextView pop_cardmode = myDialog.findViewById(R.id.mode);
                pop_cardmode.setText(card_mode.get(position));
                if(card_mode.get(position).equals("Online")){
                    //set the location of the tutorial
                }else{
                    //do not set the location of the tutorial
                }


                builder = new AlertDialog.Builder(context);



                myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                myDialog.setCanceledOnTouchOutside(true);
                myDialog.show();

            }
        });

        task_tutorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //show confirmation dialogue first
                myDialog = new Dialog(context);
                myDialog.setContentView(R.layout.card_add_task);
                Button yes = myDialog.findViewById(R.id.yes);
                Button no = myDialog.findViewById(R.id.no);
                LinearLayout progressBar = myDialog.findViewById(R.id.progressBar);
                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        progressBar.setVisibility(View.VISIBLE);
                        //add the tutorial to the taskmanager
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, TASK_MANAGER,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        System.out.println("Response = "+response);

                                        JSONObject jo = null;
                                        try {
                                            jo = new JSONObject(response);
                                            String status = jo.getString("status");

                                            if (status.equals("successful")){
                                                myDialog.dismiss();
                                                progressBar.setVisibility(View.GONE);
                                                //load the custom dialog box
                                                myDialog2 = new Dialog(context);
                                                myDialog2.setContentView(R.layout.custom_popup_successful_taskmanager);
                                                Button home = myDialog2.findViewById(R.id.home);
                                                TextView stat = myDialog2.findViewById(R.id.status);
                                                stat.setText("Successfully added task");
                                                home.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        Intent i = new Intent(context, FeedsDashboard.class);
                                                        i.putExtra("email", got_email);
                                                        i.putExtra("sent from", "");
                                                        context.startActivity(i);
                                                    }
                                                });
                                                myDialog2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                                myDialog2.setCanceledOnTouchOutside(false);
                                                myDialog2.show();
                                            }else if (status.equals("task already exists")){
                                                progressBar.setVisibility(View.GONE);
                                                Toast.makeText(context, "Error adding to task", Toast.LENGTH_SHORT).show();
                                                myDialog.dismiss();
                                            }
                                        } catch (JSONException e) {
                                            progressBar.setVisibility(View.GONE);
                                            e.printStackTrace();
                                        }
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError volleyError) {
                                        progressBar.setVisibility(View.GONE);
                                        Toast.makeText(context, "Error!", Toast.LENGTH_LONG).show();
                                        volleyError.printStackTrace();
                                    }
                                }){
                            @Override
                            protected Map<String, String> getParams(){
                                Map<String, String> params = new HashMap<>();
                                params.put("email", got_email);
                                params.put("task_name", group_name.get(position));
                                params.put("short_description", description.get(position));
                                params.put("task_category", "Tutorial");
                                params.put("task_date", date.get(position));
                                params.put("task_time", time.get(position));
                                return params;
                            }
                        };

                        RequestQueue requestQueue = Volley.newRequestQueue(context);
                        DefaultRetryPolicy retryPolicy = new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                        stringRequest.setRetryPolicy(retryPolicy);
                        requestQueue.add(stringRequest);
                    }
                });

                no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        myDialog.dismiss();
                    }
                });
                myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                myDialog.setCanceledOnTouchOutside(true);
                myDialog.show();


            }
        });


        return convertView;
    }

    private void createNotificationChannel(String name) {
        NotificationManager manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = manager.getNotificationChannel(CHANNEL_ID);
            if(channel == null){
                channel = new NotificationChannel(CHANNEL_ID, "Join Group Request", NotificationManager.IMPORTANCE_DEFAULT);
                //config notification channel
                channel.setDescription("[Channel Description]");
                channel.enableVibration(true);
                channel.enableLights(true);
                channel.setVibrationPattern(new long[]{100, 1000, 200, 340});
                channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
                manager.createNotificationChannel(channel);
            }
        }
        Intent notificationIntent = new Intent(context, FeedsDashboard.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent penIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.logo)
                .setStyle(new NotificationCompat.BigTextStyle())
                .setContentTitle("Join Group Request")
                .setContentText("Your request to join group \""+name+"\" is successful")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(false)
                .setTicker("Notification");
        builder.setContentIntent(penIntent);
        NotificationManagerCompat m = NotificationManagerCompat.from(context);
        m.notify(4, builder.build());
    }
}
