package com.example.handoutlms;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

public class LoginSignup extends AppCompatActivity {
    TextView signup, login;
    TextView tos, pp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_signup);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        signup = findViewById(R.id.signup);
        login = findViewById(R.id.login);

        tos = findViewById(R.id.tos);
        pp = findViewById(R.id.pp);

        tos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), TermsOfService.class);
                i.putExtra("from", "tos");
                startActivity(i);
            }
        });

        pp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), TermsOfService.class);
                i.putExtra("from", "privacy");
                startActivity(i);
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginSignup.this, NewSignup.class);
                startActivity(i);
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginSignup.this, Login.class);
                startActivity(i);
            }
        });
    }

    @Override
    public void onBackPressed() {
        // do nothing
    }
}
