package com.feca.mface.core.facedetection;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Base64;

import com.feca.mface.global.Constants;
import com.megvii.facepp.sdk.Facepp;
import com.megvii.licensemanager.sdk.LicenseManager;

import java.util.UUID;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by Stardust on 2017/12/15.
 */

public class FaceppService {

    public static class AuthException extends RuntimeException {
        private final int mCode;

        public AuthException(int code, String message) {
            super(message);
            this.mCode = code;
        }

        public int getCode() {
            return mCode;
        }
    }

    private Context mContext;
    private String mUUID;

    public FaceppService(Context context) {
        mContext = context;
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        mUUID = preferences.getString("uuid", null);
        if (mUUID == null) {
            mUUID = Base64.encodeToString(UUID.randomUUID().toString().getBytes(), Base64.DEFAULT);
            preferences.edit().putString("uuid", mUUID).apply();
        }
    }

    public Observable<Void> auth() {
        final PublishSubject<Void> r = PublishSubject.create();
        new LicenseManager(mContext).takeLicenseFromNetwork(mUUID, Constants.FACEPP_API_KEY, Constants.FACEPP_API_SECRET, Facepp.getApiName(), LicenseManager.DURATION_30DAYS, "Landmark", "1", true, new LicenseManager.TakeLicenseCallback() {

            @Override
            public void onSuccess() {
                r.onComplete();
            }

            @Override
            public void onFailed(int code, byte[] bytes) {
                r.onError(new AuthException(code, new String(bytes)));
            }
        });
        return r;
    }
}
