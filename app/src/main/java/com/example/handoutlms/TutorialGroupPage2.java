package com.example.handoutlms;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TutorialGroupPage2.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TutorialGroupPage2#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TutorialGroupPage2 extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    Spinner courses, quiz;
    String selected_course;
    LinearLayout create_course, create_quiz, invite;

    private OnFragmentInteractionListener mListener;

    public TutorialGroupPage2() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TutorialGroupPage2.
     */
    // TODO: Rename and change types and number of parameters
    public static TutorialGroupPage2 newInstance(String param1, String param2) {
        TutorialGroupPage2 fragment = new TutorialGroupPage2();
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
        View v = inflater.inflate(R.layout.fragment_tutorial_group_page2, container, false);

        courses = v.findViewById(R.id.spinner_courses);
        quiz = v.findViewById(R.id.spinner_quiz);
        create_course = v.findViewById(R.id.click_create_course);
        create_quiz = v.findViewById(R.id.click_create_quiz);
        invite = v.findViewById(R.id.invite);

        String[] courseArray = {"All Available courses","Graphics Design Simplicity", "Mobile App Dev", "Web Development"};
        String[] quizArray = {"View Quizzes", "Quiz 1", "Quiz 2", "Quiz 3"};
        ArrayAdapter<String> coursesAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_courses_quiz, R.id.course_name, courseArray) ;
        ArrayAdapter<String> quizAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_quiz, R.id.quiz_name, quizArray) ;
        courses.setAdapter(coursesAdapter);
        quiz.setAdapter(quizAdapter);

        create_course.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //load create course fragment
                loadFragment(new CreateCourses());
            }
        });
        create_quiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new CreateQuiz());
            }
        });
        invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //load create course fragment
                loadFragment(new InviteStudents());
            }
        });

        courses.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected_course = String.valueOf(courses.getSelectedItem());
                if(position == 0){
                    courses.setSelection(0);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        quiz.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    quiz.setSelection(0);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return v;
    }


    private void loadFragment(Fragment fragment) {
        //create fragment manager
        FragmentManager fm = getFragmentManager();
        // create a FragmentTransaction to begin the transaction and replace the Fragment
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        // replace the LinearLayout with new Fragment
        fragmentTransaction.replace(R.id.frame_tut_page2, fragment);
        fragmentTransaction.addToBackStack("tutorial group page 2");
        fragmentTransaction.commit(); // save the changes
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
