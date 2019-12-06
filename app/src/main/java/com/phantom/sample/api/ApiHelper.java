package com.phantom.sample.api;


import android.util.Log;

import com.phantom.sample.cache.CacheManager;

import java.io.Serializable;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


public class ApiHelper {

    public static <T> Observable.Transformer<T, T> handleResult() {
        return tObservable -> tObservable.flatMap((Func1<T, Observable<T>>) Observable::just).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @SuppressWarnings("unchecked")
    public static <T> Observable<T> load(final String cacheKey, Observable<T> fromNetwork,
                                         final long expireTime, boolean forceRefresh) {
        Observable<T> fromCache = Observable.create((Observable.OnSubscribe<T>) subscriber -> {
            T cache = (T) CacheManager.readObject(cacheKey, expireTime);
            if (cache != null) {
                Log.i("ApiHelper", "result from cache:  " + cacheKey + " -> " + cache.toString());
                subscriber.onNext(cache);
            } else {
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        /*
         * 这里的fromNetwork不需要指定Schedule,在handleResult中已经变换了
         */
        fromNetwork = fromNetwork.map(result -> {
            CacheManager.saveObject((Serializable) result, cacheKey);
            return result;
        });
        if (forceRefresh) {
            return fromNetwork;
        } else {
            return Observable.concat(fromCache, fromNetwork).first();
        }

    }

}