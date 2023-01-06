package com.example.handoutlms;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;
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
 * Use the {@link tutorial_on_profile_others#newInstance} factory method to
 * create an instance of this fragment.
 */
public class tutorial_on_profile_others extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private tutorial_on_profile.OnFragmentInteractionListener mListener;

    LinearLayout created_tutorial, joined_tutorial;
    View view1, view2;
    GridView gridViewCreated, gridViewJoined;
    TextView noTutorial, noTutorialJoined;
    int ArrayLength;

    ArrayList<String> Array_tutName = new ArrayList<>();
    ArrayList<String> Array_tutCategory = new ArrayList<>();
    ArrayList<String> Array_tutDescription = new ArrayList<>();
    ArrayList<String> Array_tutMode = new ArrayList<>();
    ArrayList<String> Array_tutId = new ArrayList<>();

    ArrayList<String> Array_tutName2 = new ArrayList<>();
    ArrayList<String> Array_tutCategory2 = new ArrayList<>();
    ArrayList<String> Array_tutDescription2 = new ArrayList<>();
    ArrayList<String> Array_tutMode2 = new ArrayList<>();
    ArrayList<String> Array_tutId2 = new ArrayList<>();

    String user_email;

    public static final String ALL_TUTORIAL = "https://handoutng.com/handouts/handout_get_all_tutorials";
    public static final String TUTORIALS_JOINED = "https://handoutng.com/handouts/handout_user_joined_groups";

    public tutorial_on_profile_others() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment tutorial_on_profile_others.
     */
    // TODO: Rename and change types and number of parameters
    public static tutorial_on_profile_others newInstance(String param1, String param2) {
        tutorial_on_profile_others fragment = new tutorial_on_profile_others();
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
        View v = inflater.inflate(R.layout.fragment_tutorial_on_profile_others, container, false);

        gridViewCreated = v.findViewById(R.id.simpleGridViewCreated);
        gridViewJoined = v.findViewById(R.id.simpleGridViewJoined);
        view1 = v.findViewById(R.id.viewCreated);
        view2 = v.findViewById(R.id.viewJoin);
        created_tutorial = v.findViewById(R.id.created_tutorial);
        joined_tutorial = v.findViewById(R.id.joined_tutorial);
        noTutorial = v.findViewById(R.id.no_tutorial);
        noTutorialJoined = v.findViewById(R.id.no_tutorial_joined);

        Intent i = getActivity().getIntent();
        user_email = i.getStringExtra("email");

        Toast.makeText(getContext(), "userEmail = "+user_email, Toast.LENGTH_SHORT).show();
        System.out.println("userEmail = "+user_email);

        created_tutorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gridViewCreated.setVisibility(View.VISIBLE);
                gridViewJoined.setVisibility(View.GONE);
                view1.setVisibility(View.VISIBLE);
                view2.setVisibility(View.GONE);
                noTutorialJoined.setVisibility(View.GONE);

                String from = "created";

                StringRequest stringRequest = new StringRequest(Request.Method.GET, ALL_TUTORIAL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                System.out.println("Response = "+response);

                                try{
                                    JSONArray jsonArray = new JSONArray(response);
                                    ArrayLength = jsonArray.length();

                                    for(int j = ArrayLength - 1; j >= 0; j--){
                                        JSONObject section1 = jsonArray.getJSONObject(j);
                                        String tutName = section1.getString("groupname");
                                        String tutCategory = section1.getString("category");
                                        String tutDescription = section1.getString("description");
                                        String tutCreatedBy = section1.getString("created_by");
                                        String tutMode = section1.getString("mode");
                                        String id = section1.getString("ID");

                                        if(tutCreatedBy.equals(user_email)){
                                            Array_tutName.add(tutName);
                                            Array_tutCategory.add(tutCategory);
                                            Array_tutDescription.add(tutDescription);
                                            Array_tutMode.add(tutMode);
                                            Array_tutId.add(id);
                                        }else{
                                            //do nothing
                                        }


                                    }

                                    if(Array_tutName.size() < 1){
                                        noTutorial.setVisibility(View.VISIBLE);
                                        noTutorialJoined.setVisibility(View.GONE);
                                        gridViewCreated.setVisibility(View.GONE);
                                        gridViewJoined.setVisibility(View.GONE);
                                    }else{
                                        //populate values on the gridview
                                        TutorialProfileAdapter tutorialProfileAdapter = new TutorialProfileAdapter(getContext(), Array_tutName, Array_tutCategory, Array_tutDescription, Array_tutMode, Array_tutId, from);
                                        gridViewCreated.setAdapter(tutorialProfileAdapter);
                                        noTutorial.setVisibility(View.GONE);
                                        noTutorialJoined.setVisibility(View.GONE);
                                        gridViewJoined.setVisibility(View.GONE);
                                    }

//                                    if(ArrayLength > 1){
//
//
//                                    }else{
//                                        //show "no tutorials"
//                                        noTutorial.setVisibility(View.VISIBLE);
//                                        gridViewCreated.setVisibility(View.GONE);
//                                        gridViewJoined.setVisibility(View.GONE);
//                                        noTutorialJoined.setVisibility(View.GONE);
//                                    }

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
                        return params;
                    }
                };

                RequestQueue requestQueue = Volley.newRequestQueue(getContext());
                requestQueue.add(stringRequest);

                //clear array
                Array_tutName.clear();
                Array_tutCategory.clear();
                Array_tutDescription.clear();
                Array_tutMode.clear();
                Array_tutId.clear();




            }
        });


        joined_tutorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gridViewCreated.setVisibility(View.GONE);
                gridViewJoined.setVisibility(View.VISIBLE);
                view1.setVisibility(View.GONE);
                view2.setVisibility(View.VISIBLE);
                noTutorial.setVisibility(View.GONE);

                String from = "joined";

                //for joined groups
                StringRequest stringRequest2 = new StringRequest(Request.Method.POST, TUTORIALS_JOINED,
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
                                            String tutName = section1.getString("groupname");
                                            String tutCategory = section1.getString("category");
                                            String tutDescription = section1.getString("description");
                                            String tutCreatedBy = section1.getString("created_by");
                                            String tutMode = section1.getString("status");
                                            String tutId = section1.getString("id");

                                            Array_tutName2.add(tutName);
                                            Array_tutCategory2.add(tutCategory);
                                            Array_tutDescription2.add(tutDescription);
                                            Array_tutMode2.add(tutMode);
                                            Array_tutId2.add(tutId);

                                        }

                                        //populate values on the gridview
                                        TutorialProfileAdapter tutorialProfileAdapter = new TutorialProfileAdapter(getContext(), Array_tutName2, Array_tutCategory2, Array_tutDescription2, Array_tutMode2, Array_tutId2, from);
                                        gridViewJoined.setAdapter(tutorialProfileAdapter);
                                        gridViewCreated.setVisibility(View.GONE);
                                        noTutorial.setVisibility(View.GONE);
                                        noTutorialJoined.setVisibility(View.GONE);

                                    }else{
                                        //show "no tutorials"
                                        noTutorialJoined.setVisibility(View.VISIBLE);
                                        noTutorial.setVisibility(View.GONE);
                                        gridViewCreated.setVisibility(View.GONE);
                                        gridViewJoined.setVisibility(View.GONE);
                                    }

                                }
                                catch (JSONException e) {
                                    try {
                                        JSONObject jsonObject = new JSONObject(response);
                                        String stats = jsonObject.getString("status");
                                        //show "no tutorials"
                                        noTutorialJoined.setVisibility(View.VISIBLE);
                                        noTutorial.setVisibility(View.GONE);
                                        gridViewCreated.setVisibility(View.GONE);
                                        gridViewJoined.setVisibility(View.GONE);
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
                        params.put("email", user_email);
                        return params;
                    }
                };

                RequestQueue requestQueue2 = Volley.newRequestQueue(getContext());
                requestQueue2.add(stringRequest2);

                //clear array
                Array_tutName2.clear();
                Array_tutCategory2.clear();
                Array_tutDescription2.clear();
                Array_tutMode2.clear();
                Array_tutId2.clear();

            }
        });


        StringRequest stringRequest = new StringRequest(Request.Method.GET, ALL_TUTORIAL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("Response = "+response);

                        try{
                            JSONArray jsonArray = new JSONArray(response);
                            ArrayLength = jsonArray.length();

                            String from = "created";


                            for(int j = ArrayLength - 1; j >= 0; j--){
                                JSONObject section1 = jsonArray.getJSONObject(j);
                                String tutName = section1.getString("groupname");
                                String tutCategory = section1.getString("category");
                                String tutDescription = section1.getString("description");
                                String tutCreatedBy = section1.getString("created_by");
                                String tutMode = section1.getString("mode");
                                String id = section1.getString("ID");

                                if(tutCreatedBy.equals(user_email)){
                                    Array_tutName.add(tutName);
                                    Array_tutCategory.add(tutCategory);
                                    Array_tutDescription.add(tutDescription);
                                    Array_tutMode.add(tutMode);
                                    Array_tutId.add(id);
                                }else{
                                    //do nothing
                                }


                            }

                            if(Array_tutName.size() < 1){
                                noTutorial.setVisibility(View.VISIBLE);
                                noTutorialJoined.setVisibility(View.GONE);
                                gridViewCreated.setVisibility(View.GONE);
                                gridViewJoined.setVisibility(View.GONE);
                            }else{
                                //populate values on the gridview
                                TutorialProfileAdapter tutorialProfileAdapter = new TutorialProfileAdapter(getContext(), Array_tutName, Array_tutCategory, Array_tutDescription, Array_tutMode, Array_tutId, from);
                                gridViewCreated.setAdapter(tutorialProfileAdapter);
                                noTutorial.setVisibility(View.GONE);
                                noTutorialJoined.setVisibility(View.GONE);
                                gridViewJoined.setVisibility(View.GONE);
                            }

//                            if(ArrayLength > 1){
//
//
//                            }else{
//                                //show "no tutorials"
//                                noTutorial.setVisibility(View.VISIBLE);
//                                gridViewCreated.setVisibility(View.GONE);
//                                gridViewJoined.setVisibility(View.GONE);
//                                noTutorialJoined.setVisibility(View.GONE);
//                            }

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
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);

        //clear array
        Array_tutName.clear();
        Array_tutCategory.clear();
        Array_tutDescription.clear();
        Array_tutMode.clear();
        Array_tutId.clear();



        return v;
    }
}