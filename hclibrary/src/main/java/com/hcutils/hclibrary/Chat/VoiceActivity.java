package com.hcutils.hclibrary.Chat;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.alivc.rtc.AliRtcEngine;
import com.alivc.rtc.AliRtcEngineEventListener;
import com.alivc.rtc.AliRtcEngineNotify;
import com.hcutils.hclibrary.Datautils.DataUtis;
import com.hcutils.hclibrary.Datautils.PermissionUtils;
import com.hcutils.hclibrary.R;
import com.hcutils.hclibrary.R2;
import com.hcutils.hclibrary.Utils.ThreadUtils;

import org.webrtc.alirtcInterface.AliParticipantInfo;
import org.webrtc.alirtcInterface.AliStatusInfo;
import org.webrtc.alirtcInterface.AliSubscriberInfo;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class VoiceActivity extends BaseChatActivity {

    @BindView(R2.id.voice_touser_personimage)
    ImageView voiceTouserPersonimage;
    @BindView(R2.id.voice_touser_name)
    TextView voiceTouserName;
    @BindView(R2.id.voice_touser_type)
    TextView voiceTouserType;
    @BindView(R2.id.voice_time)
    TextView voiceTime;
    @BindView(R2.id.voice_touser_line)
    LinearLayout voiceTouserLine;
    @BindView(R2.id.voice_checkvoice)
    CheckBox voiceCheckvoice;
    @BindView(R2.id.voicejieshou)
    TextView voicejieshou;
    @BindView(R2.id.voice_cancel)
    TextView voiceCancel;


    String call_type;  //拨打 还是接听  come  go
    CallInfor callInfor; //拨打的时候传入 拨打信息
    String username;
    RTCInfor rtcInfor = new RTCInfor();
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice);
        ButterKnife.bind(this);
        call_type = getIntent().getStringExtra("type");
        callInfor = getIntent().getParcelableExtra("infor");
        setTypeUi("");
        voiceTouserType.setText("正在获取信息中");
        voiceTouserName.setText("");
        chcekPermisson();
        if (!GetSpeakerphone()) {
            setSpeakerphone();
        }

    }

    /**
     * 检查权限
     */
    public void chcekPermisson() {
        setUpSplash(new PermissionUtils.PermissionGrant() {
            @Override
            public void onPermissionGranted(int requestCode) { //权限成功
                Log.i("hcc", "权限成功");
                getdata();
            }

            @Override
            public void onPermissionCancel() { //权限失败
                Log.i("hcc", "权限失败");
                finish();
            }
        });
    }


    @OnClick({R2.id.voice_checkvoice, R2.id.voicejieshou, R2.id.voice_cancel})
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.voice_checkvoice) {
            setSpeakerphone();
        } else if (id == R.id.voicejieshou) {
            stopMusic();
            setGetVideo(rtcInfor.getFrom());
        } else if (id == R.id.voice_cancel) {
            finish();
        }
    }

    /**
     * 获取信息
     */
    private void getdata() {
        if (callInfor == null) {
            Toast.makeText(VoiceActivity.this, "信息不完整", Toast.LENGTH_SHORT).show();
            return;
        }
        if (call_type.equals("go")) {
            if (callInfor.getFrom().equals("") || callInfor.getTo().equals("") || callInfor.getType().equals("")) {
                Toast.makeText(VoiceActivity.this, "拨打信息不完整", Toast.LENGTH_SHORT).show();
                return;
            }
            username = callInfor.getTo_name();
            getCallInfor(new NetWorkResult() {
                @Override
                public void result(int code, String data) {
                    if (VoiceActivity.this != null && !VoiceActivity.this.isFinishing()) {
                        if (code == 0) {
                            rtcInfor = DataUtis.parseToJson(data);
                            rtcInfor.setUsername(username);
                            startToCall();
                        } else {
                            ToastUtis("信息获取失败");
                            finish();
                        }

                    }
                }
            }, callInfor);
        } else {
            if (callInfor.getChannel().equals("")) {
                ToastUtis("频道信息不能为空");
                return;
            }
            username = callInfor.getFrom_name();
            getAnswerInfor(new NetWorkResult() {
                @Override
                public void result(int code, String data) {
                    if (VoiceActivity.this != null && !VoiceActivity.this.isFinishing()) {
                        if (code == 0) {
                            rtcInfor = DataUtis.parseToJson(data);
                                rtcInfor.setUsername(username);
                                startToAnswer();

                        } else {
                            ToastUtis("信息获取失败");
                            finish();
                        }

                    }
                }
            }, callInfor.getChannel());

        }
    }


    /**
     * 开始拨打
     */
    private void startToCall() {
        palyCallMusic();
        voiceTouserType.setText("正在拨打中，请稍后...");
        voiceTouserName.setText(username);
        if (!DataUtis.isEmuis(rtcInfor)) {
            ToastUtis("获取信息有误");
            finish();
            return;
        }
        if (mAliRtcEngine != null) {
            mAliRtcEngine.setRtcEngineEventListener(mEventListener);
            mAliRtcEngine.setRtcEngineNotify(mEngineNotify);
            setConnect();
////
        }
    }

    /**
     * 开始接收
     */
    private void startToAnswer() {

        palyComeMusic();
        voiceTouserType.setText("您有一个语音来电");
        voiceTouserName.setText(username);
        if (!DataUtis.isEmuis(rtcInfor)) {
            ToastUtis("获取信息有误");
            return;
        }
        if (mAliRtcEngine != null) {
            mAliRtcEngine.setRtcEngineEventListener(mEventListener);
            mAliRtcEngine.setRtcEngineNotify(mEngineNotify);
            setConnect();
        }
    }

    /**
     * 加入频道 自动发布订阅
     */
    public void setConnect() {
        ThreadUtils.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                joinChannel(rtcInfor,true);
            }
        }, 100);
    }

    /**
     * 底部UI变化刷新
     *
     * @param typeUi
     */
    public void setTypeUi(String typeUi) {

        if (typeUi.equals("come")) {
            if(!isOnline(rtcInfor.getFrom())){
                ToastUtis("对方已挂断");
                finish();
                return;
            }
            voicejieshou.setVisibility(View.VISIBLE);
            voiceCancel.setVisibility(View.VISIBLE);
        } else if (typeUi.equals("go")) {
            voicejieshou.setVisibility(View.GONE);
            voiceCancel.setVisibility(View.VISIBLE);
        } else if (typeUi.equals("conect")) {
            stopCount();
            voiceTouserLine.setVisibility(View.VISIBLE);
            voiceTouserType.setText("正在通话中");
            voicejieshou.setVisibility(View.GONE);
            voiceCancel.setVisibility(View.VISIBLE);
        }else{
            voicejieshou.setVisibility(View.GONE);
            voiceCancel.setVisibility(View.GONE);
        }
    }

    /**
     * 用户操作回调监听(回调接口都在子线程)
     */
    private AliRtcEngineEventListener mEventListener = new AliRtcEngineEventListener() {

        /**
         * 加入房间的回调
         * @param i 结果码
         */
        @Override
        public void onJoinChannelResult(int i) {
            Log.i("hcc", "i===" + i);
            runOnUiThread(() -> {
                if (i == 0) {
                    setTypeUi(call_type);
                    //加入频道成功
                    startServer();
                }else{
                    ToastUtis("通话连接失败");
                    finish();
                }
            });
        }

        /**
         * 离开房间的回调
         * @param i 结果码
         */
        @Override
        public void onLeaveChannelResult(int i) {

        }

        /**
         * 推流的回调
         * @param i 结果码
         * @param s publishId
         */
        @Override
        public void onPublishResult(int i, String s) { //
            Log.i("hcc", "onPublishResult" + i);

        }

        /**
         * 取消发布本地流回调
         * @param i 结果码
         */
        @Override
        public void onUnpublishResult(int i) {

        }

        /**
         * 订阅成功的回调
         * @param s userid
         * @param i 结果码
         * @param aliRtcVideoTrack 视频的track
         * @param aliRtcAudioTrack 音频的track
         */
        @Override
        public void onSubscribeResult(String s, int i, AliRtcEngine.AliRtcVideoTrack aliRtcVideoTrack,
                                      AliRtcEngine.AliRtcAudioTrack aliRtcAudioTrack) {
            Log.i("hcc", "订阅==" + i + "  " + s);
            if (i == 0) {
                setThreadTime(voiceTime);
                ThreadUtils.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        stopMusic();
                        setTypeUi("conect");
                    }
                });

            }
        }

        /**
         * 取消的回调
         * @param i 结果码
         * @param s userid
         */
        @Override
        public void onUnsubscribeResult(int i, String s) {
        }

        /**
         * 网络状态变化的回调
         * @param aliRtcNetworkQuality
         */
        @Override
        public void onNetworkQualityChanged(String s, AliRtcEngine.AliRtcNetworkQuality aliRtcNetworkQuality, AliRtcEngine.AliRtcNetworkQuality aliRtcNetworkQuality1) {

        }

        /**
         * 出现警告的回调
         * @param i
         */
        @Override
        public void onOccurWarning(int i) {

        }

        /**
         * 出现错误的回调
         * @param error 错误码
         */
        @Override
        public void onOccurError(int error) {
            Log.i("hcc", "onOccurError==");
            //错误处理
//            processOccurError(error);
        }

        /**
         * 当前设备性能不足
         */
        @Override
        public void onPerformanceLow() {

        }

        /**
         * 当前设备性能恢复
         */
        @Override
        public void onPermormanceRecovery() {

        }

        /**
         * 连接丢失
         */
        @Override
        public void onConnectionLost() {
            Log.i("hcc", "onConnectionLost==");
        }

        /**
         * 尝试恢复连接
         */
        @Override
        public void onTryToReconnect() {

        }

        /**
         * 连接已恢复
         */
        @Override
        public void onConnectionRecovery() {

        }


    };

    /**
     * SDK事件通知(回调接口都在子线程)
     */
    private AliRtcEngineNotify mEngineNotify = new AliRtcEngineNotify() {
        /**
         * 远端用户停止发布通知，处于OB（observer）状态
         * @param aliRtcEngine 核心引擎对象
         * @param s userid
         */
        @Override
        public void onRemoteUserUnPublish(AliRtcEngine aliRtcEngine, String s) {
//            updateRemoteDisplay(s, AliRtcAudioTrackNo, AliRtcVideoTrackNo);
            Log.i("hcc", "onRemoteUserUnPublish==" + s);
        }

        /**
         * 远端用户上线通知
         * @param s userid
         */
        @Override
        public void onRemoteUserOnLineNotify(String s) {
            Log.i("hcc", "onRemoteUserOnLineNotify==" + s);

//            addRemoteUser(s);
        }

        /**
         * 远端用户下线通知
         * @param s userid
         */
        @Override
        public void onRemoteUserOffLineNotify(String s) {
            Log.i("hcc", "远端用户下线通知==" + s);
            finish();
//            removeRemoteUser(s);
        }

        /**
         * 远端用户发布音视频流变化通知
         * @param s userid
         * @param aliRtcAudioTrack 音频流
         * @param aliRtcVideoTrack 相机流
         */
        @Override
        public void onRemoteTrackAvailableNotify(String s, AliRtcEngine.AliRtcAudioTrack aliRtcAudioTrack,
                                                 AliRtcEngine.AliRtcVideoTrack aliRtcVideoTrack) {
            Log.i("hcc", "远端用户发布音视频流变化通知==" + s);
        }

        /**
         * 订阅流回调，可以做UI及数据的更新
         * @param s userid
         * @param aliRtcAudioTrack 音频流
         * @param aliRtcVideoTrack 相机流
         */
        @Override
        public void onSubscribeChangedNotify(String s, AliRtcEngine.AliRtcAudioTrack aliRtcAudioTrack,
                                             AliRtcEngine.AliRtcVideoTrack aliRtcVideoTrack) {
            Log.i("hcc", "订阅流回调==" + s);

        }

        /**
         * 订阅信息
         * @param aliSubscriberInfos 订阅自己这边流的user信息
         * @param i 当前订阅人数
         */
        @Override
        public void onParticipantSubscribeNotify(AliSubscriberInfo[] aliSubscriberInfos, int i) {
            Log.i("hcc", "订阅信息==" + i);
            if (i > 0) {
                if (call_type.equals("go")) {
                    setGetVoice(aliSubscriberInfos[0].user_id);
                }
            }


        }

        /**
         * 首帧的接收回调
         * @param s callId
         * @param s1 stream_label
         * @param s2 track_label 分为video和audio
         * @param i 时间
         */
        @Override
        public void onFirstFramereceived(String s, String s1, String s2, int i) {

            ThreadUtils.runOnUiThread(new Runnable() {
                @Override
                public void run() {
//                    Toast.makeText(AliRtcChatActivity.this,"首帧接受成功",Toast.LENGTH_SHORT).show();

                }
            });


        }

        /**
         * 首包的发送回调
         * @param s callId
         * @param s1 stream_label
         * @param s2 track_label 分为video和audio
         * @param i 时间
         */
        @Override
        public void onFirstPacketSent(String s, String s1, String s2, int i) {
            Log.i("hcc", "onFirstPacketSent" + i);
//            ThreadUtils.runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    Toast.makeText(VideoActivity.this,"首包发送成功",Toast.LENGTH_SHORT).show();
//
//                }
//            });
        }

        /**
         *首包数据接收成功
         * @param callId 远端用户callId
         * @param streamLabel 远端用户的流标识
         * @param trackLabel 远端用户的媒体标识
         * @param timeCost 耗时
         */
        @Override
        public void onFirstPacketReceived(String callId, String streamLabel, String trackLabel, int timeCost) {

        }

        /**
         * 取消订阅信息回调
         * @param aliParticipantInfos 订阅自己这边流的user信息
         * @param i 当前订阅人数
         */
        @Override
        public void onParticipantUnsubscribeNotify(AliParticipantInfo[] aliParticipantInfos, int i) {

        }

        /**
         * 被服务器踢出或者频道关闭时回调
         * @param i
         */
        @Override
        public void onBye(int i) {

        }

        @Override
        public void onParticipantStatusNotify(AliStatusInfo[] aliStatusInfos, int i) {

        }

        /**
         * @param aliRtcStats
         * 实时数据回调(2s触发一次)
         */
//        @Override
//        public void onAliRtcStats(ALI_RTC_INTERFACE.AliRtcStats aliRtcStats) {
//
//        }
    };


}
