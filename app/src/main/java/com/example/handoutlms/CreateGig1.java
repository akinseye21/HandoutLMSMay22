package com.example.handoutlms;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CreateGig1 extends AppCompatActivity {

    String[] my_gig_list={"Android","Java","IOS","SQL","JDBC","Web services","Graphics","Python","Matlab","FORTRAN", "C++","C#","C#","CSO","Community Development",
                                            "Programming","Mathematics","Physics","Biology","Chemistry","Data Analysis","Engineering","Farming","Geography","History","Judiciary","Kinetics",
                                            "Luminous Science","Node JS","Oracle","Query","R","Statistics","Trigonometry","Universal Science","Vector","X-data"};

    AutoCompleteTextView autoCompleteTextView;
    Button next;
    ArrayList<String> Array_category = new ArrayList<>();
    ArrayList<String> Array_passed = new ArrayList<>();
    GridView mylist;
    LinearLayout skills_layout;
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_gig1);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mylist = findViewById(R.id.mylist);
        skills_layout = findViewById(R.id.skills_layout);
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), AddOptions.class);
                startActivity(i);
            }
        });

        for (int i=0; i<my_gig_list.length; i++){
            Array_category.add(my_gig_list[i]);
        }

        autoCompleteTextView = findViewById(R.id.auto1);
        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,my_gig_list);
        autoCompleteTextView.setAdapter(adapter);
        autoCompleteTextView.setThreshold(0);


        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String adon = String.valueOf(autoCompleteTextView.getText());
//                Toast.makeText(getApplicationContext(), adon, Toast.LENGTH_LONG).show();
                autoCompleteTextView.setText("");
                skills_layout.setVisibility(View.VISIBLE);


                Array_passed.add(adon);
                //populate values on the listview
                GigCategoryAdapter gigCategoryAdapter = new GigCategoryAdapter(getApplicationContext(), Array_passed);
                mylist.setAdapter(gigCategoryAdapter);
            }
        });



        next = findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(), ""+Array_passed, Toast.LENGTH_LONG).show();
                Intent i = new Intent(getApplicationContext(), CreateGig2.class);
                startActivity(i);
            }
        });
    }

    @Override
    public void onBackPressed() {
        // do nothing
    }
}
