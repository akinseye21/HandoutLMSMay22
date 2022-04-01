package com.example.handoutlms;

import android.animation.Animator;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import androidx.transition.Slide;
import androidx.transition.TransitionManager;
import androidx.transition.Transition;
import androidx.transition.Fade;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SeeBidsAdapter extends BaseAdapter {
    SeeBids seeBids;
    private ArrayList<String> biddername;
    private ArrayList<String> bidderemail;
    private ArrayList<String> bidstatus;
    private ArrayList<String> bidamount;
    private ArrayList<String> bidid;
    private ArrayList<String> gigid;
    private ArrayList<String> cv;

    Animation slideLeft;
    Dialog myDialog, myDialog2;
    CardView cardView;
//    public RadioGroup radioGroup;
//    public RadioButton inProgress;
//    public RadioButton gigCompleted;

    public static final String BID_REJECT = "http://35.84.44.203/handouts/handout_bid_reject";
    public static final String BID_APPROVE = "http://35.84.44.203/handouts/handout_bid_approve";

    public SeeBidsAdapter(SeeBids context, ArrayList<String> biddername, ArrayList<String> bidderemail, ArrayList<String> bidstatus, ArrayList<String> bidamount, ArrayList<String> bidid, ArrayList<String> gigid, ArrayList<String> cv){
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
        TextView name = convertView.findViewById(R.id.name);
        TextView department = convertView.findViewById(R.id.department);
        TextView school = convertView.findViewById(R.id.school);
        LinearLayout cvlay = convertView.findViewById(R.id.cv);
        TextView amount = convertView.findViewById(R.id.amount);
        ImageView accept = convertView.findViewById(R.id.accept);
        ImageView reject = convertView.findViewById(R.id.reject);
        cardView = convertView.findViewById(R.id.card);


        slideLeft = AnimationUtils.loadAnimation(seeBids, R.anim.slide_left);

        //setting views
        name.setText(biddername.get(position));
        department.setText(bidderemail.get(position));
        amount.setText(bidamount.get(position));
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
                Button select = myDialog2.findViewById(R.id.select);
                Button save = myDialog2.findViewById(R.id.save);

                select.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        myDialog2.dismiss();
                        //select the bid
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, BID_APPROVE,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        System.out.println("Rejected response = "+response);

                                        try {
                                            JSONArray jsonArray = new JSONArray(response);
                                            int bid_len = jsonArray.length();

                                            for (int i = 0; i < bid_len; i++) {
                                                JSONObject section = jsonArray.getJSONObject(i);
                                                String bidStatus = section.getString("bidstatus");

                                                if (bidStatus.equals("approved")){
                                                    //keep the view
                                                    seeBids.returnFromAdapter(1);
                                                }else{



//                                                    //animate the card to delete right
//                                                    //delete from the entry of bids
//                                                    StringRequest stringRequest = new StringRequest(Request.Method.POST, BID_REJECT,
//                                                            new Response.Listener<String>() {
//                                                                @Override
//                                                                public void onResponse(String response) {
//                                                                    System.out.println("Rejected response = "+response);
//
//                                                                    try {
//                                                                        JSONArray jsonArray = new JSONArray(response);
//                                                                        int bid_len = jsonArray.length();
//
//                                                                        seeBids.returnFromAdapter(bid_len);
//                                                                    } catch (JSONException e) {
////                                    e.printStackTrace();
//                                                                        try {
//                                                                            JSONObject jsonObject = new JSONObject(response);
//                                                                            String status = jsonObject.getString("status");
//
//                                                                            if (status.equals("no gig found")){
//                                                                                seeBids.returnFromAdapter(0);
////                                            radioGroup.setVisibility(View.GONE);
//                                                                            }
//
//                                                                        } catch (JSONException ex) {
//                                                                            ex.printStackTrace();
//                                                                        }
//                                                                    }
//
//                                                                    cardView.startAnimation(slideLeft);
//                                                                    cardView.animate().alpha(0.0f).setDuration(500).setListener(new Animator.AnimatorListener() {
//                                                                        @Override
//                                                                        public void onAnimationStart(Animator animation) {
//
//                                                                        }
//
//                                                                        @Override
//                                                                        public void onAnimationEnd(Animator animation) {
//                                                                            cardView.setVisibility(View.GONE);
//                                                                            notifyDataSetChanged();
//                                                                        }
//
//                                                                        @Override
//                                                                        public void onAnimationCancel(Animator animation) {
//
//                                                                        }
//
//                                                                        @Override
//                                                                        public void onAnimationRepeat(Animator animation) {
//
//                                                                        }
//                                                                    });
//
//
//
//                                                                }
//                                                            },
//                                                            new Response.ErrorListener() {
//                                                                @Override
//                                                                public void onErrorResponse(VolleyError volleyError) {
//                                                                    volleyError.printStackTrace();
//
//                                                                }
//                                                            }){
//                                                        @Override
//                                                        protected Map<String, String> getParams(){
//                                                            Map<String, String> params = new HashMap<>();
//                                                            params.put("gigid", gigid.get(position));
//                                                            params.put("bidid", bidid.get(position));
//                                                            return params;
//                                                        }
//                                                    };
//
//                                                    RequestQueue requestQueue = Volley.newRequestQueue(seeBids);
//                                                    requestQueue.add(stringRequest);

                                                }


                                            }
                                        } catch (JSONException e) {
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
                                params.put("gigid", gigid.get(position));
                                params.put("bidid", bidid.get(position));
                                return params;
                            }
                        };

                        RequestQueue requestQueue = Volley.newRequestQueue(seeBids);
                        requestQueue.add(stringRequest);
                    }
                });

                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //highlight the card as green bg
                        cardView.setCardBackgroundColor(Color.parseColor("#2000ff00"));
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
                Button close = myDialog.findViewById(R.id.close);
                Button remove = myDialog.findViewById(R.id.remove);
                remove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //animate the card to delete right
                        //delete from the entry of bids
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, BID_REJECT,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        System.out.println("Rejected response = "+response);

                                        try {
                                            JSONArray jsonArray = new JSONArray(response);
                                            int bid_len = jsonArray.length();

                                            seeBids.returnFromAdapter(bid_len);
                                        } catch (JSONException e) {
//                                    e.printStackTrace();
                                            try {
                                                JSONObject jsonObject = new JSONObject(response);
                                                String status = jsonObject.getString("status");

                                                if (status.equals("no gig found")){
                                                    seeBids.returnFromAdapter(0);
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
                        requestQueue.add(stringRequest);

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
