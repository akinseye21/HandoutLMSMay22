package com.example.handoutlms;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.leanback.widget.HorizontalGridView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Tutor.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Tutor#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Tutor extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

//    HorizontalGridView gridView;
    //array for tutors
    final ArrayList<String> Array_tutorName = new ArrayList<>();
    final ArrayList<String> Array_tutorFaculty = new ArrayList<>();
    final ArrayList<String> Array_tutorEmail = new ArrayList<>();
    int ArrayLength, ArrayLength2;

    final ArrayList<String> Array_groupName = new ArrayList<>();
    final ArrayList<String> Array_groupTime = new ArrayList<>();
    final ArrayList<String> Array_groupTutor = new ArrayList<>();

    SharedPreferences preferences;
    String got_email;

    RecyclerView recyclerView;
    MyRvAdapter myRvAdapter;
//    GridView gridView;
    GridView gridView2;
    ProgressBar progressBar_tutor, progressBar_group;
    GroupListViewAdapter groupListViewAdapter;

    public static final String ALL_TUTORS = "https://handoutng.com/handouts/handout_get_tutors";
    public static final String ALL_GROUPS = "https://handoutng.com/handouts/handout_get_all_tutorials";

    public Tutor() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Tutor.
     */
    // TODO: Rename and change types and number of parameters
    public static Tutor newInstance(String param1, String param2) {
        Tutor fragment = new Tutor();
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
        View v = inflater.inflate(R.layout.fragment_tutor, container, false);

        LinearLayoutManager layoutManager= new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL, false);
        recyclerView = v.findViewById(R.id.recycler_view);
        gridView2 = v.findViewById(R.id.simpleGridView2);

        progressBar_tutor = v.findViewById(R.id.progressBar_tutor);
        progressBar_group = v.findViewById(R.id.progressBar_group);

        preferences = getContext().getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        got_email = preferences.getString("email", "not available");

        //get all tutors here
        StringRequest stringRequest = new StringRequest(Request.Method.GET, ALL_TUTORS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("Response= "+response);
                        progressBar_tutor.setVisibility(View.GONE);
//                        Toast.makeText(getContext(), "Response = "+response, Toast.LENGTH_LONG).show();
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            ArrayLength = jsonArray.length();

                            for (int i = 0; i < ArrayLength; i++) {
                                JSONObject section = jsonArray.getJSONObject(i);
                                String tutorName = section.getString("fullname");
                                String tutorInstitution = section.getString("institution");
                                String tutorEmail = section.getString("email");

                                Array_tutorName.add(tutorName);
                                Array_tutorFaculty.add(tutorInstitution);
                                Array_tutorEmail.add(tutorEmail);
                            }

                            //populate values on the recyclerview
                            myRvAdapter = new MyRvAdapter(Array_tutorName, Array_tutorFaculty, Array_tutorEmail);
                            recyclerView.setLayoutManager(layoutManager);
                            recyclerView.setAdapter(myRvAdapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }


                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar_tutor.setVisibility(View.GONE);
                error.printStackTrace();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);

        //clear array
        Array_tutorName.clear();
        Array_tutorFaculty.clear();


        //get all groups here
        StringRequest stringRequest_groups = new StringRequest(Request.Method.GET, ALL_GROUPS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("Response= "+response);
                        progressBar_group.setVisibility(View.GONE);
//                        Toast.makeText(getContext(), "Response = "+response, Toast.LENGTH_LONG).show();
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            ArrayLength2 = jsonArray.length();

                            if(ArrayLength2<1){
                                progressBar_group.setVisibility(View.GONE);
                                gridView2.setVisibility(View.GONE);

                                TextView noGroups = v.findViewById(R.id.no_groups);
                                noGroups.setVisibility(View.VISIBLE);
                            }
                            else {
                                for (int i = 0; i < ArrayLength2; i++) {
                                    JSONObject section = jsonArray.getJSONObject(i);
                                    String groupName = section.getString("groupname");
                                    String groupTime = section.getString("_date");
                                    String groupTutor = section.getString("created_by");

                                    Array_groupName.add(groupName);
                                    Array_groupTime.add(groupTime);
                                    Array_groupTutor.add(groupTutor);
                                }
                            }

                            //populate values on the gridview
                            groupListViewAdapter = new GroupListViewAdapter(getActivity(), Array_groupName, Array_groupTime, Array_groupTutor);
                            gridView2.setAdapter(groupListViewAdapter);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }


                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar_group.setVisibility(View.GONE);
                error.printStackTrace();
            }
        });
        RequestQueue requestQueue2 = Volley.newRequestQueue(getActivity());
        requestQueue2.add(stringRequest_groups);

        //clear array
        Array_groupName.clear();
        Array_groupTime.clear();
        Array_groupTutor.clear();

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



    class MyRvAdapter extends RecyclerView.Adapter<MyRvAdapter.MyHolder> {
        ArrayList<String> named;
        ArrayList<String> faculty;
        ArrayList<String> email;

        public MyRvAdapter(ArrayList<String> named, ArrayList<String> faculty, ArrayList<String> email) {
            this.named = named;
            this.faculty = faculty;
            this.email = email;
        }

        @NonNull
        @Override
        public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.list_tutors, parent, false);
            return new MyHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyHolder holder, @SuppressLint("RecyclerView") int position) {
            holder.name.setText(named.get(position));
            holder.fac.setText(faculty.get(position));

            String USER_PROFILE = "https://handoutng.com/handouts/handout_get_user_profile";

            //get profile picture
            StringRequest stringRequest = new StringRequest(Request.Method.POST, USER_PROFILE,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            System.out.println("Profile = "+response);

                            try{
                                JSONObject profile = new JSONObject(response);
                                String got_pics = profile.getString("pics");

                                if(got_pics.equals("")){
                                    //do nothing
                                }else{
                                    Glide.with(getContext()).load(got_pics).into(holder.pp);
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
                    params.put("email", email.get(position));
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(getContext());
            requestQueue.add(stringRequest);


            holder.viewProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //if the clicked email is the same as the shared preference, do not go
                    if(email.get(position).equals(got_email)){
                        Toast.makeText(getContext(), "To view your profile, click on the profile tab", Toast.LENGTH_LONG).show();
                    }else{
                        //move to the user profile page
                        Intent i = new Intent(getContext(), ProfileOthers.class);
                        //pass the email of the selected user
                        i.putExtra("email", email.get(position));
                        getContext().startActivity(i);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return named.size();
        }

        class MyHolder extends RecyclerView.ViewHolder {
            TextView name;
            TextView fac;
            LinearLayout viewProfile;
            CircleImageView pp;

            public MyHolder(@NonNull View itemView) {
                super(itemView);
                name = itemView.findViewById(R.id.fullname);
                fac = itemView.findViewById(R.id.faculty);
                viewProfile = itemView.findViewById(R.id.viewprofile);
                pp = itemView.findViewById(R.id.pp);


            }
        }

    }
}
