package com.example.noticeapp.NoticeAndNotification;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.noticeapp.AppAction.MainActivity;
import com.example.noticeapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ParticularNoticeActivity extends AppCompatActivity {

    ImageView backFromNotice, deleteNotice;
    String noticeTitle, noticeDate, noticeDay, noticeTime, noticeDetails, departmentName;
    TextView noticeTitleText, noticeDateText, noticeDetailsText;
    DatabaseReference generalDatabaseReference, departmentDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_particular_notice);

        Intent intent = getIntent();
        noticeTitle = intent.getStringExtra("noticeTitleKey");
        noticeDate = intent.getStringExtra("noticeDateKey");
        noticeDay = intent.getStringExtra("noticeDayKey");
        noticeTime = intent.getStringExtra("noticeTimeKey");
        noticeDetails = intent.getStringExtra("noticeDetailsKey");
        departmentName = intent.getStringExtra("dept_Key");

        noticeTitleText = findViewById(R.id.noticeTitleId);
        noticeDateText = findViewById(R.id.noticeDateId);
        noticeDetailsText = findViewById(R.id.noticeDetailsId);

        noticeTitleText.setText(noticeTitle+"!");
        noticeDateText.setText(" Published on: " + noticeDate + ", " + noticeDay + "\n At: " + noticeTime);
        noticeDetailsText.setText(noticeDetails);

        generalDatabaseReference = FirebaseDatabase.getInstance().getReference("General Notice");
        departmentDatabaseReference = FirebaseDatabase.getInstance().getReference("Department Notice");

        backFromNotice = findViewById(R.id.backFromNoticeId);
        backFromNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(departmentName.equals("General_Notice")) {
                    finish();
                    Intent intent = new Intent(ParticularNoticeActivity.this, MainActivity.class);
                    intent.putExtra("EXTRA", "openHomeFragment");
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

                } else if(!departmentName.equals("General_Notice")){
                    finish();
                    Intent intent = new Intent(ParticularNoticeActivity.this, MainActivity.class);
                    intent.putExtra("EXTRA", "openDeptFragment");
                    intent.putExtra("deptNameKey", departmentName);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                }
            }
        });

        deleteNotice = findViewById(R.id.deleteNoticeId);
        deleteNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(departmentName.equals("General_Notice")) {
                    generalDatabaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            for (DataSnapshot item : dataSnapshot.getChildren()) {
                                String temp = item.getKey();

                                if(noticeTitle.equals(temp)) {
                                    AlertDialog.Builder alertDialogBuilder;
                                    alertDialogBuilder = new AlertDialog.Builder(ParticularNoticeActivity.this);
                                    alertDialogBuilder.setMessage("Do you want to delete this post ?");
                                    alertDialogBuilder.setCancelable(false);

                                    alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            try{
                                                generalDatabaseReference.child(temp).removeValue();

                                                Toast.makeText(getApplicationContext(), "Post Deleted", Toast.LENGTH_SHORT).show();

                                                finish();
                                                Intent intent = new Intent(ParticularNoticeActivity.this, MainActivity.class);
                                                intent.putExtra("EXTRA", "openHomeFragment");
                                                startActivity(intent);
                                                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

                                            } catch (Exception e){
                                                Log.i("Message ", "Deleted");
                                            }
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
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {}
                    });

                } else if(!departmentName.equals("General_Notice")){
                    departmentDatabaseReference.child(departmentName).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            for (DataSnapshot item : dataSnapshot.getChildren()) {
                                String temp = item.getKey();

                                if(noticeTitle.equals(temp)) {
                                    AlertDialog.Builder alertDialogBuilder;
                                    alertDialogBuilder = new AlertDialog.Builder(ParticularNoticeActivity.this);
                                    alertDialogBuilder.setMessage("Do you want to delete this post ?");
                                    alertDialogBuilder.setCancelable(false);

                                    alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            try{
                                                departmentDatabaseReference.child(departmentName).child(temp).removeValue();

                                                Toast.makeText(getApplicationContext(), temp, Toast.LENGTH_SHORT).show();

                                                finish();
                                                Intent intent = new Intent(ParticularNoticeActivity.this, MainActivity.class);
                                                intent.putExtra("EXTRA", "openDeptFragment");
                                                intent.putExtra("deptNameKey", departmentName);
                                                startActivity(intent);
                                                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

                                            } catch (Exception e){
                                                Log.i("Message ", "Deleted");
                                            }
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
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {}
                    });
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if(departmentName.equals("General_Notice")) {
            finish();
            Intent intent = new Intent(ParticularNoticeActivity.this, MainActivity.class);
            intent.putExtra("EXTRA", "openHomeFragment");
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

        } else {
            finish();
            Intent intent = new Intent(ParticularNoticeActivity.this, MainActivity.class);
            intent.putExtra("EXTRA", "openDeptFragment");
            intent.putExtra("deptNameKey", departmentName);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }

        super.onBackPressed();
    }
}
