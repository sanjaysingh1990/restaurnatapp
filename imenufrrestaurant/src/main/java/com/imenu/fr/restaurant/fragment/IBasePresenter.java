package com.imenu.fr.restaurant.fragment;

/**
 * Created by Lakhwinder Singh{lsingh@openkey.io} on 19/4/17.
 * <p>
 * Base Presenter
 */

public interface IBasePresenter {
    void onServerError();

    void onApiError(String message);

    void onDestroyView();

}
