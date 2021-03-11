package com.example.handoutlms;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.net.Uri;
import android.os.Bundle;
import android.view.WindowManager;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class StudentDashboard extends AppCompatActivity implements
        Home.OnFragmentInteractionListener,
        Message.OnFragmentInteractionListener,
        Notification.OnFragmentInteractionListener,
        Profile.OnFragmentInteractionListener,
        Settings.OnFragmentInteractionListener,
        FindStudent.OnFragmentInteractionListener,
        DiscoveredStudents.OnFragmentInteractionListener,
        ConnectedStudents.OnFragmentInteractionListener,
        MessageChat.OnFragmentInteractionListener,
        Tutors.OnFragmentInteractionListener{

    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_dashboard);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        viewPager = findViewById(R.id.viewpager);
        addTabs(viewPager);

        tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();
    }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(R.drawable.ic33);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic31);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic30);
        tabLayout.getTabAt(3).setIcon(R.drawable.ic32);
        tabLayout.getTabAt(4).setIcon(R.drawable.ic29);
    }

    private void addTabs(ViewPager viewPager) {
        StudentDashboard.ViewPagerAdapter adapter = new StudentDashboard.ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new Home(), "Home");
        adapter.addFrag(new Message(), "Message");
        adapter.addFrag(new Notification(), "Notification");
        adapter.addFrag(new Profile(), "Profile");
        adapter.addFrag(new Settings(), "Settings");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

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
