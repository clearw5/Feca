package com.feca.mface.ui.forum;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.feca.mface.R;
import com.feca.mface.ui.model.Post;
import com.feca.mface.widget.OnRecyclerViewItemClickListener;
import com.feca.mface.widget.ReadOnlyAdapter;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Stardust on 2017/10/4.
 */

@EFragment(R.layout.fragment_forum)
public class ForumFragment extends Fragment {

    private List<Post> mPosts;

    @ViewById(R.id.post_list)
    RecyclerView mPostList;

    @AfterViews
    void setupViews() {
        mPosts = Arrays.asList(
                new Post(getString(R.string.moment_title1), getString(R.string.moment_summary1), R.drawable.post_picture1),
                new Post(getString(R.string.moment_title2), getString(R.string.moment_summary2), R.drawable.post_picture2),
                new Post(getString(R.string.moment_title3), getString(R.string.moment_summary3), R.drawable.post_picture3),
                new Post(getString(R.string.moment_title4), getString(R.string.moment_summary4), R.drawable.post_picture4)
        );

        mPostList.setLayoutManager(new LinearLayoutManager(getContext()));
        mPostList.setAdapter(new ReadOnlyAdapter<>(mPosts, PostViewHolder.class));
        mPostList.addOnItemTouchListener(new OnRecyclerViewItemClickListener(this.getActivity()) {
            @Override
            public void onItemClick(View view, int position) {
                startActivity(new Intent(getActivity(), DetailActivity_.class).putExtra("number",position));
            }
        });
    }



}
