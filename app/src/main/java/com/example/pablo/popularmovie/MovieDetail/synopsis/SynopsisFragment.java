package com.example.pablo.popularmovie.MovieDetail.synopsis;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pablo.popularmovie.BuildConfig;
import com.example.pablo.popularmovie.MainActivity;
import com.example.pablo.popularmovie.R;
import com.example.pablo.popularmovie.bases.BaseFragment;
import com.example.pablo.popularmovie.data.models.MovieDetail;
import com.example.pablo.popularmovie.utilities.PaletteTransformation;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SynopsisFragment extends BaseFragment {


    @BindView(R.id.textView_original_title)
    protected TextView originaltitle;
    @BindView(R.id.textView_title)
    protected TextView title;
    @BindView(R.id.synopsis_extView)
    protected TextView synopsis;
    @BindView(R.id.imageView_backrgound_poster)
    protected ImageView background;
    @BindView(R.id.imageView_movie_preview)
    protected ImageView preview;
    @BindView(R.id.cardView_movie_detail)
    protected CardView cardView;
    @BindView(R.id.textView_rating_movieDetail)
    protected TextView rating;
    @BindView(R.id.textView_movieDetail_releaseDate)
    protected TextView release;
    @BindView(R.id.synopsis_background)
    ConstraintLayout backgroundLayout;

    private OnFragmentInteractionListener mListener;
    private MovieDetail movie;

    public SynopsisFragment() {
        // Required empty public constructor
    }

    public static SynopsisFragment newInstance(@NonNull MovieDetail movieDetail) {
        SynopsisFragment fragment = new SynopsisFragment();
        Bundle args = new Bundle();
        args.putParcelable(MainActivity.MOVIE_DETAIL, movieDetail);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            movie = getArguments().getParcelable(MainActivity.MOVIE_DETAIL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_movie_detail, container, false);
        setUnBinder(ButterKnife.bind(this, view));
        setUpView();
        return view;
    }

    private void setUpView() {
//        setTitle(movie.getOriginalTitle());
        synopsis.setText(movie.getOverview());
        title.setText(movie.getTitle());
        originaltitle.setText(String.format(getString(R.string.movie_detail_original_title), movie.getOriginalTitle()));
        String previewLink =  movie.getBackdropPath();
//            upNavigation.setOnClickListener(view -> NavUtils.navigateUpFromSameTask(MovieDetailActivity.this));

        if (previewLink != null && !previewLink.isEmpty()) {
            Picasso.with(getActivity())
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
        Picasso.with(getActivity())
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
                        Context context = SynopsisFragment.this.getActivity();

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
                        rating.setBackgroundColor(bgColor);
                        cardView.setCardBackgroundColor(bgColor);
                        title.setTextColor(textColor);
                        originaltitle.setTextColor(textColor);
                        synopsis.setTextColor(textColor);
                        release.setTextColor(textColor);

                        mListener.onBackgroundLoaded(bgColor, textColor);
                    }
                });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public interface OnFragmentInteractionListener extends BaseFragment.OnFragmentInteractionListener{
        void onBackgroundLoaded(int bgColor, int textColor);
    }
}
