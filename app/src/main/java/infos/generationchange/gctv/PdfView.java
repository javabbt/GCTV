package infos.generationchange.gctv;

import androidx.appcompat.app.AppCompatActivity;
import es.voghdev.pdfviewpager.library.RemotePDFViewPager;
import es.voghdev.pdfviewpager.library.adapter.PDFPagerAdapter;
import es.voghdev.pdfviewpager.library.remote.DownloadFile;
import es.voghdev.pdfviewpager.library.util.FileUtil;

import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnDrawListener;
import com.github.barteksc.pdfviewer.util.FitPolicy;

public class PdfView extends AppCompatActivity implements DownloadFile.Listener {
    private String link;
    ProgressBar progressbar;
    RemotePDFViewPager remotePDFViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pdf_viewer);
        link = getIntent().getStringExtra("link");
        progressbar = findViewById(R.id.progressbar);
        remotePDFViewPager =
                new RemotePDFViewPager(this
                        , link, this);
        remotePDFViewPager.setId(R.id.pdfViewPager);

    }

    private static final String TAG = "PdfView";
    @Override
    public void onSuccess(String url, String destinationPath) {
        Log.d(TAG, "onSuccess: url is "+destinationPath);
        PDFPagerAdapter adapter = new PDFPagerAdapter(this, FileUtil.extractFileNameFromURL(destinationPath));
        remotePDFViewPager.setAdapter(adapter);
        progressbar.setVisibility(View.GONE);
    }

    @Override
    public void onFailure(Exception e) {
        Log.d(TAG, "onFailure: error is"+e);
    }

    @Override
    public void onProgressUpdate(int progress, int total) {
        Log.d(TAG, "onProgressUpdate: progress is "+progress);
    }
}
