package com.example.handoutlms;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ViewGroupResources extends AppCompatActivity {

    public static final String GROUP_INFO = "https://handoutng.com/handouts/handout_get_tutorial_details";
    public static final String ALL_GIGS_AND_TUTORIALS = "http://handoutng.com/handouts/handout_gigs_groups";
    String group_name;
    String tutorial_material;

    TextView gp_name, cat, dat, tim, uni, desc, tut_type, mode1;
    TextView noMaterial;

    ProgressBar progressBar;
    TextView loading_text;
    ImageView play, pause, back;

    LinearLayout audioMaterial, videoMaterial;

    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_group_resources);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Intent i = getIntent();
        group_name = i.getStringExtra("name");

        gp_name = findViewById(R.id.tutorial_group_name);
        cat = findViewById(R.id.category);
        dat = findViewById(R.id.date);
        tim = findViewById(R.id.time);
        uni = findViewById(R.id.university);
        desc = findViewById(R.id.description);
        tut_type = findViewById(R.id.tut_type);
        mode1 = findViewById(R.id.mode);
        progressBar = findViewById(R.id.progressBar);
        loading_text = findViewById(R.id.loading_text);
        play = findViewById(R.id.idBtnPlay);
        pause = findViewById(R.id.idBtnPause);
        noMaterial = findViewById(R.id.noMaterial);
        back = findViewById(R.id.back);

        audioMaterial = findViewById(R.id.audio_material);
        videoMaterial = findViewById(R.id.video_material);

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playAudio();
            }
        });
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pauseAudio();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        StringRequest stringRequest2 = new StringRequest(Request.Method.GET, ALL_GIGS_AND_TUTORIALS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("Response= "+response);

                        progressBar.setVisibility(View.GONE);
                        loading_text.setVisibility(View.GONE);

                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            int ArrayLength = jsonArray.length();

//                            for (int i = ArrayLength - 1; i >= 0; i--) {
                            for (int i = 0; i < ArrayLength; i++) {
                                JSONObject section = jsonArray.getJSONObject(i);
                                String type = section.getString("type");

                                if (type.equals("group")){
                                    String groupName = section.getString("gig_group_name");

                                    if(group_name.equals(groupName)){
                                        String createdBy = section.getString("created_by");
                                        String createdByName = section.getString("fullname");
                                        String university = section.getString("university");
                                        String mode = section.getString("faculty");
                                        String groupNameInside = section.getString("gig_group_name");
                                        String description = section.getString("description");
                                        String time = section.getString("_time");
                                        String date = section.getString("_date");
                                        String tut_mode = section.getString("mode");
                                        String category = section.getString("category");
                                        String id = section.getString("tid");
                                        tutorial_material = section.getString("tutorial_material");

                                        if (tutorial_material.contains(".mp3")){
                                            audioMaterial.setVisibility(View.VISIBLE);
                                        }
                                        if (tutorial_material.contains(".mp4")){
                                            videoMaterial.setVisibility(View.VISIBLE);
                                            //load video url to view
                                            VideoView videoView = findViewById(R.id.videoView);
                                            Uri uri = Uri.parse(tutorial_material);
                                            videoView.setVideoURI(uri);
                                            MediaController mediaController = new MediaController(ViewGroupResources.this);
                                            mediaController.setAnchorView(videoView);
                                            mediaController.setMediaPlayer(videoView);
                                            videoView.setMediaController(mediaController);
                                            videoView.start();
                                            videoView.setZOrderOnTop(true);
                                        }
                                        if(tutorial_material.equals("")){
                                            noMaterial.setVisibility(View.VISIBLE);
                                        }


                                        gp_name.setText(groupName);
                                        cat.setText(category);
                                        dat.setText(date);
                                        tim.setText(time);
                                        uni.setText(university);
                                        desc.setText(description);
                                        tut_type.setText(tut_mode);
                                        mode1.setText(tut_mode);
                                    }

                                }

                            }



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }


                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        RequestQueue requestQueue2 = Volley.newRequestQueue(getApplicationContext());
        requestQueue2.add(stringRequest2);

    }

    private void playAudio() {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mediaPlayer.setDataSource(tutorial_material);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Toast.makeText(this, "Audio started playing..", Toast.LENGTH_SHORT).show();
    }

    private void pauseAudio() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.release();
            Toast.makeText(this, "Audio has been paused", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(this, "Audio has not played", Toast.LENGTH_SHORT).show();
        }

    }
}
