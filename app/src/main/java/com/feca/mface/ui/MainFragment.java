package com.feca.mface.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.widget.ImageView;

import com.feca.mface.R;
import com.feca.mface.ui.makeup.MakeupActivity_;
import com.feca.mface.widget.WaveView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.io.File;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Stardust on 2017/10/4.
 */

@EFragment(R.layout.fragment_main)
public class MainFragment extends Fragment {

    private static final int REQUEST_CODE_SELECT_IMAGE = 123;

    @ViewById(R.id.add)
    ImageView mAdd;

    @ViewById(R.id.wave)
    WaveView mWaveView;

    private File mPhotoTmpFile;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPhotoTmpFile = new File(getContext().getExternalCacheDir(), "feca.tmp.png");
    }

    //(531, 1130.2), r=136.4
    //w=736, h=1374
    //(779.2, 1579.3), r=195.3
    @AfterViews
    void setupViews() {
        final float picWidth = 736;
        final float picHeight = 1374;
        final float radius = 136.4f;
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int screenHeight = getResources().getDisplayMetrics().heightPixels;
        float radiusScale = (float) (Math.sqrt(screenWidth / picWidth * screenHeight / picHeight));
        float centerX = 531 * screenWidth / picWidth;
        float centerY = 1130.2f * screenHeight / picHeight;
        mWaveView.setInitialRadius(radius * radiusScale);
        mWaveView.setMaxRadius(210 * radiusScale);
        mWaveView.setSpeed(1000);
        mWaveView.setDuration(4000);
        mWaveView.setColor(0xff16f9e1);
        mWaveView.setWaveCenter(centerX, centerY);
        mWaveView.start();
    }

    @Click(R.id.add)
    void add() {
        Intent chooserIntent = Intent.createChooser(new Intent(Intent.ACTION_GET_CONTENT)
                .setType("image/*"), getString(R.string.select_image))
                .putExtra(Intent.EXTRA_INITIAL_INTENTS,
                        new Intent[]{
                                new Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                                        .putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mPhotoTmpFile))
                        });
        startActivityForResult(chooserIntent, REQUEST_CODE_SELECT_IMAGE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Uri uri = Uri.fromFile(mPhotoTmpFile);
            if (data != null && data.getData() != null) {
                uri = data.getData();
            }
            MakeupActivity_.intent(this)
                    .data(uri)
                    .start();
        }
    }

}
