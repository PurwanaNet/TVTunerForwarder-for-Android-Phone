package com.example.wifidirectapp;

import android.net.wifi.p2p.WifiP2pInfo;
import android.util.Log;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class DataTransferHelper {

    private static final int PORT = 8988;

    public static void sendData(WifiP2pInfo info, String message) {
        if (info.groupOwnerAddress == null) {
            Log.e("WiFiDirect", "Group owner address is null");
            return;
        }

        new Thread(() -> {
            try {
                Socket socket = new Socket();
                socket.bind(null);
                socket.connect(new InetSocketAddress(info.groupOwnerAddress, PORT), 5000);

                OutputStream outputStream = socket.getOutputStream();
                outputStream.write(message.getBytes());
                outputStream.flush();
                outputStream.close();
                socket.close();

                Log.d("WiFiDirect", "Data sent: " + message);
            } catch (IOException e) {
                Log.e("WiFiDirect", "Error sending data", e);
            }
        }).start();
    }
}
