package com.feca.mface.ui.makeup;

import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.feca.mface.R;
import com.feca.mface.ui.model.LipstickModel;
import com.feca.mface.widget.BindableViewHolder;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Stardust on 2017/10/1.
 */

public class LipstickViewHolder extends BindableViewHolder<LipstickModel> {

    @BindView(R.id.color)
    ImageView mLipstickColor;

    @BindView(R.id.name)
    TextView mName;

    private GradientDrawable mLipstickColorBackground;

    public LipstickViewHolder(ViewGroup parent) {
        super(parent, R.layout.item_lipstick);
        ButterKnife.bind(this, itemView);
        mLipstickColorBackground = (GradientDrawable) mLipstickColor.getBackground();
    }

    @Override
    public void bind(LipstickModel lipstick, int position) {
        mLipstickColorBackground.setColor(lipstick.getColor());
        mName.setText(lipstick.getName());
    }
}
