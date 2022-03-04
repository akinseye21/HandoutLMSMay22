package com.example.handoutlms;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

public class EditProfilePage extends AppCompatActivity {

    ImageView back;
    Dialog myDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_page);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void EditUserInfo(View view) {
        myDialog = new Dialog(EditProfilePage.this);
        myDialog.setContentView(R.layout.custom_popup_edit_userinfo);
        // Setting dialogview
        Window window = myDialog.getWindow();
        window.setGravity(Gravity.CENTER);
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.setCanceledOnTouchOutside(true);
        myDialog.show();
    }

    public void AddEducation(View view) {
        myDialog = new Dialog(EditProfilePage.this);
        myDialog.setContentView(R.layout.custom_popup_add_education);
        // Setting dialogview
        Window window = myDialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.setCanceledOnTouchOutside(true);
        myDialog.show();
    }

    public void AddExperience(View view) {
        myDialog = new Dialog(EditProfilePage.this);
        myDialog.setContentView(R.layout.custom_popup_add_experience);
        // Setting dialogview
        Window window = myDialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.setCanceledOnTouchOutside(true);
        myDialog.show();
    }
}
