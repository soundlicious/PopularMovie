package com.example.pablo.popularmovie.bases;

import com.example.pablo.popularmovie.data.DataManager;

import java.lang.ref.WeakReference;

/**
 * Created by pablo on 02/03/2018.
 */

public class BasePresenter<E extends IMVPView> implements IMVPPresenter<E> {

    private WeakReference<E> mMvpViewRef;
    private boolean isRefreshVisible;

    protected E getMvpView() {
        return mMvpViewRef.get();
    }

    public BasePresenter(String apiKey) throws NullAPIException {
        if (apiKey == null)
            throw new NullAPIException("Null key Parameter");
        isRefreshVisible = false;
    }

    @Override
    public void onAttach(E mvpView) {
            mMvpViewRef = new WeakReference<>(mvpView);
    }

    @Override
    public void onDetach() {
        mMvpViewRef = null;
    }

    @Override
    public boolean isViewAttached(){
        return mMvpViewRef != null;
    }

    public void checkViewAttached() {
        if (!isViewAttached())
            throw new MvpViewNotAttachedException();
    }

    protected DataManager getDataManager(){
        return DataManager.getInstance();
    }

    public boolean isRefreshVisible(){
        return isRefreshVisible;
    }

    public void setRefreshVisible(boolean isRefreshVisible){
        this.isRefreshVisible = isRefreshVisible;
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
