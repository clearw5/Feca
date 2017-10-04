package com.feca.mface.ui.forum;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.feca.mface.R;
import com.feca.mface.ui.model.Post;
import com.feca.mface.widget.BindableViewHolder;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Stardust on 2017/10/4.
 */

public class PostViewHolder extends BindableViewHolder<Post> {


    @BindView(R.id.summary)
    TextView mSummary;

    @BindView(R.id.picture)
    ImageView mPicture;

    @BindView(R.id.title)
    TextView mTitle;

    public PostViewHolder(ViewGroup parent) {
        super(parent, R.layout.post);
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void bind(Post data, int position) {
        mTitle.setText(data.getTitle());
        mPicture.setImageResource(data.getPictureRes());
        mSummary.setText(data.getSummary());
    }
}
