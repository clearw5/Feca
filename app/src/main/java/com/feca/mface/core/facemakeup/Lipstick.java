package com.feca.mface.core.facemakeup;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;

import com.feca.mface.core.facedetection.DetectedFaces;
import com.feca.mface.core.imaging.ColorDetector;
import com.feca.mface.core.imaging.Paths;

/**
 * Created by Stardust on 2017/9/7.
 */

public class Lipstick implements FaceMakeup {

    private static final float A = 0.5f;
    private static final int LIPSTICK_DETECTING_THRESHOLD = 0x13;
    private int mColor;

    public Lipstick(int color) {
        int newcolor = 0X60000000 + color;
        mColor = newcolor;
    }


   // @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void makeup(Bitmap image, DetectedFaces.FaceShapeItem face) {
        //Path mouthBounds = Paths.toPolygon(face.mouth);
        //int lipColor = extractLipAverageColor(image, mouthBounds);
        //applyLipstick(image, mouthBounds, lipColor, mColor);

        Canvas canvas = new Canvas(image);
        Paint p = new Paint();
        p.setAntiAlias(true);
        p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.OVERLAY));
        p.setColor(mColor);
        p.setStrokeJoin(Paint.Join.ROUND);
        canvas.drawPath(Paths.toCatmullRomCurve(face.getLowerLip()), p);
        canvas.drawPath(Paths.toCatmullRomCurve(face.getUpperLip()), p);

    }

    private int extractLipAverageColor(Bitmap image, Path mouthBounds) {
        //for test
        return 0xe09184;
    }

    private void applyLipstick(Bitmap image, Path mouthBounds, int lipAverageColor, int color) {
        Rect bounds = computeMouthBoundsInRect(mouthBounds);
        ColorDetector lipDetector = new ColorDetector.WeightedRGBDistanceDetector(lipAverageColor, LIPSTICK_DETECTING_THRESHOLD);
        Canvas canvas = new Canvas(image);
        Paint p = new Paint();
        p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.OVERLAY));
        p.setColor(mColor);
        for (int y = bounds.top; y < bounds.bottom; y++) {
            for (int x = bounds.left; x < bounds.right; x++) {
                int c = image.getPixel(x, y);
                boolean isLip = lipDetector.detectsColor(Color.red(c), Color.green(c), Color.blue(c));
                if (!isLip) {
                    continue;
                }
                //c = applyLipstick(c, lipAverageColor, color);
                canvas.drawPoint(x, y, p);
                //image.setPixel(x, y, c);
            }
        }

    }

    private int applyLipstick(int c, int lipAverageColor, int lipstickColor) {
        return (int) (lipstickColor * A + c * (1 - A));
    }

    private Rect computeMouthBoundsInRect(Path mouthBounds) {
        RectF extraBounds = new RectF();
        mouthBounds.computeBounds(extraBounds, true);
        Rect bounds = new Rect();
        extraBounds.roundOut(bounds);
        return bounds;
    }
}
