package com.example.handoutlms;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class WatchVideo extends AppCompatActivity {

    String link, title;
    ImageView back;
    TextView name;
    LinearLayout loading;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_video);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Intent i =getIntent();
        link = i.getStringExtra("link");
        title = i.getStringExtra("name");

        WebView webView = findViewById(R.id.videoView);
        loading = findViewById(R.id.loading);
        name = findViewById(R.id.name);
        name.setText(title);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = (int) (displayMetrics.heightPixels / displayMetrics.density);
        int width = (int) (displayMetrics.widthPixels / displayMetrics.density);


        System.out.println("Width = "+width+"\nHeight = "+height);

        if(link.contains(".pdf")){
            //pdf file
            String pdfViewerURL = "http://docs.google.com/gview?embedded=true&url=";
            webView.getSettings().setJavaScriptEnabled(false);
            webView.getSettings().setSupportZoom(true);
            webView.getSettings().setBuiltInZoomControls(true);
            webView.setWebViewClient(new WebViewClient());
            webView.loadUrl(pdfViewerURL+link);

            loading.setVisibility(View.GONE);
        }else{
            //video file
            String frameVideo = "<iframe width=\""+width+"\" height=\""+height+"\" " +
                    "src='" + link + "' frameborder=\"0\" allowfullscreen>" +
                    "</iframe>";
            String regexYoutUbe = "^(http(s)?:\\/\\/)?((w){3}.)?youtu(be|.be)?(\\.com)?\\/.+";

            if (link.matches(regexYoutUbe)) {
                webView.setWebViewClient(new WebViewClient() {
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        return false;
                    }
                });

                WebSettings webSettings = webView.getSettings();
                webSettings.setJavaScriptEnabled(true);
                webView.loadData(frameVideo, "text/html", "utf-8");
            }
            else {
                Toast.makeText(WatchVideo.this, "This is not a youtube video", Toast.LENGTH_SHORT).show();
            }

            loading.setVisibility(View.GONE);
        }

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}