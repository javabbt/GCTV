package infos.generationchange.gctv.utils;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import androidx.appcompat.app.AppCompatActivity;
import infos.generationchange.gctv.MainActivity;
import infos.generationchange.gctv.R;

public class FullScreenActivity extends AppCompatActivity {

    private SimpleExoPlayer player;

    private boolean playWhenReady = true;
    private int currentWindow = 0;
    private long playbackPosition = 0;
    private PlayerView playerView;
    private ProgressBar loading;

    private ImageView shrink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        playbackPosition = getIntent().getLongExtra("time" , 0);
    }

    private void releasePlayer() {
        if (player != null) {
            playbackPosition = player.getCurrentPosition();
            currentWindow = player.getCurrentWindowIndex();
            playWhenReady = player.getPlayWhenReady();
            player.release();
            player = null;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }
    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    @Override
    public void onBackPressed() {
        playbackPosition = player.getCurrentPosition();
        Intent i = new Intent(this , MainActivity.class);
        i.putExtra("time" , playbackPosition);
        startActivity(i);
        finish();
    }

    @Override
    public void onStart() {
        super.onStart();
        playerView =findViewById(R.id.videoplayer);
        shrink = playerView.findViewById(R.id.shrink);

        shrink.setOnClickListener(v -> {
            playbackPosition = player.getCurrentPosition();
            Intent i = new Intent(this , MainActivity.class);
            i.putExtra("time" , playbackPosition);
            startActivity(i);
            finish();
        });

        loading = findViewById(R.id.loading);
        TrackSelection.Factory adaptiveTrackSelection = new AdaptiveTrackSelection.Factory(new DefaultBandwidthMeter());
        player = ExoPlayerFactory.newSimpleInstance(
                new DefaultRenderersFactory(this),
                new DefaultTrackSelector(adaptiveTrackSelection),
                new DefaultLoadControl());
        playerView.setPlayer(player);
        DefaultBandwidthMeter defaultBandwidthMeter = new DefaultBandwidthMeter();
        com.google.android.exoplayer2.upstream.DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(this,
                Util.getUserAgent(this, "GCTV"), defaultBandwidthMeter);
        String hls_url = "https://5c213d823e63f.streamlock.net:1937/live/ngrp:GCTV.stream_all/manifest.mpd";
        Uri uri = Uri.parse(hls_url);
        Handler mainHandler = new Handler();
        DashMediaSource dashMediaSource = new DashMediaSource(uri, dataSourceFactory,
                new DefaultDashChunkSource.Factory(dataSourceFactory), mainHandler, null);
        player.prepare(dashMediaSource);
        player.addListener(new Player.EventListener() {
            @Override
            public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {
            }
            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
            }
            @Override
            public void onLoadingChanged(boolean isLoading) {
            }
            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                switch (playbackState) {
                    case ExoPlayer.STATE_READY:
                        loading.setVisibility(View.GONE);
                        break;
                    case ExoPlayer.STATE_BUFFERING:
                        loading.setVisibility(View.VISIBLE);
                        break;
                }
            }
            @Override
            public void onRepeatModeChanged(int repeatMode) {
            }
            @Override
            public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {
            }
            @Override
            public void onPlayerError(ExoPlaybackException error) {
            }
            @Override
            public void onPositionDiscontinuity(int reason) {
            }
            @Override
            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
            }
            @Override
            public void onSeekProcessed() {
            }
        });
        player.seekTo(currentWindow, playbackPosition);
        player.prepare(dashMediaSource, true, false);
        player.setPlayWhenReady(true);
    }
}
