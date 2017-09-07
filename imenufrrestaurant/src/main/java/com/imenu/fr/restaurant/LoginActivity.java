package com.imenu.fr.restaurant;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.view.WindowManager;

import com.imenu.fr.restaurant.activity.BaseActivity;
import com.imenu.fr.restaurant.fragment.SplashFragment;
import com.imenu.fr.restaurant.fragment.login.LoginFragment;
import com.imenu.fr.restaurant.utils.Constants;
import com.imenu.fr.restaurant.utils.Utils;

public class LoginActivity extends BaseActivity {
    private boolean isActivityLoaded;
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        changeStatusBarColorLauncher();
        handler.postDelayed(delay,200);
    }


    Runnable delay = new Runnable() {
        @Override
        public void run() {
            if (isActivityLoaded)
                startSplashScreen();
            else
                handler.postDelayed(delay,200);
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        isActivityLoaded = true;
    }

    public void changeStatusBarColorLauncher() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        }
    }


    void startSplashScreen() {
        if (Utils.getInstance().getValue(Constants.LOGGED_OUT, false, this)) {
            Utils.getInstance().saveValue(Constants.LOGGED_OUT, false, this);
            replaceFragment(LoginFragment.newInstance());
        } else
            replaceFragment(SplashFragment.newInstance());


    }
}
