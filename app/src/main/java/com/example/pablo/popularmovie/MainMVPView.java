package com.example.pablo.popularmovie;

import com.example.pablo.popularmovie.bases.IMVPView;
import com.example.pablo.popularmovie.data.models.MovieDetail;

import java.util.ArrayList;

/**
 * Created by pablo on 02/03/2018.
 */

public interface MainMVPView extends IMVPView {
    void populateList(ArrayList<MovieDetail> movieDetails);
    void flushList();
    ArrayList<MovieDetail> getMovieList();
}
