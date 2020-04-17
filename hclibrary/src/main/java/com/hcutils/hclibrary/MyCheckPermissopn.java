package com.hcutils.hclibrary;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

public class MyCheckPermissopn {
    public static int REQUSTCODE=1001;


    /**
     * 判断并申请权限
     * @param permission
     * @param activity
     */
    public static Boolean ApplyPermission(String permission,Activity activity){

        if(!checkPermission(permission,activity)){
            ActivityCompat.requestPermissions(activity, new String[]{permission}, REQUSTCODE);
            return false;
        }else{
            return true;
        }
    }

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
