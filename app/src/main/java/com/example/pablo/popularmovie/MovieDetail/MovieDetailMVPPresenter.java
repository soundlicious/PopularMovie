package com.example.pablo.popularmovie.MovieDetail;

import com.example.pablo.popularmovie.bases.IMVPPresenter;
import com.example.pablo.popularmovie.bases.IMVPView;
import com.example.pablo.popularmovie.data.models.MovieDetail;

/**
 * Created by pablo on 13/03/2018.
 */

interface MovieDetailMVPPresenter<E extends IMVPView> extends IMVPPresenter<E>{
    public boolean bookmarkMovie();
    public boolean isBoormaked();
    public void updateBookmark(MovieDetail movieDetail);
    public MovieDetail getBookmark(int id);
}
