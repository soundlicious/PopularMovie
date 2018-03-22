package com.example.pablo.popularmovie.bases;


/**
 * Created by pablo on 02/03/2018.
 */

public interface IMVPView {

    void showLoading();

    void hideLoading();

    void onError(int resId);

    void onError(String message);

    void showMessage(int resId);

    void showMessage(String message);

    boolean isNetworkOn();
}
