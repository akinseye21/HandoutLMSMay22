package com.example.handoutlms.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.handoutlms.R;
import com.github.barteksc.pdfviewer.PDFView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class WatchVideo extends AppCompatActivity {

    String link, title;
    ImageView back;
    TextView name;
    LinearLayout loading;
    PDFView pdfView;
    WebView webView;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_video);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Intent i =getIntent();
        link = i.getStringExtra("link");
        title = i.getStringExtra("name");

        webView = findViewById(R.id.videoView);
        loading = findViewById(R.id.loading);
        name = findViewById(R.id.name);
        pdfView = findViewById(R.id.pdfView);
        name.setText(title);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = (int) (displayMetrics.heightPixels / displayMetrics.density);
        int width = (int) (displayMetrics.widthPixels / displayMetrics.density);


        System.out.println("Width = "+width+"\nHeight = "+height);

        if(link.contains(".pdf")){

            pdfView.setVisibility(View.VISIBLE);
            webView.setVisibility(View.GONE);
            new RetrievePDFfromUrl().execute(link);

            //pdf file
//            String pdfViewerURL = "http://docs.google.com/gview?embedded=true&url=";
//            webView.getSettings().setJavaScriptEnabled(false);
//            webView.getSettings().setSupportZoom(true);
//            webView.getSettings().setBuiltInZoomControls(true);
//            webView.setWebViewClient(new WebViewClient());
//            webView.loadUrl(pdfViewerURL+link);

//            Uri uri = Uri.parse(link);
//
//            webView.setVisibility(View.GONE);
//            pdfView.setVisibility(View.VISIBLE);
//            pdfView.fromUri(uri)
//                    .enableSwipe(true)
//                    .swipeHorizontal(true)
//                    .enableDoubletap(true)
//                    .defaultPage(0)
//                    .enableAnnotationRendering(false)
//                    .password(null)
//                    .scrollHandle(null)
//                    .enableAntialiasing(true)
//                    .spacing(0)
//                    .load();


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

    // create an async task class for loading pdf file from URL.
    class RetrievePDFfromUrl extends AsyncTask<String, Void, InputStream> {
        @Override
        protected InputStream doInBackground(String... strings) {
            // we are using inputstream
            // for getting out PDF.
            InputStream inputStream = null;
            try {
                URL url = new URL(strings[0]);
                // below is the step where we are
                // creating our connection.
                HttpURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
                if (urlConnection.getResponseCode() == 200) {
                    // response is success.
                    // we are getting input stream from url
                    // and storing it in our variable.
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
                }

            } catch (IOException e) {
                // this is the method
                // to handle errors.
                e.printStackTrace();
                return null;
            }
            return inputStream;
        }

        @Override
        protected void onPostExecute(InputStream inputStream) {
            // after the execution of our async
            // task we are loading our pdf in our pdf view.
            pdfView.fromStream(inputStream).load();
            loading.setVisibility(View.GONE);
        }
    }
}