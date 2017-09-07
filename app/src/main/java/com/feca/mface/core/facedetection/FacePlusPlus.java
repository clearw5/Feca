package com.feca.mface.core.facedetection;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by Stardust on 2017/9/7.
 */

public class FacePlusPlus {

    private static FacePlusPlus sInstance = new FacePlusPlus();

    private Retrofit mRetrofit;

    protected FacePlusPlus() {
        mRetrofit = new Retrofit.Builder()
                .baseUrl("https://api-cn.faceplusplus.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(new OkHttpClient.Builder()
                        .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                        .build())
                .build();
    }

    public static FacePlusPlus getInstance() {
        return sInstance;
    }

    public Retrofit getRetrofit() {
        return mRetrofit;
    }

}
