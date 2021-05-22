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

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;


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


        return v;
    }

    private void addTabs(ViewPager viewPager) {
        Profile2.ViewPagerAdapter adapter = new Profile2.ViewPagerAdapter(getChildFragmentManager());
        adapter.addFrag(new tutorial_on_profile(), "");
        adapter.addFrag(new post_on_profile(), "");
        adapter.addFrag(new Games(), "");
        adapter.addFrag(new Gigs(), "");
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
