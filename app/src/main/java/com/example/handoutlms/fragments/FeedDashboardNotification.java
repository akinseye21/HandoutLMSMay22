package com.example.handoutlms.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.handoutlms.R;
import com.example.handoutlms.adapters.NotificationAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import me.leolin.shortcutbadger.ShortcutBadger;

public class FeedDashboardNotification extends Fragment {


    SharedPreferences preferences;
    String got_email;
    ListView unreadNotification, readNotification;
    TextView notificationCount;
    LinearLayout lin_unreadNot, lin_readNot;
    View viewUnread, viewRead;
    LinearLayout loading;
    TextView noNotification;

    ArrayList<String> arr_title = new ArrayList<>();
    ArrayList<String> arr_message = new ArrayList<>();
    ArrayList<String> arr_status = new ArrayList<>();
    ArrayList<String> arr_logo = new ArrayList<>();
    ArrayList<String> arr_profImg = new ArrayList<>();
    ArrayList<String> arr_type = new ArrayList<>();
    ArrayList<String> arr_id = new ArrayList<>();
    ArrayList<String> arr_time = new ArrayList<>();
    ArrayList<String> arr_date = new ArrayList<>();



    public static final String USER_PROFILE = "https://handoutng.com/handouts/handout_get_my_notifications";

    private OnFragmentInteractionListener mListener;

    public FeedDashboardNotification() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_feed_dashboard_notification, container, false);

        preferences = getActivity().getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        got_email = preferences.getString("email", "not available");

        unreadNotification = v.findViewById(R.id.listview_notification);
        readNotification = v.findViewById(R.id.listview_readNotification);
        notificationCount = v.findViewById(R.id.notificationCount);
        lin_unreadNot = v.findViewById(R.id.unreadNotification);
        lin_readNot = v.findViewById(R.id.readNotification);
        viewUnread = v.findViewById(R.id.viewUnread);
        viewRead = v.findViewById(R.id.viewRead);
        loading = v.findViewById(R.id.loading);
        noNotification = v.findViewById(R.id.no_notification);

        //get user notifications
        getNotification();
        //clear array
        arr_title.clear();
        arr_message.clear();
        arr_status.clear();
        arr_logo.clear();
        arr_profImg.clear();
        arr_type.clear();
        arr_id.clear();
        arr_time.clear();
        arr_date.clear();


        lin_readNot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loading.setVisibility(View.VISIBLE);
                viewUnread.setVisibility(View.GONE);
                viewRead.setVisibility(View.VISIBLE);
                unreadNotification.setVisibility(View.GONE);
                readNotification.setVisibility(View.VISIBLE);
                noNotification.setVisibility(View.GONE);
                //load from DB

                StringRequest stringRequest = new StringRequest(Request.Method.POST, USER_PROFILE,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                try{
                                    JSONObject profile = new JSONObject(response);
                                    String sys_notification = profile.getString("sys_notification");
                                    JSONArray arr = new JSONArray(sys_notification);

                                    int unreadCount = 0;
                                    int arr_length = arr.length();
//                                    notificationCount.setText(arr_length+" unread notification(s)");
                                    for(int i =0; i<arr_length; i++){
                                        JSONObject section1 = arr.getJSONObject(i);
                                        String title = section1.getString("title");
                                        String message = section1.getString("message");
                                        String status = section1.getString("status");
                                        String logo = section1.getString("logo");
                                        String profImg = section1.getString("img");
                                        String type = section1.getString("ntype");
                                        String id = section1.getString("id");
                                        String time = section1.getString("created_at_time");
                                        String date = section1.getString("created_at_date");

                                        if (status.equals("read")){
                                            arr_title.add(title);
                                            arr_message.add(message);
                                            arr_status.add(status);
                                            arr_logo.add(logo);
                                            arr_profImg.add(profImg);
                                            arr_type.add(type);
                                            arr_id.add(id);
                                            arr_time.add(time);
                                            arr_date.add(date);
                                        }
                                        else {
                                            unreadCount = unreadCount + 1;
                                        }
                                    }
                                    notificationCount.setText(unreadCount+" unread notification(s)");

                                    if (arr_message.size() == 0){
                                        loading.setVisibility(View.GONE);
                                        noNotification.setVisibility(View.VISIBLE);
                                        readNotification.setVisibility(View.GONE);
                                    }else{
                                        loading.setVisibility(View.GONE);
                                        NotificationAdapter myAdapter=new NotificationAdapter(getActivity(),arr_title,arr_message,arr_status, arr_logo, arr_profImg, arr_type, arr_id, arr_time, arr_date);
                                        readNotification.setAdapter(myAdapter);
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
                        params.put("email", got_email);
                        return params;
                    }
                };

                RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
                DefaultRetryPolicy retryPolicy = new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                stringRequest.setRetryPolicy(retryPolicy);
                requestQueue.add(stringRequest);

                //clear array
                arr_title.clear();
                arr_message.clear();
                arr_status.clear();
                arr_logo.clear();
                arr_profImg.clear();
                arr_type.clear();
                arr_id.clear();
                arr_time.clear();
                arr_date.clear();
            }
        });

        lin_unreadNot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loading.setVisibility(View.VISIBLE);
                viewUnread.setVisibility(View.VISIBLE);
                viewRead.setVisibility(View.GONE);
                unreadNotification.setVisibility(View.VISIBLE);
                readNotification.setVisibility(View.GONE);
                noNotification.setVisibility(View.GONE);
                //load from DB

                StringRequest stringRequest = new StringRequest(Request.Method.POST, USER_PROFILE,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                try{
                                    JSONObject profile = new JSONObject(response);
                                    String sys_notification = profile.getString("sys_notification");
                                    JSONArray arr = new JSONArray(sys_notification);
                                    int unreadCount = 0;
                                    int arr_length = arr.length();
//                                    notificationCount.setText(arr_length+" unread notification(s)");
                                    for(int i =0; i<arr_length; i++){
                                        JSONObject section1 = arr.getJSONObject(i);
                                        String title = section1.getString("title");
                                        String message = section1.getString("message");
                                        String status = section1.getString("status");
                                        String logo = section1.getString("logo");
                                        String profImg = section1.getString("img");
                                        String type = section1.getString("ntype");
                                        String id = section1.getString("id");
                                        String time = section1.getString("created_at_time");
                                        String date = section1.getString("created_at_date");

                                        if (status.equals("unread")){
                                            unreadCount = unreadCount + 1;

                                            arr_title.add(title);
                                            arr_message.add(message);
                                            arr_status.add(status);
                                            arr_logo.add(logo);
                                            arr_profImg.add(profImg);
                                            arr_type.add(type);
                                            arr_id.add(id);
                                            arr_time.add(time);
                                            arr_date.add(date);
                                        }

                                    }
                                    notificationCount.setText(unreadCount+" unread notification(s)");

                                    if (arr_message.size() == 0){
                                        loading.setVisibility(View.GONE);
                                        noNotification.setVisibility(View.VISIBLE);
                                        unreadNotification.setVisibility(View.GONE);
                                    }else{
                                        loading.setVisibility(View.GONE);
                                        NotificationAdapter myAdapter=new NotificationAdapter(getActivity(),arr_title,arr_message,arr_status, arr_logo, arr_profImg, arr_type, arr_id, arr_time, arr_date);
                                        unreadNotification.setAdapter(myAdapter);
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
                        params.put("email", got_email);
                        return params;
                    }
                };

                RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
                DefaultRetryPolicy retryPolicy = new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                stringRequest.setRetryPolicy(retryPolicy);
                requestQueue.add(stringRequest);

                //clear array
                arr_title.clear();
                arr_message.clear();
                arr_status.clear();
                arr_logo.clear();
                arr_profImg.clear();
                arr_type.clear();
                arr_id.clear();
                arr_time.clear();
                arr_date.clear();
            }
        });



        return v;
    }

    public void getNotification() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, USER_PROFILE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try{
                            JSONObject profile = new JSONObject(response);
                            String sys_notification = profile.getString("sys_notification");
                            JSONArray arr = new JSONArray(sys_notification);

                            int arr_length = arr.length();
                            int unreadCount = 0;

                            ShortcutBadger.applyCount(getContext(), arr_length);

//                            notificationCount.setText(arr_length+" unread notification(s)");

                            for(int i =0; i<arr_length; i++){
                                JSONObject section1 = arr.getJSONObject(i);
                                String title = section1.getString("title");
                                String message = section1.getString("message");
                                String status = section1.getString("status");
                                String logo = section1.getString("logo");
                                String profImg = section1.getString("img");
                                String type = section1.getString("ntype");
                                String id = section1.getString("id");
                                String time = section1.getString("created_at_time");
                                String date = section1.getString("created_at_date");

                                if (status.equals("unread")){
                                    unreadCount = unreadCount + 1;

                                    arr_title.add(title);
                                    arr_message.add(message);
                                    arr_status.add(status);
                                    arr_logo.add(logo);
                                    arr_profImg.add(profImg);
                                    arr_type.add(type);
                                    arr_id.add(id);
                                    arr_time.add(time);
                                    arr_date.add(date);
                                }
                            }
                            notificationCount.setText(unreadCount+" unread notification(s)");


                            if (arr_message.size() == 0){
                                loading.setVisibility(View.GONE);
                                noNotification.setVisibility(View.VISIBLE);
                                unreadNotification.setVisibility(View.GONE);
                            }else{
                                loading.setVisibility(View.GONE);
                                NotificationAdapter myAdapter=new NotificationAdapter(getActivity(),arr_title,arr_message,arr_status, arr_logo, arr_profImg, arr_type, arr_id, arr_time, arr_date);
                                unreadNotification.setAdapter(myAdapter);
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
                params.put("email", got_email);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
        DefaultRetryPolicy retryPolicy = new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);
        requestQueue.add(stringRequest);


        //clear array
        arr_title.clear();
        arr_message.clear();
        arr_status.clear();
        arr_logo.clear();
        arr_profImg.clear();
        arr_type.clear();
        arr_id.clear();
        arr_time.clear();
        arr_date.clear();

    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

//    @Override
//    public void onStart() {
//        super.onStart();
//        getNotification();
//    }
}
