package com.example.noticeapp.NoticeAndNotification;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.noticeapp.AppAction.MainActivity;
import com.example.noticeapp.R;

public class ParticularNoticeActivity extends AppCompatActivity {

    ImageView backFromNotice;
    String noticeTitle, noticeDate, noticeDay, noticeTime, noticeDetails, departmentName;
    TextView noticeTitleText, noticeDateText, noticeDetailsText;

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
