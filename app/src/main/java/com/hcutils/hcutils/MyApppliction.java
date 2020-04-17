package com.hcutils.hcutils;

import android.app.Application;
import android.content.Context;

import com.hcutils.hcutils.Datautils.SounPoilUtill;

public class MyApppliction extends Application {

    static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        context=this.getApplicationContext();
        SounPoilUtill.Getinstanc();
    }

    public static Context getAppContext() {
        return context;
    }

}
