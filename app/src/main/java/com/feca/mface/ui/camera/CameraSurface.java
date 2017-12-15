package com.feca.mface.ui.camera;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.hardware.camera2.CameraDevice;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.feca.mface.core.facemakeup.Lipstick;
import com.megvii.facepp.sdk.Facepp;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;

/**
 * Created by Stardust on 2017/12/14.
 */

public class CameraSurface extends SurfaceView implements Camera.PreviewCallback {

    private static final String LOG_TAG = "CameraSurface";
    private Paint mPaint = new Paint();
    private SurfaceHolder mSurfaceHolder;
    private Canvas mCanvas;
    private Facepp mFacepp;

    public CameraSurface(Context context) {
        super(context);
        init();
    }

    public CameraSurface(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CameraSurface(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public CameraSurface(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        mSurfaceHolder = getHolder();
    }

    public void setCamera(Camera camera) {
        camera.setPreviewCallback(this);
    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        Camera.Parameters parameters = camera.getParameters();
        int width = parameters.getPreviewSize().width;
        int height = parameters.getPreviewSize().height;

        YuvImage yuv = new YuvImage(data, parameters.getPreviewFormat(), width, height, null);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        yuv.compressToJpeg(new Rect(0, 0, width, height), 100, out);

        byte[] bytes = out.toByteArray();
        final Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        mCanvas = mSurfaceHolder.lockCanvas();
        Matrix m = new Matrix();
        m.postRotate(-90);
        m.postTranslate(0, 1920);
        if (mFacepp != null) {
            Facepp.Face[] faces = mFacepp.detect(data, width, height, Facepp.IMAGEMODE_NV21);
            if (faces.length > 0) {
                new Lipstick(Color.RED).makeup(bitmap, faces[0]);
                Log.d(LOG_TAG, "face: " + Arrays.toString(faces));
            }
        }
        mCanvas.drawBitmap(bitmap, m, mPaint);
        mSurfaceHolder.unlockCanvasAndPost(mCanvas);
    }

    public static byte[] getGrayscale(Bitmap bitmap) {
        if (bitmap == null)
            return null;

        byte[] ret = new byte[bitmap.getWidth() * bitmap.getHeight()];
        for (int j = 0; j < bitmap.getHeight(); ++j)
            for (int i = 0; i < bitmap.getWidth(); ++i) {
                int pixel = bitmap.getPixel(i, j);
                int red = ((pixel & 0x00FF0000) >> 16);
                int green = ((pixel & 0x0000FF00) >> 8);
                int blue = pixel & 0x000000FF;
                ret[j * bitmap.getWidth() + i] = (byte) ((299 * red + 587
                        * green + 114 * blue) / 1000);
            }
        return ret;
    }

    public void setFacepp(Facepp facepp) {
        mFacepp = facepp;
    }
}
