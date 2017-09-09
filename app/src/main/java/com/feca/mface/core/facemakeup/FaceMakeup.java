package com.feca.mface.core.facemakeup;

import android.graphics.Bitmap;

import com.feca.mface.core.facedetection.DetectedFaces;

/**
 * Created by Stardust on 2017/9/7.
 */

public interface FaceMakeup {

    void makeup(Bitmap image, DetectedFaces.FaceShapeItem face);

}
