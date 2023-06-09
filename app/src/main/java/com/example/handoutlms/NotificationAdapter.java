package com.example.handoutlms;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.commons.codec.binary.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class NotificationAdapter extends BaseAdapter {

    Context context;
    ArrayList<String> notification_title;
    ArrayList<String> notification_message;
    ArrayList<String> notification_status;
    ArrayList<String> notification_logo;
    ArrayList<String> notification_profImg;
    ArrayList<String> notification_type;
    ArrayList<String> notification_id;
    ArrayList<String> notification_time;
    ArrayList<String> notification_date;
    SharedPreferences preferences;
    String fullname;
    Dialog myDialog;
    RelativeLayout notification_card;

    public static final String UPDATE_NOTIFICATION = "https://handoutng.com/handouts/handout_update_notification_status";

    public NotificationAdapter(Context applicationContext,
                               ArrayList<String> notification_title,
                               ArrayList<String> notification_message,
                               ArrayList<String> notification_status,
                               ArrayList<String> notification_logo,
                               ArrayList<String> notification_profImg,
                               ArrayList<String> notification_type,
                               ArrayList<String> notification_id,
                               ArrayList<String> notification_time,
                               ArrayList<String> notification_date){
        this.context = applicationContext;
        this.notification_title = notification_title;
        this.notification_message = notification_message;
        this.notification_logo = notification_logo;
        this.notification_profImg = notification_profImg;
        this.notification_type = notification_type;
        this.notification_id = notification_id;
        this.notification_time = notification_time;
        this.notification_date = notification_date;
        this.notification_status = notification_status;
//        inflter = (LayoutInflater.from(applicationContext));
    }



    @Override
    public int getCount() {
        return notification_title.size();
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
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        LayoutInflater inflaInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = inflaInflater.inflate(R.layout.list_notification, viewGroup, false);
        }
//        convertView = inflter.inflate(R.layout.list_notification, null);
        preferences = context.getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        fullname = preferences.getString("fullname", "not available");

        notification_card = convertView.findViewById(R.id.notification_card);
        CircleImageView img = convertView.findViewById(R.id.img);
        ImageView ico = convertView.findViewById(R.id.ico);
        TextView time = convertView.findViewById(R.id.time);
        TextView date = convertView.findViewById(R.id.date);
        TextView title = convertView.findViewById(R.id.notification_title);
        TextView message = convertView.findViewById(R.id.notification_message);
        TextView name = convertView.findViewById(R.id.fullname);

        Glide.with(context).load(notification_profImg.get(i)).into(img);
        Glide.with(context).load(notification_logo.get(i)).into(ico);
        time.setText(notification_time.get(i));
        date.setText(notification_date.get(i));
        name.setText(fullname+"-");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            message.setText(Html.fromHtml(notification_message.get(i), Html.FROM_HTML_MODE_LEGACY));
        } else {
            message.setText(Html.fromHtml(notification_message.get(i)));
        }



        if (notification_type.get(i).equals("group")){
            title.setText("Tutorial");
            title.setTextColor(Color.parseColor("#ef5c6f"));
        }
        if (notification_type.get(i).equals("gig")){
            title.setText("Gigs");
            title.setTextColor(Color.parseColor("#00adef"));
        }
        if (notification_type.get(i).equals("system")){
            title.setText("System");
            title.setTextColor(Color.parseColor("#f2de39"));
        }

        notification_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //set to unread
                //send to DB if status is unread
                if (notification_status.get(i).equals("unread")){
                    changeToRead(i);
                    //show dialog
                    myDialog = new Dialog(context);
                    myDialog.setContentView(R.layout.custom_popup_notification);

                    // Setting dialogview
                    Window window = myDialog.getWindow();
                    window.setGravity(Gravity.BOTTOM);
                    window.setLayout(WindowManager.LayoutParams.FILL_PARENT, WindowManager.LayoutParams.FILL_PARENT);

                    CircleImageView userProf = myDialog.findViewById(R.id.img);
                    TextView not_title = myDialog.findViewById(R.id.notification_title);
                    ImageView ico = myDialog.findViewById(R.id.ico);
                    TextView not_message = myDialog.findViewById(R.id.notification_message);
                    Button close = myDialog.findViewById(R.id.close);

                    Glide.with(context).load(notification_profImg.get(i)).into(userProf);
                    if (notification_type.get(i).equals("group")){
                        not_title.setText("Tutorial");
                        not_title.setTextColor(Color.parseColor("#ef5c6f"));
                    }
                    if (notification_type.get(i).equals("gig")){
                        not_title.setText("Gigs");
                        not_title.setTextColor(Color.parseColor("#00adef"));
                    }
                    if (notification_type.get(i).equals("system")){
                        not_title.setText("System");
                        not_title.setTextColor(Color.parseColor("#f2de39"));
                    }
                    Glide.with(context).load(notification_logo.get(i)).into(ico);
                    not_message.setText(notification_message.get(i));
                    close.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            myDialog.dismiss();
                        }
                    });

                    myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    myDialog.setCanceledOnTouchOutside(true);
                    myDialog.setCancelable(true);
                    myDialog.show();
                }
                else{
                    myDialog = new Dialog(context);
                    myDialog.setContentView(R.layout.custom_popup_notification);

                    // Setting dialogview
                    Window window = myDialog.getWindow();
                    window.setGravity(Gravity.BOTTOM);
                    window.setLayout(WindowManager.LayoutParams.FILL_PARENT, WindowManager.LayoutParams.FILL_PARENT);

                    CircleImageView userProf = myDialog.findViewById(R.id.img);
                    TextView not_title = myDialog.findViewById(R.id.notification_title);
                    ImageView ico = myDialog.findViewById(R.id.ico);
                    TextView not_message = myDialog.findViewById(R.id.notification_message);
                    Button close = myDialog.findViewById(R.id.close);

                    Glide.with(context).load(notification_profImg.get(i)).into(userProf);
                    if (notification_type.get(i).equals("group")){
                        not_title.setText("Tutorial");
                        not_title.setTextColor(Color.parseColor("#ef5c6f"));
                    }
                    if (notification_type.get(i).equals("gig")){
                        not_title.setText("Gigs");
                        not_title.setTextColor(Color.parseColor("#00adef"));
                    }
                    if (notification_type.get(i).equals("system")){
                        not_title.setText("System");
                        not_title.setTextColor(Color.parseColor("#f2de39"));
                    }
                    Glide.with(context).load(notification_logo.get(i)).into(ico);
                    not_message.setText(notification_message.get(i));
                    close.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            myDialog.dismiss();
                        }
                    });

                    myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    myDialog.setCanceledOnTouchOutside(true);
                    myDialog.setCancelable(true);
                    myDialog.show();
                }



            }
        });

        return convertView;
    }

    private void changeToRead(int i) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPDATE_NOTIFICATION,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");

                            if (status.equals("success")){
                                //status has been changed
                                notification_card.setVisibility(View.GONE);
                            }else if (status.equals("failed")){
                                //status not changed
                            }

                        }
                        catch (JSONException e){
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
                params.put("id", notification_id.get(i));
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

}
