package com.example.handoutlms;

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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

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
 * Use the {@link MonthTask#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MonthTask extends Fragment {

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
    ListView month_task;
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

    public MonthTask() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MonthTask.
     */
    // TODO: Rename and change types and number of parameters
    public static MonthTask newInstance(String param1, String param2) {
        MonthTask fragment = new MonthTask();
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
        View v = inflater.inflate(R.layout.fragment_month_task, container, false);

        preferences = getActivity().getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        got_email = preferences.getString("email", "not available");

        progressBar = v.findViewById(R.id.progressBar);
        no_notification = v.findViewById(R.id.no_notification);
        month_task = v.findViewById(R.id.listview_month_tasks);
        add = v.findViewById(R.id.add);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        //clear the array
        arr_task_name.clear();
        arr_task_date.clear();
        arr_task_category.clear();
        arr_today.clear();
        arr_task_description.clear();
        arr_task_time.clear();

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

                                    char[] arr = task_date.toCharArray();
                                    String task_month = String.valueOf(arr[5]) + String.valueOf(arr[6]);
                                    String task_day = String.valueOf(arr[8]) + String.valueOf(arr[9]);

                                    System.out.println("This month = "+month+"\nTask month = "+task_month);
//                                    Toast.makeText(getContext(), "month = "+month+"\ntask month = "+task_month, Toast.LENGTH_LONG).show();

                                    if(month.equals(task_month)){
                                        arr_task_name.add(task_name);
                                        arr_task_date.add(task_date);
                                        arr_task_category.add(task_category);
                                        arr_task_description.add(task_description);
                                        arr_task_time.add(task_time);
                                        arr_today.add(today);
                                    }


                                }
                            }

                            if(arr_task_name.size() == 0){
                                no_notification.setVisibility(View.VISIBLE);
                                month_task.setVisibility(View.GONE);
                            }else{
                                no_notification.setVisibility(View.GONE);
                                TodayTaskAdapter myAdapter=new TodayTaskAdapter(getContext(),arr_task_name,arr_task_date,arr_task_category, arr_today);
                                month_task.setAdapter(myAdapter);
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

    public interface OnFragmentInteractionListener {
    }
}