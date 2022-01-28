package com.example.handoutlms;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class HandoutTrivia extends AppCompatActivity {

    ImageView back;

    LinearLayout soccer, entertainment, politics, history;
    LinearLayout maths, english, logic, physics;
    LinearLayout computer, movies, animals, fashion;

    String text;
    TextView correctAnswer, ranking;

    public static final String RANK_URL = "http://54.71.22.155/hitma/trivia/user_rank";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handout_trivia);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), FeedsDashboard.class);
                startActivity(i);
            }
        });

        TextView username = findViewById(R.id.username);
        correctAnswer = findViewById(R.id.numCorrectAns);
        ranking = findViewById(R.id.ranking);

        soccer = findViewById(R.id.soccer);
        entertainment = findViewById(R.id.entertainment);
        politics = findViewById(R.id.politics);
        history = findViewById(R.id.history);
        maths = findViewById(R.id.math);
        english = findViewById(R.id.english);
        logic = findViewById(R.id.logic);
        physics = findViewById(R.id.physics);
        computer = findViewById(R.id.computer);
        movies = findViewById(R.id.movies);
        animals = findViewById(R.id.animals);
        fashion = findViewById(R.id.fashion);

        soccer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                text = "Soccer";
                Intent mv = new Intent(HandoutTrivia.this, TriviaInstruction.class);
                mv.putExtra("Text", text);
                startActivity(mv);
            }
        });
        entertainment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                text = "Entertainment";
                Intent mv = new Intent(HandoutTrivia.this, TriviaInstruction.class);
                mv.putExtra("Text", text);
                startActivity(mv);
            }
        });
        politics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                text = "Politics";
                Intent mv = new Intent(HandoutTrivia.this, TriviaInstruction.class);
                mv.putExtra("Text", text);
                startActivity(mv);
            }
        });
        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                text = "History";
                Intent mv = new Intent(HandoutTrivia.this, TriviaInstruction.class);
                mv.putExtra("Text", text);
                startActivity(mv);
            }
        });
        maths.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                text = "Maths";
                Intent mv = new Intent(HandoutTrivia.this, TriviaInstruction.class);
                mv.putExtra("Text", text);
                startActivity(mv);
            }
        });
        english.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                text = "English";
                Intent mv = new Intent(HandoutTrivia.this, TriviaInstruction.class);
                mv.putExtra("Text", text);
                startActivity(mv);
            }
        });
        logic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                text = "Logic";
                Intent mv = new Intent(HandoutTrivia.this, TriviaInstruction.class);
                mv.putExtra("Text", text);
                startActivity(mv);
            }
        });
        physics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                text = "Physics";
                Intent mv = new Intent(HandoutTrivia.this, TriviaInstruction.class);
                mv.putExtra("Text", text);
                startActivity(mv);
            }
        });
        computer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                text = "Computer";
                Intent mv = new Intent(HandoutTrivia.this, TriviaInstruction.class);
                mv.putExtra("Text", text);
                startActivity(mv);
            }
        });
        movies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                text = "Movies";
                Intent mv = new Intent(HandoutTrivia.this, TriviaInstruction.class);
                mv.putExtra("Text", text);
                startActivity(mv);
            }
        });
        animals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                text = "Animals";
                Intent mv = new Intent(HandoutTrivia.this, TriviaInstruction.class);
                mv.putExtra("Text", text);
                startActivity(mv);
            }
        });
        fashion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                text = "Fashion";
                Intent mv = new Intent(HandoutTrivia.this, TriviaInstruction.class);
                mv.putExtra("Text", text);
                startActivity(mv);
            }
        });
    }
}
