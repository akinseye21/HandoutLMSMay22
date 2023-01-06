package com.example.handoutlms;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class CardGigClick2 extends AppCompatActivity {

    ImageView back;
    String name, department, school, gigName, gigDescription, paymentStructure, id;
    TextView name1, department1, school1, gig_name, gig_description, gig_category, popup_gigName, file_path;
    EditText bidAmount;
    Button finish, close;
    Dialog myDialog;
    LinearLayout uploadCV;
    SharedPreferences preferences;
    String myEmail;

    TextView upload_text;
    ProgressBar progressBar;

    String displayName = null;

    public static final String BID_GIG = "http://handoutng.com/handouts/handout_bid_for_gig";
    private RequestQueue rQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_gig_click2);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent i = getIntent();
        name = i.getStringExtra("name");
        department = i.getStringExtra("department");
        school = i.getStringExtra("school");
        gigName = i.getStringExtra("gig_name");
        gigDescription = i.getStringExtra("gig_description");
        paymentStructure = i.getStringExtra("payment_structure");
        id = i.getStringExtra("id");

        preferences = getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        myEmail = preferences.getString("email", "not available");


        name1 = findViewById(R.id.name);
        department1 = findViewById(R.id.department);
        school1 = findViewById(R.id.uni);
        gig_name = findViewById(R.id.gig_name);
        gig_description = findViewById(R.id.gig_description);
        gig_category = findViewById(R.id.gig_category);
        bidAmount = findViewById(R.id.bidAmount);
        finish = findViewById(R.id.finish);
        uploadCV = findViewById(R.id.uploadCV);
        file_path = findViewById(R.id.file_path);
        upload_text = findViewById(R.id.upload_text);
        progressBar = findViewById(R.id.progressBar);

        name1.setText(name);
        department1.setText(department);
        school1.setText(school);
        gig_name.setText(gigName);
        gig_description.setText(gigDescription);
        gig_category.setText(paymentStructure);

        uploadCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("application/pdf");
                startActivityForResult(intent,1);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            // Get the Uri of the selected file
            final Uri uri = data.getData();
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
                        file_path.setText(displayName);

                        finish.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {


                                progressBar.setVisibility(View.VISIBLE);
                                upload_text.setVisibility(View.VISIBLE);
                                uploadPDF(displayName, uri);

                            }
                        });


                    }
                } finally {
                    cursor.close();
                }
            } else if (uriString.startsWith("file://")) {
                displayName = myFile.getName();
                Log.d("nameeeee>>>>  ",displayName);
                file_path.setText(displayName);
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void uploadPDF(final String pdfname, Uri pdffile){


        InputStream iStream = null;
        try {

            iStream = getContentResolver().openInputStream(pdffile);
            final byte[] inputData = getBytes(iStream);

            VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, BID_GIG,
                    new Response.Listener<NetworkResponse>() {
                        @Override
                        public void onResponse(NetworkResponse response) {
                            Log.d("ressssssoo",new String(response.data));
                            rQueue.getCache().clear();
                            try {
                                JSONObject jsonObject = new JSONObject(new String(response.data));
                                String status = jsonObject.getString("status");

                                progressBar.setVisibility(View.GONE);
                                upload_text.setVisibility(View.GONE);

                                if(status.equals("success")){
                                    myDialog = new Dialog(CardGigClick2.this);
                                    myDialog.setContentView(R.layout.custom_popup_bidplaced);
                                    close = myDialog.findViewById(R.id.close);
                                    popup_gigName = myDialog.findViewById(R.id.gigName);
                                    popup_gigName.setText(gigName);
                                    close.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent i = new Intent(getApplicationContext(), FeedsDashboard.class);
                                            startActivity(i);
//                                            finish();
                                        }
                                    });
                                    myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                    myDialog.setCanceledOnTouchOutside(false);
                                    myDialog.show();

                                }
                                else{
                                    Toast.makeText(getApplicationContext(), "Bid Failed!!", Toast.LENGTH_LONG).show();
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();

                                progressBar.setVisibility(View.GONE);
                                upload_text.setVisibility(View.GONE);
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();

                            progressBar.setVisibility(View.GONE);
                            upload_text.setVisibility(View.GONE);
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
                    params.put("gigid", id);
                    params.put("amount", bidAmount.getText().toString());
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
            rQueue = Volley.newRequestQueue(CardGigClick2.this);
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
