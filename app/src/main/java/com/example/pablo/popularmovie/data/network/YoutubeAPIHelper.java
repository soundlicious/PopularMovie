package com.example.pablo.popularmovie.data.network;

import com.example.pablo.popularmovie.BuildConfig;
import com.example.pablo.popularmovie.data.models.YoutubePreview;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by pablo on 16/03/2018.
 */

public interface YoutubeAPIHelper {
    String API_URL = BuildConfig.YOUTUBE_ENDPOINT;
    String FORMAT = "json";
    String PREVIEW_ENDPOINT = API_URL + "watch?v=";

    @GET("oembed")
    Single<YoutubePreview> getYoutubePreview(@Query("url") String url, @Query("format") String format);
}
