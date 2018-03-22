package com.example.pablo.popularmovie.MovieDetail.previews;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.example.pablo.popularmovie.data.models.YoutubePreview;
import com.example.pablo.popularmovie.utilities.PaletteTransformation;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by pablo on 13/03/2018.
 */

public class PreviewsFragment extends BaseFragment implements PreviewMVPView, PreviewMVPNavigator, ReviewAdapter.ListItemClickListener {

    @BindView(R.id.fragment_previews_recyclerView) @Nullable
    RecyclerView recyclerView;
    @BindView(R.id.imageView_preview_background)
    ImageView background;

    private OnFragmentInteractionListener mListener;
    private MovieDetail movie;
    private ReviewAdapter adapter;
    private PreviewPresenter presenter;

    public static PreviewsFragment newInstance(@NonNull MovieDetail movieDetail) {
        PreviewsFragment fragment = new PreviewsFragment();
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
        adapter = new ReviewAdapter(getContext(), this);
        recyclerView.setAdapter(adapter);
        String apiKey = BuildConfig.THEMOVIEDB_API;
        String language = Locale.getDefault().toString().replace("_", "-");
        try {
            presenter = new PreviewPresenter.Builder()
                    .setApiKey(apiKey)
                    .setLanguage(language)
                    .setId(movie.getId())
                    .build();
            presenter.onAttach(this);

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
                            Context context = PreviewsFragment.this.getActivity();

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

        }
        //setAdapter
        //SetLayoutManager
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BaseFragment.OnFragmentInteractionListener) {
            mListener = (PreviewsFragment.OnFragmentInteractionListener) context;
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


    @Override
    public void watchYoutubeVideo(String id) {
        {
//            https://stackoverflow.com/a/12439378
            Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
            Intent webIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://www.youtube.com/watch?v=" + id));
            try {
                startActivity(appIntent);
            } catch (ActivityNotFoundException ex) {
                startActivity(webIntent);
            }
        }
    }


    @Override
    public void populateList(ArrayList<YoutubePreview> previews) {
        adapter.updateList(previews);
    }

    @Override
    public void onItemClick(String id) {
        watchYoutubeVideo(id);
    }

    public interface OnFragmentInteractionListener extends BaseFragment.OnFragmentInteractionListener{

    }


}

class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {

    private final Context context;
    ArrayList<YoutubePreview> previews;
    private int bgColor = -1;
    private int textColor = -1;
    final private ListItemClickListener OnClickListener;

    public ReviewAdapter(Context context, PreviewsFragment previewsFragment) {
        this.context = context;
        OnClickListener = previewsFragment;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_preview, parent, false);
        bgColor = (bgColor != -1)? bgColor:context.getResources().getColor(R.color.colorPrimary);
        textColor = (textColor != -1)? textColor:context.getResources().getColor(R.color.colorAccent);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(previews.get(position), bgColor, textColor);
    }

    @Override
    public int getItemCount() {
        return (previews != null)? previews.size():0;
    }

    public interface ListItemClickListener {
        void onItemClick(String id);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.imageView_preview_thumbmail)
        ImageView previewIMG;
        @BindView(R.id.textView_preview_title)
        TextView title;
        @BindView(R.id.cardView_preview_packground)
        CardView background;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        public void bind(YoutubePreview preview, int bgColor, int textColor) {
            title.setText(preview.getTitle());
            title.setTextColor(textColor);
            background.setCardBackgroundColor(bgColor);
            Picasso.with(context)
                    .load(preview.getThumbnailUrl())
                    .centerCrop()
                    .placeholder(R.mipmap.ic_launcher_foreground)
                    .error(R.mipmap.ic_launcher_foreground)
                    .fit()
                    .centerCrop()
                    .into(previewIMG);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            OnClickListener.onItemClick(previews.get(position).getId());
        }
    }

    public void updateList(ArrayList<YoutubePreview> previews){
        this.previews = previews;
        notifyDataSetChanged();
    }

    public void updateColor(int bgColor, int textColor){
        this.bgColor = bgColor;
        this.textColor = textColor;
        notifyDataSetChanged();
    }
}