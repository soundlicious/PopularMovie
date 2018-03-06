package com.example.pablo.popularmovie1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pablo.popularmovie1.bases.BaseActivity;
import com.example.pablo.popularmovie1.data.models.MovieDetail;
import com.example.pablo.popularmovie1.utilities.MainMVPView;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements MainMVPView {

    @BindView(R.id.movie_grid)
    protected RecyclerView movieGrid;

    private MainPresenter<MainMVPView> presenter = new MainPresenter<>(BuildConfig.THEMOVIEDB_API, Locale.getDefault().toString().replace("_","-"));
    private MovieGridAdapter adapter;
    int pastVisiblesItems, visibleItemCount, totalItemCount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        setUnBinder(ButterKnife.bind(this));
        presenter.onAttach(this);

        showLoading();
        GridLayoutManager layoutManager = new GridLayoutManager(this, calculateNoOfColumns(this));
        movieGrid.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
                if(dy > 0) //check for scroll down
                {
                    visibleItemCount = layoutManager.getChildCount();
                    totalItemCount = layoutManager.getItemCount();
                    pastVisiblesItems = layoutManager.findFirstVisibleItemPosition();

                    if (!presenter.isLoading())
                    {
                        if ( (visibleItemCount + pastVisiblesItems) >= totalItemCount)
                        {
                            presenter.fetchPopularMovie();
                        }
                    }
                }
            }
        });
        movieGrid.setLayoutManager(layoutManager);
        adapter = new MovieGridAdapter(this);
        movieGrid.setAdapter(adapter);
        presenter.fetchPopularMovie();
    }

    @Override
    protected void onDestroy() {
        presenter.onDetach();
        super.onDestroy();
    }

    @Override
    public void populateList(List<MovieDetail> movieDetails) {
        adapter.populateList(movieDetails);
        adapter.notifyDataSetChanged();
    }

    /**
     * https://stackoverflow.com/a/44764780
     */
    public static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int scalingFactor = 200;
        int columnCount = (int) (dpWidth / scalingFactor);
        return (columnCount>=2?columnCount:2);
    }
}

class MovieGridAdapter extends RecyclerView.Adapter<MovieGridAdapter.ViewHolder> {
    private Context mContext;
    private List<MovieDetail> movieList;

    MovieGridAdapter(Context c) {
        mContext = c;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.movie_grid_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(movieList.get(position));
    }

    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return (movieList != null)? movieList.size():0;
    }

    void populateList(List<MovieDetail> movieDetails) {
        if (movieList != null)
            movieList.addAll(movieDetails);
        else
            movieList = movieDetails;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.movie_poster)
        ImageView poster;
        @BindView(R.id.movie_title)
        TextView title;
        @BindView(R.id.constraintLayout)
        ConstraintLayout layout;
        com.squareup.picasso.Target target = new com.squareup.picasso.Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                Palette.from(bitmap)
                        .generate(palette -> {
                            int defaultColorLayout;
                            int defaultColorText;
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                defaultColorLayout = mContext.getColor(R.color.colorAccent);
                                defaultColorText = mContext.getColor(R.color.white);
                            } else {
                                defaultColorLayout = mContext.getResources().getColor(R.color.colorAccent);
                                defaultColorText = mContext.getResources().getColor(R.color.white);
                            }
                            Palette.Swatch vibrant = palette.getVibrantSwatch();
                            int bgColor;
                            int textColor;
                            if (vibrant != null) {
                                bgColor = vibrant.getRgb();
                                textColor = vibrant.getTitleTextColor();
                            } else {
                                bgColor = palette.getDominantColor(defaultColorLayout);
                                textColor = palette.getVibrantColor(defaultColorText);
                            }
                            layout.setBackgroundColor(bgColor);
                            title.setTextColor(textColor);
                        });
                poster.setImageBitmap(bitmap);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                System.out.println("onBitmapFailed ");
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(MovieDetail movie){
            title.setText(movie.getTitle());
            Picasso.with(mContext)
                    .load(BuildConfig.MOVIEDBIMAGE_ENDPOINT + movie.getPosterPath())
                    .centerCrop()
                    .placeholder(R.drawable.ic_launcher_background)
                    .resize(200, 300)
                    .into(target);
            }



        @Override
        public void onClick(View view) {

        }
    }
}