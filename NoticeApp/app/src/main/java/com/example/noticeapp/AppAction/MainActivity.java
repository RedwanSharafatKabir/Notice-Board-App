package com.example.noticeapp.AppAction;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
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
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnItemSelectedListener,
        View.OnClickListener, NavigationView.OnNavigationItemSelectedListener{

    DrawerLayout drawerLayout;
    public NavigationView navigationView;
    ActionBarDrawerToggle actionBarDrawerToggle;
    public TextView actionBarText;
    ImageView notificationBtn;
    BottomNavigationView bottomNavigationView;
    View parentLayout;
    Toolbar toolbar;
    ConnectivityManager cm;
    NetworkInfo netInfo;
    Fragment fragment;
    Snackbar snackbar;
    FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        actionBarText = findViewById(R.id.actionBarTextId);
        cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        netInfo = cm.getActiveNetworkInfo();
        parentLayout = findViewById(android.R.id.content);

        drawerLayout = findViewById(R.id.drawerID);
        toolbar = findViewById(R.id.toolBarID);

        setSupportActionBar(toolbar);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
                toolbar, R.string.drawerOpen, R.string.drawerClose);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        navigationView = findViewById(R.id.navigationViewID);
        navigationView.setItemIconTintList(null);
        navigationView.bringToFront();

        notificationBtn = findViewById(R.id.notificationId);
        notificationBtn.setOnClickListener(this);
        bottomNavigationView = findViewById(R.id.bottomNavigationID);
        bottomNavigationView.setOnItemSelectedListener(this);

        navigationView.setNavigationItemSelectedListener(MainActivity.this);

        actionBarText.setText("General Notice");
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
        drawerLayout.closeDrawer(GravityCompat.START,true);
        int id = item.getItemId();

        switch (id){
            case R.id.eteId:
                actionBarText.setText("ETE");
//                fragment = new EteFragment();
//                fragmentTransaction = getSupportFragmentManager().beginTransaction();
//                fragmentTransaction.replace(R.id.fragmentID, fragment);
//                fragmentTransaction.commit();
                break;

            case R.id.cseId:
                actionBarText.setText("CSE");
//                fragment = new EteFragment();
//                fragmentTransaction = getSupportFragmentManager().beginTransaction();
//                fragmentTransaction.replace(R.id.fragmentID, fragment);
//                fragmentTransaction.commit();
                break;

            case R.id.sweId:
                actionBarText.setText("SWE");
//                fragment = new EteFragment();
//                fragmentTransaction = getSupportFragmentManager().beginTransaction();
//                fragmentTransaction.replace(R.id.fragmentID, fragment);
//                fragmentTransaction.commit();
                break;

            case R.id.eeeId:
                actionBarText.setText("EEE");
//                fragment = new EteFragment();
//                fragmentTransaction = getSupportFragmentManager().beginTransaction();
//                fragmentTransaction.replace(R.id.fragmentID, fragment);
//                fragmentTransaction.commit();
                break;

            case R.id.bbaId:
                actionBarText.setText("BBA");
//                fragment = new EteFragment();
//                fragmentTransaction = getSupportFragmentManager().beginTransaction();
//                fragmentTransaction.replace(R.id.fragmentID, fragment);
//                fragmentTransaction.commit();
                break;

            case R.id.englishId:
                actionBarText.setText("English");
//                fragment = new EteFragment();
//                fragmentTransaction = getSupportFragmentManager().beginTransaction();
//                fragmentTransaction.replace(R.id.fragmentID, fragment);
//                fragmentTransaction.commit();
                break;

            case R.id.nfeId:
                actionBarText.setText("NFE");
//                fragment = new EteFragment();
//                fragmentTransaction = getSupportFragmentManager().beginTransaction();
//                fragmentTransaction.replace(R.id.fragmentID, fragment);
//                fragmentTransaction.commit();
                break;

            case R.id.physicsId:
                actionBarText.setText("Physics");
//                fragment = new EteFragment();
//                fragmentTransaction = getSupportFragmentManager().beginTransaction();
//                fragmentTransaction.replace(R.id.fragmentID, fragment);
//                fragmentTransaction.commit();
                break;

            case R.id.mathId:
                actionBarText.setText("Mathematics");
//                fragment = new EteFragment();
//                fragmentTransaction = getSupportFragmentManager().beginTransaction();
//                fragmentTransaction.replace(R.id.fragmentID, fragment);
//                fragmentTransaction.commit();
                break;

            case R.id.homeID:
                if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                    actionBarText.setText("General Notice");
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
