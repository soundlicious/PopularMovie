package com.example.pablo.popularmovie.bases;

import com.example.pablo.popularmovie.data.DataManager;

/**
 * Created by pablo on 02/03/2018.
 */

public class BasePresenter<E extends IMVPView> implements IMVPPresenter<E> {

    private E mMvpView;

    protected E getMvpView() {
        return mMvpView;
    }

    public BasePresenter(String apiKey) throws NullAPIException {
        if (apiKey == null)
            throw new NullAPIException("Null key Parameter");
    }

    @Override
    public void onAttach(E mvpView) {
            mMvpView = mvpView;
    }

    @Override
    public void onDetach() {
        mMvpView = null;
    }

    @Override
    public boolean isViewAttached(){
        return mMvpView != null;
    }

    public void checkViewAttached() {
        if (!isViewAttached())
            throw new MvpViewNotAttachedException();
    }

    protected DataManager getDataManager(){
        return DataManager.getInstance();
    }


    public static class NullAPIException extends Exception {

        public NullAPIException() {
            super();
        }

        public NullAPIException(String message) {
            super(message);
        }

        public NullAPIException(String message, Throwable cause) {
            super(message, cause);
        }
    }

}
