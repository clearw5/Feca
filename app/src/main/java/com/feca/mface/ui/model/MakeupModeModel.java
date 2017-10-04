package com.feca.mface.ui.model;

import com.feca.mface.R;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Stardust on 2017/10/1.
 */

public class MakeupModeModel {

    private static final List<MakeupModeModel> MODES = Arrays.asList(
            new MakeupModeModel(R.string.lipstick_test, R.drawable.lipstick_black),
            new MakeupModeModel(R.string.blusher_test, R.drawable.blusher_black),
            new MakeupModeModel(R.string.foundation_test, R.drawable.foundation_black),
            new MakeupModeModel(R.string.eyeshadow_test, R.drawable.eye_black),
            new MakeupModeModel(R.string.eyebrow_pencil_test, R.drawable.eyebrow_balck)
    );

    private int mNameRes;
    private int mIconRes;

    public static List<MakeupModeModel> modes() {
        return MODES;
    }

    public MakeupModeModel(int nameRes, int iconRes) {
        mNameRes = nameRes;
        mIconRes = iconRes;
    }

    public int getNameRes() {
        return mNameRes;
    }

    public int getIconRes() {
        return mIconRes;
    }
}
