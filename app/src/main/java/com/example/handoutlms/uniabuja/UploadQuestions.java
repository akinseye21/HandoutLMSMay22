package com.example.handoutlms.uniabuja;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.handoutlms.R;

public class UploadQuestions extends AppCompatActivity {

    ImageView back;
    //question
    LinearLayout linquestion;
    CardView cardMultipleChoice, cardEssay;
    //selection
    LinearLayout linselection;
    RadioButton radio100, radio200, radio300, radio400, radio500, radio600;
    //question
    LinearLayout linmultiplequestion, linessayquestion;
    //review
    LinearLayout linreview;
    //final
    LinearLayout linfinal;
    //button
    Button continu;

    //selections
    String questionType = "";
    String level = "", faculty = "", department = "", course = "";
    //for the counter
    ImageView img2, img21, img3, img31, img4, img41, img5, img51;
    TextView text2, text3, text4, text5;
    View view2, view3, view4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_questions);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        linquestion = findViewById(R.id.linquestion);
        cardMultipleChoice = findViewById(R.id.cardMultipleChoice);
        cardEssay = findViewById(R.id.cardEssay);
        continu = findViewById(R.id.continu);
        linselection = findViewById(R.id.linselection);
        radio100 = findViewById(R.id.radio100);
        radio200 = findViewById(R.id.radio200);
        radio300 = findViewById(R.id.radio300);
        radio400 = findViewById(R.id.radio400);
        radio500 = findViewById(R.id.radio500);
        radio600 = findViewById(R.id.radio600);
        linmultiplequestion = findViewById(R.id.linquestion_multiple);
        linessayquestion = findViewById(R.id.linquestion_essay);
        linreview = findViewById(R.id.linquestion_review);
        linfinal = findViewById(R.id.linquestion_final);

        //for the counter
        img2 = findViewById(R.id.img2);
        img21 = findViewById(R.id.img21);
        text2 = findViewById(R.id.text2);
        view2 = findViewById(R.id.view2);
        img3 = findViewById(R.id.img3);
        img31 = findViewById(R.id.img31);
        text3 = findViewById(R.id.text3);
        view3 = findViewById(R.id.view3);
        img4 = findViewById(R.id.img4);
        img41 = findViewById(R.id.img41);
        text4 = findViewById(R.id.text4);
        view4 = findViewById(R.id.view4);
        img5 = findViewById(R.id.img5);
        img51 = findViewById(R.id.img51);
        text5 = findViewById(R.id.text5);
        
        cardMultipleChoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cardMultipleChoice.setCardBackgroundColor(Color.parseColor("#2800ff00"));
                cardEssay.setCardBackgroundColor(Color.parseColor("#344863"));
                questionType = "multiple question";
            }
        });
        cardEssay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cardMultipleChoice.setCardBackgroundColor(Color.parseColor("#344863"));
                cardEssay.setCardBackgroundColor(Color.parseColor("#2800ff00"));
                questionType = "essay question";
            }
        });
        continu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!questionType.equals("")){
                    linquestion.setVisibility(View.GONE);
                    linselection.setVisibility(View.VISIBLE);
                    //increase the counter
                    img2.setVisibility(View.GONE);
                    img21.setImageResource(R.drawable.done);
                    text2.setTextColor(Color.parseColor("#ffffff"));
                    view2.setBackgroundColor(Color.parseColor("#42a5f6"));

                    viewForSelection();

                }else{
                    Toast.makeText(UploadQuestions.this, "Please select a question type", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void viewForSelection() {
        radio100.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radio100.setChecked(true);
                radio200.setChecked(false);
                radio300.setChecked(false);
                radio400.setChecked(false);
                radio500.setChecked(false);
                radio600.setChecked(false);
                level = "100 Level";
            }
        });
        radio200.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radio100.setChecked(false);
                radio200.setChecked(true);
                radio300.setChecked(false);
                radio400.setChecked(false);
                radio500.setChecked(false);
                radio600.setChecked(false);
                level = "200 Level";
            }
        });
        radio300.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radio100.setChecked(false);
                radio200.setChecked(false);
                radio300.setChecked(true);
                radio400.setChecked(false);
                radio500.setChecked(false);
                radio600.setChecked(false);
                level = "300 Level";
            }
        });
        radio400.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radio100.setChecked(false);
                radio200.setChecked(false);
                radio300.setChecked(false);
                radio400.setChecked(true);
                radio500.setChecked(false);
                radio600.setChecked(false);
                level = "400 Level";
            }
        });
        radio500.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radio100.setChecked(false);
                radio200.setChecked(false);
                radio300.setChecked(false);
                radio400.setChecked(false);
                radio500.setChecked(true);
                radio600.setChecked(false);
                level = "500 Level";
            }
        });
        radio600.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radio100.setChecked(false);
                radio200.setChecked(false);
                radio300.setChecked(false);
                radio400.setChecked(false);
                radio500.setChecked(false);
                radio600.setChecked(true);
                level = "600 Level";
            }
        });

        continu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linselection.setVisibility(View.GONE);
                img3.setVisibility(View.GONE);
                img31.setImageResource(R.drawable.done);
                text3.setTextColor(Color.parseColor("#ffffff"));
                view3.setBackgroundColor(Color.parseColor("#42a5f6"));

                if (questionType.equals("multiple question")){
                    linmultiplequestion.setVisibility(View.VISIBLE);
                    viewForMultipleQuestion();
                }else if (questionType.equals("essay question")){
                    linessayquestion.setVisibility(View.VISIBLE);
                    viewForEssayQuestion();
                }
            }
        });
    }

    private void viewForEssayQuestion() {
        continu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linessayquestion.setVisibility(View.GONE);
                linreview.setVisibility(View.VISIBLE);
                img4.setVisibility(View.GONE);
                img41.setImageResource(R.drawable.done);
                text4.setTextColor(Color.parseColor("#ffffff"));
                view4.setBackgroundColor(Color.parseColor("#42a5f6"));

                viewForFinal();
            }
        });
    }

    private void viewForMultipleQuestion() {
        continu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linmultiplequestion.setVisibility(View.GONE);
                linreview.setVisibility(View.VISIBLE);
                img4.setVisibility(View.GONE);
                img41.setImageResource(R.drawable.done);
                text4.setTextColor(Color.parseColor("#ffffff"));
                view4.setBackgroundColor(Color.parseColor("#42a5f6"));

                viewForFinal();
            }
        });
    }

    private void viewForFinal() {
        continu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                linreview.setVisibility(View.GONE);
                linfinal.setVisibility(View.VISIBLE);
                img5.setVisibility(View.GONE);
                img51.setImageResource(R.drawable.done);
                text5.setTextColor(Color.parseColor("#ffffff"));
                continu.setVisibility(View.GONE);

            }
        });
    }
}