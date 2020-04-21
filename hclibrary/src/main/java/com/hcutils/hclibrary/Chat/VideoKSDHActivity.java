package com.hcutils.hclibrary.Chat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import org.webrtc.alirtcInterface.AliParticipantInfo;
import org.webrtc.alirtcInterface.AliStatusInfo;
import org.webrtc.alirtcInterface.AliSubscriberInfo;
import org.webrtc.sdk.SophonSurfaceView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class VideoKSDHActivity extends BaseChatVideoActivity {

    @BindView(R2.id.content_frame_1)
    FrameLayout contentFrame1;
    @BindView(R2.id.content_frame_2)
    FrameLayout contentFrame2;
    @BindView(R2.id.video_touser_personimage)
    ImageView videoTouserPersonimage;
    @BindView(R2.id.video_touser_name)
    TextView videoTouserName;
    @BindView(R2.id.video_touser_type)
    TextView videoTouserType;
    @BindView(R2.id.video_touser_line)
    LinearLayout videoTouserLine;
    @BindView(R2.id.video_jieshou)
    TextView videoJieshou;
    @BindView(R2.id.video_cancel)
    TextView videoCancel;
    @BindView(R2.id.video_time)
    TextView videoTime;

    String call_type;  //拨打 还是接听  come  go
    CallInfor callInfor; //拨打的时候传入 拨打信息
    String username;
    RTCInfor rtcInfor = new RTCInfor();
    //    @BindView(R.id.chart_content_userlist)
    RecyclerView chartUserListView;
    SophonSurfaceView oppositeSurface;
    ChartUserAdapter mUserListAdapter;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_ksdh);
        ButterKnife.bind(this);

        call_type = getIntent().getStringExtra("type");
        callInfor = getIntent().getParcelableExtra("infor");
        setTypeUi("");
        registHOOK();
        videoTouserType.setText("正在获取信息中");
        videoTouserName.setText("");
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
            Toast.makeText(VideoKSDHActivity.this, "信息不完整", Toast.LENGTH_SHORT).show();
            return;
        }
        if (call_type.equals("go")) {
            if (callInfor.getFrom().equals("") || callInfor.getTo().equals("") || callInfor.getType().equals("")) {
                Toast.makeText(VideoKSDHActivity.this, "拨打信息不完整", Toast.LENGTH_SHORT).show();
                return;
            }
            username = callInfor.getTo_name();
            getCallInfor(new NetWorkResult() {
                @Override
                public void result(int code, String data) {
                    if (VideoKSDHActivity.this != null && !VideoKSDHActivity.this.isFinishing()) {
                        if (code == 0) {
                            rtcInfor = DataUtis.parseToJson(data);
                            rtcInfor.setUsername(username);
                            setTypeUi(call_type);
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
                    if (VideoKSDHActivity.this != null && !VideoKSDHActivity.this.isFinishing()) {
                        if (code == 0) {
                            setTypeUi(call_type);
                            rtcInfor = DataUtis.parseToJson(data);
                            if (isOnline(rtcInfor.getFrom())) {
                                rtcInfor.setUsername(username);
                                startToAnswer();
                            } else {
                                ToastUtis("通话已经结束");
                                finish();
                            }

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
        videoTouserType.setText("正在拨打中，请稍后...");
        videoTouserName.setText(username);
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
     * 开始拨打
     */
    private void startToAnswer() {

        palyComeMusic();
        videoTouserType.setText("您有一个视频来电");
        videoTouserName.setText(username);
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


    @OnClick({R2.id.video_jieshou, R2.id.video_cancel})
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.video_jieshou) {
            stopMusic();
            setGetVideo(rtcInfor.getFrom());
        } else if (id == R.id.video_cancel) {
            finish();
        }
    }


    /**
     * 加入频道 自动发布订阅
     */
    public void setConnect() {
        initLocalView(oppositeSurface);
        startPreview();
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
            videoJieshou.setVisibility(View.VISIBLE);
            videoCancel.setVisibility(View.VISIBLE);
        } else if (typeUi.equals("go")) {
            videoJieshou.setVisibility(View.GONE);
            videoCancel.setVisibility(View.VISIBLE);
        } else if (typeUi.equals("conect")) {
            videoTouserLine.setVisibility(View.GONE);
            videoJieshou.setVisibility(View.GONE);
            videoCancel.setVisibility(View.VISIBLE);
            stopCount();
            ThreadUtils.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    big_local = false;
                    swiSurface();
                }
            }, 500);

        }else{
            videoJieshou.setVisibility(View.GONE);
            videoCancel.setVisibility(View.GONE);
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
                    //加入频道成功
                    startServer();
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
                setThreadTime(videoTime);
                ThreadUtils.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateRemoteDisplay(s, aliRtcAudioTrack, aliRtcVideoTrack, mUserListAdapter);
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
            if (i > 0) {
                if (call_type.equals("go")) {
                    setGetVideo(aliSubscriberInfos[0].user_id);
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

    public void registHOOK(){
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction("zicoo.tele.HOOK_CTRL");
        registerReceiver(HOOKReceview,intentFilter);
    }

    @Override
    protected void onDestroy() {
        if(HOOKReceview!=null){
            unregisterReceiver(HOOKReceview);
        }
        super.onDestroy();
    }

    BroadcastReceiver HOOKReceview =new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent!=null){
                int hooktype=intent.getIntExtra("hookstatus",0);  // 0 听筒放下  1 听筒拿起
                if(hooktype==0){
                    if(!GetSpeakerphone()){
                        setSpeakerphone();
                    }
                }else {
                    if(GetSpeakerphone()){
                        setSpeakerphone();
                    }
                }

            }

        }
    };

}
