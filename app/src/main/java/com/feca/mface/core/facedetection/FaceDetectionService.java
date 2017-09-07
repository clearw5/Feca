package com.feca.mface.core.facedetection;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Created by Stardust on 2017/9/7.
 */

public interface FaceDetectionService {

    @FormUrlEncoded
    @POST("/facepp/v3/detect")
    Observable<DetectedFaces> detectFace(@Field("api_key") String apiKey,
                                          @Field("api_secret") String apiSecret,
                                          @Field("image_base64") String imageUrl);

}
