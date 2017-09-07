package com.imenu.fr.restaurant.fragment.login;


import com.imenu.fr.restaurant.api.model.Status;
import com.imenu.fr.restaurant.api.model.login.LoginRequest;
import com.imenu.fr.restaurant.api.model.login.LoginResponse;
import com.imenu.fr.restaurant.api.model.resetpassword.ResetPasswordRequest;

/**
 * Created by android on 16/5/17.
 */

public class LoginPresenterImp implements ILoginContract.LoginPresenter {
    private ILoginContract.LoginView mLoginView;
    private LoginService model;

    public LoginPresenterImp(ILoginContract.LoginView mLoginView) {
        this.mLoginView = mLoginView;
        model = new LoginService(this);
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
    public void onOtpSendSuccess(Status status) {
        if (mLoginView != null) {
            mLoginView.hideProgressDialog();
            mLoginView.onOtpSendSuccess(status);
        }
    }

    @Override
    public void onOtpVerifiedSuccess(Status status) {
        if (mLoginView != null) {
            mLoginView.hideProgressDialog();
            mLoginView.onOtpVerifiedSuccess(status);
        }
    }

    @Override
    public void onPasswordResetSuccess(Status status) {
        if (mLoginView != null) {
            mLoginView.hideProgressDialog();
            mLoginView.onPasswordResetSuccess(status);
        }
    }

    @Override
    public void requestOtp(ResetPasswordRequest resetPasswordRequest) {
        if (mLoginView != null) {
            mLoginView.showProgressDialog();
            model.resetPassword(resetPasswordRequest);
        }
    }

    @Override
    public void verifyOtp(ResetPasswordRequest resetPasswordRequest) {
        if (mLoginView != null) {
            mLoginView.showProgressDialog();
            model.resetPassword(resetPasswordRequest);

        }
    }

    @Override
    public void resetpassword(ResetPasswordRequest resetPasswordRequest) {
        if (mLoginView != null) {
            mLoginView.showProgressDialog();
            model.resetPassword(resetPasswordRequest);
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
        model = null;

    }
}
