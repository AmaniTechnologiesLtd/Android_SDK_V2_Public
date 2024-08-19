package com.amani.sdk.base;

import android.app.Application;


import com.amani.sdk.SANDBOX;

import ai.amani.base.utility.AmaniVersion;
import ai.amani.sdk.Amani;
import ai.amani.sdk.UploadSource;


public class BaseService extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //Amani.init(this, "https://YOUR_URL/api/v1/", "/v1");
        Amani.init(this, SANDBOX.SERVER_URL, null, AmaniVersion.V1, UploadSource.KYC);
    }

}

