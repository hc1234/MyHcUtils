package com.hcutils.hcutils.Chat;

import android.os.Parcel;
import android.os.Parcelable;

public class RTCInfor implements Parcelable {
    public int code;
    public String appid;
    public String userid;
    public String nonce;
    public long timestamp;
    public String token;
    public String username;
    public String password;
    public String[] gslb;
    public String key;
    public String channel;
    public String from;
    public String to;



   public RTCInfor(){

    }

    protected RTCInfor(Parcel in) {
        code = in.readInt();
        appid = in.readString();
        userid = in.readString();
        nonce = in.readString();
        timestamp = in.readLong();
        token = in.readString();
        username = in.readString();
        password = in.readString();
        gslb = in.createStringArray();
        key = in.readString();
        channel = in.readString();
        from = in.readString();
        to = in.readString();
    }

    public static final Creator<RTCInfor> CREATOR = new Creator<RTCInfor>() {
        @Override
        public RTCInfor createFromParcel(Parcel in) {
            return new RTCInfor(in);
        }

        @Override
        public RTCInfor[] newArray(int size) {
            return new RTCInfor[size];
        }
    };

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String[] getGslb() {
        return gslb;
    }

    public void setGslb(String[] gslb) {
        this.gslb = gslb;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(code);
        dest.writeString(appid);
        dest.writeString(userid);
        dest.writeString(nonce);
        dest.writeLong(timestamp);
        dest.writeString(token);
        dest.writeString(username);
        dest.writeString(password);
        dest.writeStringArray(gslb);
        dest.writeString(key);
        dest.writeString(channel);
        dest.writeString(from);
        dest.writeString(to);
    }
}
