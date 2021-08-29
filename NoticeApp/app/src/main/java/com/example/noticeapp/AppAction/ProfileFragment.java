package com.example.noticeapp.AppAction;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.noticeapp.Authentication.ResetPassword;
import com.example.noticeapp.HomePage.HomeFragment;
import com.example.noticeapp.Interfaces.BackListenerFragment;
import com.example.noticeapp.R;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment implements BackListenerFragment, View.OnClickListener{

//    FirebaseAuth mAuth;
//    StorageReference storageReference;
    View views, parentLayout;
    ConnectivityManager cm;
    NetworkInfo netInfo;
    Snackbar snackbar;
    ProgressDialog dialog;
    ProgressBar progressBar;
    Fragment fragment;
    FragmentTransaction fragmentTransaction;
    public static BackListenerFragment backBtnListener;
    CardView changePassword, changePicture, logoutBtn;
    CircleImageView profilePic;
    private Uri uriProfileImage;
    TextView nameText, emailText, phoneText;
    String userEmail="";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        views = inflater.inflate(R.layout.fragment_profile, container, false);

        parentLayout = views.findViewById(android.R.id.content);

        changePassword = views.findViewById(R.id.changePasswordId);
        changePassword.setOnClickListener(this);
        changePicture = views.findViewById(R.id.uploadProfilePicId);
        changePicture.setOnClickListener(this);

        profilePic = views.findViewById(R.id.profilePicId);
        nameText = views.findViewById(R.id.profileNameId);
        emailText = views.findViewById(R.id.profileEmailId);
        phoneText = views.findViewById(R.id.profilePhoneId);

        progressBar = views.findViewById(R.id.profileProgressbarId);
        progressBar.setVisibility(View.VISIBLE);

        cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        netInfo = cm.getActiveNetworkInfo();

        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            getUserData();
        } else {
            snackbar = Snackbar.make(parentLayout, "Turn on internet connection", Snackbar.LENGTH_LONG);
            View sbView = snackbar.getView();
            sbView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.Red));
            snackbar.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE);
            snackbar.setDuration(10000).show();
        }

        return views;
    }

    private void getUserData(){
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.changePasswordId){
            Bundle armgs = new Bundle();
            armgs.putString("email_key", userEmail);

            ResetPassword resetPassword = new ResetPassword();
            resetPassword.setArguments(armgs);
            resetPassword.show(getActivity().getSupportFragmentManager(), "Sample dialog");
        }
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
        ((MainActivity)getActivity()).actionBarText.setText("Home");
        fragment = new HomeFragment();
        fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
        fragmentTransaction.replace(R.id.fragmentID, fragment, "MY_FRAGMENT");
        fragmentTransaction.commit();
    }
}

