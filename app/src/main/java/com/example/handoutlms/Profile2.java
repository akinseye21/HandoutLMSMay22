package com.example.handoutlms;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.provider.OpenableColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.io.ByteStreams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Profile2.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Profile2#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Profile2 extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int RESULT_OK = -1;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    TabLayout tabLayout;
    ViewPager viewPager;
    LinearLayout lintut, linpost, lingame, lingig, editProfile;
    TextView email, username, dept, school, location, date, edit;
    String got_fullname, got_dept, got_institution, got_dob, got_usertype;
    SharedPreferences preferences;
    String got_email, got_pics;
    String signup_email, sent_from;
    CircleImageView profilePic;

    public static final String USER_PROFILE = "https://handout.com.ng/handouts/handout_get_user_profile";
    private static final String ROOT_URL = "https://handout.com.ng/handouts/handout_update_user_profile_pic";
    private RequestQueue rQueue;

    private OnFragmentInteractionListener mListener;

    public Profile2() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Profile2.
     */
    // TODO: Rename and change types and number of parameters
    public static Profile2 newInstance(String param1, String param2) {
        Profile2 fragment = new Profile2();
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
        View v = inflater.inflate(R.layout.fragment_profile2, container, false);

        viewPager =v.findViewById(R.id.viewpager2);
        tabLayout = v.findViewById(R.id.tabs);

        lintut = v.findViewById(R.id.lintut);
        lingame = v.findViewById(R.id.lingame);
        lingig = v.findViewById(R.id.lingig);
        profilePic = v.findViewById(R.id.pp);

        email = v.findViewById(R.id.email);
        username = v.findViewById(R.id.user_name);
        dept = v.findViewById(R.id.department);
        school = v.findViewById(R.id.school);
        location = v.findViewById(R.id.location);
        date = v.findViewById(R.id.date);
        edit = v.findViewById(R.id.edit);
        editProfile = v.findViewById(R.id.editProfile);

        //to get where the click is from, either login or register
//        FeedsDashboard feedsDashboard = (FeedsDashboard) getActivity();
//        sent_from = feedsDashboard.getSentFrom();
//        signup_email = feedsDashboard.getSignupEmail();

        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,1);
            }
        });


        preferences = getActivity().getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        got_email = preferences.getString("email", "not available");
        got_pics = preferences.getString("pics", "not available");

        if(!got_pics.isEmpty()){
            Glide.with(getActivity()).load(got_pics).into(profilePic);
        }else{
            //do nothing
        }

//        Toast.makeText(getActivity(), "Email = "+got_email, Toast.LENGTH_LONG).show();
//        email.setText(got_email);
        addTabs(viewPager);

        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setOnTabSelectedListener(
                new TabLayout.ViewPagerOnTabSelectedListener(viewPager) {

                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        if (tab.getPosition() == 0){
                            lintut.setVisibility(View.VISIBLE);
                            lingame.setVisibility(View.GONE);
                            lingig.setVisibility(View.GONE);
                        }
//                        else if(tab.getPosition() == 1){
//                            lintut.setVisibility(View.GONE);
//                            linpost.setVisibility(View.VISIBLE);
//                            lingame.setVisibility(View.GONE);
//                            lingig.setVisibility(View.GONE);
//                        }
                        else if(tab.getPosition() == 1){
                            lintut.setVisibility(View.GONE);
                            lingame.setVisibility(View.VISIBLE);
                            lingig.setVisibility(View.GONE);
                        }
                        else if(tab.getPosition() == 2){
                            lintut.setVisibility(View.GONE);
                            lingame.setVisibility(View.GONE);
                            lingig.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {
//                        if(tab.getPosition() == 0){
//                            Intent j = new Intent(getContext(), Profile2.class);
//                            startActivity(j);
//                            getActivity().finishActivity(1);
//                        }
                    }
                }
        );

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), EditProfilePage.class);
                i.putExtra("email", got_email);
                i.putExtra("pics", got_pics);
                i.putExtra("usertype", got_usertype);
                startActivity(i);
            }
        });

        //get user profile
        StringRequest stringRequest = new StringRequest(Request.Method.POST, USER_PROFILE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("Profile = "+response);

                        try{
                            JSONObject profile = new JSONObject(response);
//                                String got_email = profile.getString("email");
                            got_fullname = profile.getString("fullname");
                            got_dob = profile.getString("dob");
                            got_institution = profile.getString("institution");
//                            got_faculty = profile.getString("faculty");
                            got_dept = profile.getString("department");
                            got_usertype = profile.getString("usertype");

                            email.setText(got_email);
                            username.setText(got_fullname);
                            dept.setText(got_dept);
                            school.setText(got_institution);
                            date.setText(got_dob);

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

    private void addTabs(ViewPager viewPager) {
        Profile2.ViewPagerAdapter adapter = new Profile2.ViewPagerAdapter(getChildFragmentManager());
        adapter.addFrag(new tutorial_on_profile(), "");
//        adapter.addFrag(new post_on_profile(), "");
        adapter.addFrag(new GamesProfile(), "");
        adapter.addFrag(new gig_on_profile(), "");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            // Get the Uri of the selected file
            Uri uri = data.getData();
            String uriString = uri.toString();
            File myFile = new File(uriString);
//            String path = myFile.getAbsolutePath();
            String displayName = null;
            if (uriString.startsWith("content://")) {
                Cursor cursor = null;
                try {
                    cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
                    if (cursor != null && cursor.moveToFirst()) {
                        displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                        Log.d("nameeeee>>>>  ",displayName);
//                        file_path.setText(displayName);
                        uploadPDF(displayName,uri);
                    }
                } finally {
                    cursor.close();
                }
            } else if (uriString.startsWith("file://")) {
                displayName = myFile.getName();
                Log.d("nameeeee>>>>  ",displayName);
//                file_path.setText(displayName);
            }
        }

        super.onActivityResult(requestCode, resultCode, data);

    }

    private void uploadPDF(final String pdfname, Uri pdffile) {

        InputStream iStream = null;
        try {

            iStream = getActivity().getContentResolver().openInputStream(pdffile);
            final byte[] inputData = getBytes(iStream);


            VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, ROOT_URL,
                    new Response.Listener<NetworkResponse>() {
                        @Override
                        public void onResponse(NetworkResponse response) {
                            System.out.println("Upload Updated "+response);
                            Toast.makeText(getActivity(), "Upload Update "+response, Toast.LENGTH_SHORT).show();
                            Log.d("ressssssoo",new String(response.data));
                            rQueue.getCache().clear();
                            try {
                                JSONObject jsonObject = new JSONObject(new String(response.data));
                                String status = jsonObject.getString("status");
                                String pics = jsonObject.getString("pics");

                                if(status.equals("update successful")){
                                    Toast.makeText(getActivity(), "File Uploaded successfully ", Toast.LENGTH_LONG).show();
                                    System.out.println("Status = "+status);

                                    Glide.with(getActivity()).load(pics).into(profilePic);

//                                    progressBar.setVisibility(View.GONE);
//                                    upload_text.setVisibility(View.GONE);
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();

//                                progressBar.setVisibility(View.GONE);
//                                upload_text.setVisibility(View.GONE);
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }) {

                /*
                 * If you want to add more parameters with the image
                 * you can do it here
                 * here we have only one parameter with the image
                 * which is tags
                 * */
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("email", got_email);
                    return params;
                }

                /*
                 *pass files using below method
                 * */
                @Override
                protected Map<String, DataPart> getByteData() {
                    Map<String, DataPart> params = new HashMap<>();
                    params.put("myfile", new DataPart(pdfname ,inputData));
                    return params;
                }
            };


            volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(
                    0,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            rQueue = Volley.newRequestQueue(getActivity());
            rQueue.add(volleyMultipartRequest);



        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
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

    static class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title){
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }


    }
}
