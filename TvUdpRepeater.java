import android.media.tv.TvInputManager;
import android.media.tv.TvInputInfo;
import android.media.tv.TvView;
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

public class TvUdpRepeater extends AppCompatActivity {
    private Context context;
    private TvInputManager tvInputManager;
    private static final int BASE_PORT = 5000; // Port UDP awal
    private static final String BROADCAST_IP = "255.255.255.255";
    private ListView channelListView;
    private VideoView videoView;
    private List<String> channelList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        channelListView = findViewById(R.id.channelListView);
        videoView = findViewById(R.id.videoView);

        this.context = this;
        this.tvInputManager = (TvInputManager) getSystemService(Context.TV_INPUT_SERVICE);
        startStreaming();
    }

    public void startStreaming() {
        List<TvInputInfo> tvInputs = tvInputManager.getTvInputList();
        int portOffset = 0;

        for (TvInputInfo input : tvInputs) {
            if (input.isPassthroughInput()) continue;

            final int udpPort = BASE_PORT + portOffset;
            portOffset++;

            channelList.add("Channel " + portOffset + " - Port: " + udpPort);
            new Thread(() -> streamToUdp(input, udpPort)).start();
        }

        runOnUiThread(() -> {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, channelList);
            channelListView.setAdapter(adapter);
        });
    }

    private void streamToUdp(TvInputInfo input, int udpPort) {
        try {
            DatagramSocket socket = new DatagramSocket();
            InetAddress address = InetAddress.getByName(BROADCAST_IP);

            // Dummy data untuk simulasi (gantilah dengan real stream)
            byte[] buffer = ("Streaming channel from " + input.getId()).getBytes();
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, udpPort);

            while (true) {
                socket.send(packet);
                Thread.sleep(1000 / 30); // 30 FPS simulasi
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void enableHotspotForwarding() {
        try {
            WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
            if (wifiManager != null && wifiManager.isWifiEnabled()) {
                Runtime.getRuntime().exec("iptables -t nat -A POSTROUTING -o wlan0 -j MASQUERADE");
                Runtime.getRuntime().exec("iptables -A FORWARD -i wlan0 -o wlan1 -m state --state RELATED,ESTABLISHED -j ACCEPT");
                Runtime.getRuntime().exec("iptables -A FORWARD -i wlan1 -o wlan0 -j ACCEPT");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
