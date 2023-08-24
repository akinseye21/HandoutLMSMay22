package com.example.handoutlms.handoutmessager;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.handoutlms.models.Users;
import com.example.handoutlms.R;
import com.example.handoutlms.adapters.UsersAdapter2;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class UsersFragment extends Fragment {

    ListView listView;
    ProgressBar progressBar;
    TextView progressText;
    private RecyclerView recyclerView;

    SharedPreferences preferences;
    String got_email;
    String senderKey;

    ArrayList<String> arr_key = new ArrayList<>();
    ArrayList<String> arr_id = new ArrayList<>();
    ArrayList<String> arr_name = new ArrayList<>();
    ArrayList<String> arr_email = new ArrayList<>();
    ArrayList<String> arr_institution = new ArrayList<>();
    ArrayList<String> arr_status = new ArrayList<>();
    ArrayList<Users> mUsers;

//    private FirebaseAuth mAuth;
//    FirebaseUser firebaseUser;

    public UsersFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_users, container, false);

//        mAuth = FirebaseAuth.getInstance();
//        firebaseUser = mAuth.getCurrentUser();

        listView = v.findViewById(R.id.listview);
        progressBar = v.findViewById(R.id.progressBar);
        progressText = v.findViewById(R.id.progressText);

        recyclerView = v.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setStackFromEnd(false);
        recyclerView.setLayoutManager(linearLayoutManager);

        preferences = getActivity().getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        got_email = preferences.getString("email", "not available");

        mUsers = new ArrayList<>();

        //read users from Firebase
        //FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//        DatabaseReference reference = FirebaseDatabase.getInstance("https://handout-lms-default-rtdb.firebaseio.com/").getReference("Users");
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                mUsers.clear();
//
//                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
//                    Users users = snapshot.getValue(Users.class);
//
//                    if(users.getId().equals(firebaseUser.getUid())){
//                        //do nothing
//                    }else{
//                        mUsers.add(users);
//                    }
//                }
//
//                UsersAdapter2 usersAdapter2 = new UsersAdapter2(getContext(), mUsers);
//                recyclerView.setAdapter(usersAdapter2);
//                progressBar.setVisibility(View.GONE);
//                progressText.setVisibility(View.GONE);
//
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//
//        });


        mUsers.clear();


        return v;
    }

    public interface OnFragmentInteractionListener {
    }



//    private void status(String status){
//        DatabaseReference reference = FirebaseDatabase.getInstance("https://handout-lms-default-rtdb.firebaseio.com/").getReference("Users").child(firebaseUser.getUid());
//        HashMap<String, Object> hashMap = new HashMap<>();
//        hashMap.put("status", status);
//
//        reference.updateChildren(hashMap);
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        status("online");
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        status("offline");
//    }

}