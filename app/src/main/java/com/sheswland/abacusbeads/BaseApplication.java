package com.sheswland.abacusbeads;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

import org.litepal.LitePal;

public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        LitePal.initialize(this);
        Fresco.initialize(this);
    }
}
