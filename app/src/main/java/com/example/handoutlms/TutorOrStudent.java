package com.example.handoutlms;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

public class TutorOrStudent extends AppCompatActivity {
    CheckBox tutor, student;
    Button next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_or_student);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        tutor = findViewById(R.id.tutor);
        student = findViewById(R.id.student);
        next = findViewById(R.id.next);

        tutor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    student.setChecked(false);
                }
            }
        });
        student.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    tutor.setChecked(false);
                }
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(student.isChecked()){
                    //go to student dashboard
                    Intent i = new Intent(TutorOrStudent.this, StudentDashboard.class);
                    startActivity(i);
                }
                else if(tutor.isChecked()){
                    //go to tutor dashboard
                    Intent i = new Intent(TutorOrStudent.this, TutorSelectCategory.class);
                    startActivity(i);
                }
                else{
                    //prompt user to check a box
                    Toast.makeText(getApplicationContext(), "Please check a box", Toast.LENGTH_LONG).show();
                }

            }
        });
    }
}
