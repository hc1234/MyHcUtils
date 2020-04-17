package com.hcutils.hcutils.network;


import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

public class ThrowExtion implements Function<Observable<? extends Throwable>, Observable<?>> {
    int isrel=0;
    @Override
    public Observable<?> apply(final Observable<? extends Throwable> observable) throws Exception {


        return observable.flatMap(new Function<Throwable, ObservableSource<?>>() {
            @Override
            public ObservableSource<?> apply(Throwable throwable) throws Exception {

             if(throwable instanceof TimeException){

                }

                return Observable.error(throwable);
            }
        });
    }
}
