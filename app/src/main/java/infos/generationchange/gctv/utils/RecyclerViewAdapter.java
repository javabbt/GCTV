package infos.generationchange.gctv.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
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
import com.github.barteksc.pdfviewer.PDFView;

import de.hdodenhof.circleimageview.CircleImageView;
import infos.generationchange.gctv.PdfView;
import infos.generationchange.gctv.R;
import infos.generationchange.gctv.SingleItem;
import infos.generationchange.gctv.categories.PdfModel;
import infos.generationchange.gctv.models.MainModel;
import infos.generationchange.gctv.models.NewsModel;

import java.util.List;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private List<MainModel> list;
    private Context context;
    private Typeface tf;
    private int categorie;

    private static final String domain = "http://dev.sdkgames.com";

    private static final String TAG = "RecyclerViewAdapter";



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
        else if(categorie == 10)
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_pdf, parent, false);
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

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView  description;
        private ImageView thumbNail;
        private ProgressBar progressBar;
        private CardView root;
        private ImageView play;
        private CircleImageView preview ;
        private TextView title , description1;

        ViewHolder(View itemView) {
            super(itemView);
            if(categorie == 10){
                preview = itemView.findViewById(R.id.preview);
                title = itemView.findViewById(R.id.title);
                description1 = itemView.findViewById(R.id.description1);
            }else{
                description = itemView.findViewById(R.id.description);
                description.setTypeface(tf);
                thumbNail = itemView.findViewById(R.id.thumbNailImage);
                progressBar = itemView.findViewById(R.id.progress);
                root = itemView.findViewById(R.id.root);
                play = itemView.findViewById(R.id.play);
            }

        }

        void bind(final MainModel item) {

            String youtubeLink;

            switch (categorie) {
                case 0:
                    youtubeLink = ((NewsModel) item).getYoutubeLink();

                    if(!youtubeLink.equals("empty") && !youtubeLink.equals("") && !youtubeLink.equals("false")){
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

                    root.setOnClickListener(v -> {
                        Intent i = new Intent(context , SingleItem.class);
                        i.putExtra("picture" , domain + ((NewsModel) item).getThumbNail());
                        i.putExtra("description" , ((NewsModel) item).getDescription());
                        i.putExtra("title" , "Emissions");
                        i.putExtra("link" , ((NewsModel) item).getYoutubeLink());
                        context.startActivity(i);
                    });
                    break;

                case 10:
                    Glide.with(context).load(domain+((PdfModel)item).getField_image_de_fond()).into(preview);
                    title.setText(((PdfModel)item).getTitle());
                    description1.setText(((PdfModel)item).getField_description_article());
                    preview.setOnClickListener(v -> {
                        Intent i = new Intent(context , PdfView.class);
                        i.putExtra("link" , domain+((PdfModel)item).getField_pdf_camerleak());
                        context.startActivity(i);
                    });
                    title.setOnClickListener(v -> {
                        Intent i = new Intent(context , PdfView.class);
                        i.putExtra("link" , domain+((PdfModel)item).getField_pdf_camerleak());
                        context.startActivity(i);
                    });
                    description1.setOnClickListener(v -> {
                        Intent i = new Intent(context , PdfView.class);
                        i.putExtra("link" , domain+((PdfModel)item).getField_pdf_camerleak());
                        context.startActivity(i);
                    });
                    break;
                case 1:

                    youtubeLink = ((NewsModel) item).getYoutubeLink();

                    if(!youtubeLink.equals("empty") && !youtubeLink.equals("") && !youtubeLink.equals("false")){
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

                    if(!youtubeLink.equals("empty") && !youtubeLink.equals("") && !youtubeLink.equals("false")){
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

                    if(!youtubeLink.equals("empty") && !youtubeLink.equals("") && !youtubeLink.equals("false")){
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

                    if(!youtubeLink.equals("empty") && !youtubeLink.equals("") && !youtubeLink.equals("false")){
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

                    if(!youtubeLink.equals("empty") && !youtubeLink.equals("") && !youtubeLink.equals("false")){
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

                    if(!youtubeLink.equals("empty") && !youtubeLink.equals("") && !youtubeLink.equals("false")){
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

                    Log.d(TAG, "bind: youtube link : "+youtubeLink);

                    if(!youtubeLink.equals("empty") && !youtubeLink.equals("") && !youtubeLink.equals("false")){
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

                    if(!youtubeLink.equals("empty") && !youtubeLink.equals("") && !youtubeLink.equals("false")){
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
