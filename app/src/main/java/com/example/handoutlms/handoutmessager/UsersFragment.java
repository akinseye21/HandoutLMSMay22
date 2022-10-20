package com.example.handoutlms.handoutmessager;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.handoutlms.Users;
import com.example.handoutlms.UsersAdapter;
import com.example.handoutlms.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class UsersFragment extends Fragment {

    public static final String ALL_USERS = "http://handout.com.ng/handouts/handout_get_users";
    int ArrayLength;
    ListView listView;
    ProgressBar progressBar;
    TextView progressText;

    SharedPreferences preferences;
    String got_email;
    String senderKey;

    ArrayList<String> arr_key = new ArrayList<>();
    ArrayList<String> arr_id = new ArrayList<>();
    ArrayList<String> arr_name = new ArrayList<>();
    ArrayList<String> arr_email = new ArrayList<>();
    ArrayList<String> arr_institution = new ArrayList<>();
    ArrayList<String> mUsers = new ArrayList<>();

    private FirebaseAuth mAuth;

    public UsersFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_users, container, false);

        mAuth = FirebaseAuth.getInstance();

        listView = v.findViewById(R.id.listview);
        progressBar = v.findViewById(R.id.progressBar);
        progressText = v.findViewById(R.id.progressText);

        preferences = getActivity().getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        got_email = preferences.getString("email", "not available");

        StringRequest stringRequest = new StringRequest(Request.Method.GET, ALL_USERS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("Response= "+response);
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            ArrayLength = jsonArray.length();

//                            for (int i = ArrayLength - 1; i >= 0; i--) {
                            for (int i = 0; i < ArrayLength; i++) {
                                JSONObject section = jsonArray.getJSONObject(i);
                                String id = section.getString("ID");
                                String fullname = section.getString("fullname");
                                String email = section.getString("email");
                                String institution = section.getString("institution");

                                mAuth.createUserWithEmailAndPassword(email, email)
                                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                if(task.isSuccessful()){
                                                    Users users = new Users(id, fullname, email, institution);

                                                    FirebaseDatabase.getInstance("https://handout-lms-default-rtdb.firebaseio.com/").getReference("Users")
                                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                            .setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if(task.isSuccessful()){
                                                                        System.out.println("User "+fullname+" has been registered on firebase");
                                                                    }else{
                                                                        System.out.println("User "+fullname+" was not registered on firebase \n"+task.getException().getMessage());
                                                                    }
                                                                }
                                                            });
                                                }
                                                else{
                                                    System.out.println("User "+fullname+" was not registered on firebase "+task.getException().getMessage());
                                                }

                                            }
                                        });
                            }

                            //populate values on the gridview
//                            UsersAdapter usersAdapter = new UsersAdapter(getActivity(), arr_id, arr_name, arr_email, arr_institution);
//                            listView.setAdapter(usersAdapter);
                            //hide progressBar and progressText
//                            progressBar.setVisibility(View.GONE);
//                            progressText.setVisibility(View.GONE);

                        } catch (JSONException e) {
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


        //read users from Firebase
//        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance("https://handout-lms-default-rtdb.firebaseio.com/").getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUsers.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    System.out.println("Datasnapshot = "+snapshot);

                    Users users = snapshot.getValue(Users.class);
                    String keys = snapshot.getKey();

                    assert users != null;
//                    assert firebaseUser != null;
                    String id2 = users.getId();
                    String fullname2 = users.getFullname();
                    String email2 = users.getEmail();
                    String institution2 = users.institution;

                    System.out.println("Key "+fullname2+" = "+keys);

                    if(got_email.equals(email2)){
                        senderKey = keys;
                    }else{
                        arr_key.add(keys);
                        arr_id.add(id2);
                        arr_name.add(fullname2);
                        arr_email.add(email2);
                        arr_institution.add(institution2);
                    }


                }

                UsersAdapter usersAdapter = new UsersAdapter(getActivity(), arr_key, arr_id, arr_name, arr_email, arr_institution, senderKey);
                listView.setAdapter(usersAdapter);
                //hide progressBar and progressText
                progressBar.setVisibility(View.GONE);
                progressText.setVisibility(View.GONE);
                System.out.println("Sender = "+senderKey);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




        return v;
    }

    public interface OnFragmentInteractionListener {
    }
}