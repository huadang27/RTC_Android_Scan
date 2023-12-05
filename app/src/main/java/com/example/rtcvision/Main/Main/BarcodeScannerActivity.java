package com.example.rtcvision.Main.Main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.datalogic.decode.BarcodeManager;
import com.datalogic.decode.DecodeException;
import com.datalogic.decode.DecodeResult;
import com.datalogic.decode.ReadListener;
import com.example.rtcvision.Main.Main.Adapter.ScannedResultsAdapter;
import com.example.rtcvision.R;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class BarcodeScannerActivity extends AppCompatActivity {
    private String ipAddress;
    private String port;
    private Socket clientSocket;
    private DataOutputStream dataOutputStream;

    private BarcodeManager barcodeManager;
    private TextView textViewBarcodeData;
    private Button buttonScanPhysical;
    private Button buttonClearList;
    private ListView listViewScannedResults;
    private boolean isScanning = false;
    private long lastClickTime = 0;
    private static final long CLICK_TIME_INTERVAL = 1000;
    private Handler clickHandler = new Handler();
    private List<String> scannedResults;
    private ScannedResultsAdapter adapter;
    private ExecutorService executorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_scanner);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        executorService = Executors.newSingleThreadExecutor();

        barcodeManager = new BarcodeManager();
        textViewBarcodeData = findViewById(R.id.textViewBarcodeData);
        buttonScanPhysical = findViewById(R.id.buttonScanPhysical);
        buttonClearList = findViewById(R.id.buttonClearList);
        listViewScannedResults = findViewById(R.id.listViewScannedResults);

        scannedResults = new ArrayList<>();
        adapter = new ScannedResultsAdapter(this, scannedResults);
        listViewScannedResults.setAdapter(adapter);

        buttonScanPhysical.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        toggleBarcodeScanning(true);
                        break;
                    case MotionEvent.ACTION_UP:
                        toggleBarcodeScanning(false);
                        break;
                }
                return true;
            }
        });

        buttonClearList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearScannedResults();
            }
        });

        // Đi đến trang NetworkConfig
        Button buttonOpenNetworkConfig = findViewById(R.id.buttonOpenNetworkConfig);
        buttonOpenNetworkConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openNetworkConfigActivity();
            }
        });
//ĐỌC code, gửi mã lên server
        barcodeManager.addReadListener(new ReadListener() {
            @Override
            public void onRead(DecodeResult decodeResult) {
                String barcodeData = decodeResult.getText();
                scannedResults.add(0, barcodeData);
                showBarcodeData(barcodeData);
                if (clientSocket != null) {
                    //gửi đến server
                    new SendDataAsyn(clientSocket).execute(barcodeData);
                } else {

                    Toast.makeText(BarcodeScannerActivity.this, " Kết nối chưa được cấu hình", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        disableBarcodeScanning();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BUTTON_L1 || keyCode == KeyEvent.KEYCODE_BUTTON_R1) {
            toggleBarcodeScanning(true);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void enableBarcodeScanning() {
        if (!isScanning) {
            try {
                barcodeManager.startDecode();
                isScanning = true;
            } catch (DecodeException e) {
                e.printStackTrace();
            }
        }
    }

    private void disableBarcodeScanning() {
        if (isScanning) {
            try {
                barcodeManager.stopDecode();
                isScanning = false;
            } catch (DecodeException e) {
                e.printStackTrace();
            }
        }
    }

    private void toggleBarcodeScanning(boolean startScanning) {
        if (startScanning) {
            enableBarcodeScanning();
        } else {
            if (isScanning) {
                disableBarcodeScanning();
            }
        }
    }

    private void showBarcodeData(String barcodeData) {
        textViewBarcodeData.setText("Mã Vạch: " + barcodeData);
        adapter.notifyDataSetChanged();
    }




    private void clearScannedResults() {
        scannedResults.clear();
        adapter.notifyDataSetChanged();
    }

    private void openNetworkConfigActivity() {
        Intent intent = new Intent(this, NetworkConfig.class);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            ipAddress = data.getStringExtra("IP_ADDRESS");
            port = data.getStringExtra("PORT");
            setupConnection();
        }
    }


    private void setupConnection() {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    if (ipAddress != null && port != null) {
                        clientSocket = new Socket(ipAddress, Integer.parseInt(port));
                      //  dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(BarcodeScannerActivity.this, "Lỗi khi kết nối tới server", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdownNow();
        closeConnection();
    }

    private void closeConnection() {
        try {
            if (clientSocket != null && !clientSocket.isClosed()) {
                if (dataOutputStream != null) {
                    dataOutputStream.close();
                }
                clientSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}