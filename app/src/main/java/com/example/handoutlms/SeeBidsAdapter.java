package com.example.handoutlms;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SeeBidsAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<String> biddername;
    private ArrayList<String> bidderemail;
    private ArrayList<String> bidstatus;
    private ArrayList<String> bidamount;
    private ArrayList<String> bidid;
    private ArrayList<String> gigid;
    private ArrayList<String> cv;

    public static final String BID_REJECT = "http://35.84.44.203/handouts/handout_bid_reject";

    public SeeBidsAdapter(Context context, ArrayList<String> biddername, ArrayList<String> bidderemail, ArrayList<String> bidstatus, ArrayList<String> bidamount, ArrayList<String> bidid, ArrayList<String> gigid, ArrayList<String> cv){
        //Getting all the values
        this.context = context;
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
    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater inflaInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

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
        final CardView cardView = convertView.findViewById(R.id.card);

        //setting views
        name.setText(biddername.get(position));
        department.setText(bidderemail.get(position));
        amount.setText(bidamount.get(position));
        cvlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open CV in PDF view
                Intent m = new Intent(context, ViewCv.class);
                m.putExtra("biddername", biddername.get(position));
                m.putExtra("cv", cv.get(position));
                m.putExtra("bidderemail", bidderemail.get(position));
                m.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(m);
            }
        });
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //highlight the card as green bg
                cardView.setCardBackgroundColor(Color.parseColor("#2000ff00"));
            }
        });
        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //animate the card to delete right
                //delete from the entry of bids
                StringRequest stringRequest = new StringRequest(Request.Method.POST, BID_REJECT,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                System.out.println("Rejected response = "+response);

                                cardView.setVisibility(View.GONE);
//                                try{
//                                    JSONArray jsonArray = new JSONArray(response);
//                                    int bid_len = jsonArray.length();
//                                    bid_count.setText(String.valueOf(bid_len));
//
//                                    for(int j=0; j<bid_len; j++){
//                                        JSONObject jsonObject = jsonArray.getJSONObject(j);
//                                        String biddername = jsonObject.getString("biddername");
//                                        String bidderemail = jsonObject.getString("biddermail");
//                                        String bidstatus = jsonObject.getString("bidstatus");
//                                        String bidamount = jsonObject.getString("bidamount");
//                                        String bidid = jsonObject.getString("bidid");
//                                        String got_gigid = jsonObject.getString("gigid");
//                                        String CV = jsonObject.getString("cv");
//
//                                        Array_biddername.add(biddername);
//                                        Array_bidderemail.add(bidderemail);
//                                        Array_bidstatus.add(bidstatus);
//                                        Array_bidamount.add(bidamount);
//                                        Array_bidid.add(bidid);
//                                        Array_gigid.add(gigid);
//                                        Array_cv.add(CV);
//                                    }
//
//
//
//                                    //populate values on the listview
//                                    SeeBidsAdapter seeBidsAdapter = new SeeBidsAdapter(getApplicationContext(), Array_biddername, Array_bidderemail, Array_bidstatus, Array_bidamount, Array_bidid, Array_gigid, Array_cv);
//                                    my_list.setAdapter(seeBidsAdapter);
//                                }
//                                catch (JSONException e){
////                            e.printStackTrace();
//                                    try {
//                                        JSONObject jsonObject = new JSONObject(response);
//                                        String status = jsonObject.getString("status");
//
//                                        if (status.equals("no gig found")){
//                                            notification_text.setVisibility(View.VISIBLE);
//                                            my_list.setVisibility(View.GONE);
//                                        }
//                                    } catch (JSONException ex) {
//                                        ex.printStackTrace();
//                                    }
//                                }

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

                RequestQueue requestQueue = Volley.newRequestQueue(context);
                requestQueue.add(stringRequest);



            }
        });


        return convertView;
    }
}
