package com.example.handoutlms.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.handoutlms.R;
import com.example.handoutlms.activities.TriviaInstruction;

import java.util.ArrayList;

public class VoucherListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<String> status;
    private  ArrayList<String> code;


    public VoucherListAdapter(Context context, ArrayList<String> status, ArrayList<String> code){
        //Getting all the values
        this.context = context;
        this.status = status;
        this.code = code;
    }

    @Override
    public int getCount() {
        return status.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        LayoutInflater inflaInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = inflaInflater.inflate(R.layout.list_voucher, parent, false);
        }

        RadioButton radio = convertView.findViewById(R.id.radio);
        TextView txtCode = convertView.findViewById(R.id.code);

        txtCode.setText(code.get(i));
        if (!status.get(i).equals("unused")){
            radio.setChecked(false);
        }
        return convertView;
    }
}
