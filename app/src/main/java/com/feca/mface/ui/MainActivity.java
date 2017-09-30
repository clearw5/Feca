package com.feca.mface.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.feca.mface.R;
import com.feca.mface.ui.makeup.MakeupActivity_;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_TAKE_PHOTO = 111;
    private static final int REQUEST_CODE_SELECT_IMAGE = 123;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            data.setClass(this, MakeupActivity_.class);
            data.putExtra("data",data.getData());
            startActivity(data);
        }
    }

}
