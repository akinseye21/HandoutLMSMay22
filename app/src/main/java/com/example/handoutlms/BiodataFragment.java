package com.example.handoutlms;

import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BiodataFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BiodataFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BiodataFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static ViewPager viewPager;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    EditText edtname, edtemail, edtpassword, edtdob, edtphonenum;
    ImageView  layname, layemail, laypassword, laygender, laydob, laycountry, layphonenum;
    TextInputLayout labelname, labelemail, labelpassword, labelphonenum;
    Spinner country;
    TextView code;
    DatePickerDialog datePickerDialog;
    Button next;

    String gender;

    RadioGroup radioGroup;
    RadioButton male, female;

    SharedPreferences preferences;

    private OnFragmentInteractionListener mListener;

    public BiodataFragment(ViewPager viewPager) {
        // Required empty public constructor
        this.viewPager = viewPager;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BiodataFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BiodataFragment newInstance(String param1, String param2) {
        BiodataFragment fragment = new BiodataFragment(viewPager);
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
        View v = inflater.inflate(R.layout.fragment_biodata, container, false);

        preferences = getActivity().getSharedPreferences("SignUpDetails", Context.MODE_PRIVATE);
        final SharedPreferences.Editor myEdit = preferences.edit();

        edtname = v.findViewById(R.id.edtname);
        edtemail = v.findViewById(R.id.edtemail);
        edtpassword = v.findViewById(R.id.edtpassword);
        edtdob = v.findViewById(R.id.edtdob);
        edtphonenum = v.findViewById(R.id.edtphonenum);

        radioGroup = v.findViewById(R.id.radioGender);
        male = v.findViewById(R.id.radioMale);
        female = v.findViewById(R.id.radioFemale);


        layname = v.findViewById(R.id.imgname);
        layemail = v.findViewById(R.id.imgemail);
        laypassword = v.findViewById(R.id.imgpassword);
        laygender = v.findViewById(R.id.imggender);
        laydob = v.findViewById(R.id.imgdob);
        laycountry = v.findViewById(R.id.imgcountry);
        layphonenum = v.findViewById(R.id.imgphonenum);

        labelname = v.findViewById(R.id.name_textinput_layout);
        labelemail = v.findViewById(R.id.email_textinput_layout);
        labelpassword = v.findViewById(R.id.password_textinput_layout);
        labelphonenum = v.findViewById(R.id.phonenum_textinput_layout);

        code = v.findViewById(R.id.code);

        next = v.findViewById(R.id.next);

        country = v.findViewById(R.id.spinnercountry);
        String[] countries = {"Please select a country...","Afghanistan", "Albania", "Algeria", "Angola", "Anguilla", "Antarctica", "Argentina", "Armenia", "Australia", "Austria",
                                            "Azerbaijan", "Bangladesh", "Barbados", "Belarus", "Belgium", "Benin", "Bolivia", "Botswana", "Brazil", "Bulgaria", "Burkina Faso", "Burundi",
                                            "Cameroon", "Canada", "Cape Verde", "Central African Republic", "Chad", "Chile", "China", "Colombia", "Congo", "Costa Rica", "Cote D'Ivoire",
                                            "Croatia", "Cuba", "Cyprus", "Czech Rep.", "Denmark", "Ecuador", "Egypt", "Equatorial Guinea", "Eritrea", "Ethiopia", "Faroe Island", "Fiji", "Finland",
                                            "France", "Gabon", "Gambia", "Germany", "Ghana", "Greece", "Guinea", "Guinea-Bissau", "Haiti", "Hoduras", "Hong Kong", "Hungary"};
        final String[] countriescode = {"","+93","+355","+213", "+244", "+1-264", "+672", "+54", "+374", "+61", "+43",
                                            "+994", "+880", "+1-246", "+375", "+32", "+229", "+591", "+267", "+55", "+359", "+226", "+257",
                                            "+237", "+1", "+238", "+236", "+235", "+56", "+86", "+57", "+243", "+506", "+225",
                                            "+385", "+53", "+357", "+420", "+45", "+593", "+20", "+240", "+291", "+251", "+298", "+679", "+358",
                                            "+33", "+241", "+220", "+49", "+233", "+30", "+224", "+245", "+509", "+504", "+852", "+36"};
        ArrayAdapter<String> countryAdapter = new ArrayAdapter<String>(getContext(), R.layout.simple_spinner_small_text, R.id.tx, countries) ;
        country.setAdapter(countryAdapter);
        country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                if (!country.getSelectedItem().toString().isEmpty()){
//                    laycountry.setImageResource(R.drawable.tick);
//                }
                if (position != 0){
                    laycountry.setImageResource(R.drawable.tick);
                    code.setText(countriescode[position]);
                }else{
                    laycountry.setImageResource(R.drawable.whitedot);
                    code.setText(countriescode[0]);
                    layphonenum.setImageResource(R.drawable.whitedot);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        labelname.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()>5){
                    layname.setImageResource(R.drawable.tick);
                } else{
                    layname.setImageResource(R.drawable.whitedot);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        labelemail.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().contains("@") && s.toString().contains(".")){
                    layemail.setImageResource(R.drawable.tick);
                }else{
                    layemail.setImageResource(R.drawable.whitedot);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        labelpassword.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()>=6){
                    laypassword.setImageResource(R.drawable.tick);
                }else{
                    laypassword.setImageResource(R.drawable.whitedot);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        labelphonenum.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()>=10){
                    layphonenum.setImageResource(R.drawable.tick);
                }else{
                    layphonenum.setImageResource(R.drawable.whitedot);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edtdob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // calender class's instance and get current date , month and year from calender
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                datePickerDialog = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                edtdob.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

                                if (!edtdob.getText().toString().isEmpty()){
                                    laydob.setImageResource(R.drawable.tick);
                                }else{
                                    laydob.setImageResource(R.drawable.whitedot);
                                }

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(layname.getDrawable().getConstantState().equals(getResources().getDrawable(R.drawable.tick).getConstantState())&&
                        layemail.getDrawable().getConstantState().equals(getResources().getDrawable(R.drawable.tick).getConstantState()) &&
                        laypassword.getDrawable().getConstantState().equals(getResources().getDrawable(R.drawable.tick).getConstantState()) &&
                        laydob.getDrawable().getConstantState().equals(getResources().getDrawable(R.drawable.tick).getConstantState()) &&
                        laycountry.getDrawable().getConstantState().equals(getResources().getDrawable(R.drawable.tick).getConstantState()) &&
                        layphonenum.getDrawable().getConstantState().equals(getResources().getDrawable(R.drawable.tick).getConstantState()))
                {
                    // get gender from radioGroup
                    if(male.isChecked()){
                        gender = male.getText().toString();
                    }
                    else if(female.isChecked()){
                        gender = female.getText().toString();
                    }

                    myEdit.putString("fullname", edtname.getText().toString());
                    myEdit.putString("email", edtemail.getText().toString());
                    myEdit.putString("password", edtpassword.getText().toString());
                    myEdit.putString("gender", gender);
                    myEdit.putString("dob", edtdob.getText().toString());
                    myEdit.putString("country", country.getSelectedItem().toString());
                    myEdit.putString("phonenum", code.getText() + edtphonenum.getText().toString());
                    myEdit.commit();

//                    Toast.makeText(getActivity(), "Fullname: "+edtname.getText().toString()+"\nemail: "+edtemail.getText().toString()+"\nPassword : "+edtpassword.getText().toString()+"\nGender: "+gender+"\nDOB: "+edtdob.getText().toString()+"\nCountry: "+country.getSelectedItem().toString()+"\nPhone: "+code.getText() + edtphonenum.getText().toString(), Toast.LENGTH_LONG).show();

                    viewPager.setCurrentItem(1);

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
