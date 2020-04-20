package com.hcutils.hclibrary.network;


import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class TokenIntercetor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();
            builder.addHeader("token", NetworkConstant.TOKEN);
        return chain.proceed(builder.build());
    }
}
