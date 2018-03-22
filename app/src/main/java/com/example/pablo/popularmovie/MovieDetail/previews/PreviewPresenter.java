package com.example.pablo.popularmovie.MovieDetail.previews;

import android.util.Log;

import com.example.pablo.popularmovie.bases.BasePresenter;
import com.example.pablo.popularmovie.data.models.Page;
import com.example.pablo.popularmovie.data.models.VideoDetail;
import com.example.pablo.popularmovie.data.models.YoutubePreview;
import com.example.pablo.popularmovie.data.network.TheMovieDBAPIHelper;
import com.example.pablo.popularmovie.data.network.YoutubeAPIHelper;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by pablo on 15/03/2018.
 */

public class PreviewPresenter<E extends PreviewMVPView> extends BasePresenter<E> implements PreviewMVPPresenter<E> {

    public static final String TAG = PreviewPresenter.class.getSimpleName();
    private final TheMovieDBAPIHelper apiHelper;
    private final String apiKey;
    private final String language;
    private int currentPage;
    private int maxPage;
    private boolean isLoading;
    private final int id;
    private E view;

    private PreviewPresenter(String apiKey, String language, int currentPage, int maxPage, boolean isLoading, int id) throws NullAPIException {
        super(apiKey);
        this.apiHelper = getDataManager().getMovieDBAPIHelper();
        this.apiKey = apiKey;
        this.language = (language != null && !language.isEmpty()) ? language : "en-US";
        this.currentPage = currentPage;
        this.maxPage = maxPage;
        this.isLoading = isLoading;
        this.id = id;
    }

    @Override
    public void onAttach(E mvpView) {
        super.onAttach(mvpView);
        fetchVideoPreviews(id);
    }

    @Override
    public void onDetach() {
        this.view = null;
    }

    @Override
    public boolean isViewAttached() {
        return view != null;
    }

    @Override
    public void fetchVideoPreviews(int id) {
        if (currentPage <= maxPage) {
            System.out.println("Going to fetch TopRated");

            isLoading = true;
            apiHelper.getPreviewsList(id, apiKey, currentPage, language)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::fetchYoutubePreviews, throwable -> System.out.println(throwable.getMessage()));
        }
    }

    private void fetchYoutubePreviews(Page<VideoDetail> page) {
        List<VideoDetail> previews = page.getMovieDetails();
        ArrayList<Single<YoutubePreview>> obs = new ArrayList<>();

        for (VideoDetail preview : previews) {
            Log.i(TAG, "fetchYoutubePreviews - youtube ID : " + preview.getKey());
            obs.add(getYoutubePreview(YoutubeAPIHelper.PREVIEW_ENDPOINT + preview.getKey(), YoutubeAPIHelper.FORMAT));
        }
        Single.zip(obs, objects -> {
            ArrayList<YoutubePreview> previewList = new ArrayList<>();
            for (Object object : objects) {
                YoutubePreview preview = (YoutubePreview) object;
                if (!preview.isEmpty())
                    previewList.add(preview);
            }
            return previewList;
        }).subscribe(youtubePreviews -> {
            for (VideoDetail video : previews){
                for (YoutubePreview youtubePreview: youtubePreviews){
                    if (youtubePreview.getHtml().contains(video.getKey())){
                        youtubePreview.setId(video.getKey());
                        break;
                    }
                }
            }
            getMvpView().populateList(youtubePreviews);
            getMvpView().hideLoading();

            if (currentPage == 1)
                maxPage = page.getTotalPages();
            currentPage++;
            isLoading = false;
        }, throwable -> Log.i(TAG, "fetchYoutubePreviews : " + throwable.getMessage()));
    }

    private Single<YoutubePreview> getYoutubePreview(String url, String format) {
        YoutubeAPIHelper youtubeAPI = getDataManager().getYoutubeAPIHelper();

        return youtubeAPI.getYoutubePreview(url, format)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(throwable -> {
                    YoutubePreview empty = new YoutubePreview();
                    empty.setEmpty(true);
                    Log.i(TAG, "getYoutubePreview - empty preview : " + throwable.getMessage());
                    return Single.just(empty);
                });
    }

    static class Builder {
        private int currentPage = 1;
        private int maxPage = 1000;
        private boolean isLoading = false;
        private String language;
        private String apiKey;
        private int id;

        public PreviewPresenter build() throws NullAPIException {
            return new PreviewPresenter(apiKey, language, currentPage, maxPage, isLoading, id);
        }


        public PreviewPresenter.Builder setCurrentPage(int currentPage) {
            this.currentPage = currentPage;
            return this;
        }

        public PreviewPresenter.Builder setMaxPage(int maxPage) {
            this.maxPage = maxPage;
            return this;
        }

        public PreviewPresenter.Builder setLoading(boolean loading) {
            isLoading = loading;
            return this;
        }

        public PreviewPresenter.Builder setLanguage(String language) {
            this.language = language;
            return this;
        }

        public PreviewPresenter.Builder setApiKey(String apiKey) {
            this.apiKey = apiKey;
            return this;
        }

        public PreviewPresenter.Builder setId(Integer id) {
            this.id = id;
            return this;
        }

    }
}
