package com.example.noticeapp.DepartmentsNotice;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.noticeapp.AppAction.MainActivity;
import com.example.noticeapp.HomePage.HomeFragment;
import com.example.noticeapp.Interfaces.BackListenerFragment;
import com.example.noticeapp.R;

public class EteFragment extends Fragment  implements BackListenerFragment {

    View views;
    Fragment fragment;
    FragmentTransaction fragmentTransaction;
    public static BackListenerFragment backBtnListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        views = inflater.inflate(R.layout.fragment_ete, container, false);

        return views;
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
        fragment = new HomeFragment();
        fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
        fragmentTransaction.replace(R.id.fragmentID, fragment, "MY_FRAGMENT");
        fragmentTransaction.commit();
    }
}
