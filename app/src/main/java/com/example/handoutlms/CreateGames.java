package com.example.handoutlms;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class CreateGames extends AppCompatActivity {

    ImageView back;
    EditText gameName, gameDate, gameTime, location;
    LinearLayout create;
    DatePickerDialog datePickerDialog;
    TimePickerDialog picker;
    String email;
    String game_name, game_date, game_time, game_location;
    ProgressBar progressBar;
    TextView loadingText;

    public static final String CREATE_GAMES = "https://handout.com.ng/handouts/handout_create_game";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_games);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Intent j = getIntent();
        email = j.getStringExtra("email");

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        gameName = findViewById(R.id.game_name);
        gameDate = findViewById(R.id.date);
        gameTime = findViewById(R.id.time);
        location = findViewById(R.id.game_location);
        create = findViewById(R.id.create);
        progressBar = findViewById(R.id.progressBar);
        loadingText = findViewById(R.id.progressText);

        gameDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // calender class's instance and get current date , month and year from calender
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day

                // date picker dialog
                datePickerDialog = new DatePickerDialog(CreateGames.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                monthOfYear+=1;
                                // set day of month , month and year value in the edit text
                                String mt;
                                if(monthOfYear<10){
                                    mt = "0"+monthOfYear;
                                }
                                else mt = String.valueOf(monthOfYear);
                                String dy;
                                if(dayOfMonth<10)
                                    dy = "0"+dayOfMonth;
                                else dy = String.valueOf(dayOfMonth);

                                gameDate.setText(dy + "/"
                                        + mt + "/" + year);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
        gameTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar cldr = Calendar.getInstance();
                int hour = cldr.get(Calendar.HOUR_OF_DAY);
                int minutes = cldr.get(Calendar.MINUTE);
                // time picker dialog
                picker = new TimePickerDialog(CreateGames.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                                gameTime.setText(sHour + ":" + sMinute);
                            }
                        }, hour, minutes, true);
                picker.show();
            }
        });
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                loadingText.setVisibility(View.VISIBLE);

                game_name = gameName.getText().toString();
                game_date = gameDate.getText().toString();
                game_time = gameTime.getText().toString();
                game_location = location.getText().toString();

                if(game_name.equals("") || game_date.equals("") || game_time.equals("") || game_location.equals("")){
                    Toast.makeText(getApplicationContext(), "One or more fields are empty", Toast.LENGTH_LONG).show();
                }else{
                    //send to the DB
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, CREATE_GAMES,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    progressBar.setVisibility(View.GONE);
                                    loadingText.setVisibility(View.GONE);

                                    System.out.println("Response = "+response);

                                    try{
                                        JSONObject jsonObject = new JSONObject(response);

                                        String status = jsonObject.getString("status");
                                        String notification = jsonObject.getString("notification");

                                        if(status.equals("successful")){
                                            Toast.makeText(getApplicationContext(), "Game created successfully", Toast.LENGTH_LONG).show();
                                            Intent i = new Intent(getApplicationContext(), FeedsDashboard.class);
                                            startActivity(i);

                                        }
                                        else if(!status.equals("successful")){
                                            Toast.makeText(getApplicationContext(), "Game not created, please try again", Toast.LENGTH_LONG).show();

                                        }
                                    }
                                    catch (JSONException e){
                                        e.printStackTrace();
                                    }

                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError volleyError) {
                                    progressBar.setVisibility(View.GONE);
                                    System.out.println("Error = "+volleyError.getMessage());
                                }
                            }){
                        @Override
                        protected Map<String, String> getParams(){
                            Map<String, String> params = new HashMap<>();
                            params.put("game_name", game_name);
                            params.put("dates", game_date);
                            params.put("times", game_time);
                            params.put("email", email);
                            params.put("location", game_location);
                            return params;
                        }
                    };
                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                    requestQueue.add(stringRequest);
                }
            }
        });
    }
}