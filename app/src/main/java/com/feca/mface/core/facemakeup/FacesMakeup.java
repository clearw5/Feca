package com.feca.mface.core.facemakeup;

import android.graphics.Bitmap;

import com.feca.mface.core.facedetection.DetectedFaces;

/**
 * Created by Stardust on 2017/9/9.
 */

public class FacesMakeup {

    private Bitmap mOriginalFace;
    private Bitmap mMadeUpFace;
    private DetectedFaces mDetectedFaces;

    public FacesMakeup(Bitmap originalFace, DetectedFaces detectedFaces) {
        mOriginalFace = originalFace;
        mDetectedFaces = detectedFaces;
    }

    public void makeup(FaceMakeup makeup) {
        ensureMadeUpFace();
        for (int i = 0; i < mDetectedFaces.face_shape.length; i++) {
            makeup.makeup(mMadeUpFace, mDetectedFaces.face_shape[i]);
        }
    }

    public void reset() {
        mMadeUpFace = mOriginalFace.copy(Bitmap.Config.ARGB_8888, true);
    }

    public Bitmap getMadeUpFace() {
        ensureMadeUpFace();
        return mMadeUpFace;
    }

    private void ensureMadeUpFace() {
        if (mMadeUpFace == null) {
            reset();
        }
    }


    public Bitmap getOriginalFace() {
        return mOriginalFace;
    }
}
