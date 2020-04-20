package com.hcutils.hclibrary.Chat;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.hcutils.hclibrary.Datautils.PermissionUtils;
import com.hcutils.hclibrary.Utils.ThreadUtils;
import com.trello.rxlifecycle3.components.support.RxAppCompatActivity;

public class HcUtisBaseActivty extends RxAppCompatActivity {
    private PermissionUtils.PermissionGrant mGrant;
    /**
     * 请求权限
     */
    public void setUpSplash(PermissionUtils.PermissionGrant mGrant) {
        this.mGrant=mGrant;

        ThreadUtils.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                requestPermission();
            }
        }, 1000);
    }



    private void requestPermission() {
        PermissionUtils.requestMultiPermissions(this,
                new String[]{
                        PermissionUtils.PERMISSION_CAMERA,
                        PermissionUtils.PERMISSION_WRITE_EXTERNAL_STORAGE,
                        PermissionUtils.PERMISSION_RECORD_AUDIO,
                        PermissionUtils.PERMISSION_READ_EXTERNAL_STORAGE}, mGrant);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.i("hcc","权限结果"+requestCode);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PermissionUtils.CODE_MULTI_PERMISSION) {
            PermissionUtils.requestPermissionsResult(this, requestCode, permissions, grantResults, mGrant);
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == PermissionUtils.REQUEST_CODE_SETTING) {
//            new Handler().postDelayed(this::requestPermission, 500);
//        }
    }

    public void ToastUtis(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }

}
