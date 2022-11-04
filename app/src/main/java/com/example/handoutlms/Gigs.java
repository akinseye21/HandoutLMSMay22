package com.example.handoutlms;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

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


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Gigs.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Gigs#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Gigs extends Fragment {
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
    final ArrayList<String> Array_gigPaymentMode = new ArrayList<>();
    final ArrayList<String> Array_gigType = new ArrayList<>();
    final ArrayList<String> Array_gigSkills = new ArrayList<>();
    final ArrayList<String> Array_gigDescription = new ArrayList<>();
    final ArrayList<String> Array_gigId = new ArrayList<>();
    final ArrayList<String> Array_fullname = new ArrayList<>();
    final ArrayList<String> Array_institution = new ArrayList<>();
    final ArrayList<String> Array_department = new ArrayList<>();
    int ArrayLength;

    ProgressBar progressBar;
    TextView progressText;

    GridView gridView;

    public static final String ALL_GIGS = "https://handout.com.ng/handouts/handout_get_all_gigs";

    public Gigs() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Gigs.
     */
    // TODO: Rename and change types and number of parameters
    public static Gigs newInstance(String param1, String param2) {
        Gigs fragment = new Gigs();
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
        View v = inflater.inflate(R.layout.fragment_gigs, container, false);

        gridView = v.findViewById(R.id.simpleGridView);
        progressBar = v.findViewById(R.id.progressBar);
        progressText = v.findViewById(R.id.progressText);

        //get all gigs here
        StringRequest stringRequest = new StringRequest(Request.Method.GET, ALL_GIGS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("Response= "+response);
//                        Toast.makeText(getContext(), "Response = "+response, Toast.LENGTH_LONG).show();
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            ArrayLength = jsonArray.length();

                            for (int i = ArrayLength - 1; i >= 0; i--) {
                                JSONObject section = jsonArray.getJSONObject(i);
                                String gigName = section.getString("gigname");
                                String gigPrice = section.getString("budget_category");
                                String gigPaymentMode = section.getString("payment_mode");
                                String gigType = section.getString("project_type");
                                String gigSkills = section.getString("skills");
                                String gigDescription = section.getString("description");
                                String gigID = section.getString("ID");
                                String fullname = section.getString("fullname");
                                String institution = section.getString("institution");
                                String department = section.getString("department");

                                Array_gigName.add(gigName);
                                Array_gigPrice.add(gigPrice);
                                Array_gigPaymentMode.add(gigPaymentMode);
                                Array_gigType.add(gigType);
                                Array_gigSkills.add(gigSkills);
                                Array_gigDescription.add(gigDescription);
                                Array_gigId.add("gigs_"+gigID);
                                Array_fullname.add(fullname);
                                Array_institution.add(institution);
                                Array_department.add(department);
                            }

                            //populate values on the gridview
                            GigListViewAdapter gigListViewAdapter = new GigListViewAdapter(getActivity(), Array_gigName, Array_gigPrice, Array_gigType, Array_gigSkills, Array_gigPaymentMode, Array_gigDescription, Array_gigId, Array_fullname, Array_institution, Array_department);
                            gridView.setAdapter(gigListViewAdapter);
                            //hide progressBar and progressText
                            progressBar.setVisibility(View.GONE);
                            progressText.setVisibility(View.GONE);

                        } catch (JSONException e) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String status = jsonObject.getString("status");
                                if(status.equals("0")){
                                    //hide progressBar and progressText
                                    progressBar.setVisibility(View.GONE);
                                    progressText.setVisibility(View.GONE);
                                    gridView.setVisibility(View.GONE);
                                    TextView noGigs = v.findViewById(R.id.no_gigs);
                                    noGigs.setVisibility(View.VISIBLE);
                                }
                            } catch (JSONException ex) {
                                ex.printStackTrace();
                            }


                            e.printStackTrace();
                        }
                    }


                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);

        //clear array
        Array_gigName.clear();
        Array_gigPrice.clear();
        Array_gigType.clear();
        Array_gigPaymentMode.clear();
        Array_gigDescription.clear();
        Array_gigSkills.clear();
        Array_gigId.clear();

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
