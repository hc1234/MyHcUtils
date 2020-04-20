package com.hcutils.hclibrary.network;

import android.app.Activity;


public class NetWorkBobyInfor {


    String ipaddress;
    int action;
    String[] parameters;
    Object[] parameters_value;
    CallBack callBack;
    Activity ISshowLoading;
    Boolean ISshowDialog;
    String loadingmessage;
    String[] files;

    public String getIpaddress() {
        return ipaddress;
    }

    public void setIpaddress(String ipaddress) {
        this.ipaddress = ipaddress;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public String[] getParameters() {
        return parameters;
    }

    public void setParameters(String[] parameters) {
        this.parameters = parameters;
    }

    public Object[] getParameters_value() {
        return parameters_value;
    }

    public void setParameters_value(Object[] parameters_value) {
        this.parameters_value = parameters_value;
    }

    public CallBack getCallBack() {
        return callBack;
    }

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public Activity getISshowLoading() {
        return ISshowLoading;
    }

    public void setISshowLoading(Activity ISshowLoading) {
        this.ISshowLoading = ISshowLoading;
    }

    public Boolean getISshowDialog() {
        return ISshowDialog;
    }

    public void setISshowDialog(Boolean ISshowDialog) {
        this.ISshowDialog = ISshowDialog;
    }

    public String getLoadingmessage() {
        return loadingmessage;
    }

    public void setLoadingmessage(String loadingmessage) {
        this.loadingmessage = loadingmessage;
    }

    public String[] getFiles() {
        return files;
    }

    public void setFiles(String[] files) {
        this.files = files;
    }

    public interface CallBack{
        void setresult(int code, String data);
    }
}
