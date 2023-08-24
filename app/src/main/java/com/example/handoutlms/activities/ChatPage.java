package com.example.handoutlms.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.example.handoutlms.R;
import com.example.handoutlms.handoutmessager.ChatFragment;
import com.example.handoutlms.handoutmessager.UsersFragment;
import com.google.android.material.tabs.TabLayout;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChatPage extends AppCompatActivity implements
        ChatFragment.OnFragmentInteractionListener,
        UsersFragment.OnFragmentInteractionListener {

    Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager viewPager;
    SharedPreferences preferences;
    String got_email;
    LinearLayout leave;

//    private FirebaseAuth mAuth;
//    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_page);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

//        mAuth = FirebaseAuth.getInstance();
//        firebaseUser = mAuth.getCurrentUser();

        preferences = getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        got_email = preferences.getString("email", "not available");

//        toolbar = findViewById(R.id.toolbar);
        leave = findViewById(R.id.leave);
        viewPager = findViewById(R.id.viewpager);
        addTab(viewPager);
        tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
        leave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                FirebaseAuth.getInstance().signOut();
                Intent i = new Intent(ChatPage.this, FeedsDashboard.class);
                i.putExtra("email", got_email);
                i.putExtra("sent from", "ChatPage");
                startActivity(i);
            }
        });


        setSupportActionBar(toolbar);



    }

    private void addTab(ViewPager viewPager){
        FeedsDashboard.ViewPagerAdapter adapter = new FeedsDashboard.ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new UsersFragment(), "Users", "user_fragment_tag");
        adapter.addFrag(new ChatFragment(), "Chats", "chats_fragment_tag");
        viewPager.setAdapter(adapter);

    }

    public void onFragmentInteraction(Uri uri) {

    }

//    class ViewPagerAdapter extends FragmentStatePagerAdapter {
//        private final List<Fragment> mFragmentList = new ArrayList<>();
//        private final List<String> mFragmentTitleList = new ArrayList<>();
//
//
//        public ViewPagerAdapter(FragmentManager fm) {
//            super(fm);
//        }
//
//        @Override
//        public Fragment getItem(int position) {
//            return mFragmentList.get(position);
//        }
//
//        @Override
//        public int getCount() {
//            return mFragmentList.size();
//        }
//
//        public void addFrag(Fragment fragment, String title){
//            mFragmentList.add(fragment);
//            mFragmentTitleList.add(title);
//        }
//
//        @Override
//        public CharSequence getPageTitle(int position) {
//            return mFragmentTitleList.get(position);
//        }
//    }

//    private void status(String status){
//        DatabaseReference reference = FirebaseDatabase.getInstance("https://handout-lms-default-rtdb.firebaseio.com/").getReference("Users").child(firebaseUser.getUid());
//        HashMap<String, Object> hashMap = new HashMap<>();
//        hashMap.put("status", status);
//
//        reference.updateChildren(hashMap);
//    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        status("online");
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        status("offline");
//    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
//        finish();
    }
}