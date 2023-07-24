package com.example.handoutlms.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.handoutlms.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TriviaInstruction extends AppCompatActivity {

    TextView begin;
    String Text, From, ExamType;
    ImageView back;
    Dialog myDialog;

    public static final String QUESTION_URL = "https://handoutng.com/handouts/trivia/questions";
    public static final String NAIJA_QUESTION_URL = "https://handoutng.com/handouts/handout_questions";
    ArrayList<String> question_list = new ArrayList<String>();
    ArrayList<String> optionA_list = new ArrayList<String>();
    ArrayList<String> optionB_list = new ArrayList<String>();
    ArrayList<String> optionC_list = new ArrayList<String>();
    ArrayList<String> optionD_list = new ArrayList<String>();
    ArrayList<String> examType_list = new ArrayList<String>();
    ArrayList<String> examYear_list = new ArrayList<String>();
    ArrayList<String> correct_option_list = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trivia_instruction);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //get passed intent values
        Intent intent = getIntent();
        Text = intent.getStringExtra("Text");
        From = intent.getStringExtra("From");
        ExamType = intent.getStringExtra("ExamType");

        myDialog = new Dialog(TriviaInstruction.this);
        myDialog.setContentView(R.layout.custom_popup_question_loading);
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.setCanceledOnTouchOutside(false);
        myDialog.show();

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), HandoutTrivia.class);
                startActivity(i);
            }
        });

        begin = findViewById(R.id.begin);
        TextView cat = findViewById(R.id.category);
        ImageView imgicon = findViewById(R.id.imgicon);

        if(From.equals("normal")){
            if(Text.equals("Soccer")){
                cat.setText("Soccer");
                imgicon.setImageResource(R.drawable.triviasoccer);
            }
            if(Text.equals("Entertainment")){
                cat.setText("Entertainment");
                imgicon.setImageResource(R.drawable.triviaentertainment);
            }
            if(Text.equals("Politics")){
                cat.setText("Politics");
                imgicon.setImageResource(R.drawable.triviapolitics);
            }
            if(Text.equals("History")){
                cat.setText("History");
                imgicon.setImageResource(R.drawable.triviahistory);
            }
            if(Text.equals("Maths")){
                cat.setText("Maths");
                imgicon.setImageResource(R.drawable.triviamath);
            }
            if(Text.equals("English")){
                cat.setText("English");
                imgicon.setImageResource(R.drawable.triviaenglish);
            }
            if(Text.equals("Logic")){
                cat.setText("Logic");
                imgicon.setImageResource(R.drawable.trivialogic);
            }
            if(Text.equals("Physics")){
                cat.setText("Physics");
                imgicon.setImageResource(R.drawable.triviaphysics);
            }
            if(Text.equals("Computer")){
                cat.setText("Computer");
                imgicon.setImageResource(R.drawable.triviacomputer);
            }
            if(Text.equals("Movies")){
                cat.setText("Movies");
                imgicon.setImageResource(R.drawable.triviamovie);
            }
            if(Text.equals("Animals")){
                cat.setText("Animals");
                imgicon.setImageResource(R.drawable.triviaanimals);
            }
            if(Text.equals("Fashion")){
                cat.setText("Fashion");
                imgicon.setImageResource(R.drawable.triviafashion);
            }



            //send the text category to the server
            StringRequest stringRequest = new StringRequest(Request.Method.POST, QUESTION_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            System.out.println("Response = "+response);
                            //save the random questions in an array and respective answers
                            try {
                                JSONArray jsonArray = new JSONArray(response);

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    //saving questions, options and answers
                                    JSONObject section1 = jsonArray.getJSONObject(i);
                                    String question = section1.getString("question");
                                    String option1 = section1.getString("optionA");
                                    String option2 = section1.getString("optionB");
                                    String option3 = section1.getString("optionC");
                                    String option4 = section1.getString("optionD");
                                    String correct_option = section1.getString("correct_response");


                                    question_list.add(question);
                                    optionA_list.add(option1);
                                    optionB_list.add(option2);
                                    optionC_list.add(option3);
                                    optionD_list.add(option4);
                                    examType_list.add("");
                                    examYear_list.add("");
                                    correct_option_list.add(correct_option);
                                }

                                myDialog.dismiss();

                                begin.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent j = new Intent(TriviaInstruction.this, Question1.class);
                                        j.putStringArrayListExtra("question_list", question_list);
                                        j.putStringArrayListExtra("optionA_list", optionA_list);
                                        j.putStringArrayListExtra("optionB_list", optionB_list);
                                        j.putStringArrayListExtra("optionC_list", optionC_list);
                                        j.putStringArrayListExtra("optionD_list", optionD_list);
                                        j.putStringArrayListExtra("examType_list", examType_list);
                                        j.putStringArrayListExtra("examYear_list", examYear_list);
                                        j.putStringArrayListExtra("correct_option_list", correct_option_list);
                                        //pass category name also
                                        j.putExtra("category", Text);
                                        j.putExtra("From", "normal");
                                        startActivity(j);
                                    }
                                });


                            } catch (JSONException e) {
                                e.printStackTrace();
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    String stat = jsonObject.getString("status");
                                    if (stat.equals("No Questions Available")){
                                        myDialog.dismiss();

                                        Toast.makeText(getApplicationContext(), "No questions available in this category\nPlease check back later", Toast.LENGTH_LONG).show();
                                        Intent k = new Intent(TriviaInstruction.this, HandoutTrivia.class);
                                        startActivity(k);
                                    }else{
                                        myDialog.dismiss();

                                        Toast.makeText(getApplicationContext(), "No questions available in this category\nPlease check back later", Toast.LENGTH_LONG).show();
                                        Intent k = new Intent(TriviaInstruction.this, HandoutTrivia.class);
                                        startActivity(k);
                                    }
                                } catch (JSONException ex) {
                                    ex.printStackTrace();
                                }
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(TriviaInstruction.this, "Network Error!! Please try again", Toast.LENGTH_LONG).show();
//                        begin.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                //give a pop up error
//                                Toast.makeText(getApplicationContext(), "Network connection Error!!", Toast.LENGTH_LONG).show();
//                            }
//                        });
//                        System.out.println("Error not going "+error);
                        }
                    }){
                @Override
                protected Map<String, String> getParams(){
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("category", Text);
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(TriviaInstruction.this);
            requestQueue.add(stringRequest);



        }


        if(From.equals("9ja")){
            cat.setText(Text);
            imgicon.setImageResource(R.drawable.ic63);

            //send the text category to the server
            StringRequest stringRequest = new StringRequest(Request.Method.POST, NAIJA_QUESTION_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            System.out.println("Response = "+response);
                            //save the random questions in an array and respective answers
                            try {
                                JSONArray jsonArray = new JSONArray(response);

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    //saving questions, options and answers
                                    JSONObject section1 = jsonArray.getJSONObject(i);
                                    String question = section1.getString("question");
                                    String option1 = section1.getString("optionA");
                                    String option2 = section1.getString("optionB");
                                    String option3 = section1.getString("optionC");
                                    String option4 = section1.getString("optionD");
                                    String examType = section1.getString("examType");
                                    String examYear = section1.getString("examYear");
                                    String correct_option = section1.getString("correct_response");


                                    question_list.add(question);
                                    optionA_list.add(option1);
                                    optionB_list.add(option2);
                                    optionC_list.add(option3);
                                    optionD_list.add(option4);
                                    examType_list.add(examType);
                                    examYear_list.add(examYear);
                                    correct_option_list.add(correct_option);
                                }

                                myDialog.dismiss();

                                begin.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent j = new Intent(TriviaInstruction.this, Question1.class);
                                        j.putStringArrayListExtra("question_list", question_list);
                                        j.putStringArrayListExtra("optionA_list", optionA_list);
                                        j.putStringArrayListExtra("optionB_list", optionB_list);
                                        j.putStringArrayListExtra("optionC_list", optionC_list);
                                        j.putStringArrayListExtra("optionD_list", optionD_list);
                                        j.putStringArrayListExtra("examType_list", examType_list);
                                        j.putStringArrayListExtra("examYear_list", examYear_list);
                                        j.putStringArrayListExtra("correct_option_list", correct_option_list);
                                        //pass category name also
                                        j.putExtra("category", Text);
                                        j.putExtra("From", "9ja");
                                        startActivity(j);
                                    }
                                });


                            } catch (JSONException e) {
                                e.printStackTrace();
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    String stat = jsonObject.getString("status");
                                    if (stat.equals("No Questions Available")){
                                        myDialog.dismiss();

                                        Toast.makeText(getApplicationContext(), "No questions available in this category\nPlease check back later", Toast.LENGTH_LONG).show();
                                        Intent k = new Intent(TriviaInstruction.this, HandoutTrivia.class);
                                        startActivity(k);
//                                        begin.setOnClickListener(new View.OnClickListener() {
//                                            @Override
//                                            public void onClick(View view) {
//
//
//                                            }
//                                        });
                                    }else{
                                        myDialog.dismiss();

                                        Toast.makeText(getApplicationContext(), "No questions available in this category\nPlease check back later", Toast.LENGTH_LONG).show();
                                        Intent k = new Intent(TriviaInstruction.this, HandoutTrivia.class);
                                        startActivity(k);
                                    }
                                } catch (JSONException ex) {
                                    ex.printStackTrace();
                                }
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(TriviaInstruction.this, "Network Error!! Please try again", Toast.LENGTH_LONG).show();
//                        begin.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                //give a pop up error
//                                Toast.makeText(getApplicationContext(), "Network connection Error!!", Toast.LENGTH_LONG).show();
//                            }
//                        });
//                        System.out.println("Error not going "+error);
                        }
                    }){
                @Override
                protected Map<String, String> getParams(){
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("examtype", ExamType);
                    params.put("course", Text);
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(TriviaInstruction.this);
            requestQueue.add(stringRequest);
        }


    }
}
