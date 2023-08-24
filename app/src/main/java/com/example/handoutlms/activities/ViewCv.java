package com.example.handoutlms.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.handoutlms.R;
import com.github.barteksc.pdfviewer.PDFView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class ViewCv extends AppCompatActivity {

    WebView webView;
    ImageView back;
    TextView name, email;
    PDFView pdfView;
    LinearLayout loading;
    String gotName, gotEmail, gotCv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_cv);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        back = findViewById(R.id.back);
        pdfView = findViewById(R.id.pdfView);
        loading = findViewById(R.id.loading);

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

        new RetrievePDFfromUrl().execute(gotCv);


//        webView = findViewById(R.id.webView1);
//        webView.getSettings().setJavaScriptEnabled(true);
//        // Configure related browser settings
//        webView.getSettings().setLoadsImagesAutomatically(true);
//        webView.getSettings().setJavaScriptEnabled(true);
//        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
//        // Configure the client to use when opening URLs
//        webView.setWebViewClient(new WebViewClient());
//        // Enable responsive layout
//        webView.getSettings().setUseWideViewPort(true);
//        // Zoom out if the content width is greater than the width of the viewport
//        webView.getSettings().setLoadWithOverviewMode(true);
//        webView.getSettings().setSupportZoom(true);
//        webView.getSettings().setBuiltInZoomControls(true); // allow pinch to zoom
//        webView.getSettings().setDisplayZoomControls(false); // disable the default zoom controls on the page
//        // Load the initial URL
////        webView.loadUrl("https://www.handoutng.com");
//        webView.loadUrl(gotCv);
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

