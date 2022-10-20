package com.example.handoutlms;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
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
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfilePage extends AppCompatActivity {

    ImageView back;
    Dialog myDialog;
    String got_email, got_pics, got_usertype;
    CircleImageView pp;
    SharedPreferences preferences;
    DatePickerDialog datePickerDialog;
    ProgressBar mainProg;

    TextView txt_fullname, txt_email, txt_phone, txt_username, txt_dob, txt_gender;
    ListView listEducation, listExperience;
    TextView noedu, noexperience;

    ArrayList<String> arr_uni = new ArrayList<>();
    ArrayList<String> arr_qualification = new ArrayList<>();
    ArrayList<String> arr_department = new ArrayList<>();
    ArrayList<String> arr_year = new ArrayList<>();

    ArrayList<String> arr_jobposition = new ArrayList<>();
    ArrayList<String> arr_jobdescription = new ArrayList<>();
    ArrayList<String> arr_joborganization = new ArrayList<>();
    ArrayList<String> arr_jobyear = new ArrayList<>();

    String[] gender = {"Choose Gender...","","Male","Female"};
    public static final String GET_USER_PROFILE = "https://handout.com.ng/handouts/handout_get_user_profile";
    public static final String UPDATE_PERSONAL_USER_INFO = "https://handout.com.ng/handouts/handout_update_user_info";
    public static final String ADD_EDUCATION = "https://handout.com.ng/handouts/handout_add_education";
    public static final String ADD_EXPERIENCE = "https://handout.com.ng/handouts/handout_add_work_experience";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_page);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Intent i = getIntent();
        got_email = i.getStringExtra("email");
//        got_pics = i.getStringExtra("pics");
        got_usertype = i.getStringExtra("usertype");


        txt_fullname = findViewById(R.id.f_name);
        txt_email = findViewById(R.id.e_mail);
        txt_phone = findViewById(R.id.p_hone);
        txt_username = findViewById(R.id.username);
        txt_dob = findViewById(R.id.dob);
        txt_gender = findViewById(R.id.gender);
        listEducation = findViewById(R.id.list_education);
        listExperience = findViewById(R.id.list_experience);
        noedu = findViewById(R.id.noedu);
        noexperience = findViewById(R.id.noexperience);
        pp = findViewById(R.id.pp);
        back = findViewById(R.id.back);
        mainProg = findViewById(R.id.progressBar);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //get user profile
        StringRequest stringRequest = new StringRequest(Request.Method.POST, GET_USER_PROFILE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        mainProg.setVisibility(View.GONE);
                        try{
                            JSONObject update = new JSONObject(response);
                            String g_fullname = update.getString("fullname");
                            String g_email = update.getString("email");
                            String g_gender = update.getString("gender");
                            String g_phone = update.getString("phone");
                            String g_dob = update.getString("dob");
                            String g_pics = update.getString("pics");
                            String education = update.getString("education");
                            String work = update.getString("work");

                            //set text for Education
                            JSONArray jsonArray = new JSONArray(education);
                            if(jsonArray.length() < 1){
                                noedu.setVisibility(View.VISIBLE);
                                listEducation.setVisibility(View.GONE);
                            }else{
                                noedu.setVisibility(View.GONE);
                                for(int i=0; i< jsonArray.length(); i++){
                                    JSONObject section = jsonArray.getJSONObject(i);
                                    String myUni = section.getString("university");
                                    String myQual = section.getString("qualification");
                                    String myDept = section.getString("department");
                                    String myYear = section.getString("year");

                                    arr_uni.add(myUni);
                                    arr_qualification.add(myQual);
                                    arr_department.add(myDept);
                                    arr_year.add(myYear);
                                }
                            }

                            //set text for Experience
                            JSONArray jsonArray2 = new JSONArray(work);
                            if(jsonArray2.length() < 1){
                                noexperience.setVisibility(View.VISIBLE);
                                listExperience.setVisibility(View.GONE);
                            }else{
                                noexperience.setVisibility(View.GONE);
                                for(int i=0; i< jsonArray2.length(); i++){
                                    JSONObject section1 = jsonArray2.getJSONObject(i);
                                    String jobPosition = section1.getString("position");
                                    String jobDescription = section1.getString("description");
                                    String jobOrganization = section1.getString("organization");
                                    String jobYear = section1.getString("year");

                                    arr_jobposition.add(jobPosition);
                                    arr_jobdescription.add(jobDescription);
                                    arr_joborganization.add(jobOrganization);
                                    arr_jobyear.add(jobYear);
                                }
                            }


                            //set text for user info
                            txt_username.setText(g_fullname);
                            txt_fullname.setText(g_fullname);
                            txt_email.setText(g_email);
                            txt_phone.setText(g_phone);
                            txt_dob.setText(g_dob);
                            txt_gender.setText(g_gender);
                            Glide.with(getApplicationContext()).load(g_pics).into(pp);

                            //set text for education
                            //send to adapter class
                            EducationAdapter educationAdapter = new EducationAdapter(EditProfilePage.this, arr_uni, arr_qualification, arr_department, arr_year);
                            listEducation.setAdapter(educationAdapter);

                            ExperienceAdapter experienceAdapter = new ExperienceAdapter(EditProfilePage.this, arr_jobposition, arr_jobdescription, arr_joborganization, arr_jobyear);
                            listExperience.setAdapter(experienceAdapter);

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

        RequestQueue requestQueue = Volley.newRequestQueue(EditProfilePage.this);
        requestQueue.add(stringRequest);


    }

    public void EditUserInfo(View view) {
        myDialog = new Dialog(EditProfilePage.this);
        myDialog.setContentView(R.layout.custom_popup_edit_userinfo);
        // Setting dialogview
        Window window = myDialog.getWindow();
        window.setGravity(Gravity.CENTER);
        //getting dialog view
        EditText fullname = myDialog.findViewById(R.id.fullname);
        EditText emailaddress = myDialog.findViewById(R.id.email);
        EditText phonenum = myDialog.findViewById(R.id.phonenum);
        Spinner gen = myDialog.findViewById(R.id.spinnergender);
        EditText dob = myDialog.findViewById(R.id.dob);
        ProgressBar progressBar = myDialog.findViewById(R.id.progressBar);
        Button save = myDialog.findViewById(R.id.save);
        Button cancel = myDialog.findViewById(R.id.cancel);

        ArrayAdapter<String> genderAdapter = new ArrayAdapter<String>(EditProfilePage.this, R.layout.simple_spinner_small_whitebg, R.id.tx, gender);
        gen.setAdapter(genderAdapter);
        emailaddress.setText(got_email);
        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // calender class's instance and get current date , month and year from calender
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day

                // date picker dialog
                datePickerDialog = new DatePickerDialog(EditProfilePage.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                monthOfYear+=1;
                                // set day of month , month and year value in the edit text
                                String mt;
                                if(monthOfYear<10){
                                    mt = "0"+monthOfYear;
                                }
                                else mt = String.valueOf(monthOfYear);
                                String dy;
                                if(dayOfMonth<10)
                                    dy = "0"+dayOfMonth;
                                else dy = String.valueOf(dayOfMonth);

                                dob.setText(dy + "/"
                                        + mt + "/" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get all input first
                String s_fullname = fullname.getText().toString().trim();
                String s_email = emailaddress.getText().toString().trim();
                String s_phonenum = phonenum.getText().toString().trim();
                String s_gen = gen.getSelectedItem().toString();
                String s_dob = dob.getText().toString().trim();

//                System.out.println("fullname = "+s_fullname+"\nemail = "+s_email+"\nphone = "+s_phonenum+"\ngender = "+s_gen+"\ndob = "+s_dob+"\nusertype = "+got_usertype);
                if (s_fullname.isEmpty()){
                    fullname.setError("Empty Field");
                }else{
                    if(s_email.isEmpty()){
                        emailaddress.setError("Empty Field");
                    }else{
                        if (s_phonenum.isEmpty()){
                            phonenum.setError("Empty Field");
                        }else{
                            if (s_gen.isEmpty() || s_gen.equals("Choose Gender...")){

                            }else{
                                if (s_dob.isEmpty()){
                                    dob.setError("Empty Field");
                                }else{
                                    progressBar.setVisibility(View.VISIBLE);

                                    StringRequest stringRequest = new StringRequest(Request.Method.POST, UPDATE_PERSONAL_USER_INFO,
                                            new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {
                                                    progressBar.setVisibility(View.GONE);

                                                    try{
                                                        JSONObject update = new JSONObject(response);
                                                        String status = update.getString("status");
                                                        String f_name = update.getString("fullname");
                                                        String e_mail = update.getString("email");
                                                        String p_hone = update.getString("phone");

                                                        if (status.equals("update successful")){
                                                            Toast.makeText(EditProfilePage.this, "Info updated successfully", Toast.LENGTH_SHORT).show();
                                                            myDialog.dismiss();
                                                            txt_fullname.setText(f_name);
                                                            txt_email.setText(e_mail);
                                                            txt_phone.setText(p_hone);
                                                        }else{
                                                            Toast.makeText(EditProfilePage.this, "Failed!!! Please try again", Toast.LENGTH_SHORT).show();
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
                                            params.put("email", s_email);
                                            params.put("fullname", s_fullname);
                                            params.put("gender", s_gen);
                                            params.put("dob", s_dob);
                                            params.put("phone", s_phonenum);
                                            params.put("usertype", got_usertype);
                                            return params;
                                        }
                                    };

                                    RequestQueue requestQueue = Volley.newRequestQueue(EditProfilePage.this);
                                    requestQueue.add(stringRequest);
                                }
                            }
                        }
                    }
                }

//                if(s_fullname.equals("") || s_email.equals("") || s_phonenum.equals("") || !s_gen.equals("Male") || !s_gen.equals("Female") || s_dob.equals("")){
//                    //do not send to database
//                }else{
//
//
//                }

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialog.dismiss();
            }
        });

        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.setCanceledOnTouchOutside(true);
        myDialog.show();
    }

    public void AddEducation(View view) {
        myDialog = new Dialog(EditProfilePage.this);
        myDialog.setContentView(R.layout.custom_popup_add_education);
        // Setting dialogview
        Window window = myDialog.getWindow();
        window.setGravity(Gravity.CENTER);
        //getting dialog view
        EditText uni = myDialog.findViewById(R.id.university);
        EditText dept = myDialog.findViewById(R.id.department);
        EditText qualification = myDialog.findViewById(R.id.qualification);
        EditText year = myDialog.findViewById(R.id.year);
        ProgressBar progressBar = myDialog.findViewById(R.id.progressBar);
        Button save = myDialog.findViewById(R.id.save);
        Button cancel = myDialog.findViewById(R.id.cancel);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get all input
                String s_uni = uni.getText().toString().trim();
                String s_dept = dept.getText().toString().trim();
                String s_qualification = qualification.getText().toString().trim();
                String s_year = year.getText().toString();

                //checks and balance
                if(s_uni.isEmpty()){
                    uni.setError("Empty Field");
                }else{
                    if(s_dept.isEmpty()){
                        dept.setError("Empty Field");
                    }else{
                        if (s_qualification.isEmpty()){
                            qualification.setError("Empty Field");
                        }else{
                            if (s_year.isEmpty()){
                                year.setError("Empty Field");
                            }else{
                                //send to DB
                                progressBar.setVisibility(View.VISIBLE);

                                StringRequest stringRequest = new StringRequest(Request.Method.POST, ADD_EDUCATION,
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                progressBar.setVisibility(View.GONE);

                                                try{
                                                    JSONObject update = new JSONObject(response);
                                                    String status = update.getString("status");

                                                    if (status.equals("successful")){
                                                        Toast.makeText(EditProfilePage.this, "Education successfully added", Toast.LENGTH_SHORT).show();
                                                        myDialog.dismiss();
                                                    }else{
                                                        Toast.makeText(EditProfilePage.this, "Error!! Failed to add. Please try again", Toast.LENGTH_SHORT).show();
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
                                        params.put("email", got_email);
                                        params.put("qualification", s_qualification);
                                        params.put("university", s_uni);
                                        params.put("department", s_dept);
                                        params.put("year", s_year);
                                        return params;
                                    }
                                };

                                RequestQueue requestQueue = Volley.newRequestQueue(EditProfilePage.this);
                                requestQueue.add(stringRequest);

                            }
                        }
                    }
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialog.dismiss();
            }
        });

        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.setCanceledOnTouchOutside(true);
        myDialog.show();
    }

    public void AddExperience(View view) {
        myDialog = new Dialog(EditProfilePage.this);
        myDialog.setContentView(R.layout.custom_popup_add_experience);
        // Setting dialogview
        Window window = myDialog.getWindow();
        window.setGravity(Gravity.CENTER);
        //getting dialog view
        EditText position = myDialog.findViewById(R.id.position);
        EditText year = myDialog.findViewById(R.id.year);
        EditText description = myDialog.findViewById(R.id.description);
        EditText organization = myDialog.findViewById(R.id.organization);
        ProgressBar progressBar = myDialog.findViewById(R.id.progressBar);
        Button save = myDialog.findViewById(R.id.save);
        Button cancel = myDialog.findViewById(R.id.cancel);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get all input
                String s_position = position.getText().toString().trim();
                String s_description = description.getText().toString().trim();
                String s_organization = organization.getText().toString().trim();
                String s_year = year.getText().toString();

                //checks and balance
                if(s_position.isEmpty()){
                    position.setError("Empty Field");
                }else{
                    if(s_description.isEmpty()){
                        description.setError("Empty Field");
                    }else{
                        if (s_organization.isEmpty()){
                            organization.setError("Empty Field");
                        }else{
                            if (s_year.isEmpty()){
                                year.setError("Empty Field");
                            }else{
                                //send to DB
                                progressBar.setVisibility(View.VISIBLE);

                                StringRequest stringRequest = new StringRequest(Request.Method.POST, ADD_EXPERIENCE,
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                progressBar.setVisibility(View.GONE);

                                                try{
                                                    JSONObject update = new JSONObject(response);
                                                    String status = update.getString("status");

                                                    if (status.equals("successful")){
                                                        Toast.makeText(EditProfilePage.this, "Experience successfully added", Toast.LENGTH_SHORT).show();
                                                        myDialog.dismiss();
                                                    }else{
                                                        Toast.makeText(EditProfilePage.this, "Error!! Failed to add. Please try again", Toast.LENGTH_SHORT).show();
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
                                        params.put("email", got_email);
                                        params.put("position", s_position);
                                        params.put("description", s_description);
                                        params.put("organization", s_organization);
                                        params.put("year", s_year);
                                        return params;
                                    }
                                };

                                RequestQueue requestQueue = Volley.newRequestQueue(EditProfilePage.this);
                                requestQueue.add(stringRequest);

                            }
                        }
                    }
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialog.dismiss();
            }
        });

        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.setCanceledOnTouchOutside(true);
        myDialog.show();
    }
}
