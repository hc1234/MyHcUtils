package com.hcutils.hclibrary.FragmentDialog;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.hcutils.hclibrary.R;

public class HC_FragmentDilog extends DialogFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view=inflater.inflate(getlayout(),container,false);
        bindview(view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        getDialog().setCanceledOnTouchOutside(getCanceledOutside());
        Window window=getDialog().getWindow();
        WindowManager.LayoutParams params=window.getAttributes();
        params.dimAmount=0.3f;

        if(getGravity()<0){
            params.gravity=Gravity.BOTTOM;
        }else{
            params.gravity=getGravity();
        }
        if(getHeight()<1){
            params.height=WindowManager.LayoutParams.WRAP_CONTENT;
        }else{
            params.height=getHeight();
        }
        if(getWith()<1){
            params.width=WindowManager.LayoutParams.MATCH_PARENT;
        }else {
            params.width = getWith();
        }
        window.setBackgroundDrawable(new ColorDrawable( 0x00000000));
        window.setAttributes(params);
        if(getGravity()==Gravity.LEFT){
            window.setWindowAnimations(R.style.left_right);
        }else if(getGravity()==Gravity.RIGHT){
            window.setWindowAnimations(R.style.right_left);
        }else if(getGravity()==Gravity.TOP){
            window.setWindowAnimations(R.style.shang_xia);
        }else if(getGravity()==Gravity.CENTER){

        } else {
            window.setWindowAnimations(R.style.xia_shang);
        }



    }

    public View bindview(View view){
        return view;
    }
    public  int getlayout(){
        return 0;
    }
    public Boolean getCanceledOutside(){
        return true;
    }
    public int getGravity(){
        return Gravity.BOTTOM;
    }
    public int getWith(){
        return WindowManager.LayoutParams.MATCH_PARENT;
    }
    public int getHeight(){
    return WindowManager.LayoutParams.WRAP_CONTENT;
    }
}
