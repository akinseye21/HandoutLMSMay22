package com.example.handoutlms.adapters;

import android.content.Context;
import android.os.Build;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.handoutlms.R;

import java.util.ArrayList;

public class RemoteJobAdapter extends BaseAdapter {

    Context context;
    ArrayList<String> arr_date;
    ArrayList<String> arr_company;
    ArrayList<String> arr_company_logo;
    ArrayList<String> arr_position;
    ArrayList<String> arr_logo;
    ArrayList<String> arr_description;
    ArrayList<String> arr_location;
    ArrayList<String> arr_salary_min;
    ArrayList<String> arr_salary_max;
    ArrayList<String> arr_url;
    ArrayList<String> arr_apply_url;
    ArrayList<String> arr_tags;

    public RemoteJobAdapter(Context context, ArrayList<String> arr_date, ArrayList<String> arr_company, ArrayList<String> arr_company_logo, ArrayList<String> arr_position, ArrayList<String> arr_logo, ArrayList<String> arr_description, ArrayList<String> arr_location, ArrayList<String> arr_salary_min, ArrayList<String> arr_salary_max, ArrayList<String> arr_url, ArrayList<String> arr_apply_url, ArrayList<String> arr_tags){
        this.context = context;
        this.arr_date = arr_date;
        this.arr_company = arr_company;
        this.arr_company_logo = arr_company_logo;
        this.arr_position = arr_position;
        this.arr_logo = arr_logo;
        this.arr_description = arr_description;
        this.arr_location = arr_location;
        this.arr_salary_min = arr_salary_min;
        this.arr_salary_max = arr_salary_max;
        this.arr_url = arr_url;
        this.arr_apply_url = arr_apply_url;
        this.arr_tags = arr_tags;
    }

    @Override
    public int getCount() {
        return arr_company.size();
    }

    @Override
    public Object getItem(int i) {
        return arr_company.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflaInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = inflaInflater.inflate(R.layout.list_remote_job, parent, false);
        }

        ImageView companylogo = convertView.findViewById(R.id.companylogo);
        TextView companyname = convertView.findViewById(R.id.companyname);
        TextView date = convertView.findViewById(R.id.date);
        TextView location = convertView.findViewById(R.id.location);
        TextView jobposition = convertView.findViewById(R.id.position);
        TextView salary_min = convertView.findViewById(R.id.salarymin);
        TextView salary_max = convertView.findViewById(R.id.salarymax);
        TextView description = convertView.findViewById(R.id.description);
        Button apply = convertView.findViewById(R.id.apply);

        Glide.with(context).load(arr_company_logo.get(position)).into(companylogo);
        companyname.setText(arr_company.get(position));
        date.setText("Date: "+arr_date.get(position).charAt(0)+""+arr_date.get(position).charAt(1)+""+
                arr_date.get(position).charAt(2)+""+arr_date.get(position).charAt(3)+""+
                arr_date.get(position).charAt(4)+""+arr_date.get(position).charAt(5)+""+
                arr_date.get(position).charAt(6)+""+arr_date.get(position).charAt(7)+""+
                arr_date.get(position).charAt(8)+""+arr_date.get(position).charAt(9));
        location.setText("Location: "+arr_location.get(position));
        salary_min.setText(arr_salary_min.get(position));
        salary_max.setText(arr_salary_max.get(position));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            jobposition.setText(Html.fromHtml(arr_position.get(position), Html.FROM_HTML_MODE_LEGACY));
            description.setText(Html.fromHtml(arr_description.get(position), Html.FROM_HTML_MODE_LEGACY));
        } else {
            jobposition.setText(Html.fromHtml(arr_position.get(position)));
            description.setText(Html.fromHtml(arr_description.get(position)));
        }

        return convertView;
    }
}
