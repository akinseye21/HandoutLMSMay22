package com.example.handoutlms;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Profile2.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Profile2#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Profile2 extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    TabLayout tabLayout;
    ViewPager viewPager;
    LinearLayout lintut, linpost, lingame, lingig;
    TextView email, username, dept, school, location, date, edit;
    String got_fullname, got_dept, got_institution, got_dob;
    SharedPreferences preferences;

    public static final String USER_PROFILE = "http://35.84.44.203/handouts/handout_get_user_profile";

    private OnFragmentInteractionListener mListener;

    public Profile2() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Profile2.
     */
    // TODO: Rename and change types and number of parameters
    public static Profile2 newInstance(String param1, String param2) {
        Profile2 fragment = new Profile2();
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
        View v = inflater.inflate(R.layout.fragment_profile2, container, false);

        viewPager =v.findViewById(R.id.viewpager2);
        tabLayout = v.findViewById(R.id.tabs);

        lintut = v.findViewById(R.id.lintut);
        linpost = v.findViewById(R.id.linpost);
        lingame = v.findViewById(R.id.lingame);
        lingig = v.findViewById(R.id.lingig);

        email = v.findViewById(R.id.email);
        username = v.findViewById(R.id.user_name);
        dept = v.findViewById(R.id.department);
        school = v.findViewById(R.id.school);
        location = v.findViewById(R.id.location);
        date = v.findViewById(R.id.date);
        edit = v.findViewById(R.id.edit);
        preferences = getActivity().getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        final String got_email = preferences.getString("email", "not available");

        addTabs(viewPager);

        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setOnTabSelectedListener(
                new TabLayout.ViewPagerOnTabSelectedListener(viewPager) {

                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        if (tab.getPosition() == 0){
                            lintut.setVisibility(View.VISIBLE);
                            linpost.setVisibility(View.GONE);
                            lingame.setVisibility(View.GONE);
                            lingig.setVisibility(View.GONE);
                        }
                        else if(tab.getPosition() == 1){
                            lintut.setVisibility(View.GONE);
                            linpost.setVisibility(View.VISIBLE);
                            lingame.setVisibility(View.GONE);
                            lingig.setVisibility(View.GONE);
                        }
                        else if(tab.getPosition() == 2){
                            lintut.setVisibility(View.GONE);
                            linpost.setVisibility(View.GONE);
                            lingame.setVisibility(View.VISIBLE);
                            lingig.setVisibility(View.GONE);
                        }
                        else if(tab.getPosition() == 3){
                            lintut.setVisibility(View.GONE);
                            linpost.setVisibility(View.GONE);
                            lingame.setVisibility(View.GONE);
                            lingig.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {
                        super.onTabReselected(tab);
                    }
                }
        );

        //get user profile
        StringRequest stringRequest = new StringRequest(Request.Method.POST, USER_PROFILE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("Profile = "+response);

                        try{
                                JSONObject profile = new JSONObject(response);
//                                String got_email = profile.getString("email");
                            got_fullname = profile.getString("fullname");
                            got_dob = profile.getString("dob");
                            got_institution = profile.getString("institution");
//                            got_faculty = profile.getString("faculty");
                             got_dept = profile.getString("department");

                            email.setText(got_email);
                            username.setText(got_fullname);
                            dept.setText(got_dept);
                            school.setText(got_institution);
                            date.setText(got_dob);

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

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);


        return v;
    }

    private void addTabs(ViewPager viewPager) {
        Profile2.ViewPagerAdapter adapter = new Profile2.ViewPagerAdapter(getChildFragmentManager());
        adapter.addFrag(new tutorial_on_profile(), "");
        adapter.addFrag(new post_on_profile(), "");
        adapter.addFrag(new Games(), "");
        adapter.addFrag(new gig_on_profile(), "");
        viewPager.setAdapter(adapter);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
}
