package com.sheswland.abacusbeads;

import android.Manifest;
import android.app.Activity;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.sheswland.abacusbeads.utils.Const;
import com.sheswland.abacusbeads.utils.InitHistoryController;
import com.sheswland.abacusbeads.utils.JumperHelper;
import com.sheswland.abacusbeads.utils.PFUtil;
import com.sheswland.abacusbeads.utils.permission.PermissionHelper;
import com.sheswland.abacusbeads.utils.permission.PermissionInterface;
import com.sheswland.abacusbeads.utils.permission.PermissionUtil;

public class LoadingActivity extends AppCompatActivity  implements PermissionInterface {

    private Activity mActivity;
    private PermissionHelper permissionHelper;
    private String[] mPermissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        mActivity = this;
        init();
    }

    private void init() {
        // 安装周期第一次打开时导入历史数据
        if (!PFUtil.getBoolean(this, Const.PF_INIT_HISTORY_DATA, false)) {
            PFUtil.putBoolean(this, Const.PF_INIT_HISTORY_DATA, true);
            initPermissionHelper();
        } else {
            new Handler(getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    jump2Operate();
                }
            }, 500);
        }
    }

    private void initPermissionHelper() {
        permissionHelper = new PermissionHelper(this, this);
        permissionHelper.requestPermissions();
    }

    private void jump2Operate() {
        JumperHelper.jump2Operate(mActivity);
        finish();
    }

    @Override
    public int getPermissionsRequestCode() {
        return 0;
    }

    @Override
    public String[] getPermissions() {
        return mPermissions;
    }

    @Override
    public void requestPermissionsSuccess() {
        new InitHistoryController().init(this);
        jump2Operate();
    }

    @Override
    public void requestPermissionsFail() {
        StringBuilder sb = new StringBuilder();
        mPermissions = PermissionUtil.getDeniedPermissions(this, mPermissions);
        for (String s : mPermissions) {
            if (s.equals(Manifest.permission.CAMERA)) {
                sb.append("相机权限(用于拍照，视频聊天);\n");
            } else if (s.equals(Manifest.permission.RECORD_AUDIO)) {
                sb.append("麦克风权限(用于发语音，语音及视频聊天);\n");
            } else if (s.equals(Manifest.permission.READ_EXTERNAL_STORAGE) || s.equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                sb.append("存储,读取权限(用于存储必要信息，缓存数据);\n");
            }
        }
//        PermissionUtil.PermissionDialog(this, "程序运行需要如下权限：\n" + sb.toString() + "请在应用权限管理进行设置！");
        jump2Operate();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] mPermissions, int[] grantResults) {
        if (permissionHelper.requestPermissionsResult(requestCode, mPermissions, grantResults)) {
            //权限请求结果，并已经处理了该回调
            return;
        }
        super.onRequestPermissionsResult(requestCode, mPermissions, grantResults);
    }

}
