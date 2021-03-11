package com.example.handoutlms;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

public class TutorsDashboard extends AppCompatActivity implements
        TutorHome.OnFragmentInteractionListener,
        TutorMessages.OnFragmentInteractionListener,
        TutorProfile.OnFragmentInteractionListener,
        TutorSettings.OnFragmentInteractionListener,
        CreateTutorialGroup.OnFragmentInteractionListener,
        InviteStudents.OnFragmentInteractionListener,
        TutorialGroupPage.OnFragmentInteractionListener,
        TutorialGroupPage2.OnFragmentInteractionListener,
        CreateCourses.OnFragmentInteractionListener,
        CourseFilePage.OnFragmentInteractionListener,
        DocumentPage.OnFragmentInteractionListener,
        CreateQuiz.OnFragmentInteractionListener,
        ViewTutorialGroups.OnFragmentInteractionListener,
        ViewCourses.OnFragmentInteractionListener,
        BottomNavigationView.OnNavigationItemSelectedListener {

    BottomAppBar bottomAppBar;
    BottomNavigationView bottomNavigationView;
    FloatingActionButton floatingActionButton;
    GenerateDialog generateDialog;
    LinearLayout create_new_tut_group, create_new_course, create_quiz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutors_dashboard);
//        setTheme(R.style.AppTheme);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        loadFragment(new TutorHome());

        bottomAppBar = findViewById(R.id.bottomAppBar);
        bottomNavigationView = findViewById(R.id.bottomNavView);
//        bottomAppBar.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        bottomNavigationView.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        bottomNavigationView.setOnNavigationItemSelectedListener(TutorsDashboard.this);

        generateDialog = new GenerateDialog(this);
        floatingActionButton = findViewById(R.id.fab);
//
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                create_new_tut_group = generateDialog.findViewById(R.id.lincreatenewtutgroup);
                create_new_course = generateDialog.findViewById(R.id.lincreatenewcourse);
                create_quiz = generateDialog.findViewById(R.id.lincreatequiz);

                create_new_tut_group.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //what to do?
                        loadFragment(new CreateTutorialGroup());
                        generateDialog.dismiss();
                    }
                });

                create_new_course.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //what to do
                        loadFragment(new CreateCourses());
                        generateDialog.dismiss();
                    }
                });

                create_quiz.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //what ti do
                        loadFragment(new CreateQuiz());
                        generateDialog.dismiss();
                    }
                });

                generateDialog.show();
            }
        });
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        Fragment fragment = null;

        switch (item.getItemId()) {
            case R.id.miHome:
                fragment = new TutorHome();
                break;

            case R.id.miMessage:
                fragment = new TutorMessages();
                break;

            case R.id.miProfile:
                fragment = new TutorProfile();
                break;

            case R.id.miSettings:
                fragment = new TutorSettings();
                break;
        }

        return loadFragment(fragment);
    }
}
