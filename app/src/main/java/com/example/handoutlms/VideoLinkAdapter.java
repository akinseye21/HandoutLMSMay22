package com.example.handoutlms;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class VideoLinkAdapter extends BaseAdapter {


    Context context;
    String groupName;
    ArrayList<String> arr_fileUrl;
    ArrayList<String> arr_fileDescription;
    ArrayList<String> arr_thumbnail;
    ArrayList<String> arr_fileName;
    ArrayList<String> arr_resID;
    String from;
    LayoutInflater inflter;
    Dialog myDialog, myDialog2;

    String videoId = "";

    SharedPreferences preferences;
    String myEmail;


    public static final String INCREASE_COUNT = "https://handoutng.com/handouts/handout_add_resource_hits";
    public static final String DELETE_RESOURCE = "https://handoutng.com/handouts/handout_delete_resource";


    public VideoLinkAdapter(Context applicationContext, String groupName, ArrayList<String> arr_fileUrl, ArrayList<String> arr_fileDescription, ArrayList<String> arr_thumbnail, ArrayList<String> arr_fileName, ArrayList<String> arr_resID, String from){
        this.context = applicationContext;
        this.groupName = groupName;
        this.arr_fileDescription = arr_fileDescription;
        this.arr_fileUrl = arr_fileUrl;
        this.arr_thumbnail = arr_thumbnail;
        this.arr_fileName = arr_fileName;
        this.arr_resID = arr_resID;
        this.from = from;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return arr_fileDescription.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {

        convertView = inflter.inflate(R.layout.list_videolink_creator, null);
        LinearLayout loading = convertView.findViewById(R.id.loading);

        TextView groupNAME = convertView.findViewById(R.id.groupName);
        TextView description = convertView.findViewById(R.id.description);

        LinearLayout popup = convertView.findViewById(R.id.popup);
        ImageView dots = convertView.findViewById(R.id.dots);
        ImageView imageView = convertView.findViewById(R.id.img);
        Button edit = convertView.findViewById(R.id.edit);
        Button delete = convertView.findViewById(R.id.delete);
        TextView close = convertView.findViewById(R.id.close);
        RelativeLayout card = convertView.findViewById(R.id.card);

        preferences = context.getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        myEmail = preferences.getString("email", "not available");

        if (from.equals("viewer")){
            dots.setVisibility(View.GONE);
        }


        dots.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                myDialog2 = new Dialog(viewGroup.getContext());
//                myDialog2.setContentView(R.layout.custom_popup_edit_delete);
//                // Setting dialogview
//                Window window = myDialog2.getWindow();
//                window.setGravity(Gravity.RIGHT|Gravity.TOP);
//
//                TextView close = myDialog2.findViewById(R.id.close);
//                Button edit = myDialog2.findViewById(R.id.edit);
//                Button delete = myDialog2.findViewById(R.id.delete);
//
//                myDialog2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                myDialog2.setCanceledOnTouchOutside(true);
//                myDialog2.show();


                popup.setVisibility(View.VISIBLE);
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        myDialog2.dismiss();
                        popup.setVisibility(View.GONE);
                    }
                });
                edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        popup.setVisibility(View.GONE);
//                        myDialog2.dismiss();

                        Intent intent = new Intent(context, EditResources.class);
                        intent.putExtra("groupName", groupName);
                        intent.putExtra("fileName", arr_fileName.get(i));
                        intent.putExtra("fileDesc", arr_fileDescription.get(i));
                        intent.putExtra("thumbnail", arr_thumbnail.get(i));
                        intent.putExtra("fileURL", arr_fileUrl.get(i));
                        intent.putExtra("resID", arr_resID.get(i));
                        context.startActivity(intent);
                    }
                });
                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        popup.setVisibility(View.GONE);
//                        myDialog2.dismiss();

                        myDialog = new Dialog(context);
                        myDialog.setContentView(R.layout.custom_popup_exit);
                        // Setting dialogview
                        Window window = myDialog.getWindow();
                        window.setGravity(Gravity.CENTER);
                        //getting dialog view
                        TextView text1 = myDialog.findViewById(R.id.text1);
                        TextView text2 = myDialog.findViewById(R.id.text2);
                        Button delete = myDialog.findViewById(R.id.no);
                        Button edit = myDialog.findViewById(R.id.yes);
                        ProgressBar progressBar = myDialog.findViewById(R.id.progressBar);

                        text1.setText("Are you sure you want to delete this tutorial resource?");
                        text2.setText("You can as well edit the resource file");
                        delete.setText("Delete");
                        edit.setText("Edit");

                        delete.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //show loader
                                progressBar.setVisibility(View.VISIBLE);
                                delete.setVisibility(View.GONE);
                                edit.setVisibility(View.GONE);
                                //delete resource file
                                StringRequest stringRequest = new StringRequest(Request.Method.POST, DELETE_RESOURCE,
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {

                                                try{
                                                    JSONObject update = new JSONObject(response);
                                                    String status = update.getString("status");
                                                    if (status.equals("success")){
                                                        //show popup of successful
                                                        progressBar.setVisibility(View.GONE);
                                                        edit.setVisibility(View.VISIBLE);
                                                        text1.setText("Delete was successful");
                                                        text2.setVisibility(View.GONE);
                                                        edit.setText("OK");
                                                        edit.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View view) {
                                                                myDialog.dismiss();
                                                            }
                                                        });
                                                    }else{
                                                        //toast error deleting
                                                        Toast.makeText(context, "Error deleting resource file", Toast.LENGTH_SHORT).show();
                                                        progressBar.setVisibility(View.GONE);
                                                        delete.setVisibility(View.GONE);
                                                        edit.setVisibility(View.GONE);
                                                        text1.setText("Error deleting");
                                                        text2.setText("");
                                                        edit.setText("OK");
                                                        edit.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View view) {
                                                                myDialog.dismiss();
                                                            }
                                                        });
                                                    }

                                                }
                                                catch (JSONException e){
                                                    progressBar.setVisibility(View.GONE);
                                                    myDialog.dismiss();
                                                    Toast.makeText(context, "Error deleting resource file", Toast.LENGTH_SHORT).show();
                                                    e.printStackTrace();
                                                }

                                            }
                                        },
                                        new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError volleyError) {
                                                progressBar.setVisibility(View.GONE);
                                                myDialog.dismiss();
                                                Toast.makeText(context, "Error deleting resource file", Toast.LENGTH_SHORT).show();
                                                volleyError.printStackTrace();
                                            }
                                        }){
                                    @Override
                                    protected Map<String, String> getParams(){
                                        Map<String, String> params = new HashMap<>();
                                        params.put("email", myEmail);
                                        params.put("resid", arr_resID.get(i));
                                        return params;
                                    }
                                };

                                RequestQueue requestQueue = Volley.newRequestQueue(context);
                                DefaultRetryPolicy retryPolicy = new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                                stringRequest.setRetryPolicy(retryPolicy);
                                requestQueue.add(stringRequest);
                            }
                        });
                        edit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                myDialog.dismiss();

                                Intent intent = new Intent(context, EditResources.class);
                                intent.putExtra("groupName", groupName);
                                intent.putExtra("fileName", arr_fileName.get(i));
                                intent.putExtra("fileDesc", arr_fileDescription.get(i));
                                intent.putExtra("thumbnail", arr_thumbnail.get(i));
                                intent.putExtra("fileURL", arr_fileUrl.get(i));
                                context.startActivity(intent);
                            }
                        });


                        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        myDialog.setCanceledOnTouchOutside(false);
                        myDialog.show();
                    }
                });

            }
        });
        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popup.setVisibility(View.GONE);

                //send to the watch video activity
                Intent intent = new Intent(context, WatchVideo.class);
                intent.putExtra("name", arr_fileName.get(i));
                intent.putExtra("link", arr_fileUrl.get(i));
                context.startActivity(intent);

                // increase the count of resource view by 1
                StringRequest stringRequest = new StringRequest(Request.Method.POST, INCREASE_COUNT,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                System.out.println("Increase count = "+response);

                                try{
                                    JSONObject update = new JSONObject(response);
                                    String status = update.getString("status");

                                }
                                catch (JSONException e){
                                    e.printStackTrace();
                                }

                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                                volleyError.printStackTrace();
                            }
                        }){
                    @Override
                    protected Map<String, String> getParams(){
                        Map<String, String> params = new HashMap<>();
                        params.put("email", myEmail);
                        params.put("resid", arr_resID.get(i));
                        return params;
                    }
                };

                RequestQueue requestQueue = Volley.newRequestQueue(context);
                DefaultRetryPolicy retryPolicy = new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                stringRequest.setRetryPolicy(retryPolicy);
                requestQueue.add(stringRequest);
            }
        });


        groupNAME.setText(arr_fileName.get(i));
        description.setText(arr_fileDescription.get(i));
        Glide.with(context)
                .load(arr_thumbnail.get(i))
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        loading.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(imageView);


        return convertView;
    }


}
