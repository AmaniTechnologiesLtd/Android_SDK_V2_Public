package com.amani.sdk.base.cb;

import androidx.annotation.Nullable;

public class CallBack {

    public static CallBack.EventListener listener;

    public interface EventListener {

        void activityFinished(boolean activityFinished, CallBackMessages reason, @Nullable Integer customerID, @Nullable Throwable exception);

        void nfcScan(boolean isSuccess);

        void idUpload(boolean isSuccess);

        void selfieUpload(boolean isSuccess);

    }

    public CallBack() {
        this.listener = null;
    }

    public void setEventListener(CallBack.EventListener listener) {
        this.listener = listener;
    }
}