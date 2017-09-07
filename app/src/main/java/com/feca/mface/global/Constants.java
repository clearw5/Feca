package com.feca.mface.global;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by Stardust on 2017/9/7.
 */

public class Constants {
    public static final RequestBody REQUEST_BODY_FACEPP_API_KEY = RequestBody.create(MediaType.parse("text/plain"), "KmFLGxOjT0RUDE6l6sNJkFtKYhTbWgEy");
    public static final RequestBody REQUEST_BODY_FACEPP_API_SECRET = RequestBody.create(MediaType.parse("text/plain"), "rWMaDvvoJRr64c2VZ5aRIoJUyqhAjlf0");
    public static final String FACEPP_API_KEY = "KmFLGxOjT0RUDE6l6sNJkFtKYhTbWgEy";
    public static final String FACEPP_API_SECRET = "rWMaDvvoJRr64c2VZ5aRIoJUyqhAjlf0";

}
