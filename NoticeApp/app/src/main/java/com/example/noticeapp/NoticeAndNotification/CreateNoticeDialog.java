package com.example.noticeapp.NoticeAndNotification;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.noticeapp.Adapters.DepartmentSpinnerAdapter;
import com.example.noticeapp.ModelClasses.StoreDepartmentNoticeData;
import com.example.noticeapp.ModelClasses.StoreGeneralNoticeData;
import com.example.noticeapp.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CreateNoticeDialog extends AppCompatDialogFragment implements View.OnClickListener{

    View view;
    EditText noticeTitleText, noticeDetailsText;
    Button create;
    DatabaseReference generalNoticeReference, departmentReference;
    ConnectivityManager cm;
    NetworkInfo netInfo;
    String department[], departmentName, noticeDate, noticeDay, noticeTime, dateForDay;
    Spinner departmentSpinner;
    DepartmentSpinnerAdapter departmentSpinnerAdapter;
    ProgressDialog dialog;
    Date date = null;
    SimpleDateFormat simpleDateFormat1, simpleDateFormat2;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.fragment_create_notice_dialog, null);
        builder.setView(view).setCancelable(false).setTitle("Create new notice")
                .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {}
                });

        cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        netInfo = cm.getActiveNetworkInfo();
        dialog = new ProgressDialog(getActivity());

        noticeTitleText = view.findViewById(R.id.noticeTitleInputID);
        noticeDetailsText = view.findViewById(R.id.noticeDetailsInputID);

        create = view.findViewById(R.id.createNoticeID);
        create.setOnClickListener(this);

        department = getResources().getStringArray(R.array.department_array);
        departmentSpinner = view.findViewById(R.id.departmentSpinnerId);

        departmentSpinnerAdapter = new DepartmentSpinnerAdapter(getActivity(), department);
        departmentSpinner.setAdapter(departmentSpinnerAdapter);

        generalNoticeReference = FirebaseDatabase.getInstance().getReference("General Notice");
        departmentReference = FirebaseDatabase.getInstance().getReference("Department Notice");

        departmentsMethod();

        return builder.create();
    }

    private void departmentsMethod() {
        departmentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                departmentName = department[position];
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    @Override
    public void onClick(View v) {
        String noticeTitle = noticeTitleText.getText().toString();
        String noticeDeails = noticeDetailsText.getText().toString();

        if(v.getId()==R.id.createNoticeID){
            // Get current date, day and time
            Date cal = Calendar.getInstance().getTime();
            simpleDateFormat1 = new SimpleDateFormat("dd-MMM-yyyy");
            simpleDateFormat2 = new SimpleDateFormat("dd/MM/yyyy");
            noticeDate = simpleDateFormat1.format(cal);
            dateForDay = simpleDateFormat2.format(cal);

            try {
                date = simpleDateFormat2.parse(dateForDay);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            DateFormat dayFormat = new SimpleDateFormat("EEEE");
            noticeDay = dayFormat.format(date);

            SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("hh:mm:ss aaa");
            noticeTime = simpleDateFormat2.format(new Date());

            // Prepare for save notice data
            if(noticeTitle.isEmpty()){
                noticeTitleText.setError("Enter notice title");
            }

            if(noticeDeails.isEmpty()){
                noticeDetailsText.setError("Enter notice description");
            }

            if(departmentName.equals("General Notice") && !noticeDeails.isEmpty() && !noticeTitle.isEmpty()){
                storeGeneralNoticeMethod(noticeTitle, noticeDate, noticeDay, noticeTime, noticeDeails);
            }

            else if(!departmentName.equals("General Notice") && !noticeDeails.isEmpty() && !noticeTitle.isEmpty()) {
                if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                    storeDepartmentNoticeMethod(departmentName, noticeTitle, noticeDate, noticeDay, noticeTime, noticeDeails);

                } else {
                    Toast.makeText(getActivity(), "Turn on internet connection", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private void storeGeneralNoticeMethod(String noticeTitle, String noticeDate, String noticeDay, String noticeTime, String noticeDeails) {

        StoreGeneralNoticeData storeGeneralNoticeData = new StoreGeneralNoticeData(noticeTitle, noticeDate, noticeDay, noticeTime, noticeDeails);
        generalNoticeReference.child(noticeTitle).setValue(storeGeneralNoticeData);

        Toast.makeText(getActivity(), "Notice added successfully", Toast.LENGTH_SHORT).show();
        getDialog().dismiss();
    }

    private void storeDepartmentNoticeMethod(String departmentName, String noticeTitle, String noticeDate, String noticeDay,
                                             String noticeTime, String noticeDeails) {

        StoreDepartmentNoticeData storeDepartmentNoticeData = new StoreDepartmentNoticeData(departmentName, noticeTitle, noticeDate, noticeDay,
                noticeTime, noticeDeails);

        departmentReference.child(departmentName).child(noticeTitle).setValue(storeDepartmentNoticeData);

        Toast.makeText(getActivity(), "Notice added successfully", Toast.LENGTH_SHORT).show();
        getDialog().dismiss();
    }
}
