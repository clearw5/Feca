package com.feca.mface.ui.camera;

/**
 * Created by Stardust on 2017/12/14.
 */


import android.app.Activity;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.Window;
import android.view.WindowManager;

import com.feca.mface.R;
import com.feca.mface.core.facedetection.FaceppService;
import com.megvii.facepp.sdk.Facepp;

import java.io.IOException;
import java.io.InputStream;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;

public class CameraActivity extends Activity implements SurfaceHolder.Callback {

    private static final String LOG_TAG = "CameraActivity";
    CameraSurface mCameraSurface;
    Camera mCamera;
    SurfaceTexture mSurfaceTexture;
    Facepp mFacepp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_camera);
        mCameraSurface = (CameraSurface) findViewById(R.id.surface);
        SurfaceHolder surfaceholder = mCameraSurface.getHolder();
        surfaceholder.addCallback(this);
        mSurfaceTexture = new SurfaceTexture(10);
        //authFacepp();
        initFacepp();
    }

    private void authFacepp() {
        new FaceppService(this)
                .auth()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete(new Action() {
                    @Override
                    public void run() throws Exception {
                        Log.d(LOG_TAG, "auth succeed");
                        initFacepp();
                    }
                })
                .subscribe();
    }

    private void initFacepp() {
        mFacepp = new Facepp();
        InputStream is = getResources().openRawResource(R.raw.megviifacepp_0_5_2_model);
        try {
            byte[] fuck = new byte[is.available()];
            is.read(fuck);
            is.close();
            mFacepp.init(CameraActivity.this, fuck, 1);
            mCameraSurface.setFacepp(mFacepp);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mCamera = openFront();
        try {
            mCamera.setPreviewTexture(mSurfaceTexture);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Camera.Parameters parameters = mCamera.getParameters();
        parameters.setPreviewSize(1920, 1080);
        parameters.setPreviewFormat(ImageFormat.NV21);
        mCamera.setParameters(parameters);
        mCamera.startPreview();
        mCameraSurface.setCamera(mCamera);
    }


    public static Camera openFront() {
        int numberOfCameras = Camera.getNumberOfCameras();
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.getCameraInfo(i, cameraInfo);
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                return Camera.open(i);
            }
        }
        throw new RuntimeException("没有前置摄像头");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder arg0) {
        mCamera.stopPreview();
        mCamera.release();
        mCamera = null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mFacepp != null) {
            mFacepp.release();
        }
    }
}