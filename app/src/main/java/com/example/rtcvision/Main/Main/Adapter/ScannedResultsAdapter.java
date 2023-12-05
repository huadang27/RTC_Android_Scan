package com.example.rtcvision.Main.Main.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ScannedResultsAdapter extends ArrayAdapter<String> {
    private Context context;
    private List<String> scannedResults;

    public ScannedResultsAdapter(Context context, List<String> scannedResults) {
        super(context, 0, scannedResults);
        this.context = context;
        this.scannedResults = scannedResults;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Kiểm tra xem convertView đã được tái sử dụng chưa, nếu không, inflate lại
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, parent, false);
        }

        // Lấy dữ liệu ở vị trí hiện tại
        String result = scannedResults.get(position);

        // Hiển thị dữ liệu trong TextView
        TextView textView = convertView.findViewById(android.R.id.text1);
        textView.setText(result);

        return convertView;
    }
    public List<String> getScannedDataList() {
        List<String> scannedDataList = new ArrayList<>();
        for (int i = 0; i < getCount(); i++) {
            scannedDataList.add(getItem(i));
        }
        return scannedDataList;
    }
}
