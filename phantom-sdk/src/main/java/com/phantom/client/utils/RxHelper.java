package com.phantom.client.utils;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Description：用于切换线程
 */

public class RxHelper {

    public static void runOnUI(Runnable runnable) {
        Observable.just(true)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aBoolean -> runnable.run());
    }

    public static void runOnBackground(final Runnable runnable) {
        Observable.just(true)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(a -> runnable.run());
    }


}
