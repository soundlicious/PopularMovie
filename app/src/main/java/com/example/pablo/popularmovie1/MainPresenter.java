package com.example.pablo.popularmovie1;

import com.example.pablo.popularmovie1.bases.BasePresenter;
import com.example.pablo.popularmovie1.data.network.TheMovieDBAPIHelper;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by pablo on 02/03/2018.
 */

public class MainPresenter<E extends MainMVPView> extends BasePresenter<E> implements MainMVPPresenter<E> {

    private final TheMovieDBAPIHelper apiHelper;
    private final String apiKey;
    private int currentPage;
    private int maxPage;
    private boolean isLoading = false;
    private String language;
    private int categorie;

    private MainPresenter(String apiKey, String language, int currentPage, int maxPage, boolean isLoading, int categorie) throws NullAPIException {
        this.apiHelper = getDataManager().getMovieDBAPIHelper();
        if (apiKey == null)
            throw new NullAPIException("Null key Parameter");
        this.apiKey = apiKey;
        this.language = (language != null && !language.isEmpty()) ? language : "en-US";
        this.currentPage = currentPage;
        this.maxPage = maxPage;
        this.isLoading = isLoading;
        this.categorie = categorie;
    }

    @Override
    public void fetchPopularMovie() {
        if (currentPage <= maxPage) {
            System.out.println("Going to fetch Popular");
            isLoading = true;
            apiHelper.getPopularMovieList(apiKey, currentPage, language)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(page -> {
                        System.out.println("GOT POPULAR LIST");
                        getMvpView().populateList(new ArrayList<>(page.getMovieDetails()));
                        getMvpView().hideLoading();
                        if (currentPage == 1)
                            maxPage = page.getTotalPages();
                        currentPage++;
                        isLoading = false;
                    }, throwable -> System.out.println(throwable.getMessage()));
        }
    }

    @Override
    public void fetchTopRatedMovie() {
        if (currentPage <= maxPage) {
            System.out.println("Going to fetch TopRated");

            isLoading = true;
            apiHelper.getTopRatedMovieList(apiKey, currentPage, language)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(page -> {
                        System.out.println("GOT TOP RATED LIST");
                        getMvpView().populateList(new ArrayList<>(page.getMovieDetails()));
                        getMvpView().hideLoading();
                        if (currentPage == 1)
                            maxPage = page.getTotalPages();
                        currentPage++;
                        isLoading = false;
                    }, throwable -> System.out.println(throwable.getMessage()));
        }
    }

    @Override
    public void reset() {
        currentPage = 1;
        maxPage = 1000;
        isLoading = false;
        language = "en-US";
        getMvpView().flushList();
        getMvpView().showLoading();
    }


    public boolean isLoading() {
        return isLoading;
    }

    @Override
    public void fetchMovieList(int which) {
        if (which != categorie && which != -1) {
            System.out.println("reset presenter with categorie = " + categorie + " and selected cat = " + which);
            reset();
        }
        if (which != categorie) {
            categorie = (which == -1) ? 0 : which;
        }
        switch (categorie) {
            case 0:
                fetchPopularMovie();
                break;
            case 1:
                fetchTopRatedMovie();
                break;
            default:
        }
    }

    public int getCategorie() {
        return categorie;
    }

    public int getMaxPage() {
        return maxPage;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    static class Builder {
        private int currentPage = 1;
        private int maxPage = 1000;
        private boolean isLoading = false;
        private String language;
        private int categorie = 0;
        private String apiKey;

        public MainPresenter build() throws NullAPIException {
            return new MainPresenter(apiKey, language, currentPage, maxPage, isLoading, categorie);
        }


        public Builder setCurrentPage(int currentPage) {
            this.currentPage = currentPage;
            return this;
        }

        public Builder setMaxPage(int maxPage) {
            this.maxPage = maxPage;
            return this;
        }

        public Builder setLoading(boolean loading) {
            isLoading = loading;
            return this;
        }

        public Builder setLanguage(String language) {
            this.language = language;
            return this;
        }

        public Builder setCategorie(int categorie) {
            this.categorie = categorie;
            return this;
        }

        public Builder setApiKey(String apiKey) {
            this.apiKey = apiKey;
            return this;
        }

    }

}

class NullAPIException extends Exception {

    public NullAPIException() {
        super();
    }

    public NullAPIException(String message) {
        super(message);
    }

    public NullAPIException(String message, Throwable cause) {
        super(message, cause);
    }
}





