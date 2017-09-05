package com.imenu.fr.restaurant.fragment.login;


import com.imenu.fr.restaurant.api.model.login.LoginRequest;
import com.imenu.fr.restaurant.api.model.login.LoginResponse;

/**
 * Created by android on 16/5/17.
 */

public class LoginPresenterImp implements ILoginContract.LoginPresenter {
    private ILoginContract.LoginView mLoginView;
    private LoginService model;

    public LoginPresenterImp(ILoginContract.LoginView mLoginView) {
        this.mLoginView = mLoginView;
        model=new LoginService(this);
    }

    @Override
    public void validateAndLoginUser(LoginRequest loginRequest) {
        if (mLoginView != null) {
            model.loginUser(loginRequest);
            mLoginView.showProgressDialog();

        }
    }



    @Override
    public void onLoginSuccess(LoginResponse loginResponse) {
        if (mLoginView != null) {
            mLoginView.hideProgressDialog();
            mLoginView.onLoginSuccess(loginResponse);
        }
    }

    @Override
    public void onServerError() {
        if (mLoginView != null) {
            mLoginView.hideProgressDialog();
            mLoginView.onServerError();
        }
    }

    @Override
    public void onApiError(String message) {
        if (mLoginView != null) {
            mLoginView.hideProgressDialog();
            mLoginView.onError(message);
        }
    }



    @Override
    public void onDestroyView() {
        mLoginView = null;
        model=null;

    }
}
