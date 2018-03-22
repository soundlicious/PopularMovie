package com.example.pablo.popularmovie;

import android.os.Build;
import android.util.Log;

import com.example.pablo.popularmovie.bases.BasePresenter;
import com.example.pablo.popularmovie.data.db.ContentProviderManager;
import com.example.pablo.popularmovie.data.models.MovieDetail;
import com.example.pablo.popularmovie.data.network.TheMovieDBAPIHelper;

import java.util.ArrayList;
import java.util.function.Predicate;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by pablo on 02/03/2018.
 */

public class MainPresenter<E extends MainMVPView> extends BasePresenter<E> implements MainMVPPresenter<E> {

    private static final String TAG = MainPresenter.class.getSimpleName();

    private final TheMovieDBAPIHelper apiHelper;
    private final String apiKey;
    private final ContentProviderManager cpManager;
    private int currentPage;
    private int maxPage;
    private boolean isLoading = false;
    private String language;
    private int categorie;
    private ArrayList movies;

    private MainPresenter(String apiKey, String language, int currentPage, int maxPage, boolean isLoading, int categorie, ContentProviderManager cpManager) throws NullAPIException {
        super(apiKey);
        this.apiHelper = getDataManager().getMovieDBAPIHelper();
        this.apiKey = apiKey;
        this.language = (language != null && !language.isEmpty()) ? language : "en-US";
        this.currentPage = currentPage;
        this.maxPage = maxPage;
        this.isLoading = isLoading;
        this.categorie = categorie;
        this.cpManager = cpManager;
    }

    @Override
    public void fetchPopularMovie() {
        if (currentPage <= maxPage) {
            isLoading = true;
            apiHelper.getPopularMovieList(apiKey, currentPage, language)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(page -> {
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

            isLoading = true;
            apiHelper.getTopRatedMovieList(apiKey, currentPage, language)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(page -> {
                        movies = new ArrayList<>(page.getMovieDetails());
                        getMvpView().populateList(movies);
                        getMvpView().hideLoading();
                        if (currentPage == 1)
                            maxPage = page.getTotalPages();
                        currentPage++;
                        isLoading = false;
                    }, throwable -> System.out.println(throwable.getMessage()));
        }
    }

    @Override
    public void getBookmarkedMovie() {
        getMvpView().populateList(cpManager.queryMovies());
        getMvpView().hideLoading();
    }

    @Override
    public void deleteMovieFromList(MovieDetail movie) {
        if (movie != null && categorie == 2) {
            ArrayList<MovieDetail> list = getMvpView().getMovieList();
            if (list != null ) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    list.removeIf(movieDetail -> movieDetail.getId().equals(movie.getId()));
                } else {
                    for (MovieDetail movieDetail : list ){
                        if (movieDetail.getId().equals(movie.getId())) {
                            list.remove(movieDetail);
                            break;
                        }
                    }
                }
                getMvpView().flushList();
                getMvpView().populateList(list);
            }
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
            case 2:
                getBookmarkedMovie();
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
        private ContentProviderManager cpManager;

        public MainPresenter build() throws NullAPIException {
            return new MainPresenter(apiKey, language, currentPage, maxPage, isLoading, categorie, cpManager);
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

        public Builder setCpManager(ContentProviderManager cpManager) {
            this.cpManager = cpManager;
            return this;
        }

    }

}





