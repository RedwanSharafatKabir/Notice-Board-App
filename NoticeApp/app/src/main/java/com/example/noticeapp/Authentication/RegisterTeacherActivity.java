package com.example.noticeapp.Authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.noticeapp.AppAction.MainActivity;
import com.example.noticeapp.ModelClasses.StoreTeacherData;
import com.example.noticeapp.ModelClasses.StoreUserData;
import com.example.noticeapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class RegisterTeacherActivity extends AppCompatActivity implements View.OnClickListener{

    View parentLayout;
    Button signUp;
    ImageView backPage;
    ProgressDialog waitingDialog;
    ConnectivityManager cm;
    NetworkInfo netInfo;
    FirebaseAuth mAuth;
    TextView loginPage;
    DatabaseReference databaseReference, IdsReference;
    EditText signupTeacherEmailText, signupTeacherNameText, signupTeacherPasswordText, signupTeacherPhoneText, signupTeacherIdText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_teacher);

        cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        netInfo = cm.getActiveNetworkInfo();
        parentLayout = findViewById(android.R.id.content);
        waitingDialog = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Teacher Info");
        IdsReference = FirebaseDatabase.getInstance().getReference("Teacher Ids");

        signupTeacherEmailText = findViewById(R.id.enterTeacherEmailSignupId);
        signupTeacherNameText = findViewById(R.id.enterTeacherNameSignupId);
        signupTeacherPasswordText = findViewById(R.id.enterTeacherPasswordSignupId);
        signupTeacherPhoneText = findViewById(R.id.enterTeacherPhoneSignupId);
        signupTeacherIdText = findViewById(R.id.enterTeacherIdSignupId);

        signUp = findViewById(R.id.signUpTeacherId);
        signUp.setOnClickListener(this);
        backPage = findViewById(R.id.backFromTeacherId);
        backPage.setOnClickListener(this);
        loginPage = findViewById(R.id.loginPageFromTcId);
        loginPage.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.loginPageFromTcId){
            finish();
            Intent intent = new Intent(RegisterTeacherActivity.this, LoginActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }

        if(view.getId()==R.id.backFromTeacherId){
            finish();
            Intent intent = new Intent(RegisterTeacherActivity.this, LoginActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }

        if(view.getId()==R.id.signUpTeacherId){
            final String email = signupTeacherEmailText.getText().toString();
            final String phone = signupTeacherPhoneText.getText().toString();
            final String username = signupTeacherNameText.getText().toString();
            final String teacherId = signupTeacherIdText.getText().toString();
            final String password = signupTeacherPasswordText.getText().toString();

            waitingDialog.setMessage("Signing up...");
            waitingDialog.show();

            if (email.isEmpty()) {
                waitingDialog.dismiss();
                signupTeacherEmailText.setError("Please enter email address");
                return;
            }

            if (username.isEmpty()) {
                waitingDialog.dismiss();
                signupTeacherNameText.setError("Please enter username");
                return;
            }

            if (phone.isEmpty()) {
                waitingDialog.dismiss();
                signupTeacherPhoneText.setError("Please enter your contact number");
                return;
            }

            if(teacherId.isEmpty()){
                waitingDialog.dismiss();
                signupTeacherIdText.setError("Please enter teacher Id");
                return;
            }

            if (password.isEmpty()) {
                waitingDialog.dismiss();
                signupTeacherPasswordText.setError("Please enter password");
                return;
            }

            if (password.length() < 8) {
                waitingDialog.dismiss();
                signupTeacherPasswordText.setError("Password must be at least 8 characters");
                return;
            }

            if((phone.length() < 11) || phone.length() > 11) {
                waitingDialog.dismiss();
                signupTeacherPhoneText.setError("Invalid phone number");
            }

            else {
                if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                    if(!teacherId.isEmpty()){

                        IdsReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for(DataSnapshot dataSnapshot: snapshot.getChildren()){

                                    String temp = dataSnapshot.child("Id").getValue().toString();

                                    if(teacherId.equals(temp)){
                                        signupWithEmail(email, username, phone, teacherId, password);

                                        signupTeacherEmailText.setText("");
                                        signupTeacherNameText.setText("");
                                        signupTeacherPhoneText.setText("");
                                        signupTeacherIdText.setText("");
                                        signupTeacherPasswordText.setText("");
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                waitingDialog.dismiss();
                                signupTeacherIdText.setError("Invalid teacher Id");
                            }
                        });
                    }

                } else {
                    waitingDialog.dismiss();
                    Snackbar snackbar = Snackbar.make(parentLayout, "Turn on internet connection", Snackbar.LENGTH_LONG);
                    View sbView = snackbar.getView();
                    sbView.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.Red));
                    snackbar.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE);
                    snackbar.setDuration(10000).show();
                }
            }
        }
    }

    private void signupWithEmail(String email, String username, String phone, String teacherId, String password){
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    storeTcDataMethod(email, username, phone, teacherId);

                    waitingDialog.dismiss();

                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                rememberMethod("I_User");

                                finish();
                                Intent it = new Intent(RegisterTeacherActivity.this, MainActivity.class);
                                startActivity(it);
                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                            } else {
                                waitingDialog.dismiss();
                                Toast t = Toast.makeText(getApplicationContext(), "Authentication failed\nError : " +
                                        task.getException().getMessage(), Toast.LENGTH_LONG);
                                t.setGravity(Gravity.CENTER, 0, 0);
                                t.show();
                            }
                        }
                    });

                } else {
                    if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                        waitingDialog.dismiss();
                        Toast t = Toast.makeText(getApplicationContext(), "Authentication failed",
                                Toast.LENGTH_LONG);
                        t.setGravity(Gravity.CENTER, 0, 0);
                        t.show();
                    } else {
                        waitingDialog.dismiss();
                        Toast t = Toast.makeText(getApplicationContext(), "Authentication failed. Error : "
                                + "Connection lost.", Toast.LENGTH_LONG);
                        t.setGravity(Gravity.CENTER, 0, 0);
                        t.show();
                    }
                }
            }
        });
    }

    private void rememberMethod(String passedString){
        try {
            FileOutputStream fileOutputStream = openFileOutput("Users_Info.txt", Context.MODE_PRIVATE);
            fileOutputStream.write(passedString.getBytes());
            fileOutputStream.close();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void storeTcDataMethod(String teacherEmail, String teachername, String teacherPhone, String teacherId){
        String displayname = teacherPhone;
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            UserProfileChangeRequest profile;
            profile = new UserProfileChangeRequest.Builder().setDisplayName(displayname).build();
            user.updateProfile(profile).addOnCompleteListener(task -> {});
        }

        StoreTeacherData storeTeacherData = new StoreTeacherData(teachername, teacherPhone, teacherEmail, teacherId);
        databaseReference.child(teacherPhone).setValue(storeTeacherData);

        Toast.makeText(RegisterTeacherActivity.this, "Successfully Registered", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        finish();
        Intent intent = new Intent(RegisterTeacherActivity.this, LoginActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
