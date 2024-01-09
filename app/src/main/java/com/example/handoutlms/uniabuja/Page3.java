package com.example.handoutlms.uniabuja;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.handoutlms.R;
import com.example.handoutlms.adapters.UniAbujaAdapter;
import com.google.android.material.slider.Slider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Page3 extends AppCompatActivity {

    ImageView back;
    Button next;
    TextView counter, questionnumber, question;
    TextView option1, option2, option3, option4, option5;
    Slider slider;

    Intent i;
    String level, faculty, department, course, uniname;
    ArrayList<String> arr_question = new ArrayList();
    ArrayList<String> arr_option1 = new ArrayList();
    ArrayList<String> arr_option2 = new ArrayList();
    ArrayList<String> arr_option3 = new ArrayList();
    ArrayList<String> arr_option4 = new ArrayList();
    ArrayList<String> arr_option5 = new ArrayList();
    ArrayList<String> arr_correct = new ArrayList();
    ArrayList<String> arr_year = new ArrayList();
    int index = 1;
    ArrayList<String> arr_selected_answers = new ArrayList();
    String selected_option = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page3);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        i = getIntent();
        level = i.getStringExtra("level");
        faculty = i.getStringExtra("faculty");
        department = i.getStringExtra("department");
        course = i.getStringExtra("course");
        uniname = i.getStringExtra("uniname");
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog myDialog = new Dialog(Page3.this);
                myDialog.setContentView(R.layout.custom_popup_exit_chat);
                myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                TextView text = myDialog.findViewById(R.id.text);
                text.setText("If you go back, game progress will be cancelled");
                Button yes = myDialog.findViewById(R.id.yes);
                Button no = myDialog.findViewById(R.id.no);
                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //quit
                        onBackPressed();
                    }
                });
                no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        myDialog.dismiss();
                    }
                });
                myDialog.setCanceledOnTouchOutside(false);
                myDialog.show();
            }
        });
        next = findViewById(R.id.next);
        counter = findViewById(R.id.counter);
        questionnumber = findViewById(R.id.questionnumber);
        question = findViewById(R.id.question);
        option1 = findViewById(R.id.option1);
        option2 = findViewById(R.id.option2);
        option3 = findViewById(R.id.option3);
        option4 = findViewById(R.id.option4);
        option5 = findViewById(R.id.option5);
        slider = findViewById(R.id.counterSlider);
        slider.setOnTouchListener((v, event) -> true);

        getQuestions();

        option1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selected_option = "a";
                option1.setBackgroundResource(R.drawable.rounded_selected);
                option2.setBackgroundResource(R.drawable.rounded_white_transparent_100);
                option3.setBackgroundResource(R.drawable.rounded_white_transparent_100);
                option4.setBackgroundResource(R.drawable.rounded_white_transparent_100);
                option5.setBackgroundResource(R.drawable.rounded_white_transparent_100);
            }
        });
        option2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selected_option = "b";
                option2.setBackgroundResource(R.drawable.rounded_selected);
                option1.setBackgroundResource(R.drawable.rounded_white_transparent_100);
                option3.setBackgroundResource(R.drawable.rounded_white_transparent_100);
                option4.setBackgroundResource(R.drawable.rounded_white_transparent_100);
                option5.setBackgroundResource(R.drawable.rounded_white_transparent_100);
            }
        });
        option3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selected_option = "c";
                option3.setBackgroundResource(R.drawable.rounded_selected);
                option2.setBackgroundResource(R.drawable.rounded_white_transparent_100);
                option1.setBackgroundResource(R.drawable.rounded_white_transparent_100);
                option4.setBackgroundResource(R.drawable.rounded_white_transparent_100);
                option5.setBackgroundResource(R.drawable.rounded_white_transparent_100);
            }
        });
        option4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selected_option = "d";
                option4.setBackgroundResource(R.drawable.rounded_selected);
                option2.setBackgroundResource(R.drawable.rounded_white_transparent_100);
                option3.setBackgroundResource(R.drawable.rounded_white_transparent_100);
                option1.setBackgroundResource(R.drawable.rounded_white_transparent_100);
                option5.setBackgroundResource(R.drawable.rounded_white_transparent_100);
            }
        });
        option5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selected_option = "e";
                option5.setBackgroundResource(R.drawable.rounded_selected);
                option2.setBackgroundResource(R.drawable.rounded_white_transparent_100);
                option3.setBackgroundResource(R.drawable.rounded_white_transparent_100);
                option4.setBackgroundResource(R.drawable.rounded_white_transparent_100);
                option1.setBackgroundResource(R.drawable.rounded_white_transparent_100);
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (selected_option.equals("")){
                    Toast.makeText(Page3.this, "Please select an option", Toast.LENGTH_SHORT).show();
                }else{

                    if (arr_correct.get(index - 1).equals(selected_option)){
                        arr_selected_answers.add("correct");
                        selected_option = "";
                    }
                    else{
                        arr_selected_answers.add("wrong");
                        selected_option = "";
                    }

                    option1.setBackgroundResource(R.drawable.rounded_white_transparent_100);
                    option2.setBackgroundResource(R.drawable.rounded_white_transparent_100);
                    option3.setBackgroundResource(R.drawable.rounded_white_transparent_100);
                    option4.setBackgroundResource(R.drawable.rounded_white_transparent_100);
                    option5.setBackgroundResource(R.drawable.rounded_white_transparent_100);

                    index++;
                    if (index <= 10){
                        //increase slider
                        //change the counter number, the question number and the options
                        slider.setValue(Float.parseFloat(String.valueOf(index*10)));
                        counter.setText(index+"/10");
                        questionnumber.setText(String.valueOf(index));
                        question.setText(arr_question.get(index - 1));
                        option1.setText(arr_option1.get(index-1));
                        option2.setText(arr_option2.get(index-1));
                        option3.setText(arr_option3.get(index-1));
                        option4.setText(arr_option4.get(index-1));
                        option5.setText(arr_option5.get(index-1));
                    }else {
                        Intent i = new Intent(Page3.this, Page4.class);
                        i.putStringArrayListExtra("selected_options", arr_selected_answers);
                        i.putExtra("level", level);
                        i.putExtra("faculty", faculty);
                        i.putExtra("department", department);
                        i.putExtra("uniname", uniname);
                        startActivity(i);
                    }

                    System.out.println("Answers = "+arr_selected_answers);
                }

            }
        });

    }

    private void getQuestions() {
        Dialog myDialog = new Dialog(Page3.this);
        myDialog.setContentView(R.layout.custom_popup_login_loading);
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView text = myDialog.findViewById(R.id.text);
        text.setText("Fetching courses, please wait...");
        myDialog.setCanceledOnTouchOutside(false);
        myDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://handoutng.com/handouts/handout_get_questions",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try{
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String question = jsonObject.getString("question");
                                String optionA = jsonObject.getString("optionA");
                                String optionB = jsonObject.getString("optionB");
                                String optionC = jsonObject.getString("optionC");
                                String optionD = jsonObject.getString("optionD");
                                String optionE = jsonObject.getString("optionE");
                                String correct = jsonObject.getString("correct_option");
                                String year = jsonObject.getString("examYear");

                                arr_question.add(question);
                                arr_option1.add(optionA);
                                arr_option2.add(optionB);
                                arr_option3.add(optionC);
                                arr_option4.add(optionD);
                                arr_option5.add(optionE);
                                arr_correct.add(correct);
                                arr_year.add(year);
                            }

                            myDialog.dismiss();
                            counter.setText("1/10");
                            questionnumber.setText("1");
                            question.setText(arr_question.get(index-1));
                            option1.setText(arr_option1.get(index-1));
                            option2.setText(arr_option2.get(index-1));
                            option3.setText(arr_option3.get(index-1));
                            option4.setText(arr_option4.get(index-1));
                            option5.setText(arr_option5.get(index-1));
                        }
                        catch (JSONException e){
                            e.printStackTrace();
                            myDialog.dismiss();
                            Toast.makeText(Page3.this, "Problem fetching questions", Toast.LENGTH_SHORT).show();
                            onBackPressed();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(Page3.this, "Problem fetching questions", Toast.LENGTH_SHORT).show();
                        myDialog.dismiss();
                        onBackPressed();
                    }
                }){
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("university", "University of Abuja");
                params.put("level", level);
                params.put("faculty", faculty);
                params.put("course", course);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
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
}