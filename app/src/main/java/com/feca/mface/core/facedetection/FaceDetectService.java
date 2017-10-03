package com.feca.mface.core.facedetection;

import android.graphics.Bitmap;

import com.feca.mface.core.facedetection.api.FaceDetectionApi;
import com.feca.mface.core.facedetection.entity.FaceDetection;
import com.feca.mface.core.imaging.Images;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Stardust on 2017/10/2.
 */

public class FaceDetectService {

    private static FaceDetectService sFaceDetectService = new FaceDetectService();

    private FaceDetectionApi mFaceDetectionApi;

    public FaceDetectService() {
        mFaceDetectionApi = Youtu.getInstance().getRetrofit()
                .create(FaceDetectionApi.class);
    }

    public static FaceDetectService getInstance() {
        return sFaceDetectService;
    }

    public Observable<DetectedFaces> detect(Bitmap bitmap) {
        return Observable.just(bitmap)
                .observeOn(Schedulers.computation())
                .map(new Function<Bitmap, String>() {
                    @Override
                    public String apply(@NonNull Bitmap bitmap) throws Exception {
                        return Images.toBase64(bitmap);
                    }
                })
                .observeOn(Schedulers.io())
                .flatMap(new Function<String, ObservableSource<DetectedFaces>>() {
                    @Override
                    public ObservableSource<DetectedFaces> apply(@NonNull String imageBase64) throws Exception {
                        return mFaceDetectionApi.detectFace(Youtu.getInstance().sign(),
                                new FaceDetection(imageBase64));
                    }
                });
    }
}
