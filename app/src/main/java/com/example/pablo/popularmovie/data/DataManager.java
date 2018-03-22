package com.example.pablo.popularmovie.data;

import android.content.ContentResolver;
import android.content.Context;

import com.example.pablo.popularmovie.data.network.APIHelperFactory;
import com.example.pablo.popularmovie.data.network.TheMovieDBAPIHelper;
import com.example.pablo.popularmovie.data.network.YoutubeAPIHelper;

/**
 * Created by pablo on 02/03/2018.
 */

/**
 * Thread Safe Singleton, made on the following Model : https://stackoverflow.com/questions/16106260/thread-safe-singleton-class
 */
public class DataManager {

    private TheMovieDBAPIHelper movieDBAPIHelper;
    private YoutubeAPIHelper youtubeAPIHelper;

    private static class Holder {
        private static final DataManager INSTANCE = new DataManager();
    }

    public static DataManager getInstance() {
        return Holder.INSTANCE;
    }

    private DataManager() {
        this.movieDBAPIHelper = APIHelperFactory.createRetrofitService(TheMovieDBAPIHelper.class, TheMovieDBAPIHelper.API_URL);
        this.youtubeAPIHelper = APIHelperFactory.createRetrofitService(YoutubeAPIHelper.class, YoutubeAPIHelper.API_URL);
    }

    public TheMovieDBAPIHelper getMovieDBAPIHelper() {
        return movieDBAPIHelper;
    }

    public YoutubeAPIHelper getYoutubeAPIHelper() {
        return youtubeAPIHelper;
    }

    public ContentResolver getContentResolver(Context context){
        return context.getContentResolver();
    }
}