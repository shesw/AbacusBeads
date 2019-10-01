package com.sheswland.abacusbeads.utils.work.utils;

import android.support.annotation.NonNull;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public abstract class WorkContinuation {
    public WorkContinuation() {
    }

    @NonNull
    public final WorkContinuation then(@NonNull WorkRequest... workRequests) {
        return this.then(Arrays.asList(workRequests));
    }

    @NonNull
    public abstract WorkContinuation then(@NonNull List<WorkRequest> var1);

    public abstract UUID enqueue();
}
