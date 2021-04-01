package com.example.handoutlms;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TutorHome.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TutorHome#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TutorHome extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    RelativeLayout total_groups, create_tut_group, create_courses, create_quiz;

    private TabLayout tabLayout;
    private ViewPager viewPager;

    private OnFragmentInteractionListener mListener;

    public TutorHome() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TutorHome.
     */
    // TODO: Rename and change types and number of parameters
    public static TutorHome newInstance(String param1, String param2) {
        TutorHome fragment = new TutorHome();
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
        View v = inflater.inflate(R.layout.fragment_tutor_home, container, false);
        total_groups = v.findViewById(R.id.total_groups);
//        create_tut_group = v.findViewById(R.id.tut_group);
//        create_courses = v.findViewById(R.id.new_courses);
//        create_quiz = v.findViewById(R.id.new_quiz);

        viewPager = v.findViewById(R.id.viewpager);
        addTabs(viewPager);

        tabLayout = v.findViewById(R.id.tabLayout);
//        tabLayout.setupWithViewPager(viewPager);
//        setupTabIcons();

        total_groups.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //load total groups page
                loadFragment(new ViewTutorialGroups());
            }
        });
//        create_tut_group.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //load total groups page
//                loadFragment(new CreateTutorialGroup());
//            }
//        });
//        create_courses.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //load total groups page
//                loadFragment(new CreateCourses());
//            }
//        });
//        create_quiz.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //load total groups page
//                loadFragment(new CreateQuiz());
//            }
//        });

        return v;
    }

//    private void setupTabIcons() {
//        tabLayout.getTabAt(0).setIcon(R.drawable.ic33);
//        tabLayout.getTabAt(1).setIcon(R.drawable.ic31);
//    }

    private void addTabs(ViewPager viewPager) {
        TutorHome.ViewPagerAdapter adapter = new TutorHome.ViewPagerAdapter(getChildFragmentManager());
        adapter.addFrag(new TutorHomeView1(), "");
        adapter.addFrag(new TutorHomeView2(), "");
        viewPager.setAdapter(adapter);
    }

    private void loadFragment(Fragment fragment) {
        //create fragment manager
        FragmentManager fm = getFragmentManager();
        // create a FragmentTransaction to begin the transaction and replace the Fragment
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        // replace the LinearLayout with new Fragment
        fragmentTransaction.replace(R.id.frame_tutor_home, fragment);
        fragmentTransaction.addToBackStack("tutor home");
        fragmentTransaction.commit(); // save the changes
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
