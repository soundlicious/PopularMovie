package com.example.pablo.popularmovie1.data;

import com.example.pablo.popularmovie1.data.network.APIHelperFactory;
import com.example.pablo.popularmovie1.data.network.TheMovieDBAPIHelper;

/**
 * Created by pablo on 02/03/2018.
 */

/**
 * Thread Safe Singleton, made on the following Model : https://stackoverflow.com/questions/16106260/thread-safe-singleton-class
 */
public class DataManager {

    private TheMovieDBAPIHelper movieDBAPIHelper;

    private static class Holder {
        private static final DataManager INSTANCE = new DataManager();
    }

    public static DataManager getInstance() {
        return Holder.INSTANCE;
    }

    private DataManager() {
        this.movieDBAPIHelper = APIHelperFactory.createRetrofitService(TheMovieDBAPIHelper.class, TheMovieDBAPIHelper.API_URL);
    }

    public TheMovieDBAPIHelper getMovieDBAPIHelper() {
        return movieDBAPIHelper;
    }
}