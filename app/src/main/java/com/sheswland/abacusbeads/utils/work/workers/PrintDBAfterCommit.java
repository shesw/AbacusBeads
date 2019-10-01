package com.sheswland.abacusbeads.utils.work.workers;

import com.sheswland.abacusbeads.FileController;
import com.sheswland.abacusbeads.utils.Const;
import com.sheswland.abacusbeads.utils.DebugLog;
import com.sheswland.abacusbeads.utils.work.utils.WorkerCallback;

public class PrintDBAfterCommit extends Worker<WorkerCallback>{
    private final String TAG = "PrintDBAfterCommit";

    private int[] ydm;

    public PrintDBAfterCommit(int[] var) {
        this.ydm = var;
    }

    @Override
    public String getName() {
        return TAG;
    }

    @Override
    public WorkerCallback call() throws Exception {

        DebugLog.d(TAG, "call()");
        FileController.getInstance().printTable2SD(Const.TableType.ACCOUNT_DAY, Const.FilterAccuracy.month, ydm);
        FileController.getInstance().printTable2SD(Const.TableType.ACCOUNT_MONTH_AND_YEAR, Const.FilterAccuracy.all_month, ydm);
        FileController.getInstance().printTable2SD(Const.TableType.ACCOUNT_MONTH_AND_YEAR, Const.FilterAccuracy.all_year, ydm);

        WorkerCallback callback = new WorkerCallback(TAG);
        return callback;
    }
}
