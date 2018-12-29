package infos.generationchange.gctv;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import infos.generationchange.gctv.models.MainModel;
import infos.generationchange.gctv.models.NewsModel;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;


import infos.generationchange.gctv.utils.RecyclerViewAdapter;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private SearchView searchView;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TextView enChantier;
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));

        searchView = findViewById(R.id.searchview);
        recyclerView = findViewById(R.id.recycler);

        progressBar = findViewById(R.id.search);

        enChantier = findViewById(R.id.enchantier);

        back = findViewById(R.id.back);

        back.setOnClickListener(v -> {finish();});

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        searchView.setActivated(true);
        searchView.setQueryHint("Entrez votre mot clé");
        searchView.onActionViewExpanded();
        searchView.setIconified(false);
        searchView.clearFocus();


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                new FetchItems(query).execute();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private class FetchItems extends AsyncTask<String, Void, JSONArray> {
        String newText;
        public FetchItems(String newText) {
            this.newText = newText;
            progressBar.setVisibility(View.VISIBLE);
        }

        protected JSONArray doInBackground(String... params) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httpget = new HttpGet("https://dev.sdkgames.com/gctv/web/api/v01/gctv/contenu?_format=hal_json");
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
            List<MainModel> listItems=new ArrayList<MainModel>();
            //iterate through JSON to read the title of nodes
            for(int i=0;i<result.length();i++){
                try {
                    String description = result.getJSONObject(i).getString("field_description_article").toLowerCase().toString();
                    if(description.contains(newText.toLowerCase())) {
                        String newString = description.replaceAll(newText, "<font color='red'>"+newText+"</font>");
                        String thumbNail = result.getJSONObject(i).getString("field_image_de_fond").toString();
                        String youtubeLink = result.getJSONObject(i).getString("field_video_link").toString();
                        listItems.add(new NewsModel(thumbNail, newString, youtubeLink));
                    }
                } catch (Exception e) {
                    Log.v("Error adding article", e.getMessage());
                }
            }
            progressBar.setVisibility(View.GONE);
            if(listItems.isEmpty())
                enChantier.setVisibility(View.VISIBLE);
            else enChantier.setVisibility(View.GONE);
            //create array adapter and give it our list of nodes, pass context, layout and list of items
            RecyclerViewAdapter ad= new RecyclerViewAdapter( listItems , SearchActivity.this , 8);
            //give adapter to ListView UI element to render
            recyclerView.setAdapter(ad);
        }
    }
}
