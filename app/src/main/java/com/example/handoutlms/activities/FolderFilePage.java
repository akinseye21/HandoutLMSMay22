package com.example.handoutlms.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.example.handoutlms.adapters.FileAdapter;
import com.example.handoutlms.adapters.FolderAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FolderFilePage extends AppCompatActivity {

    String foldername;
    int foldercount;
    TextView txt_foldername;
    LinearLayout mylayout2, addfile;
    GridView gridView2;
    ImageView back;

    ArrayList<String> arr_file = new ArrayList<>();
    ArrayList<String> arr_filename = new ArrayList<>();
    ArrayList<String> arr_thumbnail = new ArrayList<>();
    ArrayList<Integer> arr_id = new ArrayList<>();

    SharedPreferences preferences;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder_file_page);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //get sharedpreference
        preferences = getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        email = preferences.getString("email", "not available");

        Intent i = getIntent();
        foldername = i.getStringExtra("foldername");
        foldercount = i.getIntExtra("foldercount", 0);

        mylayout2 = findViewById(R.id.mylayout2);
        gridView2 = findViewById(R.id.grid2);
        addfile = findViewById(R.id.add_file);
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(FolderFilePage.this, FeedsDashboard.class);
                i.putExtra("email", email);
                i.putExtra("sent from", "folder file");
                startActivity(i);
//                onBackPressed();
            }
        });

        txt_foldername = findViewById(R.id.foldername);
        txt_foldername.setText(foldername);
        if (foldercount == 0){
            // do nothing
        }else{
            gridView2.setVisibility(View.VISIBLE);
            mylayout2.setVisibility(View.GONE);
            //load files in folder
            getFiles();
        }

        addfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //open the add file page
                Intent intent = new Intent(FolderFilePage.this, UploadFile.class);
                intent.putExtra("folder_name", foldername);
                intent.putExtra("folder_count", foldercount);
                startActivity(intent);
            }
        });
    }

    private void getFiles() {
        Dialog myDialog = new Dialog(FolderFilePage.this);
        myDialog.setContentView(R.layout.custom_popup_login_loading);
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView text = myDialog.findViewById(R.id.text);
        text.setText("Loading your file(s)");
        myDialog.setCanceledOnTouchOutside(false);
        myDialog.show();
        
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://handoutng.com/handouts/userdirectory/handout_get_files",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        myDialog.dismiss();
                        try{
                            JSONArray jsonArray = new JSONArray(response);
                            int length = jsonArray.length();
                            for (int i = length - 1; i >= 0; i--){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String folder = jsonObject.getString("folder");
                                if (folder.equals(foldername)){
                                    String file = jsonObject.getString("file");
                                    String filename = jsonObject.getString("filename");
                                    String thumbnail = jsonObject.getString("thumbnail");
                                    int id = jsonObject.getInt("id");

                                    arr_file.add(file);
                                    arr_id.add(id);
                                    arr_filename.add(filename);
                                    arr_thumbnail.add(thumbnail);
                                }
                            }

                            FileAdapter fileAdapter = new FileAdapter(FolderFilePage.this, arr_file, arr_id, arr_filename, arr_thumbnail);
                            gridView2.setAdapter(fileAdapter);
                        }
                        catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                        myDialog.dismiss();
                        Toast.makeText(FolderFilePage.this, "Error getting files", Toast.LENGTH_SHORT).show();
//                        if(volleyError == null){
//                            return;
//                        }
//                        myDialog.dismiss();
//                        Toast.makeText(Login.this,  "Network Error", Toast.LENGTH_LONG).show();
//                        System.out.println("Network Error "+volleyError);
                    }
                }){
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                return params;
            }
        };

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
}