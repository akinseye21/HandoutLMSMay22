package com.example.handoutlms;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HandoutTrivia extends AppCompatActivity implements
CountryRank.OnFragmentInteractionListener,
InstitutionRank.OnFragmentInteractionListener{

    ImageView back;
    ImageView fraggame;

    LinearLayout soccer, entertainment, politics, history;
    LinearLayout maths, english, logic, physics;
    LinearLayout computer, movies, animals, fashion;
    LinearLayout leaderboards;
    LinearLayout fragcategory, fragleaderboard;
    LinearLayout lincountry, lininstitution, linprofile;

    RelativeLayout relavatar;
    RelativeLayout relbeginner, relamateur, relhustler, relpro, relexpert;
    RelativeLayout relveteran, relmaster, relgrandmaster, relking, relemperor;

    LinearLayout nigExam;

    TextView textinbox;
    GridView gridView;

    ViewPager viewPager;
    TabLayout tabLayout;

    Dialog myDialog;
    ArrayList<String> Array_examType = new ArrayList<>();
    ProgressBar progressBar;

    String text;
//    TextView correctAnswer, ranking;

    public static final String RANK_URL = "http://35.84.44.203/handouts/trivia/user_rank";
    public static final String NIGERIA_EXAMS = "https://handoutng.com/handouts/handout_examtype";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handout_trivia);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), FeedsDashboard.class);
                startActivity(i);
            }
        });

        //get sharedpreference
        SharedPreferences sp = getSharedPreferences("LoginDetails", MODE_PRIVATE);
        final String email = sp.getString("email", "");

        relbeginner = findViewById(R.id.relbeginner);
        relamateur = findViewById(R.id.relamateur);
        relhustler = findViewById(R.id.relhustler);
        relpro = findViewById(R.id.relpro);
        relexpert = findViewById(R.id.relexpert);
        relveteran = findViewById(R.id.relveteran);
        relmaster = findViewById(R.id.relmaster);
        relgrandmaster = findViewById(R.id.relgrandmaster);
        relking = findViewById(R.id.relking);
        relemperor = findViewById(R.id.relemperor);
        nigExam = findViewById(R.id.nigExams);

        fraggame = findViewById(R.id.fraggame);
        linprofile = findViewById(R.id.linprofile);
        relavatar = findViewById(R.id.relavatar);
        textinbox = findViewById(R.id.textinbox);
        leaderboards = findViewById(R.id.leaderboards);
        fragcategory = findViewById(R.id.fragcategory);
        fragleaderboard = findViewById(R.id.fragleaderboard);
        lincountry = findViewById(R.id.lincountry);
        lininstitution = findViewById(R.id.lininstitution);
        viewPager = findViewById(R.id.viewpager);
        tabLayout = findViewById(R.id.tabs);
        gridView = findViewById(R.id.simpleGridView);
        progressBar = findViewById(R.id.progressBar);
        addTabs(viewPager);


        //get Nigerian Exams
        StringRequest stringRequest = new StringRequest(Request.Method.GET, NIGERIA_EXAMS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            int len = jsonArray.length();

                            for(int i =0; i<len; i++){
                                JSONObject myResp = jsonArray.getJSONObject(i);
                                String examType = myResp.getString("examType");

                                Array_examType.add(examType);
                            }


                            //populate values on the gridview
                            NigeriaExamAdapter nigeriaExamAdapter = new NigeriaExamAdapter(getApplicationContext(), Array_examType);
                            gridView.setAdapter(nigeriaExamAdapter);

                            progressBar.setVisibility(View.GONE);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressBar.setVisibility(View.GONE);
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        progressBar.setVisibility(View.GONE);
                    }
                }){
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(HandoutTrivia.this);
        DefaultRetryPolicy retryPolicy = new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);
        requestQueue.add(stringRequest);





        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setOnTabSelectedListener(
                new TabLayout.ViewPagerOnTabSelectedListener(viewPager) {

                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        if (tab.getPosition() == 0){
                            lincountry.setVisibility(View.VISIBLE);
                            lininstitution.setVisibility(View.INVISIBLE);
                        }
                        else if(tab.getPosition() == 1){
                            lincountry.setVisibility(View.INVISIBLE);
                            lininstitution.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {

                    }
                }
        );


        TextView username = findViewById(R.id.username);
//        correctAnswer = findViewById(R.id.numCorrectAns);
//        ranking = findViewById(R.id.ranking);

        username.setText(email);

        soccer = findViewById(R.id.soccer);
        entertainment = findViewById(R.id.entertainment);
        politics = findViewById(R.id.politics);
        history = findViewById(R.id.history);
        maths = findViewById(R.id.math);
        english = findViewById(R.id.english);
        logic = findViewById(R.id.logic);
        physics = findViewById(R.id.physics);
        computer = findViewById(R.id.computer);
        movies = findViewById(R.id.movies);
        animals = findViewById(R.id.animals);
        fashion = findViewById(R.id.fashion);




        soccer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                text = "Soccer";
                Intent mv = new Intent(HandoutTrivia.this, TriviaInstruction.class);
                mv.putExtra("Text", text);
                mv.putExtra("From", "normal");
                mv.putExtra("ExamType", "");
                startActivity(mv);
            }
        });
        entertainment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                text = "Entertainment";
                Intent mv = new Intent(HandoutTrivia.this, TriviaInstruction.class);
                mv.putExtra("Text", text);
                mv.putExtra("From", "normal");
                mv.putExtra("ExamType", "");
                startActivity(mv);
            }
        });
        politics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                text = "Politics";
                Intent mv = new Intent(HandoutTrivia.this, TriviaInstruction.class);
                mv.putExtra("Text", text);
                mv.putExtra("From", "normal");
                mv.putExtra("ExamType", "");
                startActivity(mv);
            }
        });
        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                text = "History";
                Intent mv = new Intent(HandoutTrivia.this, TriviaInstruction.class);
                mv.putExtra("Text", text);
                mv.putExtra("From", "normal");
                mv.putExtra("ExamType", "");
                startActivity(mv);
            }
        });
        maths.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                text = "Maths";
                Intent mv = new Intent(HandoutTrivia.this, TriviaInstruction.class);
                mv.putExtra("Text", text);
                mv.putExtra("From", "normal");
                mv.putExtra("ExamType", "");
                startActivity(mv);
            }
        });
        english.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                text = "English";
                Intent mv = new Intent(HandoutTrivia.this, TriviaInstruction.class);
                mv.putExtra("Text", text);
                mv.putExtra("From", "normal");
                mv.putExtra("ExamType", "");
                startActivity(mv);
            }
        });
        logic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                text = "Logic";
                Intent mv = new Intent(HandoutTrivia.this, TriviaInstruction.class);
                mv.putExtra("Text", text);
                mv.putExtra("From", "normal");
                mv.putExtra("ExamType", "");
                startActivity(mv);
            }
        });
        physics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                text = "Physics";
                Intent mv = new Intent(HandoutTrivia.this, TriviaInstruction.class);
                mv.putExtra("Text", text);
                mv.putExtra("From", "normal");
                mv.putExtra("ExamType", "");
                startActivity(mv);
            }
        });
        computer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                text = "Computer";
                Intent mv = new Intent(HandoutTrivia.this, TriviaInstruction.class);
                mv.putExtra("Text", text);
                mv.putExtra("From", "normal");
                mv.putExtra("ExamType", "");
                startActivity(mv);
            }
        });
        movies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                text = "Movies";
                Intent mv = new Intent(HandoutTrivia.this, TriviaInstruction.class);
                mv.putExtra("Text", text);
                mv.putExtra("From", "normal");
                mv.putExtra("ExamType", "");
                startActivity(mv);
            }
        });
        animals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                text = "Animals";
                Intent mv = new Intent(HandoutTrivia.this, TriviaInstruction.class);
                mv.putExtra("Text", text);
                mv.putExtra("From", "normal");
                mv.putExtra("ExamType", "");
                startActivity(mv);
            }
        });
        fashion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                text = "Fashion";
                Intent mv = new Intent(HandoutTrivia.this, TriviaInstruction.class);
                mv.putExtra("Text", text);
                mv.putExtra("From", "normal");
                mv.putExtra("ExamType", "");
                startActivity(mv);
            }
        });

        leaderboards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragcategory.setVisibility(View.GONE);
                nigExam.setVisibility(View.GONE);
                linprofile.setVisibility(View.GONE);
                fragleaderboard.setVisibility(View.VISIBLE);
                textinbox.setText("LEADERBOARD");
            }
        });
        relavatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragcategory.setVisibility(View.GONE);
                nigExam.setVisibility(View.GONE);
                linprofile.setVisibility(View.VISIBLE);
                fragleaderboard.setVisibility(View.GONE);
                textinbox.setText("PROFILE");
            }
        });
        fraggame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragcategory.setVisibility(View.VISIBLE);
                nigExam.setVisibility(View.VISIBLE);
                linprofile.setVisibility(View.GONE);
                fragleaderboard.setVisibility(View.GONE);
                textinbox.setText("CATEGORIES");
            }
        });



        relamateur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialog = new Dialog(HandoutTrivia.this);
                myDialog.setContentView(R.layout.custom_popup_avatar_click);
                ImageView img = myDialog.findViewById(R.id.img);
                TextView coin_num = myDialog.findViewById(R.id.num_coin);
                Button close = myDialog.findViewById(R.id.close);
                img.setImageResource(R.drawable.amateur);
                coin_num.setText("100 coins");
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        myDialog.dismiss();
                    }
                });
                myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                myDialog.setCanceledOnTouchOutside(true);
                myDialog.show();
            }
        });
        relhustler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialog = new Dialog(HandoutTrivia.this);
                myDialog.setContentView(R.layout.custom_popup_avatar_click);
                ImageView img = myDialog.findViewById(R.id.img);
                TextView coin_num = myDialog.findViewById(R.id.num_coin);
                Button close = myDialog.findViewById(R.id.close);
                img.setImageResource(R.drawable.hustler);
                coin_num.setText("500 coins");
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        myDialog.dismiss();
                    }
                });
                myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                myDialog.setCanceledOnTouchOutside(true);
                myDialog.show();
            }
        });
        relpro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialog = new Dialog(HandoutTrivia.this);
                myDialog.setContentView(R.layout.custom_popup_avatar_click);
                ImageView img = myDialog.findViewById(R.id.img);
                TextView coin_num = myDialog.findViewById(R.id.num_coin);
                Button close = myDialog.findViewById(R.id.close);
                img.setImageResource(R.drawable.pro);
                coin_num.setText("900 coins");
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        myDialog.dismiss();
                    }
                });
                myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                myDialog.setCanceledOnTouchOutside(true);
                myDialog.show();
            }
        });
        relexpert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialog = new Dialog(HandoutTrivia.this);
                myDialog.setContentView(R.layout.custom_popup_avatar_click);
                ImageView img = myDialog.findViewById(R.id.img);
                TextView coin_num = myDialog.findViewById(R.id.num_coin);
                Button close = myDialog.findViewById(R.id.close);
                img.setImageResource(R.drawable.expert);
                coin_num.setText("2,700 coins");
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        myDialog.dismiss();
                    }
                });
                myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                myDialog.setCanceledOnTouchOutside(true);
                myDialog.show();
            }
        });
        relveteran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialog = new Dialog(HandoutTrivia.this);
                myDialog.setContentView(R.layout.custom_popup_avatar_click);
                ImageView img = myDialog.findViewById(R.id.img);
                TextView coin_num = myDialog.findViewById(R.id.num_coin);
                Button close = myDialog.findViewById(R.id.close);
                img.setImageResource(R.drawable.veteran);
                coin_num.setText("6,300 coins");
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        myDialog.dismiss();
                    }
                });
                myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                myDialog.setCanceledOnTouchOutside(true);
                myDialog.show();
            }
        });
        relmaster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialog = new Dialog(HandoutTrivia.this);
                myDialog.setContentView(R.layout.custom_popup_avatar_click);
                ImageView img = myDialog.findViewById(R.id.img);
                TextView coin_num = myDialog.findViewById(R.id.num_coin);
                Button close = myDialog.findViewById(R.id.close);
                img.setImageResource(R.drawable.master);
                coin_num.setText("13,500 coins");
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        myDialog.dismiss();
                    }
                });
                myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                myDialog.setCanceledOnTouchOutside(true);
                myDialog.show();
            }
        });
        relgrandmaster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialog = new Dialog(HandoutTrivia.this);
                myDialog.setContentView(R.layout.custom_popup_avatar_click);
                ImageView img = myDialog.findViewById(R.id.img);
                TextView coin_num = myDialog.findViewById(R.id.num_coin);
                Button close = myDialog.findViewById(R.id.close);
                img.setImageResource(R.drawable.grandmaster);
                coin_num.setText("27,900 coins");
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        myDialog.dismiss();
                    }
                });
                myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                myDialog.setCanceledOnTouchOutside(true);
                myDialog.show();
            }
        });
        relking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialog = new Dialog(HandoutTrivia.this);
                myDialog.setContentView(R.layout.custom_popup_avatar_click);
                ImageView img = myDialog.findViewById(R.id.img);
                TextView coin_num = myDialog.findViewById(R.id.num_coin);
                Button close = myDialog.findViewById(R.id.close);
                img.setImageResource(R.drawable.king);
                coin_num.setText("56,700 coins");
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        myDialog.dismiss();
                    }
                });
                myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                myDialog.setCanceledOnTouchOutside(true);
                myDialog.show();
            }
        });
        relemperor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialog = new Dialog(HandoutTrivia.this);
                myDialog.setContentView(R.layout.custom_popup_avatar_click);
                ImageView img = myDialog.findViewById(R.id.img);
                TextView coin_num = myDialog.findViewById(R.id.num_coin);
                Button close = myDialog.findViewById(R.id.close);
                img.setImageResource(R.drawable.emperor);
                coin_num.setText("114,300 coins");
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        myDialog.dismiss();
                    }
                });
                myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                myDialog.setCanceledOnTouchOutside(true);
                myDialog.show();
            }
        });


//        //send info to database to get ranks
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, RANK_URL,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        System.out.println("User ranking array = "+response);
//                        JSONObject jsonObject;
//                        try {
//                            jsonObject = new JSONObject(response);
//                            String rank = jsonObject.getString("overall_rank");
//                            String test_completed = jsonObject.getString("overall_test_completed");
//
////                            ranking.setText(rank);
////                            correctAnswer.setText(test_completed);
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
////                        Toast.makeText(getApplicationContext(), "Network connectivity problem", Toast.LENGTH_LONG).show();
//                    }
//                }){
//            @Override
//            protected Map<String, String> getParams(){
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("email", email);
//                return params;
//            }
//        };
//        RequestQueue requestQueue = Volley.newRequestQueue(HandoutTrivia.this);
//        requestQueue.add(stringRequest);
    }

    private void addTabs(ViewPager viewPager) {
        HandoutTrivia.ViewPagerAdapter adapter = new HandoutTrivia.ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new CountryRank(), "");
        adapter.addFrag(new InstitutionRank(), "");
        viewPager.setAdapter(adapter);
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

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
