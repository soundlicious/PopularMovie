package com.example.pablo.popularmovie1.data.network;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * Created by pablo on 02/03/2018.
 */

/**
 * Factory Class for creating the good API Helper.
 */
public class APIHelperFactory {

    /**
     * Create the retrofit service used to make network call
     * @param mClass Class of the API Helper
     * @param endPoint String Url of the API Helper
     * @param <T> Api Helper Object
     * @return
     */
    public static <T> T createRetrofitService(final Class<T> mClass, final String endPoint){
        final Retrofit restAdapter = new Retrofit.Builder()
                .baseUrl(endPoint)
                .addConverterFactory(JacksonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        return restAdapter.create(mClass);
    }
}
