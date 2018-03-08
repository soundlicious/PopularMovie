package com.example.pablo.popularmovie1.MovieDetail;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pablo.popularmovie1.BuildConfig;
import com.example.pablo.popularmovie1.MainActivity;
import com.example.pablo.popularmovie1.R;
import com.example.pablo.popularmovie1.bases.BaseActivity;
import com.example.pablo.popularmovie1.data.models.MovieDetail;
import com.example.pablo.popularmovie1.utilities.PaletteTransformation;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieDetailActivity extends BaseActivity implements MovieDetailMVPView {

    @BindView(R.id.textView_original_title)
    TextView originaltitle;
    @BindView(R.id.textView_title)
    TextView title;
    @BindView(R.id.synopsis_extView)
    TextView synopsis;
    @BindView(R.id.imageView_backrgound_poster)
    ImageView background;
    @BindView(R.id.imageView_movie_preview)
    ImageView preview;
    @BindView(R.id.cardView_movie_detail)
    CardView cardView;
    @BindView(R.id.imageView_upNavigation)
    ImageView upNavigation;
    @BindView(R.id.textView_rating_movieDetail)
    TextView rating;
    @BindView(R.id.textView_movieDetail_releaseDate)
    TextView release;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(MainActivity.MOVIE_DETAIL)) {
            MovieDetail movie = intent.getParcelableExtra(MainActivity.MOVIE_DETAIL);
            setTitle(movie.getOriginalTitle());
            synopsis.setText(movie.getOverview());
            title.setText(movie.getTitle());
            originaltitle.setText(String.format(getString(R.string.movie_detail_original_title), movie.getOriginalTitle()));
            String previewLink =  movie.getBackdropPath();
            upNavigation.setOnClickListener(view -> NavUtils.navigateUpFromSameTask(MovieDetailActivity.this));

            if (previewLink != null && !previewLink.isEmpty()) {
                Picasso.with(this)
                        .load(BuildConfig.MOVIEDBIMAGE_ENDPOINT + previewLink)
                        .centerCrop()
                        .placeholder(R.mipmap.ic_launcher_foreground)
                        .error(R.mipmap.ic_launcher_foreground)
                        .fit()
                        .centerCrop()
                        .into(preview);
            } else {
                preview.setVisibility(View.GONE);
            }
            rating.setText(String.format(getString(R.string.rating_format), movie.getVoteAverage()));
            release.setText(String.format(getString(R.string.release_date_format), movie.getReleaseDate()));
            Picasso.with(this)
                    .load(BuildConfig.MOVIEDBIMAGE_ENDPOINT + movie.getPosterPath())
                    .centerCrop()
                    .placeholder(R.color.white)
                    .error(R.color.white)
                    .fit()
                    .centerCrop()
                    .transform (PaletteTransformation.instance())
                    .into(background, new PaletteTransformation.Callback() {
                        @Override
                        public void onPalette() {
                            Bitmap bitmap = ((BitmapDrawable) background.getDrawable()).getBitmap();
                            Palette palette = PaletteTransformation.getPalette(bitmap);
                            MovieDetailActivity context = MovieDetailActivity.this;

                            int defaultColorLayout;
                            int defaultColorText;
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                defaultColorLayout = context.getColor(R.color.colorAccent);
                                defaultColorText = context.getColor(R.color.white);
                            } else {
                                defaultColorLayout = context.getResources().getColor(R.color.colorAccent);
                                defaultColorText = context.getResources().getColor(R.color.white);
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
                            cardView.setCardBackgroundColor(bgColor);
                            title.setTextColor(textColor);
                            originaltitle.setTextColor(textColor);
                            synopsis.setTextColor(textColor);
                            release.setTextColor(textColor);
                        }
                    });
        } else {
            Toast.makeText(this, getString(R.string.error_loading_movie_detail), Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
