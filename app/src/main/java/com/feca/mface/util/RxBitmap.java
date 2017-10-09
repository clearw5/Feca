package com.feca.mface.util;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;

import com.feca.mface.core.imaging.Images;

import java.io.File;
import java.io.FileOutputStream;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * Created by Stardust on 2017/10/2.
 */

public class RxBitmap {

    public static Observable<Bitmap> decodeBitmap(final ContentResolver resolver, final Uri uri, int sizeLimit) {
        return Observable.fromCallable(new Callable<Bitmap>() {
            @Override
            public Bitmap call() throws Exception {
                return BitmapFactory.decodeStream(resolver.openInputStream(uri));
            }
        });
    }


    public static Observable<File> saveBitmap(Bitmap bitmap, File file) {
        return Observable.just(bitmap)
                .map(new Function<Bitmap, File>() {
                    @Override
                    public File apply(@NonNull Bitmap bitmap) throws Exception {
                        File file = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis() + ".png");
                        FileOutputStream outputStream = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                        return file;
                    }
                });
    }
}
