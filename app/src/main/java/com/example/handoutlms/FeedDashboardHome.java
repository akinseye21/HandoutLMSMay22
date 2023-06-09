package com.example.handoutlms;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FeedDashboardHome.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FeedDashboardHome#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FeedDashboardHome extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentViewCreatedListener callback;
    RelativeLayout tutors, games, gigs;
    TabLayout tab;
    ViewPager viewpager2;
    LinearLayout ext_yel, ext_pink, ext_green;
    View lineview;
    ListView my_list;
    TextView noNot;
    int ArrayLength;
//    ScrollView biglin;

    ProgressBar progressBar;
    TextView progressText;

    TabLayout tabLayout;
    SwipeRefreshLayout swipeRefreshLayout;
    SharedPreferences preferences;
    String got_email;


    //array for all users
    final ArrayList<String> Array_type = new ArrayList<>();
    final ArrayList<String> Array_createdBy = new ArrayList<>();
    final ArrayList<String> Array_createdByName = new ArrayList<>();
    final ArrayList<String> Array_groupName = new ArrayList<>();
    final ArrayList<String> Array_university = new ArrayList<>();
    final ArrayList<String> Array_mode = new ArrayList<>();
    final ArrayList<String> Array_groupNameInside = new ArrayList<>();
    final ArrayList<String> Array_description = new ArrayList<>();
    final ArrayList<String> Array_time = new ArrayList<>();
    final ArrayList<String> Array_date = new ArrayList<>();
    final ArrayList<String> Array_cardmode = new ArrayList<>();
    final ArrayList<String> Array_category = new ArrayList<>();
    final ArrayList<String> Array_id = new ArrayList<>();
    final ArrayList<String> Array_pic = new ArrayList<>();
    final ArrayList<String> Array_total_approved = new ArrayList<>();

//    public static final String ALL_TUTORIALS = "http://35.84.44.203/handouts/handout_get_all_tutorials";
    public static final String ALL_GIGS_AND_TUTORIALS = "http://handoutng.com/handouts/handout_gigs_groups";

    private OnFragmentInteractionListener mListener;

    public FeedDashboardHome() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FeedDashboardHome.
     */
    // TODO: Rename and change types and number of parameters
    public static FeedDashboardHome newInstance(String param1, String param2) {
        FeedDashboardHome fragment = new FeedDashboardHome();
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
        View v =inflater.inflate(R.layout.fragment_feed_dashboard_home, container, false);

        // Call the callback method to notify the Activity
        if (callback != null) {
            callback.onFragmentViewCreated(v);
        }

        preferences = getContext().getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        got_email = preferences.getString("email", "not available");

        tutors = v.findViewById(R.id.tutor);
        games = v.findViewById(R.id.games);
        gigs = v.findViewById(R.id.gigs);
        noNot = v.findViewById(R.id.no_not);

        ext_pink = v.findViewById(R.id.extension_red);
        ext_yel = v.findViewById(R.id.extension_yellow);
        ext_green = v.findViewById(R.id.extension_green);
//        biglin = v.findViewById(R.id.biglin);
        lineview = v.findViewById(R.id.lineview);

        my_list = v.findViewById(R.id.my_list);
        swipeRefreshLayout = v.findViewById(R.id.swiperefresh);


        progressBar = v.findViewById(R.id.progressBar);
        progressText = v.findViewById(R.id.progressText);

        viewpager2 = v.findViewById(R.id.viewpager);
        addTabs2(viewpager2);
        tab = v.findViewById(R.id.tablayout);
        tab.setupWithViewPager(viewpager2);
        tab.setOnTabSelectedListener(
                new TabLayout.ViewPagerOnTabSelectedListener(viewpager2) {

                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        if (tab.getPosition() == 1){
                            ext_pink.setVisibility(View.GONE);
                            ext_yel.setVisibility(View.VISIBLE);
                            ext_green.setVisibility(View.GONE);
//                            biglin.setVisibility(View.GONE);
                            lineview.setVisibility(View.GONE);
                            noNot.setVisibility(View.GONE);
                            progressBar.setVisibility(View.GONE);
                            progressText.setVisibility(View.GONE);
                            my_list.setVisibility(View.GONE);
                        }
                        else if(tab.getPosition() == 2){
                            ext_pink.setVisibility(View.GONE);
                            ext_yel.setVisibility(View.GONE);
                            ext_green.setVisibility(View.VISIBLE);
//                            biglin.setVisibility(View.GONE);
                            lineview.setVisibility(View.GONE);
                            noNot.setVisibility(View.GONE);
                            progressBar.setVisibility(View.GONE);
                            progressText.setVisibility(View.GONE);
                            my_list.setVisibility(View.GONE);
                        }
                        else if(tab.getPosition() == 0){
                            ext_pink.setVisibility(View.VISIBLE);
                            ext_yel.setVisibility(View.GONE);
                            ext_green.setVisibility(View.GONE);
//                            biglin.setVisibility(View.GONE);
                            lineview.setVisibility(View.GONE);
                            noNot.setVisibility(View.GONE);
                            progressBar.setVisibility(View.GONE);
                            progressText.setVisibility(View.GONE);
                            my_list.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {
                        super.onTabReselected(tab);
                    }
                }
        );





        StringRequest stringRequest = new StringRequest(Request.Method.GET, ALL_GIGS_AND_TUTORIALS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            ArrayLength = jsonArray.length();

                            if(ArrayLength<1){
                                noNot.setVisibility(View.VISIBLE);
                                //hide progressBar and progressText
                                progressBar.setVisibility(View.GONE);
                                progressText.setVisibility(View.GONE);
                                swipeRefreshLayout.setVisibility(View.GONE);
                            }else{

//                            for (int i = ArrayLength - 1; i >= 0; i--) {
                                for (int i = 0; i < ArrayLength; i++) {
                                    JSONObject section = jsonArray.getJSONObject(i);
                                    String type = section.getString("type");

                                    if (type.equals("group")){
                                        String createdBy = section.getString("created_by");
                                        String createdByName = section.getString("fullname");
                                        String groupName = section.getString("gig_group_name");
                                        String university = section.getString("school");
                                        String mode = section.getString("faculty");
                                        String groupNameInside = section.getString("gig_group_name");
                                        String description = section.getString("description");
                                        String time = section.getString("_time");
                                        String date = section.getString("_date");
                                        String tut_mode = section.getString("mode");
                                        String category = section.getString("category");
                                        String id = section.getString("tid");
                                        String pic = section.getString("creator_pic");
                                        String total_approved = section.getString("total_approved");

                                        Array_type.add(type);
                                        Array_createdBy.add(createdBy);
                                        Array_createdByName.add(createdByName);
                                        Array_groupName.add(groupName);
                                        Array_university.add(university);
                                        Array_mode.add(mode);
                                        Array_groupNameInside.add(groupNameInside);
                                        Array_description.add(description);
                                        Array_time.add(time);
                                        Array_date.add(date);
                                        Array_cardmode.add(tut_mode);
                                        Array_category.add(category);
                                        Array_id.add(id);
                                        Array_pic.add(pic);
                                        Array_total_approved.add(total_approved);
                                    }
                                    else if (type.equals("gigs")){
                                        String createdBy = section.getString("created_by"); //1
                                        String createdByName = section.getString("fullname"); //2
                                        String gigName = section.getString("gig_group_name"); //3
                                        String university = section.getString("school"); //4
                                        String description = section.getString("description"); //7
                                        String budgetCategory = section.getString("budget_category"); //9
                                        String faculty = section.getString("faculty"); //6
                                        String date = section.getString("_date");
                                        String gig_mode = section.getString("mode");
                                        String department = section.getString("department");//8
                                        String skills = section.getString("skills"); //5
                                        String id = section.getString("tid");
                                        String pic = section.getString("creator_pic");
                                        String total_approved = section.getString("total_approved");

                                        Array_type.add(type);
                                        Array_createdBy.add(createdBy);
                                        Array_createdByName.add(createdByName);
                                        Array_groupName.add(gigName);
                                        Array_university.add(university);
                                        Array_mode.add(skills);
                                        Array_groupNameInside.add(faculty);
                                        Array_description.add(description);
                                        Array_time.add(department);
                                        Array_date.add(date);
                                        Array_cardmode.add(gig_mode);
                                        Array_category.add(budgetCategory);
                                        Array_id.add(id);
                                        Array_pic.add(pic);
                                        Array_total_approved.add(total_approved);
                                    }

                                }
                            }


                            //populate values on the gridview
                            HomeListViewAdapter homeListViewAdapter = new HomeListViewAdapter(getActivity(), Array_type, Array_createdBy, Array_createdByName, Array_groupName, Array_university, Array_mode, Array_groupNameInside, Array_description, Array_time, Array_date, Array_cardmode, Array_category, Array_id, Array_pic, Array_total_approved);
                            my_list.setAdapter(homeListViewAdapter);
                            //hide progressBar and progressText
                            progressBar.setVisibility(View.GONE);
                            progressText.setVisibility(View.GONE);

                        } catch (JSONException e) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String status = jsonObject.getString("status");
                                if(status.equals("no group")){
                                    noNot.setVisibility(View.VISIBLE);
                                    //hide progressBar and progressText
                                    progressBar.setVisibility(View.GONE);
                                    progressText.setVisibility(View.GONE);
                                    swipeRefreshLayout.setVisibility(View.GONE);
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
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        DefaultRetryPolicy retryPolicy = new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);
        requestQueue.add(stringRequest);

        Array_type.clear();
        Array_createdBy.clear();
        Array_createdByName.clear();
        Array_groupName.clear();
        Array_university.clear();
        Array_mode.clear();
        Array_groupNameInside.clear();
        Array_description.clear();
        Array_time.clear();
        Array_date.clear();
        Array_cardmode.clear();
        Array_category.clear();
        Array_id.clear();
        Array_pic.clear();
        Array_total_approved.clear();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                StringRequest stringRequest = new StringRequest(Request.Method.GET, ALL_GIGS_AND_TUTORIALS,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONArray jsonArray = new JSONArray(response);
                                    ArrayLength = jsonArray.length();

                                    if(ArrayLength<1){
                                        noNot.setVisibility(View.VISIBLE);
                                        //hide progressBar and progressText
                                        progressBar.setVisibility(View.GONE);
                                        progressText.setVisibility(View.GONE);
                                        my_list.setVisibility(View.GONE);
                                    }else{

//                            for (int i = ArrayLength - 1; i >= 0; i--) {
                                        for (int i = 0; i < ArrayLength; i++) {
                                            JSONObject section = jsonArray.getJSONObject(i);
                                            String type = section.getString("type");

                                            if (type.equals("group")){
                                                String createdBy = section.getString("created_by");
                                                String createdByName = section.getString("fullname");
                                                String groupName = section.getString("gig_group_name");
                                                String university = section.getString("school");
                                                String mode = section.getString("faculty");
                                                String groupNameInside = section.getString("gig_group_name");
                                                String description = section.getString("description");
                                                String time = section.getString("_time");
                                                String date = section.getString("_date");
                                                String tut_mode = section.getString("mode");
                                                String category = section.getString("category");
                                                String id = section.getString("tid");
                                                String pic = section.getString("creator_pic");
                                                String total_approved = section.getString("total_approved");

                                                Array_type.add(type);
                                                Array_createdBy.add(createdBy);
                                                Array_createdByName.add(createdByName);
                                                Array_groupName.add(groupName);
                                                Array_university.add(university);
                                                Array_mode.add(mode);
                                                Array_groupNameInside.add(groupNameInside);
                                                Array_description.add(description);
                                                Array_time.add(time);
                                                Array_date.add(date);
                                                Array_cardmode.add(tut_mode);
                                                Array_category.add(category);
                                                Array_id.add(id);
                                                Array_pic.add(pic);
                                                Array_total_approved.add(total_approved);
                                            }
                                            else if (type.equals("gigs")){
                                                String createdBy = section.getString("created_by"); //1
                                                String createdByName = section.getString("fullname"); //2
                                                String gigName = section.getString("gig_group_name"); //3
                                                String university = section.getString("school"); //4
                                                String description = section.getString("description"); //7
                                                String budgetCategory = section.getString("budget_category"); //9
                                                String faculty = section.getString("faculty"); //6
                                                String date = section.getString("_date");
                                                String gig_mode = section.getString("mode");
                                                String department = section.getString("department");//8
                                                String skills = section.getString("skills"); //5
                                                String id = section.getString("tid");
                                                String pic = section.getString("creator_pic");
                                                String total_approved = section.getString("total_approved");

                                                Array_type.add(type);
                                                Array_createdBy.add(createdBy);
                                                Array_createdByName.add(createdByName);
                                                Array_groupName.add(gigName);
                                                Array_university.add(university);
                                                Array_mode.add(skills);
                                                Array_groupNameInside.add(faculty);
                                                Array_description.add(description);
                                                Array_time.add(department);
                                                Array_date.add(date);
                                                Array_cardmode.add(gig_mode);
                                                Array_category.add(budgetCategory);
                                                Array_id.add(id);
                                                Array_pic.add(pic);
                                                Array_total_approved.add(total_approved);
                                            }

                                        }
                                    }


                                    //populate values on the gridview
                                    HomeListViewAdapter homeListViewAdapter = new HomeListViewAdapter(getActivity(), Array_type, Array_createdBy, Array_createdByName, Array_groupName, Array_university, Array_mode, Array_groupNameInside, Array_description, Array_time, Array_date, Array_cardmode, Array_category, Array_id, Array_pic, Array_total_approved);
                                    my_list.setAdapter(homeListViewAdapter);
                                    //hide progressBar and progressText
                                    progressBar.setVisibility(View.GONE);
                                    progressText.setVisibility(View.GONE);

                                } catch (JSONException e) {
                                    try {
                                        JSONObject jsonObject = new JSONObject(response);
                                        String status = jsonObject.getString("status");
                                        if(status.equals("no group")){
                                            noNot.setVisibility(View.VISIBLE);
                                            //hide progressBar and progressText
                                            progressBar.setVisibility(View.GONE);
                                            progressText.setVisibility(View.GONE);
                                            swipeRefreshLayout.setVisibility(View.GONE);
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
                RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
                DefaultRetryPolicy retryPolicy = new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                stringRequest.setRetryPolicy(retryPolicy);
                requestQueue.add(stringRequest);


                Array_type.clear();
                Array_createdBy.clear();
                Array_createdByName.clear();
                Array_groupName.clear();
                Array_university.clear();
                Array_mode.clear();
                Array_groupNameInside.clear();
                Array_description.clear();
                Array_time.clear();
                Array_date.clear();
                Array_cardmode.clear();
                Array_category.clear();
                Array_id.clear();
                Array_pic.clear();
                Array_total_approved.clear();
                swipeRefreshLayout.setRefreshing(false);
            }
        });


//        ViewPager viewPager = getActivity().findViewById(R.id.viewpager);
//        tabLayout = getActivity().findViewById(R.id.tablayout);
////        tabLayout.setupWithViewPager(viewPager);
//        tabLayout.setOnTabSelectedListener(
//                new TabLayout.OnTabSelectedListener() {
//
//                    @Override
//                    public void onTabSelected(TabLayout.Tab tab) {
//                        if (tab.getPosition() == 1){
//
//                        }
//                        else if(tab.getPosition() == 2){
//
//                        }
//                        else if(tab.getPosition() == 0){
//                            viewpager2.setVisibility(View.GONE);
//                            ext_pink.setVisibility(View.GONE);
//                            ext_yel.setVisibility(View.GONE);
//                            ext_green.setVisibility(View.GONE);
//                            swipeRefreshLayout.setVisibility(View.GONE);
//                            lineview.setVisibility(View.GONE);
////                            navigateFragment(0);
//                            noNot.setVisibility(View.GONE);
//                            progressBar.setVisibility(View.GONE);
//                            progressText.setVisibility(View.GONE);
//                            swipeRefreshLayout.setVisibility(View.GONE);
//
//                        }
//                        else if(tab.getPosition() == 3){
//
//                        }
//                        else if(tab.getPosition() == 4){
//
//                        }
//                    }
//
//                    @Override
//                    public void onTabUnselected(TabLayout.Tab tab) {
//
//                    }
//
//                    @Override
//                    public void onTabReselected(TabLayout.Tab tab) {
////                        super.onTabReselected(tab);
//                        if(tab.getPosition() == 0){
//                            viewpager2.setVisibility(View.GONE);
//                            ext_pink.setVisibility(View.GONE);
//                            ext_yel.setVisibility(View.GONE);
//                            ext_green.setVisibility(View.GONE);
//                            swipeRefreshLayout.setVisibility(View.GONE);
//                            lineview.setVisibility(View.GONE);
////                            navigateFragment(0);
//                            noNot.setVisibility(View.GONE);
//                            progressBar.setVisibility(View.GONE);
//                            progressText.setVisibility(View.GONE);
//                            swipeRefreshLayout.setVisibility(View.GONE);
//
//                        }
//                    }
//                }
//        );

//        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//                if(tab.getPosition() == 0){
//                    Intent j = new Intent(getContext(), FeedsDashboard.class);
//                    j.putExtra("email", got_email);
//                    j.putExtra("sent from", "");
//                    startActivity(j);
//                    getActivity().finish();
//                }
//            }
//        });


        tutors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewpager2.setVisibility(View.VISIBLE);
                tab.setVisibility(View.VISIBLE);
                tab.getTabAt(0).select();
                ext_pink.setVisibility(View.VISIBLE);
                ext_yel.setVisibility(View.GONE);
                ext_green.setVisibility(View.GONE);
                swipeRefreshLayout.setVisibility(View.GONE);
                lineview.setVisibility(View.GONE);

                noNot.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                progressText.setVisibility(View.GONE);
                swipeRefreshLayout.setVisibility(View.GONE);
            }
        });

        games.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewpager2.setVisibility(View.VISIBLE);
                tab.setVisibility(View.VISIBLE);
                tab.getTabAt(1).select();
                ext_pink.setVisibility(View.GONE);
                ext_yel.setVisibility(View.VISIBLE);
                ext_green.setVisibility(View.GONE);
                swipeRefreshLayout.setVisibility(View.GONE);
                lineview.setVisibility(View.GONE);

                noNot.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                progressText.setVisibility(View.GONE);
                swipeRefreshLayout.setVisibility(View.GONE);
            }
        });

        gigs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewpager2.setVisibility(View.VISIBLE);
                tab.setVisibility(View.VISIBLE);
                tab.getTabAt(2).select();
                ext_pink.setVisibility(View.GONE);
                ext_yel.setVisibility(View.GONE);
                ext_green.setVisibility(View.VISIBLE);
                swipeRefreshLayout.setVisibility(View.GONE);
                lineview.setVisibility(View.GONE);

                noNot.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                progressText.setVisibility(View.GONE);
                swipeRefreshLayout.setVisibility(View.GONE);
            }
        });

        return v;
    }

    private void addTabs2(ViewPager viewpager2) {
        FeedDashboardHome.ViewPagerAdapter adapter = new FeedDashboardHome.ViewPagerAdapter(getChildFragmentManager());
        adapter.addFrag(new Tutor(), "");
        adapter.addFrag(new Games(), "");
        adapter.addFrag(new Gigs(), "");
        viewpager2.setAdapter(adapter);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentViewCreatedListener) {
            callback = (OnFragmentViewCreatedListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callback = null;
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
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

    public interface OnFragmentViewCreatedListener {
        void onFragmentViewCreated(View view);
    }

}
