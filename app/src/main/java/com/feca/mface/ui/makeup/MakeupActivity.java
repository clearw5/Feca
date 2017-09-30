package com.feca.mface.ui.makeup;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.TextView;
import android.widget.Toast;

import com.feca.mface.R;
import com.feca.mface.core.facedetection.DetectedFaces;
import com.feca.mface.core.facedetection.FaceDetectionService;
import com.feca.mface.core.facedetection.Youtu;
import com.feca.mface.core.facemakeup.Lipstick;
import com.feca.mface.core.imaging.Images;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.io.FileOutputStream;
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
            R.drawable.e, R.drawable.f, R.drawable.g, R.drawable.h, R.mipmap.returnback
    );
    private static final List<String> LIPSTICK_TEXT = Arrays.asList(
            "正红", "西瓜红", "嫩粉", "自然粉", "珊瑚橘", "裸棕", "豆沙粽", "玫瑰紫", "返回"
    );

    private static final List<Integer> MODE_IMAGES = Arrays.asList(
            R.mipmap.lipstick_black, R.mipmap.blusher_black, R.mipmap.beauty_black,
            R.mipmap.eye_black, R.mipmap.eyebrow_balck
    );
    private static final List<String> MODE_TEXT = Arrays.asList(
            "口红试色", "腮红试色", "粉底试色", "眼影试色", "眉笔试色"
    );
    //各种颜色,,,
    // private int [] colors = {0xffF3524A,0xffB37D69,0xffDA7B93,0xffF977A5,0xffDF2F3E,0xffD98FA9,0xffE22739,0xffF199A0};
    private int[] colors = {0x60d50004, 0x60f94743, 0x60ff7d8b, 0x60fc3d5c, 0x60ff605b, 0x60d78d84, 0x60ba6755, 0x60e37b9e};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFaceDetectionService = Youtu.getInstance().getRetrofit()
                .create(FaceDetectionService.class);
        onPhotoToke(getIntent().getData(), 0);
    }

    @AfterViews
    void setUpViews() {
        mLipstickRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mLipstickRecyclerView.setAdapter(new ModeAdapter());
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
                        Toast.makeText(MakeupActivity.this, "Saved to " + file.getPath(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Click(R.id.back)
    void back() {
        this.finish();
    }

    @Click(R.id.buy)
    void buy() {
        Uri uri = Uri.parse("https://list.tmall.com/search_product.htm?q=%E5%8F%A3%E7%BA%A2&type=p&vmarket=&spm=875.7931836%2FB.a2227oh.d100&from=mallfp..pc_1_searchbutton");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    private void onPhotoToke(final Uri imageUri, final int position) {
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
                        Lipstick lipstick = new Lipstick(colors[position]);
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

    class ModeViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.lipstick_img)
        ImageView mLipStickImg;
        @BindView(R.id.info)
        TextView mInfo;


        public ModeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private class ModeAdapter extends RecyclerView.Adapter<ModeViewHolder> {

        @Override
        public ModeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            //return new LipstickViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lipstick, parent, false));

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lipstick, parent, false);
            final ModeViewHolder holder = new ModeViewHolder(view);
            holder.mLipStickImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getAdapterPosition();
                    if (position == 0) {
                        mLipstickRecyclerView.setAdapter(new LipstickAdapter());
                        notifyDataSetChanged();
                    } else
                        Toast.makeText(MakeupActivity.this, "本功能正在开发中", Toast.LENGTH_SHORT).show();
                }
            });

            return holder;
        }

        @Override
        public void onBindViewHolder(ModeViewHolder holder, int position) {
            holder.mLipStickImg.setImageResource(MODE_IMAGES.get(position));
            holder.mInfo.setText(MODE_TEXT.get(position));
        }

        @Override
        public int getItemCount() {
            return MODE_IMAGES.size();
        }
    }

    private class LipstickAdapter extends RecyclerView.Adapter<ModeViewHolder> {

        @Override
        public ModeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            //return new LipstickViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lipstick, parent, false));

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lipstick, parent, false);
            final ModeViewHolder holder = new ModeViewHolder(view);
            holder.mLipStickImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getAdapterPosition();
                    if (position == getItemCount() - 1) {
                        mLipstickRecyclerView.setAdapter(new ModeAdapter());
                        notifyDataSetChanged();
                    }
                    onPhotoToke(getIntent().getData(), position);

                    /*View chooseview = mLipstickRecyclerView.getChildAt(position);
                    ModeViewHolder viewHolder = new ModeViewHolder(chooseview);
                    viewHolder.mInfo.setTextColor(0xfffab1ce);
                    notifyDataSetChanged();*/
                }
            });

            return holder;
        }

        @Override
        public void onBindViewHolder(ModeViewHolder holder, int position) {
            holder.mLipStickImg.setImageResource(LIPSTICK_IMAGES.get(position));
            holder.mInfo.setText(LIPSTICK_TEXT.get(position));

        }

        @Override
        public int getItemCount() {
            return LIPSTICK_IMAGES.size();
        }
    }

}
