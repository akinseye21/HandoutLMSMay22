package com.example.handoutlms.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.handoutlms.R;
import com.example.handoutlms.activities.UploadFile;
import com.example.handoutlms.activities.WatchVideo;
import com.example.handoutlms.adapters.PublicFileAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FeedDashboardHandout extends Fragment {

    public FeedDashboardHandout() {
        // Required empty public constructor
    }

    AutoCompleteTextView autoCompleteTextView;
    SwipeRefreshLayout swipeRefreshLayout;
    ListView listHandouts;
    LinearLayout loading;

    //arrays
    ArrayList<Integer> arr_id = new ArrayList<>();
    ArrayList<String> arr_folder = new ArrayList<>();
    ArrayList<String> arr_description = new ArrayList<>();
    ArrayList<String> arr_file = new ArrayList<>();
    ArrayList<String> arr_email = new ArrayList<>();
    ArrayList<String> arr_fullname = new ArrayList<>();
    ArrayList<String> arr_filename = new ArrayList<>();
    ArrayList<String> arr_thumbnail = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_feed_dashboard_handout, container, false);

        loading = v.findViewById(R.id.loading);
        autoCompleteTextView = v.findViewById(R.id.autocomplete);
        swipeRefreshLayout = v.findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getAllPublicHandout();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        listHandouts = v.findViewById(R.id.listHandouts);
        getAllPublicHandout();

        return v;
    }

    private void getAllPublicHandout() {
        arr_id.clear();
        arr_description.clear();
        arr_email.clear();
        arr_folder.clear();
        arr_file.clear();
        arr_fullname.clear();
        arr_filename.clear();
        arr_thumbnail.clear();

//        Dialog myDialog3 = new Dialog(getContext());
//        myDialog3.setContentView(R.layout.custom_popup_login_loading);
//        TextView textView = myDialog3.findViewById(R.id.text);
//        textView.setText("Loading public handouts...");
//        myDialog3.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        myDialog3.setCanceledOnTouchOutside(false);
//        myDialog3.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://handoutng.com/handouts/userdirectory/handout_get_public_files",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONArray jsonArray = new JSONArray(response);
                            int len = jsonArray.length();
                            for (int i = len - 1; i >= 0; i--){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                int id = jsonObject.getInt("id");
                                String folder = jsonObject.getString("folder");
                                String description = jsonObject.getString("description");
                                String file = jsonObject.getString("file");
                                String email = jsonObject.getString("email");
                                String fullname = jsonObject.getString("fullname");
                                String filename = jsonObject.getString("filename");
                                String thumbnail = jsonObject.getString("thumbnail");


                                arr_id.add(id);
                                arr_folder.add(folder);
                                arr_description.add(description);
                                arr_file.add(file);
                                arr_email.add(email);
                                arr_fullname.add(fullname);
                                arr_filename.add(filename);
                                arr_thumbnail.add(thumbnail);
                            }


                            PublicFileAdapter publicFileAdapter = new PublicFileAdapter(getContext(), arr_id, arr_folder, arr_description, arr_file, arr_fullname, arr_filename, arr_thumbnail);
                            listHandouts.setAdapter(publicFileAdapter);


                            ArrayAdapter adapter = new ArrayAdapter(getContext(),R.layout.simple_spinner_small_text, R.id.tx, arr_filename);
                            autoCompleteTextView.setAdapter(adapter);
                            autoCompleteTextView.setThreshold(0);

//                            myDialog3.dismiss();
                            loading.setVisibility(View.GONE);

                        }
                        catch (JSONException e){
//                            myDialog3.dismiss();
                            loading.setVisibility(View.GONE);
                            Toast.makeText(getContext(), "Error loading handouts", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
//                        myDialog3.dismiss();
                        loading.setVisibility(View.GONE);
                        Toast.makeText(getContext(), "Server response problem", Toast.LENGTH_SHORT).show();
                        volleyError.printStackTrace();
                    }
                }){
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        DefaultRetryPolicy retryPolicy = new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);
        requestQueue.add(stringRequest);
        requestQueue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
            @Override
            public void onRequestFinished(Request<Object> request) {
                requestQueue.getCache().clear();
            }
        });


        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                for (int j=0; j<arr_description.size(); j++){
                    if (autoCompleteTextView.getText().toString().trim().equals(arr_filename.get(j))){

                        Dialog myDialog = new Dialog(getContext());
                        myDialog.setContentView(R.layout.custom_popup_fileinformation);
                        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                        TextView close = myDialog.findViewById(R.id.close);
                        close.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                myDialog.dismiss();
                            }
                        });
                        TextView filename = myDialog.findViewById(R.id.filename);
                        filename.setText("File name: "+arr_filename.get(j));
                        TextView filedescription = myDialog.findViewById(R.id.filedescription);
                        filedescription.setText(arr_description.get(j));
                        TextView uploadedby = myDialog.findViewById(R.id.uploadedby);
                        uploadedby.setText("Uploaded by: "+arr_fullname.get(j));
                        ImageView thumbnail = myDialog.findViewById(R.id.thumbnail);
                        int finalJ = j;
                        thumbnail.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                gotofilepage(arr_file.get(finalJ), arr_filename.get(finalJ));
                            }
                        });

                        myDialog.setCanceledOnTouchOutside(true);
                        myDialog.show();



                    }
                }
            }
        });
    }

    private void gotofilepage(String file, String filename) {
        // go to the file details page
        Intent w = new Intent(getContext(), WatchVideo.class);
        w.putExtra("link", file);
        w.putExtra("name", filename);
        w.putExtra("from", "handout_page");
        startActivity(w);
    }


}