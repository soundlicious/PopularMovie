package com.example.pablo.popularmovie.MovieDetail.comments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pablo.popularmovie.BuildConfig;
import com.example.pablo.popularmovie.MainActivity;
import com.example.pablo.popularmovie.R;
import com.example.pablo.popularmovie.bases.BaseFragment;
import com.example.pablo.popularmovie.bases.BasePresenter;
import com.example.pablo.popularmovie.data.models.MovieDetail;
import com.example.pablo.popularmovie.data.models.ReviewDetail;
import com.example.pablo.popularmovie.utilities.PaletteTransformation;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.pablo.popularmovie.MovieDetail.comments.CommentsFragment.TAG;

/**
 * Created by pablo on 18/03/2018.
 */

public class CommentsFragment extends BaseFragment implements CommentsMVPView {
    public static String TAG = CommentsFragment.class.getSimpleName();

    @BindView(R.id.fragment_previews_recyclerView) @Nullable
    RecyclerView recyclerView;
    @BindView(R.id.imageView_preview_background)
    ImageView background;
    CommentsPresenter presenter;

    private MovieDetail movie;
    private CommentsAdapter adapter;

    public static CommentsFragment newInstance(@NonNull MovieDetail movieDetail) {
        CommentsFragment fragment = new CommentsFragment();
        Bundle args = new Bundle();
        args.putParcelable(MainActivity.MOVIE_DETAIL, movieDetail);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            movie = getArguments().getParcelable(MainActivity.MOVIE_DETAIL);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_previews, container, false);
        setUnBinder(ButterKnife.bind(this, view));
        setUpView();
        return view;
    }

    private void setUpView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new CommentsAdapter(getContext());
        recyclerView.setAdapter(adapter);
        String apiKey = BuildConfig.THEMOVIEDB_API;
        String language = Locale.getDefault().toString().replace("_", "-");
        try {
            presenter = new CommentsPresenter.Builder()
                    .setApiKey(apiKey)
                    .setLanguage(language)
                    .setId(movie.getId())
                    .build();
            presenter.onAttach(this);
            presenter.fetchReviews();

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
                            Context context = CommentsFragment.this.getActivity();

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
                            adapter.updateColor(bgColor, textColor);
                        }
                    });
        } catch (BasePresenter.NullAPIException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void populateList(ArrayList<ReviewDetail> reviewDetails) {
        adapter.updateList(reviewDetails);
    }
}

class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.ViewHolder> {
    private final Context context;
    private ArrayList<ReviewDetail> comments;
    private int bgColor;
    private int textColor;

    public CommentsAdapter(Context context) {
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_review, parent, false);
        bgColor = (bgColor != -1)? bgColor:context.getResources().getColor(R.color.colorPrimary);
        textColor = (textColor != -1)? textColor:context.getResources().getColor(R.color.colorAccent);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(comments.get(position), bgColor, textColor);
    }

    @Override
    public int getItemCount() {
        return (comments != null)? comments.size():0;
    }

    public void updateList(ArrayList<ReviewDetail> comments){
        this.comments = comments;
        notifyDataSetChanged();
    }

    public void updateColor(int bgColor, int textColor) {
        this.bgColor = bgColor;
        this.textColor = textColor;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.textView_review_author)
        TextView author;
        @BindView(R.id.textView_review_body)
        TextView reviewBody;
        @BindView(R.id.cardView_comments_background)
        CardView background;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(ReviewDetail reviewDetail, int bgColor, int textColor) {
            author.setText(reviewDetail.getAuthor());
            author.setTextColor(textColor);
            reviewBody.setText(reviewDetail.getContent());
            Log.i(TAG, "ViewHolder - Bind - review content : " + reviewDetail.getContent());
            reviewBody.setTextColor(textColor);
            background.setCardBackgroundColor(bgColor);
        }
    }
}
