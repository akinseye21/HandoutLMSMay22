package com.example.handoutlms;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class FeedsDashboard extends AppCompatActivity implements
        FeedDashboardHome.OnFragmentInteractionListener,
        FeedDashboardMessages.OnFragmentInteractionListener,
        FeedDashboardTaskManager.OnFragmentInteractionListener,
        FeedDashboardNotification.OnFragmentInteractionListener,
        FeedDashboardProfie.OnFragmentInteractionListener,
        Tutor.OnFragmentInteractionListener,
        Games.OnFragmentInteractionListener,
        GamesProfile.OnFragmentInteractionListener,
        Gigs.OnFragmentInteractionListener,
        Profile2.OnFragmentInteractionListener,
        tutorial_on_profile.OnFragmentInteractionListener,
        TaskManager1.OnFragmentInteractionListener,
        TodayTask.OnFragmentInteractionListener,
        gig_on_profile.OnFragmentInteractionListener{

    private TabLayout tabLayout;
    private ViewPager viewPager;


    View linev;
    LinearLayout lin1;
    FrameLayout fl;
    ImageView plus;
    String email, sent_from;

    private static final int NOTIFICATION_PERMISSION_CODE = 123;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feeds_dashboard);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

//        requestNotificationPermission();

        Intent j = getIntent();
        email = j.getStringExtra("email");
        sent_from = j.getStringExtra("sent from");  //either "Login" or "Register"



        Bundle args2 = new Bundle();
        args2.putString("email", email);
        gig_on_profile gop = new gig_on_profile();
        gop.setArguments(args2);


        plus = findViewById(R.id.plus);
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(FeedsDashboard.this, AddOptions.class);
                i.putExtra("email", email);
                startActivity(i);
            }
        });
//        linev = findViewById(R.id.lineview);
//        lin1 = findViewById(R.id.lin1);
//        fl = findViewById(R.id.framelayout_);



        viewPager = findViewById(R.id.viewpager);
        addTabs(viewPager);

        tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();



        tabLayout.setOnTabSelectedListener(
                new TabLayout.ViewPagerOnTabSelectedListener(viewPager) {

                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        if (tab.getPosition() == 1){
//                            viewpager2.setVisibility(View.GONE);
//                            fl.setVisibility(View.GONE);
//                            linev.setVisibility(View.VISIBLE);
//                            lin1.setVisibility(View.VISIBLE);
//                            viewPager.setVisibility(View.VISIBLE);
//                            ext_pink.setVisibility(View.GONE);
//                            ext_yel.setVisibility(View.GONE);
//                            ext_green.setVisibility(View.GONE);
//                            tabLayout.getTabAt(1).select();
                        }
                        else if(tab.getPosition() == 2){
//                            viewpager2.setVisibility(View.GONE);
//                            fl.setVisibility(View.VISIBLE);
//                            linev.setVisibility(View.GONE);
//                            lin1.setVisibility(View.GONE);
//                            viewPager.setVisibility(View.GONE);
//                            ext_pink.setVisibility(View.GONE);
//                            ext_yel.setVisibility(View.GONE);
//                            ext_green.setVisibility(View.GONE);
//                            tabLayout.getTabAt(2).select();
                        }
                        else if(tab.getPosition() == 0){
//                            viewPager.setVisibility(View.GONE);
//                            fl.setVisibility(View.GONE);
//                            lin1.setVisibility(View.VISIBLE);
//                            linev.setVisibility(View.VISIBLE);
//                            viewPager.setVisibility(View.VISIBLE);
//                            ext_pink.setVisibility(View.GONE);
//                            ext_yel.setVisibility(View.GONE);
//                            ext_green.setVisibility(View.GONE);
//                            tabLayout.getTabAt(0).select();
//                            loadFragment(new FeedDashboardHome());
                        }
                        else if(tab.getPosition() == 3){
//                            viewpager2.setVisibility(View.GONE);
//                            fl.setVisibility(View.GONE);
//                            linev.setVisibility(View.VISIBLE);
//                            lin1.setVisibility(View.VISIBLE);
//                            viewPager.setVisibility(View.VISIBLE);
//                            ext_pink.setVisibility(View.GONE);
//                            ext_yel.setVisibility(View.GONE);
//                            ext_green.setVisibility(View.GONE);
//                            tabLayout.getTabAt(3).select();
                        }
                        else if(tab.getPosition() == 4){
//                            viewpager2.setVisibility(View.GONE);
//                            linev.setVisibility(View.GONE);
//                            viewPager.setVisibility(View.GONE);
//                            ext_pink.setVisibility(View.GONE);
//                            ext_yel.setVisibility(View.GONE);
//                            ext_green.setVisibility(View.GONE);
//                            lin1.setVisibility(View.GONE);
////                            tabLayout.getTabAt(4).select();
//                            fl.setVisibility(View.VISIBLE);
////                            getSupportFragmentManager().beginTransaction().add(R.id.fl_profile2, profile2).commit();
//                            loadFragment(new Profile2());
                        }
                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {
//                        super.onTabReselected(tab);
                        if(tab.getPosition() == 0){
                            Intent j = new Intent(getApplicationContext(), FeedsDashboard.class);
                            startActivity(j);
                            finish();
                        }
                    }
                }
        );



    }

//    private void requestNotificationPermission() {
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NOTIFICATION_POLICY) == PackageManager.PERMISSION_GRANTED)
//            return;
//
//        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_NOTIFICATION_POLICY)) {
//
//        }
//
//        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_NOTIFICATION_POLICY}, NOTIFICATION_PERMISSION_CODE );
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//
//        // Checking the request code of our request
//        if (requestCode == NOTIFICATION_PERMISSION_CODE ) {
//
//            // If permission is granted
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                // Displaying a toast
//                Toast.makeText(this, "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
//            } else {
//                // Displaying another toast if permission is not granted
//                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
//            }
//        }
//    }


//    private void loadFragment(Fragment fragment) {
//        // create a FragmentTransaction to begin the transaction and replace the Fragment
//        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//        fragmentTransaction.replace(R.id.viewpager, fragment);
//        fragmentTransaction.commit();
//    }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(R.drawable.ic33);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic31);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic99);
        tabLayout.getTabAt(3).setIcon(R.drawable.ic30);
        tabLayout.getTabAt(4).setIcon(R.drawable.ic32);
    }

    private void addTabs(ViewPager viewPager) {
        FeedsDashboard.ViewPagerAdapter adapter = new FeedsDashboard.ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new FeedDashboardHome(), "Home");
        adapter.addFrag(new FeedDashboardMessages(), "Message");
        adapter.addFrag(new TaskManager1(), "Task Manager");
        adapter.addFrag(new FeedDashboardNotification(), "Notification");
        adapter.addFrag(new Profile2(), "Profile");
        viewPager.setAdapter(adapter);
    }

//    private void addTabs2(ViewPager viewpager2) {
//        FeedsDashboard.ViewPagerAdapter adapter = new FeedsDashboard.ViewPagerAdapter(getSupportFragmentManager());
//        adapter.addFrag(new Tutor(), "");
//        adapter.addFrag(new Games(), "");
//        adapter.addFrag(new Gigs(), "");
//        viewpager2.setAdapter(adapter);
//    }
//    private void addTutorTab(ViewPager viewPager){
//        FeedsDashboard.ViewPagerAdapter adapter= new FeedsDashboard.ViewPagerAdapter(getSupportFragmentManager());
//        adapter.addFrag(new Tutor(), "");
//        viewPager.setAdapter(adapter);
//    }
//
//    private void addGameTab(ViewPager viewPager){
//        FeedsDashboard.ViewPagerAdapter adapter= new FeedsDashboard.ViewPagerAdapter(getSupportFragmentManager());
//        adapter.addFrag(new Games(), "");
//        viewPager.setAdapter(adapter);
//    }
//
//    private void addGigTab(ViewPager viewPager){
//        FeedsDashboard.ViewPagerAdapter adapter= new FeedsDashboard.ViewPagerAdapter(getSupportFragmentManager());
//        adapter.addFrag(new Gigs(), "");
//        viewPager.setAdapter(adapter);
//    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onBackPressed() {
        // do nothing
    }

    class ViewPagerAdapter extends FragmentStatePagerAdapter {
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

    public String getSentFrom(){
        return sent_from;
    }
    public String getSignupEmail(){
        return email;
    }
}
