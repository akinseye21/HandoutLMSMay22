package com.example.handoutlms;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import static android.content.Context.MODE_APPEND;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AcademicFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AcademicFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AcademicFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static ViewPager viewPager;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    Spinner institution, department, level;
    ImageView inst, dept, lev, imgregnum;
    EditText edtregnum;
    TextInputLayout labelregnum;
    Button next;

    SharedPreferences preferences;

    private OnFragmentInteractionListener mListener;

    public AcademicFragment(ViewPager viewPager) {
        // Required empty public constructor
        this.viewPager = viewPager;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AcademicFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AcademicFragment newInstance(String param1, String param2) {
        AcademicFragment fragment = new AcademicFragment(viewPager);
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
        View v = inflater.inflate(R.layout.fragment_academic, container, false);

        preferences = getActivity().getSharedPreferences("SignUpDetails", Context.MODE_PRIVATE);
        final String fullname = preferences.getString("fullname", "not available");
        final String email = preferences.getString("email", "not available");
        final String password = preferences.getString("password", "not available");
        final String gender = preferences.getString("gender", "not available");
        final String dob = preferences.getString("dob", "not available");
        final String country = preferences.getString("country", "not available");
        final String phonenum = preferences.getString("phonenum", "not available");


        edtregnum = v.findViewById(R.id.regnumber);
        labelregnum = v.findViewById(R.id.regnum_textinput_layout);

        next = v.findViewById(R.id.next);

        institution = v.findViewById(R.id.spinnerinstitution);
        department = v.findViewById(R.id.spinnerdepartment);
        level = v.findViewById(R.id.spinnerlevel);

        inst = v.findViewById(R.id.imginstitution);
        dept = v.findViewById(R.id.imgdepartment);
        lev = v.findViewById(R.id.imglevel);
        imgregnum = v.findViewById(R.id.imgregnumber);

        String[] institutions = {"Select an institution...","Ahmadu Bello University Zaria", "Adekunle Ajasin Unversity Akungba", "Adeleke University",
                "Babcock University", "Federal University of Tech. Minna", "Federal University of Tech. Akure", "University of Lagos", "University of Abuja", "Obafemi Awolowo University Ife"};
        String[] departments = {"Select a department...","Computer science", "Mathematics", "Social Studies", "Economics", "Statistics", "Accounting", "Banking and Finance", "Business Administration", "Human Kinetics", "Physics", "Int. Relations"};
        String[] levels = {"Select a level...","Pre-Degree","Diploma","100 Level", "200 Level", "300 Level", "400 Level", "500 Level", "MSc", "PGD"};

        ArrayAdapter<String> institutionadapter = new ArrayAdapter<String>(getContext(), R.layout.simple_spinner_small_text, R.id.tx, institutions) ;
        ArrayAdapter<String> departmentadapter = new ArrayAdapter<String>(getContext(), R.layout.simple_spinner_small_text, R.id.tx, departments) ;
        ArrayAdapter<String> leveladapter = new ArrayAdapter<String>(getContext(), R.layout.simple_spinner_small_text, R.id.tx, levels) ;

        institution.setAdapter(institutionadapter);
        department.setAdapter(departmentadapter);
        level.setAdapter(leveladapter);

        institution.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0){
                    inst.setImageResource(R.drawable.tick);
                }else{
                    inst.setImageResource(R.drawable.whitedot);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        department.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0){
                    dept.setImageResource(R.drawable.tick);
                }else{
                    dept.setImageResource(R.drawable.whitedot);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        level.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0){
                    lev.setImageResource(R.drawable.tick);
                }else{
                    lev.setImageResource(R.drawable.whitedot);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        labelregnum.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()>5){
                    imgregnum.setImageResource(R.drawable.tick);
                } else{
                    imgregnum.setImageResource(R.drawable.whitedot);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(inst.getDrawable().getConstantState().equals(getResources().getDrawable(R.drawable.tick).getConstantState()) &&
                        dept.getDrawable().getConstantState().equals(getResources().getDrawable(R.drawable.tick).getConstantState()) &&
                        lev.getDrawable().getConstantState().equals(getResources().getDrawable(R.drawable.tick).getConstantState()) &&
                        imgregnum.getDrawable().getConstantState().equals(getResources().getDrawable(R.drawable.tick).getConstantState()))
                {
                    SharedPreferences.Editor myEdit = preferences.edit();
                    myEdit.putString("institution", institution.getSelectedItem().toString());
                    myEdit.putString("department", department.getSelectedItem().toString());
                    myEdit.putString("level", level.getSelectedItem().toString());
                    myEdit.putString("regNum", edtregnum.getText().toString());
                    myEdit.commit();

                    Toast.makeText(getActivity(), "Fullname: "+fullname+
                            "\nemail: "+email+
                            "\nPassword : "+password+
                            "\nGender: "+gender+
                            "\nDOB: "+dob+
                            "\nCountry: "+country+
                            "\nPhone: "+phonenum+
                            "\nInstitution: "+institution.getSelectedItem().toString()+
                            "\nDepartment: "+department.getSelectedItem().toString()+
                            "\nLevel: "+level.getSelectedItem().toString()+
                            "\nReg Num: "+edtregnum.getText().toString(), Toast.LENGTH_LONG).show();

                    viewPager.setCurrentItem(2);
                }

                else{
                    Toast.makeText(getContext(), "Error in one or more fields", Toast.LENGTH_LONG).show();
                }



            }
        });





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
