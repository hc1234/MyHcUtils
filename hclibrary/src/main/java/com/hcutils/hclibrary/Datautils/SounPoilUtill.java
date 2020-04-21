package com.hcutils.hclibrary.Datautils;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

import com.hcutils.hclibrary.MyApppliction;
import com.hcutils.hclibrary.R;


/**
 * Created by Chao on 2017/12/4.
 */

public class SounPoilUtill {
    static SounPoilUtill sounPoilUtill;
    static SoundPool soundPool;
    static int comeid,outid,nocallid;
    static int playcomeid,playoutid,playnocall;


    public static SounPoilUtill Getinstanc(Context context){
        if(sounPoilUtill==null){
            sounPoilUtill=new SounPoilUtill();
            initrool(context);
        }
        return sounPoilUtill;
    }

    public static void initrool(Context context){
        soundPool=new SoundPool(10, AudioManager.STREAM_SYSTEM,0);
        comeid=soundPool.load(context, R.raw.comecome3,1);
        outid=soundPool.load(context,R.raw.em_outgoing,1);
        nocallid=soundPool.load(context,R.raw.nocall,1);


    }

    public void playrool(int i){
        if(i==1){
           playcomeid= soundPool.play(comeid,1, 1, 1, -1, 1);
        }else if(i==2){
           playoutid= soundPool.play(outid,1, 1, 1, -1, 1);
        }else if(i==3){
            playnocall=soundPool.play(nocallid,1,1,1,1,1);
        }

    }


    public void stop(){
        if(playcomeid>0){
            soundPool.stop(playcomeid);
        }
        if(playoutid>0){
            soundPool.stop(playoutid);
        }


    }
}
