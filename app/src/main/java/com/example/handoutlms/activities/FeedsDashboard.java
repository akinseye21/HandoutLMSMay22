package com.example.handoutlms.activities;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.handoutlms.fragments.ChatPage2;
import com.example.handoutlms.fragments.FeedDashboardHome;
import com.example.handoutlms.fragments.FeedDashboardNotification;
import com.example.handoutlms.fragments.FeedDashboardProfie;
import com.example.handoutlms.fragments.FeedDashboardTaskManager;
import com.example.handoutlms.fragments.Games;
import com.example.handoutlms.fragments.GamesProfile;
import com.example.handoutlms.fragments.Gigs;
import com.example.handoutlms.fragments.Profile2;
import com.example.handoutlms.R;
import com.example.handoutlms.fragments.TaskManager1;
import com.example.handoutlms.fragments.TodayTask;
import com.example.handoutlms.fragments.Tutor;
import com.example.handoutlms.fragments.gig_on_profile;
import com.example.handoutlms.fragments.gig_on_profile_others;
import com.example.handoutlms.fragments.tutorial_on_profile;
import com.example.handoutlms.fragments.tutorial_on_profile_others;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class FeedsDashboard extends AppCompatActivity implements
        FeedDashboardHome.OnFragmentInteractionListener,
        ChatPage2.OnFragmentInteractionListener,
        FeedDashboardTaskManager.OnFragmentInteractionListener,
        FeedDashboardNotification.OnFragmentInteractionListener,
        FeedDashboardProfie.OnFragmentInteractionListener,
        Tutor.OnFragmentInteractionListener,
        Games.OnFragmentInteractionListener,
        GamesProfile.OnFragmentInteractionListener,
        Gigs.OnFragmentInteractionListener,
        Profile2.OnFragmentInteractionListener,
        tutorial_on_profile.OnFragmentInteractionListener,
        tutorial_on_profile_others.OnFragmentInteractionListener,
        TaskManager1.OnFragmentInteractionListener,
        TodayTask.OnFragmentInteractionListener,
        gig_on_profile.OnFragmentInteractionListener,
        gig_on_profile_others.OnFragmentInteractionListener {

    private TabLayout tabLayout;
    ViewPager viewPager;

    DrawerLayout drawerLayout;
    LinearLayout drawerItemsLayout;
    ImageView plus, menu;
    String email, sent_from;

    private FirebaseAuth mAuth;
    FirebaseUser firebaseUser;

    SharedPreferences preferences;
    String got_email, fullname, pics;
    private View overlayView;



    public static final String USER_PROFILE = "https://handoutng.com/handouts/handout_get_my_notifications";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feeds_dashboard);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        preferences = getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        got_email = preferences.getString("email", "not available");
        fullname = preferences.getString("fullname", "not available");
        pics = preferences.getString("pics", "not available");

        Intent j = getIntent();
        email = j.getStringExtra("email");
        sent_from = j.getStringExtra("sent from");  //either "Login" or "Register"

        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();

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
        viewPager = findViewById(R.id.viewpager);
        addTabs(viewPager);
        tabLayout = findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);
        checkNotificationCount();
//        setupTabIcons();


        if (sent_from.equals("justCreatedResources") || sent_from.equals("checking gigs")){
            navigateFragment(4);
        }else if (sent_from.equals("task manager click")){
            navigateFragment(2);
        }else if (sent_from.equals("clear error")){
            navigateFragment(0);
        }
        else{
            //do nothing
        }

        tabLayout.setOnTabSelectedListener(
                new TabLayout.ViewPagerOnTabSelectedListener(viewPager) {

                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        if (tab.getPosition() == 1){
//                            navigateFragment(0);
                        }
                        else if(tab.getPosition() == 2){

                        }
                        else if(tab.getPosition() == 0){
                            Intent j = new Intent(getApplicationContext(), FeedsDashboard.class);
                            j.putExtra("email", got_email);
                            j.putExtra("sent from", "clear error");
                            startActivity(j);
                            finish();
                        }
                        else if(tab.getPosition() == 3){

                        }
                        else if(tab.getPosition() == 4){

                        }
                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {
//                        super.onTabReselected(tab);
                        if(tab.getPosition() == 0){
                            Intent j = new Intent(getApplicationContext(), FeedsDashboard.class);
                            j.putExtra("email", got_email);
                            j.putExtra("sent from", "clear error");
                            startActivity(j);
                            finish();
                        }
                    }
                }
        );

        //navigation drawer
        overlayView = findViewById(R.id.overlayView);
        drawerLayout = findViewById(R.id.drawer_layout);
        drawerItemsLayout = findViewById(R.id.drawer_items_layout);
        createDrawerItem();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.setDrawerIndicatorEnabled(true);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        overlayView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Close the drawer when the overlay is clicked
                drawerLayout.closeDrawers();
            }
        });
        drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerOpened(View drawerView) {
                overlayView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                overlayView.setVisibility(View.GONE);
            }
        });


        menu = findViewById(R.id.menu);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

    }

    private void checkNotificationCount() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, USER_PROFILE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try{
                            JSONObject profile = new JSONObject(response);
                            String sys_notification = profile.getString("sys_notification");
                            System.out.println("My Notification = "+sys_notification);
                            JSONArray arr = new JSONArray(sys_notification);

                            int arr_length = arr.length();
                            int unreadCount = 0;

                            for(int i =0; i<arr_length; i++){
                                JSONObject section1 = arr.getJSONObject(i);
                                String status = section1.getString("status");

                                if (status.equals("unread")){
                                    unreadCount = unreadCount + 1;
                                }
                            }

                            setupTabIcons(unreadCount);

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

        RequestQueue requestQueue = Volley.newRequestQueue(FeedsDashboard.this);
        DefaultRetryPolicy retryPolicy = new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);
        requestQueue.add(stringRequest);
        requestQueue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
            @Override
            public void onRequestFinished(Request<Object> request) {
                requestQueue.getCache().clear();
            }
        });
    }

    private void setupTabIcons(int unreadCount) {
//        checkNotificationCount();

        if (unreadCount == 0){
            tabLayout.getTabAt(0).setIcon(R.drawable.ic33);
            tabLayout.getTabAt(1).setIcon(R.drawable.ic31);
            tabLayout.getTabAt(2).setIcon(R.drawable.ic99);
            tabLayout.getTabAt(3).setIcon(R.drawable.bell);
            tabLayout.getTabAt(4).setIcon(R.drawable.ic32);
        }else{
            tabLayout.getTabAt(0).setIcon(R.drawable.ic33);
            tabLayout.getTabAt(1).setIcon(R.drawable.ic31);
            tabLayout.getTabAt(2).setIcon(R.drawable.ic99);
            tabLayout.getTabAt(3).setIcon(R.drawable.bell).getOrCreateBadge().setNumber(unreadCount);
            tabLayout.getTabAt(4).setIcon(R.drawable.ic32);
        }

    }

    private void addTabs(ViewPager viewPager) {
        FeedsDashboard.ViewPagerAdapter adapter = new FeedsDashboard.ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new FeedDashboardHome(), "Home");
        adapter.addFrag(new ChatPage2(), "Message");
        adapter.addFrag(new TaskManager1(), "Task Manager");
        adapter.addFrag(new FeedDashboardNotification(), "Notification");
        adapter.addFrag(new Profile2(), "Profile");
        viewPager.setAdapter(adapter);
    }

    public void navigateFragment(int position){
        viewPager.setCurrentItem(position);
    }

    private void createDrawerItem() {
        // Inflate the drawer item layout
        View drawerItem = getLayoutInflater().inflate(R.layout.drawer_item, drawerItemsLayout, false);

        // Set the icon and title
        LinearLayout viewPoints = drawerItem.findViewById(R.id.viewPoints);
        LinearLayout dashboard = drawerItem.findViewById(R.id.dashboard);
        LinearLayout remote = drawerItem.findViewById(R.id.remote_jobs);
        LinearLayout virtualLibrary = drawerItem.findViewById(R.id.virtual_library);
        LinearLayout createTutorial = drawerItem.findViewById(R.id.createTutorial);
        LinearLayout viewTutorial = drawerItem.findViewById(R.id.viewTutorial);
        LinearLayout createGig = drawerItem.findViewById(R.id.createGigs);
        LinearLayout viewGig = drawerItem.findViewById(R.id.viewGig);
        LinearLayout createGames = drawerItem.findViewById(R.id.createGame);
        LinearLayout viewGames = drawerItem.findViewById(R.id.viewGame);
        LinearLayout createTask = drawerItem.findViewById(R.id.createTask);
        LinearLayout viewTask = drawerItem.findViewById(R.id.viewTask);
        LinearLayout logout = drawerItem.findViewById(R.id.logout);
        LinearLayout profile = drawerItem.findViewById(R.id.profile);

        viewPoints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(FeedsDashboard.this, PointsOverview.class);
                startActivity(i);
            }
        });
        virtualLibrary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(FeedsDashboard.this, VirtualLibrary.class);
                i.putExtra("email", email);
                startActivity(i);
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawer(GravityCompat.START);
                navigateFragment(4);
            }
        });
        ImageView close = drawerItem.findViewById(R.id.close);
        CircleImageView pp = drawerItem.findViewById(R.id.userPP);
        Glide.with(FeedsDashboard.this).load(pics).into(pp);
        TextView txtName = drawerItem.findViewById(R.id.username);
        TextView txtEmail = drawerItem.findViewById(R.id.userEmail);
        txtEmail.setText(got_email);
        txtName.setText(fullname);
        createTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawer(GravityCompat.START);
                Intent i = new Intent(FeedsDashboard.this, CreateNewTask.class);
                i.putExtra("email", got_email);
                startActivity(i);
            }
        });
        createTutorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawer(GravityCompat.START);
                Intent i = new Intent(FeedsDashboard.this, CreateTutorialGroup2.class);
                i.putExtra("email", got_email);
                startActivity(i);
            }
        });
        createGig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawer(GravityCompat.START);
                Intent i = new Intent(FeedsDashboard.this, CreateGig1.class);
                startActivity(i);
            }
        });
        createGames.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawer(GravityCompat.START);
                Intent i = new Intent(FeedsDashboard.this, CreateGames.class);
                i.putExtra("email", got_email);
                startActivity(i);
            }
        });
        viewGames.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });
        viewTutorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawer(GravityCompat.START);
                navigateFragment(4);
            }
        });
        viewGig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawer(GravityCompat.START);
                navigateFragment(4);
            }
        });
        viewTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawer(GravityCompat.START);
                navigateFragment(2);
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });
        dashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });
        remote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =  new Intent( FeedsDashboard.this, RemotePage.class);
                i.putExtra("email", email);
                startActivity(i);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //popup dialog
                Dialog myDialog = new Dialog(FeedsDashboard.this);
                myDialog.setContentView(R.layout.custom_popup_logout);
                Button yes = myDialog.findViewById(R.id.yes);
                Button no = myDialog.findViewById(R.id.no);
                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SharedPreferences sharedPreferences = getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.clear();
                        editor.apply();

                        startActivity(new Intent(FeedsDashboard.this, LoginSignup.class));
                    }
                });
                no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        myDialog.dismiss();
                    }
                });
                myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                myDialog.setCanceledOnTouchOutside(false);
                myDialog.show();
            }
        });
        // Add the item to the drawer layout
        drawerItemsLayout.addView(drawerItem);
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onBackPressed() {
        // do nothing
    }


    public static class ViewPagerAdapter extends FragmentStatePagerAdapter {
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

    private void status(String status){
        DatabaseReference reference = FirebaseDatabase.getInstance("https://handout-lms-default-rtdb.firebaseio.com/").getReference("Users").child(firebaseUser.getUid());
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("status", status);

        reference.updateChildren(hashMap);
    }

    @Override
    public void onResume() {
        super.onResume();
        status("online");
    }

    @Override
    public void onPause() {
        super.onPause();
        status("offline");
    }


}
