package com.example.pablo.popularmovie.data.network;

import com.example.pablo.popularmovie.BuildConfig;
import com.example.pablo.popularmovie.data.models.MovieDetail;
import com.example.pablo.popularmovie.data.models.Page;
import com.example.pablo.popularmovie.data.models.ReviewDetail;
import com.example.pablo.popularmovie.data.models.VideoDetail;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by pablo on 02/03/2018.
 */

public interface TheMovieDBAPIHelper {

    String API_URL = BuildConfig.MOVIEDBAPI_ENDPOINT;

    @GET("popular")
    Single<Page<MovieDetail>> getPopularMovieList(@Query("api_key") String key, @Query("page") int page, @Query("language") String language);
    @GET("top_rated")
    Single<Page<MovieDetail>> getTopRatedMovieList(@Query("api_key") String key, @Query("page") int page, @Query("language") String language);
    @GET("{id}/videos")
    Single<Page<VideoDetail>> getPreviewsList( @Path("id") int id, @Query("api_key") String key, @Query("page") int page, @Query("language") String language);
    @GET("{id}/reviews")
    Single<Page<ReviewDetail>> getReviewsList( @Path("id") double id, @Query("api_key") String key, @Query("page") int page, @Query("language") String language);
}
