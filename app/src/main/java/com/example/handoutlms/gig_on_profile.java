package com.example.handoutlms;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link gig_on_profile.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link gig_on_profile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class gig_on_profile extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    final ArrayList<String> Array_gigName = new ArrayList<>();
    final ArrayList<String> Array_gigPrice = new ArrayList<>();
    final ArrayList<String> Array_gigTime = new ArrayList<>();
    int ArrayLength;

    ProgressBar progressBar;
    TextView progressText, nogig;

    GridView gridView;
    SharedPreferences preferences;

    public static final String USER_GIGS = "http://35.84.44.203/handouts/handout_get_user_gig";

    public gig_on_profile() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment gig_on_profile.
     */
    // TODO: Rename and change types and number of parameters
    public static gig_on_profile newInstance(String param1, String param2) {
        gig_on_profile fragment = new gig_on_profile();
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

        gridView = v.findViewById(R.id.simpleGridView);
        progressBar = v.findViewById(R.id.progressBar);
        progressText = v.findViewById(R.id.progressText);
        nogig = v.findViewById(R.id.notification_text);

        preferences = getActivity().getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        final String got_email = preferences.getString("email", "not available");
        Toast.makeText(getActivity(), "Email = "+got_email, Toast.LENGTH_LONG).show();
        System.out.println("Email = "+got_email);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, USER_GIGS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("Response = "+response);

                        try{
                            JSONArray jsonArray = new JSONArray(response);
                            ArrayLength = jsonArray.length();

                            for (int i = 0; i < ArrayLength; i++) {
                                JSONObject section = jsonArray.getJSONObject(i);
                                String gigName = section.getString("gigname");
                                String gigPrice = section.getString("_price");
                                String gigTime = section.getString("_time");
                                String stat = section.getString("status");

                                if(stat.equals("0")){
                                    nogig.setVisibility(View.VISIBLE);
                                }else{
                                    Array_gigName.add(gigName);
                                    Array_gigPrice.add(gigPrice);
                                    Array_gigTime.add(gigTime);
                                }


                            }
                            //populate values on the gridview
                            GigProfileListViewAdapter gigProfileListViewAdapter = new GigProfileListViewAdapter(getContext(), Array_gigName, Array_gigPrice, Array_gigTime);
                            gridView.setAdapter(gigProfileListViewAdapter);
                            //hide progressBar and progressText
                            progressBar.setVisibility(View.GONE);
                            progressText.setVisibility(View.GONE);
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
                params.put("email", got_email);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);

        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
