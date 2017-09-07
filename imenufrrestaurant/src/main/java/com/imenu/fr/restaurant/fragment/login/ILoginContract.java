package com.imenu.fr.restaurant.fragment.login;


import com.imenu.fr.restaurant.api.model.Status;
import com.imenu.fr.restaurant.api.model.login.LoginRequest;
import com.imenu.fr.restaurant.api.model.login.LoginResponse;
import com.imenu.fr.restaurant.api.model.resetpassword.ResetPasswordRequest;
import com.imenu.fr.restaurant.fragment.IBasePresenter;
import com.imenu.fr.restaurant.fragment.IBaseView;

/**
 * Created by android on 16/5/17.
 */

public interface ILoginContract {
    interface LoginView extends IBaseView {
        void onLoginSuccess(LoginResponse loginResponse);

        void onOtpSendSuccess(Status status);

        void onOtpVerifiedSuccess(Status status);

        void onPasswordResetSuccess(Status status);


    }

    interface LoginPresenter extends IBasePresenter {
        void validateAndLoginUser(LoginRequest loginRequest);

        void onLoginSuccess(LoginResponse loginResponse);

        void onOtpSendSuccess(Status status);

        void onOtpVerifiedSuccess(Status status);

        void onPasswordResetSuccess(Status status);

        void requestOtp(ResetPasswordRequest resetPasswordRequest);

        void verifyOtp(ResetPasswordRequest resetPasswordRequest);

        void resetpassword(ResetPasswordRequest resetPasswordRequest);


    }
}
