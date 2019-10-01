package com.sheswland.abacusbeads.utils.work.utils;

import android.support.annotation.NonNull;

import com.sheswland.abacusbeads.utils.work.workers.Worker;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public abstract class WorkRequest {

    @NonNull
    private WorkSpec mWorkSpec;
    @NonNull
    private Set<String> mTags;
    @NonNull
    private UUID mId;

    public WorkSpec getWorkSpec() {return mWorkSpec;}

    public WorkRequest(@NonNull UUID id, @NonNull WorkSpec workSpec, @NonNull Set<String> tags) {
        this.mId = id;
        this.mWorkSpec = workSpec;
        this.mTags = tags;
    }

    public static abstract class Builder<B extends Builder, W extends WorkRequest> {

        WorkSpec mWorkSpec;
        Set<String> mTags = new HashSet();
        UUID mId = UUID.randomUUID();

        Builder(Worker worker) {
            this.mWorkSpec = new WorkSpec(worker, this.mId.toString(), worker.getName());
            this.addTag(worker.getName());
        }

        public B setInputData(Data inputData) {
            this.mWorkSpec.input = inputData;
            return this.getThis();
        }

        public B addTag(String tag) {
            this.mTags.add(tag);
            return this.getThis();
        }

        public W build() {
            W returnVal = this.buildInternal();
            this.mId = UUID.randomUUID();
            this.mWorkSpec = new WorkSpec(this.mWorkSpec);
            this.mWorkSpec.id = this.mId.toString();
            return returnVal;
        }

        @NonNull
        protected abstract B getThis();

        @NonNull
        protected abstract W buildInternal();
    }

}
