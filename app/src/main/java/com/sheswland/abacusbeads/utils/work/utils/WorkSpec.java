package com.sheswland.abacusbeads.utils.work.utils;

import android.support.annotation.NonNull;

import com.sheswland.abacusbeads.utils.work.workers.Worker;

public final class WorkSpec {
    @NonNull
    public String id;
//    @NonNull
//    public State state;
    @NonNull
    public String workerClassName;
//    public String inputMergerClassName;
    @NonNull
    public Data input;
    @NonNull
    public Data output;
    public long startTime;
    public String chainId;
    public String submittedTime;

    @NonNull
    public Worker mWorker;

    public WorkSpec(WorkSpec workSpec) {
//        this.state = State.ENQUEUED;
//        this.input = Data.EMPTY;
//        this.output = Data.EMPTY;
        this.id = workSpec.id;
        this.workerClassName = workSpec.workerClassName;
    }

    public WorkSpec(@NonNull Worker worker, @NonNull String id, @NonNull String workerClassName) {
        this.mWorker = worker;
//        this.state = State.ENQUEUED;
//        this.input = Data.EMPTY;
//        this.output = Data.EMPTY;
        this.id = id;
        this.workerClassName = workerClassName;
    }
}
