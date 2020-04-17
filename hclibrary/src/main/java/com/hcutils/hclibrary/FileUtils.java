package com.hcutils.hclibrary;

import android.Manifest;
import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;

public class FileUtils {


    /**
     * 创建文件
     * @param context
     * @param filepath
     * @return
     */
    public static Boolean CreteNewFile(Context context, String filepath){

        if(ISSDCARD(context)){
            File file=new File(filepath);
            if(!file.exists()){
                try {
                    return file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return false;

    }

    /**
     * 创建文件夹
     * @param context
     * @param filepath
     * @return
     */
    public static Boolean CreteNewFileMidr(Context context, String filepath){

        if(ISSDCARD(context)){
            File file = new File(filepath);
            if(!file.exists()){
                return file.mkdirs();
            }

        }
    return false;
    }

    /**
     * 删除文件（文件夹，文件） 文件过大时建议不要在主线程调用
     * @param fielpath
     * @return
     */
    public static Boolean Deletefile(Context context, String fielpath){
        if(ISSDCARD(context)) {
            File file = new File(fielpath);
            if (file.exists()) {
                if (file.isFile()) {
                    return file.delete();
                } else if (file.isDirectory()) {
                    deletefile(file);
                    return true;
                }
            }else{
            }
        }
        return false;
    }

    /**
     * 删除文件夹下所有文件
     * @param file
     */
    public static void deletefile(File file){
        if(file.exists()&&file.isDirectory()){
            for (File file1 : file.listFiles()){
                if(file1.isFile()){
                    file.delete();
                }else if(file1.isDirectory()){
                    deletefile(file1);
                }
            }
            file.delete();
        }
    }

    /**
     *
     * @param filepath
     * @param isbackLong false 返回文件大小String 如1M,100K等   true 返回文件size大小 如1024
     * @return
     */
    public static String GetFileSize(Context context, String filepath, Boolean isbackLong){

        if(ISSDCARD(context)) {
            File file=new File(filepath);
            if(file.exists()){
                if(file.isFile()){
                    if(isbackLong){
                        return file.length()+"";
                    }else{
                        return getFormatSize(file.length());
                    }
                }else if(file.isDirectory()){
                    try {
                        if(isbackLong) {
                            return getFolderSize(file)+"";
                        }else{
                            return getFormatSize(getFolderSize(file));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }else{
            }
        }
        return "0";
    }

    /**
     * 获取指定文件夹内所有文件大小的和
     *
     * @param file file
     * @return size
     * @throws Exception
     */
    public static long getFolderSize(File file) throws Exception {
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for (File aFileList : fileList) {
                if (aFileList.isDirectory()) {
                    size = size + getFolderSize(aFileList);
                } else {
                    size = size + aFileList.length();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }
    /**
     * 格式化单位
     *
     * @param size size
     * @return size
     */
    public static String getFormatSize(double size) {

        double kiloByte = size / 1024;
        if (kiloByte < 1) {
            return size + "Byte";
        }

        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "KB";
        }

        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "MB";
        }

        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "GB";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);

        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "TB";
    }


    /**
     * 判断是否存在SD卡和存储权限是否打开
     * @return
     */
    public static Boolean ISSDCARD(Context context){
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)||!Environment.isExternalStorageRemovable()){
                if(MyCheckPermissopn.checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,context)){
                    return true;
                }else{
                    Log.i("hc","存储权限未开启");
                    Toast.makeText(context,"存储权限未开启", Toast.LENGTH_SHORT).show();
                }
        }else{
            Toast.makeText(context,"SD卡不可使用", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    /**
     * 指定文件写入数据 (追加写入)
     * @param fileName
     * @param message
     */
    public static void writeFileSdcard(String fileName, String message) {

        try {
            // FileOutputStream fout = openFileOutput(fileName, MODE_PRIVATE);

            FileOutputStream fout = new FileOutputStream(fileName,true);

            byte[] bytes = message.getBytes();

            fout.write(bytes);

            fout.close();
        }
        catch (Exception e) {

            e.printStackTrace();

        }

    }


}
