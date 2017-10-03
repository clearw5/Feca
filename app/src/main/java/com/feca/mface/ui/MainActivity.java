package com.feca.mface.ui;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.RippleDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewAnimationUtils;
import android.widget.ImageView;

import com.arsy.maps_library.MapRipple;
import com.feca.mface.R;
import com.feca.mface.ui.makeup.MakeupActivity_;
import com.feca.mface.widget.WaveView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_TAKE_PHOTO = 111;
    private static final int REQUEST_CODE_SELECT_IMAGE = 123;

    @ViewById(R.id.add)
    ImageView mAdd;

    @ViewById(R.id.wave)
    WaveView mWaveView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    //(531, 1130.2), r=136.4
    //w=736, h=1374
    //(779.2, 1579.3), r=195.3
    @AfterViews
    void setupViews() {
        super.onStart();
        mWaveView.setInitialRadius(195);
        mWaveView.setMaxRadius(300);
        mWaveView.setSpeed(1000);
        mWaveView.setDuration(4000);
        mWaveView.setColor(0xff16f9e1);
        mWaveView.start();
    }

    @Click(R.id.add)
    void add() {

    }

    //  @Click(R.id.take_photo)
    void takePhoto() {
        startActivityForResult(new Intent("android.media.action.IMAGE_CAPTURE"), REQUEST_CODE_TAKE_PHOTO);
    }

    //@Click(R.id.pick_photo)
    void pickPhoto() {
        startActivityForResult(Intent.createChooser(
                new Intent(Intent.ACTION_GET_CONTENT).setType("image/*"), getString(R.string.select_image)),
                REQUEST_CODE_SELECT_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            data.setClass(this, MakeupActivity_.class);
            data.putExtra("data", data.getData());
            startActivity(data);
        }
    }

}
