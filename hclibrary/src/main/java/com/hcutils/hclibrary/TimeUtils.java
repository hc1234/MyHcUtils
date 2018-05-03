package com.hcutils.hclibrary;

import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeUtils {

    /**
     * 查询网站当前时间
     *
     * "yyyy-MM-dd HH:mm:ss"格式
     * @throws Exception
     */
    public static String getNetDateTime() throws Exception {
        URL url = new URL("http://www.baidu.com");// 取得资源对象
        URLConnection uc = url.openConnection();// 生成连接对象
        uc.connect(); // 发出连接
        Date d = new Date(uc.getDate());
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date curDate = new Date(c.getTimeInMillis());
        String str = formatter.format(curDate);
        return str;
    }
    /**
     * 查询网站当前时间
     *
     * format格式
     * @throws Exception
     */
    public static String getNetDateTime(String format) throws Exception {
        URL url = new URL("http://www.baidu.com");// 取得资源对象
        URLConnection uc = url.openConnection();// 生成连接对象
        uc.connect(); // 发出连接
        Date d = new Date(uc.getDate());
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        Date curDate = new Date(c.getTimeInMillis());
        String str = formatter.format(curDate);
        return str;
    }

    /**
     * 查询本地时间
     * yyyy-MM-dd HH:mm:ss格式
     * @return
     */
    public static String getDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
        String str = formatter.format(curDate);
        return str;
    }

    /**
     * 查询本地时间
     * format格式
     * @return
     */
    public static String getDate(String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
        String str = formatter.format(curDate);
        return str;
    }


    /**
     * 根据 mss转换为文字
     * @param mss
     * @return
     */
    public static String formatDateTime(long mss) {
        String DateTimes = null;
        long days = mss / (60 * 60 * 24);
        long hours = (mss % (60 * 60 * 24)) / (60 * 60);
        long minutes = (mss % (60 * 60)) / 60;
        long seconds = mss % 60;
        if (days > 0) {
            DateTimes = days + "天" + hours + "小时" + minutes + "分钟" + seconds + "秒";
        } else if (hours > 0) {
            DateTimes = hours + "小时" + minutes + "分钟" + seconds + "秒";
        } else if (minutes > 0) {
            DateTimes = minutes + "分钟" + seconds + "秒";
        } else {
            DateTimes = seconds + "秒";
        }

        return DateTimes;
    }

    /**
     * 计算两个时间大小
     * 返回正数 t1 比 t2大
     * @param t1
     * @param t2
     *
     */
    public static long paretime(String t1,String t2 ){
        String str="";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long diff=0;
        try
        {
            Date d1 = df.parse(t1);
            Date d2 = df.parse(t2);
            diff = d1.getTime() - d2.getTime();//这样得到的差值是微秒级别
        }
        catch (Exception e)
        {
        }
        return diff;
    }
}
