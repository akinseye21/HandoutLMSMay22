package com.example.handoutlms;

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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class GroupListViewAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<String> groupName;
    private ArrayList<String> groupTime;
    private ArrayList<String> groupTutor;
    private ArrayList<String> groupID;
    private ArrayList<String> groupCategory;
    private ArrayList<String> groupDate;
    private ArrayList<String> groupUniversity;
    private ArrayList<String> groupDescription;
    private ArrayList<String> groupTutorEmail;
    private ArrayList<String> groupSize;
    private ArrayList<String> groupMode;
    private ArrayList<String> pp;

    public static final String CHECK_STATUS = "https://handoutng.com/handouts/handout_user_joined_groups";
    public static final String JOIN_TUTORIAL = "https://handoutng.com/handouts/handout_group_join";
    String got_email;
    SharedPreferences preferences;

    AlertDialog.Builder builder;
    Dialog myDialog, myDialog2, myDialog3, myDialog4;
    String CHANNEL_ID = "channelID3";

    public GroupListViewAdapter(Context context, ArrayList<String> groupName, ArrayList<String> groupTime, ArrayList<String> groupTutor,
                                ArrayList<String> groupID, ArrayList<String> groupCategory, ArrayList<String> groupDate, ArrayList<String> groupUniversity,
                                ArrayList<String> groupDescription, ArrayList<String> groupTutorEmail, ArrayList<String> groupSize, ArrayList<String> groupMode,
                                ArrayList<String> pp){
        //Getting all the values
        this.context = context;
        this.groupName = groupName;
        this.groupTime = groupTime;
        this.groupTutor = groupTutor;
        this.groupID = groupID;
        this.groupCategory = groupCategory;
        this.groupDate = groupDate;
        this.groupUniversity = groupUniversity;
        this.groupDescription = groupDescription;
        this.groupTutorEmail = groupTutorEmail;
        this.groupSize = groupSize;
        this.groupMode = groupMode;
        this.pp = pp;
    }
    @Override
    public int getCount() {
        return groupName.size();
    }

    @Override
    public Object getItem(int position) {
        return groupName.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflaInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = inflaInflater.inflate(R.layout.list_tutors_group, parent, false);
        }

        preferences = context.getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        got_email = preferences.getString("email", "not available");

        LinearLayout card = convertView.findViewById(R.id.card);
        TextView name = convertView.findViewById(R.id.groupName);
        TextView time = convertView.findViewById(R.id.groupTime);
        TextView tutor = convertView.findViewById(R.id.groupTutor);


        name.setText(groupName.get(position));
        time.setText(groupTime.get(position));
        tutor.setText("Tutor: "+groupTutor.get(position));
        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //load the custom dialog box
                myDialog = new Dialog(context);
                myDialog.setContentView(R.layout.card_tutorial_click_popup);
                //get views in the popup page
                TextView totalClass = myDialog.findViewById(R.id.total_approved);
                totalClass.setText(groupSize.get(position));
                Button join = myDialog.findViewById(R.id.join);
                LinearLayout linloader = myDialog.findViewById(R.id.linloader);
                CircleImageView prp = myDialog.findViewById(R.id.image);
                Glide.with(context).load(pp.get(position)).into(prp);
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
                                            String tutname = section1.getString("groupname");

                                            if (tutname.equals(groupName.get(position))){
                                                String status = section1.getString("status");

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
                                                            if(groupTutorEmail.get(position).equals(got_email)){
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
                                                                                                stat.setText("You have successfully requested to join "+groupName.get(position));

                                                                                                createNotificationChannel(groupName.get(position));

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
                                                                                params.put("tid", groupID.get(position));
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
                        if(groupTutorEmail.get(position).equals(got_email)){
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
                                                            stat.setText("You have successfully requested to join "+groupName.get(position));
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
                                            params.put("tid", groupID.get(position));
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
                                    myDialog2.dismiss();
                                }
                            });
                            myDialog2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            myDialog2.setCanceledOnTouchOutside(true);
                            myDialog2.show();

                        }

                    }
                });
                LinearLayout profile = myDialog.findViewById(R.id.profile);
                profile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(got_email.equals(groupTutorEmail.get(position))){
                            //show this is your profile
                            Toast.makeText(context, "To view your profile, click on Profile tab", Toast.LENGTH_SHORT).show();
                        }else{
                            //move to the user profile page
                            Intent i = new Intent(context, ProfileOthers.class);
                            //pass the email of the selected user
                            i.putExtra("email", groupTutorEmail.get(position));
                            context.startActivity(i);
                        }
                    }
                });
                TextView pop_name = myDialog.findViewById(R.id.created_by);
                pop_name.setText(groupTutor.get(position));
                TextView pop_department = myDialog.findViewById(R.id.dept);
                pop_department.setText(groupMode.get(position));
                TextView pop_university = myDialog.findViewById(R.id.uni_gig);
                pop_university.setText(groupUniversity.get(position));
                TextView pop_gigname = myDialog.findViewById(R.id.gig_name);
                pop_gigname.setText(groupName.get(position));
                TextView pop_gigcategory = myDialog.findViewById(R.id.gig_category);
                pop_gigcategory.setText(groupCategory.get(position));
                TextView pop_gigdescription = myDialog.findViewById(R.id.gig_desc);
                pop_gigdescription.setText(groupDescription.get(position));
                TextView pop_gigdate = myDialog.findViewById(R.id.tut_date);
                pop_gigdate.setText(groupDate.get(position));
                TextView pop_gigtime = myDialog.findViewById(R.id.tut_time);
                pop_gigtime.setText(groupTime.get(position));
                TextView pop_cardmode = myDialog.findViewById(R.id.mode);
                pop_cardmode.setText(groupMode.get(position));



                builder = new AlertDialog.Builder(context);



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
