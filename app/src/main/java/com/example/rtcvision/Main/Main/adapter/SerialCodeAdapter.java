package com.example.rtcvision.Main.Main.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class SerialCodeAdapter extends ArrayAdapter<String> {
    public SerialCodeAdapter(@NonNull Context context, int resource, @NonNull List<String> serialCodes) {
        super(context, resource, serialCodes);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        }

        // Hiển thị giá trị serialCode trong TextView
        TextView textViewSerialCode = convertView.findViewById(android.R.id.text1);
        String serialCode = getItem(position);
        textViewSerialCode.setText(serialCode);

        return convertView;
    }
}
