package com.example.rtcvision.Main.Main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.rtcvision.Main.Main.api.PushDataAPI;
import com.example.rtcvision.Main.Main.tcp_ip.BarcodeScannerActivity;
import com.example.rtcvision.R;

public class HomeScan extends AppCompatActivity {
Button bt_dn,bt_API;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_scan);
        bt_dn = findViewById(R.id.bt_dn);
        bt_API = findViewById(R.id.bt_API);
        bt_dn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openScanTCP_IP();
            }
        });
        bt_API.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openScanAPI();
            }
        });
    }
    private void openScanTCP_IP() {
        Intent intent = new Intent(this, BarcodeScannerActivity.class);
        startActivityForResult(intent, 1);
    }
    private void openScanAPI() {
        Intent intent = new Intent(this, PushDataAPI.class);
        startActivityForResult(intent, 1);
    }

}