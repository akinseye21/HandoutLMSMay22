package com.example.handoutlms.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.handoutlms.R;

public class ViewCv extends AppCompatActivity {

    WebView webView;
    ImageView back;
    TextView name, email;
    String gotName, gotEmail, gotCv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_cv);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        back = findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent i = getIntent();
        gotName = i.getStringExtra("biddername");
        gotCv = i.getStringExtra("cv");
        gotEmail = i.getStringExtra("bidderemail");

        name.setText(gotName);
        email.setText(gotEmail);


        webView = findViewById(R.id.webView1);
        webView.getSettings().setJavaScriptEnabled(true);
        // Configure related browser settings
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        // Configure the client to use when opening URLs
        webView.setWebViewClient(new WebViewClient());
        // Enable responsive layout
        webView.getSettings().setUseWideViewPort(true);
        // Zoom out if the content width is greater than the width of the viewport
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true); // allow pinch to zoom
        webView.getSettings().setDisplayZoomControls(false); // disable the default zoom controls on the page
        // Load the initial URL
        webView.loadUrl("https://www.google.com");
    }


}

