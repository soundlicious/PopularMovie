package com.example.pablo.popularmovie1.bases;

import com.example.pablo.popularmovie1.data.DataManager;

/**
 * Created by pablo on 02/03/2018.
 */

public class BasePresenter<E extends IMVPView> implements IMVPPresenter<E> {

    private E mMvpView;

    protected E getMvpView() {
        return mMvpView;
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




}
