package com.example.handoutlms;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TaskManager1.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TaskManager1#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TaskManager1 extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    TabLayout tabLayout;
    ViewPager viewPager;
    TextView name;
    TextView no_of_task;
    String day, month, year, today;
    int count_Exam=0, count_Test=0, count_Assignment=0, count_Others=0, count_Tutorials=0, count_Gigs=0;
    TextView countExam, countTest, countAssignment, countOthers, countTutorials, countGigs;

    LinearLayout test, exam, assignment, others, tutorials, gigs;

    ArrayList<String> arr_task_name = new ArrayList<>();
    ArrayList<String> arr_task_date = new ArrayList<>();
    ArrayList<String> arr_task_category = new ArrayList<>();
    ArrayList<String> arr_task_description = new ArrayList<>();
    ArrayList<String> arr_task_time = new ArrayList<>();
    ArrayList<String> arr_today = new ArrayList<>();

    ArrayList<String> arr_task_name_gen = new ArrayList<>();
    ArrayList<String> arr_task_date_gen = new ArrayList<>();
    ArrayList<String> arr_task_category_gen = new ArrayList<>();
    ArrayList<String> arr_task_description_gen = new ArrayList<>();
    ArrayList<String> arr_task_time_gen = new ArrayList<>();

    SharedPreferences preferences;
    String got_email, got_fullname;
    public static final String GET_TASKS = "https://handoutng.com/handouts/handout_get_user_task";

    private OnFragmentInteractionListener mListener;

    public TaskManager1() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TaskManager1.
     */
    // TODO: Rename and change types and number of parameters
    public static TaskManager1 newInstance(String param1, String param2) {
        TaskManager1 fragment = new TaskManager1();
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
        View v = inflater.inflate(R.layout.fragment_task_manager1, container, false);

        Date date=new Date(System.currentTimeMillis());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        //today
        day          = (String) DateFormat.format("dd",   date); // 20
        month  = (String) DateFormat.format("MM",  date); // Jun
        year         = (String) DateFormat.format("yyyy", date); // 2013
        today = day+"/"+month+"/"+year;

        countExam = v.findViewById(R.id.countExam);
        countTest = v.findViewById(R.id.countTest);
        countAssignment = v.findViewById(R.id.countAssignment);
        countOthers = v.findViewById(R.id.countOthers);
        countTutorials = v.findViewById(R.id.countTutorials);
        countGigs = v.findViewById(R.id.countGig);
        viewPager =v.findViewById(R.id.viewpager2);
        tabLayout = v.findViewById(R.id.tabs);
        name = v.findViewById(R.id.name);
        no_of_task = v.findViewById(R.id.num_of_task);

        preferences = getActivity().getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        got_email = preferences.getString("email", "not available");
        got_fullname = preferences.getString("fullname", "not available");

        //get user task
        StringRequest stringRequest = new StringRequest(Request.Method.POST, GET_TASKS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            int ArrayLength = jsonArray.length();

                            if(ArrayLength >= 1){
                                for(int i=0; i<ArrayLength; i++){
                                    JSONObject section1 = jsonArray.getJSONObject(i);
                                    String task_date = section1.getString("ddate");
                                    String task_name = section1.getString("taskname");
                                    String task_category = section1.getString("category");
                                    String task_description = section1.getString("description");
                                    String task_time = section1.getString("dtime");

                                    arr_task_name_gen.add(task_name);
                                    arr_task_date_gen.add(task_date);
                                    arr_task_category_gen.add(task_category);
                                    arr_task_description_gen.add(task_description);
                                    arr_task_time_gen.add(task_time);

                                    if(task_date.equals(today)){
                                        arr_task_name.add(task_name);
                                        arr_task_date.add(task_date);
                                        arr_task_category.add(task_category);
                                        arr_task_description.add(task_description);
                                        arr_task_time.add(task_time);
                                        arr_today.add(today);
                                    }

                                    if(task_category.equals("Exam")){
                                        count_Exam = count_Exam+1;
                                    }
                                    if(task_category.equals("Test")){
                                        count_Test = count_Test+1;
                                    }
                                    if(task_category.equals("Assignment")){
                                        count_Assignment = count_Assignment+1;
                                    }
                                    if(task_category.equals("Tutorial")){
                                        count_Tutorials = count_Tutorials+1;
                                    }
                                    if(task_category.equals("Gig")){
                                        count_Gigs = count_Gigs+1;
                                    }
                                    if (!task_category.equals("Exam") || !task_category.equals("Test") || !task_category.equals("Assignment")){
                                        count_Others = count_Others+1;
                                    }


                                }

                                no_of_task.setText(arr_task_name.size()+" task \nfor today");
                                countExam.setText(count_Exam+" tasks");
                                countTest.setText(count_Test+" tasks");
                                countAssignment.setText(count_Assignment+" tasks");
                                countOthers.setText(count_Others+" tasks");
                                countTutorials.setText(count_Tutorials+" tasks");
                                countGigs.setText(count_Gigs+" tasks");
                            }else{
                                no_of_task.setText("0 task \nfor today");
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

        //clear array and count
        count_Exam=0;
        count_Test=0;
        count_Assignment=0;
        count_Others=0;
        count_Tutorials=0;
        count_Gigs=0;



//        no_of_task.setText(arr_task_name.size()+" task \nfor today");
        name.setText("Hi, "+got_fullname+"!");

        exam = v.findViewById(R.id.exam);
        exam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), TaskManagerClick.class);
                i.putExtra("category", "exam");
                i.putExtra("email", got_email);
                //put all the array here also
                i.putStringArrayListExtra("task name", arr_task_name_gen);
                i.putStringArrayListExtra("task date", arr_task_date_gen);
                i.putStringArrayListExtra("task category", arr_task_category_gen);
                i.putStringArrayListExtra("task description", arr_task_description_gen);
                i.putStringArrayListExtra("task time", arr_task_time_gen);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);

                arr_task_name_gen.clear();
                arr_task_date_gen.clear();
                arr_task_category_gen.clear();
                arr_task_description_gen.clear();
                arr_task_time_gen.clear();
            }
        });

        assignment = v.findViewById(R.id.assignment);
        assignment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), TaskManagerClick.class);
                i.putExtra("category", "assignment");
                i.putExtra("email", got_email);
                //put all the array here also
                i.putStringArrayListExtra("task name", arr_task_name_gen);
                i.putStringArrayListExtra("task date", arr_task_date_gen);
                i.putStringArrayListExtra("task category", arr_task_category_gen);
                i.putStringArrayListExtra("task description", arr_task_description_gen);
                i.putStringArrayListExtra("task time", arr_task_time_gen);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);

                arr_task_name_gen.clear();
                arr_task_date_gen.clear();
                arr_task_category_gen.clear();
                arr_task_description_gen.clear();
                arr_task_time_gen.clear();
            }
        });

        test =  v.findViewById(R.id.test);
        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), TaskManagerClick.class);
                i.putExtra("category", "test");
                i.putExtra("email", got_email);
                //put all the array here also
                i.putStringArrayListExtra("task name", arr_task_name_gen);
                i.putStringArrayListExtra("task date", arr_task_date_gen);
                i.putStringArrayListExtra("task category", arr_task_category_gen);
                i.putStringArrayListExtra("task description", arr_task_description_gen);
                i.putStringArrayListExtra("task time", arr_task_time_gen);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);

                arr_task_name_gen.clear();
                arr_task_date_gen.clear();
                arr_task_category_gen.clear();
                arr_task_description_gen.clear();
                arr_task_time_gen.clear();
            }
        });

        others =  v.findViewById(R.id.others);
        others.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), TaskManagerClick.class);
                i.putExtra("category", "others");
                i.putExtra("email", got_email);
                //put all the array here also
                i.putStringArrayListExtra("task name", arr_task_name_gen);
                i.putStringArrayListExtra("task date", arr_task_date_gen);
                i.putStringArrayListExtra("task category", arr_task_category_gen);
                i.putStringArrayListExtra("task description", arr_task_description_gen);
                i.putStringArrayListExtra("task time", arr_task_time_gen);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);

                arr_task_name_gen.clear();
                arr_task_date_gen.clear();
                arr_task_category_gen.clear();
                arr_task_description_gen.clear();
                arr_task_time_gen.clear();
            }
        });

        tutorials = v.findViewById(R.id.tutorials);
        tutorials.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), TaskManagerClick.class);
                i.putExtra("category", "tutorials");
                i.putExtra("email", got_email);
                //put all the array here also
                i.putStringArrayListExtra("task name", arr_task_name_gen);
                i.putStringArrayListExtra("task date", arr_task_date_gen);
                i.putStringArrayListExtra("task category", arr_task_category_gen);
                i.putStringArrayListExtra("task description", arr_task_description_gen);
                i.putStringArrayListExtra("task time", arr_task_time_gen);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);

                arr_task_name_gen.clear();
                arr_task_date_gen.clear();
                arr_task_category_gen.clear();
                arr_task_description_gen.clear();
                arr_task_time_gen.clear();
            }
        });

        gigs = v.findViewById(R.id.gigs);
        gigs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), TaskManagerClick.class);
                i.putExtra("category", "gigs");
                i.putExtra("email", got_email);
                //put all the array here also
                i.putStringArrayListExtra("task name", arr_task_name_gen);
                i.putStringArrayListExtra("task date", arr_task_date_gen);
                i.putStringArrayListExtra("task category", arr_task_category_gen);
                i.putStringArrayListExtra("task description", arr_task_description_gen);
                i.putStringArrayListExtra("task time", arr_task_time_gen);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);

                arr_task_name_gen.clear();
                arr_task_date_gen.clear();
                arr_task_category_gen.clear();
                arr_task_description_gen.clear();
                arr_task_time_gen.clear();
            }
        });

        addTabs(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setOnTabSelectedListener(
                new TabLayout.ViewPagerOnTabSelectedListener(viewPager) {

                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        if (tab.getPosition() == 0){

                        }
                        else if(tab.getPosition() == 1){

                        }
                        else if(tab.getPosition() == 2){

                        }
                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {
                        super.onTabReselected(tab);
                    }
                }
        );

        return v;
    }

    private void addTabs(ViewPager viewPager) {
        TaskManager1.ViewPagerAdapter adapter = new TaskManager1.ViewPagerAdapter(getChildFragmentManager());
        adapter.addFrag(new TodayTask(), "Today");
        adapter.addFrag(new WeekTask(), "Week");
        adapter.addFrag(new MonthTask(), "Month");
        viewPager.setAdapter(adapter);
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
    }



    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title){
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }


    }
}
