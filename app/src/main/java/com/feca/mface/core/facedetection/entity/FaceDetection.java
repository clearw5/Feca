package com.feca.mface.core.facedetection.entity;

import com.feca.mface.global.Constants;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Stardust on 2017/10/2.
 */

public class FaceDetection {

    @SerializedName("app_id")
    String mAppId;
    @SerializedName("image")
    String mImage;

    public FaceDetection(String image) {
        this.mAppId = Constants.YOUTU_APP_ID;
        this.mImage = image;
    }

    public String getAppId() {
        return mAppId;
    }

    public String getImage() {
        return mImage;
    }


}
