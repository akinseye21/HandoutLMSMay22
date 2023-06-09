package com.example.handoutlms;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class PendingAdapterClass extends BaseAdapter {

    private Context context;
    ArrayList<String> arr_email;
    ArrayList<String> arr_name;
    ArrayList<String> arr_picture;
    String from;
    String id;
    Dialog myDialog;
    TextView emptyList;
    CardView card;


    public static final String APPROVE = "https://handoutng.com/handouts/handout_group_join_approve";
    public static final String DELETE = "https://handoutng.com/handouts/handout_group_join_reject";
    private static final String USERS_TO_JOIN = "https://handoutng.com/handouts/handout_group_join_all";

    public PendingAdapterClass(Context context, ArrayList<String> email, ArrayList<String> name, ArrayList<String> picture, String from, String id){
        //Getting all the values
        this.context = context;
        this.arr_email = email;
        this.arr_name = name;
        this.arr_picture = picture;
        this.from = from;
        this.id = id;
    }


    @Override
    public int getCount() {
        return arr_email.size();
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
    public View getView(int i, View convertView, ViewGroup parent) {

        LayoutInflater inflaInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = inflaInflater.inflate(R.layout.list_request, parent, false);
        }

//        checkListFromAPI();

        card = convertView.findViewById(R.id.card);
        CircleImageView img = convertView.findViewById(R.id.img);
        TextView txtname = convertView.findViewById(R.id.name);
        TextView txtemail = convertView.findViewById(R.id.email);
        ProgressBar progressBar = convertView.findViewById(R.id.progressBar);
        Button accept = convertView.findViewById(R.id.accept);
        Button ignore = convertView.findViewById(R.id.ignore);
        emptyList = convertView.findViewById(R.id.empty_list);

        if(from.equals("approved")){
            accept.setVisibility(View.GONE);
            ignore.setVisibility(View.GONE);
        }

        if (arr_picture.get(i).equals("")){

        }else{
            Glide.with(context).load(arr_picture.get(i)).into(img);
        }

        txtname.setText(arr_name.get(i));
        txtemail.setText(arr_email.get(i));

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // show the loader
                progressBar.setVisibility(View.VISIBLE);
                //approve the email for the tutorial group
                approveEmail(i, progressBar, card);
                //delist the email from the list

            }
        });
        ignore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // show the loader
                progressBar.setVisibility(View.VISIBLE);
                //delete the email for the tutorial group
                deleteEmail(i, progressBar, card);
            }
        });

        return convertView;
    }

    private void deleteEmail(int i, ProgressBar progressBar, CardView card) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, DELETE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("Response approval = "+response);
                        System.out.println("email = "+arr_email.get(i));
                        System.out.println("tid = group_"+id);

                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");

                            if (status.equals("rejected")){
//                                checkListFromAPI();
                                progressBar.setVisibility(View.GONE);
                                card.setVisibility(View.GONE);

                                //show a popup
                                myDialog = new Dialog(context);
                                myDialog.setContentView(R.layout.custom_popup_successful_taskmanager);
                                TextView text = myDialog.findViewById(R.id.status);
                                Button ok = myDialog.findViewById(R.id.home);
                                text.setText("Successfully rejected\n"+arr_email.get(i));
                                ok.setText("Ok");
                                ok.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        myDialog.dismiss();
                                    }
                                });
                                myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                myDialog.setCanceledOnTouchOutside(false);
                                myDialog.show();

                            }else if (status.equals("failed")){
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(context, "Rejection failed", Toast.LENGTH_SHORT).show();
                                //show a popup
                            }

                        }
                        catch (JSONException e){
                            e.printStackTrace();
                            progressBar.setVisibility(View.GONE);
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        volleyError.printStackTrace();
                        progressBar.setVisibility(View.GONE);
                    }
                }){
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("email", arr_email.get(i));
                params.put("tid", "group_"+id);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
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

    private void approveEmail(int i, ProgressBar progressBar, CardView card) {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, APPROVE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("Response approval = "+response);
                        System.out.println("email = "+arr_email.get(i));
                        System.out.println("tid = group_"+id);

                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");

                            if (status.equals("success")){
//                                checkListFromAPI();
                                progressBar.setVisibility(View.GONE);
                                card.setVisibility(View.GONE);

                                //show a popup
                                myDialog = new Dialog(context);
                                myDialog.setContentView(R.layout.custom_popup_successful_taskmanager);
                                TextView text = myDialog.findViewById(R.id.status);
                                Button ok = myDialog.findViewById(R.id.home);
                                text.setText("Approval successful");
                                ok.setText("Ok");
                                ok.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        myDialog.dismiss();
                                    }
                                });
                                myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                myDialog.setCanceledOnTouchOutside(false);
                                myDialog.show();

                            }else if (status.equals("failed")){
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(context, "Approval failed", Toast.LENGTH_SHORT).show();
                                //show a popup
                            }

                        }
                        catch (JSONException e){
                            e.printStackTrace();
                            progressBar.setVisibility(View.GONE);
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        volleyError.printStackTrace();
                        progressBar.setVisibility(View.GONE);
                    }
                }){
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("email", arr_email.get(i));
                params.put("tid", "group_"+id);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
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

    private void checkListFromAPI() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, USERS_TO_JOIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try{
                            JSONArray update = new JSONArray(response);
                            int size = update.length();
                            if (size<1){
                                //show no list
                                emptyList.setVisibility(View.VISIBLE);
                                card.setVisibility(View.GONE);
                            }
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
                params.put("tid", "group_"+id);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        DefaultRetryPolicy retryPolicy = new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);
        requestQueue.add(stringRequest);

    }
}
