package com.example.handoutlms.adapters;

import static com.example.handoutlms.adapters.HomeListViewAdapter.GIGS_BIDDED;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
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
import com.bumptech.glide.Glide;
import com.example.handoutlms.R;
import com.example.handoutlms.activities.CardGigClick2;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class GigListViewAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<String> gigName;
    private ArrayList<String> gigPrice;
    private ArrayList<String> gigType;
    private ArrayList<String> gigSkills;
    private ArrayList<String> gigPaymentMode;
    private ArrayList<String> gigDescription;
    private ArrayList<String> gigId;
    private ArrayList<String> fullname;
    private ArrayList<String> institution;
    private ArrayList<String> department;
    private ArrayList<String> pp;
    private ArrayList<String> created_by;

    SharedPreferences preferences;
    String got_email;
    Dialog myDialog;

    public GigListViewAdapter(Context context, ArrayList<String> gigName, ArrayList<String> gigPrice, ArrayList<String> gigType, ArrayList<String> gigSkills, ArrayList<String> gigPaymentMode, ArrayList<String> gigDescription, ArrayList<String> gigId, ArrayList<String> fullname, ArrayList<String> institution, ArrayList<String> department, ArrayList<String> pp, ArrayList<String> created_by){
        //Getting all the values
        this.context = context;
        this.gigName = gigName;
        this.gigPrice = gigPrice;
        this.gigType = gigType;
        this.gigSkills = gigSkills;
        this.gigPaymentMode = gigPaymentMode;
        this.gigDescription = gigDescription;
        this.gigId = gigId;
        this.fullname = fullname;
        this.institution = institution;
        this.department = department;
        this.pp = pp;
        this.created_by = created_by;
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
            convertView = inflaInflater.inflate(R.layout.list_gig_home, parent, false);
        }

        preferences = context.getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        got_email = preferences.getString("email", "not available");

        LinearLayout lin_lay = convertView.findViewById(R.id.lin_lay);
        TextView name = convertView.findViewById(R.id.gig_name);
        TextView price = convertView.findViewById(R.id.gig_price);
        TextView type = convertView.findViewById(R.id.gig_type);

        name.setText(gigName.get(position));
        price.setText(gigPrice.get(position));
        type.setText(gigType.get(position));

        lin_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //load the custom dialog box
                myDialog = new Dialog(context);
                myDialog.setContentView(R.layout.card_gig_click_popup);
                //get views in the popup page
                CircleImageView profpic = myDialog.findViewById(R.id.image);
                Glide.with(context).load(pp.get(position)).into(profpic);
                LinearLayout linloader = myDialog.findViewById(R.id.linloader);
                linloader.setVisibility(View.GONE);
                TextView pop_name = myDialog.findViewById(R.id.created_by_gig);
                pop_name.setText(fullname.get(position));
                TextView pop_department = myDialog.findViewById(R.id.dept_gig);
                pop_department.setText(department.get(position));
                TextView pop_school = myDialog.findViewById(R.id.uni_gig);
                pop_school.setText(institution.get(position));
                TextView pop_gigname = myDialog.findViewById(R.id.gig_name);
                pop_gigname.setText(gigName.get(position));
                TextView pop_gigdescription = myDialog.findViewById(R.id.gig_description);
                pop_gigdescription.setText(gigDescription.get(position));
                TextView pop_gigskills = myDialog.findViewById(R.id.skills);
                pop_gigskills.setText(gigSkills.get(position));
                TextView pop_gigcategory = myDialog.findViewById(R.id.gig_category);
                pop_gigcategory.setText(gigPrice.get(position));
                Button bid = myDialog.findViewById(R.id.bid);

                // check API to see if Gig is approved, pending or rejected
                StringRequest stringRequest2 = new StringRequest(Request.Method.POST, GIGS_BIDDED,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                System.out.println("Response gig card clicked 2 = "+response);

                                try{
                                    JSONArray jsonArray = new JSONArray(response);
                                    int ArrayLength = jsonArray.length();

                                    int count = 0;

                                    if(ArrayLength >= 1){
                                        for(int j = ArrayLength - 1; j >= 0; j--){
                                            JSONObject section1 = jsonArray.getJSONObject(j);
                                            String gigname = section1.getString("gigname");
                                            String picture = section1.getString("picture");


                                            if (gigname.equals(gigName.get(position))){
                                                String status = section1.getString("status");

                                                count = count+1;

                                                System.out.println("Status - "+status);

                                                if (status.equals("approved")){
                                                    bid.setText("Bid Approved");
                                                    bid.setEnabled(false);
                                                    bid.setBackgroundResource(R.drawable.rounded_grey);
                                                }else if (status.equals("pending")){
                                                    bid.setText("Bid Pending");
                                                    bid.setEnabled(false);
                                                    bid.setBackgroundResource(R.drawable.rounded_grey);
                                                }else if (status.equals("rejected")){
                                                    bid.setText("Bid rejected");
                                                    bid.setEnabled(false);
                                                    bid.setBackgroundResource(R.drawable.rounded_grey);
                                                }else if (status.equals("")){
                                                    bid.setEnabled(true);
                                                    bid.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            myDialog.dismiss();
                                                            //move to the next gig page
                                                            Intent i = new Intent(context, CardGigClick2.class);
                                                            i.putExtra("name", created_by.get(position));
                                                            i.putExtra("department", department.get(position));
                                                            i.putExtra("school", institution.get(position));
                                                            i.putExtra("gig_name", gigName.get(position));
                                                            i.putExtra("picture", pp.get(position));
                                                            i.putExtra("gig_description", gigDescription.get(position));
                                                            i.putExtra("payment_structure", gigPrice.get(position));
                                                            i.putExtra("id", gigId.get(position));
                                                            context.startActivity(i);
                                                        }
                                                    });
                                                }

                                            }

                                        }
                                    }else{
                                        bid.setText("Place Bid");
                                        bid.setEnabled(true);
                                        bid.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                myDialog.dismiss();
                                                //move to the next gig page
                                                Intent i = new Intent(context, CardGigClick2.class);
                                                i.putExtra("name", created_by.get(position));
                                                i.putExtra("department", department.get(position));
                                                i.putExtra("school", institution.get(position));
                                                i.putExtra("gig_name", gigName.get(position));
                                                i.putExtra("picture", pp.get(position));
                                                i.putExtra("gig_description", gigDescription.get(position));
                                                i.putExtra("payment_structure", gigPrice.get(position));
                                                i.putExtra("id", gigId.get(position));
                                                context.startActivity(i);
                                            }
                                        });
                                    }

                                    if (count<1){
                                        if (created_by.get(position).equals(got_email)){
                                            bid.setEnabled(true);
                                            bid.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    myDialog.dismiss();
                                                    // you created the gig
                                                    Toast.makeText(context, "Sorry you created this Gig", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }else{
                                            bid.setEnabled(true);
                                            bid.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    myDialog.dismiss();
                                                    //move to the next gig page
                                                    Intent i = new Intent(context, CardGigClick2.class);
                                                    i.putExtra("name", created_by.get(position));
                                                    i.putExtra("department", department.get(position));
                                                    i.putExtra("school", institution.get(position));
                                                    i.putExtra("gig_name", gigName.get(position));
                                                    i.putExtra("picture", pp.get(position));
                                                    i.putExtra("gig_description", gigDescription.get(position));
                                                    i.putExtra("payment_structure", gigPrice.get(position));
                                                    i.putExtra("id", gigId.get(position));
                                                    context.startActivity(i);
                                                }
                                            });
                                        }



                                    }

                                }
                                catch (JSONException e) {
                                    try {
                                        JSONObject jsonObject = new JSONObject(response);
                                        String stats = jsonObject.getString("status");
                                        if(stats.equals("no gig bids")){
                                            //show "no gig bidded"
                                            bid.setEnabled(true);
                                            bid.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    myDialog.dismiss();
                                                    //move to the next gig page
                                                    Intent i = new Intent(context, CardGigClick2.class);
                                                    i.putExtra("name", created_by.get(position));
                                                    i.putExtra("department", department.get(position));
                                                    i.putExtra("school", institution.get(position));
                                                    i.putExtra("gig_name", gigName.get(position));
                                                    i.putExtra("picture", pp.get(position));
                                                    i.putExtra("gig_description", gigDescription.get(position));
                                                    i.putExtra("payment_structure", gigPrice.get(position));
                                                    i.putExtra("id", gigId.get(position));
                                                    context.startActivity(i);
                                                }
                                            });
                                        }

                                    } catch (JSONException ee) {
                                        ee.printStackTrace();
                                    }
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
                        params.put("email", got_email);
                        return params;
                    }
                };

                RequestQueue requestQueue2 = Volley.newRequestQueue(context);
                requestQueue2.add(stringRequest2);
                DefaultRetryPolicy retryPolicy = new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                stringRequest2.setRetryPolicy(retryPolicy);
                requestQueue2.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
                    @Override
                    public void onRequestFinished(Request<Object> request) {
                        requestQueue2.getCache().clear();
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
