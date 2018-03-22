package com.example.pablo.popularmovie.MovieDetail.comments;

import com.example.pablo.popularmovie.bases.IMVPView;
import com.example.pablo.popularmovie.data.models.ReviewDetail;

import java.util.ArrayList;

/**
 * Created by pablo on 18/03/2018.
 */

interface CommentsMVPView extends IMVPView {
    void populateList(ArrayList<ReviewDetail> reviewDetails);
}
