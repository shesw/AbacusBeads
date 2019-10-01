package com.sheswland.abacusbeads.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.sheswland.abacusbeads.utils.DebugLog;
import com.sheswland.abacusbeads.utils.work.WorkManager;
import com.sheswland.abacusbeads.utils.work.utils.OneTimeWorkRequest;
import com.sheswland.abacusbeads.utils.work.workers.PrintDBAfterCommit;

public class PrintAccountService extends Service {
    private final String TAG = "PrintAccountService";

    public PrintAccountService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    private void startPrintWork(int[] ymd) {
        PrintDBAfterCommit printWork = new PrintDBAfterCommit(ymd);
        OneTimeWorkRequest printWorkRequest = new OneTimeWorkRequest.Builder(printWork).build();
        WorkManager.getInstance().beginWith(printWorkRequest).enqueue();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        DebugLog.d(TAG, "onDestroy");
    }

    @Override
    public IBinder onBind(Intent intent) {
        DebugLog.d(TAG, "onBind");
        return new MyBinder();
    }



    class MyBinder extends Binder{
        MyBinder() {}
        public void startPrintWork(int[] ymd) {
            PrintAccountService.this.startPrintWork(ymd);
        }
    }

}
