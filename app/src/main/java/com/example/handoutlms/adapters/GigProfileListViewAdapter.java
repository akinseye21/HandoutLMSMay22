package com.example.handoutlms.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
import com.example.handoutlms.activities.ApprovedGigView;
import com.example.handoutlms.activities.ShowCreatedGigs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GigProfileListViewAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<String> gigName;
    private ArrayList<String> gigPrice;
    private ArrayList<String> gigType;
    private ArrayList<String> gigSkills;
    private ArrayList<String> gigPaymentMode;
    private ArrayList<String> gigDescription;
    private ArrayList<String> startDate;
    private ArrayList<String> endDate;
    private ArrayList<String> gigId;
    private ArrayList<String> gigStatus;
    private String info;
    SharedPreferences preferences;
    String email;

    String biddername = "";
    String bidderemail ="";
    String bidamount="";
    String bidid="";
    String CV="";
    String profile_pic="";

    public static final String GET_ALL_BIDS = "https://handoutng.com/handouts/handout_get_all_bids_for_gig";

    public GigProfileListViewAdapter(Context context, ArrayList<String> gigName, ArrayList<String> gigPrice, ArrayList<String> gigType, ArrayList<String> gigSkills, ArrayList<String> gigPaymentMode, ArrayList<String> gigDescription, ArrayList<String> startDate, ArrayList<String> endDate, ArrayList<String> gigId, ArrayList<String> gigStatus, String info){
        //Getting all the values
        this.context = context;
        this.gigName = gigName;
        this.gigPrice = gigPrice;
        this.gigType = gigType;
        this.gigSkills = gigSkills;
        this.gigPaymentMode = gigPaymentMode;
        this.gigDescription = gigDescription;
        this.startDate = startDate;
        this.endDate = endDate;
        this.gigId = gigId;
        this.gigStatus = gigStatus;
        this.info = info;
    }

    @Override
    public int getCount() {
        return gigName.size();
    }

    @Override
    public Object getItem(int position) {
        return gigName.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflaInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = inflaInflater.inflate(R.layout.list_gig, parent, false);
        }

        preferences = context.getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        email = preferences.getString("email", "not available");

        TextView name = convertView.findViewById(R.id.gig_name);
        TextView price = convertView.findViewById(R.id.gig_price);
        TextView typ = convertView.findViewById(R.id.gig_type);
        ImageView img = convertView.findViewById(R.id.img);
        LinearLayout linlay = convertView.findViewById(R.id.lin_lay);

        name.setText(gigName.get(position));
        price.setText(gigPrice.get(position));
        typ.setText(gigType.get(position));

        if(info.equals("myProfile")){
            linlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, ShowCreatedGigs.class);
                    i.putExtra("gigname", gigName.get(position));
                    i.putExtra("gigdescription", gigDescription.get(position));
                    i.putExtra("gigskills", gigSkills.get(position));
                    i.putExtra("gigprice", gigPrice.get(position));
                    i.putExtra("startDate", startDate.get(position));
                    i.putExtra("endDate", endDate.get(position));
                    i.putExtra("gigId", gigId.get(position));
                    context.startActivity(i);
                }
            });

        }

        else if(info.equals("otherProfile")){

        }

        else if(info.equals("myBid")){

            if (gigStatus.get(position).equals("pending")){
                img.setImageResource(R.drawable.gig_pending);
                linlay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context, "This gig is pending, please check back", Toast.LENGTH_LONG).show();
                    }
                });
            }else if (gigStatus.get(position).equals("rejected")){
                img.setImageResource(R.drawable.gig_rejected);
                linlay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context, "This gig has been rejected by the creator", Toast.LENGTH_LONG).show();
                    }
                });
            }else if (gigStatus.get(position).equals("approved")){
                img.setImageResource(R.drawable.ic192);
                linlay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        //get the bidder info for the bid
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, GET_ALL_BIDS,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {

                                        try{
                                            JSONArray jsonArray = new JSONArray(response);
                                            int bid_len = jsonArray.length();
                                            for(int j=0; j<bid_len; j++){
                                                JSONObject jsonObject = jsonArray.getJSONObject(j);

                                                bidderemail = jsonObject.getString("biddermail");
                                                if (bidderemail.equals(email)){
                                                    biddername = jsonObject.getString("biddername");
                                                    bidamount = jsonObject.getString("bidamount");
                                                    bidid = jsonObject.getString("bidid");
                                                    CV = jsonObject.getString("cv");
                                                    profile_pic = jsonObject.getString("profile_pic");

                                                    //go to info page
                                                    Intent intent = new Intent(context, ApprovedGigView.class);
                                                    intent.putExtra("biddername", biddername);
                                                    intent.putExtra("bidderemail", bidderemail);
                                                    intent.putExtra("bidamount", bidamount);
                                                    intent.putExtra("cv", CV);
                                                    intent.putExtra("pp", profile_pic);
                                                    intent.putExtra("gigname", gigName.get(position));
                                                    intent.putExtra("gigdescription", gigDescription.get(position));
                                                    intent.putExtra("gigskills", gigSkills.get(position));
                                                    intent.putExtra("gigprice", gigPrice.get(position));
                                                    intent.putExtra("gigid", gigId.get(position));
                                                    intent.putExtra("startdate", startDate.get(position));
                                                    intent.putExtra("enddate", endDate.get(position));
                                                    context.startActivity(intent);
                                                }
                                            }

                                        }
                                        catch (JSONException e){

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
                                params.put("gigid", gigId.get(position));
                                return params;
                            }
                        };

                        RequestQueue requestQueue = Volley.newRequestQueue(context);
                        requestQueue.add(stringRequest);
                        DefaultRetryPolicy retryPolicy = new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                        stringRequest.setRetryPolicy(retryPolicy);
                        requestQueue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
                            @Override
                            public void onRequestFinished(Request<Object> request) {
                                requestQueue.getCache().clear();
                            }
                        });

                    }
                });
            }
        }

        return convertView;
    }
}
