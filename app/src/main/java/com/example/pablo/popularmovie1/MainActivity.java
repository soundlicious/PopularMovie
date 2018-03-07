package com.example.pablo.popularmovie1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pablo.popularmovie1.bases.BaseActivity;
import com.example.pablo.popularmovie1.data.models.MovieDetail;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements MainMVPView {

    private static final String MOVIE_LIST = "movieList";
    private static final String CATEGORIE = "categorie";
    private static final String MAX_PAGE = "maxPage";
    private static final String CURRENT_PAGE = "currentPage";

    @BindView(R.id.movie_grid)
    protected RecyclerView movieGrid;

    private MainPresenter presenter;
    private MovieGridAdapter adapter;

    private int tempCat;
    private String[] categories;

    private int visibleItemCount;
    private int totalItemCount;
    private int pastVisiblesItems;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ArrayList<MovieDetail> movieList = null;
        String apiKey = BuildConfig.THEMOVIEDB_API;
        String language = Locale.getDefault().toString().replace("_", "-");
        categories = getResources().getStringArray(R.array.categorie_movie_filter);

        setContentView(R.layout.activity_main);

        try {
            MainPresenter.Builder builder = new MainPresenter.Builder()
                    .setApiKey(apiKey)
                    .setLanguage(language);

            if (savedInstanceState != null) {
                builder = builder.setCategorie(savedInstanceState.getInt(CATEGORIE))
                        .setCurrentPage(savedInstanceState.getInt(CURRENT_PAGE))
                        .setLoading(false)
                        .setMaxPage(savedInstanceState.getInt(MAX_PAGE));
                movieList = savedInstanceState.getParcelableArrayList(MOVIE_LIST);
            }

            presenter = builder.build();
            setUnBinder(ButterKnife.bind(this));
            presenter.onAttach(this); //REMARK How to check that call?

            GridLayoutManager layoutManager = new GridLayoutManager(this, calculateNoOfColumns(this));
            movieGrid.setLayoutManager(layoutManager);
            movieGrid.addOnScrollListener(new RecyclerView.OnScrollListener() {
                /** https://stackoverflow.com/a/26561717 **/
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    if (dy > 0) //check for scroll down
                    {
                        visibleItemCount = layoutManager.getChildCount();
                        totalItemCount = layoutManager.getItemCount();
                        pastVisiblesItems = layoutManager.findFirstVisibleItemPosition();

                        if (!presenter.isLoading() && (visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                                System.out.println("will fetch movie list");
                                presenter.fetchMovieList(presenter.getCategorie());
                        }
                    }
                }

            });
            adapter = new

                    MovieGridAdapter(this);
            movieGrid.setAdapter(adapter);


            setTitle(categories[presenter.getCategorie()]);

            if (movieList != null)

            {
                populateList(movieList);
            } else

            {
                showLoading();
                presenter.fetchMovieList(-1);
            }
        } catch (NullAPIException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    protected void onDestroy() {
        presenter.onDetach();
        super.onDestroy();
    }

    @Override
    public void populateList(ArrayList<MovieDetail> movieDetails) {
        adapter.populateList(movieDetails);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void flushList() {
        adapter.resetList();
    }

    /**
     * https://stackoverflow.com/a/44764780
     */

    public static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int scalingFactor = 200;
        int columnCount = (int) (dpWidth / scalingFactor);
        return (columnCount >= 2 ? columnCount : 2);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.MovieFilter:
                final AlertDialog.Builder build = new AlertDialog.Builder(this);
                build.setTitle(getResources().getString(R.string.select_categorie));
                build.setCancelable(true);
                build.setSingleChoiceItems(categories, presenter.getCategorie(), (dialog, which) -> tempCat = which);
                build.setPositiveButton(getResources().getString(R.string.dialog_categorie_positive), (dialog, which) -> {
                    if (tempCat != presenter.getCategorie()) {
                        presenter.fetchMovieList(tempCat);
                        setTitle(categories[tempCat]);
                    }
                });
                build.show();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (adapter != null) {
            outState.putParcelableArrayList(MOVIE_LIST, adapter.getList());
            outState.putInt(CATEGORIE, presenter.getCategorie());
            outState.putInt(MAX_PAGE, presenter.getMaxPage());
            outState.putInt(CURRENT_PAGE, presenter.getCurrentPage());
        }
        super.onSaveInstanceState(outState);
    }
}

class MovieGridAdapter extends RecyclerView.Adapter<MovieGridAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<MovieDetail> movieList;

    MovieGridAdapter(Context c) {
        mContext = c;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.movie_grid_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(movieList.get(position));
    }

    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return (movieList != null) ? movieList.size() : 0;
    }

    void populateList(ArrayList<MovieDetail> movieDetails) {
        if (movieList != null)
            movieList.addAll(movieDetails);
        else
            movieList = movieDetails;
    }

    void resetList() {
        movieList = new ArrayList<>();
        notifyDataSetChanged();
    }

    ArrayList<MovieDetail> getList() {
        return movieList;
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

        void bind(MovieDetail movie) {
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