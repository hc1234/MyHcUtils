package com.hcutils.hclibrary.Chat;

import android.os.Parcel;
import android.os.Parcelable;

public class CallInfor implements Parcelable {

    String device;   //设备key
    String from;  //拨打人key
    String from_name; //拨打人名字
    String to;  //接收人key
    String to_name; //接收人姓名
    String type;  // 通话类型  0是语音 1是视频
    String memo;
    String channel; //频道
    String relkey;
    String push_type;//推送类型  P2P人推送给人 P2D人推送给设备 D2P设备推送给人

    public CallInfor(){

    }

    protected CallInfor(Parcel in) {
        device = in.readString();
        from = in.readString();
        from_name = in.readString();
        to = in.readString();
        to_name = in.readString();
        type = in.readString();
        memo = in.readString();
        channel = in.readString();
        relkey = in.readString();
        push_type = in.readString();
    }

    public static final Creator<CallInfor> CREATOR = new Creator<CallInfor>() {
        @Override
        public CallInfor createFromParcel(Parcel in) {
            return new CallInfor(in);
        }

        @Override
        public CallInfor[] newArray(int size) {
            return new CallInfor[size];
        }
    };

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getFrom_name() {
        return from_name;
    }

    public void setFrom_name(String from_name) {
        this.from_name = from_name;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getTo_name() {
        return to_name;
    }

    public void setTo_name(String to_name) {
        this.to_name = to_name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getRelkey() {
        return relkey;
    }

    public void setRelkey(String relkey) {
        this.relkey = relkey;
    }

    public String getPush_type() {
        return push_type;
    }

    public void setPush_type(String push_type) {
        this.push_type = push_type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(device);
        dest.writeString(from);
        dest.writeString(from_name);
        dest.writeString(to);
        dest.writeString(to_name);
        dest.writeString(type);
        dest.writeString(memo);
        dest.writeString(channel);
        dest.writeString(relkey);
        dest.writeString(push_type);
    }
}
