package com.example.handoutlms.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.handoutlms.R;
import com.example.handoutlms.VolleyMultipartRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class EditResources extends AppCompatActivity {

    String groupName, fileName, fileURL, fileDesc, thumbnail, resID, mode;
    TextView username, txtGroupName;
    EditText edtTutorialName, edtTutorialDesc, edtTutorialLink;
    ImageView imgThumbNail, delete, back;
    Button save;
    String fullnamed;
    LinearLayout linlink, file;
    Button selectpdf;

    String myEmail;
    String resname, resdesc, reslink;

    Dialog myDialog, myDialog2;
    String CHANNEL_ID = "channelID5";

    SharedPreferences preferences;

    private static final String VIDEO_UPDATE = "http://handoutng.com/handouts/handout_update_tutorial_group_resource_url";
    private static final String PDF_UPDATE = "http://handoutng.com/handouts/handout_update_tutorial_group_resource_file";
    private RequestQueue rQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_resources);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        preferences = getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        fullnamed = preferences.getString("fullname", "");
        myEmail = preferences.getString("email", "not available");

        Intent i = getIntent();
        groupName = i.getStringExtra("groupName");
        fileName = i.getStringExtra("fileName");
        fileDesc = i.getStringExtra("fileDesc");
        thumbnail = i.getStringExtra("thumbnail");
        fileURL = i.getStringExtra("fileURL");
        resID = i.getStringExtra("resID");
        mode = i.getStringExtra("mode");

        username = findViewById(R.id.fullname);
        txtGroupName = findViewById(R.id.groupName);
        edtTutorialName = findViewById(R.id.edtTutorialName);
        edtTutorialDesc = findViewById(R.id.edtTutorialDesc);
        edtTutorialLink = findViewById(R.id.edtTutorialLink);
        imgThumbNail = findViewById(R.id.img);
        delete = findViewById(R.id.delete);
        save = findViewById(R.id.save);

        file = findViewById(R.id.file);
        linlink = findViewById(R.id.linlink);
        selectpdf = findViewById(R.id.selectpdf);

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        username.setText(fullnamed);
        txtGroupName.setText(groupName);
        edtTutorialName.setText(fileName);
        edtTutorialDesc.setText(fileDesc);
        Glide.with(getApplicationContext()).load(thumbnail).into(imgThumbNail);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                file.setVisibility(View.GONE);

                //if resource is pdf file
                if (fileURL.contains(".pdf")){
                    selectpdf.setVisibility(View.VISIBLE);
                    save.setVisibility(View.GONE);
                    selectpdf.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //get file name
                            resname = edtTutorialName.getText().toString().trim();
                            //get file description
                            resdesc = edtTutorialDesc.getText().toString().trim();

                            if (resname.equals("") || resdesc.equals("")){
                                Toast.makeText(EditResources.this, "Value(s) empty", Toast.LENGTH_SHORT).show();
                                edtTutorialName.setError("Error");
                                edtTutorialDesc.setError("Error");
                            }else{
                                // select a file and upload
                                Intent intent = new Intent();
                                intent.setAction(Intent.ACTION_GET_CONTENT);
                                intent.setType("application/pdf");
                                startActivityForResult(intent,1);
                            }


                        }
                    });
                }else{
                    //resource is video file
                    linlink.setVisibility(View.VISIBLE);
                    save.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //get the file name
                            resname = edtTutorialName.getText().toString().trim();
                            //get the file description
                            resdesc = edtTutorialDesc.getText().toString().trim();
                            //get the new video link
                            reslink = edtTutorialLink.getText().toString().trim();

                            //check for empty field
                            if (resname.equals("") || resdesc.equals("") || reslink.equals("")){
                                Toast.makeText(EditResources.this, "Value(s) empty", Toast.LENGTH_SHORT).show();
                                edtTutorialName.setError("Error");
                                edtTutorialDesc.setError("Error");
                                edtTutorialLink.setError("Error");
                            }else {
                                //send to DB
                                updateVideo();
                            }

                        }
                    });
                }



            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get the file name
                resname = edtTutorialName.getText().toString().trim();
                //get the file description
                resdesc = edtTutorialDesc.getText().toString().trim();

                //check for empty field
                if (resname.equals("") || resdesc.equals("")){
                    Toast.makeText(EditResources.this, "Value(s) empty", Toast.LENGTH_SHORT).show();
                    edtTutorialName.setError("Error");
                    edtTutorialDesc.setError("Error");
                }else {
                    //send to DB
                    updateVideo2();
                }
            }
        });


    }

    private void updateVideo() {
        myDialog = new Dialog(EditResources.this);
        myDialog.setContentView(R.layout.custom_popup_login_loading);
        TextView text = myDialog.findViewById(R.id.text);
        text.setText("Uploading, please wait");
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.setCanceledOnTouchOutside(false);
        myDialog.show();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, VIDEO_UPDATE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try{
                            JSONObject update = new JSONObject(response);
                            String status = update.getString("status");
                            if (status.equals("success")){
                                //show popup of successful
                                myDialog.dismiss();
                                createNotificationChannel("Video");

                                myDialog2 = new Dialog(EditResources.this);
                                myDialog2.setContentView(R.layout.custom_popup_successful_taskmanager);
                                TextView text = myDialog2.findViewById(R.id.status);
                                Button home = myDialog2.findViewById(R.id.home);
                                home.setText("OK");
                                text.setText("Edit was successful!!");
                                home.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        myDialog2.dismiss();
                                        Intent intent = new Intent(EditResources.this, VideoCreatorView.class);
                                        intent.putExtra("groupName", groupName);
                                        intent.putExtra("from", "EditResources");
                                        intent.putExtra("mode", mode);
                                        intent.putExtra("name", fileName);
                                        intent.putExtra("category", "");
                                        intent.putExtra("description", fileDesc);
                                        intent.putExtra("id", resID);
                                        intent.putExtra("date", "");
                                        startActivity(intent);
                                    }
                                });
                                myDialog2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                myDialog2.setCanceledOnTouchOutside(false);
                                myDialog2.show();

                            }else{
                                //toast error deleting
                                myDialog.dismiss();
                                Toast.makeText(EditResources.this, "Edit failed. Please try again", Toast.LENGTH_SHORT).show();

                            }

                        }
                        catch (JSONException e){
                            myDialog.dismiss();
                            Toast.makeText(EditResources.this, "Edit failed. Please try again", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        myDialog.dismiss();
                        Toast.makeText(EditResources.this, "Edit failed. Please try again", Toast.LENGTH_SHORT).show();
                        volleyError.printStackTrace();
                    }
                }){
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("email", myEmail);
                params.put("resid", resID);
                params.put("fileDescription", resdesc);
                params.put("fileName", resname);
                params.put("fileurl", reslink);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(EditResources.this);
        DefaultRetryPolicy retryPolicy = new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);
        requestQueue.add(stringRequest);
    }

    private void updateVideo2() {
        myDialog = new Dialog(EditResources.this);
        myDialog.setContentView(R.layout.custom_popup_login_loading);
        TextView text = myDialog.findViewById(R.id.text);
        text.setText("Uploading, please wait");
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.setCanceledOnTouchOutside(false);
        myDialog.show();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, VIDEO_UPDATE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try{
                            JSONObject update = new JSONObject(response);
                            String status = update.getString("status");
                            if (status.equals("success")){
                                //show popup of successful
                                myDialog.dismiss();
                                createNotificationChannel("Video");

                                myDialog2 = new Dialog(EditResources.this);
                                myDialog2.setContentView(R.layout.custom_popup_successful_taskmanager);
                                TextView text = myDialog2.findViewById(R.id.status);
                                Button home = myDialog2.findViewById(R.id.home);
                                home.setText("OK");
                                text.setText("Edit was successful!!");
                                home.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        myDialog2.dismiss();
                                        Intent intent = new Intent(EditResources.this, VideoCreatorView.class);
                                        intent.putExtra("groupName", groupName);
                                        intent.putExtra("from", "EditResources");
                                        intent.putExtra("mode", mode);
                                        intent.putExtra("name", fileName);
                                        intent.putExtra("category", "");
                                        intent.putExtra("description", fileDesc);
                                        intent.putExtra("id", resID);
                                        intent.putExtra("date", "");
                                        startActivity(intent);
                                    }
                                });
                                myDialog2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                myDialog2.setCanceledOnTouchOutside(false);
                                myDialog2.show();

                            }else{
                                //toast error deleting
                                myDialog.dismiss();
                                Toast.makeText(EditResources.this, "Edit failed. Please try again", Toast.LENGTH_SHORT).show();

                            }

                        }
                        catch (JSONException e){
                            myDialog.dismiss();
                            Toast.makeText(EditResources.this, "Edit failed. Please try again", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        myDialog.dismiss();
                        Toast.makeText(EditResources.this, "Edit failed. Please try again", Toast.LENGTH_SHORT).show();
                        volleyError.printStackTrace();
                    }
                }){
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("email", myEmail);
                params.put("resid", resID);
                params.put("fileDescription", resdesc);
                params.put("fileName", resname);
                params.put("fileurl", fileURL);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(EditResources.this);
        DefaultRetryPolicy retryPolicy = new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);
        requestQueue.add(stringRequest);
    }

    @SuppressLint("Range")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            // Get the Uri of the selected file
            Uri uri = data.getData();
            String uriString = uri.toString();
            File myFile = new File(uriString);
//            String path = myFile.getAbsolutePath();
            String displayName = null;
            if (uriString.startsWith("content://")) {
                Cursor cursor = null;
                try {
                    cursor = this.getContentResolver().query(uri, null, null, null, null);
                    if (cursor != null && cursor.moveToFirst()) {
                        displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                        Log.d("nameeeee>>>>  ",displayName);
//                        file_path.setText(displayName);
                        uploadPDF(displayName,uri);
                    }
                } finally {
                    cursor.close();
                }
            } else if (uriString.startsWith("file://")) {
                displayName = myFile.getName();
                Log.d("nameeeee>>>>  ",displayName);
//                file_path.setText(displayName);
            }
        }

        super.onActivityResult(requestCode, resultCode, data);

    }

    private void uploadPDF(final String pdfname, Uri pdffile){

        myDialog = new Dialog(EditResources.this);
        myDialog.setContentView(R.layout.custom_popup_login_loading);
        TextView text = myDialog.findViewById(R.id.text);
        ProgressBar progressBar = myDialog.findViewById(R.id.progressBar);
        text.setText("Uploading, please wait");
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.setCanceledOnTouchOutside(false);
        myDialog.show();
//        upload_text.setVisibility(View.VISIBLE);

        InputStream iStream = null;
        try {

            iStream = getContentResolver().openInputStream(pdffile);
            final byte[] inputData = getBytes(iStream);

            VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, PDF_UPDATE,
                    new Response.Listener<NetworkResponse>() {
                        @Override
                        public void onResponse(NetworkResponse response) {
                            Log.d("ressssssoo",new String(response.data));
                            rQueue.getCache().clear();
                            try {
                                JSONObject jsonObject = new JSONObject(new String(response.data));
                                String status = jsonObject.getString("status");

                                if(status.equals("success")){
                                    Toast.makeText(getApplicationContext(), "File Uploaded successfully", Toast.LENGTH_LONG).show();
                                    System.out.println("Status = "+status);
                                    myDialog.dismiss();
                                    createNotificationChannel("PDF");

                                    myDialog2 = new Dialog(EditResources.this);
                                    myDialog2.setContentView(R.layout.custom_popup_successful_taskmanager);
                                    TextView text = myDialog2.findViewById(R.id.status);
                                    Button home = myDialog2.findViewById(R.id.home);
                                    home.setText("OK");
                                    text.setText("Edit was successful!!");
                                    home.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            myDialog2.dismiss();
                                            Intent intent = new Intent(EditResources.this, VideoCreatorView.class);
                                            intent.putExtra("groupName", groupName);
                                            intent.putExtra("from", "EditResources");
                                            startActivity(intent);
                                        }
                                    });
                                    myDialog2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                    myDialog2.setCanceledOnTouchOutside(false);
                                    myDialog2.show();
//                                    upload_text.setVisibility(View.GONE);
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(getApplicationContext(), "Edit failed", Toast.LENGTH_LONG).show();
                                myDialog.dismiss();
//                                upload_text.setVisibility(View.GONE);
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), "Error, check network connectivity", Toast.LENGTH_SHORT).show();
                            System.out.println("Error message = "+error.getMessage());
                            myDialog.dismiss();
//                            upload_text.setVisibility(View.GONE);
                        }
                    }) {

                /*
                 * If you want to add more parameters with the image
                 * you can do it here
                 * here we have only one parameter with the image
                 * which is tags
                 * */
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("email", myEmail);
                    params.put("resid", resID);
                    params.put("fileDescription", resdesc);
                    params.put("fileName", resname);
                    return params;
                }

                /*
                 *pass files using below method
                 * */
                @Override
                protected Map<String, DataPart> getByteData() {
                    Map<String, DataPart> params = new HashMap<>();
                    params.put("myfile", new DataPart(pdfname ,inputData));
                    return params;
                }
            };


            volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(
                    0,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            rQueue = Volley.newRequestQueue(EditResources.this);
            rQueue.add(volleyMultipartRequest);



        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    private void createNotificationChannel(String type) {
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = manager.getNotificationChannel(CHANNEL_ID);
            if(channel == null){
                channel = new NotificationChannel(CHANNEL_ID, "Resource Edited", NotificationManager.IMPORTANCE_DEFAULT);
                //config notification channel
                channel.setDescription("[Channel Description]");
                channel.enableVibration(true);
                channel.enableLights(true);
                channel.setVibrationPattern(new long[]{100, 1000, 200, 340});
                channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
                manager.createNotificationChannel(channel);
            }
        }
        Intent notificationIntent = new Intent(EditResources.this, VideoCreatorView.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent penIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.logo)
                .setStyle(new NotificationCompat.BigTextStyle())
                .setContentTitle("Resource Edited")
                .setContentText("You have successfully edited a "+type+" resource in your group - \""+groupName+"\"")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(false)
                .setTicker("Notification");
        builder.setContentIntent(penIntent);
        NotificationManagerCompat m = NotificationManagerCompat.from(EditResources.this);
        m.notify(6, builder.build());
    }
}