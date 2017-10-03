package com.feca.mface.widget;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * Created by Stardust on 2017/10/1.
 */

public class ReadOnlyAdapter<DT, VH extends BindableViewHolder<DT>> extends RecyclerView.Adapter<VH> {

    private List<DT> mList;
    private Class<VH> mVHClass;

    public ReadOnlyAdapter(List<DT> list, Class<VH> vhClass) {
        mList = list;
        mVHClass = vhClass;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        try {
            return mVHClass.getConstructor(ViewGroup.class).newInstance(parent);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        holder.bind(mList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
