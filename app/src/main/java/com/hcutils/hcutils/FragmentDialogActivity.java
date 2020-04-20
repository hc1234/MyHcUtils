package com.hcutils.hcutils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.hcutils.hclibrary.Chat.CallInfor;
import com.hcutils.hclibrary.Chat.VideoActivity;
import com.hcutils.hclibrary.Chat.VideoKSDHActivity;
import com.hcutils.hclibrary.Chat.VoiceActivity;
import com.hcutils.hclibrary.Chat.VoiceKSDHActiivty;
import com.hcutils.hclibrary.FragmentDialog.HCSetFragmentDialog;

public class FragmentDialogActivity extends AppCompatActivity {
    TextView showpop;
    TextView call,answer,call_ksdh,answer_ksdh,call_voice,call_voice_ksdh;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_layout);
        showpop= (TextView) findViewById(R.id.showpop);
        call=findViewById(R.id.call);
        answer=findViewById(R.id.answer);
        call_ksdh=findViewById(R.id.call_ksdh);
        call_voice=findViewById(R.id.call_voice);
        answer_ksdh=findViewById(R.id.answer_ksdh);
        call_voice_ksdh=findViewById(R.id.call_voice_ksdh);
        showpop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CallInfor callInfor=new CallInfor();
                callInfor.setFrom("1532848689{74f2d1f6-685c-4915-87bc-205a1034d5df}");
                callInfor.setTo("1568038565{f1418697-d38c-4241-ac2e-cbce59a5859e}");
                callInfor.setTo_name("陈斌眼");
                callInfor.setDevice("");
                callInfor.setRelkey("85738e77-53c6-4d06-a2c4-db63eb9dca6a");
                callInfor.setType("1"); //视频
                startActivity(new Intent(FragmentDialogActivity.this, VideoActivity.class).putExtra("infor",callInfor).putExtra("type","go"));
            }
        });

        call_voice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CallInfor callInfor=new CallInfor();
                callInfor.setFrom("1532848689{74f2d1f6-685c-4915-87bc-205a1034d5df}");
                callInfor.setTo("1568038565{f1418697-d38c-4241-ac2e-cbce59a5859e}");
                callInfor.setTo_name("陈斌眼");
                callInfor.setDevice("");
                callInfor.setRelkey("85738e77-53c6-4d06-a2c4-db63eb9dca6a");
                callInfor.setType("0"); //语音
                startActivity(new Intent(FragmentDialogActivity.this, VoiceActivity.class).putExtra("infor",callInfor).putExtra("type","go"));
            }
        });

        answer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CallInfor callInfor=new CallInfor();
                callInfor.setFrom("2699921761");
                callInfor.setTo("85288439187");
                callInfor.setFrom_name("陈斌眼");
                callInfor.setChannel("253227d5-d671-4443-8c1d-b5a51685f000");
                callInfor.setDevice(getAndroidId());
                callInfor.setType("1"); //视频
                startActivity(new Intent(FragmentDialogActivity.this, VideoActivity.class).putExtra("infor",callInfor).putExtra("type","come"));
            }
        });
        call_ksdh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CallInfor callInfor=new CallInfor();
                callInfor.setFrom("1532848689{74f2d1f6-685c-4915-87bc-205a1034d5df}");
                callInfor.setTo("1568038565{f1418697-d38c-4241-ac2e-cbce59a5859e}");
                callInfor.setTo_name("陈斌眼");
                callInfor.setDevice("qweqweqweqwe");
                callInfor.setRelkey("");
                callInfor.setType("1"); //视频
                startActivity(new Intent(FragmentDialogActivity.this, VideoKSDHActivity.class).putExtra("infor",callInfor).putExtra("type","go"));
            }
        });
        call_voice_ksdh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CallInfor callInfor=new CallInfor();
                callInfor.setFrom("1532848689{74f2d1f6-685c-4915-87bc-205a1034d5df}");
                callInfor.setTo("1568038565{f1418697-d38c-4241-ac2e-cbce59a5859e}");
                callInfor.setTo_name("陈斌眼");
                callInfor.setDevice("qweqweqweqwe");
                callInfor.setRelkey("");
                callInfor.setType("0"); //语音
                startActivity(new Intent(FragmentDialogActivity.this, VoiceKSDHActiivty.class).putExtra("infor",callInfor).putExtra("type","go"));
            }
        });


    }
    public String getAndroidId() {
        return Settings.System.getString(FragmentDialogActivity.this.getContentResolver(), Settings.System.ANDROID_ID);
    }
    public void showDialog(){
        Log.i("hcc"," hxx");
        HCSetFragmentDialog hcSetFragmentDialog=HCSetFragmentDialog.Getinstace(getSupportFragmentManager())
                .setgravity(Gravity.LEFT)
                .setheight((int)(getScreenHeight(this)))
                .setwith(300)
                .setlayout(R.layout.dialog_item)
                .getview(new HCSetFragmentDialog.Getview() {
                    @Override
                    public void getview(View view) {
                    }
                })
                .show();

    }
    /**
     * 获得屏幕高度
     *
     * @param context
     * @return
     */
    public  int getScreenHeight(Context context)
    {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }
}
