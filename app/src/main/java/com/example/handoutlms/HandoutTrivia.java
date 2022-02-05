package com.example.handoutlms;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class HandoutTrivia extends AppCompatActivity {

    ImageView back;

    LinearLayout soccer, entertainment, politics, history;
    LinearLayout maths, english, logic, physics;
    LinearLayout computer, movies, animals, fashion;

    String text;
    TextView correctAnswer, ranking;

    public static final String RANK_URL = "http://35.84.44.203/handouts/trivia/user_rank";

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

        //get sharedpreference
        SharedPreferences sp = getSharedPreferences("LoginDetails", MODE_PRIVATE);
        final String email = sp.getString("email", "");

        TextView username = findViewById(R.id.username);
        correctAnswer = findViewById(R.id.numCorrectAns);
        ranking = findViewById(R.id.ranking);

        username.setText(email);

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


        //send info to database to get ranks
        StringRequest stringRequest = new StringRequest(Request.Method.POST, RANK_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("User ranking array = "+response);
                        JSONObject jsonObject;
                        try {
                            jsonObject = new JSONObject(response);
                            String rank = jsonObject.getString("overall_rank");
                            String test_completed = jsonObject.getString("overall_test_completed");

                            ranking.setText(rank);
                            correctAnswer.setText(test_completed);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Network connectivity problem", Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(HandoutTrivia.this);
        requestQueue.add(stringRequest);
    }
}
