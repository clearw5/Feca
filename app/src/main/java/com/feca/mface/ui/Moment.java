package com.feca.mface.ui;

import java.io.Serializable;

/**
 * Created by ljh on 2017/9/28.
 */

public class Moment implements Serializable{
    private String title;
    private String content;
    private int photoId;

    public Moment(String title, String content, int photoId) {
        this.title=title;
        this.content=content;
        this.photoId=photoId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getPhotoId() {
        return photoId;
    }

    public void setPhotoId(int photoId) {
        this.photoId = photoId;
    }
}
