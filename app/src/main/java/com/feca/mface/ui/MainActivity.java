package com.feca.mface.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.feca.mface.R;
import com.feca.mface.core.facedetection.DetectedFaces;
import com.feca.mface.core.facedetection.FaceDetectionService;
import com.feca.mface.core.facedetection.Youtu;
import com.feca.mface.core.facemakeup.Lipstick;
import com.feca.mface.core.imaging.Images;
import com.feca.mface.core.imaging.Paths;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.io.FileOutputStream;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_TAKE_PHOTO = 111;
    private static final int REQUEST_CODE_SELECT_IMAGE = 123;

    @ViewById(R.id.picture)
    ImageView mPhoto;

    @ViewById(R.id.R)
    TextView mR;

    @ViewById(R.id.G)
    TextView mG;

    @ViewById(R.id.B)
    TextView mB;


    private Bitmap mBitmap;

    private FaceDetectionService mFaceDetectionService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFaceDetectionService = Youtu.getInstance().getRetrofit()
                .create(FaceDetectionService.class);
    }

    @Click(R.id.take_photo)
    void takePhoto() {
        startActivityForResult(new Intent("android.media.action.IMAGE_CAPTURE"), REQUEST_CODE_TAKE_PHOTO);
    }

    @Click(R.id.pick_photo)
    void pickPhoto() {
        startActivityForResult(Intent.createChooser(
                new Intent(Intent.ACTION_GET_CONTENT).setType("image/*"), getString(R.string.select_image)),
                REQUEST_CODE_SELECT_IMAGE);
    }

    @Click(R.id.save_photo)
    void savePhoto() {
        if (mBitmap == null) {
            return;
        }
        Observable.just(mBitmap)
                .map(new Function<Bitmap, File>() {
                    @Override
                    public File apply(@NonNull Bitmap bitmap) throws Exception {
                        File file = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis() + ".png");
                        FileOutputStream outputStream = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                        return file;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<File>() {
                    @Override
                    public void accept(@NonNull File file) throws Exception {
                        Toast.makeText(MainActivity.this, "Saved to " + file.getPath(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            onPhotoToke(data.getData());
        }
    }

    private void onPhotoToke(final Uri imageUri) {
        Observable.fromCallable(new Callable<Bitmap>() {
            @Override
            public Bitmap call() throws Exception {
                return BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
            }
        })
                .map(new Function<Bitmap, String>() {
                    @Override
                    public String apply(@NonNull Bitmap bitmap) throws Exception {
                        mBitmap = bitmap;
                        return Images.toBase64(mBitmap);
                    }
                })
                .subscribeOn(Schedulers.computation())
                .flatMap(new Function<String, ObservableSource<DetectedFaces>>() {
                    @Override
                    public ObservableSource<DetectedFaces> apply(@NonNull String imageBase64) throws Exception {
                        return mFaceDetectionService.detectFace(Youtu.getInstance().sign(0),
                                new FaceDetectionService.FaceDetection(imageBase64));
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<DetectedFaces>() {
                    @Override
                    public void accept(@NonNull DetectedFaces detectedFaces) throws Exception {
                        Bitmap bmCopy = mBitmap.copy(Bitmap.Config.ARGB_8888, true);
                        Lipstick lipstick = new Lipstick(getColor());
                        for (int i = 0; i < detectedFaces.face_shape.length; i++) {
                            lipstick.makeup(bmCopy, detectedFaces.face_shape[i]);
                        }
                        mPhoto.setImageBitmap(bmCopy);
                        mBitmap = bmCopy;
                        //showDetectedFaces(detectedFaces);
                    }

                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                        Toast.makeText(MainActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private int getColor() {
        return Color.rgb(Integer.parseInt(mR.getText().toString()), Integer.parseInt(mG.getText().toString()),
                Integer.parseInt(mB.getText().toString()));
    }

    private void showDetectedFaces(DetectedFaces detectedFaces) {
        Canvas canvas = new Canvas(mBitmap);
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        for (int i = 0; i < detectedFaces.face_shape.length; i++) {
            drawPoints(canvas, paint, 8, Color.RED, detectedFaces.face_shape[i].face_profile);
            drawPoints(canvas, paint, 4, Color.BLUE, detectedFaces.face_shape[i].left_eye);
            drawPoints(canvas, paint, 4, Color.BLUE, detectedFaces.face_shape[i].right_eye);
            drawPoints(canvas, paint, 4, Color.YELLOW, detectedFaces.face_shape[i].left_eyebrow);
            drawPoints(canvas, paint, 4, Color.YELLOW, detectedFaces.face_shape[i].right_eyebrow);
            drawPoints(canvas, paint, 4, Color.GREEN, detectedFaces.face_shape[i].mouth);
            drawPoints(canvas, paint, 4, Color.BLACK, detectedFaces.face_shape[i].nose);
        }
        mPhoto.setImageBitmap(mBitmap);
    }

    private void drawPoints(Canvas canvas, Paint paint, int width, int color, Point[] points) {
        paint.setStrokeWidth(width);
        paint.setColor(color);
        Path p = Paths.toPolygon(points);
        p.close();
        canvas.drawPath(p, paint);
    }

}
