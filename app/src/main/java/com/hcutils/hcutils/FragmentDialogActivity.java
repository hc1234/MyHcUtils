package com.hcutils.hcutils;

import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.hcutils.hclibrary.FragmentDialog.HCSetFragmentDialog;

public class FragmentDialogActivity extends AppCompatActivity {
    TextView showpop;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_layout);
        showpop= (TextView) findViewById(R.id.showpop);
        showpop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

    }

    public void showDialog(){
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
