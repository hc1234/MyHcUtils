package com.hcutils.hclibrary.Datautils;

import android.util.Log;

import com.hcutils.hclibrary.Chat.RTCInfor;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DataUtis {

    public static long getStringToDate() {
        String dateString=System.currentTimeMillis()+"";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        try {
            date = dateFormat.parse(dateString);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return date.getTime();
    }


    public static RTCInfor parseToJson(String data){
        RTCInfor rtcInfor=new RTCInfor();
        try {
            JSONObject jb = new JSONObject(data);
            JSONObject jb_data = jb.getJSONObject("data");
            rtcInfor.setAppid(jb_data.getString("appId"));
            rtcInfor.setChannel(jb_data.getString("channel"));
            rtcInfor.setUserid(jb_data.getString("user"));
            rtcInfor.setNonce(jb_data.getString("nonce"));
            rtcInfor.setTimestamp(jb_data.getInt("timestamp"));
            rtcInfor.setToken(jb_data.getString("token"));
            rtcInfor.setFrom(jb_data.getString("from"));
            rtcInfor.setTo(jb_data.getString("to"));
            Log.i("hcc","jb_data.=="+jb_data.getString("gslb"));
            rtcInfor.setGslb(new String[]{jb_data.getString("gslb")});


        } catch (JSONException e) {
            Log.i("hcc","e.=="+e.getMessage());
            e.printStackTrace();
        }
        return rtcInfor;
    }

    public static Boolean isEmuis(RTCInfor rtcInfor){
        Log.i("hcc","infor=="+rtcInfor.getGslb()+" "+rtcInfor.getToken()+"  "+rtcInfor.getTimestamp()+" "+rtcInfor.getNonce()+" "+rtcInfor.getUserid()+" "+rtcInfor.getChannel()+" "+rtcInfor.getUsername());
        if(rtcInfor.getGslb()==null||rtcInfor.getToken()==null||rtcInfor.getTimestamp()<0||rtcInfor.getNonce()==null||rtcInfor.getUserid()==null||rtcInfor.getChannel()==null||
                rtcInfor.getUsername()==null){
            return false;
        }
        return true;
    }
}
