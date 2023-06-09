package com.example.handoutlms;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.text.format.DateFormat;

public class AddTask extends AppCompatActivity {

//    List<EventDay> events = new ArrayList<>();

    ImageView back;
    String day_of_the_week, day, month, year;
    TextView monthYear;
    LinearLayout lintoday, linyesterday, lintomorrow, lin2daysback, linnexttomorrow;
    TextView dateToday, dateTomorrow, dateYesterday, date2daysback, dateNextTomorrow;
    TextView dayToday, dayTomorrow, dayYesterday, day2daysback, dayNextTomorrow;
    LinearLayout add;
    String email;
    ListView listView;

    ArrayList<String> arr_task_name = new ArrayList<>();
    ArrayList<String> arr_task_date = new ArrayList<>();
    ArrayList<String> arr_task_category = new ArrayList<>();
    ArrayList<String> arr_task_description = new ArrayList<>();
    ArrayList<String> arr_task_time = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Intent i = getIntent();
        email = i.getStringExtra("email");
        arr_task_name = i.getStringArrayListExtra("task name");
        arr_task_date = i.getStringArrayListExtra("task date");
        arr_task_category = i.getStringArrayListExtra("task category");
        arr_task_description = i.getStringArrayListExtra("task description");
        arr_task_time = i.getStringArrayListExtra("task time");

        back = findViewById(R.id.back);
        monthYear = findViewById(R.id.monthYear);
        lintoday = findViewById(R.id.linToday);
        linyesterday = findViewById(R.id.linyesterday);
        lintomorrow = findViewById(R.id.lintomorrow);
        lin2daysback = findViewById(R.id.lin2daysback);
        linnexttomorrow = findViewById(R.id.linNexttomorrow);
        dateToday = findViewById(R.id.dateToday);
        dateTomorrow = findViewById(R.id.dateTomorrow);
        dateYesterday = findViewById(R.id.dateYesterday);
        date2daysback = findViewById(R.id.date2daysback);
        dateNextTomorrow = findViewById(R.id.dateNextTomorrow);
        dayToday = findViewById(R.id.dayToday);
        dayTomorrow = findViewById(R.id.dayTomorrow);
        dayYesterday = findViewById(R.id.dayYesterday);
        dayNextTomorrow = findViewById(R.id.dayNextTomorrow);
        day2daysback = findViewById(R.id.day2daysback);
        add = findViewById(R.id.add);
        listView = findViewById(R.id.listview);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        Date date=new Date(System.currentTimeMillis());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        //today
        day_of_the_week = (String) DateFormat.format("EEE", date); // Thursday
        day          = (String) DateFormat.format("dd",   date); // 20
        month  = (String) DateFormat.format("MMMM",  date); // Jun
        year         = (String) DateFormat.format("yyyy", date); // 2013

        //yesterday
        calendar.add(Calendar.DAY_OF_MONTH, -1); //Goes to previous day
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd", Locale.getDefault());
        String yesterdayDateString = dateFormat.format(calendar.getTime());
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("EEE", Locale.getDefault());
        String yesterdayDayString = dateFormat2.format(calendar.getTime());

        //2 days back
        calendar.add(Calendar.DAY_OF_MONTH, -1); //Goes to previous day
        SimpleDateFormat dateFormat3 = new SimpleDateFormat("dd", Locale.getDefault());
        String TwodaysagoDate = dateFormat3.format(calendar.getTime());
        SimpleDateFormat dateFormat4 = new SimpleDateFormat("EEE", Locale.getDefault());
        String TwodaysagoDay = dateFormat4.format(calendar.getTime());

        Date dateTomorrows=new Date(System.currentTimeMillis());
        Calendar calendarTomorrow = Calendar.getInstance();
        calendarTomorrow.setTime(dateTomorrows);
        // tomorrow
        calendarTomorrow.add(Calendar.DAY_OF_MONTH, 1); //Goes to previous day
        SimpleDateFormat dateFormat5 = new SimpleDateFormat("dd", Locale.getDefault());
        String TomorrowDate = dateFormat5.format(calendarTomorrow.getTime());
        SimpleDateFormat dateFormat6 = new SimpleDateFormat("EEE", Locale.getDefault());
        String TomorrowDay = dateFormat6.format(calendarTomorrow.getTime());

        // next tomorrow
        calendarTomorrow.add(Calendar.DAY_OF_MONTH, 1); //Goes to previous day
        SimpleDateFormat dateFormat7 = new SimpleDateFormat("dd", Locale.getDefault());
        String NextTomorrowDate = dateFormat7.format(calendarTomorrow.getTime());
        SimpleDateFormat dateFormat8 = new SimpleDateFormat("EEE", Locale.getDefault());
        String NextTomorrowDay = dateFormat8.format(calendarTomorrow.getTime());

        monthYear.setText(month+" "+year);
        dateToday.setText(day);
        dayToday.setText(day_of_the_week);
        dateYesterday.setText(yesterdayDateString);
        dayYesterday.setText(yesterdayDayString);
        date2daysback.setText(TwodaysagoDate);
        day2daysback.setText(TwodaysagoDay);
        dateTomorrow.setText(TomorrowDate);
        dayTomorrow.setText(TomorrowDay);
        dateNextTomorrow.setText(NextTomorrowDate);
        dayNextTomorrow.setText(NextTomorrowDay);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AddTask.this, CreateNewTask.class);
                i.putExtra("email", email);
                startActivity(i);
            }
        });

        //populate listview
        TodayTaskAdapter2 myAdapter=new TodayTaskAdapter2(getApplicationContext(),arr_task_name,arr_task_date,arr_task_category, arr_task_description, arr_task_time);
        listView.setAdapter(myAdapter);




    }

    @Override
    protected void onStart() {
        super.onStart();

//        arr_task_name.clear();
//        arr_task_date.clear();
//        arr_task_category.clear();
//        arr_task_description.clear();
//        arr_task_time.clear();




    }
}