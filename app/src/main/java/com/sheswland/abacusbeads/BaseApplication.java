package com.sheswland.abacusbeads;

import android.app.Application;

import org.litepal.LitePal;

public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        LitePal.initialize(this);
    }
}
