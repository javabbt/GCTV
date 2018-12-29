package infos.generationchange.gctv.categories;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import infos.generationchange.gctv.R;
import infos.generationchange.gctv.models.MainModel;
import infos.generationchange.gctv.models.NewsModel;
import infos.generationchange.gctv.utils.RecyclerViewAdapter;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class GctvKamtoNews extends AppCompatActivity {

    private Toolbar toolbar;

    private ImageView back;

    private static final String TAG = "GctvKamtoNews";

    private RelativeLayout enchantier;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gctv_kamto_news);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        enchantier = findViewById(R.id.enchantier);
        back = toolbar.findViewById(R.id.back);
        progressBar = findViewById(R.id.progress);
        back.setOnClickListener(v -> {
            finish();
        });
        new GctvKamtoNews.FetchItems().execute();
    }


    private class FetchItems extends AsyncTask<String, Void, JSONArray> {
        protected JSONArray doInBackground(String... params) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httpget = new HttpGet("https://dev.sdkgames.com/gctv/web/api/v01/gctv/contenu/3?_format=hal_json");
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
                Log.v("Error adding article",e.getMessage());
            }
            return json;
        }

        //executed after the background nodes fetching process is complete
        protected void onPostExecute(JSONArray result) {
            //get the ListView UI element
            RecyclerView recyclerView = findViewById(R.id.recyclerView);
            LinearLayoutManager layoutManager = new LinearLayoutManager(GctvKamtoNews.this, RecyclerView.VERTICAL, false);
            recyclerView.setLayoutManager(layoutManager);
            //create the ArrayList to store the titles of nodes
            List<MainModel> listItems=new ArrayList<MainModel>();
            //iterate through JSON to read the title of nodes
            for(int i=0;i<result.length();i++){
                try {
                    String description = result.getJSONObject(i).getString("field_description_article").toString();
                    String thumbNail = result.getJSONObject(i).getString("field_image_de_fond").toString();
                    Log.d(TAG, "onPostExecute: description : " +description+" thumbnail : "+thumbNail);
                    String youtubeLink = result.getJSONObject(i).getString("field_video_link").toString();
                    listItems.add(new NewsModel(thumbNail , description , youtubeLink));
                } catch (Exception e) {
                    Log.v("Error adding article", e.getMessage());
                }
            }
            progressBar.setVisibility(View.GONE);
            if(listItems.isEmpty())
                enchantier.setVisibility(View.VISIBLE);
            //create array adapter and give it our list of nodes, pass context, layout and list of items
            RecyclerViewAdapter ad= new RecyclerViewAdapter( listItems , GctvKamtoNews.this , 3);
            //give adapter to ListView UI element to render
            recyclerView.setAdapter(ad);
        }
    }
}
