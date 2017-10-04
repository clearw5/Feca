package com.feca.mface.ui.model;

import android.content.Context;
import android.content.res.TypedArray;

import com.feca.mface.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Stardust on 2017/10/1.
 */

public class LipstickModel {

    private static final List<LipstickModel> LIPSTICKS = new ArrayList<>();
    private int mColor;
    private String mName;

    public LipstickModel(int color, String name) {
        mColor = color;
        mName = name;
    }


    public static List<LipstickModel> lipsticks(Context context) {
        if (!LIPSTICKS.isEmpty())
            return LIPSTICKS;
        TypedArray colors = context.getResources().obtainTypedArray(R.array.lipstick_colors);
        TypedArray names = context.getResources().obtainTypedArray(R.array.lipstick_names);
        for (int i = 0; i < colors.length(); i++) {
            int c = colors.getColor(i, 0);
            LIPSTICKS.add(new LipstickModel(c, names.getString(i)));
        }
        colors.recycle();
        names.recycle();
        return LIPSTICKS;
    }


    public int getColor() {
        return mColor;
    }

    public String getName() {
        return mName;
    }
}
