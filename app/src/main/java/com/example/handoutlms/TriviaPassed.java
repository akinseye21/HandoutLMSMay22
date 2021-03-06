package com.example.handoutlms;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class TriviaPassed extends AppCompatActivity {

    TextView point, timer, cate, name;
    ImageView caticon;
    LinearLayout playAgain, mainMenu;
    int counters;
    String timeCounting, category;

    String[] questio = new String[10];
    String[] score = new String[10];

    public static final String RESULT_URL = "http://54.71.22.155/hitma/trivia/results";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trivia_passed);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        timer = findViewById(R.id.time);
        cate = findViewById(R.id.cat);
        point = findViewById(R.id.points);
        playAgain = findViewById(R.id.playagain2);
        mainMenu = findViewById(R.id.mainmenu);
        caticon = findViewById(R.id.catico);
        name = findViewById(R.id.username);

        Intent intent = getIntent();
        counters = intent.getIntExtra("counter", 0);
        timeCounting = intent.getStringExtra("timer");
        category = intent.getStringExtra("category");
        questio = intent.getStringArrayExtra("question");
        score = intent.getStringArrayExtra("score");

        //get sharedpreference
        SharedPreferences sp = getSharedPreferences("LoginDetails", MODE_PRIVATE);
        final String email = sp.getString("email", "");

        name.setText(email);

        cate.setText(category);
        point.setText(String.valueOf(counters));
        timer.setText(timeCounting);

        //set the imgicon
        if(category.equals("Soccer")){
            caticon.setImageResource(R.drawable.triviasoccer);
        }
        if(category.equals("Entertainment")){
            caticon.setImageResource(R.drawable.triviaentertainment);
        }
        if(category.equals("Politics")){
            caticon.setImageResource(R.drawable.triviapolitics);
        }
        if(category.equals("History")){
            caticon.setImageResource(R.drawable.triviahistory);
        }
        if(category.equals("Maths")){
            caticon.setImageResource(R.drawable.triviamath);
        }
        if(category.equals("English")){
            caticon.setImageResource(R.drawable.triviaenglish);
        }
        if(category.equals("Logic")){
            caticon.setImageResource(R.drawable.trivialogic);
        }
        if(category.equals("Physics")){
            caticon.setImageResource(R.drawable.triviaphysics);
        }
        if(category.equals("Computer")){
            caticon.setImageResource(R.drawable.triviacomputer);
        }
        if(category.equals("Movies")){
            caticon.setImageResource(R.drawable.triviamovie);
        }
        if(category.equals("Animals")){
            caticon.setImageResource(R.drawable.triviaanimals);
        }
        if(category.equals("Fashion")){
            caticon.setImageResource(R.drawable.triviafashion);
        }

        if(timeCounting.equals("TIME UP!!")){

            Toast toast= Toast.makeText(getApplicationContext(),
                    "Time Elapsed!!!, you scored "+counters+" points. Scores will not be saved", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();
            timer.setTextColor(Color.RED);

        }else {
            //send info to database
            StringRequest stringRequest = new StringRequest(Request.Method.POST, RESULT_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {


                            System.out.println("Server response = "+response);
                            System.out.println("Sending = "+score[0]
                                    +"\n"+score[1]
                                    +"\n"+score[2]
                                    +"\n"+score[3]
                                    +"\n"+score[4]
                                    +"\n"+score[5]
                                    +"\n"+score[6]
                                    +"\n"+score[7]
                                    +"\n"+score[8]
                                    +"\n"+score[9]);

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            System.out.println("Server response = "+error);
                        }
                    }){
                @Override
                protected Map<String, String> getParams(){
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("email", email);
                    params.put("category", category);
                    params.put("question_1", score[0]);
                    params.put("question_2", score[1]);
                    params.put("question_3", score[2]);
                    params.put("question_4", score[3]);
                    params.put("question_5", score[4]);
                    params.put("question_6", score[5]);
                    params.put("question_7", score[6]);
                    params.put("question_8", score[7]);
                    params.put("question_9", score[8]);
                    params.put("question_10", score[9]);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(TriviaPassed.this);
            requestQueue.add(stringRequest);
        }




        playAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(TriviaPassed.this, HandoutTrivia.class);
                startActivity(i);
            }
        });
        mainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent j = new Intent(TriviaPassed.this, FeedsDashboard.class);
                startActivity(j);
            }
        });
    }

    @Override
    public void onBackPressed(){
        //do nothing
    }
}
