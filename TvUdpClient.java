import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.VideoView;
import androidx.appcompat.app.AppCompatActivity;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public class TvUdpClient extends AppCompatActivity {
    private static final int BASE_PORT = 5000;
    private static final int CHANNEL_COUNT = 10;
    private ListView channelListView;
    private VideoView videoView;
    private List<String> channelList = new ArrayList<>();
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);

        channelListView = findViewById(R.id.channelListView);
        videoView = findViewById(R.id.videoView);
        context = this;

        setupChannelList();
        startHotspotCheckService();
    }

    private void setupChannelList() {
        for (int i = 0; i < CHANNEL_COUNT; i++) {
            int udpPort = BASE_PORT + i;
            channelList.add("Channel " + (i + 1) + " - Port: " + udpPort);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, channelList);
        channelListView.setAdapter(adapter);

        channelListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int selectedPort = BASE_PORT + position;
                startStreaming(selectedPort);
            }
        });
    }

    private void startStreaming(int port) {
        // Implementasi streaming video dari UDP ke VideoView
    }

    private void startHotspotCheckService() {
        new Thread(() -> {
            while (true) {
                if (isHotspotActive()) {
                    broadcastToConnectedDevices();
                }
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private boolean isHotspotActive() {
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        return wifiManager != null && wifiManager.isWifiEnabled();
    }

    private void broadcastToConnectedDevices() {
        try {
            DatagramSocket socket = new DatagramSocket();
            InetAddress address = InetAddress.getByName("255.255.255.255");
            byte[] buffer = "Repeating broadcast".getBytes();
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, BASE_PORT);
            socket.send(packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
