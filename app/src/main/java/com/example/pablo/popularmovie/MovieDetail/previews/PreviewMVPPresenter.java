package com.example.pablo.popularmovie.MovieDetail.previews;

import com.example.pablo.popularmovie.bases.IMVPPresenter;
import com.example.pablo.popularmovie.bases.IMVPView;

/**
 * Created by pablo on 15/03/2018.
 */

public interface PreviewMVPPresenter<E extends IMVPView> extends IMVPPresenter<E> {

    void fetchVideoPreviews(int id);
}
