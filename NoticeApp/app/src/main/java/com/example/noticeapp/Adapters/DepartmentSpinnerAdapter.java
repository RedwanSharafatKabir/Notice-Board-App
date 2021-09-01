package com.example.noticeapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.noticeapp.R;

public class DepartmentSpinnerAdapter extends BaseAdapter {

    String department[];
    Context context;
    LayoutInflater layoutInflater;

    public DepartmentSpinnerAdapter(Context context, String[] department){
        this.context = context;
        this.department = department;
    }

    @Override
    public int getCount() {
        return department.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View SpinnerView, ViewGroup parent) {
        if(SpinnerView==null){
            layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            SpinnerView = layoutInflater.inflate(R.layout.department_spinner_adapter, parent, false);
        }

        TextView divisionTextView = SpinnerView.findViewById(R.id.departmentTextViewId);

        divisionTextView.setText(department[position]);

        return SpinnerView;
    }
}
