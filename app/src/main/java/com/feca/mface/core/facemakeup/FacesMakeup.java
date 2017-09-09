package com.feca.mface.core.facemakeup;

import android.graphics.Bitmap;

import com.feca.mface.core.facedetection.DetectedFaces;

/**
 * Created by Stardust on 2017/9/9.
 */

public class FacesMakeup {

    private FaceMakeup mFaceMakeup;

    public FacesMakeup(FaceMakeup faceMakeup) {
        mFaceMakeup = faceMakeup;
    }

    public void makeup(Bitmap bitmap, DetectedFaces faces) {
        for (int i = 0; i < faces.face_shape.length; i++) {
            mFaceMakeup.makeup(bitmap, faces.face_shape[i]);
        }
    }


}
