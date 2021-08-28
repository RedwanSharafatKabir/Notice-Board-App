package com.example.noticeapp.AppAction;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.noticeapp.Authentication.LoginActivity;
import com.example.noticeapp.HomePage.HomeFragment;
import com.example.noticeapp.Interfaces.BackListenerFragment;
import com.example.noticeapp.Notification.NotificationActivity;
import com.example.noticeapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnItemSelectedListener,
        View.OnClickListener{

    public TextView actionBarText;
    public ImageView backPage;
    ImageView notificationBtn;
    BottomNavigationView bottomNavigationView;
    View parentLayout;
    ConnectivityManager cm;
    NetworkInfo netInfo;
    Fragment fragment;
    Snackbar snackbar;
    FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        backPage = findViewById(R.id.backPageId);
        actionBarText = findViewById(R.id.actionBarTextId);

        cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        netInfo = cm.getActiveNetworkInfo();
        parentLayout = findViewById(android.R.id.content);

        notificationBtn = findViewById(R.id.notificationId);
        notificationBtn.setOnClickListener(this);
        bottomNavigationView = findViewById(R.id.bottomNavigationID);
        bottomNavigationView.setOnItemSelectedListener(this);

        actionBarText.setText("Home");
        fragment = new HomeFragment();
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragmentID, fragment, "MY_FRAGMENT");
        fragmentTransaction.commit();

        try {
            switch (getIntent().getStringExtra("EXTRA")) {
                case "openHomeFragment":
                    fragment = new HomeFragment();
                    fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.fragmentID, fragment, "MY_FRAGMENT");
                    fragmentTransaction.commit();
                    break;
            }
        } catch (Exception e){
            Log.i("Error ", e.getMessage());
        }
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.notificationId){
            Intent intent = new Intent(MainActivity.this, NotificationActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.homeID:
                if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                    backPage.setImageDrawable(null);
                    actionBarText.setText("Home");
                    fragment = new HomeFragment();
                    fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.fragmentID, fragment, "MY_FRAGMENT");
                    fragmentTransaction.commit();
                } else {
                    snackbar = Snackbar.make(parentLayout, "Turn on internet connection", Snackbar.LENGTH_LONG);
                    View sbView = snackbar.getView();
                    sbView.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.Red));
                    snackbar.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE);
                    snackbar.setDuration(10000).show();
                }
                break;

            case R.id.aboutID:
                if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                    actionBarText.setText("About");
                    fragment = new AboutFragment();
                    fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.fragmentID, fragment);
                    fragmentTransaction.commit();
                } else {
                    snackbar = Snackbar.make(parentLayout, "Turn on internet connection", Snackbar.LENGTH_LONG);
                    View sbView = snackbar.getView();
                    sbView.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.Red));
                    snackbar.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE);
                    snackbar.setDuration(10000).show();
                }
                break;

            case R.id.profileID:
                if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                    actionBarText.setText("Profile");
                    fragment = new ProfileFragment();
                    fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.fragmentID, fragment);
                    fragmentTransaction.commit();
                } else {
                    snackbar = Snackbar.make(parentLayout, "Turn on internet connection", Snackbar.LENGTH_LONG);
                    View sbView = snackbar.getView();
                    sbView.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.Red));
                    snackbar.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE);
                    snackbar.setDuration(10000).show();
                }
                break;
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        if(HomeFragment.backBtnListener!=null){
            HomeFragment.backBtnListener.onBackPressed();
        }

        if(AboutFragment.backBtnListener!=null){
            AboutFragment.backBtnListener.onBackPressed();
        }

        if(ProfileFragment.backBtnListener!=null){
            ProfileFragment.backBtnListener.onBackPressed();
        }
    }
}
