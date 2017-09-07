package com.feca.mface.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.Toast;

import com.feca.mface.R;
import com.feca.mface.core.facedetection.DetectedFaces;
import com.feca.mface.core.facedetection.FaceDetectionService;
import com.feca.mface.core.facedetection.FacePlusPlus;
import com.feca.mface.core.imaging.Images;
import com.feca.mface.global.Constants;
import com.feca.mface.util.FileUtils;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Retrofit;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_TAKE_PHOTO = 111;

    @ViewById(R.id.picture)
    ImageView mPhoto;

    private Bitmap mBitmap;

    private Uri mImageUri;

    private FaceDetectionService mFaceDetectionService;

    //
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mImageUri = FileUtils.getUri(this, new File(getExternalCacheDir(), "tmp.jpg"));
        mFaceDetectionService = FacePlusPlus.getInstance().getRetrofit()
                .create(FaceDetectionService.class);
    }

    @Click(R.id.take_photo)
    void takePhoto() {
        startActivityForResult(new Intent("android.media.action.IMAGE_CAPTURE")
                        .putExtra(MediaStore.EXTRA_OUTPUT, mImageUri)
                , REQUEST_CODE_TAKE_PHOTO);
    }

    @Click(R.id.test)
    void test() {
        mImageUri = FileUtils.getUri(this, new File("/storage/emulated/0/Pictures/CoolMarket/ls.png"));
        onPhotoToke(null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_TAKE_PHOTO && resultCode == RESULT_OK) {
            onPhotoToke(data);
        }
    }

    private void onPhotoToke(final Intent data) {
        Observable.fromCallable(new Callable<Bitmap>() {
            @Override
            public Bitmap call() throws Exception {
                return BitmapFactory.decodeStream(getContentResolver().openInputStream(mImageUri));
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
                        //return mFaceDetectionService.detectFace4(requestBody);
                        return mFaceDetectionService.detectFace(Constants.FACEPP_API_KEY, Constants.FACEPP_API_SECRET,
                                imageBase64);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<DetectedFaces>() {
                    @Override
                    public void accept(@NonNull DetectedFaces detectedFaces) throws Exception {
                        showDetectedFaces(detectedFaces);
                    }

                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        Toast.makeText(MainActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void showDetectedFaces(DetectedFaces detectedFaces) {
        Bitmap bmCopy = mBitmap.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(bmCopy);
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10);
        DetectedFaces.Face face = detectedFaces.faces[0];
        canvas.drawRect(face.face_rectangle.toRect(), paint);
        mPhoto.setImageBitmap(bmCopy);
    }

}
