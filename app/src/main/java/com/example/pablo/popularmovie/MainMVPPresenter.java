package com.example.pablo.popularmovie;

import com.example.pablo.popularmovie.bases.IMVPPresenter;
import com.example.pablo.popularmovie.bases.IMVPView;
import com.example.pablo.popularmovie.data.models.MovieDetail;

/**
 * Created by pablo on 02/03/2018.
 */

public interface MainMVPPresenter<E extends IMVPView> extends IMVPPresenter<E>{

    void fetchMovieList(int which);
    void fetchPopularMovie();
    void fetchTopRatedMovie();
    void getBookmarkedMovie();
    void deleteMovieFromList(MovieDetail movie);
    void reset();
}
