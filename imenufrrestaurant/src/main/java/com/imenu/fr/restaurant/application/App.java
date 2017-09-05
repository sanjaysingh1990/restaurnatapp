package com.imenu.fr.restaurant.application;

import android.app.Application;
import android.content.Context;

/**
 * Created by android on 28/8/17.
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
    }

    public  Context getApplicationContext()
    {
        return getBaseContext();
    }
}
