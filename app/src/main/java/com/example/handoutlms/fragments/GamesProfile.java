package com.example.handoutlms.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.handoutlms.R;
import com.example.handoutlms.activities.HandoutTrivia;
import com.example.handoutlms.activities.NigerianUniversities;
import com.example.handoutlms.activities.PastQuestion;
import com.example.handoutlms.adapters.GameAdapter;
import com.example.handoutlms.uniabuja.GetStarted;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class GamesProfile extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public static final String GET_GAMES = "https://handoutng.com/handouts/handout_get_games";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    RelativeLayout trivia_layout, pastQuestion, abj_trivia;
    ProgressBar progressBar;
    ListView listView;
    ArrayList<String> arr_gamename = new ArrayList<>();
    ArrayList<String> arr_gametime = new ArrayList<>();
    ArrayList<String> arr_gamedate = new ArrayList<>();
    ArrayList<String> arr_gamelocation = new ArrayList<>();
    ArrayList<String> arr_gamecreatedby = new ArrayList<>();

    SharedPreferences preferences;
    String got_email;

    private OnFragmentInteractionListener mListener;

    public GamesProfile() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_games_profile, container, false);

        preferences = getActivity().getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        got_email = preferences.getString("email", "not available");

        trivia_layout = v.findViewById(R.id.trivia_layout);
        trivia_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), HandoutTrivia.class);
                startActivity(i);
            }
        });
        pastQuestion = v.findViewById(R.id.pastQuestions);
        pastQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), PastQuestion.class);
                startActivity(i);
            }
        });
        abj_trivia = v.findViewById(R.id.abj_trivia);
        abj_trivia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), NigerianUniversities.class);
                startActivity(i);
            }
        });
        progressBar = v.findViewById(R.id.progressBar);
        listView = v.findViewById(R.id.gamelist);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, GET_GAMES,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        int ArrayLength;
                        progressBar.setVisibility(View.GONE);
                        System.out.println("Response = "+response);

                        try{
                            JSONArray jsonArray = new JSONArray(response);
                            ArrayLength = jsonArray.length();

                            for(int i=ArrayLength-1; i>=0; i--){
                                JSONObject section1 = jsonArray.getJSONObject(i);
                                String _gamename = section1.getString("gamename");
                                String _date = section1.getString("_date");
                                String _time = section1.getString("_time");
                                String _location = section1.getString("location");
                                String _createdby = section1.getString("created_by");

                                if(_createdby.equals(got_email)){
                                    arr_gamename.add(_gamename);
                                    arr_gamedate.add(_date);
                                    arr_gametime.add(_time);
                                    arr_gamelocation.add(_location);
                                    arr_gamecreatedby.add(_createdby);

                                    GameAdapter myAdapter=new GameAdapter(getActivity(),arr_gamename,arr_gamedate,arr_gametime, arr_gamelocation, arr_gamecreatedby);
                                    listView.setAdapter(myAdapter);
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
                        System.out.println("Error = "+volleyError.getMessage());
                    }
                }){
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
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
