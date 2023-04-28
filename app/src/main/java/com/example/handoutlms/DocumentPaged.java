package com.example.handoutlms;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DocumentPaged extends AppCompatActivity {

    String name, group_name;
    TextView upload_default, upload_btn;
    SharedPreferences preferences;
    ImageView back;
    Dialog myDialog, myDialog2;

    TextView groupName;
//    TextView upload_text;
//    ProgressBar progressBar;
    LinearLayout loading;
    EditText pdfName, pdfDesc;

    private static final String ROOT_URL = "http://handoutng.com/handouts/handout_update_tutorial_group";
    private RequestQueue rQueue;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document_paged);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //get sharedpreference
        preferences = getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        email = preferences.getString("email", "not available");

        Intent i = getIntent();
        name = i.getStringExtra("name");
        group_name = i.getStringExtra("group_name");

        upload_default = findViewById(R.id.upload_default);
        upload_btn = findViewById(R.id.upload_button);
//        file_path = findViewById(R.id.file_path);
        back = findViewById(R.id.back);
        loading = findViewById(R.id.loading);
        groupName = findViewById(R.id.groupNAME);
//        upload_text = findViewById(R.id.upload_text);
//        progressBar = findViewById(R.id.progressBar);
        pdfName = findViewById(R.id.edtPdfName);
        pdfDesc = findViewById(R.id.edtPdfDesc);

        groupName.setText(group_name);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });



        upload_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("application/pdf");
                startActivityForResult(intent,1);

            }
        });

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

        myDialog = new Dialog(DocumentPaged.this);
        myDialog.setContentView(R.layout.custom_popup_login_loading);
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView text = myDialog.findViewById(R.id.text);
        text.setText("Uploading file, please wait");
        myDialog.setCanceledOnTouchOutside(false);
        myDialog.show();

//        loading.setVisibility(View.VISIBLE);
//        upload_text.setVisibility(View.VISIBLE);

        InputStream iStream = null;
        try {

            iStream = getContentResolver().openInputStream(pdffile);
            final byte[] inputData = getBytes(iStream);

            VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, ROOT_URL,
                    new Response.Listener<NetworkResponse>() {
                        @Override
                        public void onResponse(NetworkResponse response) {
                            Log.d("ressssssoo",new String(response.data));
                            rQueue.getCache().clear();
                            try {
                                JSONObject jsonObject = new JSONObject(new String(response.data));
                                String status = jsonObject.getString("status");

                                if(status.equals("successful")){
//                                    Toast.makeText(getApplicationContext(), "File Uploaded successfully", Toast.LENGTH_LONG).show();
                                    System.out.println("Status = "+status);

                                    myDialog.dismiss();

                                    myDialog2 = new Dialog(DocumentPaged.this);
                                    myDialog2.setContentView(R.layout.custom_popup_upload_successful);
                                    Button addmore = myDialog2.findViewById(R.id.addmore);
                                    Button viewgroup = myDialog2.findViewById(R.id.viewgroup);
                                    addmore.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            onBackPressed();
                                            myDialog2.dismiss();
                                        }
                                    });
                                    viewgroup.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            // go to the view resources page
                                            Intent i = new Intent(DocumentPaged.this, VideoCreatorView.class);
                                            i.putExtra("groupName", group_name);
                                            startActivity(i);
                                        }
                                    });
                                    myDialog2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                    myDialog2.setCanceledOnTouchOutside(true);
                                    myDialog2.show();
//                                    loading.setVisibility(View.GONE);
//                                    upload_text.setVisibility(View.GONE);
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(getApplicationContext(), "Uploading failed", Toast.LENGTH_LONG).show();

                                myDialog.dismiss();
//                                loading.setVisibility(View.GONE);
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
//                            loading.setVisibility(View.GONE);
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
                    params.put("email", email);
                    params.put("tutorial_group_name", group_name);
                    params.put("fileDescription", pdfDesc.getText().toString());
                    params.put("fileName", pdfName.getText().toString());
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
            rQueue = Volley.newRequestQueue(DocumentPaged.this);
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

}
