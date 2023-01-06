package com.example.handoutlms;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class TutorListViewAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<String> tutorName;
    private ArrayList<String> tutorFaculty;
    private ArrayList<String> tutorEmail;

    SharedPreferences preferences;
    String got_email;

    public static final String USER_PROFILE = "https://handoutng.com/handouts/handout_get_user_profile";

    public TutorListViewAdapter(Context context, ArrayList<String> tutorName, ArrayList<String> tutorFaculty, ArrayList<String> tutorEmail){
        //Getting all the values
        this.context = context;
        this.tutorName = tutorName;
        this.tutorFaculty = tutorFaculty;
        this.tutorEmail = tutorEmail;
    }

    @Override
    public int getCount() {
        return tutorName.size();
    }

    @Override
    public Object getItem(int position) {
        return tutorFaculty.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflaInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = inflaInflater.inflate(R.layout.list_tutors, parent, false);
        }

        preferences = context.getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        got_email = preferences.getString("email", "not available");

        TextView name = convertView.findViewById(R.id.fullname);
        TextView fac = convertView.findViewById(R.id.faculty);
        LinearLayout viewProfile = convertView.findViewById(R.id.viewprofile);
        CircleImageView pp = convertView.findViewById(R.id.pp);

        name.setText(tutorName.get(position));
        fac.setText(tutorFaculty.get(position));

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
                                Glide.with(context).load(got_pics).into(pp);
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
                params.put("email", tutorEmail.get(position));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);

        viewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if the clicked email is the same as the shared preference, do not go
                if(tutorEmail.get(position).equals(got_email)){
                    Toast.makeText(context, "To view your profile, click on the profile tab", Toast.LENGTH_LONG).show();
                }else{
                    //move to the user profile page
                    Intent i = new Intent(context, ProfileOthers.class);
                    //pass the email of the selected user
                    i.putExtra("email", tutorEmail.get(position));
                    context.startActivity(i);
                }
            }
        });

        return convertView;
    }
}
