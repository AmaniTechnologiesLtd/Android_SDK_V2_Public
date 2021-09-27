package com.amani.sdk.base.cb;

public class CallBackInternal {
    public static CallBackInternal.EventListener listener;

    public interface EventListener {

        void courierRequested(boolean isRequested);

        void lastStepCompleted(boolean isCompleted);

        void loginSuccess(boolean isSuccess);

        void userExit(boolean isExit);

    }

    public CallBackInternal() {
        this.listener = null;
    }

    public void setEventListener(CallBackInternal.EventListener listener) {
        this.listener = listener;
    }
}

