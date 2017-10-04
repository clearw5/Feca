package com.feca.mface.ui.makeup;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.feca.mface.R;
import com.feca.mface.core.facedetection.DetectedFaces;
import com.feca.mface.core.facedetection.FaceDetectService;
import com.feca.mface.core.facemakeup.FacesMakeup;
import com.feca.mface.core.facemakeup.Lipstick;
import com.feca.mface.ui.model.LipstickModel;
import com.feca.mface.ui.model.MakeupModeModel;
import com.feca.mface.util.RxBitmap;
import com.feca.mface.widget.OnRecyclerViewItemClickListener;
import com.feca.mface.widget.ReadOnlyAdapter;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.File;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.BiFunction;
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
    RecyclerView mBottomList;

    private RecyclerView.Adapter<LipstickViewHolder> mLipstickColorAdapter;
    private final RecyclerView.Adapter<MakeupModeViewHolder> mMakeupModeAdapter =
            new ReadOnlyAdapter<>(MakeupModeModel.modes(), MakeupModeViewHolder.class);
    private FacesMakeup mFacesMakeup;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void initFaceMakeup() {
        Uri uri = getIntent().getData();
        if (uri == null)
            return;
        Observable<Bitmap> bitmapObservable = RxBitmap.decodeBitmap(getContentResolver(), uri);
        Observable<DetectedFaces> detectedFacesObservable =
                bitmapObservable.flatMap(new Function<Bitmap, ObservableSource<DetectedFaces>>() {
                    @Override
                    public ObservableSource<DetectedFaces> apply(@NonNull Bitmap bitmap) throws Exception {
                        return FaceDetectService.getInstance().detect(bitmap);
                    }
                });
        Observable.zip(bitmapObservable, detectedFacesObservable, new BiFunction<Bitmap, DetectedFaces, FacesMakeup>() {
            @Override
            public FacesMakeup apply(@NonNull Bitmap bitmap, @NonNull DetectedFaces faces) throws Exception {
                return new FacesMakeup(bitmap, faces);
            }
        })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<FacesMakeup>() {
                    @Override
                    public void accept(@NonNull FacesMakeup o) throws Exception {
                        mFacesMakeup = o;
                        mPhoto.setImageBitmap(o.getOriginalFace());
                    }
                });

    }


    @AfterViews
    void setUpViews() {
        initFaceMakeup();
        mBottomList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mBottomList.setAdapter(mMakeupModeAdapter);
        mLipstickColorAdapter = new ReadOnlyAdapter<>(LipstickModel.lipsticks(this)
                , LipstickViewHolder.class);
        mBottomList.addOnItemTouchListener(new OnRecyclerViewItemClickListener(this) {
            @Override
            public void onItemClick(View view, int position) {
                RecyclerView.ViewHolder holder = mBottomList.getChildViewHolder(view);
                if (holder instanceof MakeupModeViewHolder) {
                    onMakeupModeSelected((MakeupModeViewHolder) holder, position);
                } else {
                    onLipstickColorSelected((LipstickViewHolder) holder, position);
                }
            }
        });
    }

    private void onLipstickColorSelected(LipstickViewHolder holder, int position) {
        LipstickModel lipstickModel = LipstickModel.lipsticks(this).get(position);
        Observable.just(lipstickModel.getColor())
                .map(new Function<Integer, Bitmap>() {
                    @Override
                    public Bitmap apply(@NonNull Integer color) throws Exception {
                        mFacesMakeup.reset();
                        mFacesMakeup.makeup(new Lipstick(color));
                        return mFacesMakeup.getMadeUpFace();
                    }
                })
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Bitmap>() {
                    @Override
                    public void accept(@NonNull Bitmap bitmap) throws Exception {
                        mPhoto.setImageBitmap(bitmap);
                    }
                });
    }

    private void onMakeupModeSelected(MakeupModeViewHolder holder, int position) {
        mBottomList.setAdapter(mLipstickColorAdapter);
    }

    @Click(R.id.save_photo)
    void savePhoto() {
        File file = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis() + ".png");
        RxBitmap.saveBitmap(mFacesMakeup.getMadeUpFace(), file)
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


}
