package com.example.rtcvision.Main.Main;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.example.rtcvision.Main.Main.tcp_ip.BarcodeScannerActivity;
import com.example.rtcvision.R;

import java.util.Timer;
import java.util.TimerTask;

public class SilentLoginActivity extends AppCompatActivity {

    private static final int SPLASH_TIME_OUT = 1000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_silent_login);

        startApplication();
        createNotificationChannel();

    }

    private void startApplication() {
        Timer RunSplash = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Toast.makeText(SilentLoginActivity.this, "Chào mừng bạn trở lại !", Toast.LENGTH_SHORT).show();

                            startActivity(new Intent(SilentLoginActivity.this, HomeScan.class));
                            finish();
                    }
                });
            }
        };
        RunSplash.schedule(timerTask, SPLASH_TIME_OUT);
    }
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Your Channel Name";
            String description = "Your Channel Description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("channel_id", name, importance);
            channel.setDescription(description);

            // Đăng ký kênh thông báo với hệ thống
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }






}