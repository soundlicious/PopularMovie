package com.example.pablo.popularmovie.MovieDetail;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;

import com.example.pablo.popularmovie.BuildConfig;
import com.example.pablo.popularmovie.MainActivity;
import com.example.pablo.popularmovie.MovieDetail.comments.CommentsFragment;
import com.example.pablo.popularmovie.MovieDetail.previews.PreviewsFragment;
import com.example.pablo.popularmovie.MovieDetail.synopsis.SynopsisFragment;
import com.example.pablo.popularmovie.R;
import com.example.pablo.popularmovie.bases.BaseActivity;
import com.example.pablo.popularmovie.bases.BasePresenter;
import com.example.pablo.popularmovie.data.db.ContentProviderManager;
import com.example.pablo.popularmovie.data.models.MovieDetail;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by pablo on 12/03/2018.
 */

public class MovieDetailActivity extends BaseActivity implements MovieDetailMVPView, SynopsisFragment.OnFragmentInteractionListener, PreviewsFragment.OnFragmentInteractionListener{

    public static final String MOVIE_ID = "movie_id";
    private static final String TAG = MovieDetail.class.getSimpleName();
    @BindView(R.id.viewPager_movieDetail) @Nullable
    ViewPager viewPager;
    @BindView(R.id.tabLayout_movieDetail) @Nullable
    public TabLayout tabLayout;
    private MovieDetailPresenter presenter;
    private Menu menu;
    private MovieDetail movie;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moviedetail);
        setUnBinder(ButterKnife.bind(this));
        String apiKey = BuildConfig.THEMOVIEDB_API;
        String language = Locale.getDefault().toString().replace("_", "-");
        String[] tabs = getResources().getStringArray(R.array.movie_filter_tab);
        Intent intent = getIntent();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
        if (intent != null && intent.hasExtra(MainActivity.MOVIE_DETAIL)) {
            movie = intent.getParcelableExtra(MainActivity.MOVIE_DETAIL);
            setTitle(movie.getTitle());
            try {
                ContentProviderManager cPManager = new ContentProviderManager(this);
                presenter = new MovieDetailPresenter(apiKey, cPManager, movie);
                viewPager.setAdapter(new MovieFragmentPagerAdapter(getSupportFragmentManager(), movie, tabs));
                tabLayout.setupWithViewPager(viewPager);
                presenter.onAttach(this);
            } catch (BasePresenter.NullAPIException e) {
                e.printStackTrace();
            }
        } else {
            finish();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackgroundLoaded(int bgColor, int textColor) {
        tabLayout.setBackgroundColor(bgColor);
        int[][] states = new int[][] {
                new int[] { android.R.attr.state_enabled}  // pressed
        };

        int[] colors = new int[] {Color.WHITE};
        ColorStateList colorStateList = new ColorStateList(states, colors);
        tabLayout.setTabTextColors(colorStateList);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setBackgroundDrawable(new ColorDrawable(bgColor));
        }
        Window window = getWindow();
        if (window != null) {
            window.setStatusBarColor(bgColor);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        this.menu = menu;
        inflater.inflate(R.menu.movie_detail_menu, menu);
        setStarIcon(presenter.isBoormaked());
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.i(TAG, "onOptionsItemSelected : " + item.getItemId() + "and Home : " + R.id.home);
        switch (item.getItemId()) {
            case R.id.menu_bookmark:
                setStarIcon(presenter.bookmarkMovie());
                return true;
            case R.id.home :
                presenter.updateBookmark(movie);
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }    }

    @Override
    public void onFragmentAttached() {

    }

    @Override
    public void onFragmentDetached(String tag) {

    }

    @Override
    public void onBackPressed() {
        presenter.updateBookmark(movie);
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        presenter.onDetach();
        super.onDestroy();
    }

    @Override
    public void setStarIcon(Boolean isBookmarked) {
        switch ((isBookmarked)?1:0) {
            case 1:
                menu.getItem(0).setIcon(getResources().getDrawable(R.drawable.ic_star_grey_24dp));
                break;
            case 0 :
                menu.getItem(0).setIcon(getResources().getDrawable(R.drawable.ic_star_border_grey_24dp));
                break;
            default:
        }
    }

    @Override
    public void setActivityResult() {
        Intent intent = new Intent();
        intent.putExtra(MOVIE_ID, movie);
        intent.setFlags(0);
        setResult(RESULT_OK, intent);
    }
}

class MovieFragmentPagerAdapter extends FragmentStatePagerAdapter {
    private final MovieDetail movie;
    private final String[] tabs;
    private int tabCount;

    public MovieFragmentPagerAdapter(FragmentManager fm, MovieDetail movieDetail, String[] tabs) {
        super(fm);
        tabCount = 3;
        this.movie = movieDetail;
        this.tabs = tabs;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position){
            case 0 :
                fragment = SynopsisFragment.newInstance(movie);
                break;
            case 1 :
                fragment = PreviewsFragment.newInstance(movie);

                break;
            case 2 :
                fragment = CommentsFragment.newInstance(movie);
                break;

            default:
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return tabCount;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabs[position];
    }
}
