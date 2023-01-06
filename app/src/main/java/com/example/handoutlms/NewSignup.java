package com.example.handoutlms;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

public class NewSignup extends AppCompatActivity {

    ImageView back;
    Spinner institution;
    LinearLayout login;

    EditText fullname, emailaddress,  phonenum,  password, confirmpassword;
    String name, e_mail, phone, pass, confirmpass, school;

    ProgressBar progressBar;
    Button signup;
    Dialog myDialog;

    SharedPreferences preferences;

    public static final String SIGNUP = "https://handoutng.com/handouts/handout_registration";

    FirebaseAuth auth;
    DatabaseReference reference;

//    OkHttpClient client = new OkHttpClient();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_signup);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        preferences = getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        final SharedPreferences.Editor myEdit = preferences.edit();

        fullname = findViewById(R.id.edtname);
        emailaddress = findViewById(R.id.edtemail);
        phonenum = findViewById(R.id.edtphonenumber);
        password = findViewById(R.id.edtpassword);
        confirmpassword = findViewById(R.id.edtconfirmpassword);
        progressBar = findViewById(R.id.progressBar);
        signup = findViewById(R.id.signup);

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), LoginSignup.class);
                startActivity(i);
            }
        });

        login = findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Login.class);
                startActivity(i);
            }
        });

        String[] institutions = {"Select your university", "","Ahmadu Bello University Zaria", "Adekunle Ajasin Unversity Akungba", "Adeleke University",
                "Babcock University", "Federal University of Tech. Minna", "Federal University of Tech. Akure", "University of Lagos", "University of Abuja", "Obafemi Awolowo University Ife"};
        ArrayAdapter<String> institutionadapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.simple_spinner_small_text, R.id.tx, institutions) ;

        institution = findViewById(R.id.spinnerinstitution);
        institution.setAdapter(institutionadapter);
        institution.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView)view).setTextColor(Color.WHITE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = fullname.getText().toString().trim();
                e_mail = emailaddress.getText().toString().trim();
                phone = phonenum.getText().toString().trim();
                pass = password.getText().toString().trim();
                confirmpass = confirmpassword.getText().toString().trim();
                school = institution.getSelectedItem().toString().trim();

                //check if fields are empty
                if(name.isEmpty()){
                    fullname.setError("Name can not be empty");
                }else{
                    if (!e_mail.contains("@")){
                        emailaddress.setError("wrong email");
                    }else{
                        if(phone.isEmpty()){
                            phonenum.setError("Phone number required");
                        }else{
                            if(pass.equals(confirmpass)){
                                if(school.equals("Select your university")){
                                    Toast.makeText(getApplicationContext(), "Please select an institution", Toast.LENGTH_LONG).show();
                                }else{
                                    if(pass.length()>=6){

                                        myDialog = new Dialog(NewSignup.this);
                                        myDialog.setContentView(R.layout.custom_popup_signing_up_loading);
                                        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                        myDialog.setCanceledOnTouchOutside(false);
                                        myDialog.show();


                                        StringRequest stringRequest = new StringRequest(Request.Method.POST, SIGNUP,
                                                new Response.Listener<String>() {
                                                    @Override
                                                    public void onResponse(String response) {

                                                        System.out.println("Signup Response = "+response);

                                                        try{
                                                            JSONObject jsonObject = new JSONObject(response);

                                                            String status = jsonObject.getString("status");
                                                            if(status.equals("successful")){

                                                                Toast.makeText(getApplicationContext(), "Registration Successful", Toast.LENGTH_LONG).show();
//
                                                                myEdit.putString( "email", e_mail);
                                                                myEdit.putString("fullname", name);
//                                                                myEdit.putString("pics", pics);
                                                                myEdit.putString("password", pass);
                                                                myEdit.commit();

                                                                myDialog.dismiss();

                                                                Intent i = new Intent(getApplicationContext(), WelcomePage.class);
                                                                i.putExtra("fullname", name);
                                                                i.putExtra("email", e_mail);
                                                                i.putExtra("phone", phone);
                                                                i.putExtra("password", pass);
                                                                i.putExtra("school", school);
                                                                startActivity(i);


                                                            }else{
                                                                myDialog.dismiss();
                                                                Toast.makeText(NewSignup.this, "Signup failed, please try again", Toast.LENGTH_SHORT).show();
                                                            }


                                                        }
                                                        catch (JSONException e){
                                                            e.printStackTrace();
                                                            myDialog.dismiss();

                                                            try {
                                                                JSONArray jsonArray = new JSONArray(response);
                                                                JSONObject jsonObject = jsonArray.getJSONObject(0);
                                                                String status = jsonObject.getString("status");
                                                                if(status.equals("User email exists")){
                                                                    Toast.makeText(NewSignup.this, "Signup failed. User email already exist", Toast.LENGTH_LONG).show();
                                                                }
                                                            } catch (JSONException ex) {
                                                                ex.printStackTrace();
                                                            }

                                                        }

                                                    }
                                                },
                                                new Response.ErrorListener() {
                                                    @Override
                                                    public void onErrorResponse(VolleyError volleyError) {
                                                        myDialog.dismiss();
                                                        if(volleyError == null){
                                                            return;
                                                        }
                                                        System.out.println("Error = "+volleyError.getCause());
                                                    }
                                                }){
                                            @Override
                                            protected Map<String, String> getParams(){
                                                Map<String, String> params = new HashMap<>();
                                                params.put("fullname", name);
                                                params.put("email", e_mail);
                                                params.put("phone", phone);
                                                params.put("password", pass);
                                                params.put("institution", school);
                                                return params;
                                            }
                                        };

                                        RequestQueue requestQueue = Volley.newRequestQueue(NewSignup.this);
                                        DefaultRetryPolicy retryPolicy = new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                                        stringRequest.setRetryPolicy(retryPolicy);
                                        requestQueue.add(stringRequest);



                                    }else{
                                        password.setError("Password must be greater than 5 characters");
                                    }
                                }
                            }else{
                                password.setError("Password do not match");
                                confirmpassword.setError("Password do not match");
                            }
                        }
                    }
                }
            }
        });
    }

//    private void postElement() {
//
//        final RequestBody formBody = new FormBody.Builder()
//                .add("username", "test")
//                .add("password", "test").build();
//
//        okhttp3.Request.Builder builder = new Request.Builder();
//                .url(SIGNUP + "/users")
//                .post(formBody).build();
//
//        final Call call = client.newCall(request);
//        final Response response = call.execute();
//        System.out.println(response);
//    }
}
