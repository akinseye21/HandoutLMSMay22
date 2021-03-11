package com.example.handoutlms;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TableLayout;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class Signup extends AppCompatActivity  implements BiodataFragment.OnFragmentInteractionListener, AcademicFragment.OnFragmentInteractionListener, FinishFragment.OnFragmentInteractionListener {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        viewPager = findViewById(R.id.viewpager);
        addTabs(viewPager);

        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();
        tabLayout.setOnTabSelectedListener(
                new TabLayout.ViewPagerOnTabSelectedListener(viewPager) {

                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        if (tab.getPosition() == 1){
                            tab.setIcon(R.drawable.ic15);
                        }
                        else if(tab.getPosition() == 2){
                            tab.setIcon(R.drawable.ic13);
                        }
                        else if(tab.getPosition() == 0){
                            tab.setIcon(R.drawable.ic46);
                        }
                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {
                        if (tab.getPosition() == 1){
                            tab.setIcon(R.drawable.ic11);
                        }
                        else if(tab.getPosition() == 2){
                            tab.setIcon(R.drawable.ic10);
                        }
                        else if(tab.getPosition() == 0){
                            tab.setIcon(R.drawable.ic12);
                        }
                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {
                        super.onTabReselected(tab);
                    }
                }
        );
    }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(R.drawable.ic12);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic14);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic10);
    }
    private void addTabs(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new BiodataFragment(viewPager), "Personal Bio Details");
        adapter.addFrag(new AcademicFragment(viewPager), "Academic Details");
        adapter.addFrag(new FinishFragment(), "Finish");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    class ViewPagerAdapter extends FragmentPagerAdapter{
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
