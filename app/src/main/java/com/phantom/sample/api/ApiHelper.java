package com.phantom.sample.api;


import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


public class ApiHelper {

    public static <T> Observable.Transformer<T, T> handleResult() {
        return tObservable -> tObservable.flatMap((Func1<T, Observable<T>>) Observable::just).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}