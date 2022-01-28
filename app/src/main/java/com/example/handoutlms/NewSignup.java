package com.example.handoutlms;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class NewSignup extends AppCompatActivity {

    ImageView back;
    Spinner institution;
    LinearLayout login;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_signup);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), LoginSignup.class);
                startActivity(i);
            }
        });

        login = findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Login.class);
                startActivity(i);
            }
        });

        String[] institutions = {"Select your university", "","Ahmadu Bello University Zaria", "Adekunle Ajasin Unversity Akungba", "Adeleke University",
                "Babcock University", "Federal University of Tech. Minna", "Federal University of Tech. Akure", "University of Lagos", "University of Abuja", "Obafemi Awolowo University Ife"};
        ArrayAdapter<String> institutionadapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.simple_spinner_small_text, R.id.tx, institutions) ;

        institution = findViewById(R.id.spinnerinstitution);
        institution.setAdapter(institutionadapter);
        institution.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView)view).setTextColor(Color.WHITE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }
}
