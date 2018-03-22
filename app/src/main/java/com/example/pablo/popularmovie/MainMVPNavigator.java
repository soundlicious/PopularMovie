package com.example.pablo.popularmovie;

import android.widget.ImageView;

import com.example.pablo.popularmovie.data.models.MovieDetail;

/**
 * Created by pablo on 02/03/2018.
 */

public interface MainMVPNavigator {

    void openMovieDetail(MovieDetail movie, ImageView view);
}
