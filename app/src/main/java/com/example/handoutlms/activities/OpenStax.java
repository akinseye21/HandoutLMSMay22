package com.example.handoutlms.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.ImageView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.handoutlms.R;
import com.example.handoutlms.adapters.OpenStaxAdapter;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class OpenStax extends AppCompatActivity {

    ImageView back;
    TabLayout tabLayout;
    ArrayList<String> LibraryCategories;
    ArrayList<String> uniqueList;
    ArrayList<String> generalTitle;
    ArrayList<String> generalCoverUrl;
    ArrayList<String> generalPdfUrl;
    ArrayList<String> generalSalesforceName;
    ArrayList<String> generalSubject;
    ArrayList<String> generalPosition;
    GridView gridView;

    public static final String GET_CATEGORIES = "https://openstax.org/apps/cms/api/v2/pages/subjects/?format=json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_stax);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        tabLayout = findViewById(R.id.tab_layout);
        gridView = findViewById(R.id.simpleGridView2);

        LibraryCategories = new ArrayList<>();
        generalTitle = new ArrayList<>();
        generalSubject = new ArrayList<>();
        generalCoverUrl = new ArrayList<>();
        generalPdfUrl = new ArrayList<>();
        generalSalesforceName = new ArrayList<>();
        generalPosition = new ArrayList<>();

        //get the categories and store in array
        StringRequest stringRequest = new StringRequest(Request.Method.GET, GET_CATEGORIES,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            // Create a JSONObject from the JSON response
                            JSONObject jsonObject = new JSONObject(response);
                            // Get the "books" JSONArray from the JSONObject
                            JSONArray booksArray = jsonObject.getJSONArray("books");

                            int arrayLength = booksArray.length();
                            for (int i = 1; i<91; i++){
                                // Get the first item from the booksArray (assuming it contains only one item)
                                JSONObject bookObject = booksArray.getJSONObject(i);

//                                System.out.println("Response = "+bookObject);
                                //get the information per book
                                String title = bookObject.getString("title");
                                String cover_url = bookObject.getString("cover_url");
                                String pdf_url = bookObject.getString("high_resolution_pdf_url");
                                String salesforce_name = bookObject.getString("salesforce_name");
                                JSONArray subjectsArray2 = bookObject.getJSONArray("subjects");
                                String subject2 = subjectsArray2.getString(0);


                                generalTitle.add(title);
                                generalCoverUrl.add(cover_url);
                                generalPdfUrl.add(pdf_url);
                                generalSalesforceName.add(salesforce_name);
                                generalSubject.add(subject2);
                                generalPosition.add(String.valueOf(i));


                                System.out.println("Title = "+generalTitle);
                                System.out.println("CoverURL = "+generalCoverUrl);

//                                // Get the "subjects" JSONArray from the bookObject
                                JSONArray subjectsArray = bookObject.getJSONArray("subjects");
                                String subject = subjectsArray.getString(0);
//                                // Print the subject
                                LibraryCategories.add(subject);
//                                // Remove duplicates using Stream API
                                uniqueList = LibraryCategories.stream().distinct().collect(Collectors.toCollection(ArrayList::new));
//                                // Replace the original ArrayList with the unique list
                                LibraryCategories.clear();
                                LibraryCategories.addAll(uniqueList);

                            }
//                            System.out.println("Image Url = "+generalCoverUrl);
                            OpenStaxAdapter openStaxAdapter = new OpenStaxAdapter(OpenStax.this, generalTitle, generalCoverUrl, generalPdfUrl, generalSalesforceName, generalSubject, generalPosition);
                            gridView.setAdapter(openStaxAdapter);

//
//                            System.out.println("Unique = "+LibraryCategories);

//                            for (String item : LibraryCategories) {
//                                TabLayout.Tab tab = tabLayout.newTab();
//                                tab.setText(item);
//                                tabLayout.addTab(tab);
//                            }
//                            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//                                @Override
//                                public void onTabSelected(TabLayout.Tab tab) {
//                                    // Handle tab selection
//                                }
//
//                                @Override
//                                public void onTabUnselected(TabLayout.Tab tab) {
//                                    // Handle tab unselection
//                                }
//
//                                @Override
//                                public void onTabReselected(TabLayout.Tab tab) {
//                                    // Handle tab reselection
//                                }
//                            });




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
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        DefaultRetryPolicy retryPolicy = new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);
        requestQueue.add(stringRequest);
        requestQueue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
            @Override
            public void onRequestFinished(Request<Object> request) {
                requestQueue.getCache().clear();
            }
        });
    }

    public void getCategories() {

        //GET CATEGORIES


    }
}