package com.example.handoutlms.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.handoutlms.R;
import com.example.handoutlms.uniabuja.GetStarted;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class NigerianUniversities extends AppCompatActivity {

    SharedPreferences preferences;
    String email;
    ImageView back;
    RecyclerView recyclerView;
    MyCustomAdapter myCustomAdapter;
    ArrayList<String> uninames = new ArrayList<>();
    ArrayList<String> unilogo = new ArrayList<>();

    Dialog myDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nigerian_universities);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        preferences = getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        email = preferences.getString("email", "not available");

        myDialog = new Dialog(NigerianUniversities.this);
        myDialog.setContentView(R.layout.custom_popup_login_loading);
        TextView txt = myDialog.findViewById(R.id.text);
        txt.setText("Loading universities...");
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.setCanceledOnTouchOutside(false);
        myDialog.show();

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(NigerianUniversities.this, FeedsDashboard.class);
                i.putExtra("email", email);
                i.putExtra("sent from", "");
                startActivity(i);
            }
        });

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        recyclerView = findViewById(R.id.recycler_view);

        //get all tutors here
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://handoutng.com/handouts/handout_get_trivia_universities",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("Universities= "+response);
//                        Toast.makeText(getContext(), "Response = "+response, Toast.LENGTH_LONG).show();
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            int ArrayLength = jsonArray.length();

                            for (int i = 0; i < ArrayLength; i++) {
                                JSONObject section = jsonArray.getJSONObject(i);
                                String name = section.getString("name");
                                String logo = section.getString("logo");

                                uninames.add(name);
                                unilogo.add(logo);
                            }

                            //populate values on the recyclerview
                            myCustomAdapter = new MyCustomAdapter(uninames, unilogo);
                            recyclerView.setLayoutManager(gridLayoutManager);
                            recyclerView.setAdapter(myCustomAdapter);
                            myDialog.dismiss();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            myDialog.dismiss();
                            Toast.makeText(NigerianUniversities.this, "Error loading universities...", Toast.LENGTH_SHORT).show();
                        }
                    }


                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                myDialog.dismiss();
                Toast.makeText(NigerianUniversities.this, "Network error", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    class MyCustomAdapter extends RecyclerView.Adapter<MyCustomAdapter.MyHolder> {
        ArrayList<String> names;
        ArrayList<String> logo;

        public MyCustomAdapter(ArrayList<String> names, ArrayList<String> logo) {
            this.names = names;
            this.logo = logo;
        }

        @NonNull
        @Override
        public MyCustomAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(NigerianUniversities.this).inflate(R.layout.list_nigerian_universities, parent, false);
            return new MyCustomAdapter.MyHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyHolder holder, @SuppressLint("RecyclerView") int position) {
            holder.name.setText(names.get(position));
            Glide.with(NigerianUniversities.this).load(logo.get(position)).into(holder.img);
            holder.card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(NigerianUniversities.this, GetStarted.class);
                    i.putExtra("uniname", names.get(position));
                    startActivity(i);
                }
            });
        }


        @Override
        public int getItemCount() {
            return names.size();
        }

        class MyHolder extends RecyclerView.ViewHolder {
            TextView name;
            ImageView img;
            RelativeLayout card;

            public MyHolder(@NonNull View itemView) {
                super(itemView);
                name = itemView.findViewById(R.id.uniname);
                img = itemView.findViewById(R.id.img1);
                card = itemView.findViewById(R.id.trivia_layout);
            }
        }

    }
}