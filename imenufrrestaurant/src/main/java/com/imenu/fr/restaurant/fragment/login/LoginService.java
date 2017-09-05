package com.imenu.fr.restaurant.fragment.login;


import com.imenu.fr.restaurant.api.apihelper.APIHelper;
import com.imenu.fr.restaurant.api.model.login.LoginRequest;
import com.imenu.fr.restaurant.api.model.login.LoginResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by android on 1/3/17.
 */

public class LoginService {
    private ILoginContract.LoginPresenter mLoginPresenter;

    public LoginService(ILoginContract.LoginPresenter mLoginPresenter) {
        this.mLoginPresenter = mLoginPresenter;
    }

    public void loginUser(LoginRequest loginRequest) {

//        Gson gson=new Gson();
//        Log.e("data",gson.toJson(loginRequest)+"");

        Call<LoginResponse> loginResponseCall = APIHelper.getInstance().createService().loginUser(loginRequest);
        loginResponseCall.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful()) {
                    LoginResponse loginResponse = response.body();
                    mLoginPresenter.onLoginSuccess(loginResponse);

                } else {
                    mLoginPresenter.onApiError(APIHelper.getInstance().handleApiError(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                mLoginPresenter.onServerError();

            }
        });

    }


}
