package com.hcutils.hclibrary.network;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hcutils.hclibrary.Datautils.DataUtis;
import com.trello.rxlifecycle3.components.support.RxAppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class NetworkUtil {


    @SuppressLint("CheckResult")
    public static void func_post(final NetWorkBobyInfor netWorkBobyInfor, RxAppCompatActivity activity) {

        Observable
                .defer(new Callable<ObservableSource<?>>() {
                    @Override
                    public ObservableSource<?> call() throws Exception {
                        return RetrofitMange.Getinstance().retrofit_Post()
                                .post(netWorkBobyInfor.getIpaddress(), repase(netWorkBobyInfor.getParameters(), netWorkBobyInfor.getParameters_value(), netWorkBobyInfor.getAction()));
                    }
                })
                .subscribeOn(Schedulers.io())
                .retryWhen(new ThrowExtion())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .compose(activity.bindToLifecycle())
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {
                    }

                    @Override
                    public void onNext(Object str) {
                        if (str != null) {
                            try {
                                JSONObject jb = new JSONObject(str.toString());
                                int code = jb.optInt("code");
                                String message = jb.optString("message");

                                netWorkBobyInfor.getCallBack().setresult(code, str.toString());
                            } catch (JSONException e) {
                                e.printStackTrace();
                                netWorkBobyInfor.getCallBack().setresult(101, "json解析异常");
                            }
                        } else {
//                            setlogin_network("json解析异常");
                            netWorkBobyInfor.getCallBack().setresult(101, "json解析异常");
                        }


                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Log.e("hcc", "throwable==" + throwable);
                        netWorkBobyInfor.getCallBack().setresult(9, "网络异常，请检查网络连接");
                    }

                    @Override
                    public void onComplete() {
                    }
                });

    }

    @SuppressLint("CheckResult")
    public static void func_file(final NetWorkBobyInfor netWorkBobyInfor, List<File> listfile) {
        Observable
                .defer(new Callable<ObservableSource<?>>() {
                    @Override
                    public ObservableSource<?> call() throws Exception {
                        return RetrofitMange.Getinstance().retrofit_Post()
                                .put(netWorkBobyInfor.getIpaddress(), repaseFile(netWorkBobyInfor.getParameters(), netWorkBobyInfor.getParameters_value(), netWorkBobyInfor.getAction(), listfile));
                    }
                })
                .subscribeOn(Schedulers.io())
                .retryWhen(new ThrowExtion())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {
                    }

                    @Override
                    public void onNext(Object str) {
                        if (str != null) {
                            try {
                                JSONObject jb = new JSONObject(str.toString());
                                int code = jb.optInt("code");
                                String message = jb.optString("message");
//                                JSONObject data = jb.getJSONObject("data");
                                if (code == 0) {
                                    if (str != null) {
                                        netWorkBobyInfor.getCallBack().setresult(code, str.toString());
                                    } else {
                                        netWorkBobyInfor.getCallBack().setresult(code, "data==null");
                                    }

                                } else if (code == 1) {
                                    netWorkBobyInfor.getCallBack().setresult(code, str.toString());
                                } else {
                                    if (message == null) {
                                        netWorkBobyInfor.getCallBack().setresult(code, "message==null");
                                    } else {
                                        netWorkBobyInfor.getCallBack().setresult(code, str.toString());
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                netWorkBobyInfor.getCallBack().setresult(101, "json解析异常");
                            }
                        } else {
                            netWorkBobyInfor.getCallBack().setresult(101, "json解析异常");
                        }


                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Log.e("hcc", "throwable==" + throwable);
                        netWorkBobyInfor.getCallBack().setresult(9, "网络异常，请检查网络连接");
                    }

                    @Override
                    public void onComplete() {
                    }
                });

    }


    @SuppressLint("CheckResult")
    public static void func_get(final NetWorkBobyInfor netWorkBobyInfor) {
        Observable
                .defer(new Callable<ObservableSource<?>>() {
                    @Override
                    public ObservableSource<?> call() throws Exception {
                        return RetrofitMange.Getinstance().retrofit_Post()
                                .get(netWorkBobyInfor.getIpaddress());
                    }
                })
                .subscribeOn(Schedulers.io())
                .retryWhen(new ThrowExtion())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {
                    }

                    @Override
                    public void onNext(Object str) {

                        if (str != null) {
                            try {
                                JSONObject jb = new JSONObject(str.toString());
                                int code = jb.optInt("code");
                                String message = jb.optString("message");
                                JSONObject data = jb.getJSONObject("data");
                                if (code == 0) {
                                    Log.i("hcc", "get==" + str);
                                    if (data != null) {
                                        netWorkBobyInfor.getCallBack().setresult(code, data.toString());
                                    } else {
                                        netWorkBobyInfor.getCallBack().setresult(code, "data==null");
                                    }

                                } else if (code == 1) {
                                    netWorkBobyInfor.getCallBack().setresult(code, message);
//                                    setlogin_network(str.toString());
                                } else {
//                                    setlogin_network(str.toString());
                                    if (message == null) {
                                        netWorkBobyInfor.getCallBack().setresult(code, "message==null");
                                    } else {
                                        netWorkBobyInfor.getCallBack().setresult(code, message);
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                netWorkBobyInfor.getCallBack().setresult(101, "json解析异常");
                            }
                        } else {
                            netWorkBobyInfor.getCallBack().setresult(101, "json解析异常");
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Log.e("hcc", "throwable==" + throwable);
                        netWorkBobyInfor.getCallBack().setresult(9, "网络异常，请检查网络连接");
                    }

                    @Override
                    public void onComplete() {
                    }
                });

    }


    public static RequestBody repase(String[] s1, Object[] o1, int action) {
        long time = DataUtis.getStringToDate() / 1000;
        Log.i("hcc","time=="+time);
        RequestBody boby;
        Map<String, Object> mapall = new HashMap<>();
        Map<String, Object> map = new HashMap<>();
        String sianvalue = "";
        Map<String, Object> parsemap = null;
        if (s1 != null && o1 != null && s1.length == o1.length) {
            for (int i = 0; i < s1.length; i++) {
                map.put(s1[i], o1[i]);
            }
            parsemap = new Gson().fromJson(new Gson().toJson(map).toString(), new TypeToken<Map<String, Object>>() {
            }.getType());

            for (String key : parsemap.keySet()) {
                sianvalue = sianvalue + parsemap.get(key);
            }
        }
        mapall.put(NetworkConstant.Baseparameter[0], action);
        mapall.put(NetworkConstant.Baseparameter[1], time + "");
        mapall.put(NetworkConstant.Baseparameter[2], Getsin(time, sianvalue));
        mapall.put(NetworkConstant.Baseparameter[3], "1.0");
        if (parsemap == null) {
            parsemap = new HashMap<>();
        }
        mapall.put(NetworkConstant.Baseparameter[4], parsemap);
        boby = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), new Gson().toJson(mapall));
        return boby;
    }

    public static RequestBody repaseFile(String[] s1, Object[] o1, int action, List<File> listfile) {
        long time = DataUtis.getStringToDate() / 1000;
        RequestBody boby;
        Map<String, Object> mapall = new HashMap<>();
        Map<String, Object> map = new HashMap<>();
        String sianvalue = "";
        Map<String, Object> parsemap = null;
        if (s1 != null && o1 != null && s1.length == o1.length) {
            for (int i = 0; i < s1.length; i++) {
                map.put(s1[i], o1[i]);
            }
            parsemap = new Gson().fromJson(new Gson().toJson(map).toString(), new TypeToken<Map<String, Object>>() {
            }.getType());

            for (String key : parsemap.keySet()) {
                sianvalue = sianvalue + parsemap.get(key);
            }
        }
        mapall.put(NetworkConstant.Baseparameter[0], action);
        mapall.put(NetworkConstant.Baseparameter[1], time + "");
        mapall.put(NetworkConstant.Baseparameter[2], Getsin(time, sianvalue));
        mapall.put(NetworkConstant.Baseparameter[3], "1.0");
        if (parsemap == null) {
            parsemap = new HashMap<>();
        }
        mapall.put(NetworkConstant.Baseparameter[4], parsemap);
//        boby = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), new Gson().toJson(mapall));
        MultipartBody.Builder request2 = new MultipartBody.Builder().setType(MultipartBody.FORM);
        request2.addFormDataPart("json", new Gson().toJson(mapall));
        if (listfile != null && listfile.size() > 0) {
            for (int i = 0; i < listfile.size(); i++) {
                try {
                    request2.addFormDataPart("file", URLEncoder.encode(listfile.get(i).getName(), "UTF-8"), RequestBody.create(MediaType.parse("multipart/form-data"), listfile.get(i)));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }

        boby = request2.build();

        return boby;

    }


    private static String Getsin(Long time, String signvalue) {


        return md5((time + signvalue + "JHXKJHZN")).toUpperCase();
    }

    private static String md5(String string) {
        if (TextUtils.isEmpty(string)) {
            return "";
        }
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest(string.getBytes());
            String result = "";
            for (byte b : bytes) {
                String temp = Integer.toHexString(b & 0xff);
                if (temp.length() == 1) {
                    temp = "0" + temp;
                }
                result += temp;
            }
            return result;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

}
