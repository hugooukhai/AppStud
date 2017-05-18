package com.example.adminpc.appstud;

import android.app.Application;
import android.content.Context;

/**
 * Created by adminPC on 18/05/2017.
 */

public class AppStudApplication extends Application {

    private  static AppStudApplication instance;

    public static Context getContext() {
        return instance.getApplicationContext();

    }
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
}
