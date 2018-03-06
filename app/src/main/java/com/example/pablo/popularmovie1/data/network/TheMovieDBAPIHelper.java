package com.example.pablo.popularmovie1.data.network;

import com.example.pablo.popularmovie1.BuildConfig;
import com.example.pablo.popularmovie1.data.models.Page;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by pablo on 02/03/2018.
 */

public interface TheMovieDBAPIHelper {

    String API_URL = BuildConfig.MOVIEDBAPI_ENDPOINT;

    @GET("popular")
    Single<Page> getPopularMovieList(@Query("api_key") String key, @Query("page") int page, @Query("language") String language);
    @GET("top_rated")
    Single<Page> getTopRatedMovieList(@Query("api_key") String key, @Query("page") int page, @Query("language") String language);
}
