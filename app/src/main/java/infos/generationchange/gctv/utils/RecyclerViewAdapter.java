package infos.generationchange.gctv.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import infos.generationchange.gctv.R;
import infos.generationchange.gctv.SingleItem;
import infos.generationchange.gctv.models.MainModel;
import infos.generationchange.gctv.models.NewsModel;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private List<MainModel> list;
    private Context context;
    private Typeface tf;
    private int categorie;

    private static final String domain = "http://dev.sdkgames.com";

    public RecyclerViewAdapter(List<MainModel> list, Context context, int category) {
        this.list = list;
        this.context = context;
        tf = Typeface.createFromAsset(context.getAssets(), "BitterBold.ttf");
        this.categorie = category;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        if(categorie == 0)
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_emissions, parent, false);
        if(categorie == 7)
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_emissions, parent, false);
        if(categorie == 8)
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_emissions, parent, false);
        if(categorie == 1)
             view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_news, parent, false);
        else if(categorie == 2)
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_business, parent, false);
        else if(categorie == 3)
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_kamto_news, parent, false);
        else if(categorie == 4)
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_arts, parent, false);
        else if(categorie == 5)
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_sports, parent, false);
        else if(categorie == 6)
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_education, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(list.get(position));
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView share, description;
        private ImageView thumbNail;
        private ProgressBar progressBar;
        private CardView root;
        private ImageView play;

        public ViewHolder(View itemView) {
            super(itemView);
            share = itemView.findViewById(R.id.share);
            description = itemView.findViewById(R.id.description);
            description.setTypeface(tf);
            thumbNail = itemView.findViewById(R.id.thumbNailImage);
            progressBar = itemView.findViewById(R.id.progress);
            root = itemView.findViewById(R.id.root);
            play = itemView.findViewById(R.id.play);
        }

        public void bind(final MainModel item) {

            String youtubeLink;

            switch (categorie) {
                case 0:
                    youtubeLink = ((NewsModel) item).getYoutubeLink();

                    if(youtubeLink != null){
                        play.setVisibility(View.VISIBLE);
                    }

                    description.setText(((NewsModel) item).getDescription());
                    Glide.with(context).load(domain + ((NewsModel) item).getThumbNail())
                            .listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    thumbNail.setBackgroundResource(R.drawable.kamto);
                                    progressBar.setVisibility(View.GONE);
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                    progressBar.setVisibility(View.GONE);
                                    return false;
                                }
                            })
                            .into(thumbNail);
                    share.setOnClickListener(v -> {

                    });
                    root.setOnClickListener(v -> {
                        Intent i = new Intent(context , SingleItem.class);
                        i.putExtra("picture" , domain + ((NewsModel) item).getThumbNail());
                        i.putExtra("description" , ((NewsModel) item).getDescription());
                        i.putExtra("title" , "Emissions");
                        i.putExtra("link" , ((NewsModel) item).getYoutubeLink());
                        context.startActivity(i);
                    });

                    break;
                case 1:

                    youtubeLink = ((NewsModel) item).getYoutubeLink();

                    if(youtubeLink != null){
                        play.setVisibility(View.VISIBLE);
                    }

                    description.setText(((NewsModel) item).getDescription());
                    Glide.with(context).load(domain + ((NewsModel) item).getThumbNail())
                            .listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    thumbNail.setBackgroundResource(R.drawable.kamto);
                                    progressBar.setVisibility(View.GONE);
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                    progressBar.setVisibility(View.GONE);
                                    return false;
                                }
                            })
                            .into(thumbNail);
                    share.setOnClickListener(v -> {

                    });
                    root.setOnClickListener(v -> {
                        Intent i = new Intent(context , SingleItem.class);
                        i.putExtra("picture" , domain + ((NewsModel) item).getThumbNail());
                        i.putExtra("description" , ((NewsModel) item).getDescription());
                        i.putExtra("title" , "News");
                        i.putExtra("link" , ((NewsModel) item).getYoutubeLink());
                        context.startActivity(i);
                    });
                    break;

                case 2:
                    youtubeLink = ((NewsModel) item).getYoutubeLink();

                    if(youtubeLink != null){
                        play.setVisibility(View.VISIBLE);
                    }

                    description.setText(((NewsModel) item).getDescription());
                    Glide.with(context).load(domain + ((NewsModel) item).getThumbNail())
                            .listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    thumbNail.setBackgroundResource(R.drawable.kamto);
                                    progressBar.setVisibility(View.GONE);
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                    progressBar.setVisibility(View.GONE);
                                    return false;
                                }
                            })
                            .into(thumbNail);
                    share.setOnClickListener(v -> {

                    });
                    root.setOnClickListener(v -> {
                        Intent i = new Intent(context , SingleItem.class);
                        i.putExtra("picture" , domain + ((NewsModel) item).getThumbNail());
                        i.putExtra("description" , ((NewsModel) item).getDescription());
                        i.putExtra("title" , "Business");
                        i.putExtra("link" , ((NewsModel) item).getYoutubeLink());
                        context.startActivity(i);
                    });
                    break;

                case 3:

                    youtubeLink = ((NewsModel) item).getYoutubeLink();

                    if(youtubeLink != null){
                        play.setVisibility(View.VISIBLE);
                    }


                    description.setText(((NewsModel) item).getDescription());
                    Glide.with(context).load(domain + ((NewsModel) item).getThumbNail())
                            .listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    thumbNail.setBackgroundResource(R.drawable.kamto);
                                    progressBar.setVisibility(View.GONE);
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                    progressBar.setVisibility(View.GONE);
                                    return false;
                                }
                            })
                            .into(thumbNail);
                    share.setOnClickListener(v -> {

                    });
                    root.setOnClickListener(v -> {
                        Intent i = new Intent(context , SingleItem.class);
                        i.putExtra("picture" , domain + ((NewsModel) item).getThumbNail());
                        i.putExtra("description" , ((NewsModel) item).getDescription());
                        i.putExtra("title" , "Kamto News");
                        i.putExtra("link" , ((NewsModel) item).getYoutubeLink());
                        context.startActivity(i);
                    });
                    break;

                case 4:

                    youtubeLink = ((NewsModel) item).getYoutubeLink();

                    if(youtubeLink != null){
                        play.setVisibility(View.VISIBLE);
                    }


                    description.setText(((NewsModel) item).getDescription());
                    Glide.with(context).load(domain + ((NewsModel) item).getThumbNail())
                            .listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    thumbNail.setBackgroundResource(R.drawable.kamto);
                                    progressBar.setVisibility(View.GONE);
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                    progressBar.setVisibility(View.GONE);
                                    return false;
                                }
                            })
                            .into(thumbNail);
                    share.setOnClickListener(v -> {

                    });
                    root.setOnClickListener(v -> {
                        Intent i = new Intent(context , SingleItem.class);
                        i.putExtra("picture" , domain + ((NewsModel) item).getThumbNail());
                        i.putExtra("description" , ((NewsModel) item).getDescription());
                        i.putExtra("title" , "Arts");
                        i.putExtra("link" , ((NewsModel) item).getYoutubeLink());
                        context.startActivity(i);
                    });
                    break;

                case 5:

                    youtubeLink = ((NewsModel) item).getYoutubeLink();

                    if(youtubeLink != null){
                        play.setVisibility(View.VISIBLE);
                    }


                    description.setText(((NewsModel) item).getDescription());
                    Glide.with(context).load(domain + ((NewsModel) item).getThumbNail())
                            .listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    thumbNail.setBackgroundResource(R.drawable.kamto);
                                    progressBar.setVisibility(View.GONE);
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                    progressBar.setVisibility(View.GONE);
                                    return false;
                                }
                            })
                            .into(thumbNail);
                    share.setOnClickListener(v -> {

                    });
                    root.setOnClickListener(v -> {
                        Intent i = new Intent(context , SingleItem.class);
                        i.putExtra("picture" , domain + ((NewsModel) item).getThumbNail());
                        i.putExtra("description" , ((NewsModel) item).getDescription());
                        i.putExtra("title" , "Sports");
                        i.putExtra("link" , ((NewsModel) item).getYoutubeLink());
                        context.startActivity(i);
                    });
                    break;

                case 6:

                    youtubeLink = ((NewsModel) item).getYoutubeLink();

                    if(youtubeLink != null){
                        play.setVisibility(View.VISIBLE);
                    }

                    description.setText(((NewsModel) item).getDescription());
                    Glide.with(context).load(domain + ((NewsModel) item).getThumbNail())
                            .listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    thumbNail.setBackgroundResource(R.drawable.kamto);
                                    progressBar.setVisibility(View.GONE);
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                    progressBar.setVisibility(View.GONE);
                                    return false;
                                }
                            })
                            .into(thumbNail);
                    share.setOnClickListener(v -> {

                    });
                    root.setOnClickListener(v -> {
                        Intent i = new Intent(context , SingleItem.class);
                        i.putExtra("picture" , domain + ((NewsModel) item).getThumbNail());
                        i.putExtra("description" , ((NewsModel) item).getDescription());
                        i.putExtra("title" , "Education");
                        i.putExtra("link" , ((NewsModel) item).getYoutubeLink());
                        context.startActivity(i);
                    });
                    break;

                case 7:

                    youtubeLink = ((NewsModel) item).getYoutubeLink();

                    if(youtubeLink != null){
                        play.setVisibility(View.VISIBLE);
                    }


                    description.setText(((NewsModel) item).getDescription());
                    Glide.with(context).load(domain + ((NewsModel) item).getThumbNail())
                            .listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    thumbNail.setBackgroundResource(R.drawable.kamto);
                                    progressBar.setVisibility(View.GONE);
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                    progressBar.setVisibility(View.GONE);
                                    return false;
                                }
                            })
                            .into(thumbNail);
                    share.setOnClickListener(v -> {

                    });
                    root.setOnClickListener(v -> {
                        Intent i = new Intent(context , SingleItem.class);
                        i.putExtra("picture" , domain + ((NewsModel) item).getThumbNail());
                        i.putExtra("description" , ((NewsModel) item).getDescription());
                        i.putExtra("title" , "A la une");
                        i.putExtra("link" , ((NewsModel) item).getYoutubeLink());
                        context.startActivity(i);
                    });
                    break;

                case 8:

                    youtubeLink = ((NewsModel) item).getYoutubeLink();

                    if(youtubeLink != null){
                        play.setVisibility(View.VISIBLE);
                    }

                    description.setText(Html.fromHtml(((NewsModel) item).getDescription()));

                    Glide.with(context).load(domain + ((NewsModel) item).getThumbNail())
                            .listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    thumbNail.setBackgroundResource(R.drawable.kamto);
                                    progressBar.setVisibility(View.GONE);
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                    progressBar.setVisibility(View.GONE);
                                    return false;
                                }
                            })
                            .into(thumbNail);
                    share.setOnClickListener(v -> {

                    });
                    root.setOnClickListener(v -> {
                        Intent i = new Intent(context , SingleItem.class);
                        i.putExtra("picture" , domain + ((NewsModel) item).getThumbNail());
                        i.putExtra("description" , ((NewsModel) item).getDescription());
                        i.putExtra("title" , "A la une");
                        i.putExtra("link" , ((NewsModel) item).getYoutubeLink());
                        context.startActivity(i);
                    });
                    break;

            }


        }

    }
}
