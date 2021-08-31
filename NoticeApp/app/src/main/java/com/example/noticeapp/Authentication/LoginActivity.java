package com.example.noticeapp.Authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.noticeapp.AppAction.MainActivity;
import com.example.noticeapp.HomePage.HomeFragment;
import com.example.noticeapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    CheckBox rememberPass;
    View parentLayout;
    Button enterBtn;
    public TextView signUpPage, textViewHome, signUpTeacherPage;
    ProgressDialog waitingDialog;
    ConnectivityManager cm;
    NetworkInfo netInfo;
    FirebaseAuth mAuth;
    FirebaseUser user;
    EditText emailText, passwordText;
    String passedString="I_User", getPassedString="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        netInfo = cm.getActiveNetworkInfo();
        parentLayout = findViewById(android.R.id.content);
        waitingDialog = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();

        textViewHome = findViewById(R.id.textViewHomeId);
        rememberPass = findViewById(R.id.rememberPasswordCheckId);
        emailText = findViewById(R.id.enterEmailId);
        passwordText = findViewById(R.id.enterPasswordId);

        enterBtn = findViewById(R.id.loginId);
        enterBtn.setOnClickListener(this);
        signUpPage = findViewById(R.id.signUpPageId);
        signUpPage.setOnClickListener(this);
        signUpTeacherPage = findViewById(R.id.signUpTeacherPageId);
        signUpTeacherPage.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        gotUserMethod();

        if (user != null && !getPassedString.isEmpty()) {
            finish();
            Intent it = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(it);
        }

        super.onStart();
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.loginId){
            final String email = emailText.getText().toString();
            final String password = passwordText.getText().toString();

            waitingDialog.setMessage("Signing up...");
            waitingDialog.show();

            if (email.isEmpty()) {
                waitingDialog.dismiss();
                emailText.setError("Please enter email address");
                return;
            }

            if (password.isEmpty()) {
                waitingDialog.dismiss();
                passwordText.setError("Please enter password");
                return;
            }

            else {
                if (rememberPass.isChecked()) {
                    if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                        rememberMethod(passedString);

                        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    waitingDialog.dismiss();

                                    finish();
                                    Intent it = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(it);
                                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                                    emailText.setText("");
                                    passwordText.setText("");

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
                        Toast.makeText(getApplicationContext(), "wifi or mobile data connection lost", Toast.LENGTH_SHORT).show();
                        waitingDialog.dismiss();
                    }
                }

                if (!rememberPass.isChecked()) {
                    if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                        passedString = "";
                        setNullMethod(passedString);

                        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    waitingDialog.dismiss();

                                    finish();
                                    Intent it = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(it);
                                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                                    emailText.setText("");
                                    passwordText.setText("");

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
                        Toast.makeText(getApplicationContext(), "wifi or mobile data connection lost", Toast.LENGTH_SHORT).show();
                        waitingDialog.dismiss();
                    }
                }
            }
        }

        if(v.getId()==R.id.signUpTeacherPageId){
            finish();
            Intent intent = new Intent(LoginActivity.this, RegisterTeacherActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }

        if(v.getId()==R.id.signUpPageId){
            finish();
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
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

    private void setNullMethod(String passedString){
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

    private void gotUserMethod(){
        try {
            String recievedMessageTc;
            FileInputStream fileInputStreamTc = openFileInput("Users_Info.txt");
            InputStreamReader inputStreamReaderTc = new InputStreamReader(fileInputStreamTc);
            BufferedReader bufferedReaderTc = new BufferedReader(inputStreamReaderTc);
            StringBuffer stringBufferTc = new StringBuffer();

            while((recievedMessageTc=bufferedReaderTc.readLine())!=null){
                stringBufferTc.append(recievedMessageTc);
            }

            getPassedString = stringBufferTc.toString();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDialogBuilder;
        alertDialogBuilder = new AlertDialog.Builder(LoginActivity.this);
        alertDialogBuilder.setTitle("EXIT !");
        alertDialogBuilder.setMessage("Are you sure you want to close this app ?");
        alertDialogBuilder.setIcon(R.drawable.exit);
        alertDialogBuilder.setCancelable(false);

        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
                System.exit(0);
            }
        });

        alertDialogBuilder.setNeutralButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
