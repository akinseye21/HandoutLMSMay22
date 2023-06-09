package com.example.handoutlms;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

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
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class TutorialProfileAdapter extends BaseAdapter {

    private Context context;
    ArrayList<String> arr_tutName;
    ArrayList<String> arr_tutCategory;
    ArrayList<String> arr_tutDescription;
    ArrayList<String> arr_tutMode;
    ArrayList<String> arr_tutId;
    ArrayList<String> arr_tutDate;
    ArrayList<String> arr_tutType;
    ArrayList<String> arr_classSize;
    String from;
    Dialog myDialog, myDialog2, myDialog3, myDialog4;
    SharedPreferences preferences;
    String got_email;
    public static final String JOIN_TUTORIAL = "https://handoutng.com/handouts/handout_group_join";

    public static final String CHECK_STATUS = "https://handoutng.com/handouts/handout_user_joined_groups";
    public static final String ALL_GIGS_AND_TUTORIALS = "http://handoutng.com/handouts/handout_gigs_groups";

    public TutorialProfileAdapter(Context context, ArrayList<String> tutName, ArrayList<String> tutCategory, ArrayList<String> tutDescription, ArrayList<String> tutMode, ArrayList<String> tutId, ArrayList<String> tutDate, ArrayList<String> tutType, ArrayList<String> classSize, String from){
        //Getting all the values
        this.context = context;
        this.arr_tutName = tutName;
        this.arr_tutCategory = tutCategory;
        this.arr_tutDescription = tutDescription;
        this.arr_tutMode = tutMode;
        this.arr_tutId = tutId;
        this.arr_tutDate = tutDate;
        this.arr_tutType = tutType;
        this.arr_classSize = classSize;
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

        preferences = context.getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        got_email = preferences.getString("email", "not available");

        ImageView img = convertView.findViewById(R.id.img);
        LinearLayout linLay = convertView.findViewById(R.id.lin_lay);
        TextView tutorial = convertView.findViewById(R.id.tutorial);
        TextView tutname = convertView.findViewById(R.id.tut_name);
        TextView tutcategory = convertView.findViewById(R.id.tut_category);
        TextView tutdescription = convertView.findViewById(R.id.tut_description);
        TextView tutmode = convertView.findViewById(R.id.tut_mode);

        tutname.setText(arr_tutName.get(i));
        tutcategory.setText(arr_tutCategory.get(i));
        tutdescription.setText(arr_tutDescription.get(i));
        tutmode.setText(arr_tutMode.get(i));

        if (arr_tutMode.get(i).equals("pending")){
//            linLay.setBackgroundResource(R.drawable.rounded_grey2);
            img.setImageResource(R.drawable.grey_scale);
            tutname.setTextColor(Color.parseColor("#CECCC9"));
            tutcategory.setTextColor(Color.parseColor("#CECCC9"));
            tutdescription.setTextColor(Color.parseColor("#CECCC9"));
            tutmode.setTextColor(Color.parseColor("#CECCC9"));
            tutorial.setText("");
        }else if (arr_tutMode.get(i).equals("rejected")){
//            linLay.setBackgroundResource(R.drawable.rounded_red2);
            img.setImageResource(R.drawable.rejected);
            tutname.setTextColor(Color.parseColor("#FFAAB3"));
            tutcategory.setTextColor(Color.parseColor("#FFAAB3"));
            tutdescription.setTextColor(Color.parseColor("#FFAAB3"));
            tutmode.setTextColor(Color.parseColor("#FFAAB3"));
            tutorial.setText("");
        }else if (arr_tutMode.get(i).equals("approved")){
            img.setImageResource(R.drawable.ic44);
            tutname.setTextColor(Color.parseColor("#FFFFFF"));
            tutcategory.setTextColor(Color.parseColor("#FFFFFF"));
            tutdescription.setTextColor(Color.parseColor("#FFFFFF"));
            tutmode.setTextColor(Color.parseColor("#FFFFFF"));
            tutorial.setText("Tutorial");
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
                        text.setText("Sorry, your request is still pending.");
                        text.setTextSize(10);
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
                        text.setText("Sorry, your request to join group is rejected.");
                        text.setTextSize(10);
                        img.setImageResource(R.drawable.notification);
                        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        myDialog.setCanceledOnTouchOutside(true);
                        myDialog.show();
                    }

                }

                else if(from.equals("created")){
                    Intent intent2 = new Intent(context, ClickTutOnProfile.class);
                    intent2.putExtra("groupName", arr_tutName.get(i));
                    intent2.putExtra("name", arr_tutName.get(i));
                    intent2.putExtra("category", arr_tutCategory.get(i));
                    intent2.putExtra("description", arr_tutDescription.get(i));
                    intent2.putExtra("mode", arr_tutMode.get(i));
                    intent2.putExtra("id", arr_tutId.get(i));
                    intent2.putExtra("date", arr_tutDate.get(i));
                    intent2.putExtra("type", arr_tutType.get(i));
                    intent2.putExtra("classsize", arr_classSize.get(i));
                    intent2.putExtra("from", "created");
                    context.startActivity(intent2);
                }

                else if (from.equals("tut_others")){
                    //show popup like the dashboard own
                    likeHomeView(i, arr_tutName.get(i), arr_tutId.get(i));
                }
            }
        });

        return convertView;
    }

    public void likeHomeView(int i, String passed_tutname, String passed_tutId){
        //show loader
        //load the custom dialog box
        myDialog2 = new Dialog(context);
        myDialog2.setContentView(R.layout.custom_popup_login_loading);
        //get views in the popup page
        TextView text = myDialog2.findViewById(R.id.text);
        text.setText("Loading, please wait...");
        myDialog2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog2.setCanceledOnTouchOutside(true);
        myDialog2.show();
        // check if the user has been approved on the tutorial or not
        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, CHECK_STATUS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        myDialog2.dismiss();
                        System.out.println("My def stats = "+response);

                        try{
                            JSONArray jsonArray = new JSONArray(response);
                            int ArrayLength = jsonArray.length();
                            ArrayList<String> myGName = new ArrayList<>();


                            for(int j = 0; j < ArrayLength; j++){
                                JSONObject section1 = jsonArray.getJSONObject(j);
                                String tutname = section1.getString("groupname");
                                myGName.add(tutname);

                                if(tutname.equals(passed_tutname)){
                                    String status = section1.getString("status");
                                    System.out.println("My def stats2 = "+status);

                                    if (status.equals("approved")){
                                        // if approved, go straight to the resources page of the group
                                        Intent intent = new Intent(context, ResourceViewerView2.class);
                                        intent.putExtra("name", arr_tutName.get(i));
                                        intent.putExtra("category", arr_tutCategory.get(i));
                                        intent.putExtra("description", arr_tutDescription.get(i));
                                        intent.putExtra("mode", arr_tutMode.get(i));
                                        intent.putExtra("date", arr_tutDate.get(i));
                                        intent.putExtra("id", arr_tutId.get(i));
                                        context.startActivity(intent);
                                    }else if (status.equals("pending")){
                                        // if not approved, i.e pending, rejected or not sent request at all, should show the pop up just like dashboard
                                        showLoader("Request pending", passed_tutname, passed_tutId);
                                    }else if (status.equals("rejected")){
                                        // if not approved, i.e pending, rejected or not sent request at all, should show the pop up just like dashboard
                                        showLoader("Request rejected", passed_tutname, passed_tutId);
                                    }else{
                                        // if not approved, i.e pending, rejected or not sent request at all, should show the pop up just like dashboard
                                        showLoader("Join Group", passed_tutname, passed_tutId);
                                    }
                                }
                            }

                            if (!myGName.contains(passed_tutname)){
                                showLoader("Join Group", passed_tutname, passed_tutId);
                            }





                        }catch (JSONException e){

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        myDialog2.dismiss();
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
    }

    public void showLoader(String s, String tutName, String tutId){

        //load the custom dialog box
        myDialog = new Dialog(context);
        myDialog.setContentView(R.layout.card_tutorial_click_popup);
        //get views in the popup page
        TextView totalClass = myDialog.findViewById(R.id.total_approved);
        totalClass.setVisibility(View.GONE);
        TextView studentNo = myDialog.findViewById(R.id.studentNo);
        studentNo.setVisibility(View.GONE);

        TextView pop_tutdate = myDialog.findViewById(R.id.tut_date);
        pop_tutdate.setVisibility(View.GONE);
        TextView pop_tuttime = myDialog.findViewById(R.id.tut_time);
        pop_tuttime.setVisibility(View.GONE);
        TextView pop_cardmode = myDialog.findViewById(R.id.mode);
        pop_cardmode.setVisibility(View.GONE);
        TextView txtdate = myDialog.findViewById(R.id.txtdate);
        txtdate.setVisibility(View.GONE);
        ImageView imgdate = myDialog.findViewById(R.id.imgdate);
        imgdate.setVisibility(View.GONE);
        TextView txttime = myDialog.findViewById(R.id.txttime);
        txttime.setVisibility(View.GONE);
        ImageView imgtime = myDialog.findViewById(R.id.imgtime);
        imgtime.setVisibility(View.GONE);
        TextView txtmode = myDialog.findViewById(R.id.txtmode);
        txtmode.setVisibility(View.GONE);
        ImageView imgmode = myDialog.findViewById(R.id.imgmode);
        imgmode.setVisibility(View.GONE);

        LinearLayout linloader = myDialog.findViewById(R.id.linloader);
        CircleImageView pp = myDialog.findViewById(R.id.image);
        TextView pop_name = myDialog.findViewById(R.id.created_by);
        TextView pop_department = myDialog.findViewById(R.id.dept);
        TextView pop_university = myDialog.findViewById(R.id.uni_gig);
        TextView pop_tutname = myDialog.findViewById(R.id.gig_name);
        TextView pop_tutcategory = myDialog.findViewById(R.id.gig_category);
        TextView pop_tutdescription = myDialog.findViewById(R.id.gig_desc);

        Button join = myDialog.findViewById(R.id.join);
        join.setText(s);
        if (s.equals("Request pending")){
            join.setEnabled(false);
            join.setBackgroundResource(R.drawable.rounded_grey);
        }
        else if (s.equals("Request rejected")){
            join.setEnabled(false);
            join.setBackgroundResource(R.drawable.rounded_grey);
        }
        else {
            join.setEnabled(true);
        }
        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Request to join tutorial
                joinTutorial(tutName, tutId, myDialog);
            }
        });

        StringRequest stringRequest = new StringRequest(Request.Method.GET, ALL_GIGS_AND_TUTORIALS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            int ArrayLength = jsonArray.length();

                            if(ArrayLength<1){
                                // no tutorials available
                            }else{
                                for (int i = 0; i < ArrayLength; i++) {
                                    JSONObject section = jsonArray.getJSONObject(i);
                                    String type = section.getString("type");

                                    if (type.equals("group")){


                                        String groupName = section.getString("gig_group_name");

                                        if (groupName.equals(tutName)){

                                            linloader.setVisibility(View.GONE);
                                            String pic = section.getString("creator_pic");
                                            Glide.with(context).load(pic).into(pp);
                                            String createdBy = section.getString("created_by");
                                            pop_name.setText(createdBy);
                                            String faculty = section.getString("faculty");
                                            pop_department.setText(faculty);
                                            String university = section.getString("school");
                                            pop_university.setText(university);
                                            String gpName = section.getString("gig_group_name");
                                            pop_tutname.setText(gpName);
                                            String category = section.getString("category");
                                            pop_tutcategory.setText(category);
                                            String description = section.getString("description");
                                            pop_tutdescription.setText(description);
                                        }

                                    }

                                }
                            }

                        } catch (JSONException e) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String status = jsonObject.getString("status");
                                if(status.equals("no group")){
                                    //no groups
                                }
                            } catch (JSONException ex) {
                                ex.printStackTrace();
                            }
                            e.printStackTrace();
                        }
                    }


                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        DefaultRetryPolicy retryPolicy = new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);
        requestQueue.add(stringRequest);

        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.setCanceledOnTouchOutside(true);
        myDialog.show();

    }

    private void joinTutorial(String tutName, String tutId, Dialog myDialog) {

        //show loader first
        myDialog2 = new Dialog(context);
        myDialog2.setContentView(R.layout.custom_popup_login_loading);
        //get views in the popup page
        TextView text = myDialog2.findViewById(R.id.text);
        text.setText("Requesting to join group...");
        myDialog2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog2.setCanceledOnTouchOutside(false);
        myDialog2.show();
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
                                myDialog2.dismiss();
                                //load the custom dialog box
                                myDialog4 = new Dialog(context);
                                myDialog4.setContentView(R.layout.custom_popup_successful_taskmanager);
                                Button home = myDialog4.findViewById(R.id.home);
                                TextView stat = myDialog4.findViewById(R.id.status);
                                stat.setText("You have successfully requested to join "+tutName);

//                                createNotificationChannel(tutName);

                                home.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
//                                        Intent i = new Intent(context, FeedsDashboard.class);
//                                        i.putExtra("email", got_email);
//                                        i.putExtra("sent from", "");
//                                        context.startActivity(i);
                                        myDialog4.dismiss();
                                        myDialog.dismiss();
                                    }
                                });
                                myDialog4.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                myDialog4.setCanceledOnTouchOutside(false);
                                myDialog4.show();
                            }else if(status.equals("request to join group already sent")){
                                myDialog.dismiss();
                                myDialog2.dismiss();
                                Toast.makeText(context, "You have requested already to join group, Please wait for approval from group admin", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            myDialog.dismiss();
                            myDialog2.dismiss();
                            Toast.makeText(context, "There was an error "+e.getMessage(), Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        myDialog.dismiss();
                        myDialog2.dismiss();
                        Toast.makeText(context, "Error!", Toast.LENGTH_LONG).show();
                        volleyError.printStackTrace();
                    }
                }){
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("email", got_email);
                params.put("tid", "group_"+tutId);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        DefaultRetryPolicy retryPolicy = new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);
        requestQueue.add(stringRequest);
    }

}
