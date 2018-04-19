package com.hcutils.hclibrary;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;

public class MyCheckPermissopn {

    /**
     * 判断是否有权限
     * @param permission
     * @param context
     * @return
     */
    public static  boolean checkPermission(@NonNull String permission, Context context) {
        if (Build.VERSION.SDK_INT <23) {
            return true;
        }
        return ActivityCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }
}
