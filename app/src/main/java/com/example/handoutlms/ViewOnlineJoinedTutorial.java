package com.example.handoutlms;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ViewOnlineJoinedTutorial extends AppCompatActivity {

    TextView groupName;
    String name, category, description, mode, g_id;
    ImageView img, back;
    LinearLayout audio, video, pdf, quiz, address, task;
    View view1, view2, view3, view4, view5;

    public static final String ALL_TUTORIAL = "https://handout.com.ng/handouts/handout_get_all_tutorials";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_online_joined_tutorial);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Intent i = getIntent();
        name = i.getStringExtra("name");
        category = i.getStringExtra("category");
        description = i.getStringExtra("description");
        mode = i.getStringExtra("mode");
        g_id = i.getStringExtra("id");

        back = findViewById(R.id.back);
        groupName = findViewById(R.id.groupNAME);
        img = findViewById(R.id.img);
        audio = findViewById(R.id.audio);
        video = findViewById(R.id.video);
        pdf = findViewById(R.id.pdf);
        quiz = findViewById(R.id.quiz);
        address = findViewById(R.id.location);
        task = findViewById(R.id.addTask);

        view1 = findViewById(R.id.view1);
        view2 = findViewById(R.id.view2);
        view3 = findViewById(R.id.view3);
        view4 = findViewById(R.id.view4);
        view5 = findViewById(R.id.view5);

        groupName.setText(name);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        //get the group info using id
        StringRequest stringRequest = new StringRequest(Request.Method.GET, ALL_TUTORIAL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("Response = "+response);

                        try{
                            JSONArray jsonArray = new JSONArray(response);
                            int ArrayLength = jsonArray.length();

                            if(ArrayLength > 1){
                                for(int j = ArrayLength - 1; j >= 0; j--){
                                    JSONObject section1 = jsonArray.getJSONObject(j);


                                    String id = section1.getString("ID");

                                    if(id.equals(g_id)){
                                        String mode = section1.getString("mode");
                                        if(mode.equals("online")){
                                            quiz.setVisibility(View.GONE);
                                            address.setVisibility(View.GONE);
                                            view5.setVisibility(View.GONE);

                                        }
                                        else if(mode.equals("offline")){
                                            img.setImageResource(R.drawable.ic127);
                                            audio.setVisibility(View.GONE);
                                            view1.setVisibility(View.GONE);
                                        }
                                    }



                                }

                            }else{
                                //"no tutorials"
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
                        volleyError.printStackTrace();
                    }
                }){
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);



    }


}