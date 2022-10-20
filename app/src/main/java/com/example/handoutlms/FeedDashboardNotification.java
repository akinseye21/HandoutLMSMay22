package com.example.handoutlms;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

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
 * {@link FeedDashboardNotification.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FeedDashboardNotification#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FeedDashboardNotification extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    SharedPreferences preferences;
    String got_email;
    ListView notification;

    ArrayList<String> arr_title = new ArrayList<>();
    ArrayList<String> arr_message = new ArrayList<>();
    ArrayList<String> arr_status = new ArrayList<>();

    public static final String USER_PROFILE = "https://handout.com.ng/handouts/handout_get_user_profile";

    private OnFragmentInteractionListener mListener;

    public FeedDashboardNotification() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FeedDashboardNotification.
     */
    // TODO: Rename and change types and number of parameters
    public static FeedDashboardNotification newInstance(String param1, String param2) {
        FeedDashboardNotification fragment = new FeedDashboardNotification();
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
        View v = inflater.inflate(R.layout.fragment_feed_dashboard_notification, container, false);

        preferences = getActivity().getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        got_email = preferences.getString("email", "not available");

        notification = v.findViewById(R.id.listview_notification);

        //get user profile
        StringRequest stringRequest = new StringRequest(Request.Method.POST, USER_PROFILE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("Profile = "+response);

                        try{
                            JSONObject profile = new JSONObject(response);
                            String sys_notification = profile.getString("sys_notification");
                            JSONArray arr = new JSONArray(sys_notification);

                            int arr_length = arr.length();
                            for(int i =0; i<arr_length; i++){
                                JSONObject section1 = arr.getJSONObject(i);
                                String title = section1.getString("title");
                                String message = section1.getString("message");
                                String status = section1.getString("status");

                                arr_title.add(title);
                                arr_message.add(message);
                                arr_status.add(status);
                            }

                            NotificationAdapter myAdapter=new NotificationAdapter(getContext(),arr_title,arr_message,arr_status);
                            notification.setAdapter(myAdapter);

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

        //clear array
        arr_title.clear();
        arr_message.clear();
        arr_status.clear();


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
