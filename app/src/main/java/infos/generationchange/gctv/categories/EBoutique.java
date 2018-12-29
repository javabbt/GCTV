package infos.generationchange.gctv.categories;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;




import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import infos.generationchange.gctv.R;

public class EBoutique extends AppCompatActivity {

    private Toolbar toolbar;

    private ImageView back;

    private static final String TAG = "EBoutique";

    private RelativeLayout enchantier;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gctv_news);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        back = toolbar.findViewById(R.id.back);
        enchantier = findViewById(R.id.enchantier);
        back.setOnClickListener(v -> {
            finish();
        });
    }

}
