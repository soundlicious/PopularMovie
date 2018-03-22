package com.example.pablo.popularmovie.MovieDetail.comments;

import com.example.pablo.popularmovie.bases.BasePresenter;
import com.example.pablo.popularmovie.bases.IMVPView;

/**
 * Created by pablo on 18/03/2018.
 */

class CommentsMVPPresenter<E extends IMVPView> extends BasePresenter<E> {

    public CommentsMVPPresenter(String apiKey) throws NullAPIException {
        super(apiKey);
    }

    static class Builder {
        private int currentPage = 1;
        private int maxPage = 1000;
        private boolean isLoading = false;
        private String language;
        private String apiKey;
        private int id;

        public CommentsPresenter build() throws NullAPIException {
            return new CommentsPresenter(apiKey, language, currentPage, maxPage, isLoading, id);
        }


        public CommentsPresenter.Builder setCurrentPage(int currentPage) {
            this.currentPage = currentPage;
            return this;
        }

        public CommentsPresenter.Builder setMaxPage(int maxPage) {
            this.maxPage = maxPage;
            return this;
        }

        public CommentsPresenter.Builder setLoading(boolean loading) {
            isLoading = loading;
            return this;
        }

        public CommentsPresenter.Builder setLanguage(String language) {
            this.language = language;
            return this;
        }

        public CommentsPresenter.Builder setApiKey(String apiKey) {
            this.apiKey = apiKey;
            return this;
        }

        public CommentsPresenter.Builder setId(Integer id) {
            this.id = id;
            return this;
        }

    }
}
