package com.hcutils.hclibrary.Chat;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.alivc.rtc.AliRtcAuthInfo;
import com.alivc.rtc.AliRtcEngine;
import com.hcutils.hclibrary.Datautils.DataUtis;
import com.hcutils.hclibrary.Datautils.SounPoilUtill;
import com.hcutils.hclibrary.Utils.ThreadUtils;
import com.hcutils.hclibrary.network.NetWorkBobyInfor;
import com.hcutils.hclibrary.network.NetworkConstant;
import com.hcutils.hclibrary.network.NetworkUtil;

import java.util.List;


public class BaseChatActivity extends HcUtisBaseActivty {
    public interface NetWorkResult{
        void result(int code, String data);
    }

    /**
     * SDK提供的对音视频通话处理的引擎类
     */
    public AliRtcEngine mAliRtcEngine;
    /**
     * 前台服务的Intent
     */
    private Intent mForeServiceIntent;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("hcc","base");

        initRTCEngineAndStartPreview();
        SounPoilUtill.Getinstanc(BaseChatActivity.this);
    }


    /**
     * 初始化
     */
    private void initRTCEngineAndStartPreview() {
        AliRtcEngine.setH5CompatibleMode(1);
        // 防止初始化过多
        if (mAliRtcEngine == null) {
            //实例化,必须在主线程进行。
            mAliRtcEngine = AliRtcEngine.getInstance(BaseChatActivity.this);
            mAliRtcEngine.startAudioPlayer();
            mAliRtcEngine.startAudioCapture();
        }
    }

    /**
     * 切换 听筒 扬声器
     */
    public void setSpeakerphone(){
        if(mAliRtcEngine!=null){
            mAliRtcEngine.enableSpeakerphone(!GetSpeakerphone());
        }
    }

    /**
     * 判断是否扬声器
     * @return
     */
    public Boolean GetSpeakerphone(){
        if(mAliRtcEngine!=null){
            return mAliRtcEngine.isSpeakerOn();
        }
        return false;
    }


    /**
     * 加入频道
     * @param rtcInfor
     */
    public void joinChannel(RTCInfor rtcInfor ,Boolean cammer ) {
        if (mAliRtcEngine == null) {
            return;
        }
        AliRtcAuthInfo userInfo = new AliRtcAuthInfo();
        userInfo.setAppid(rtcInfor.getAppid());
        userInfo.setNonce(rtcInfor.getNonce());
        userInfo.setTimestamp(rtcInfor.getTimestamp());
        userInfo.setUserId(rtcInfor.getUserid());
        userInfo.setGslb(rtcInfor.getGslb());
        userInfo.setToken(rtcInfor.getToken());
        userInfo.setConferenceId(rtcInfor.getChannel());
        Log.i("hcc2",rtcInfor.getAppid()+" "+rtcInfor.getChannel()+" "+rtcInfor.getUserid()+" "+rtcInfor.getNonce()+" "+rtcInfor.getTimestamp()+" "+rtcInfor.getToken()+" "+rtcInfor.getGslb()[0]);
        /*
         *设置自动发布和订阅，只能在joinChannel之前设置
         *参数1    true表示自动发布；false表示手动发布
         *参数2    true表示自动订阅；false表示手动订阅
         */
//        mAliRtcEngine.setChannelProfile(ALI_RTC_INTERFACE.AliRTCSDK_Channel_Profile.AliRTCSDK_Communication);
        mAliRtcEngine.setAutoPublishSubscribe(true, false);
        mAliRtcEngine.configLocalCameraPublish(cammer);
        // 加入频道
        mAliRtcEngine.joinChannel(userInfo, rtcInfor.getUsername());
    }

    public void palyCallMusic(){
        if(mAliRtcEngine!=null){
            SounPoilUtill.Getinstanc(BaseChatActivity.this).playrool(2);
        }
    }

    public void palyComeMusic(){
        if(mAliRtcEngine!=null){
            SounPoilUtill.Getinstanc(BaseChatActivity.this).playrool(1);
        }
    }
    public void stopMusic(){
        SounPoilUtill.Getinstanc(BaseChatActivity.this).stop();
    }

    /**
     * 手动订阅 视频
     * @param uid
     */
    public void setGetVideo(String uid){
        if(mAliRtcEngine!=null){
            mAliRtcEngine.configRemoteCameraTrack(uid,true,true);
            mAliRtcEngine.configRemoteScreenTrack(uid,true);
            mAliRtcEngine.configRemoteAudio(uid,true);
            mAliRtcEngine.subscribe(uid);
        }

    }
    /**
     * 手动订阅 音频
     * @param uid
     */
    public void setGetVoice(String uid){
        if(mAliRtcEngine!=null){
            mAliRtcEngine.configRemoteCameraTrack(uid,false,false);
            mAliRtcEngine.configRemoteScreenTrack(uid,false);
            mAliRtcEngine.configRemoteAudio(uid,true);
            mAliRtcEngine.subscribe(uid);
        }

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        desServer();
    }

    @Override
    protected void onDestroy() {
        stopMusic();
        stopCount();
        stopThreadTime();
        //离会
        if (mAliRtcEngine != null) {
            mAliRtcEngine.setRtcEngineNotify(null);
            mAliRtcEngine.setRtcEngineEventListener(null);
            mAliRtcEngine.stopPreview();
            mAliRtcEngine.leaveChannel();
            mAliRtcEngine.destroy();
        }

        super.onDestroy();
    }

    /**
     * 开启前台服务
     */
    public void startServer(){

        if (null == mForeServiceIntent) {
            mForeServiceIntent = new Intent(BaseChatActivity.this.getApplicationContext(),
                    ForegroundService.class);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(mForeServiceIntent);
        } else {
            startService(mForeServiceIntent);
        }
    }
    /**
     * 销毁服务
     */
    public void desServer(){
        //销毁服务
        if (null != mForeServiceIntent && isServiceRunning(this.getApplicationContext(),
                ForegroundService.class.getName())) {
            stopService(mForeServiceIntent);
        }
    }
    public Boolean isOnline(String uid){

        if(mAliRtcEngine!=null) {
            String [] strings=mAliRtcEngine.getOnlineRemoteUsers();
            Log.i("hcc","isOnline=="+uid+"  "+mAliRtcEngine.isUserOnline(uid)+"  数量=="+mAliRtcEngine.getOnlineRemoteUsers().length);
            if(strings.length>0){
                return true;
            }
            return mAliRtcEngine.isUserOnline(uid);
        }else{
            return false;
        }
    }

    /**
     * 判断服务是否运行
     * @param context
     * @param serviceName
     * @return
     */
    public  boolean isServiceRunning(Context context, String serviceName) {
        if (TextUtils.isEmpty(serviceName)){
            return false;
        }

        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> runningServiceInfos = am.getRunningServices(100);
        if (runningServiceInfos.size() <= 0) {
            return false;
        }
        for (ActivityManager.RunningServiceInfo serviceInfo : runningServiceInfos) {
            if (serviceInfo.service.getClassName().equals(serviceName)) {
                return true;
            }
        }
        return false;
    }


    /**
     * 获取拨打电话信息
     * @param netWorkResult
     */
     public void getCallInfor(NetWorkResult netWorkResult, CallInfor callInfor){

         NetWorkBobyInfor netWorkBobyInfor = new NetWorkBobyInfor();
         netWorkBobyInfor.setAction(9);
         netWorkBobyInfor.setIpaddress(NetworkConstant.Videophone);
         netWorkBobyInfor.setISshowDialog(false);
         netWorkBobyInfor.setParameters(NetworkConstant.Videophone_a9);
         netWorkBobyInfor.setParameters_value(new String[]{callInfor.getDevice(),callInfor.getFrom(),callInfor.getTo(),callInfor.getType(),callInfor.getRelkey()});
         netWorkBobyInfor.setCallBack(new NetWorkBobyInfor.CallBack() {
             @Override
             public void setresult(int code, String data) {

                 if(code==0){
                     startCount(BaseChatActivity.this);
                     netWorkResult.result(code,data);
                    RTCInfor rtcInfor = DataUtis.parseToJson(data);
                    if(callInfor.getPush_type().equals("D2P")){
                        pushTuisongD2P(callInfor.getTo(),callInfor.getDevice(),rtcInfor.getChannel(),callInfor.getType());
                    }else if(callInfor.getPush_type().equals("P2D")){
                        pushTuisongP2D(callInfor.getRelkey(),callInfor.getTo(),rtcInfor.getChannel(),callInfor.getType());
                    }else if(callInfor.getPush_type().equals("P2P")){
                        pushTuisongP2P(callInfor.getRelkey(),callInfor.getTo(),rtcInfor.getChannel(),callInfor.getType());
                    }
                 }else{
                     ToastUtis("信息获取失败");
                     finish();
                 }
             }
         });
         NetworkUtil.func_post(netWorkBobyInfor,BaseChatActivity.this);
     }

    /**
     * 获取接听电话信息
     * @param netWorkResult
     */
    public void getAnswerInfor(NetWorkResult netWorkResult,String channel){
        NetWorkBobyInfor netWorkBobyInfor = new NetWorkBobyInfor();
        netWorkBobyInfor.setAction(10);
        netWorkBobyInfor.setIpaddress(NetworkConstant.Videophone);
        netWorkBobyInfor.setISshowDialog(false);
        netWorkBobyInfor.setParameters(NetworkConstant.Videophone_a10);
        netWorkBobyInfor.setParameters_value(new String[]{channel});
        netWorkBobyInfor.setCallBack(new NetWorkBobyInfor.CallBack() {
            @Override
            public void setresult(int code, String data) {
                if(code==0){
                    netWorkResult.result(code,data);
                }else{
                    ToastUtis("信息获取失败");
                    finish();
                }

            }
        });
        NetworkUtil.func_post(netWorkBobyInfor,BaseChatActivity.this);
    }

    /**
     * 发送推送 设备推送给人
     * @param user
     * @param divicenumber
     * @param cannne
     * @param callType
     */
    public void pushTuisongD2P(String user,String divicenumber,String cannne,String callType){
        NetWorkBobyInfor netWorkBobyInfor = new NetWorkBobyInfor();
        netWorkBobyInfor.setAction(3);
        netWorkBobyInfor.setIpaddress(NetworkConstant.Bridge);
        netWorkBobyInfor.setParameters(NetworkConstant.Bridge_arr_a3);
        netWorkBobyInfor.setParameters_value(new Object[]{user, divicenumber, cannne,callType});
        netWorkBobyInfor.setCallBack(new NetWorkBobyInfor.CallBack() {
            @Override
            public void setresult(int code, String data) {


            }
        });
        NetworkUtil.func_post(netWorkBobyInfor,BaseChatActivity.this);
    }


    /**
     *  发送推送 人推送给设备
     * @param relkey
     * @param divicenumber
     * @param cannel
     * @param callType
     */
    public void pushTuisongP2D(String relkey,String divicenumber,String cannel,String callType){
        NetWorkBobyInfor netWorkBobyInfor = new NetWorkBobyInfor();
        netWorkBobyInfor.setAction(1);
        netWorkBobyInfor.setIpaddress(NetworkConstant.Bridge);
        netWorkBobyInfor.setParameters(NetworkConstant.Bridge_arr_a1);
        netWorkBobyInfor.setParameters_value(new Object[]{divicenumber,relkey,cannel, callType});
        netWorkBobyInfor.setCallBack(new NetWorkBobyInfor.CallBack() {
            @Override
            public void setresult(int code, String data) {


            }
        });
        NetworkUtil.func_post(netWorkBobyInfor,BaseChatActivity.this);
    }

    /**
     * 人推送给人
     * @param relkey
     * @param user
     * @param cannel
     * @param callType
     */
    public void pushTuisongP2P(String relkey,String user,String cannel,String callType){
        NetWorkBobyInfor netWorkBobyInfor = new NetWorkBobyInfor();
        netWorkBobyInfor.setAction(2);
        netWorkBobyInfor.setIpaddress(NetworkConstant.Bridge);
        netWorkBobyInfor.setParameters(NetworkConstant.Bridge_arr_a2);
        netWorkBobyInfor.setParameters_value(new Object[]{user,relkey,cannel, callType});
        netWorkBobyInfor.setCallBack(new NetWorkBobyInfor.CallBack() {
            @Override
            public void setresult(int code, String data) {


            }
        });
        NetworkUtil.func_post(netWorkBobyInfor,BaseChatActivity.this);
    }

    Boolean closeTime=true;
    int hours = 0, min = 0, seco = 0;
    public void stopThreadTime(){
        closeTime=false;
    }
    public void setThreadTime(TextView textView){
        closeTime=true;
        hours = 0; min = 0; seco = 0;
        ThreadUtils.runOnSubThread(new Runnable() {
            @Override
            public void run() {
                while (closeTime){
                    ThreadUtils.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            textView.setText(parseTime());
                        }
                    });
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
    }

    Boolean DownCount=true;
    int count=0;
    public void stopCount(){
        DownCount=false;
    }
    public void  startCount(Context context){
        count=0;
        DownCount=true;
        ThreadUtils.runOnSubThread(new Runnable() {
            @Override
            public void run() {
                while (DownCount){
                    count++;
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if(count>=60){
                        DownCount=false;
                        ThreadUtils.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                SounPoilUtill.Getinstanc(context).playrool(3);
                                ThreadUtils.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ToastUtis("对方无人接听,请稍后再拨");
                                        finish();
                                    }
                                },1000);

                            }
                        });
                    }
                }
            }
        });

    }

    private String parseTime(){
            if(seco<60){
                seco++;
            }else{
                seco=0;
                if(min<60){
                    min++;
                }else{
                    hours++;
                    min=0;
                }

            }
            String secostr=""+seco;
            String minstr=""+min;
            String hourstr=""+hours;
            if(secostr.length()<2){
                secostr="0"+secostr;
            }
            if(minstr.length()<2){
                minstr="0"+minstr;
            }
            if(hourstr.length()<2){
                hourstr="0"+hourstr;
            }
            return hourstr+" : "+minstr+" : "+secostr;
    }




}
