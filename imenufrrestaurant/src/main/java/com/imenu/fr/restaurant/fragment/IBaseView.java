package com.imenu.fr.restaurant.fragment;

/**
 * Created by Lakhwinder Singh{lsingh@openkey.io} on 19/4/17.
 * <p>
 * Base View interface
 */

public interface IBaseView {

    void showProgressDialog();


    void hideProgressDialog();

    void onServerError();

    void onError(String message);

    void showAlert(String message);
}
