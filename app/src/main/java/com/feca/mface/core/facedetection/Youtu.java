package com.feca.mface.core.facedetection;

import android.support.annotation.NonNull;

import com.feca.mface.core.facedetection.youtu.YoutuSign;
import com.feca.mface.global.Constants;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by Stardust on 2017/9/7.
 */

public class Youtu {

    private static Youtu sInstance = new Youtu();

    private Retrofit mRetrofit;

    protected Youtu() {
        mRetrofit = new Retrofit.Builder()
                .baseUrl("http://api.youtu.qq.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(new OkHttpClient.Builder()
                        .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                        .addInterceptor(new Interceptor() {
                            @Override
                            public Response intercept(@NonNull Chain chain) throws IOException {
                                Request original = chain.request();
                                Request request = original.newBuilder()
                                        .headers(original.headers())
                                        .header("Authorization", sign(0))
                                        .method(original.method(), original.body())
                                        .build();
                                return chain.proceed(request);
                            }
                        })
                        .build())
                .build();
    }

    public static Youtu getInstance() {
        return sInstance;
    }

    public Retrofit getRetrofit() {
        return mRetrofit;
    }

    public String sign(long expired) {
        StringBuffer sign = new StringBuffer();
        YoutuSign.appSign(Constants.YOUTU_APP_ID, Constants.YOUTU_SECRET_ID, Constants.YOUTU_SECRET_KEY, expired,
                Constants.YOUTU_USER_ID, sign);
        return sign.toString();
    }

    public String sign() {
        return sign(0);
    }
}
