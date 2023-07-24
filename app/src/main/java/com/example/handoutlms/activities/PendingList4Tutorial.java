package com.example.handoutlms.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.handoutlms.adapters.PendingAdapterClass;
import com.example.handoutlms.R;

import java.util.ArrayList;

public class PendingList4Tutorial extends AppCompatActivity {
    ImageView back;
    ListView listview;
    TextView text;
    ImageView icon;
    TextView emptylist;

    ArrayList<String> arr_email = new ArrayList<>();
    ArrayList<String> arr_name = new ArrayList<>();
    ArrayList<String> arr_picture = new ArrayList<>();
    String from, id, groupName, category, description, mode, date, name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_list4_tutorial);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Intent i = getIntent();
        arr_email = i.getStringArrayListExtra("email");
        arr_name = i.getStringArrayListExtra("name");
        arr_picture = i.getStringArrayListExtra("picture");
        //send from here back
        from = i.getStringExtra("from");
        id = i.getStringExtra("id");
        groupName = i.getStringExtra("groupName");
        category = i.getStringExtra("category");
        description = i.getStringExtra("description");
        mode = i.getStringExtra("mode");
        date = i.getStringExtra("date");
        name = i.getStringExtra("named");

        text = findViewById(R.id.text);
        icon = findViewById(R.id.icon);
        listview = findViewById(R.id.listview);
        emptylist = findViewById(R.id.empty_list);
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
//                Intent i = new Intent(PendingList4Tutorial.this, ClickTutOnProfile.class);
//                i.putExtra("groupName", groupName);
//                i.putExtra("name", name);
//                i.putExtra("category", category);
//                i.putExtra("description", description);
//                i.putExtra("mode", mode);
//                i.putExtra("id", id);
//                i.putExtra("date", date);
//                startActivity(i);

            }
        });

        if (from.equals("pending")){
            text.setText("Pending");
        }
        if (from.equals("approved")){
            text.setText("Approved");
            icon.setImageResource(R.drawable.new1);
        }

        if (arr_name.size()<1){
            emptylist.setVisibility(View.VISIBLE);
            listview.setVisibility(View.GONE);
        }else{
            emptylist.setVisibility(View.GONE);
            listview.setVisibility(View.VISIBLE);
            PendingAdapterClass pendingAdapterClass = new PendingAdapterClass(PendingList4Tutorial.this, arr_email, arr_name, arr_picture, from, id);
            listview.setAdapter(pendingAdapterClass);

        }





    }
}