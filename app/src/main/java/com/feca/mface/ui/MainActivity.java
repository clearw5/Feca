package com.feca.mface.ui;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.RippleDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewAnimationUtils;
import android.widget.ImageView;

import com.arsy.maps_library.MapRipple;
import com.feca.mface.R;
import com.feca.mface.ui.forum.ForumFragment;
import com.feca.mface.ui.forum.ForumFragment_;
import com.feca.mface.ui.makeup.MakeupActivity_;
import com.feca.mface.widget.WaveView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import fr.castorflex.android.verticalviewpager.VerticalViewPager;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {

    @ViewById(R.id.viewpager)
    VerticalViewPager mVerticalViewPager;

    @AfterViews
    void setupViews() {
        mVerticalViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int i) {
                switch (i) {
                    case 0:
                        return new MainFragment_();
                    case 1:
                        return new ForumFragment_();
                }
                throw new IllegalArgumentException();
            }

            @Override
            public int getCount() {
                return 2;
            }
        });
    }

}
