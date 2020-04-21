package com.hcutils.hclibrary.network;

public class NetworkConstant {
    public static String NowTime="";
    public static int TIME_OUT = 5;//超时
    public static int TOKEN_ERROR = 6;  //token错误
    public static int TIME_OVER=11;//时间与服务器快
    public static final String VERVISON="5"; //版本号
    public static String TOKEN="";
    public static final String IP="http://117.187.12.43";
    public static final String NewIp= IP+":41712/api/"; //常用地址

    public static final String[] Baseparameter={"action","time","sign","version","parameters"};
    public static final String[] MacBaseparameter={"source","mac"};



    //音频通信
    public static final String Videophone="Videophone";
    public static final String [] Videophone_a9={"device","from","to","type","relkey"};// 拨打电话  type 0语音 1视频
    public static final String [] Videophone_a10={"channel"};// 接听电话


    public static final String Bridge="Bridge"; //推送
    public static final String [] Bridge_arr_a1={"device","relkey","channel","type"};  //人推送给设备  type 0是语音 1是视频
    public static final String [] Bridge_arr_a2={"user","relkey","channel","type"};  //人推送给人  type 0是语音 1是视频
    public static final String [] Bridge_arr_a3={"user","device","channel","type"};  //设备推送给人  user device 设备key  type

    //上传文件
    public static final String File="File";
    public static final String [] File_a0={"folder"};// NoticeImages 上传图片
    public static final String [] File_a1={"folder"};// NoticeFiles  上传附件




}
