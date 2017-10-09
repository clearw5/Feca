package com.feca.mface.ui.makeup;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
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
import com.tbruyelle.rxpermissions2.RxPermissions;

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

    @ViewById(R.id.toolbar)
    Toolbar mToolbar;

    @ViewById(R.id.makeup_mode)
    TextView mMakeupMode;

    @ViewById(R.id.bottom_bar)
    View mBottomBar;

    private LipstickViewHolder mSelectedLipstickHolder;

    private RecyclerView.Adapter<LipstickViewHolder> mLipstickColorAdapter;
    private final RecyclerView.Adapter<MakeupModeViewHolder> mMakeupModeAdapter =
            new ReadOnlyAdapter<>(MakeupModeModel.modes(), MakeupModeViewHolder.class);
    private FacesMakeup mFacesMakeup;

    private void initFaceMakeup() {
        Uri uri = getIntent().getData();
        if (uri == null)
            return;
        Observable<Bitmap> bitmapObservable = RxBitmap.decodeBitmap(getContentResolver(), uri, 1024 * 1500);
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
        setupToolbar();
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
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setupToolbar() {
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar == null)
            return;
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void onLipstickColorSelected(LipstickViewHolder holder, int position) {
        if (mSelectedLipstickHolder != null) {
            mSelectedLipstickHolder.mLipstickColor.setImageDrawable(null);
        }
        mSelectedLipstickHolder = holder;
        mSelectedLipstickHolder.mLipstickColor.setImageResource(R.drawable.ic_done_white_48dp);
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

        if (position != 0) {
            Toast.makeText(this, R.string.developing, Toast.LENGTH_SHORT).show();
            return;
        }
        mBottomList.setAdapter(mLipstickColorAdapter);
        mBottomBar.setVisibility(View.VISIBLE);
        mMakeupMode.setText(holder.mModeName.getText());
    }

    void savePhoto() {
        new RxPermissions(this)
                .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(@NonNull Boolean granted) throws Exception {
                        if (granted) {
                            doSavingPhoto();
                        } else {
                            Toast.makeText(MakeupActivity.this, R.string.no_permission, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Click(R.id.close)
    void undoMakeup() {
        mPhoto.setImageBitmap(mFacesMakeup.getOriginalFace());
        exitMakeupMode();
    }

    private void exitMakeupMode() {
        mBottomBar.setVisibility(View.GONE);
        mBottomList.setAdapter(mMakeupModeAdapter);
    }

    @Click(R.id.done)
    void completeMakeup() {
        exitMakeupMode();
    }


    private void doSavingPhoto() {
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


    void buy() {
        Uri uri = Uri.parse("https://list.tmall.com/search_product.htm?q=%E5%8F%A3%E7%BA%A2&type=p&vmarket=&spm=875.7931836%2FB.a2227oh.d100&from=mallfp..pc_1_searchbutton");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_makeup, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.buy:
                buy();
                break;
            case R.id.save:
                savePhoto();
                break;
        }
        return true;
    }

}
