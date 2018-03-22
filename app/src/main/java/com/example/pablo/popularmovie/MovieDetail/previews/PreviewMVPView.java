package com.example.pablo.popularmovie.MovieDetail.previews;

import com.example.pablo.popularmovie.bases.IMVPView;
import com.example.pablo.popularmovie.data.models.YoutubePreview;

import java.util.ArrayList;
/**
 * Created by pablo on 13/03/2018.
 */

interface PreviewMVPView extends IMVPView{

    void populateList(ArrayList<YoutubePreview> movieDetails);
}
