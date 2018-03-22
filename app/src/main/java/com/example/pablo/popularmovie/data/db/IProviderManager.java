package com.example.pablo.popularmovie.data.db;

import com.example.pablo.popularmovie.data.models.MovieDetail;

import java.util.ArrayList;

/**
 * Created by pablo on 19/03/2018.
 */

public interface IProviderManager {
    void bookmarkMovie(MovieDetail movieDetail);
    MovieDetail queryMovie(int id);
    ArrayList<MovieDetail> queryMovies();
    void deleteMovie(int id);
}
