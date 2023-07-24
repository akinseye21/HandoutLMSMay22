package com.example.handoutlms.adapters;

import android.animation.Animator;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.example.handoutlms.R;
import com.example.handoutlms.activities.ApprovedGigUserView;
import com.example.handoutlms.activities.SeeBids;
import com.example.handoutlms.activities.ViewCv;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class SeeBidsAdapter extends BaseAdapter {
    SeeBids seeBids;
    private ArrayList<String> biddername;
    private ArrayList<String> bidderemail;
    private ArrayList<String> bidstatus;
    private ArrayList<String> bidamount;
    private ArrayList<String> bidid;
    private ArrayList<String> gigid;
    private ArrayList<String> cv;
    private ArrayList<String> pp;
    private String gigname;

    Animation slideLeft;
    Dialog myDialog, myDialog2, loadingDialog;
    CardView cardView;
//    public RadioGroup radioGroup;
//    public RadioButton inProgress;
//    public RadioButton gigCompleted;

    public static final String BID_REJECT = "https://handoutng.com/handouts/handout_bid_reject";
    public static final String BID_APPROVE = "https://handoutng.com/handouts/handout_bid_approve";

    public SeeBidsAdapter(SeeBids context, ArrayList<String> biddername, ArrayList<String> bidderemail, ArrayList<String> bidstatus, ArrayList<String> bidamount, ArrayList<String> bidid, ArrayList<String> gigid, ArrayList<String> cv, ArrayList<String> pp, String gigname){
        //Getting all the values
        seeBids = (SeeBids) context;
//        this.context = context;
        this.biddername = biddername;
        this.bidderemail = bidderemail;
        this. bidstatus = bidstatus;
        this.bidamount = bidamount;
        this.bidid = bidid;
        this.gigid = gigid;
        this.cv = cv;
        this.pp = pp;
        this.gigname = gigname;
    }


    @Override
    public int getCount() {
        return biddername.size();
    }

    @Override
    public Object getItem(int position) {
        return biddername.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        LayoutInflater inflaInflater = (LayoutInflater) this.seeBids.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = inflaInflater.inflate(R.layout.list_bids, parent, false);
        }

        //codes
        CircleImageView profpic = convertView.findViewById(R.id.pp);
        TextView name = convertView.findViewById(R.id.name);
        TextView email = convertView.findViewById(R.id.email);
        TextView txtamount = convertView.findViewById(R.id.txtamount);
        LinearLayout cvlay = convertView.findViewById(R.id.cv);
        LinearLayout amount = convertView.findViewById(R.id.amount);
        LinearLayout accept = convertView.findViewById(R.id.accept);
        LinearLayout reject = convertView.findViewById(R.id.reject);
        ImageView circle_save = convertView.findViewById(R.id.circle_save);
        cardView = convertView.findViewById(R.id.card);


        slideLeft = AnimationUtils.loadAnimation(seeBids, R.anim.slide_left);

        //setting views
        email.setText(bidderemail.get(position));
        txtamount.setText(bidamount.get(position));
        name.setText(biddername.get(position));
        Glide.with(seeBids).load(pp.get(position)).into(profpic);

        cvlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open CV in PDF view
                Intent m = new Intent(seeBids, ViewCv.class);
                m.putExtra("biddername", biddername.get(position));
                m.putExtra("cv", cv.get(position));
                m.putExtra("bidderemail", bidderemail.get(position));
                m.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                seeBids.startActivity(m);
            }
        });

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog2 = new Dialog(seeBids);
                myDialog2.setContentView(R.layout.custom_save_bid);
                LinearLayout close = myDialog2.findViewById(R.id.close);
                Button select = myDialog2.findViewById(R.id.select);
                Button save = myDialog2.findViewById(R.id.save);

                select.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        loadingDialog = new Dialog(seeBids);
                        loadingDialog.setContentView(R.layout.custom_popup_login_loading);
                        TextView txt = loadingDialog.findViewById(R.id.text);
                        txt.setText("Loading, please wait...");
                        loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        loadingDialog.setCanceledOnTouchOutside(false);
                        loadingDialog.show();
                        //select the bid
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, BID_APPROVE,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        System.out.println("Approved response = "+response);

                                        try {
                                            JSONArray jsonArray = new JSONArray(response);
                                            int bid_len = jsonArray.length();

                                            for (int i = 0; i < bid_len; i++) {
                                                JSONObject section = jsonArray.getJSONObject(i);
                                                String bidStatus = section.getString("bidstatus");

                                                if (bidStatus.equals("approved")){
                                                    //keep the view
                                                    myDialog2.dismiss();
                                                    loadingDialog.dismiss();
                                                    Toast.makeText(seeBids, "Approval successful", Toast.LENGTH_SHORT).show();
                                                    //move to the next activity
                                                    Intent intent = new Intent(seeBids, ApprovedGigUserView.class);
                                                    intent.putExtra("biddername", biddername.get(position));
                                                    intent.putExtra("bidderemail", bidderemail.get(position));
                                                    intent.putExtra("bidamount", bidamount.get(position));
                                                    intent.putExtra("cv", cv.get(position));
                                                    intent.putExtra("pp", pp.get(position));
                                                    intent.putExtra("gigname", gigname);
                                                    seeBids.startActivity(intent);
//                                                    seeBids.returnFromAdapter(1);
                                                }else{
                                                    myDialog2.dismiss();
                                                    loadingDialog.dismiss();
                                                    Toast.makeText(seeBids, "Approval Failed", Toast.LENGTH_SHORT).show();
                                                }


                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                            myDialog2.dismiss();
                                            loadingDialog.dismiss();
                                            Toast.makeText(seeBids, "Failed exception", Toast.LENGTH_SHORT).show();

                                        }

                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError volleyError) {
                                        volleyError.printStackTrace();
                                        myDialog2.dismiss();
                                        loadingDialog.dismiss();
                                        Toast.makeText(seeBids, "Network Error!", Toast.LENGTH_SHORT).show();

                                    }
                                }){
                            @Override
                            protected Map<String, String> getParams(){
                                Map<String, String> params = new HashMap<>();
                                params.put("gigid", gigid.get(position));
                                params.put("bidid", bidid.get(position));
                                return params;
                            }
                        };

                        RequestQueue requestQueue = Volley.newRequestQueue(seeBids);
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
                });
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        myDialog2.dismiss();
                    }
                });
                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Drawable desiredDrawable = seeBids.getResources().getDrawable(R.drawable.circle_green);
                        Drawable imageViewDrawable = circle_save.getDrawable();
                        if (imageViewDrawable != null && imageViewDrawable.getConstantState().equals(desiredDrawable.getConstantState())) {
                            // The ImageView has the desired drawable
                            circle_save.setImageResource(R.drawable.circle_green);
                        } else {
                            // The ImageView does not have the desired drawable
                            circle_save.setImageResource(R.drawable.circle_transparent);
                        }


                        //highlight the card as green bg
//                        circle_save.setImageResource(R.drawable.circle_green);
                        myDialog2.dismiss();
                    }
                });

                myDialog2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                myDialog2.setCanceledOnTouchOutside(true);
                myDialog2.show();


            }
        });

        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ask if user want to remove card from the list of bids
                myDialog = new Dialog(seeBids);
                myDialog.setContentView(R.layout.custom_remove_bid);
                LinearLayout close1 = myDialog.findViewById(R.id.close1);
                Button close = myDialog.findViewById(R.id.close);
                Button remove = myDialog.findViewById(R.id.remove);

                remove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        loadingDialog = new Dialog(seeBids);
                        loadingDialog.setContentView(R.layout.custom_popup_login_loading);
                        TextView txt = loadingDialog.findViewById(R.id.text);
                        txt.setText("Loading, please wait...");
                        loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        loadingDialog.setCanceledOnTouchOutside(false);
                        loadingDialog.show();
                        //animate the card to delete right
                        //delete from the entry of bids
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, BID_REJECT,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        System.out.println("Rejected response = "+response);
                                        myDialog.dismiss();
                                        loadingDialog.dismiss();
                                        try {
                                            JSONArray jsonArray = new JSONArray(response);
                                            int bid_len = jsonArray.length();

                                            for (int i = 0; i < bid_len; i++) {
                                                JSONObject section = jsonArray.getJSONObject(i);
                                                String bidStatus = section.getString("status");
                                                if (bidStatus.equals("success")){
                                                    seeBids.recreate();
                                                }
                                            }

//                                            seeBids.returnFromAdapter(bid_len);
                                        } catch (JSONException e) {
//                                    e.printStackTrace();
                                            try {
                                                JSONObject jsonObject = new JSONObject(response);
                                                String status = jsonObject.getString("status");

                                                if (status.equals("no gig found")){
//                                                    seeBids.returnFromAdapter(0);
//                                            radioGroup.setVisibility(View.GONE);
                                                }

                                            } catch (JSONException ex) {
                                                ex.printStackTrace();
                                            }
                                        }

                                        cardView.startAnimation(slideLeft);
                                        cardView.animate().alpha(0.0f).setDuration(500).setListener(new Animator.AnimatorListener() {
                                            @Override
                                            public void onAnimationStart(Animator animation) {

                                            }

                                            @Override
                                            public void onAnimationEnd(Animator animation) {
                                                cardView.setVisibility(View.GONE);
                                                notifyDataSetChanged();
                                            }

                                            @Override
                                            public void onAnimationCancel(Animator animation) {

                                            }

                                            @Override
                                            public void onAnimationRepeat(Animator animation) {

                                            }
                                        });



                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError volleyError) {
                                        volleyError.printStackTrace();
                                        myDialog.dismiss();
                                        loadingDialog.dismiss();
                                    }
                                }){
                            @Override
                            protected Map<String, String> getParams(){
                                Map<String, String> params = new HashMap<>();
                                params.put("gigid", gigid.get(position));
                                params.put("bidid", bidid.get(position));
                                return params;
                            }
                        };

                        RequestQueue requestQueue = Volley.newRequestQueue(seeBids);
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
                });
                close1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        myDialog.dismiss();
                    }
                });
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myDialog.dismiss();
                    }
                });

                myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                myDialog.setCanceledOnTouchOutside(true);
                myDialog.show();

            }
        });


        return convertView;
    }



}
