package com.hcutils.hcutils.network;

import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

public class NetworkConverterFactory extends Converter.Factory {



    public static NetworkConverterFactory create() {
        return new NetworkConverterFactory();
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {

        return super.requestBodyConverter(type, parameterAnnotations, methodAnnotations, retrofit);
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        Type token = new TypeToken<String>(){}.getType();
        if (type == token) {
            return new StringConverter();
        }

        return super.responseBodyConverter(type, annotations, retrofit);
    }



    private class StringConverter implements Converter{

        @Override
        public Object convert(Object value) throws IOException {
            if (value != null) {
                if (value instanceof ResponseBody) {
                    ResponseBody rb = (ResponseBody) value;
                    String rbstr=rb.string();
                    try {
                        JSONObject jb = new JSONObject(rbstr);
                    int code = jb.optInt("code");
                        if(code== NetworkConstant.TOKEN_ERROR){
                            throw new TokenException();
                        }else if(code== NetworkConstant.TIME_OUT||code== NetworkConstant.TIME_OVER){
                            throw new TimeException();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    return rbstr;
                }
            }
            return null;
        }
    }


}
