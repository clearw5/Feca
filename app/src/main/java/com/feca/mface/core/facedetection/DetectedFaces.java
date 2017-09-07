package com.feca.mface.core.facedetection;

import android.graphics.Rect;

/**
 * Created by Stardust on 2017/9/7.
 */

public class DetectedFaces {

    public String request_id;
    public Face[] faces;
    public String image_id;
    public int time_used;
    public String error_message;

    public static class Face {
        public String face_token;
        public FaceRect face_rectangle;
        public FaceAttributes attributes;

    }

    public static class FaceRect {
        public int left;
        public int top;
        public int width;
        public int height;

        public Rect toRect() {
            return new Rect(left, top, left + width, top + height);
        }
    }

    public static class FaceAttributes {

    }


}
