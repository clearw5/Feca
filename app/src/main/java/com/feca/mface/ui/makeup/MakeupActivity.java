package com.feca.mface.ui.makeup;

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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.feca.mface.R;
import com.feca.mface.core.facedetection.DetectedFaces;
import com.feca.mface.core.facedetection.FaceDetectionService;
import com.feca.mface.core.facedetection.Youtu;
import com.feca.mface.core.facemakeup.Lipstick;
import com.feca.mface.core.imaging.Images;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Fullscreen;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Bob on 2017/9/10.
 */
@EActivity(R.layout.activity_makeup)
public class MakeupActivity extends AppCompatActivity {

    @ViewById(R.id.picture)
    ImageView mPhoto;

    @ViewById(R.id.lipsticks)
    RecyclerView mLipstickRecyclerView;

    private Bitmap mBitmap;

    private FaceDetectionService mFaceDetectionService;


    private static final List<Integer> LIPSTICK_IMAGES = Arrays.asList(
            R.drawable.a, R.drawable.b, R.drawable.c, R.drawable.d,
            R.drawable.e, R.drawable.f, R.drawable.g, R.drawable.h
    );
    private int mColor = 0xff882211;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFaceDetectionService = Youtu.getInstance().getRetrofit()
                .create(FaceDetectionService.class);
        onPhotoToke(getIntent().getData());
    }

    @AfterViews
    void setUpViews() {
        mLipstickRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mLipstickRecyclerView.setAdapter(new LipstickAdapter());
    }


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
                        Toast.makeText(MakeupActivity.this, "Saved to " + file.getPath(), Toast.LENGTH_SHORT).show();
                    }
                });
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
                        Lipstick lipstick = new Lipstick(mColor);
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
                        Toast.makeText(MakeupActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

    class LipstickViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.lipstick_img)
        ImageView mLipStickImg;

        public LipstickViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private class LipstickAdapter extends RecyclerView.Adapter<LipstickViewHolder> {

        @Override
        public LipstickViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new LipstickViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lipstick, parent, false));
        }

        @Override
        public void onBindViewHolder(LipstickViewHolder holder, int position) {
            holder.mLipStickImg.setImageResource(LIPSTICK_IMAGES.get(position));
        }

        @Override
        public int getItemCount() {
            return LIPSTICK_IMAGES.size();
        }
    }
}
