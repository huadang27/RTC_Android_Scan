package com.example.rtcvision.Main.Main;

import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.datalogic.decode.BarcodeManager;
import com.datalogic.decode.DecodeException;
import com.datalogic.decode.DecodeResult;
import com.datalogic.decode.ReadListener;
import com.example.rtcvision.R;

public class BarcodeScannerActivity extends AppCompatActivity {
    private BarcodeManager barcodeManager;
    private TextView textViewBarcodeData;
    private Button buttonScanPhysical;
    private boolean isScanning = false;
    private long lastClickTime = 0;
    private static final long CLICK_TIME_INTERVAL = 1000; // Thời gian giữa các lần nhấn ít nhất 1 giây
    private Handler clickHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_scanner);

        barcodeManager = new BarcodeManager();
        textViewBarcodeData = findViewById(R.id.textViewBarcodeData);
        buttonScanPhysical = findViewById(R.id.buttonScanPhysical);

        barcodeManager.addReadListener(new ReadListener() {
            @Override
            public void onRead(DecodeResult decodeResult) {
                String barcodeData = decodeResult.getText();
                showBarcodeData(barcodeData);
                disableBarcodeScanning(); // Tắt quét sau khi quét thành công
                enableButtonAfterDelay(); // Kích hoạt lại nút sau một khoảng thời gian
            }
        });

        buttonScanPhysical.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                // Kiểm tra thời gian giữa các lần nhấn
                long currentTime = System.currentTimeMillis();
                if (currentTime - lastClickTime > CLICK_TIME_INTERVAL) {
                    // Nếu đã hết thời gian giữa các lần nhấn, xử lý sự kiện
                    toggleBarcodeScanning();
                    lastClickTime = currentTime;
                    disableButtonForDelay(); // Tạm thời vô hiệu hóa nút trong một khoảng thời gian
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Không cần bật quét ở đây, chờ người dùng giữ nút
    }

    @Override
    protected void onPause() {
        super.onPause();
        disableBarcodeScanning();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BUTTON_L1 || keyCode == KeyEvent.KEYCODE_BUTTON_R1) {
            // Sự kiện khi nhấn nút trên thiết bị, chuyển đổi trạng thái quét
            toggleBarcodeScanning();
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

    private void toggleBarcodeScanning() {
        if (!isScanning) {
            enableBarcodeScanning();
        }
    }

    private void showBarcodeData(String barcodeData) {
        textViewBarcodeData.setText("Mã Vạch: " + barcodeData);
        // Tắt camera sau khi hiển thị dữ liệu
        // Cần thực hiện phương thức tắt camera phù hợp với SDK hoặc thư viện bạn đang sử dụng
        // Dưới đây là một ví dụ giả định:
        // turnOffCamera();
    }

    private void disableButtonForDelay() {
        buttonScanPhysical.setEnabled(false);
        clickHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Kích hoạt lại nút sau khoảng thời gian
                buttonScanPhysical.setEnabled(true);
            }
        }, CLICK_TIME_INTERVAL);
    }

    private void enableButtonAfterDelay() {
        clickHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Kích hoạt lại nút sau khoảng thời gian
                buttonScanPhysical.setEnabled(true);
            }
        }, CLICK_TIME_INTERVAL);
    }
}