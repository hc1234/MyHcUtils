package com.hcutils.hcutils.Datautils;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;

import com.hcutils.hcutils.MyApppliction;
import com.hcutils.hcutils.R;

/**
 * Created by Chao on 2017/12/4.
 */

public class SounPoilUtill {
    static SounPoilUtill sounPoilUtill;
    static SoundPool soundPool;
    static int comeid,outid;
    static int playcomeid,playoutid;


    public static SounPoilUtill Getinstanc(){
        if(sounPoilUtill==null){
            sounPoilUtill=new SounPoilUtill();
            initrool();
        }
        return sounPoilUtill;
    }

    public static void initrool(){
        soundPool=new SoundPool(10, AudioManager.STREAM_SYSTEM,0);
        comeid=soundPool.load(MyApppliction.getAppContext(), R.raw.comecome3,1);
        outid=soundPool.load(MyApppliction.getAppContext(),R.raw.em_outgoing,1);


    }

    public void playrool(int i){
        if(i==1){
           playcomeid= soundPool.play(comeid,1, 1, 1, -1, 1);
        }else if(i==2){
           playoutid= soundPool.play(outid,1, 1, 1, -1, 1);
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
