package com.example.pablo.popularmovie1.utilities;

import com.example.pablo.popularmovie1.bases.IMVPView;
import com.example.pablo.popularmovie1.data.models.MovieDetail;

import java.util.List;

/**
 * Created by pablo on 02/03/2018.
 */

public interface MainMVPView extends IMVPView {
    void populateList(List<MovieDetail> movieDetails);
}
