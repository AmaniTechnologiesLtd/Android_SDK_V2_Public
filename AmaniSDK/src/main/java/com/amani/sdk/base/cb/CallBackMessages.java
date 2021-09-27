package com.amani.sdk.base.cb;

import com.google.gson.annotations.SerializedName;

public enum
CallBackMessages {
    @SerializedName("1000")
    DESTROYED_BY_USER,
    @SerializedName("1001")
    CAUSE_LOGIN,
    @SerializedName("2000")
    COURIER_REQUESTED,
    @SerializedName("100")
    VERIFICATION_COMPLETED,
    @SerializedName("500")
    THROW_EXCEPTION,

}

