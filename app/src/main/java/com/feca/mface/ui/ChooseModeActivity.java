package com.feca.mface.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.feca.mface.R;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Fullscreen;

/**
 * Created by Bob on 2017/9/19.
 */
@EActivity(R.layout.activity_choosemode)
@Fullscreen
public class ChooseModeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Click(R.id.lipstick)
    void Lipstick(){
        startActivity(new Intent(this,MainActivity_.class));
    }

    @Click(R.id.blusher)
    void Note(){
        Toast.makeText(this,"本功能正在开发中",Toast.LENGTH_SHORT).show();
    }

    @Click(R.id.eye_shadow)
    void Note1(){
        Toast.makeText(this,"本功能正在开发中",Toast.LENGTH_SHORT).show();
    }

    @Click(R.id.eyebrow)
    void Note2(){
        Toast.makeText(this,"本功能正在开发中",Toast.LENGTH_SHORT).show();
    }

    @Click(R.id.foundation)
    void Note3(){
        Toast.makeText(this,"本功能正在开发中",Toast.LENGTH_SHORT).show();
    }

    @Click(R.id.more)
    void Note4(){
        Toast.makeText(this,"本功能正在开发中",Toast.LENGTH_SHORT).show();
    }


}
