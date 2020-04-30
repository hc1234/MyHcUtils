package com.hcutils.hclibrary.Chat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alivc.rtc.AliRtcEngine;
import com.alivc.rtc.AliRtcEngineEventListener;
import com.alivc.rtc.AliRtcEngineNotify;
import com.hcutils.hclibrary.Datautils.DataUtis;
import com.hcutils.hclibrary.Datautils.PermissionUtils;
import com.hcutils.hclibrary.R;
import com.hcutils.hclibrary.R2;
import com.hcutils.hclibrary.Utils.ThreadUtils;
import com.hcutils.hclibrary.views.CircleImageView;

import org.webrtc.alirtcInterface.AliParticipantInfo;
import org.webrtc.alirtcInterface.AliStatusInfo;
import org.webrtc.alirtcInterface.AliSubscriberInfo;
import org.webrtc.sdk.SophonSurfaceView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class VideoActivity extends BaseChatVideoActivity {
    ChartUserAdapter mUserListAdapter;
    @BindView(R2.id.content_frame_1)
    FrameLayout contentFrame1;
    @BindView(R2.id.content_frame_2)
    FrameLayout contentFrame2;
    @BindView(R2.id.content_relative)
    RelativeLayout contentRelative;
    @BindView(R2.id.person_image)
    CircleImageView personImage;
    @BindView(R2.id.person_image_line)
    LinearLayout personImageLine;
    @BindView(R2.id.person_name)
    TextView personName;
    @BindView(R2.id.chat_status)
    TextView chatStatus;
    @BindView(R2.id.chat_count)
    TextView chatCount;
    @BindView(R2.id.chat_voice)
    TextView chatVoice;
    @BindView(R2.id.chat_voice_line)
    LinearLayout chatVoiceLine;
    @BindView(R2.id.chat_guaduan)
    TextView chatGuaduan;
    @BindView(R2.id.chat_guaduan_line)
    LinearLayout chatGuaduanLine;
    @BindView(R2.id.chat_jieting)
    TextView chatJieting;
    @BindView(R2.id.chat_jieting_line)
    LinearLayout chatJietingLine;
    @BindView(R2.id.chat_swi)
    TextView chatSwi;
    @BindView(R2.id.chat_swi_line)
    LinearLayout chatSwiLine;
    @BindView(R2.id.head_line)
    LinearLayout headLine;
    String call_type;  //拨打 还是接听  come  go
    CallInfor callInfor; //拨打的时候传入 拨打信息
    String username;
    RTCInfor rtcInfor = new RTCInfor();
    //    @BindView(R.id.chart_content_userlist)
    RecyclerView chartUserListView;
    SophonSurfaceView oppositeSurface;
    Boolean isonline = false;  //对方是否在线



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("hcc", "video");
        setContentView(R.layout.activity_new_chat);
        ButterKnife.bind(this);
        isonline = false;
        registHOOK();
        call_type = getIntent().getStringExtra("type");
        callInfor = getIntent().getParcelableExtra("infor");
        setTypeUi("");
        chcekPermisson();
        if (!GetSpeakerphone()) {
            setSpeakerphone();
        }

        initview();
        // 承载远程User的Adapter
        mUserListAdapter = new ChartUserAdapter();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        chartUserListView.setLayoutManager(layoutManager);
        DefaultItemAnimator anim = new DefaultItemAnimator();
        anim.setSupportsChangeAnimations(false);
        chartUserListView.setItemAnimator(anim);
        chartUserListView.setAdapter(mUserListAdapter);
        mUserListAdapter.setItemOclick(new ChartUserAdapter.MyItemOclick() {
            @Override
            public void item() {
                if (big_local) {
                    big_local = !big_local;
                    swiSurface();
                }
            }
        });
    }

    Boolean big_local = true;

    private void initview() {
        chartUserListView = new RecyclerView(this);
        contentFrame2.addView(chartUserListView, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        oppositeSurface = new SophonSurfaceView(this);
        contentFrame1.addView(oppositeSurface, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        setLoadimage(callInfor.getMemo(), personImage);
        contentFrame2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!big_local) {
                    big_local = !big_local;
                    swiSurface();
                }
            }
        });

    }

    private void swiSurface() {
        Log.i("hcc", "chartUserListView==" + big_local);
        contentFrame2.removeAllViews();
        contentFrame1.removeAllViews();
        if (big_local) {
            contentFrame1.addView(oppositeSurface, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
            contentFrame2.addView(chartUserListView, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        } else {
            contentFrame1.addView(chartUserListView, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
            contentFrame2.addView(oppositeSurface, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
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
            }
        });
    }


    /**
     * 获取信息
     */
    private void getdata() {
        if (callInfor == null) {
            Toast.makeText(VideoActivity.this, "信息不完整", Toast.LENGTH_SHORT).show();
            return;
        }
        if (call_type.equals("go")) {
            if (callInfor.getFrom().equals("") || callInfor.getTo().equals("") || callInfor.getType().equals("")) {
                Toast.makeText(VideoActivity.this, "拨打信息不完整", Toast.LENGTH_SHORT).show();
                return;
            }
            username = callInfor.getTo_name();
            getCallInfor(new NetWorkResult() {
                @Override
                public void result(int code, String data) {
                    if (VideoActivity.this != null && !VideoActivity.this.isFinishing()) {
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
                    if (VideoActivity.this != null && !VideoActivity.this.isFinishing()) {
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


        if (!DataUtis.isEmuis(rtcInfor)) {
            ToastUtis("获取信息有误");
            finish();
            return;
        }
        if (mAliRtcEngine != null) {
            mAliRtcEngine.setRtcEngineEventListener(mEventListener);
            mAliRtcEngine.setRtcEngineNotify(mEngineNotify);
            setConnect(true);
////
        }
    }

    /**
     * 开始接听
     */
    private void startToAnswer() {


        if (!DataUtis.isEmuis(rtcInfor)) {
            ToastUtis("获取信息有误");
            return;
        }
        if (mAliRtcEngine != null) {
            mAliRtcEngine.setRtcEngineEventListener(mEventListener);
            mAliRtcEngine.setRtcEngineNotify(mEngineNotify);
            setConnect(false);
        }
    }

    @OnClick({R2.id.chat_voice, R2.id.chat_guaduan, R2.id.chat_jieting, R2.id.chat_swi})
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.chat_voice) {
            setSpeakerphone();
        } else if (id == R.id.chat_guaduan) {
            finish();
        } else if (id == R.id.chat_jieting) {
            stopMusic();
            setGetVideo(rtcInfor.getFrom());
        } else if (id == R.id.chat_swi) {
            swiCamer();
        }
    }

    /**
     * 加入频道 自动发布订阅
     */
    public void setConnect(Boolean iscall) {
        contentRelative.setVisibility(View.VISIBLE);
        initLocalView(oppositeSurface);
        startPreview();
        ThreadUtils.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                joinChannel(rtcInfor, iscall);
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
            palyComeMusic();
            isonline3s();
            chatStatus.setText("您有一个视频电话");
            personName.setText(username);
            personImageLine.setVisibility(View.VISIBLE);
            personName.setVisibility(View.VISIBLE);
            chatGuaduanLine.setVisibility(View.VISIBLE);
            chatJietingLine.setVisibility(View.VISIBLE);
        } else if (typeUi.equals("go")) {
            palyCallMusic();
            chatStatus.setText("正在拨打视频中，请稍后...");
            personName.setText(username);
            personImageLine.setVisibility(View.VISIBLE);
            personName.setVisibility(View.VISIBLE);
            chatGuaduanLine.setVisibility(View.VISIBLE);
        } else if (typeUi.equals("conect")) {
            headLine.setVisibility(View.GONE);
            chatJietingLine.setVisibility(View.GONE);

            if(callInfor.getPush_type()!=null&&callInfor.getPush_type().equals("D2P")){
                chatSwiLine.setVisibility(View.GONE);
                chatVoiceLine.setVisibility(View.GONE);
            }else {
                chatSwiLine.setVisibility(View.VISIBLE);
                chatVoiceLine.setVisibility(View.VISIBLE);
            }
            chatCount.setVisibility(View.VISIBLE);
            stopCount();
            ThreadUtils.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    big_local = false;
                    swiSurface();
                }
            }, 300);

        } else {
            contentRelative.setVisibility(View.GONE);
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
                } else {
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

                ThreadUtils.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateRemoteDisplay(s, aliRtcAudioTrack, aliRtcVideoTrack, mUserListAdapter);
                        stopMusic();
                        setTypeUi("conect");
                        setThreadTime(chatCount);
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
            Log.i("hcc","onPerformanceLow===当前设备性能不足");
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
            isonline = true;

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
            updateRemoteDisplay(s, aliRtcAudioTrack, aliRtcVideoTrack, mUserListAdapter);
//            updateRemoteDisplay(s, aliRtcAudioTrack, aliRtcVideoTrack);
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

        @Override
        public void onFirstLocalVideoFrameDrawn() {
            Log.i("hcc","onFirstLocalVideoFrameDrawn==本地预览");
            super.onFirstLocalVideoFrameDrawn();
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

    @Override
    protected void onDestroy() {
        isonline = true;
        if(HOOKReceview!=null){
            unregisterReceiver(HOOKReceview);
        }
        super.onDestroy();

    }

    int online_count = 0;

    private void isonline3s() {
        ThreadUtils.runOnSubThread(new Runnable() {
            @Override
            public void run() {
                while (!isonline) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    online_count++;
                    if (online_count >= 3) {
                        ThreadUtils.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtis("对方已挂断");
                                finish();
                            }
                        });
                    }
                }
            }
        });

    }


}
