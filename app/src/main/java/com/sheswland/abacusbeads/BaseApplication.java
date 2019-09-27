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
        init();
    }

    private void init() {
        // 安装周期第一次打开时导入历史数据
        if (!PFUtil.getBoolean(this, Const.PF_INIT_HISTORY_DATA, false)) {
            new InitHistoryController().init(this);
            PFUtil.putBoolean(this, Const.PF_INIT_HISTORY_DATA, true);
        }
    }
}
