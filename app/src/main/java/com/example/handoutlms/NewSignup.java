package com.example.handoutlms;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class NewSignup extends AppCompatActivity {

    ImageView back;
    Spinner institution;
    LinearLayout login;

    EditText fullname, emailaddress,  phonenum,  password, confirmpassword;
    String name, e_mail, phone, pass, confirmpass, school;

    ProgressBar progressBar;
    Button signup;

    public static final String SIGNUP = "https://handout.com.ng/handouts/handout_registration";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_signup);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

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
                                    progressBar.setVisibility(View.VISIBLE);

                                    //if fields are not empty and are valid,
                                    //pass details to DB
                                    StringRequest stringRequest = new StringRequest(Request.Method.POST, SIGNUP,
                                            new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {
                                                    progressBar.setVisibility(View.GONE);

                                                    System.out.println("Response = "+response);
                                                    try{
                                                        JSONObject jsonObject = new JSONObject(response);

                                                        String signed_name = jsonObject.getString("fullname");
                                                        String status = jsonObject.getString("status");

                                                        if(status.equals("successful")){
                                                            Toast.makeText(getApplicationContext(), "Registration Successful", Toast.LENGTH_LONG).show();

                                                            Intent i = new Intent(getApplicationContext(), WelcomePage.class);
                                                            i.putExtra("fullname", signed_name);
                                                            i.putExtra("email", e_mail);
                                                            startActivity(i);
                                                        }else{
                                                            Toast.makeText(getApplicationContext(), "Signup failed. Please try again", Toast.LENGTH_LONG).show();
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

                                                    if(volleyError == null){
                                                        return;
                                                    }

                                                    progressBar.setVisibility(View.GONE);
                                                    System.out.println("Error = "+volleyError.getMessage());
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
                                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext().getApplicationContext());
                                    requestQueue.add(stringRequest);
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
}
