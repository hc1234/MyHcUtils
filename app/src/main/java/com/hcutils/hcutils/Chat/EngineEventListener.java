package com.hcutils.hcutils.Chat;

import com.alivc.rtc.AliRtcEngine;

public interface EngineEventListener {

    void onJoinChannelResult(int i);
    void onLeaveChannelResult(int i);
    void onPublishResult(int i, String s);
    void onUnpublishResult(int i);
    void onSubscribeResult(String s, int i, AliRtcEngine.AliRtcVideoTrack aliRtcVideoTrack,
                           AliRtcEngine.AliRtcAudioTrack aliRtcAudioTrack);
    void onUnsubscribeResult(int i, String s);
    void onNetworkQualityChanged(String s, AliRtcEngine.AliRtcNetworkQuality aliRtcNetworkQuality, AliRtcEngine.AliRtcNetworkQuality aliRtcNetworkQuality1);
    void onOccurWarning(int i);
    void onOccurError(int error);
    void onPerformanceLow();
    void onPermormanceRecovery();
    void onConnectionLost();
}
