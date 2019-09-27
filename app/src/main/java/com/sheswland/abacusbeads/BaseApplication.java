package com.sheswland.abacusbeads;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.sheswland.abacusbeads.utils.Const;
import com.sheswland.abacusbeads.utils.InitHistoryController;
import com.sheswland.abacusbeads.utils.PFUtil;

import org.litepal.LitePal;

import java.io.File;

public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        LitePal.initialize(this);
        Fresco.initialize(this);
    }

}
