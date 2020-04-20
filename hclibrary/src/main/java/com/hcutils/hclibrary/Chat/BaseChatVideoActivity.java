package com.hcutils.hclibrary.Chat;

import android.graphics.PixelFormat;

import com.alivc.rtc.AliRtcEngine;
import com.alivc.rtc.AliRtcRemoteUserInfo;

import org.webrtc.sdk.SophonSurfaceView;

import static com.alivc.rtc.AliRtcEngine.AliRtcRenderMode.AliRtcRenderModeAuto;
import static com.alivc.rtc.AliRtcEngine.AliRtcVideoTrack.AliRtcVideoTrackBoth;
import static com.alivc.rtc.AliRtcEngine.AliRtcVideoTrack.AliRtcVideoTrackCamera;
import static com.alivc.rtc.AliRtcEngine.AliRtcVideoTrack.AliRtcVideoTrackNo;
import static com.alivc.rtc.AliRtcEngine.AliRtcVideoTrack.AliRtcVideoTrackScreen;

public class BaseChatVideoActivity extends BaseChatActivity {



    /**
     * 设置本地的预览视图的view
     */
    public void initLocalView(SophonSurfaceView mLocalView) {

        mLocalView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
        mLocalView.setZOrderOnTop(false);
        mLocalView.setZOrderMediaOverlay(false);
        AliRtcEngine.AliVideoCanvas aliVideoCanvas = new AliRtcEngine.AliVideoCanvas();
        aliVideoCanvas.view = mLocalView;
        aliVideoCanvas.renderMode = AliRtcRenderModeAuto;
        if (mAliRtcEngine != null) {
            mAliRtcEngine.configLocalCameraPublish(true); //允许发布视频
            mAliRtcEngine.configLocalAudioPublish(true); //允许发布音频
            mAliRtcEngine.setLocalViewConfig(aliVideoCanvas, AliRtcVideoTrackCamera);
        }
    }

    /**
     * 切换 网络和本地界面
     * @param mLocalView
     */
    public void swiLocalview(SophonSurfaceView mLocalView){
        if (mAliRtcEngine != null) {
            AliRtcEngine.AliVideoCanvas aliVideoCanvas = new AliRtcEngine.AliVideoCanvas();
            aliVideoCanvas.view = mLocalView;
            aliVideoCanvas.renderMode = AliRtcRenderModeAuto;
            mAliRtcEngine.setLocalViewConfig(aliVideoCanvas, AliRtcVideoTrackCamera);
        }
    }


    public void updateRemoteDisplay(String uid, AliRtcEngine.AliRtcAudioTrack at, AliRtcEngine.AliRtcVideoTrack vt, ChartUserAdapter mUserListAdapter) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (null == mAliRtcEngine) {
                    return;
                }
                AliRtcRemoteUserInfo remoteUserInfo = mAliRtcEngine.getUserInfo(uid);
                // 如果没有，说明已经退出了或者不存在。则不需要添加，并且删除
                if (remoteUserInfo == null) {
                    // remote user exit room
                    return;
                }
                //change
                AliRtcEngine.AliVideoCanvas cameraCanvas = remoteUserInfo.getCameraCanvas();
                AliRtcEngine.AliVideoCanvas screenCanvas = remoteUserInfo.getScreenCanvas();
                //视频情况
                if (vt == AliRtcVideoTrackNo) {
                    //没有视频流
                    cameraCanvas = null;
                    screenCanvas = null;
                } else if (vt == AliRtcVideoTrackCamera) {
                    //相机流
                    screenCanvas = null;
                    cameraCanvas = createCanvasIfNull(cameraCanvas);
                    //SDK内部提供进行播放的view
                    mAliRtcEngine.setRemoteViewConfig(cameraCanvas, uid, AliRtcVideoTrackCamera);
                } else if (vt == AliRtcVideoTrackScreen) {
                    //屏幕流
                    cameraCanvas = null;
                    screenCanvas = createCanvasIfNull(screenCanvas);
                    //SDK内部提供进行播放的view
                    mAliRtcEngine.setRemoteViewConfig(screenCanvas, uid, AliRtcVideoTrackScreen);
                } else if (vt == AliRtcVideoTrackBoth) {
                    //多流
                    cameraCanvas = createCanvasIfNull(cameraCanvas);
                    //SDK内部提供进行播放的view
                    mAliRtcEngine.setRemoteViewConfig(cameraCanvas, uid, AliRtcVideoTrackCamera);
                    screenCanvas = createCanvasIfNull(screenCanvas);
                    //SDK内部提供进行播放的view
                    mAliRtcEngine.setRemoteViewConfig(screenCanvas, uid, AliRtcVideoTrackScreen);
                } else {
                    return;
                }
                ChartUserBean chartUserBean = convertRemoteUserInfo(remoteUserInfo, cameraCanvas, screenCanvas,mUserListAdapter);
                mUserListAdapter.updateData(chartUserBean, true);

            }
        });
    }

    private ChartUserBean convertRemoteUserToUserData(AliRtcRemoteUserInfo remoteUserInfo,ChartUserAdapter mUserListAdapter) {
        String uid = remoteUserInfo.getUserID();
        ChartUserBean ret = mUserListAdapter.createDataIfNull(uid);
        ret.mUserId = uid;
        ret.mUserName = remoteUserInfo.getDisplayName();
        ret.mIsCameraFlip = false;
        ret.mIsScreenFlip = false;
        return ret;
    }

    private AliRtcEngine.AliVideoCanvas createCanvasIfNull(AliRtcEngine.AliVideoCanvas canvas) {
        if (canvas == null || canvas.view == null) {
            //创建canvas，Canvas为SophonSurfaceView或者它的子类
            canvas = new AliRtcEngine.AliVideoCanvas();
            SophonSurfaceView surfaceView = new SophonSurfaceView(this);
            surfaceView.setZOrderOnTop(false);
            surfaceView.setZOrderMediaOverlay(false);
            canvas.view = surfaceView;
            //renderMode提供四种模式：Auto、Stretch、Fill、Crop，建议使用Auto模式。
            canvas.renderMode = AliRtcRenderModeAuto;
        }
        return canvas;
    }

    private ChartUserBean convertRemoteUserInfo(AliRtcRemoteUserInfo remoteUserInfo,
                                                AliRtcEngine.AliVideoCanvas cameraCanvas,
                                                AliRtcEngine.AliVideoCanvas screenCanvas,ChartUserAdapter mUserListAdapter) {
        String uid = remoteUserInfo.getUserID();
        ChartUserBean ret = mUserListAdapter.createDataIfNull(uid);
        ret.mUserId = remoteUserInfo.getUserID();

        ret.mUserName = remoteUserInfo.getDisplayName();

        ret.mCameraSurface = cameraCanvas != null ? cameraCanvas.view : null;
        ret.mIsCameraFlip = cameraCanvas != null && cameraCanvas.mirrorMode == AliRtcEngine.AliRtcRenderMirrorMode.AliRtcRenderMirrorModeAllEnabled;

        ret.mScreenSurface = screenCanvas != null ? screenCanvas.view : null;
        ret.mIsScreenFlip = screenCanvas != null && screenCanvas.mirrorMode == AliRtcEngine.AliRtcRenderMirrorMode.AliRtcRenderMirrorModeAllEnabled;

        return ret;
    }

    /**
     * 切换摄像头
     */
    public void swiCamer(){
        if(mAliRtcEngine!=null) {
            mAliRtcEngine.switchCamera();
        }
    }

    /**
     * 开始预览本地界面
     */
    public void startPreview() {
        if (mAliRtcEngine == null) {
            return;
        }
        try {
            mAliRtcEngine.startPreview();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
