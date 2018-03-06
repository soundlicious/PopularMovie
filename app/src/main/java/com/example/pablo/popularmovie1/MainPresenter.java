package com.example.pablo.popularmovie1;

import com.example.pablo.popularmovie1.bases.BasePresenter;
import com.example.pablo.popularmovie1.data.network.TheMovieDBAPIHelper;
import com.example.pablo.popularmovie1.utilities.MainMVPView;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by pablo on 02/03/2018.
 */

public class MainPresenter<E extends MainMVPView> extends BasePresenter<E> implements MainMVPPresenter<E> {

    private final TheMovieDBAPIHelper apiHelper;
    private final String apiKey;
    private int currentPage = 1;
    private int maxPage = 1000;
    private boolean isLoading = false;
    private String language = "en-US";

    MainPresenter(String themoviedbApi, String language) {
        this.apiHelper = getDataManager().getMovieDBAPIHelper();
        this.apiKey = themoviedbApi;
        this.language = language;
    }

    @Override
    public void fetchPopularMovie(){
        if (currentPage <= maxPage) {
            isLoading = true;
            apiHelper.getPopularMovieList(apiKey, currentPage, language)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(page -> {
                        getMvpView().populateList(page.getMovieDetails());
                        getMvpView().hideLoading();
                        if (currentPage == 1)
                            maxPage = page.getTotalPages();
                            currentPage++;
                        isLoading = false;
                    }, throwable -> {
                        System.out.println(throwable.getMessage());
                    });
        }
    }

    @Override
    public void fetchTopRatedMovie(){
        apiHelper.getTopRatedMovieList(apiKey, currentPage, language)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(page -> {
                    getMvpView().populateList(page.getMovieDetails());
                    getMvpView().hideLoading();
                    if (currentPage == 1)
                        maxPage = page.getTotalPages();
                    currentPage++;
                    isLoading = false;
                }, throwable -> {
                    System.out.println(throwable.getMessage());
                });
    }

    @Override
    public void reset() {
        currentPage = 1;
        maxPage = 1000;
        isLoading = false;
        language = "en-US";
    }


    boolean isLoading() {
        return isLoading;
    }
}


