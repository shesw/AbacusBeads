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
        init();
    }


    private void init() {
        // 安装周期第一次打开时导入历史数据

    }
}
