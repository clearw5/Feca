package com.feca.mface.core.facedetection;

import com.feca.mface.global.Constants;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by Stardust on 2017/9/7.
 */

public interface FaceDetectionService {

    @POST("/youtu/api/faceshape")
    Observable<DetectedFaces> detectFace(@Header("Authorization") String sign, @Body FaceDetection body);

    class FaceDetection {
        String app_id;
        String image;

        public FaceDetection(String image) {
            this.app_id = Constants.YOUTU_APP_ID;
            this.image = image;
        }
    }

}
