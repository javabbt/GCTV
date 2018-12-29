package infos.generationchange.gctv;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.YouTubePlayerView;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.YouTubePlayerInitListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


public class SingleItem extends AppCompatActivity {

    private TextView text;
    private TextView title;
    private ImageView back;
    private ImageView image;
    private ImageView play;


    YouTubePlayerView youtubePlayerView;
    @NonNull
    @Override
    public LayoutInflater getLayoutInflater() {
        return super.getLayoutInflater();
    }

    private static final String TAG = "SingleItem";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        setContentView(R.layout.activity_single_item);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        title = toolbar.findViewById(R.id.title);
        title.setText(getIntent().getStringExtra("title"));
        back = findViewById(R.id.back);
        setSupportActionBar(toolbar);

        image = findViewById(R.id.image);

        play = findViewById(R.id.play);

        Glide.with(this).load(getIntent().getStringExtra("picture")).into(image);

        text = findViewById(R.id.content);

        back.setOnClickListener(v -> {
            finish();
        });

        if(getIntent().getStringExtra("link") != null)
            play.setVisibility(View.VISIBLE);

        text.setText(getIntent().getStringExtra("description"));

        youtubePlayerView = findViewById(R.id.youtube_player_view);

        play.setOnClickListener(v -> {

            image.setVisibility(View.GONE);
            play.setVisibility(View.GONE);
            youtubePlayerView.setVisibility(View.VISIBLE);

            getLifecycle().addObserver(youtubePlayerView);

            youtubePlayerView.initialize(new YouTubePlayerInitListener() {
                @Override
                public void onInitSuccess(@NonNull final YouTubePlayer initializedYouTubePlayer) {
                    initializedYouTubePlayer.addListener(new AbstractYouTubePlayerListener() {
                        @Override
                        public void onReady() {
                            String videoLink = getIntent().getStringExtra("link");
                            int  i = videoLink.indexOf("=");
                            String videoId = videoLink.substring(i+1);
                            initializedYouTubePlayer.loadVideo(videoId, 0);
                        }

                        @Override
                        public void onError(@NonNull PlayerConstants.PlayerError error) {
                            image.setVisibility(View.VISIBLE);
                            play.setVisibility(View.VISIBLE);
                            youtubePlayerView.setVisibility(View.GONE);
                        }

                        @Override
                        public void onStateChange(@NonNull PlayerConstants.PlayerState state) {
                            if(state == PlayerConstants.PlayerState.ENDED){
                                image.setVisibility(View.VISIBLE);
                                play.setVisibility(View.VISIBLE);
                                youtubePlayerView.setVisibility(View.GONE);
                            }
                        }
                    });
                }
            }, true);


        });
    }

    public static String extractYTId(String ytUrl) {
        String vId = null;
        Pattern pattern = Pattern.compile(
                "^https?://.*(?:youtu.be/|v/|u/\\w/|embed/|watch?v=)([^#&?]*).*$",
                Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(ytUrl);
        if (matcher.matches()){
            vId = matcher.group(1);
        }
        return vId;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        youtubePlayerView.release();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
