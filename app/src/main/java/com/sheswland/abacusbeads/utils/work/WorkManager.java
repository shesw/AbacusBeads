package com.sheswland.abacusbeads.utils.work;

import android.support.annotation.NonNull;

import com.sheswland.abacusbeads.utils.DebugLog;
import com.sheswland.abacusbeads.utils.work.utils.OneTimeWorkRequest;
import com.sheswland.abacusbeads.utils.work.utils.WorkContinuation;
import com.sheswland.abacusbeads.utils.work.utils.WorkRequest;
import com.sheswland.abacusbeads.utils.work.utils.WorkerCallback;
import com.sheswland.abacusbeads.utils.work.workers.Worker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class WorkManager {
    private final String TAG = "WorkManager";
    private final int TIME_OUT = 60000;

    private static WorkManager _HOLDER;
    public synchronized static WorkManager getInstance() {
        if (_HOLDER == null) {
            _HOLDER = new WorkManager();
            _HOLDER.init();
        }
        return _HOLDER;
    }

    private ExecutorService mExecutorService;
    private void init() {
        mExecutorService = Executors.newCachedThreadPool();
    }

    /***************************** interface ***********************/

    public final WorkContinuation beginWith(@NonNull OneTimeWorkRequest... work) {
        return this.beginWith(Arrays.asList(work));
    }


    /************************ inner ***************************/
    private WorkContinuation beginWith(final @NonNull List<? extends OneTimeWorkRequest> var) {
        executeAll(var);
        return new WorkContinuation() {
            @NonNull
            @Override
            public WorkContinuation then(@NonNull List<WorkRequest> var1) {
                executeAll(var1);
                return this;
            }

            @Override
            public UUID enqueue() {
                return null;
            }
        };
    }

    private void executeAll(List<? extends WorkRequest> var) {
        List<Worker<WorkerCallback>> workers = new ArrayList<>();

        for (WorkRequest request : var) {
            workers.add(request.getWorkSpec().mWorker);
        }

        try {
            DebugLog.d(TAG, "start invokeAll");
            List<Future<WorkerCallback>> futures = mExecutorService.invokeAll(workers, TIME_OUT, TimeUnit.MILLISECONDS);

            if (futures.size() > 0) {
                for (Future<WorkerCallback> future : futures) {
                    DebugLog.d(TAG, future.get().getName());
                }
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }

}
