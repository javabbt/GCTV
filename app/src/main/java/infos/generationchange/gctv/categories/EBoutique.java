package infos.generationchange.gctv.categories;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;


import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;
import infos.generationchange.gctv.R;
import infos.generationchange.gctv.fragments.ALaUne;
import infos.generationchange.gctv.models.MainModel;
import infos.generationchange.gctv.models.NewsModel;
import infos.generationchange.gctv.utils.RecyclerViewAdapter;

public class EBoutique extends AppCompatActivity {

    private Toolbar toolbar;

    private ImageView back;

    private static final String TAG = "EBoutique";

    private RecyclerView pdfs;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gctv_news);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (android.os.Build.VERSION.SDK_INT >= 21)
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        back = toolbar.findViewById(R.id.back);
        back.setOnClickListener(v -> {
            finish();
        });
        pdfs = findViewById(R.id.pdfs);
        progressBar = findViewById(R.id.progressbar);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        pdfs.setLayoutManager(layoutManager);
        new FetchItems().execute();
    }


    private class FetchItems extends AsyncTask<String, Void, JSONArray> {
        protected JSONArray doInBackground(String... params) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httpget = new HttpGet("https://dev.sdkgames.com/gctv/web/api/v01/gctv/camerleak?_format=hal_json");
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
            //create the ArrayList to store the titles of nodes
            List<MainModel> pdfItems=new ArrayList<>();
            //iterate through JSON to read the title of nodes
            for(int i=0;i<result.length();i++){
                try {
                    String field_image_de_fond = result.getJSONObject(i).getString("field_image_de_fond");
                    String field_description_article = result.getJSONObject(i).getString("field_description_article");
                    String title = result.getJSONObject(i).getString("title");
                    String field_pdf_camerleak = result.getJSONObject(i).getString("field_pdf_camerleak");
                    pdfItems.add(new PdfModel(field_image_de_fond , field_description_article , title , field_pdf_camerleak));
                } catch (Exception e) {
                    Log.v("Error adding article", e.getMessage());
                }
            }
            progressBar.setVisibility(View.GONE);
            //create array adapter and give it our list of nodes, pass context, layout and list of items
            RecyclerViewAdapter ad= new RecyclerViewAdapter( pdfItems , getApplicationContext() , 10);
            //give adapter to ListView UI element to render
            pdfs.setAdapter(ad);
        }
    }

}
