package com.hcutils.hcutils.network;

import android.util.Log;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitMange {

    public static RetrofitMange retrofitMange;
    HttpApi httpApi;

    public static synchronized RetrofitMange Getinstance() {

        if (retrofitMange == null) {
            retrofitMange = new RetrofitMange();
        }
        return retrofitMange;
    }

    public HttpApi retrofit_Post() {

        if (httpApi == null) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                @Override
                public void log(String message) {
                    Log.d("hc_network", message);
                }
            }).setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient okClient = new OkHttpClient.Builder()
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .retryOnConnectionFailure(true)
                    .addInterceptor(new TokenIntercetor())
                    .addInterceptor(logging)
                    .build();

            Retrofit retrofit = new Retrofit.Builder().client(okClient)
                    .baseUrl(NetworkConstant.NewIp)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(NetworkConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            httpApi = retrofit.create(HttpApi.class);

        }

        return httpApi;


    }





}
