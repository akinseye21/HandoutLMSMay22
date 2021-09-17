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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;



/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FinishFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FinishFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FinishFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    Button welcome;
    CheckBox agreement;
    ProgressBar progressBar;
    SharedPreferences preferences;

    public static final String SIGNUP = "http://35.84.44.203/handouts/handout_registration";

    public FinishFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FinishFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FinishFragment newInstance(String param1, String param2) {
        FinishFragment fragment = new FinishFragment();
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
        View v = inflater.inflate(R.layout.fragment_finish, container, false);


        //get sharedpreference
        preferences = getActivity().getSharedPreferences("SignUpDetails", Context.MODE_PRIVATE);
        final String fullname = preferences.getString("fullname", "not available");
        final String email = preferences.getString("email", "not available");
        final String password = preferences.getString("password", "not available");
        final String gender = preferences.getString("gender", "not available");
        final String dob = preferences.getString("dob", "not available");
        final String country = preferences.getString("country", "not available");
        final String phonenum = preferences.getString("phonenum", "not available");
        final String institution = preferences.getString("institution", "not available");
        final String department = preferences.getString("department", "not available");
        final String level = preferences.getString("level", "not available");
        final String regNum = preferences.getString("regNum", "not available");

        welcome = v.findViewById(R.id.finis);
        agreement = v.findViewById(R.id.agreement);
        progressBar = v.findViewById(R.id.progressBar);
        welcome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(agreement.isChecked()){
                    //send data to the endpoint for registration and initiate loader
                    progressBar.setVisibility(View.VISIBLE);

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, SIGNUP,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    progressBar.setVisibility(View.GONE);

                                    System.out.println("Response = "+response);
                                    Toast.makeText(getActivity(), "Response = "+response, Toast.LENGTH_LONG).show();

                                    try{
                                        JSONObject jsonObject = new JSONObject(response);

                                        String signed_name = jsonObject.getString("fullname");
                                        String status = jsonObject.getString("status");

                                        if(status.equals("register successful")){
                                            Intent i = new Intent(getActivity(), WelcomePage.class);
                                            i.putExtra("fullname", signed_name);
                                            startActivity(i);
                                        }else{
                                            Toast.makeText(getContext(), "Signup failed. Please try again", Toast.LENGTH_LONG).show();
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

                                    if(volleyError == null){
                                        return;
                                    }

                                    progressBar.setVisibility(View.GONE);
//                                    Toast.makeText(getContext(),  volleyError.getMessage(), Toast.LENGTH_LONG).show();
                                    System.out.println("Error = "+volleyError.getMessage());
//                                    NetworkResponse statusCode = volleyError.networkResponse;
//                                    System.out.println("Codigo " + statusCode);
                                }
                            }){
                        @Override
                        protected Map<String, String> getParams(){
                            Map<String, String> params = new HashMap<>();
                            params.put("fullname", fullname);
                            params.put("email", email);
                            params.put("phone", phonenum);
                            params.put("password", password);
                            params.put("dob", dob);
                            params.put("gender", gender);
                            params.put("country", country);
                            params.put("institution", institution);
                            params.put("faculty", department);
                            params.put("department", department);
                            params.put("level", level);
                            params.put("registration_number", regNum);
                            return params;
                        }
                    };

//                    DefaultRetryPolicy retryPolicy = new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
//                    stringRequest.setRetryPolicy(retryPolicy);

                    RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
                    requestQueue.add(stringRequest);

                }
                else {
                    Toast.makeText(getActivity(), "Please agree to the terms and condition", Toast.LENGTH_LONG).show();
                }

            }
        });



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
