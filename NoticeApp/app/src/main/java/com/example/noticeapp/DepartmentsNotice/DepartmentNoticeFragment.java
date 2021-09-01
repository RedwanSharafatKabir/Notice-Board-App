package com.example.noticeapp.DepartmentsNotice;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.noticeapp.Adapters.DepartmentNoticeAdapter;
import com.example.noticeapp.Adapters.GeneralNoticeAdapter;
import com.example.noticeapp.AppAction.MainActivity;
import com.example.noticeapp.HomePage.HomeFragment;
import com.example.noticeapp.Interfaces.BackListenerFragment;
import com.example.noticeapp.ModelClasses.StoreDepartmentNoticeData;
import com.example.noticeapp.ModelClasses.StoreGeneralNoticeData;
import com.example.noticeapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DepartmentNoticeFragment extends Fragment implements BackListenerFragment {

    View views;
    Fragment fragment;
    TextView departmentNameText;
    FragmentTransaction fragmentTransaction;
    public static BackListenerFragment backBtnListener;
    String departmentName;
    RecyclerView recyclerView;
    ArrayList<StoreDepartmentNoticeData> storeDepartmentNoticeDataArrayList;
    DepartmentNoticeAdapter departmentNoticeAdapter;
    ProgressBar progressBar;
    DatabaseReference databaseReference;
    Parcelable recyclerViewState;
    ConnectivityManager cm;
    NetworkInfo netInfo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        views = inflater.inflate(R.layout.fragment_department_notice, container, false);

        ((MainActivity) getActivity()).setBtnVisibility();
        departmentName = getArguments().getString("dept_Key");
        departmentNameText = views.findViewById(R.id.departmentNameId);

        if(departmentName.equals("ETE")){
            departmentNameText.setText(getResources().getText(R.string.ete_name));
        } else if(departmentName.equals("CSE")){
            departmentNameText.setText(getResources().getText(R.string.cse_name));
        } else if(departmentName.equals("SWE")){
            departmentNameText.setText(getResources().getText(R.string.swe_name));
        } else  if(departmentName.equals("NFE")){
            departmentNameText.setText(getResources().getText(R.string.nfe_name));
        } else if(departmentName.equals("English")){
            departmentNameText.setText(getResources().getText(R.string.english_name));
        } else if(departmentName.equals("Physics")){
            departmentNameText.setText(getResources().getText(R.string.phy_name));
        } else if(departmentName.equals("Mathematics")){
            departmentNameText.setText(getResources().getText(R.string.math_name));
        } else if(departmentName.equals("EEE")){
            departmentNameText.setText(getResources().getText(R.string.eee_name));
        } else if(departmentName.equals("BBA")){
            departmentNameText.setText(getResources().getText(R.string.bba_name));
        }

        cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        netInfo = cm.getActiveNetworkInfo();
        progressBar = views.findViewById(R.id.noticeListProgressbarId2);

        recyclerView = views.findViewById(R.id.departmentNoticeRecyclerViewId);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                recyclerViewState = recyclerView.getLayoutManager().onSaveInstanceState();
            }
        });

        storeDepartmentNoticeDataArrayList = new ArrayList<StoreDepartmentNoticeData>();
        databaseReference = FirebaseDatabase.getInstance().getReference("Department Notice");

        getDeptNotice();

        return views;
    }

    private void getDeptNotice(){
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            try {
                databaseReference.child(departmentName).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        storeDepartmentNoticeDataArrayList.clear();

                        for (DataSnapshot item : snapshot.getChildren()) {
                            StoreDepartmentNoticeData storeDepartmentNoticeData = item.getValue(StoreDepartmentNoticeData.class);
                            storeDepartmentNoticeDataArrayList.add(storeDepartmentNoticeData);
                        }

                        departmentNoticeAdapter = new DepartmentNoticeAdapter(getActivity(), storeDepartmentNoticeDataArrayList);
                        recyclerView.setAdapter(departmentNoticeAdapter);
                        departmentNoticeAdapter.notifyDataSetChanged();
                        recyclerView.getLayoutManager().onRestoreInstanceState(recyclerViewState);

                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        progressBar.setVisibility(View.GONE);
                    }
                });

            } catch (Exception e){
                progressBar.setVisibility(View.GONE);
                Log.i("Error ", e.getMessage());
            }
        }

        else {
            Toast.makeText(getActivity(), "Turn on internet connection", Toast.LENGTH_LONG).show();
            progressBar.setVisibility(View.GONE);
        }

        refresh(1000);
    }

    private void refresh(int milliSecond){
        final Handler handler = new Handler(Looper.getMainLooper());

        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                getDeptNotice();
            }
        };

        handler.postDelayed(runnable, milliSecond);
    }

    @Override
    public void onResume() {
        super.onResume();
        backBtnListener = this;
    }

    @Override
    public void onPause() {
        backBtnListener = null;
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        ((MainActivity)getActivity()).actionBarText.setText("General Notice");
        ((MainActivity) getActivity()).setBtnVisibility();
        fragment = new HomeFragment();
        fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
        fragmentTransaction.replace(R.id.fragmentID, fragment, "MY_FRAGMENT");
        fragmentTransaction.commit();
    }
}
