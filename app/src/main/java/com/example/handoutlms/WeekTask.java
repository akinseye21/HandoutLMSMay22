package com.example.handoutlms;

import android.content.Context;
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
import android.widget.Toast;

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
 * Use the {@link WeekTask#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WeekTask extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    SharedPreferences preferences;
    String got_email;
    ListView week_task;
    String day, month, year, today;
    ProgressBar progressBar;
    TextView no_notification;

    ArrayList<String> arr_task_name = new ArrayList<>();
    ArrayList<String> arr_task_date = new ArrayList<>();
    ArrayList<String> arr_task_category = new ArrayList<>();
    ArrayList<String> arr_task_description = new ArrayList<>();
    ArrayList<String> arr_task_time = new ArrayList<>();
    ArrayList<String> arr_today = new ArrayList<>();

    public static final String GET_TASKS = "https://handout.com.ng/handouts/handout_get_user_task";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public WeekTask() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WeekTask.
     */
    // TODO: Rename and change types and number of parameters
    public static WeekTask newInstance(String param1, String param2) {
        WeekTask fragment = new WeekTask();
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
        View v = inflater.inflate(R.layout.fragment_week_task, container, false);

        preferences = getActivity().getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        got_email = preferences.getString("email", "not available");

        progressBar = v.findViewById(R.id.progressBar);
        no_notification = v.findViewById(R.id.no_notification);
        week_task = v.findViewById(R.id.listview_week_tasks);

        Date date=new Date(System.currentTimeMillis());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
//        int week_year = calendar.get(Calendar.WEEK_OF_YEAR);

//        Toast.makeText(getContext(), "Week of the year "+week+"\nWeek of the month "+week2, Toast.LENGTH_LONG).show();
//        System.out.println("Week of the year "+week+"\nWeek of the month "+week2);

        //today
        day          = (String) DateFormat.format("dd",   date); // 20
        int dayInt = Integer.parseInt(day);
        month  = (String) DateFormat.format("MM",  date); // Jun
        year         = (String) DateFormat.format("yyyy", date); // 2013
        today = day+"/"+month+"/"+year;

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
                                    String task_month = String.valueOf(arr[3]) + String.valueOf(arr[4]);
                                    String task_day = String.valueOf(arr[0]) + String.valueOf(arr[1]);

                                    //6 days less than today

                                    int sixLessToday = dayInt - 6;

                                    //6 days greater than today
                                    int sixGreaterToday = dayInt + 6;

                                    //if the task month is the same as today's month,
                                    //if the task day is 6 less than or 6 greater than today's date
                                    // show on the weeks page
//                                    System.out.println("Integer Day = "+dayInt);
                                    System.out.println("task day = "+task_day+", six days less = "+sixLessToday+", six days greater ="+sixGreaterToday);
//                                    Toast.makeText(getContext(), "month = "+month+"\ntask month = "+task_month, Toast.LENGTH_LONG).show();

                                    if(month.equals(task_month)){
                                        if(Integer.parseInt(task_day) >= sixLessToday && Integer.parseInt(task_day) <= sixGreaterToday){
                                            arr_task_name.add(task_name);
                                            arr_task_date.add(task_date);
                                            arr_task_category.add(task_category);
                                            arr_task_description.add(task_description);
                                            arr_task_time.add(task_time);
                                            arr_today.add(today);


                                        }
                                    }


                                }
                            }

                            if(arr_task_name.size() == 0){
                                no_notification.setVisibility(View.VISIBLE);
                                week_task.setVisibility(View.GONE);
                            }else{
                                no_notification.setVisibility(View.GONE);
                                TodayTaskAdapter myAdapter=new TodayTaskAdapter(getContext(),arr_task_name,arr_task_date,arr_task_category, arr_today);
                                week_task.setAdapter(myAdapter);
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
        requestQueue.add(stringRequest);


        //clear the array
        arr_task_name.clear();
        arr_task_date.clear();
        arr_task_category.clear();
        arr_today.clear();
        arr_task_description.clear();
        arr_task_time.clear();


        return v;
    }
}