package com.feca.mface.ui.model;

/**
 * Created by Stardust on 2017/10/4.
 */

public class Post {
    private String mSummary;
    private String mTitle;
    private int mPictureRes;

    public Post(String title, String summary, int pictureRes) {
        mSummary = summary;
        mTitle = title;
        mPictureRes = pictureRes;
    }

    public String getSummary() {
        return mSummary;
    }

    public String getTitle() {
        return mTitle;
    }

    public int getPictureRes() {
        return mPictureRes;
    }
}
