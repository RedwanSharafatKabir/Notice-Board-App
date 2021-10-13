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

import com.example.noticeapp.DepartmentsNotice.DepartmentNoticeFragment;
import com.example.noticeapp.NoticeAndNotification.CreateNoticeDialog;
import com.example.noticeapp.HomePage.HomeFragment;
import com.example.noticeapp.NoticeAndNotification.NotificationActivity;
import com.example.noticeapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnItemSelectedListener,
        View.OnClickListener, NavigationView.OnNavigationItemSelectedListener{

    public TextView addNoticeBtn;
    DrawerLayout drawerLayout;
    public NavigationView navigationView;
    ActionBarDrawerToggle actionBarDrawerToggle;
    public TextView actionBarText;
    ImageView notificationBtn;
    BottomNavigationView bottomNavigationView;
    View parentLayout;
    Toolbar toolbar;
    ConnectivityManager cm;
    FirebaseAuth mAuth;
    FirebaseUser user;
    NetworkInfo netInfo;
    String userPhone, departmentName;
    Fragment fragment;
    Snackbar snackbar;
    DatabaseReference teacherReference;
    FragmentTransaction fragmentTransaction;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        actionBarText = findViewById(R.id.actionBarTextId);
        cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        netInfo = cm.getActiveNetworkInfo();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        userPhone = user.getDisplayName();

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

//        notificationBtn = findViewById(R.id.notificationId);
//        notificationBtn.setOnClickListener(this);
        bottomNavigationView = findViewById(R.id.bottomNavigationID);
        bottomNavigationView.setOnItemSelectedListener(this);
        addNoticeBtn = findViewById(R.id.addNoticeBtnId);
        addNoticeBtn.setVisibility(View.GONE);

        navigationView.setNavigationItemSelectedListener(MainActivity.this);

        actionBarText.setText("General Notice");
        fragment = new HomeFragment();
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragmentID, fragment, "MY_FRAGMENT");
        fragmentTransaction.commit();

        Intent intent = getIntent();
        try {
            departmentName = intent.getStringExtra("deptNameKey");
        } catch (Exception e){
            Log.i("Error ", e.getMessage());
        }

        try {
            switch (getIntent().getStringExtra("EXTRA")) {
                case "openDeptFragment":
                    bundle = new Bundle();
                    bundle.putString("dept_Key", departmentName);
                    fragment = new DepartmentNoticeFragment();
                    fragment.setArguments(bundle);
                    fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.fragmentID, fragment, "MY_FRAGMENT");
                    fragmentTransaction.commit();
                    break;
            }
        } catch (Exception e){
            Log.i("Error ", e.getMessage());
        }

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

        teacherReference = FirebaseDatabase.getInstance().getReference("Teacher Info");

        setBtnVisibility();
    }

    public void setBtnVisibility(){
        try {
            teacherReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    try {
                        for(DataSnapshot item: snapshot.getChildren()){
                            try {
                                if (userPhone.equals(item.getKey())) {
                                    addNoticeBtn.setVisibility(View.VISIBLE);
                                    checkBtnVisibility();
                                }

                            } catch (Exception e){
                                addNoticeBtn.setVisibility(View.GONE);
                                Log.i("Error ", e.getMessage());
                            }
                        }
                    } catch (Exception e){
                        addNoticeBtn.setVisibility(View.GONE);
                        Log.i("Error ", e.getMessage());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    addNoticeBtn.setVisibility(View.GONE);
                }
            });

        } catch (Exception e){
            addNoticeBtn.setVisibility(View.GONE);
            Log.i("Error ", e.getMessage());
        }
    }

    public void checkBtnVisibility(){
        // Open create notice dialog
        if(addNoticeBtn.getVisibility()==View.VISIBLE){
            addNoticeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CreateNoticeDialog createNoticeDialog = new CreateNoticeDialog();
                    createNoticeDialog.show(getSupportFragmentManager(), "Sample dialog");
                }
            });
        }
    }

    @Override
    public void onClick(View view) {
//        if(view.getId()==R.id.notificationId){
//            finish();
//            Intent intent = new Intent(MainActivity.this, NotificationActivity.class);
//            startActivity(intent);
//        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawerLayout.closeDrawer(GravityCompat.START,true);
        int id = item.getItemId();

        switch (id){
            case R.id.eteId:
                actionBarText.setText("ETE");
                bundle = new Bundle();
                bundle.putString("dept_Key", "ETE");
                fragment = new DepartmentNoticeFragment();
                fragment.setArguments(bundle);
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragmentID, fragment);
                fragmentTransaction.commit();
                break;

            case R.id.cseId:
                actionBarText.setText("CSE");
                bundle = new Bundle();
                bundle.putString("dept_Key", "CSE");
                fragment = new DepartmentNoticeFragment();
                fragment.setArguments(bundle);
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragmentID, fragment);
                fragmentTransaction.commit();
                break;

            case R.id.sweId:
                actionBarText.setText("SWE");
                bundle = new Bundle();
                bundle.putString("dept_Key", "SWE");
                fragment = new DepartmentNoticeFragment();
                fragment.setArguments(bundle);
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragmentID, fragment);
                fragmentTransaction.commit();
                break;

            case R.id.eeeId:
                actionBarText.setText("EEE");
                bundle = new Bundle();
                bundle.putString("dept_Key", "EEE");
                fragment = new DepartmentNoticeFragment();
                fragment.setArguments(bundle);
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragmentID, fragment);
                fragmentTransaction.commit();
                break;

            case R.id.bbaId:
                actionBarText.setText("BBA");
                bundle = new Bundle();
                bundle.putString("dept_Key", "BBA");
                fragment = new DepartmentNoticeFragment();
                fragment.setArguments(bundle);
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragmentID, fragment);
                fragmentTransaction.commit();
                break;

            case R.id.englishId:
                actionBarText.setText("English");
                bundle = new Bundle();
                bundle.putString("dept_Key", "English");
                fragment = new DepartmentNoticeFragment();
                fragment.setArguments(bundle);
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragmentID, fragment);
                fragmentTransaction.commit();
                break;

            case R.id.nfeId:
                actionBarText.setText("NFE");
                bundle = new Bundle();
                bundle.putString("dept_Key", "NFE");
                fragment = new DepartmentNoticeFragment();
                fragment.setArguments(bundle);
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragmentID, fragment);
                fragmentTransaction.commit();
                break;

            case R.id.physicsId:
                actionBarText.setText("Physics");
                bundle = new Bundle();
                bundle.putString("dept_Key", "Physics");
                fragment = new DepartmentNoticeFragment();
                fragment.setArguments(bundle);
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragmentID, fragment);
                fragmentTransaction.commit();
                break;

            case R.id.mathId:
                actionBarText.setText("Mathematics");
                bundle = new Bundle();
                bundle.putString("dept_Key", "Mathematics");
                fragment = new DepartmentNoticeFragment();
                fragment.setArguments(bundle);
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragmentID, fragment);
                fragmentTransaction.commit();
                break;

            case R.id.homeID:
                if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                    actionBarText.setText("General Notice");
                    setBtnVisibility();
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
                    setBtnVisibility();
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
                    addNoticeBtn.setVisibility(View.GONE);
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

        if(DepartmentNoticeFragment.backBtnListener!=null){
            DepartmentNoticeFragment.backBtnListener.onBackPressed();
        }
    }
}
