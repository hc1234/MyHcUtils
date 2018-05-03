package com.hcutils.hclibrary;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * Created by Chao on 2017/5/16.
 */

public class OkHttpNetWork {

   static private OkHttpNetWork Ins;



    public interface ReturnStr {
         void setStr(String str, String type, String message, String data);
    }


    public OkHttpNetWork() {
        this.Ins = this;
    }

    public static OkHttpNetWork getOkhttpNetWork(){
        if(Ins==null){
            Ins=new OkHttpNetWork();
        }
        return Ins;
    }


    /**
     *  OKhttp访问  设置访问超时为 60s
     * @param uri  访问地址
     * @param builder 组装的requestBoby 包含各种参数
     * @param returnstr 访问完成之后返回的值
     * @param context
     * @param isshowTishi 是否打开访问失败提示
     */

    public void OkhttpPost(final String uri, final RequestBody builder, final Context context, final Boolean isshowTishi, final ReturnStr returnstr) {

        final Handler hand = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                String str = (String) msg.obj;
                if (str.equals("exception")) {
                    if (isshowTishi){
                        Toast.makeText(context,"网络连接超时或网络异常",Toast.LENGTH_SHORT).show();
                    }
                    returnstr.setStr(str, "", "网络连接超时或网络异常", "");
                } else {

                    if ( str.equals("NetError")) {
                        if(isshowTishi ) {
                            Toast.makeText(context, "网络连接失败", Toast.LENGTH_SHORT).show();
                        }
                        returnstr.setStr(str, "", "网络连接失败", "");
                    }else {
                        try {
                            JSONObject jb = new JSONObject(str);
                            String type = jb.optString("type");
                            String message = jb.optString("message");
                            String data = jb.optString("data");
                            if (type == null) type = "";
                            if (message == null) message = "";
                            if (data == null) message = "";

                            returnstr.setStr(str, type, message, data);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            returnstr.setStr(str, "", "", "");
                        }
                    }
                    Ins = null;
                }
            }

        };

        new Thread(new Runnable() {
            @Override
            public void run() {
                String str = "";
                try {
                    OkHttpClient client = new OkHttpClient.Builder().connectTimeout(60, TimeUnit.SECONDS).readTimeout(60, TimeUnit.SECONDS).writeTimeout(60, TimeUnit.SECONDS).build();
//                    client.newBuilder().connectTimeout(60, TimeUnit.SECONDS).readTimeout(60,TimeUnit.SECONDS).writeTimeout(60,TimeUnit.SECONDS);
                    Request request = new Request.Builder().url(uri).post(builder).build();
                    Response response = client.newCall(request).execute();
                    String str1 = response.body().string();
                    str = str1;
                    if (!response.isSuccessful()) {
                        // throw new IOException("Unexpected code " + response);
                        str = "NetError";
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                    str = "exception";
                }
                Message msg = hand.obtainMessage(101, str);
                hand.sendMessage(msg);
            }
        }).start();

    }



}
