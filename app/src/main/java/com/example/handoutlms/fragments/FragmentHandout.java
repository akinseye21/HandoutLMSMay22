package com.example.handoutlms.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.example.handoutlms.adapters.FolderAdapter;
import com.example.handoutlms.adapters.GameAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FragmentHandout extends Fragment {


    public FragmentHandout() {
        // Required empty public constructor
    }

    CardView plus;
    Dialog myDialog;
    GridView gridView;
    SharedPreferences preferences;
    String got_email;
    LinearLayout add_folder;
    LinearLayout myLayout;
    LinearLayout loadingFolders;
    RelativeLayout rel1, rel2;

    ArrayList<String> folderNames = new ArrayList();
    ArrayList<Integer> fileCount = new ArrayList();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_handout, container, false);

        preferences = getContext().getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        got_email = preferences.getString("email", "not available");

        rel1 = v.findViewById(R.id.rel1);
        loadingFolders = v.findViewById(R.id.loadingFolders);
//        rel2 = v.findViewById(R.id.rel2);
        myLayout = v.findViewById(R.id.mylayout);
        gridView = v.findViewById(R.id.gridview);
        add_folder = v.findViewById(R.id.add_folder);

        refreshFragment();

        plus = v.findViewById(R.id.plus);
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createFolder();
            }
        });

        add_folder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createFolder();
            }
        });

        return v;
    }

    private void createFolder() {
        myDialog = new Dialog(getContext());
        myDialog.setContentView(R.layout.custom_popup_foldername);
        EditText foldername = myDialog.findViewById(R.id.edtfoldername);
        Button save = myDialog.findViewById(R.id.btnsave);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callAPI(myDialog, foldername.getText().toString());
            }
        });

        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.setCanceledOnTouchOutside(true);
        myDialog.show();
    }

    private void callAPI(Dialog dialog, String name) {
        Dialog myDialog3 = new Dialog(getContext());
        myDialog3.setContentView(R.layout.custom_popup_login_loading);
        TextView textView = myDialog3.findViewById(R.id.text);
        textView.setText("Creating folder...");
        myDialog3.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog3.setCanceledOnTouchOutside(false);
        myDialog3.show();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://handoutng.com/handouts/userdirectory/handout_make_directory",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equals("success")) {
                            dialog.dismiss();
                            myDialog3.dismiss();
                            Toast.makeText(getContext(), "Folder created", Toast.LENGTH_SHORT).show();
                            //refresh the fragment
                            refreshFragment();
                        }else{
                            myDialog3.dismiss();
                            Toast.makeText(getContext(), "Problem creating folder", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        System.out.println("Error = "+volleyError.getMessage());
                        myDialog3.dismiss();
                        Toast.makeText(getContext(), "Error creating folders", Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("email", got_email);
                params.put("foldername", name);
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
    }

    private void refreshFragment() {
//        Dialog myDialog2 = new Dialog(getContext());
//        myDialog2.setContentView(R.layout.custom_popup_login_loading);
//        TextView textView = myDialog2.findViewById(R.id.text);
//        textView.setText("Loading folders...");
//        myDialog2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        myDialog2.setCanceledOnTouchOutside(false);
//        myDialog2.show();

        //get folders here
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://handoutng.com/handouts/userdirectory/handout_get_directory",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equals("")) {
//                            myDialog2.dismiss();
                            loadingFolders.setVisibility(View.GONE);
                        }else{
//                            myDialog2.dismiss();
                            loadingFolders.setVisibility(View.GONE);
                            myLayout.setVisibility(View.GONE);
                            gridView.setVisibility(View.VISIBLE);
                            add_folder.setVisibility(View.VISIBLE);
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                for (int i = 0; i < jsonArray.length(); i++){
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    String name = jsonObject.getString("name");
                                    int file_count = jsonObject.getInt("file_count");

                                    folderNames.add(name);
                                    fileCount.add(file_count);
                                }
                                FolderAdapter folderAdapter = new FolderAdapter(getContext(), folderNames, fileCount);
                                gridView.setAdapter(folderAdapter);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        System.out.println("Error = "+volleyError.getMessage());
//                        myDialog2.dismiss();
                        loadingFolders.setVisibility(View.GONE);
                        Toast.makeText(getContext(), "Error getting folders", Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("email", got_email);
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

        folderNames.clear();
        fileCount.clear();
    }

}