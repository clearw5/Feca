package com.feca.mface.core.facemakeup;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.support.v7.graphics.Palette;

import com.feca.mface.core.facedetection.DetectedFaces;
import com.feca.mface.core.imaging.Paths;

/**
 * Created by Stardust on 2017/9/7.
 */

public class Lipstick implements FaceMakeup {

    private int mColor = Color.rgb(161, 42, 32);

    public Lipstick(int color) {
        mColor = color;
    }


    @Override
    public void makeup(Bitmap image, DetectedFaces.FaceShapeItem face) {
        Path p = Paths.fromPoints(face.mouth);
        p.close();
        Canvas canvas = new Canvas(image);
        Paint paint = new Paint();
        paint.setAntiAlias(true);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.OVERLAY));
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(mColor);

        canvas.drawPath(p, paint);
    }
}
