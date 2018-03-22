package com.example.pablo.popularmovie.MovieDetail.comments;

import android.util.Log;

import com.example.pablo.popularmovie.data.network.TheMovieDBAPIHelper;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by pablo on 18/03/2018.
 */

class CommentsPresenter<E extends CommentsMVPView> extends CommentsMVPPresenter<E> {
    private final TheMovieDBAPIHelper apiHelper;
    private final String TAG = CommentsPresenter.class.getSimpleName();
    private final String apiKey;
    private final String language;
    private int currentPage;
    private int maxPage;
    private boolean isLoading;
    private final int id;

    public CommentsPresenter(String apiKey, String language, int currentPage, int maxPage, boolean isLoading, int id) throws NullAPIException {
        super(apiKey);
        this.apiHelper = getDataManager().getMovieDBAPIHelper();
        this.apiKey = apiKey;
        this.language = (language != null && !language.isEmpty()) ? language : "en-US";
        this.currentPage = currentPage;
        this.maxPage = maxPage;
        this.isLoading = isLoading;
        this.id = id;
    }

    public void fetchReviews() {
        apiHelper.getReviewsList(id, apiKey, currentPage, language)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(reviewDetailPage -> {
                    getMvpView().populateList(new ArrayList<>(reviewDetailPage.getMovieDetails()));
                }, throwable -> Log.e(TAG, throwable.getMessage()));
    }
}
