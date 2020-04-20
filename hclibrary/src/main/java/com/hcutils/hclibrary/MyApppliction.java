package com.hcutils.hclibrary;

import android.app.Application;
import android.content.Context;

import com.hcutils.hclibrary.Datautils.SounPoilUtill;

public class MyApppliction extends Application {

    static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        context=this.getApplicationContext();
//        SounPoilUtill.Getinstanc();
    }

    public static Context getAppContext() {
        return context;
    }

}
