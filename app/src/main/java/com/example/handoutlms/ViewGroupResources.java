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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ViewGroupResources extends AppCompatActivity {

    public static final String GROUP_INFO = "http://35.84.44.203/handouts/handout_get_tutorial_details";
    String group_name;
    String tutorial_material;

    TextView gp_name, cat, dat, tim, uni, desc, tut_type, mode;

    ProgressBar progressBar;
    TextView loading_text;
    ImageView play, pause;

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
        mode = findViewById(R.id.mode);
        progressBar = findViewById(R.id.progressBar);
        loading_text = findViewById(R.id.loading_text);
        play = findViewById(R.id.idBtnPlay);
        pause = findViewById(R.id.idBtnPause);

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

        StringRequest stringRequest = new StringRequest(Request.Method.POST, GROUP_INFO,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        System.out.println("Response = "+response);
                        Toast.makeText(getApplicationContext(), "Response = "+response, Toast.LENGTH_LONG).show();

                        progressBar.setVisibility(View.GONE);
                        loading_text.setVisibility(View.GONE);

                        try{
                            JSONObject jsonObject = new JSONObject(response);

                            String gpname = jsonObject.getString("groupname");
                            String category = jsonObject.getString("category");
                            String date = jsonObject.getString("_date");
                            String time = jsonObject.getString("_time");
                            String university = jsonObject.getString("university");
                            String description = jsonObject.getString("description");
                            String tuttype = jsonObject.getString("tutorial_type");
                            String tutmode = jsonObject.getString("mode");
                            tutorial_material = jsonObject.getString("tutorial_material");

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

                            gp_name.setText(gpname);
                            cat.setText(category);
                            dat.setText(date);
                            tim.setText(time);
                            uni.setText(university);
                            desc.setText(description);
                            tut_type.setText(tuttype);
                            mode.setText(tutmode);

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
                        loading_text.setVisibility(View.GONE);
                        if(volleyError == null){
                            return;
                        }
                        System.out.println("Error = "+volleyError.getMessage());
                    }
                }){
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("groupname", group_name);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
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
