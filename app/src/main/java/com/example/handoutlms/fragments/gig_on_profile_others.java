package com.example.handoutlms.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.handoutlms.R;
import com.example.handoutlms.adapters.GigProfileListViewAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link gig_on_profile_others#newInstance} factory method to
 * create an instance of this fragment.
 */
public class gig_on_profile_others extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private gig_on_profile.OnFragmentInteractionListener mListener;

    final ArrayList<String> Array_gigName = new ArrayList<>();
    final ArrayList<String> Array_gigPrice = new ArrayList<>();
    final ArrayList<String> Array_gigType = new ArrayList<>();
    final ArrayList<String> Array_gigSkills = new ArrayList<>();
    final ArrayList<String> Array_gigPaymentMode = new ArrayList<>();
    final ArrayList<String> Array_gigDescription = new ArrayList<>();
    final ArrayList<String> Array_startDate = new ArrayList<>();
    final ArrayList<String> Array_endDate = new ArrayList<>();
    final ArrayList<String> Array_gigID = new ArrayList<>();
    final ArrayList<String> Array_gigStatus = new ArrayList<>();

    final ArrayList<String> Array_gigName2 = new ArrayList<>();
    final ArrayList<String> Array_gigPrice2 = new ArrayList<>();
    final ArrayList<String> Array_gigType2 = new ArrayList<>();
    final ArrayList<String> Array_gigSkills2 = new ArrayList<>();
    final ArrayList<String> Array_gigPaymentMode2 = new ArrayList<>();
    final ArrayList<String> Array_gigDescription2 = new ArrayList<>();
    final ArrayList<String> Array_startDate2 = new ArrayList<>();
    final ArrayList<String> Array_endDate2 = new ArrayList<>();
    final ArrayList<String> Array_gigID2 = new ArrayList<>();
    final ArrayList<String> Array_gigStatus2 = new ArrayList<>();
    int ArrayLength;

    String user_email;

    LinearLayout created_gigs, placed_bids, filter, out;

    ProgressBar progressBar;
    TextView progressText, nogig, nogigbid, closedText;

    View view1, view2;
    GridView gridView1, gridView2;

    public static final String USER_GIGS = "http://handoutng.com/handouts/handout_get_user_gig";

    public gig_on_profile_others() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment gig_on_profile_others.
     */
    // TODO: Rename and change types and number of parameters
    public static gig_on_profile_others newInstance(String param1, String param2) {
        gig_on_profile_others fragment = new gig_on_profile_others();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_gig_on_profile, container, false);

        gridView1 = v.findViewById(R.id.simpleGridView);
        gridView2 = v.findViewById(R.id.simpleGridView2);
        view1 = v.findViewById(R.id.viewCreated);
        view2 = v.findViewById(R.id.viewBids);
//        progressBar = v.findViewById(R.id.progressBar);
//        progressText = v.findViewById(R.id.progressText);
//        nogig = v.findViewById(R.id.notification_text);
        nogig = v.findViewById(R.id.no_gig);
        nogigbid = v.findViewById(R.id.no_gig_bid);
        created_gigs = v.findViewById(R.id.created_gigs);
        placed_bids = v.findViewById(R.id.placed_bids);
        filter = v.findViewById(R.id.filter);
        out = v.findViewById(R.id.out);
        closedText = v.findViewById(R.id.closedText);

        Intent i = getActivity().getIntent();
        user_email = i.getStringExtra("email");

        created_gigs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gridView1.setVisibility(View.VISIBLE);
                gridView2.setVisibility(View.GONE);
                view1.setVisibility(View.VISIBLE);
                view2.setVisibility(View.GONE);
                nogigbid.setVisibility(View.GONE);


                StringRequest stringRequest = new StringRequest(Request.Method.POST, USER_GIGS,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                System.out.println("Response = "+response);

                                try{
                                    JSONArray jsonArray = new JSONArray(response);
                                    ArrayLength = jsonArray.length();

                                    if(ArrayLength > 1){
                                        for(int j = ArrayLength - 1; j >= 0; j--){
                                            JSONObject section1 = jsonArray.getJSONObject(j);
                                            String gigName = section1.getString("gigname");
                                            String gigPrice = section1.getString("budget_category");
                                            String gigType = section1.getString("project_type");
                                            String gigSkills = section1.getString("skills");
                                            String gigPaymentMode = section1.getString("payment_mode");
                                            String gigDescription = section1.getString("description");
                                            String start_date = section1.getString("start_date");
                                            String end_date = section1.getString("end_date");
                                            String gigId = section1.getString("ID");

                                            Array_gigName.add(gigName);
                                            Array_gigPrice.add(gigPrice);
                                            Array_gigType.add(gigType);
                                            Array_gigSkills.add(gigSkills);
                                            Array_gigPaymentMode.add(gigPaymentMode);
                                            Array_gigDescription.add(gigDescription);
                                            Array_startDate.add(start_date);
                                            Array_endDate.add(end_date);
                                            Array_gigID.add("gigs_"+gigId);
                                            Array_gigStatus.add("");

                                        }

                                        //populate values on the gridview
                                        GigProfileListViewAdapter gigProfileListViewAdapter = new GigProfileListViewAdapter(getContext(), Array_gigName, Array_gigPrice, Array_gigType, Array_gigSkills, Array_gigPaymentMode, Array_gigDescription, Array_startDate, Array_endDate, Array_gigID, Array_gigStatus, "otherProfile");
                                        gridView1.setAdapter(gigProfileListViewAdapter);
                                        //hide progressBar and progressText
//                                progressBar.setVisibility(View.GONE);
//                                progressText.setVisibility(View.GONE);
                                    }else{
                                        JSONObject section2 = jsonArray.getJSONObject(0);
                                        int len = section2.length();
                                        if(len == 1){
                                            nogig.setVisibility(View.VISIBLE);
                                            nogigbid.setVisibility(View.GONE);
                                            //hide progressBar and progressText
//                                    progressBar.setVisibility(View.GONE);
//                                    progressText.setVisibility(View.GONE);
                                        }else{
                                            String gigName = section2.getString("gigname");
                                            String gigPrice = section2.getString("budget_category");
                                            String gigType = section2.getString("project_type");
                                            String gigSkills = section2.getString("skills");
                                            String gigPaymentMode = section2.getString("payment_mode");
                                            String gigDescription = section2.getString("description");
                                            String start_date = section2.getString("start_date");
                                            String end_date = section2.getString("end_date");
                                            String gigId = section2.getString("ID");

                                            Array_gigName2.add(gigName);
                                            Array_gigPrice2.add(gigPrice);
                                            Array_gigType2.add(gigType);
                                            Array_gigSkills2.add(gigSkills);
                                            Array_gigPaymentMode2.add(gigPaymentMode);
                                            Array_gigDescription2.add(gigDescription);
                                            Array_startDate2.add(start_date);
                                            Array_endDate2.add(end_date);
                                            Array_gigID2.add("gigs_"+gigId);
                                            Array_gigStatus2.add("");

                                            //populate values on the gridview
                                            GigProfileListViewAdapter gigProfileListViewAdapter = new GigProfileListViewAdapter(getContext(), Array_gigName2, Array_gigPrice2, Array_gigType2, Array_gigSkills2, Array_gigPaymentMode2, Array_gigDescription2, Array_startDate2, Array_endDate2, Array_gigID2, Array_gigStatus2, "otherProfile");
                                            gridView1.setAdapter(gigProfileListViewAdapter);
                                            //hide progressBar and progressText
//                                    progressBar.setVisibility(View.GONE);
//                                    progressText.setVisibility(View.GONE);
                                        }
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
                        params.put("email", user_email);
                        return params;
                    }
                };

                RequestQueue requestQueue = Volley.newRequestQueue(getContext());
                requestQueue.add(stringRequest);


                //clear array
                Array_gigName.clear();
                Array_gigPrice.clear();
                Array_gigType.clear();
                Array_gigPaymentMode.clear();
                Array_gigDescription.clear();
                Array_gigSkills.clear();
                Array_startDate.clear();
                Array_endDate.clear();
                Array_gigID.clear();
                Array_gigStatus.clear();

                //clear array2
                Array_gigName2.clear();
                Array_gigPrice2.clear();
                Array_gigType2.clear();
                Array_gigPaymentMode2.clear();
                Array_gigDescription2.clear();
                Array_gigSkills2.clear();
                Array_startDate2.clear();
                Array_endDate2.clear();
                Array_gigID2.clear();
                Array_gigStatus2.clear();


            }
        });


        StringRequest stringRequest = new StringRequest(Request.Method.POST, USER_GIGS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("Response = "+response);

                        try{
                            JSONArray jsonArray = new JSONArray(response);
                            ArrayLength = jsonArray.length();

                            if(ArrayLength > 1){
                                for(int j = ArrayLength - 1; j >= 0; j--){
                                    JSONObject section1 = jsonArray.getJSONObject(j);
                                    String gigName = section1.getString("gigname");
                                    String gigPrice = section1.getString("budget_category");
                                    String gigType = section1.getString("project_type");
                                    String gigSkills = section1.getString("skills");
                                    String gigPaymentMode = section1.getString("payment_mode");
                                    String gigDescription = section1.getString("description");
                                    String start_date = section1.getString("start_date");
                                    String end_date = section1.getString("end_date");
                                    String gigId = section1.getString("ID");

                                    Array_gigName.add(gigName);
                                    Array_gigPrice.add(gigPrice);
                                    Array_gigType.add(gigType);
                                    Array_gigSkills.add(gigSkills);
                                    Array_gigPaymentMode.add(gigPaymentMode);
                                    Array_gigDescription.add(gigDescription);
                                    Array_startDate.add(start_date);
                                    Array_endDate.add(end_date);
                                    Array_gigID.add("gigs_"+gigId);
                                    Array_gigStatus.add("");

                                }

                                //populate values on the gridview
                                GigProfileListViewAdapter gigProfileListViewAdapter = new GigProfileListViewAdapter(getContext(), Array_gigName, Array_gigPrice, Array_gigType, Array_gigSkills, Array_gigPaymentMode, Array_gigDescription, Array_startDate, Array_endDate, Array_gigID, Array_gigStatus, "otherProfile");
                                gridView1.setAdapter(gigProfileListViewAdapter);
                                //hide progressBar and progressText
//                                progressBar.setVisibility(View.GONE);
//                                progressText.setVisibility(View.GONE);
                            }else{
                                JSONObject section2 = jsonArray.getJSONObject(0);
                                int len = section2.length();
                                if(len == 1){
                                    nogig.setVisibility(View.VISIBLE);
                                    nogigbid.setVisibility(View.GONE);
                                    //hide progressBar and progressText
//                                    progressBar.setVisibility(View.GONE);
//                                    progressText.setVisibility(View.GONE);
                                }else{
                                    String gigName = section2.getString("gigname");
                                    String gigPrice = section2.getString("budget_category");
                                    String gigType = section2.getString("project_type");
                                    String gigSkills = section2.getString("skills");
                                    String gigPaymentMode = section2.getString("payment_mode");
                                    String gigDescription = section2.getString("description");
                                    String start_date = section2.getString("start_date");
                                    String end_date = section2.getString("end_date");
                                    String gigId = section2.getString("ID");

                                    Array_gigName2.add(gigName);
                                    Array_gigPrice2.add(gigPrice);
                                    Array_gigType2.add(gigType);
                                    Array_gigSkills2.add(gigSkills);
                                    Array_gigPaymentMode2.add(gigPaymentMode);
                                    Array_gigDescription2.add(gigDescription);
                                    Array_startDate2.add(start_date);
                                    Array_endDate2.add(end_date);
                                    Array_gigID2.add("gigs_"+gigId);
                                    Array_gigStatus2.add("");

                                    //populate values on the gridview
                                    GigProfileListViewAdapter gigProfileListViewAdapter = new GigProfileListViewAdapter(getContext(), Array_gigName2, Array_gigPrice2, Array_gigType2, Array_gigSkills2, Array_gigPaymentMode2, Array_gigDescription2, Array_startDate2, Array_endDate2, Array_gigID2, Array_gigStatus2, "otherProfile");
                                    gridView1.setAdapter(gigProfileListViewAdapter);
                                    //hide progressBar and progressText
//                                    progressBar.setVisibility(View.GONE);
//                                    progressText.setVisibility(View.GONE);
                                }
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
                params.put("email", user_email);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);


        //clear array
        Array_gigName.clear();
        Array_gigPrice.clear();
        Array_gigType.clear();
        Array_gigPaymentMode.clear();
        Array_gigDescription.clear();
        Array_gigSkills.clear();
        Array_gigID.clear();

        //clear array2
        Array_gigName2.clear();
        Array_gigPrice2.clear();
        Array_gigType2.clear();
        Array_gigPaymentMode2.clear();
        Array_gigDescription2.clear();
        Array_gigSkills2.clear();
        Array_gigID2.clear();

        return v;
    }

    public interface OnFragmentInteractionListener {
    }
}