package com.example.handoutlms.uniabuja;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.handoutlms.R;
import com.example.handoutlms.activities.FeedsDashboard;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class Page4 extends AppCompatActivity {

    SharedPreferences preferences;
    String fullname, pics, got_email;
    ImageView back;
    TextView viewBoard;
    Button explore;

    CircleImageView pp;
    TextView username, score;
    ImageView score1, score2, score3, score4, score5, score6, score7, score8, score9, score10;

    Intent i;
    String level, faculty, department, uniname;
    ArrayList<String> answers = new ArrayList();
    int count_of_correct_answers = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page4);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        preferences = getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        fullname = preferences.getString("fullname", "");
        pics = preferences.getString("pics", "");
        got_email = preferences.getString("email", "not available");

        i = getIntent();
        level = i.getStringExtra("level");
        faculty = i.getStringExtra("faculty");
        department = i.getStringExtra("department");
        uniname = i.getStringExtra("uniname");
        answers = i.getStringArrayListExtra("selected_options");

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                onBackPressed();
            }
        });

        pp = findViewById(R.id.pp);
        Glide.with(Page4.this).load(pics).into(pp);
        username = findViewById(R.id.username);
        username.setText(fullname);

        for (int i = 0; i < answers.size(); i++) {
            if (answers.get(i).equals("correct")){
                count_of_correct_answers++;
            }
        }
        score = findViewById(R.id.scores);
        score.setText(count_of_correct_answers+"/10");

        score1 = findViewById(R.id.score1);
        score2 = findViewById(R.id.score2);
        score3 = findViewById(R.id.score3);
        score4 = findViewById(R.id.score4);
        score5 = findViewById(R.id.score5);
        score6 = findViewById(R.id.score6);
        score7 = findViewById(R.id.score7);
        score8 = findViewById(R.id.score8);
        score9 = findViewById(R.id.score9);
        score10 = findViewById(R.id.score10);

        if (answers.get(0).equals("correct")) {
            score1.setImageTintList(null);
            score1.setImageResource(R.drawable.correct);
        }
        if (answers.get(1).equals("correct")) {
            score2.setImageTintList(null);
            score2.setImageResource(R.drawable.correct);
        }
        if (answers.get(2).equals("correct")) {
            score3.setImageTintList(null);
            score3.setImageResource(R.drawable.correct);
        }
        if (answers.get(3).equals("correct")) {
            score4.setImageTintList(null);
            score4.setImageResource(R.drawable.correct);
        }
        if (answers.get(4).equals("correct")) {
            score5.setImageTintList(null);
            score5.setImageResource(R.drawable.correct);
        }
        if (answers.get(5).equals("correct")) {
            score6.setImageTintList(null);
            score6.setImageResource(R.drawable.correct);
        }if (answers.get(6).equals("correct")) {
            score7.setImageTintList(null);
            score7.setImageResource(R.drawable.correct);
        }
        if (answers.get(7).equals("correct")) {
            score8.setImageTintList(null);
            score8.setImageResource(R.drawable.correct);
        }
        if (answers.get(8).equals("correct")) {
            score9.setImageTintList(null);
            score9.setImageResource(R.drawable.correct);
        }
        if (answers.get(9).equals("correct")) {
            score10.setImageTintList(null);
            score10.setImageResource(R.drawable.correct);
        }

        viewBoard = findViewById(R.id.viewboard);
        viewBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Page4.this, Page5.class);
                startActivity(i);
            }
        });
        explore = findViewById(R.id.explore);
        explore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Page4.this, Page2.class);
                i.putExtra("level", level);
                i.putExtra("faculty", faculty);
                i.putExtra("department", department);
                i.putExtra("uniname", uniname);
                startActivity(i);
            }
        });

    }
}