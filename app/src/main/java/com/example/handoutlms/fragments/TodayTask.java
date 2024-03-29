package com.example.handoutlms.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.format.DateFormat;
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
import com.example.handoutlms.activities.AddTask;
import com.example.handoutlms.adapters.TodayTaskAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TodayTask.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TodayTask#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TodayTask extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    LinearLayout add;
    SharedPreferences preferences;
    String got_email;
    ListView today_task;
    String day, month, year, today;
    LinearLayout progressBar;
    TextView no_notification;

    ArrayList<String> arr_task_name = new ArrayList<>();
    ArrayList<String> arr_task_date = new ArrayList<>();
    ArrayList<String> arr_task_category = new ArrayList<>();
    ArrayList<String> arr_task_description = new ArrayList<>();
    ArrayList<String> arr_task_time = new ArrayList<>();
    ArrayList<String> arr_today = new ArrayList<>();

    public static final String GET_TASKS = "https://handoutng.com/handouts/handout_get_user_task";

    private OnFragmentInteractionListener mListener;

    public TodayTask() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TodayTask.
     */
    // TODO: Rename and change types and number of parameters
    public static TodayTask newInstance(String param1, String param2) {
        TodayTask fragment = new TodayTask();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_today_task, container, false);

        progressBar = v.findViewById(R.id.progressBar);
        no_notification = v.findViewById(R.id.no_notification);


        //get user task
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, GET_TASKS,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//
//                        progressBar.setVisibility(View.GONE);
//                        try {
//                            JSONArray jsonArray = new JSONArray(response);
//                            int ArrayLength = jsonArray.length();
//
//                            System.out.println("Length = "+ArrayLength);
//
//                            if(ArrayLength >= 1){
//                                for(int i=0; i<ArrayLength; i++){
//                                    JSONObject section1 = jsonArray.getJSONObject(i);
//                                    String task_date = section1.getString("ddate");
//                                    String task_name = section1.getString("taskname");
//                                    String task_category = section1.getString("category");
//                                    String task_description = section1.getString("description");
//                                    String task_time = section1.getString("dtime");
//
//                                    System.out.println("Task Date = "+task_date+"\nToday = "+today);
//
//                                    if(task_date.equals(today)){
//                                        arr_task_name.add(task_name);
//                                        arr_task_date.add(task_date);
//                                        arr_task_category.add(task_category);
//                                        arr_task_description.add(task_description);
//                                        arr_task_time.add(task_time);
//                                        arr_today.add(today);
//                                    }
//
//
//                                }
//                            }else{
//
//                            }
//
//                            if(arr_task_name.size() == 0){
//                                no_notification.setVisibility(View.VISIBLE);
//                                today_task.setVisibility(View.GONE);
//                            }else{
//                                no_notification.setVisibility(View.GONE);
//                                TodayTaskAdapter myAdapter=new TodayTaskAdapter(getContext(),arr_task_name,arr_task_date,arr_task_category, arr_today);
//                                today_task.setAdapter(myAdapter);
//                            }
//
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//
//                    }
//                }){
//            @Override
//            protected Map<String, String> getParams(){
//                Map<String, String> params = new HashMap<>();
//                params.put("email", got_email);
//                return params;
//            }
//        };
//        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
//        requestQueue.add(stringRequest);


        today_task = v.findViewById(R.id.listview_today_tasks);


        add = v.findViewById(R.id.add);


        return v;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
    }

    @Override
    public void onStart() {
        super.onStart();

        arr_task_name.clear();
        arr_task_date.clear();
        arr_task_category.clear();
        arr_task_description.clear();
        arr_task_time.clear();
        arr_today.clear();

        preferences = getActivity().getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        got_email = preferences.getString("email", "not available");

        Date date=new Date(System.currentTimeMillis());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        //today
        day          = (String) DateFormat.format("dd",   date); // 20
        month  = (String) DateFormat.format("MM",  date); // Jun
        year         = (String) DateFormat.format("yyyy", date); // 2013
        today = year+"-"+month+"-"+day;

        //get user task
        StringRequest stringRequest = new StringRequest(Request.Method.POST, GET_TASKS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        progressBar.setVisibility(View.GONE);
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            int ArrayLength = jsonArray.length();

                            System.out.println("Length = "+ArrayLength);

                            if(ArrayLength >= 1){
                                for(int i=0; i<ArrayLength; i++){
                                    JSONObject section1 = jsonArray.getJSONObject(i);
                                    String task_date = section1.getString("ddate");
                                    String task_name = section1.getString("taskname");
                                    String task_category = section1.getString("category");
                                    String task_description = section1.getString("description");
                                    String task_time = section1.getString("dtime");

                                    System.out.println("Task Date = "+task_date+"\nToday = "+today);

                                    if(task_date.equals(today)){
                                        arr_task_name.add(task_name);
                                        arr_task_date.add(task_date);
                                        arr_task_category.add(task_category);
                                        arr_task_description.add(task_description);
                                        arr_task_time.add(task_time);
                                        arr_today.add(today);
                                    }


                                }
                            }else{

                            }

                            if(arr_task_name.size() == 0){
                                no_notification.setVisibility(View.VISIBLE);
                                today_task.setVisibility(View.GONE);
                            }else{
                                no_notification.setVisibility(View.GONE);
                                TodayTaskAdapter myAdapter=new TodayTaskAdapter(getContext(),arr_task_name,arr_task_date,arr_task_category, arr_today);
                                today_task.setAdapter(myAdapter);
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }){
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("email", got_email);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        DefaultRetryPolicy retryPolicy = new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);
        requestQueue.add(stringRequest);
        requestQueue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
            @Override
            public void onRequestFinished(Request<Object> request) {
                requestQueue.getCache().clear();
            }
        });


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), AddTask.class);
                i.putExtra("email", got_email);
                //put the arrays inside the intent also
                i.putStringArrayListExtra("task name", arr_task_name);
                i.putStringArrayListExtra("task date", arr_task_date);
                i.putStringArrayListExtra("task category", arr_task_category);
                i.putStringArrayListExtra("task description", arr_task_description);
                i.putStringArrayListExtra("task time", arr_task_time);
                startActivity(i);
            }
        });


    }
}
