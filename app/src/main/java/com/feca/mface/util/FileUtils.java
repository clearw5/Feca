package com.feca.mface.util;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;

import com.feca.mface.ui.MainActivity;

import java.io.File;

/**
 * Created by Stardust on 2017/9/7.
 */

public class FileUtils {

    public static Uri getUri(Context context, File file) {
        return Uri.fromFile(file);

    }
}
