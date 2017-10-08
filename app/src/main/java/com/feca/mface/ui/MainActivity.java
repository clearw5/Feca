package com.feca.mface.ui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;

import com.feca.mface.R;
import com.feca.mface.ui.forum.ForumFragment_;

import org.androidannotations.annotations.AfterViews;
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
