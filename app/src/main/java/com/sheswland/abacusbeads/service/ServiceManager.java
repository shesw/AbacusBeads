package com.sheswland.abacusbeads.service;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.sheswland.abacusbeads.utils.DebugLog;

import java.util.HashSet;
import java.util.Set;

public class ServiceManager {

    private final String TAG = "ServiveManager";

    private static ServiceManager _holder;
    public static ServiceManager getInstance() {
        if (_holder == null) {
            _holder = new ServiceManager();
        }
        return _holder;
    }



    private Set<String> printServiceSet;
    private IBinder mPrintService;
    public void startPrintService(final Activity activity, final int[] ymd) {
        if (printServiceSet == null) {
            printServiceSet = new HashSet<>();
        }
        final String packageName = activity.getPackageName();
        final String className = activity.getLocalClassName();

        if (!printServiceSet.contains(packageName + className) || mPrintService == null) {
            Intent intent = new Intent(activity, PrintAccountService.class);
            ServiceConnection connection = new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName name, IBinder service) {
                    DebugLog.d(TAG, "onServiceConnected " );
                    mPrintService = service;
                    if (mPrintService instanceof PrintAccountService.MyBinder) {
                        ((PrintAccountService.MyBinder)mPrintService).startPrintWork(ymd);
                        printServiceSet.add(packageName + className);
                    }
                }
                @Override
                public void onServiceDisconnected(ComponentName name) {
                    DebugLog.d(TAG, "onServiceDisconnected");
                    printServiceSet.remove(packageName + className);
                }
            };
            activity.bindService(intent, connection, Context.BIND_AUTO_CREATE);
        } else {
            ((PrintAccountService.MyBinder)mPrintService).startPrintWork(ymd);
        }
    }



}
