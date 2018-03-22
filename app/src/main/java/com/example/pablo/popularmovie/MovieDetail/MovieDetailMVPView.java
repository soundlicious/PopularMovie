package com.example.pablo.popularmovie.MovieDetail;

import com.example.pablo.popularmovie.bases.IMVPView;

/**
 * Created by pablo on 07/03/2018.
 */

interface MovieDetailMVPView extends IMVPView {
    void setStarIcon(Boolean isBookmarked);
    void setActivityResult();
}
