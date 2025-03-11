import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class TvUdpMiddleware extends Service {
    private static final int BASE_PORT = 5000;
    private static final int CHANNEL_COUNT = 10;
    private static final String BROADCAST_IP = "255.255.255.255";
    private boolean isRunning = true;

    @Override
    public void onCreate() {
        super.onCreate();
        new Thread(this::startRepeater).start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isRunning = false;
    }

    private void startRepeater() {
        while (isRunning) {
            if (isHotspotActive()) {
                relayBroadcast();
            }
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean isHotspotActive() {
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        return wifiManager != null && wifiManager.isWifiEnabled();
    }

    private void relayBroadcast() {
        try {
            DatagramSocket socket = new DatagramSocket();
            InetAddress address = InetAddress.getByName(BROADCAST_IP);
            for (int i = 0; i < CHANNEL_COUNT; i++) {
                int port = BASE_PORT + i;
                byte[] buffer = ("Relaying channel on port " + port).getBytes();
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, port);
                socket.send(packet);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
