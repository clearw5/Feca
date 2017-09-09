package com.feca.mface.core.imaging;

import android.graphics.Path;
import android.graphics.Point;

/**
 * Created by Stardust on 2017/9/7.
 */

public class Paths {

    private static final float LINE_SMOOTHNESS = 0.2f;

    public static Path toPolygon(Point[] points) {
        Path p = new Path();
        p.moveTo(points[0].x, points[0].y);
        for (int i = 1; i < points.length; i++) {
            p.lineTo(points[i].x, points[i].y);
        }
        return p;
    }

    public static Path toQuadCurve(Point[] points) {
        Path p = new Path();
        p.moveTo(points[0].x, points[0].y);
        for (int i = 2; i < points.length; i += 2) {
            p.quadTo(points[i].x, points[i].y, points[i + 1].x, points[i + 1].y);
        }
        return p;
    }

    public static Path toCatmullRomCurve(Point[] points) {
        //保存曲线路径
        Path path = new Path();
        //保存辅助线路径
        Path assistPath = new Path();
        float prePreviousPointX = Float.NaN;
        float prePreviousPointY = Float.NaN;
        float previousPointX = Float.NaN;
        float previousPointY = Float.NaN;
        float currentPointX = Float.NaN;
        float currentPointY = Float.NaN;
        float nextPointX;
        float nextPointY;

        final int lineSize = points.length;
        for (int valueIndex = 0; valueIndex < lineSize; ++valueIndex) {
            if (Float.isNaN(currentPointX)) {
                Point point = points[valueIndex];
                currentPointX = point.x;
                currentPointY = point.y;
            }
            if (Float.isNaN(previousPointX)) {
                //是否是第一个点
                if (valueIndex > 0) {
                    Point point = points[valueIndex - 1];
                    previousPointX = point.x;
                    previousPointY = point.y;
                } else {
                    //是的话就用当前点表示上一个点
                    previousPointX = currentPointX;
                    previousPointY = currentPointY;
                }
            }

            if (Float.isNaN(prePreviousPointX)) {
                //是否是前两个点
                if (valueIndex > 1) {
                    Point point = points[valueIndex - 2];
                    prePreviousPointX = point.x;
                    prePreviousPointY = point.y;
                } else {
                    //是的话就用当前点表示上上个点
                    prePreviousPointX = previousPointX;
                    prePreviousPointY = previousPointY;
                }
            }

            // 判断是不是最后一个点了
            if (valueIndex < lineSize - 1) {
                Point point = points[valueIndex + 1];
                nextPointX = point.x;
                nextPointY = point.y;
            } else {
                //是的话就用当前点表示下一个点
                nextPointX = currentPointX;
                nextPointY = currentPointY;
            }

            if (valueIndex == 0) {
                // 将Path移动到开始点
                path.moveTo(currentPointX, currentPointY);
                assistPath.moveTo(currentPointX, currentPointY);
            } else {
                // 求出控制点坐标
                final float firstDiffX = (currentPointX - prePreviousPointX);
                final float firstDiffY = (currentPointY - prePreviousPointY);
                final float secondDiffX = (nextPointX - previousPointX);
                final float secondDiffY = (nextPointY - previousPointY);
                final float firstControlPointX = previousPointX + (LINE_SMOOTHNESS * firstDiffX);
                final float firstControlPointY = previousPointY + (LINE_SMOOTHNESS * firstDiffY);
                final float secondControlPointX = currentPointX - (LINE_SMOOTHNESS * secondDiffX);
                final float secondControlPointY = currentPointY - (LINE_SMOOTHNESS * secondDiffY);
                //画出曲线
                path.cubicTo(firstControlPointX, firstControlPointY, secondControlPointX, secondControlPointY,
                        currentPointX, currentPointY);
                //将控制点保存到辅助路径上
                assistPath.lineTo(firstControlPointX, firstControlPointY);
                assistPath.lineTo(secondControlPointX, secondControlPointY);
                assistPath.lineTo(currentPointX, currentPointY);
            }

            // 更新值,
            prePreviousPointX = previousPointX;
            prePreviousPointY = previousPointY;
            previousPointX = currentPointX;
            previousPointY = currentPointY;
            currentPointX = nextPointX;
            currentPointY = nextPointY;
        }
        return path;
    }

}
