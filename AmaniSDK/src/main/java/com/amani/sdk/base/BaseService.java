package com.amani.sdk.base;

import android.app.Application;

import ai.amani.sdk.Amani;


public class BaseService extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //Amani.init(this, "https://YOUR_URL/api/v1/", "/v1");
        Amani.init(this, "https://tr3.amani.ai/api/v1/", "/v1");
    }

}

