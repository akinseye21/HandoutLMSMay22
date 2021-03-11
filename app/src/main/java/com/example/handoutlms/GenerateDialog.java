package com.example.handoutlms;

import android.app.Dialog;
import android.content.Context;

import androidx.annotation.NonNull;

public class GenerateDialog extends Dialog {
    public GenerateDialog(@NonNull Context context) {
        super(context);
        setContentView(R.layout.custom_tutor_popup);
    }
}
