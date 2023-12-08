package com.example.rtcvision.Main.Main.api;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.datalogic.decode.BarcodeManager;
import com.datalogic.decode.DecodeException;
import com.datalogic.decode.DecodeResult;
import com.datalogic.decode.ReadListener;
import com.example.rtcvision.Main.Main.adapter.SerialCodeAdapter;
import com.example.rtcvision.Main.Main.modal.InventoryWarehouseSerial;
import com.example.rtcvision.Main.Main.set_api.GetAPI_Service;
import com.example.rtcvision.Main.Main.set_api.RetrofitClient;
import com.example.rtcvision.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PushDataAPI extends AppCompatActivity {

    private BarcodeManager barcodeManager;
    private ListView listViewScannedResults;
    private List<String> scannedResults;
    private SerialCodeAdapter adapter;
    private Button buttonScanBarcode,send;
    private boolean isScanning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push_data_api);

        // Ánh xạ các phần tử giao diện
        barcodeManager = new BarcodeManager();
        listViewScannedResults = findViewById(R.id.listViewScannedResults);
        buttonScanBarcode = findViewById(R.id.buttonScanBarcode);
        send = findViewById(R.id.send);
        // Khởi tạo dữ liệu và adapter cho ListView
        scannedResults = new ArrayList<>();
        adapter = new SerialCodeAdapter(this, android.R.layout.simple_list_item_1, scannedResults);
        listViewScannedResults.setAdapter(adapter);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!scannedResults.isEmpty()) {
                    sendDataToAPI(scannedResults);
                } else {
                    Toast.makeText(PushDataAPI.this, "No barcode scanned", Toast.LENGTH_SHORT).show();
                }
                Log.d("PushDataAPI", "Button Send Clicked");
            }
        });

        buttonScanBarcode.setOnTouchListener(new View.OnTouchListener() {
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

        // Thêm sự kiện quét mã vạch
        barcodeManager.addReadListener(new ReadListener() {
            @Override
            public void onRead(DecodeResult decodeResult) {
                String barcodeData = decodeResult.getText();
                scannedResults.add(0, barcodeData);
                adapter.notifyDataSetChanged();
                // Dừng quét mã vạch sau khi quét thành công
                stopBarcodeScanning();
                // sendDataToAPI(barcodeData);
                sendDataToAPI(scannedResults);
            }
        });
    }

    // Hàm xử lý gửi dữ liệu API
    private void sendDataToAPI(List<String> serialCodes) {
        InventoryWarehouseSerial inventoryWarehouseSerial = new InventoryWarehouseSerial();
        inventoryWarehouseSerial.setSerialCode(serialCodes);
        inventoryWarehouseSerial.setCode("PN.230413.001");
        inventoryWarehouseSerial.setMaterialCode("AWW014AD1700");
        inventoryWarehouseSerial.setAreaCode("110");
        inventoryWarehouseSerial.setQuantity(1);
        inventoryWarehouseSerial.setLoginName("sample string 5");

        // Thực hiện cuộc gọi API bằng AsyncTask
        new SendApiTask().execute(inventoryWarehouseSerial);
    }

    private class SendApiTask extends AsyncTask<InventoryWarehouseSerial, Void, Boolean> {
        @Override
        protected Boolean doInBackground(InventoryWarehouseSerial... inventoryWarehouseSerials) {
            try {
                // Tạo đối tượng Retrofit
                GetAPI_Service apiService = RetrofitClient.RetrofitInstance().create(GetAPI_Service.class);

                // Thực hiện cuộc gọi API
                Call<Void> call = apiService.updateInventoryWarehouse(inventoryWarehouseSerials[0]);
                Response<Void> response = call.execute();

                // Kiểm tra xem cuộc gọi có thành công không
                return response.isSuccessful();
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("PushDataAPI", "Error sending API request", e);
                return false;
            }
        }
        @Override
        protected void onPostExecute(Boolean isSuccess) {
            // Xử lý kết quả sau khi cuộc gọi hoàn thành
            if (isSuccess) {
                // Xử lý khi cuộc gọi thành công
                Toast.makeText(PushDataAPI.this, "Đã gửi dữ liệu thành công", Toast.LENGTH_SHORT).show();
            } else {
                // Xử lý khi cuộc gọi không thành công
                Toast.makeText(PushDataAPI.this, "Gửi dữ liệu thất bại", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void startBarcodeScanning() {
        if (!isScanning) {
            try {
                barcodeManager.startDecode();
                isScanning = true;
            } catch (DecodeException e) {
                e.printStackTrace();
            }
        }
    }

    private void stopBarcodeScanning() {
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
            startBarcodeScanning();
        } else {
            if (isScanning) {
                stopBarcodeScanning();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopBarcodeScanning();
    }
}