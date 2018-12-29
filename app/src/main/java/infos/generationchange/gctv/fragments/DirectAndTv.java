package infos.generationchange.gctv.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;

import androidx.appcompat.app.AlertDialog;
import infos.generationchange.gctv.R;
import infos.generationchange.gctv.categories.GctvKamtoNews;
import infos.generationchange.gctv.categories.EBoutique;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import cn.jzvd.JZUserAction;
import cn.jzvd.JZUserActionStd;
import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;
import infos.generationchange.gctv.models.MainModel;
import infos.generationchange.gctv.models.NewsModel;
import infos.generationchange.gctv.utils.CustomDialog;
import infos.generationchange.gctv.utils.RecyclerViewAdapter;


public class DirectAndTv extends Fragment {
    private TextView textView3;
    private ImageView imageView , bg;
    private ImageView imageView5;
    private JzvdStd jzvdStd;
    private Button eBoutique , dons , tontinard , kamtoNews , journaliste;

    private static final String TAG = "DirectAndTv";
    public DirectAndTv(){ }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.tv , container , false);

        jzvdStd = (JzvdStd) view.findViewById(R.id.videoplayer);

        eBoutique = view.findViewById(R.id.eboutique);
        dons = view.findViewById(R.id.dons);
        tontinard = view.findViewById(R.id.tontinard);
        kamtoNews = view.findViewById(R.id.kamtonews);
        journaliste = view.findViewById(R.id.journaliste);

        bg = view.findViewById(R.id.bg);

        eBoutique.setOnClickListener(v -> {
            startActivity(new Intent(getActivity() , EBoutique.class));
        });

        dons.setOnClickListener(v -> {
            String url = "https://www.gofundme.com/generation-change-television";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        });

        tontinard.setOnClickListener(v -> {
            String url = "https://www.paypal.me/GCTVLLC";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        });

        journaliste.setOnClickListener((View v) -> {
            CustomDialog cdd = new CustomDialog(getActivity());
            cdd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            cdd.show();
        });

        kamtoNews.setOnClickListener(v -> {
            startActivity(new Intent(getActivity() , GctvKamtoNews.class));
        });


        imageView5 = view.findViewById(R.id.imageView5);
        imageView = view.findViewById(R.id.imageView);
        textView3 = view.findViewById(R.id.textView3);

        imageView5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jzvdStd.setVisibility(View.VISIBLE);
                imageView5.setVisibility(View.GONE);
                bg.setVisibility(View.GONE);
                imageView.setVisibility(View.GONE);
                textView3.setVisibility(View.GONE);
                jzvdStd.setUp("http://generation-change.info:1935/live/ngrp:GCTV.stream_all/playlist.m3u8",
                        "" , Jzvd.SCREEN_WINDOW_NORMAL);
                jzvdStd.startWindowTiny();
            }
        });
        new FetchItems().execute();
        return view;
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
            }catch (Exception e) {
            }
            return json;
        }

        //executed after the background nodes fetching process is complete
        protected void onPostExecute(JSONArray result) {
            String url = null;
            for(int i=0;i<result.length();i++){
                try {
                    url = "http://dev.sdkgames.com"+result.getJSONObject(i).getString("field_image_direct").toString();
                } catch (Exception e) {
                    Log.v("Error adding article", e.getMessage());
                }
            }
            Log.d(TAG, "onPostExecute: url "+url);
            Glide.with(getActivity()).load(url).listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    bg.setBackgroundResource(R.drawable.tv);
                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    return false;
                }
            }).into(bg);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Jzvd.releaseAllVideos();
    }

    @Override
    public void onStop() {
        super.onStop();
        Jzvd.releaseAllVideos();
    }

    class MyUserActionStd implements JZUserActionStd {

        @Override
        public void onEvent(int type, Object url, int screen, Object... objects) {
            switch (type) {
                case JZUserAction.ON_CLICK_START_ICON:
                    Log.i("USER_EVENT", "ON_CLICK_START_ICON" + " title is : " + (objects.length == 0 ? "" : objects[0]) + " url is : " + url + " screen is : " + screen);
                    break;
                case JZUserAction.ON_CLICK_START_ERROR:
                    Log.i("USER_EVENT", "ON_CLICK_START_ERROR" + " title is : " + (objects.length == 0 ? "" : objects[0]) + " url is : " + url + " screen is : " + screen);
                    break;
                case JZUserAction.ON_CLICK_START_AUTO_COMPLETE:
                    Log.i("USER_EVENT", "ON_CLICK_START_AUTO_COMPLETE" + " title is : " + (objects.length == 0 ? "" : objects[0]) + " url is : " + url + " screen is : " + screen);
                    break;
                case JZUserAction.ON_CLICK_PAUSE:
                    Log.i("USER_EVENT", "ON_CLICK_PAUSE" + " title is : " + (objects.length == 0 ? "" : objects[0]) + " url is : " + url + " screen is : " + screen);
                    break;
                case JZUserAction.ON_CLICK_RESUME:
                    Log.i("USER_EVENT", "ON_CLICK_RESUME" + " title is : " + (objects.length == 0 ? "" : objects[0]) + " url is : " + url + " screen is : " + screen);
                    break;
                case JZUserAction.ON_SEEK_POSITION:
                    Log.i("USER_EVENT", "ON_SEEK_POSITION" + " title is : " + (objects.length == 0 ? "" : objects[0]) + " url is : " + url + " screen is : " + screen);
                    break;
                case JZUserAction.ON_AUTO_COMPLETE:
                    Log.i("USER_EVENT", "ON_AUTO_COMPLETE" + " title is : " + (objects.length == 0 ? "" : objects[0]) + " url is : " + url + " screen is : " + screen);
                    break;
                case JZUserAction.ON_ENTER_FULLSCREEN:
                    Log.i("USER_EVENT", "ON_ENTER_FULLSCREEN" + " title is : " + (objects.length == 0 ? "" : objects[0]) + " url is : " + url + " screen is : " + screen);
                    break;
                case JZUserAction.ON_QUIT_FULLSCREEN:
                    Log.i("USER_EVENT", "ON_QUIT_FULLSCREEN" + " title is : " + (objects.length == 0 ? "" : objects[0]) + " url is : " + url + " screen is : " + screen);
                    break;
                case JZUserAction.ON_ENTER_TINYSCREEN:
                    Log.i("USER_EVENT", "ON_ENTER_TINYSCREEN" + " title is : " + (objects.length == 0 ? "" : objects[0]) + " url is : " + url + " screen is : " + screen);
                    break;
                case JZUserAction.ON_QUIT_TINYSCREEN:
                    Log.i("USER_EVENT", "ON_QUIT_TINYSCREEN" + " title is : " + (objects.length == 0 ? "" : objects[0]) + " url is : " + url + " screen is : " + screen);
                    break;
                case JZUserAction.ON_TOUCH_SCREEN_SEEK_VOLUME:
                    Log.i("USER_EVENT", "ON_TOUCH_SCREEN_SEEK_VOLUME" + " title is : " + (objects.length == 0 ? "" : objects[0]) + " url is : " + url + " screen is : " + screen);
                    break;
                case JZUserAction.ON_TOUCH_SCREEN_SEEK_POSITION:
                    Log.i("USER_EVENT", "ON_TOUCH_SCREEN_SEEK_POSITION" + " title is : " + (objects.length == 0 ? "" : objects[0]) + " url is : " + url + " screen is : " + screen);
                    break;

                case JZUserActionStd.ON_CLICK_START_THUMB:
                    Log.i("USER_EVENT", "ON_CLICK_START_THUMB" + " title is : " + (objects.length == 0 ? "" : objects[0]) + " url is : " + url + " screen is : " + screen);
                    break;
                case JZUserActionStd.ON_CLICK_BLANK:
                    Log.i("USER_EVENT", "ON_CLICK_BLANK" + " title is : " + (objects.length == 0 ? "" : objects[0]) + " url is : " + url + " screen is : " + screen);
                    break;
                default:
                    Log.i("USER_EVENT", "unknow");
                    break;
            }
        }
    }
}
