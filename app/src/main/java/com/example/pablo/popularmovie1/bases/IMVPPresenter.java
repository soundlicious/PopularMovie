package com.example.pablo.popularmovie1.bases;

/**
 * Created by pablo on 02/03/2018.
 */

public interface IMVPPresenter<E extends IMVPView> {

    void onAttach(E mvpView);

    void onDetach();

    boolean isViewAttached();

    class MvpViewNotAttachedException extends RuntimeException {
        public MvpViewNotAttachedException() {
            super("Please call Presenter.onAttach(MvpView) before" +
                    " requesting data to the Presenter");
        }
    }
}
