package com.example.handoutlms.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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

public class UploadFile extends AppCompatActivity {

    Intent intent;
    String folderName;
    int folderCount;
    TextView txtfoldername;
    ImageView back;
    LinearLayout publicLayout, privateLayout;
    TextView publicText, privateText, txtfilename;
    EditText edtresourcename, edtdescription;
    RelativeLayout add;
    Button save;
    String filetype = "";
    String filename, filedescription;

    String displayName = "";
    Uri uri;
    private RequestQueue rQueue;

    SharedPreferences preferences;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_file);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //get sharedpreference
        preferences = getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        email = preferences.getString("email", "not available");

        intent = getIntent();
        folderName = intent.getStringExtra("folder_name");
        folderCount = intent.getIntExtra("folder_count", 0);

        txtfoldername = findViewById(R.id.folderName);
        txtfoldername.setText(folderName);

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        publicLayout = findViewById(R.id.publicLayout);
        privateLayout = findViewById(R.id.privateLayout);
        publicText = findViewById(R.id.txtPublic);
        privateText = findViewById(R.id.txtPrivate);
        txtfilename = findViewById(R.id.filename);

        edtresourcename = findViewById(R.id.edtResourceName);
        edtdescription = findViewById(R.id.edtResourceDesc);
        add = findViewById(R.id.add);
        save = findViewById(R.id.save);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filename = edtresourcename.getText().toString().trim();
                filedescription = edtdescription.getText().toString().trim();

                if (filename.equals("") || filedescription.equals("") || displayName.equals("") || filetype.equals("")){
                    Toast.makeText(UploadFile.this, "Please fill all required field and add a document", Toast.LENGTH_SHORT).show();
                }else{
                    uploadPDF(displayName,uri);
                }

            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //select pdf from phone folder
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("application/pdf");
                startActivityForResult(intent,1);
            }
        });
    }


    public void privateClick(View view) {
        privateLayout.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#51C8F2")));
        privateText.setTextColor(Color.parseColor("#51C8F2"));
        publicLayout.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ffffff")));
        publicText.setTextColor(Color.parseColor("#ffffff"));
        filetype = "private";
    }

    public void publicClick(View view) {
        privateLayout.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ffffff")));
        privateText.setTextColor(Color.parseColor("#ffffff"));
        publicLayout.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#51C8F2")));
        publicText.setTextColor(Color.parseColor("#51C8F2"));
        filetype = "public";
    }

    @SuppressLint("Range")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            // Get the Uri of the selected file
            uri = data.getData();
            String uriString = uri.toString();
            File myFile = new File(uriString);
//            String path = myFile.getAbsolutePath();

            if (uriString.startsWith("content://")) {
                Cursor cursor = null;
                try {
                    cursor = this.getContentResolver().query(uri, null, null, null, null);
                    if (cursor != null && cursor.moveToFirst()) {
                        displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                        Log.d("nameeeee>>>>  ",displayName);
                        txtfilename.setText(displayName);
                    }
                } finally {
                    cursor.close();
                }
            } else if (uriString.startsWith("file://")) {
                displayName = myFile.getName();
                Log.d("nameeeee>>>>  ",displayName);
                txtfilename.setText(displayName);
            }
        }

        super.onActivityResult(requestCode, resultCode, data);

    }

    private void uploadPDF(String pdfname, Uri pdffile) {
        Dialog myDialog = new Dialog(UploadFile.this);
        myDialog.setContentView(R.layout.custom_popup_login_loading);
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView text = myDialog.findViewById(R.id.text);
        text.setText("Uploading file, please wait");
        myDialog.setCanceledOnTouchOutside(false);
        myDialog.show();

        InputStream iStream = null;
        try {

            iStream = getContentResolver().openInputStream(pdffile);
            final byte[] inputData = getBytes(iStream);

            VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, "https://handoutng.com/handouts/userdirectory/handout_save_file",
                    new Response.Listener<NetworkResponse>() {
                        @Override
                        public void onResponse(NetworkResponse response) {
                            Log.d("ressssssoo",new String(response.data));
                            rQueue.getCache().clear();
                            try {
                                JSONObject jsonObject = new JSONObject(new String(response.data));
                                String status = jsonObject.getString("status");

                                if (status.equals("success")){
                                    Toast.makeText(getApplicationContext(), "Upload successful", Toast.LENGTH_LONG).show();
                                    myDialog.dismiss();
                                    //go to folder file page
                                    Intent intent = new Intent(getApplicationContext(), FolderFilePage.class);
                                    intent.putExtra("foldername", folderName);
                                    intent.putExtra("foldercount", folderCount+1);
                                    startActivity(intent);
                                    finish();
                                }else{
                                    Toast.makeText(getApplicationContext(), "Uploading failed", Toast.LENGTH_LONG).show();
                                    myDialog.dismiss();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(getApplicationContext(), "Uploading failed", Toast.LENGTH_LONG).show();

                                myDialog.dismiss();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), "Error, check network connectivity", Toast.LENGTH_SHORT).show();
                            System.out.println("Error message = "+error.getMessage());

                            myDialog.dismiss();
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
                    params.put("foldername", folderName);
                    params.put("fileDescription", filedescription);
                    params.put("filetype", filetype);
                    params.put("fname", filename);
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
            rQueue = Volley.newRequestQueue(UploadFile.this);
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