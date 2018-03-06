package com.example.pablo.popularmovie1;

import com.example.pablo.popularmovie1.bases.IMVPPresenter;
import com.example.pablo.popularmovie1.bases.IMVPView;

/**
 * Created by pablo on 02/03/2018.
 */

public interface MainMVPPresenter<E extends IMVPView> extends IMVPPresenter<E>{

    void fetchPopularMovie();
    void fetchTopRatedMovie();
    void reset();
}
