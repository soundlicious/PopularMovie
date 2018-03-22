package com.example.pablo.popularmovie.MovieDetail;

import com.example.pablo.popularmovie.bases.BasePresenter;
import com.example.pablo.popularmovie.data.db.ContentProviderManager;
import com.example.pablo.popularmovie.data.db.IProviderManager;
import com.example.pablo.popularmovie.data.models.MovieDetail;

/**
 * Created by pablo on 13/03/2018.
 */

class MovieDetailPresenter<E extends MovieDetailMVPView> extends BasePresenter<E> implements MovieDetailMVPPresenter<E>{

    private final IProviderManager providerManager;
    private boolean isBookMarked;

    public MovieDetailPresenter(String apiKey, ContentProviderManager cPManager, MovieDetail movieDetail) throws NullAPIException {
        super(apiKey);
        this.providerManager = cPManager;
        isBookMarked = (cPManager.queryMovie(movieDetail.getId()) != null);
        movieDetail.setBookmarked(isBookMarked);
    }

    @Override
    public boolean bookmarkMovie() {
        return isBookMarked = !isBookMarked;
    }

    @Override
    public boolean isBoormaked() {
        return isBookMarked;
    }

    @Override
    public void updateBookmark(MovieDetail movieDetail) {
        if (movieDetail.isBookmarked() && !isBookMarked) {
            providerManager.deleteMovie(movieDetail.getId());
            movieDetail.setBookmarked(false);
            getMvpView().setActivityResult();
        }
        else if (!movieDetail.isBookmarked() && isBookMarked) {
            providerManager.bookmarkMovie(movieDetail);
            movieDetail.setBookmarked(true);
        }
    }

    @Override
    public MovieDetail getBookmark(int id) {
        return providerManager.queryMovie(id);
    }

    @Override
    public void onAttach(E mvpView) {
        super.onAttach(mvpView);
    }
}
