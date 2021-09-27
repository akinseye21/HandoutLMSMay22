package com.example.handoutlms;

import android.content.Context;
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
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.google.android.material.tabs.TabLayout;

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

    RelativeLayout tutors, games, gigs;
    private TabLayout tab;
    private ViewPager viewpager2;
    LinearLayout ext_yel, ext_pink, ext_green;
    View lineview;
    ScrollView biglin;

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
        tutors = v.findViewById(R.id.tutor);
        games = v.findViewById(R.id.games);
        gigs = v.findViewById(R.id.gigs);

        ext_pink = v.findViewById(R.id.extension_red);
        ext_yel = v.findViewById(R.id.extension_yellow);
        ext_green = v.findViewById(R.id.extension_green);
        biglin = v.findViewById(R.id.biglin);
        lineview = v.findViewById(R.id.lineview);

        viewpager2 = v.findViewById(R.id.viewpager);
        addTabs2(viewpager2);
        tab = v.findViewById(R.id.tabLayout);
        tab.setupWithViewPager(viewpager2);
        tab.setOnTabSelectedListener(
                new TabLayout.ViewPagerOnTabSelectedListener(viewpager2) {

                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        if (tab.getPosition() == 1){
                            ext_pink.setVisibility(View.GONE);
                            ext_yel.setVisibility(View.VISIBLE);
                            ext_green.setVisibility(View.GONE);
                            biglin.setVisibility(View.GONE);
                            lineview.setVisibility(View.GONE);
                        }
                        else if(tab.getPosition() == 2){
                            ext_pink.setVisibility(View.GONE);
                            ext_yel.setVisibility(View.GONE);
                            ext_green.setVisibility(View.VISIBLE);
                            biglin.setVisibility(View.GONE);
                            lineview.setVisibility(View.GONE);
                        }
                        else if(tab.getPosition() == 0){
                            ext_pink.setVisibility(View.VISIBLE);
                            ext_yel.setVisibility(View.GONE);
                            ext_green.setVisibility(View.GONE);
                            biglin.setVisibility(View.GONE);
                            lineview.setVisibility(View.GONE);
                        }
                    }

                }
        );



        tutors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //hide the view
                //extend the tutors button further down
                //show tutors fragment
//                addTabs2(viewpager2);
//                linev.setVisibility(View.GONE);
//                addTutorTab(viewPager);
//                tabLayout.setVisibility(View.GONE);
//                viewPager.setVisibility(View.GONE);
                viewpager2.setVisibility(View.VISIBLE);
                tab.setVisibility(View.VISIBLE);
                tab.getTabAt(0).select();
                ext_pink.setVisibility(View.VISIBLE);
                ext_yel.setVisibility(View.GONE);
                ext_green.setVisibility(View.GONE);
                biglin.setVisibility(View.GONE);
                lineview.setVisibility(View.GONE);
//                fl.setVisibility(View.GONE);
            }
        });

        games.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //hide the view
                //extend the tutors button further down
                //show tutors fragment
//                addTabs2(viewpager2);
//                linev.setVisibility(View.GONE);
//                addGameTab(viewPager);
//                tabLayout.setVisibility(View.GONE);
//                viewPager.setVisibility(View.GONE);
                viewpager2.setVisibility(View.VISIBLE);
                tab.setVisibility(View.VISIBLE);
                tab.setupWithViewPager(viewpager2);
                tab.getTabAt(1).select();
                ext_pink.setVisibility(View.GONE);
                ext_yel.setVisibility(View.VISIBLE);
                ext_green.setVisibility(View.GONE);
                biglin.setVisibility(View.GONE);
                lineview.setVisibility(View.GONE);
//                fl.setVisibility(View.GONE);
            }
        });

        gigs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //hide the view
                //extend the tutors button further down
                //show tutors fragment
//                addTabs2(viewpager2);
//                linev.setVisibility(View.GONE);
//                addGigTab(viewPager);
//                tabLayout.setVisibility(View.GONE);
//                viewPager.setVisibility(View.GONE);
                viewpager2.setVisibility(View.VISIBLE);
                tab.setVisibility(View.VISIBLE);
                tab.getTabAt(2).select();
                ext_pink.setVisibility(View.GONE);
                ext_yel.setVisibility(View.GONE);
                ext_green.setVisibility(View.VISIBLE);
                biglin.setVisibility(View.GONE);
                lineview.setVisibility(View.GONE);
//                fl.setVisibility(View.GONE);
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