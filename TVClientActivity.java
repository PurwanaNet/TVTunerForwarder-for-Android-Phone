import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.ui.PlayerView;
import java.util.ArrayList;

public class TVClientActivity extends AppCompatActivity {
    private ExoPlayer player;
    private PlayerView playerView;
    private ListView channelListView;
    private ArrayList<String> channels;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tv_client);
        
        playerView = findViewById(R.id.playerView);
        channelListView = findViewById(R.id.channelListView);
        
        player = new ExoPlayer.Builder(this).build();
        playerView.setPlayer(player);
        
        channels = new ArrayList<>();
        channels.add("udp://239.255.255.250:1234");
        channels.add("udp://239.255.255.251:1234");
        
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, channels);
        channelListView.setAdapter(adapter);
        
        channelListView.setOnItemClickListener((parent, view, position, id) -> playChannel(channels.get(position)));
    }
    
    private void playChannel(String url) {
        player.setMediaItem(MediaItem.fromUri(Uri.parse(url)));
        player.prepare();
        player.play();
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        player.release();
    }
}
