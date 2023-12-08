package com.example.rtcvision.Main.Main.tcp_ip;

import android.os.AsyncTask;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class SendDataAsyn extends AsyncTask<String, Void, Void> {
    private Socket clientSocket;
    private DataOutputStream dataOutputStream;

    public SendDataAsyn(Socket socket) {
        this.clientSocket = socket;
        try {
            this.dataOutputStream = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected Void doInBackground(String... params) {
        if (clientSocket != null && clientSocket.isConnected()) {
            String data = params[0];
            try {
                if (dataOutputStream != null) {
                    dataOutputStream.writeUTF(data);
                    dataOutputStream.flush();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}