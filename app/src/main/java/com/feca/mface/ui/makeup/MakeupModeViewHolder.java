package com.feca.mface.ui.makeup;

import android.graphics.drawable.GradientDrawable;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.feca.mface.R;
import com.feca.mface.ui.model.MakeupModeModel;
import com.feca.mface.widget.BindableViewHolder;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Stardust on 2017/10/1.
 */

public class MakeupModeViewHolder extends BindableViewHolder<MakeupModeModel> {


    @BindView(R.id.icon)
    ImageView mIcon;

    @BindView(R.id.name)
    TextView mModeName;

    GradientDrawable mLipstickColorBackground;

    public MakeupModeViewHolder(ViewGroup parent) {
        super(parent, R.layout.item_makeup_mode);
        ButterKnife.bind(this, itemView);

    }


    @Override
    public void bind(MakeupModeModel mode, int position) {
        mModeName.setText(mode.getNameRes());
        mIcon.setImageResource(mode.getIconRes());
    }
}
