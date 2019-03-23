package infos.generationchange.gctv.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.MediaSourceEventListener;
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

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;

import java.io.IOException;

import infos.generationchange.gctv.R;
import infos.generationchange.gctv.categories.EBoutique;
import infos.generationchange.gctv.categories.GctvKamtoNews;
import infos.generationchange.gctv.utils.CustomDialog;
import infos.generationchange.gctv.utils.FullScreenActivity;


public class DirectAndTv extends Fragment {
    private TextView textView3;
    private ImageView imageView;
    private ImageView imageView5;
    private PlayerView playerView;
    private Button eBoutique, kamtoNews, journaliste , camerLeaks;
    private TextView important;

    private static final String TAG = "DirectAndTv";

    public static SimpleExoPlayer player;

    private boolean playWhenReady = true;
    private int currentWindow = 0;
    private long playbackPosition = 0;
    private ImageView tv;
    private FrameLayout frameLayout;
    private ProgressBar loading;
    private ImageView fullSCreen;
    private String hls_url;
    public DirectAndTv() {
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tv, container, false);
        frameLayout = view.findViewById(R.id.parent_view);

        eBoutique = view.findViewById(R.id.eboutique);
        kamtoNews = view.findViewById(R.id.kamtonews);
        journaliste = view.findViewById(R.id.journaliste);

        important = view.findViewById(R.id.important);

        camerLeaks = view.findViewById(R.id.camerleaks);

        camerLeaks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity() , EBoutique.class);
                startActivity(i);
            }
        });

        tv = view.findViewById(R.id.tv);

        eBoutique.setOnClickListener(v -> {
            String url = "https://www.generation-change.info/boutique/";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        });

        journaliste.setOnClickListener((View v) -> {
            CustomDialog cdd = new CustomDialog((AppCompatActivity) getActivity());
            cdd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            cdd.show();
        });
        kamtoNews.setOnClickListener(v -> {
            String url = "http://freekamto.org/";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        });
        imageView5 = view.findViewById(R.id.imageView5);
        imageView = view.findViewById(R.id.imageView);
        textView3 = view.findViewById(R.id.textView3);

        imageView5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (hls_url != null) {
                    player.setPlayWhenReady(playWhenReady);
                    frameLayout.setVisibility(View.VISIBLE);
                    imageView5.setVisibility(View.GONE);
                    imageView.setVisibility(View.GONE);
                    textView3.setVisibility(View.GONE);
                    tv.setVisibility(View.GONE);
                }
            }
        });

        new FetchItems().execute();
        new FetchItems1().execute();
        return view;
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
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        playerView = getView().findViewById(R.id.videoplayer);
        fullSCreen = playerView.findViewById(R.id.fullscreen_icon);
        fullSCreen.setOnClickListener(v -> {
            playbackPosition = player.getCurrentPosition();
            Intent i = new Intent(getActivity(), FullScreenActivity.class);
            i.putExtra("time", playbackPosition);
            startActivity(i);
            getActivity().finish();
        });
        new FetchItems2().execute();
    }


    private class FetchItems2 extends AsyncTask<String, Void, JSONArray> {
        protected JSONArray doInBackground(String... params) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httpget = new HttpGet("http://dev.sdkgames.com/gctv/web/api/v01/gctv/streaming_url?_format=hal_json");
            //set header to tell REST endpoint the request and response content types
            httpget.setHeader("Accept", "application/json");
            httpget.setHeader("Content-type", "application/json");
            JSONArray json = new JSONArray();
            try {
                HttpResponse response = httpclient.execute(httpget);
                //read the response and convert it into JSON array
                json = new JSONArray(EntityUtils.toString(response.getEntity()));
                //return the JSON array for post processing to onPostExecute function
                return json;
            } catch (Exception e) {
            }
            return json;
        }

        //executed after the background nodes fetching process is complete
        protected void onPostExecute(JSONArray result) {
            for (int i = 0; i < result.length(); i++) {
                try {
                    hls_url = result.getJSONObject(i).getString("field_gctv_streaming_link");
                } catch (Exception e) {
                    Log.v("Error adding article", e.getMessage());
                }
            }
            if (hls_url == null) {
                hls_url = "https://5c213d823e63f.streamlock.net:1937/live/ngrp:GCTV.stream_all/manifest.mpd";
            }
            loading = getView().findViewById(R.id.loading);
            TrackSelection.Factory adaptiveTrackSelection = new AdaptiveTrackSelection.Factory(new DefaultBandwidthMeter());
            player = ExoPlayerFactory.newSimpleInstance(
                    new DefaultRenderersFactory(getContext()),
                    new DefaultTrackSelector(adaptiveTrackSelection),
                    new DefaultLoadControl());
            playerView.setPlayer(player);
            DefaultBandwidthMeter defaultBandwidthMeter = new DefaultBandwidthMeter();
            com.google.android.exoplayer2.upstream.DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getContext(),
                    Util.getUserAgent(getContext(), "GCTV"), defaultBandwidthMeter);
            Log.d(TAG, "onPostExecute: hlsurl "+hls_url);
            Uri uri = Uri.parse(hls_url);
            Handler mainHandler = new Handler();
            DashMediaSource dashMediaSource = new DashMediaSource(uri, dataSourceFactory,
                    new DefaultDashChunkSource.Factory(dataSourceFactory), mainHandler, new MediaSourceEventListener() {
                @Override
                public void onMediaPeriodCreated(int windowIndex, MediaSource.MediaPeriodId mediaPeriodId) {
                }
                @Override
                public void onMediaPeriodReleased(int windowIndex, MediaSource.MediaPeriodId mediaPeriodId) {
                }
                @Override
                public void onLoadStarted(int windowIndex, MediaSource.MediaPeriodId mediaPeriodId, LoadEventInfo loadEventInfo, MediaLoadData mediaLoadData) {
                }

                @Override
                public void onLoadCompleted(int windowIndex, MediaSource.MediaPeriodId mediaPeriodId, LoadEventInfo loadEventInfo, MediaLoadData mediaLoadData) {

                }

                @Override
                public void onLoadCanceled(int windowIndex, MediaSource.MediaPeriodId mediaPeriodId, LoadEventInfo loadEventInfo, MediaLoadData mediaLoadData) {

                }

                @Override
                public void onLoadError(int windowIndex, MediaSource.MediaPeriodId mediaPeriodId, LoadEventInfo loadEventInfo, MediaLoadData mediaLoadData, IOException error, boolean wasCanceled) {

                }

                @Override
                public void onReadingStarted(int windowIndex, MediaSource.MediaPeriodId mediaPeriodId) {

                }

                @Override
                public void onUpstreamDiscarded(int windowIndex, MediaSource.MediaPeriodId mediaPeriodId, MediaLoadData mediaLoadData) {

                }

                @Override
                public void onDownstreamFormatChanged(int windowIndex, MediaSource.MediaPeriodId mediaPeriodId, MediaLoadData mediaLoadData) {

                }
            });
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
                        case ExoPlayer.STATE_ENDED:
                            player.seekTo(0);
                            player.setPlayWhenReady(true);
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
            playbackPosition = getActivity().getIntent().getLongExtra("time", 0);
            if (playbackPosition != 0) {
                player.setPlayWhenReady(playWhenReady);
                frameLayout.setVisibility(View.VISIBLE);
                imageView5.setVisibility(View.GONE);
                imageView.setVisibility(View.GONE);
                textView3.setVisibility(View.GONE);
                tv.setVisibility(View.GONE);
            }
            player.seekTo(currentWindow, playbackPosition);
            player.prepare(dashMediaSource, true, false);
        }

    }

    private class FetchItems1 extends AsyncTask<String, Void, JSONArray> {
        protected JSONArray doInBackground(String... params) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httpget = new HttpGet("http://dev.sdkgames.com/gctv/web/api/v01/gctv/important_message?_format=hal_json");
            //set header to tell REST endpoint the request and response content types
            httpget.setHeader("Accept", "application/json");
            httpget.setHeader("Content-type", "application/json");
            JSONArray json = new JSONArray();
            try {
                HttpResponse response = httpclient.execute(httpget);
                //read the response and convert it into JSON array
                json = new JSONArray(EntityUtils.toString(response.getEntity()));
                //return the JSON array for post processing to onPostExecute function
                return json;
            } catch (Exception e) {
            }
            return json;
        }

        //executed after the background nodes fetching process is complete
        protected void onPostExecute(JSONArray result) {
            String titre = null;
            String breakingNews = null;
            for (int i = 0; i < result.length(); i++) {
                try {
                    titre = result.getJSONObject(i).getString("title");
                    breakingNews = result.getJSONObject(i).getString("field_infos_importante");
                } catch (Exception e) {
                    Log.v("Error adding article", e.getMessage());
                }
            }

            if (breakingNews != null && !breakingNews.trim().equals("")) {
                important.setVisibility(View.VISIBLE);
                String text = "<b>" + titre + "</b>" + "<br>" + breakingNews;
                important.setText(Html.fromHtml(text));
            }
        }

    }

    private class FetchItems extends AsyncTask<String, Void, JSONArray> {
        protected JSONArray doInBackground(String... params) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httpget = new HttpGet("http://dev.sdkgames.com/gctv/web/api/v01/gctv/image_live?_format=hal_json");
            //set header to tell REST endpoint the request and response content types
            httpget.setHeader("Accept", "application/json");
            httpget.setHeader("Content-type", "application/json");
            JSONArray json = new JSONArray();
            try {
                HttpResponse response = httpclient.execute(httpget);
                //read the response and convert it into JSON array
                json = new JSONArray(EntityUtils.toString(response.getEntity()));
                //return the JSON array for post processing to onPostExecute function
                return json;
            } catch (Exception e) {
            }
            return json;
        }

        //executed after the background nodes fetching process is complete
        protected void onPostExecute(JSONArray result) {
            String url = null;
            for (int i = 0; i < result.length(); i++) {
                try {
                    url = "http://dev.sdkgames.com" + result.getJSONObject(i).getString("field_image_direct").toString();
                    Log.d(TAG, "onPostExecute: url "+url);
                } catch (Exception e) {
                    Log.v("Error adding article", e.getMessage());
                }
            }
            Log.d(TAG, "onPostExecute: url " + url);
            Glide.with(getActivity()).load(url).listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    tv.setBackgroundResource(R.drawable.tv);
                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    return false;
                }
            }).into(tv);
        }

    }

}
