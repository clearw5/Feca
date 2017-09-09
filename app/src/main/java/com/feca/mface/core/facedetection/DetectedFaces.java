package com.feca.mface.core.facedetection;

import android.graphics.Point;
import android.graphics.Rect;

/**
 * Created by Stardust on 2017/9/7.
 */

public class DetectedFaces {

    public FaceShapeItem[] face_shape; // 人脸轮廓结构体，包含所有人脸的轮廓点
    public int image_width; // 请求图片的宽度
    public int image_height; //	请求图片的高度
    public int errorcode; //返回状态值
    public String errormsg; //返回错误消息

    public static class FaceShapeItem {
        public Point[] face_profile; //	描述脸型轮廓的21点
        public Point[] left_eye; //描述左眼轮廓的8点
        public Point[] right_eye;    //	描述右眼轮廓的8点
        public Point[] left_eyebrow; //	描述左眉轮廓的8点
        public Point[] right_eyebrow; //	描述右眉轮廓的8点
        public Point[] mouth; //描述嘴巴轮廓的22点
        public Point[] nose; //描述鼻子轮廓的13点

    }

}
