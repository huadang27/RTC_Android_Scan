package com.example.rtcvision.Main.Main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toolbar;

import com.example.rtcvision.R;

public class NetworkConfig extends AppCompatActivity {
    private EditText editTextIpAddress;
    private EditText editTextPort;
    private Button buttonSave;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_config);


        editTextIpAddress = findViewById(R.id.editTextIpAddress);
        editTextPort = findViewById(R.id.editTextPort);
        buttonSave = findViewById(R.id.buttonSave);

        sharedPreferences = getSharedPreferences(getPackageName() + ".NetworkConfig", MODE_PRIVATE);

        // Lấy giá trị từ SharedPreferences

        String ipAddress = sharedPreferences.getString("IP_ADDRESS", "");
        String port = sharedPreferences.getString("PORT", "");

        // Kiểm tra và đặt giá trị cho EditText
        if (ipAddress != null) {
            editTextIpAddress.setText(ipAddress);
        } else {
            editTextIpAddress.setText("");
        }

        if (port != null) {
            editTextPort.setText(port);
        } else {
            editTextPort.setText("");
        }
        setSupportActionBar(findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ipAddress = editTextIpAddress.getText().toString();
                String port = editTextPort.getText().toString();

                // Lưu giá trị vào SharedPreferences
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("IP_ADDRESS", ipAddress);
                editor.putString("PORT", port);
                editor.apply();

                // Gửi dữ liệu trở lại BarcodeScannerActivity
                Intent resultIntent = new Intent();
                resultIntent.putExtra("IP_ADDRESS", ipAddress);
                resultIntent.putExtra("PORT", port);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}