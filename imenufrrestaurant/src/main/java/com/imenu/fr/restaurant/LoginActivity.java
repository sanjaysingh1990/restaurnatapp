package com.imenu.fr.restaurant;

import android.os.Bundle;
import android.os.Handler;

import com.imenu.fr.restaurant.activity.BaseActivity;
import com.imenu.fr.restaurant.fragment.SplashFragment;
import com.imenu.fr.restaurant.fragment.login.LoginFragment;
import com.imenu.fr.restaurant.utils.Constants;
import com.imenu.fr.restaurant.utils.Utils;

public class LoginActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


    }

    @Override
    protected void onStart() {
        super.onStart();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startSplashScreen();
            }
        }, 200);
    }

    void startSplashScreen() {
        if (Utils.getInstance().getValue(Constants.LOGGED_OUT, false, this)) {
            Utils.getInstance().saveValue(Constants.LOGGED_OUT, false, this);
            replaceFragment(LoginFragment.newInstance());
        } else
            replaceFragment(SplashFragment.newInstance());


    }
}
