package com.example.handoutlms.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.handoutlms.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class Question10 extends AppCompatActivity {

    ImageView caticon;
    TextView category;
    TextView quest;
    TextView opt1, opt2, opt3, opt4;
    TextView timeCount, runningout;
    String Text, timeCounting, From;
    TextView examType, examYear;

    //    int counter;
    int counters;

    CountDownTimer countDownTimer;
    long mil;

    String[] questio = new String[10];
    String[] score = new String[10];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question10);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //get passed intent values
        Intent intent = getIntent();
        From = intent.getStringExtra("From");
        final ArrayList<String> question_list = intent.getExtras().getStringArrayList("question_list");
        final ArrayList<String> optionA_list = intent.getStringArrayListExtra("optionA_list");
        final ArrayList<String> optionB_list = intent.getStringArrayListExtra("optionB_list");
        final ArrayList<String> optionC_list = intent.getStringArrayListExtra("optionC_list");
        final ArrayList<String> optionD_list = intent.getStringArrayListExtra("optionD_list");
        final ArrayList<String> examType_list = intent.getStringArrayListExtra("examType_list");
        final ArrayList<String> examYear_list = intent.getStringArrayListExtra("examYear_list");
        final ArrayList<String> correct_option_list = intent.getStringArrayListExtra("correct_option_list");
        Text = intent.getStringExtra("category");
        counters = intent.getIntExtra("counter", 0);
        timeCounting = intent.getStringExtra("timer");
        questio = intent.getStringArrayExtra("question");
        score = intent.getStringArrayExtra("score");

        runningout = findViewById(R.id.runningout);

        //convert the passed string time to milliseconds and continue countdown
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm:ss");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            Date date = simpleDateFormat.parse(timeCounting);
            mil = date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        timeCount = findViewById(R.id.timecount);
        examType = findViewById(R.id.examType);
        examYear = findViewById(R.id.examYear);
        countDownTimer = new CountDownTimer(mil, 1000) {
            @Override
            public void onTick(long l) {
                long millis = l;
                String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis), TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)), TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
                if (hms.equals("00:01:00")){
                    timeCount.setTextColor(Color.RED);
                    runningout.setVisibility(View.VISIBLE);
                    runningout.setTextColor(Color.RED);
                }
                timeCount.setText(hms);
            }

            @Override
            public void onFinish() {
                timeCount.setText("TIME UP!!");
            }
        }.start();




        category = findViewById(R.id.categori);
        category.setText(Text);

        caticon = findViewById(R.id.categoryicon);
        //set the imgicon
        if(From.equals("normal")){
            if(Text.equals("Soccer")){
                caticon.setImageResource(R.drawable.triviasoccer);
            }
            if(Text.equals("Entertainment")){
                caticon.setImageResource(R.drawable.triviaentertainment);
            }
            if(Text.equals("Politics")){
                caticon.setImageResource(R.drawable.triviapolitics);
            }
            if(Text.equals("History")){
                caticon.setImageResource(R.drawable.triviahistory);
            }
            if(Text.equals("Maths")){
                caticon.setImageResource(R.drawable.triviamath);
            }
            if(Text.equals("English")){
                caticon.setImageResource(R.drawable.triviaenglish);
            }
            if(Text.equals("Logic")){
                caticon.setImageResource(R.drawable.trivialogic);
            }
            if(Text.equals("Physics")){
                caticon.setImageResource(R.drawable.triviaphysics);
            }
            if(Text.equals("Computer")){
                caticon.setImageResource(R.drawable.triviacomputer);
            }
            if(Text.equals("Movies")){
                caticon.setImageResource(R.drawable.triviamovie);
            }
            if(Text.equals("Animals")){
                caticon.setImageResource(R.drawable.triviaanimals);
            }
            if(Text.equals("Fashion")){
                caticon.setImageResource(R.drawable.triviafashion);
            }
        }
        else{
            caticon.setImageResource(R.drawable.ic63);
        }

        quest = findViewById(R.id.question);
        opt1 = findViewById(R.id.option1);
        opt2 = findViewById(R.id.option2);
        opt3 = findViewById(R.id.option3);
        opt4 = findViewById(R.id.option4);


        quest.setText(question_list.get(9));
        opt1.setText(optionA_list.get(9));
        opt2.setText(optionB_list.get(9));
        opt3.setText(optionC_list.get(9));
        opt4.setText(optionD_list.get(9));
        examType.setText(examType_list.get(9));
        examYear.setText(examYear_list.get(9));

        final String ans = correct_option_list.get(9);


        //check for option1
        opt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ans.equals(opt1.getText().toString())){
                    opt1.setBackgroundColor(Color.GREEN);
                    counters = counters + 1;
                    questio[9] = "question_10";
                    score[9] = "pass";

                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            // this code will be executed after 2 seconds
                            //check counter
                            if(counters >= 7){
                                //show qualified page
                                Intent mv = new Intent(Question10.this, TriviaPassed.class);
                                mv.putExtra("counter", counters);
                                mv.putExtra("category", Text);
                                mv.putExtra("question", questio);
                                mv.putExtra("score", score);
                                mv.putExtra("From", From);
                                mv.putExtra("timer", timeCount.getText().toString());
                                startActivity(mv);
                            }else{
                                Intent mv = new Intent(Question10.this, TriviaFail.class);
                                mv.putExtra("counter", counters);
                                mv.putExtra("category", Text);
                                mv.putExtra("question", questio);
                                mv.putExtra("score", score);
                                mv.putExtra("From", From);
                                mv.putExtra("timer", timeCount.getText().toString());
                                startActivity(mv);
                            }
                        }
                    }, 2000);

                }
                else{
                    opt1.setBackgroundColor(Color.RED);
                    if(ans.equals(opt2.getText().toString())) {
                        opt2.setBackgroundColor(Color.GREEN);
                    }
                    if(ans.equals(opt3.getText().toString())) {
                        opt3.setBackgroundColor(Color.GREEN);
                    }
                    if(ans.equals(opt4.getText().toString())) {
                        opt4.setBackgroundColor(Color.GREEN);
                    }
                    counters = counters + 0;
                    questio[9] = "question_10";
                    score[9] = "fail";

                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            // this code will be executed after 2 seconds
                            //check counter
                            if(counters >= 7){
                                //show qualified page
                                Intent mv = new Intent(Question10.this, TriviaPassed.class);
                                mv.putExtra("counter", counters);
                                mv.putExtra("category", Text);
                                mv.putExtra("question", questio);
                                mv.putExtra("score", score);
                                mv.putExtra("From", From);
                                mv.putExtra("timer", timeCount.getText().toString());
                                startActivity(mv);
                            }else{
                                Intent mv = new Intent(Question10.this, TriviaFail.class);
                                mv.putExtra("counter", counters);
                                mv.putExtra("category", Text);
                                mv.putExtra("question", questio);
                                mv.putExtra("score", score);
                                mv.putExtra("From", From);
                                mv.putExtra("timer", timeCount.getText().toString());
                                startActivity(mv);
                            }
                        }
                    }, 2000);
                }

            }
        });


        //check for option2
        opt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ans.equals(opt2.getText().toString())){
                    opt2.setBackgroundColor(Color.GREEN);
                    counters = counters + 1;
                    questio[9] = "question_10";
                    score[9] = "pass";

                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            // this code will be executed after 2 seconds
                            //check counter
                            if(counters >= 7){
                                //show qualified page
                                Intent mv = new Intent(Question10.this, TriviaPassed.class);
                                mv.putExtra("counter", counters);
                                mv.putExtra("category", Text);
                                mv.putExtra("question", questio);
                                mv.putExtra("score", score);
                                mv.putExtra("From", From);
                                mv.putExtra("timer", timeCount.getText().toString());
                                startActivity(mv);
                            }else{
                                Intent mv = new Intent(Question10.this, TriviaFail.class);
                                mv.putExtra("counter", counters);
                                mv.putExtra("category", Text);
                                mv.putExtra("question", questio);
                                mv.putExtra("score", score);
                                mv.putExtra("From", From);
                                mv.putExtra("timer", timeCount.getText().toString());
                                startActivity(mv);
                            }
                        }
                    }, 2000);
                }
                else{
                    opt2.setBackgroundColor(Color.RED);
                    if(ans.equals(opt1.getText().toString())) {
                        opt1.setBackgroundColor(Color.GREEN);
                    }
                    if(ans.equals(opt3.getText().toString())) {
                        opt3.setBackgroundColor(Color.GREEN);
                    }
                    if(ans.equals(opt4.getText().toString())) {
                        opt4.setBackgroundColor(Color.GREEN);
                    }
                    counters = counters + 0;
                    questio[9] = "question_10";
                    score[9] = "fail";

                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            // this code will be executed after 2 seconds
                            //check counter
                            if(counters >= 7){
                                //show qualified page
                                Intent mv = new Intent(Question10.this, TriviaPassed.class);
                                mv.putExtra("counter", counters);
                                mv.putExtra("category", Text);
                                mv.putExtra("question", questio);
                                mv.putExtra("score", score);
                                mv.putExtra("From", From);
                                mv.putExtra("timer", timeCount.getText().toString());
                                startActivity(mv);
                            }else{
                                Intent mv = new Intent(Question10.this, TriviaFail.class);
                                mv.putExtra("counter", counters);
                                mv.putExtra("category", Text);
                                mv.putExtra("question", questio);
                                mv.putExtra("score", score);
                                mv.putExtra("From", From);
                                mv.putExtra("timer", timeCount.getText().toString());
                                startActivity(mv);
                            }
                        }
                    }, 2000);
                }

            }
        });


        //check for option3
        opt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ans.equals(opt3.getText().toString())){
                    opt3.setBackgroundColor(Color.GREEN);
                    counters = counters + 1;
                    questio[9] = "question_10";
                    score[9] = "pass";

                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            // this code will be executed after 2 seconds
                            //check counter
                            if(counters >= 7){
                                //show qualified page
                                Intent mv = new Intent(Question10.this, TriviaPassed.class);
                                mv.putExtra("counter", counters);
                                mv.putExtra("category", Text);
                                mv.putExtra("question", questio);
                                mv.putExtra("score", score);
                                mv.putExtra("From", From);
                                mv.putExtra("timer", timeCount.getText().toString());
                                startActivity(mv);
                            }else{
                                Intent mv = new Intent(Question10.this, TriviaFail.class);
                                mv.putExtra("counter", counters);
                                mv.putExtra("category", Text);
                                mv.putExtra("question", questio);
                                mv.putExtra("score", score);
                                mv.putExtra("From", From);
                                mv.putExtra("timer", timeCount.getText().toString());
                                startActivity(mv);
                            }
                        }
                    }, 2000);
                }
                else{
                    opt3.setBackgroundColor(Color.RED);
                    if(ans.equals(opt2.getText().toString())) {
                        opt2.setBackgroundColor(Color.GREEN);
                    }
                    if(ans.equals(opt1.getText().toString())) {
                        opt1.setBackgroundColor(Color.GREEN);
                    }
                    if(ans.equals(opt4.getText().toString())) {
                        opt4.setBackgroundColor(Color.GREEN);
                    }
                    counters = counters + 0;
                    questio[9] = "question_10";
                    score[9] = "fail";

                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            // this code will be executed after 2 seconds
                            //check counter
                            if(counters >= 7){
                                //show qualified page
                                Intent mv = new Intent(Question10.this, TriviaPassed.class);
                                mv.putExtra("counter", counters);
                                mv.putExtra("category", Text);
                                mv.putExtra("question", questio);
                                mv.putExtra("score", score);
                                mv.putExtra("From", From);
                                mv.putExtra("timer", timeCount.getText().toString());
                                startActivity(mv);
                            }else{
                                Intent mv = new Intent(Question10.this, TriviaFail.class);
                                mv.putExtra("counter", counters);
                                mv.putExtra("category", Text);
                                mv.putExtra("question", questio);
                                mv.putExtra("score", score);
                                mv.putExtra("From", From);
                                mv.putExtra("timer", timeCount.getText().toString());
                                startActivity(mv);
                            }
                        }
                    }, 2000);
                }

            }
        });


        //check for option4
        opt4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ans.equals(opt4.getText().toString())){
                    opt4.setBackgroundColor(Color.GREEN);
                    counters = counters + 1;
                    questio[9] = "question_10";
                    score[9] = "pass";

                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            // this code will be executed after 2 seconds
                            //check counter
                            if(counters >= 7){
                                //show qualified page
                                Intent mv = new Intent(Question10.this, TriviaPassed.class);
                                mv.putExtra("counter", counters);
                                mv.putExtra("category", Text);
                                mv.putExtra("question", questio);
                                mv.putExtra("score", score);
                                mv.putExtra("From", From);
                                mv.putExtra("timer", timeCount.getText().toString());
                                startActivity(mv);
                            }else{
                                Intent mv = new Intent(Question10.this, TriviaFail.class);
                                mv.putExtra("counter", counters);
                                mv.putExtra("category", Text);
                                mv.putExtra("question", questio);
                                mv.putExtra("score", score);
                                mv.putExtra("From", From);
                                mv.putExtra("timer", timeCount.getText().toString());
                                startActivity(mv);
                            }
                        }
                    }, 2000);
                }
                else{
                    opt4.setBackgroundColor(Color.RED);
                    if(ans.equals(opt2.getText().toString())) {
                        opt2.setBackgroundColor(Color.GREEN);
                    }
                    if(ans.equals(opt3.getText().toString())) {
                        opt3.setBackgroundColor(Color.GREEN);
                    }
                    if(ans.equals(opt1.getText().toString())) {
                        opt1.setBackgroundColor(Color.GREEN);
                    }
                    counters = counters + 0;
                    questio[9] = "question_10";
                    score[9] = "fail";

                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            // this code will be executed after 2 seconds
                            //check counter
                            if(counters >= 7){
                                //show qualified page
                                Intent mv = new Intent(Question10.this, TriviaPassed.class);
                                mv.putExtra("counter", counters);
                                mv.putExtra("category", Text);
                                mv.putExtra("question", questio);
                                mv.putExtra("score", score);
                                mv.putExtra("From", From);
                                mv.putExtra("timer", timeCount.getText().toString());
                                startActivity(mv);
                            }else{
                                Intent mv = new Intent(Question10.this, TriviaFail.class);
                                mv.putExtra("counter", counters);
                                mv.putExtra("category", Text);
                                mv.putExtra("question", questio);
                                mv.putExtra("score", score);
                                mv.putExtra("From", From);
                                mv.putExtra("timer", timeCount.getText().toString());
                                startActivity(mv);
                            }
                        }
                    }, 2000);
                }

            }
        });
    }

    @Override
    public void onBackPressed(){
        //do nothing
    }
}
