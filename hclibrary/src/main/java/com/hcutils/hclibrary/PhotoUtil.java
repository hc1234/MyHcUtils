package com.hcutils.hclibrary;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PhotoUtil {

    public static int PHOTO_REQUEST=1002;


    /**
     * 默认拍照，自动生成文件
     * @param activity
     * @return
     */

    public static File takePicture(Activity activity) {

        if(MyCheckPermissopn.ApplyPermission(Manifest.permission.CAMERA,activity)&&MyCheckPermissopn.ApplyPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,activity)) {
            File takeImageFile = null;
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            takePictureIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            if (takePictureIntent.resolveActivity(activity.getPackageManager()) != null) {
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
                    takeImageFile = new File(Environment.getExternalStorageDirectory(), "/DCIM/camera/");
                else takeImageFile = Environment.getDataDirectory();
                takeImageFile = createFile(takeImageFile, "IMG_", ".jpg");
                if (takeImageFile != null) {
                    // 默认情况下，即不需要指定intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                    // 照相机有自己默认的存储路径，拍摄的照片将返回一个缩略图。如果想访问原始图片，
                    // 可以通过dat extra能够得到原始图片位置。即，如果指定了目标uri，data就没有数据，
                    // 如果没有指定uri，则data就返回有数据！

                    Uri uri;
                    if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
                        uri = Uri.fromFile(takeImageFile);
                    } else {

                        /**
                         * 7.0 调用系统相机拍照不再允许使用Uri方式，应该替换为FileProvider
                         * 并且这样可以解决MIUI系统上拍照返回size为0的情况
                         */
                        uri = FileProvider.getUriForFile(activity, activity.getPackageName() + ".provider", takeImageFile);
                        //加入uri权限
                        List<ResolveInfo> resInfoList = activity.getPackageManager().queryIntentActivities
                                (takePictureIntent, PackageManager.MATCH_DEFAULT_ONLY);
                        for (ResolveInfo resolveInfo : resInfoList) {
                            String packageName = resolveInfo.activityInfo.packageName;
                            activity.grantUriPermission(packageName, uri, Intent
                                    .FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        }
                    }

                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                }
            }
            activity.startActivityForResult(takePictureIntent, PHOTO_REQUEST);
        return takeImageFile;
        }
        return null;
    }

    /**
     *  拍照，传一个文件过来 （文件 或 文件夹）
     * @param activity
     * @param
     * @return
     */

    public static File takePicture(Activity activity,File phtofile) {

        if(MyCheckPermissopn.ApplyPermission(Manifest.permission.CAMERA,activity)&&MyCheckPermissopn.ApplyPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,activity)) {
            File takeImageFile = null;
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            takePictureIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            if (takePictureIntent.resolveActivity(activity.getPackageManager()) != null) {
                if (phtofile.exists() && phtofile.isDirectory()) {
                    takeImageFile = createFile(phtofile, "IMG_", ".jpg");
                } else if (phtofile.exists() && phtofile.isFile()) {
                    takeImageFile = phtofile;
                } else {
                    Toast.makeText(activity, "该文件不存在", Toast.LENGTH_SHORT).show();
                }
                if (takeImageFile != null) {
                    // 默认情况下，即不需要指定intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                    // 照相机有自己默认的存储路径，拍摄的照片将返回一个缩略图。如果想访问原始图片，
                    // 可以通过dat extra能够得到原始图片位置。即，如果指定了目标uri，data就没有数据，
                    // 如果没有指定uri，则data就返回有数据！

                    Uri uri;
                    if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
                        uri = Uri.fromFile(takeImageFile);
                    } else {

                        /**
                         * 7.0 调用系统相机拍照不再允许使用Uri方式，应该替换为FileProvider
                         * 并且这样可以解决MIUI系统上拍照返回size为0的情况
                         */
                        uri = FileProvider.getUriForFile(activity, activity.getPackageName() + ".provider", takeImageFile);
                        //加入uri权限
                        List<ResolveInfo> resInfoList = activity.getPackageManager().queryIntentActivities
                                (takePictureIntent, PackageManager.MATCH_DEFAULT_ONLY);
                        for (ResolveInfo resolveInfo : resInfoList) {
                            String packageName = resolveInfo.activityInfo.packageName;
                            activity.grantUriPermission(packageName, uri, Intent
                                    .FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        }
                    }

                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                }
            }
            activity.startActivityForResult(takePictureIntent, PHOTO_REQUEST);
            return takeImageFile;
        }
        return null;
    }

    /**
     * 根据系统时间、前缀、后缀产生一个文件
     */
    public static File createFile(File folder, String prefix, String suffix) {
        if (!folder.exists() || !folder.isDirectory()) folder.mkdirs();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA);
        String filename = prefix + dateFormat.format(new Date(System.currentTimeMillis())) + suffix;
        return new File(folder, filename);
    }
}
