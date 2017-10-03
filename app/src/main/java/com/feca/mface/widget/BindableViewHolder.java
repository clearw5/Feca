package com.feca.mface.widget;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Stardust on 2017/10/1.
 */

public abstract class BindableViewHolder<DT> extends RecyclerView.ViewHolder {

    public BindableViewHolder(View itemView) {
        super(itemView);
    }

    public BindableViewHolder(ViewGroup parent, int layoutRes) {
        this(LayoutInflater.from(parent.getContext()).inflate(layoutRes, parent, false));
    }

    public abstract void bind(DT data, int position);

}
