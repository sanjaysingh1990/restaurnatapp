package com.imenu.fr.restaurant.app;

import android.app.Application;
import android.content.Context;

/**
 * Created by android on 1/9/17.
 */

public class Imenufr extends Application {
    private static Context context;
    private boolean isHomeActivityAlive;
    @Override
    public void onCreate() {
        super.onCreate();
        context=this;
    }
    public static Context getContext()
    {
        return context;
    }
    public void setStatus(boolean status)
    {
        isHomeActivityAlive=status;
    }
    public boolean getStatus()
    {
        return isHomeActivityAlive;
    }
}
