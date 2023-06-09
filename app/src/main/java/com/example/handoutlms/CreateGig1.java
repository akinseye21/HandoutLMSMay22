package com.example.handoutlms;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import de.hdodenhof.circleimageview.CircleImageView;

public class CreateGig1 extends AppCompatActivity {

    String[] my_gig_list={"Android","Java","IOS","SQL","JDBC","Web services","Graphics","Python","Matlab","FORTRAN", "C++","C#","C#","CSO","Community Development",
            "Programming","Mathematics","Physics","Biology","Chemistry","Data Analysis","Engineering","Farming","Geography","History","Judiciary","Kinetics",
            "Luminous Science","Node JS","Oracle","Query","R","Statistics","Trigonometry","Universal Science","Vector","X-data","Communication","AI","C#","C++","Apache",
    "COBOL","Dart","CMS","Fortran","Go","Google App Script","Haskel","Javascript","JavaFX","Kotlin","Kojo","Lua","MATLAB","Django","Flask","Node JS","Objective-C","Pascal",
    "Prolog","PHP","Swift","SwiftUI","AWS","Rust","Spark","TypeScript","Unity","XQuery"};

    MultiAutoCompleteTextView multiAutoCompleteTextView;
    LinearLayout next;
    ArrayList<String> Array_category = new ArrayList<>();
    ArrayList<String> Array_passed = new ArrayList<>();
    LinearLayout skills_layout;
    ImageView back;

    EditText ProjectName, ProjectDescription;
    String projectName, projectDescription;

    RecyclerView recyclerView;
    Adapter madapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_gig1);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        LinearLayoutManager layoutManager= new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false);
        recyclerView = findViewById(R.id.recycler_view);

        skills_layout = findViewById(R.id.skills_layout);
        back = findViewById(R.id.back);
        ProjectName = findViewById(R.id.edt_projectname);
        ProjectDescription = findViewById(R.id.edt_projectdesc);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        for (int i=0; i<my_gig_list.length; i++){
            Array_category.add(my_gig_list[i]);
        }


        multiAutoCompleteTextView = findViewById(R.id.auto2);
        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,my_gig_list);
        multiAutoCompleteTextView.setAdapter(adapter);
        multiAutoCompleteTextView.setThreshold(0);
        multiAutoCompleteTextView.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

        multiAutoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String input = multiAutoCompleteTextView.getText().toString().trim();
                String[] singleInputs = input.split("\\s*,\\s*");

                for (int j=0; j<singleInputs.length; j++){
                    Array_passed.add(singleInputs[j]);

                    //REMOVE DUPLICATE ITEMS
                    // create a stream from arraylist
                    Stream<String> stream = Array_passed.stream();
                    // call the distinct() of Stream
                    // to remove duplicate elements
                    stream = stream.distinct();
                    // convert the stream to arraylist
                    Array_passed = (ArrayList<String>)stream.collect(Collectors.toList());
                }


//
                if (skills_layout.getVisibility()==View.GONE || skills_layout.getVisibility()==View.INVISIBLE){
                    skills_layout.setVisibility(View.VISIBLE);
                }
//
//                //populate values on the listview
                madapter = new Adapter(Array_passed);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(madapter);

            }
        });

//        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                String adon = String.valueOf(autoCompleteTextView.getText());
//                autoCompleteTextView.setText("");
//                if (skills_layout.getVisibility()==View.GONE || skills_layout.getVisibility()==View.INVISIBLE){
//                    skills_layout.setVisibility(View.VISIBLE);
//                }
//
//                Array_passed.add(adon);
//                //populate values on the listview
//                GigCategoryAdapter gigCategoryAdapter = new GigCategoryAdapter(CreateGig1.this, Array_passed);
//                mylist.setAdapter(gigCategoryAdapter);
//            }
//        });
        next = findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                projectName = ProjectName.getText().toString();
                projectDescription = ProjectDescription.getText().toString();

                if (projectName.isEmpty() || projectDescription.isEmpty()){
                    Snackbar.make(findViewById(android.R.id.content), "Some fields are empty", Snackbar.LENGTH_LONG).show();
                }else{
                    Intent i = new Intent(CreateGig1.this, CreateGig2.class);
                    i.putExtra("Project name", projectName);
                    i.putExtra("Project description", projectDescription);
                    i.putStringArrayListExtra("Required skills", Array_passed);
                    startActivity(i);
                }

            }
        });
    }

    class Adapter extends RecyclerView.Adapter<Adapter.MyHolder> {
        ArrayList<String> named;

        public Adapter(ArrayList<String> named) {
            this.named = named;
        }

        @NonNull
        @Override
        public Adapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(CreateGig1.this).inflate(R.layout.list_gig_category, parent, false);
            return new Adapter.MyHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull Adapter.MyHolder holder, @SuppressLint("RecyclerView") int position) {
            holder.name.setText(named.get(position));
        }

        @Override
        public int getItemCount() {
            return named.size();
        }

        class MyHolder extends RecyclerView.ViewHolder {
            TextView name;

            public MyHolder(@NonNull View itemView) {
                super(itemView);
                name = itemView.findViewById(R.id.category_name);
            }
        }

    }
    
}
