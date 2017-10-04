package com.feca.mface.ui.forum;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.feca.mface.R;
import com.feca.mface.ui.model.Post;
import com.feca.mface.widget.ReadOnlyAdapter;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
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
                new Post(getString(R.string.moment_one_title), getString(R.string.news_one_content), R.drawable.post_picture1),
                new Post(getString(R.string.moment_two_title), getString(R.string.news_two_content), R.drawable.post_picture2),
                new Post(getString(R.string.moment_three_title), getString(R.string.news_two_content), R.drawable.post_picture3),
                new Post(getString(R.string.moment_four_title), getString(R.string.news_two_content), R.drawable.post_picture4)
        );

        mPostList.setLayoutManager(new LinearLayoutManager(getContext()));
        mPostList.setAdapter(new ReadOnlyAdapter<>(mPosts, PostViewHolder.class));
    }


}
