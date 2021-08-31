package com.example.noticeapp.Notification;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.noticeapp.AppAction.MainActivity;
import com.example.noticeapp.R;

public class NotificationActivity extends AppCompatActivity {

    ImageView backFromNotification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        backFromNotification = findViewById(R.id.backFromNotificationId);
        backFromNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent intent = new Intent(NotificationActivity.this, MainActivity.class);
                intent.putExtra("EXTRA", "openHomeFragment");
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
        Intent intent = new Intent(NotificationActivity.this, MainActivity.class);
        intent.putExtra("EXTRA", "openHomeFragment");
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

        super.onBackPressed();
    }
}
