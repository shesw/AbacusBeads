package com.sheswland.abacusbeads.utils.work.utils;

import android.support.annotation.NonNull;

import com.sheswland.abacusbeads.utils.work.workers.Worker;

import java.util.Set;
import java.util.UUID;

public class OneTimeWorkRequest extends WorkRequest {

    OneTimeWorkRequest(@NonNull UUID id, @NonNull WorkSpec workSpec, @NonNull Set<String> tags) {
        super(id, workSpec, tags);
    }

    public static final class Builder extends WorkRequest.Builder<OneTimeWorkRequest.Builder, OneTimeWorkRequest> {
        public Builder(Worker worker) {
            super(worker);
//            this.mWorkSpec.inputMergerClassName = OverwritingInputMerger.class.getName();
        }

        @NonNull
        protected OneTimeWorkRequest.Builder getThis() {
            return this;
        }

        @NonNull
        protected OneTimeWorkRequest buildInternal() {
            return new OneTimeWorkRequest(this.mId, this.mWorkSpec, this.mTags);
        }
    }


}
