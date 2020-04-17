package com.hcutils.hcutils.network;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface HttpApi {

    @POST("{id}")
    Observable<String> post(@Path("id") String id, @Body RequestBody body);
    @PUT("{id}")
    Observable<String> put(@Path("id") String id, @Body RequestBody body);
    @GET("{id}")
    Observable<String> get(@Path("id") String id);

}
